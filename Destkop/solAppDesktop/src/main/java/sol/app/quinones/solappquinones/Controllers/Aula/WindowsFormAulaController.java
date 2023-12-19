package sol.app.quinones.solappquinones.Controllers.Aula;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sol.app.quinones.solappquinones.Controllers.ErrorController;
import sol.app.quinones.solappquinones.Models.Alumne;
import sol.app.quinones.solappquinones.Models.Aula;
import sol.app.quinones.solappquinones.Models.Peticio;
import sol.app.quinones.solappquinones.Models.Professor;
import sol.app.quinones.solappquinones.Service.ConsultesSocket;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador del formulari de l'aula
 * Gestiona la logica i UI per crear i editar aules
 *
 * @author david
 */
public class WindowsFormAulaController implements Initializable {

    @FXML
    private ListView<Alumne> listUsers;
    @FXML
    private Button idBtnAcceptar;
    @FXML
    private TextField idTxtFld1;
    @FXML
    private ComboBox idTxtFld2;

    private AulaController aulaController;
    private Aula aula;
    private Aula aulaCarregada;
    private Professor professorSeleccionat;
    private ObservableList<Alumne> observableList = callAlumnes();

    private List<Professor> professorsList = new ArrayList<>();

    private Peticio peticio = new Peticio();
    private ServerComunication socket = new ServerComunication();

    /**
     * Estableix el controlador de Aula
     * @param aulaController controllador de l'aula assignada
     */
    public void setAulaController (AulaController aulaController) {
        this.aulaController = aulaController;
    }


    /**
     * Metode que inizialitza elk controlador, i configura UI
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idBtnAcceptar.setOnAction(e -> saveObject());

        /* CONFIGURACIO DEL COMBOX PROFESSOR */
        //carrgem els professors
        loadTeacher();

        idTxtFld2.setCellFactory(cf -> new ListCell<Professor>(){
            @Override
            protected void updateItem(Professor item, boolean empty){
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNom());
            }
        });

        idTxtFld2.setConverter(new StringConverter<Professor>() {
            @Override
            public String toString(Professor professor) {
                return professor != null ? professor.getNom() : "";
            }
            @Override
            public Professor fromString(String s) {
                return null;
            }
        });

        //add to comboBox
        idTxtFld2.getItems().addAll(professorsList);

        //ListenerCanvisProfessor
        idTxtFld2.valueProperty().addListener((obs, oldValue, newValue) -> {
            professorSeleccionat = (Professor) newValue;
        });

        //listener deselccionar en cliar
        idTxtFld2.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            Professor seleccionat = (Professor) idTxtFld2.getSelectionModel().getSelectedItem();

            if(seleccionat != null) {
                idTxtFld2.getSelectionModel().clearSelection();
                e.consume();
            }
        });



        /*CONFGURE ALUMNES TO LIST*/

        listUsers.setItems(observableList);
        listUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //definit cell factory per veure objectes
        listUsers.setCellFactory(e -> new ListCell<Alumne>() {
            @Override
            protected void updateItem(Alumne alumne, boolean empty){
                super.updateItem(alumne, empty);
                setText(empty ? "" : alumne.getNom().toUpperCase() + " " + alumne.getCognom1().toUpperCase());
            }
        });
    }

    /**
     * Metode per carregar al comboBox els professors
     * Només carregaran els professors que son actius, els inactius no
     */
    private void loadTeacher() {
        try{
            JSONObject obj = new JSONObject(ConsultesSocket.serverPeticioConsulta("LLISTAR_EMPLEATS"));
            JSONArray listArray = obj.getJSONArray("dades");
            for(int i = 1; i< listArray.length(); i++){
                // revisem si es actiu, sino es actiu no l'inserim a la llista
                Professor professor = Professor.fromJson(listArray.get(i).toString());
                if(professor.isActiu()) professorsList.add(professor);
                //professorsList.add(Professor.fromJson(listArray.get(i).toString()));
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Metode per obtenir els usuaris seleccionas al list
     */
    @FXML
    private void handleAddStudentToAula(){
        ObservableList<Alumne> selectedStudent = listUsers.getSelectionModel().getSelectedItems();
        aula.setAlumnes(selectedStudent);
    }

    /**
     *Metode per obtenir els alumnes de la base de dades,
     * només inserirem i retornem els alumnes que son actius, els inactius els ignorarem
     * per tal de nose mostrats a la llista
     *
     * @return List alumnes actius
     */
    private ObservableList<Alumne> callAlumnes() {

        ObservableList<Alumne> alumnes = FXCollections.observableArrayList();

        try{
        JSONObject obj = new JSONObject(ConsultesSocket.serverPeticioConsulta("LLISTAR_ALUMNES"));
        JSONArray listArray = obj.getJSONArray("dades");
        for (int i = 1 ; i<listArray.length(); i++){
            // revisem si es actiu, sino es actiu no l'inserim a la llista
            Alumne alumne = Alumne.fromJson(listArray.get(i).toString());

            if (alumne.getIsActiu()) alumnes.add(alumne);
            //alumnes.add(Alumne.fromJson(listArray.get(i).toString()));
        }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return alumnes;
    }

    /**
     * Metode que guarda l'objecte aula creat / modificat
     * Gestiona la crida al servidor
     */
    private void saveObject() {
        String tipusPeticio;

        this.aula = new Aula(
          //idAula
                idBtnAcceptar.getText().equalsIgnoreCase("Crear") ? 0 : aulaCarregada.getId(),
          //nom Aula
                idTxtFld1.getText(),
          //Objecte Professor
                 professorSeleccionat

          // List alumnes
        );

        //call to add selected user at aula
        handleAddStudentToAula();

        if(!idBtnAcceptar.getText().equalsIgnoreCase("modificar")){
            tipusPeticio = "ALTA_AULA";
        }else{
            tipusPeticio = "MODIFICAR_AULA";
        }
        
        //crida al server amb un metode apart
        boolean guardat = guardarAulaDb(tipusPeticio);
        
        if(guardat){
            tancarFinestra();
        }else{
            ErrorController.showErrorAlert(
                    "ERROR AL TRACTAR AULA",
                    tipusPeticio.equalsIgnoreCase("ALTA_AULA")
                            ? " No s'ha pogut guardar l'aula"
                            : " No s'ha pogut modificar l'aula",
                    "",
                    Alert.AlertType.INFORMATION
            );
        }
    }

    /**
     * Tancar finestra actual i recarregar llistat d'aules al controaldor
     */
    private void tancarFinestra() {
        Stage stage = (Stage) idTxtFld1.getScene().getWindow();
        stage.close();
        aulaController.carregarAules();
    }

    /**
     * Metode per fer la crida al servidor per guardar o modifcar un objecte aula
     * @param tipusPeticio tipus peticio
     * @return si es exitos o no
     */
    private boolean guardarAulaDb(String tipusPeticio) {
        
        peticio.dropDades();
        
        try {
            socket.connect();
            peticio.setPeticio(tipusPeticio);
            peticio.addDades(SingletonConnection.getInstance().getKey());
            peticio.addDades(JsonUtil.toJson(aula));

            //System.out.println(JsonUtil.toJson(aula));
            String resposta = socket.sendMessage(JsonUtil.toJson(peticio));

            JSONObject jO = new JSONObject(resposta);
            if(jO.getInt("codiResultat") != 0 ){
                return true;
            }else{
                return false;
            }
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Metode per carrgar a la finesta l'aulta seleccionada per editar
     * també recorre la llista per tal de seleccionar.los
     * @param aula aula seleccionada
     */
    public void loadAula(Aula aula) {

        idTxtFld1.setText(aula.getNomAula());
        idTxtFld2.setValue(aula.getEmpleat());

        //seleccionem cada alumne de la llista que te l'aula assignats
        for(Alumne alumne : aula.getAlumnes()){
            listUsers.getSelectionModel().select(alumne);
        }

        this.aulaCarregada = aula;
        idBtnAcceptar.setText("Modificar");
    }
}
