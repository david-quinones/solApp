package sol.app.quinones.solappquinones.Controllers.Aula;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sol.app.quinones.solappquinones.Controllers.ErrorController;
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

public class WindowsFormAulaController implements Initializable {

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

    }

    private void loadTeacher() {
        try{
            JSONObject obj = new JSONObject(ConsultesSocket.serverPeticioConsulta("LLISTAR_EMPLEATS"));
            JSONArray listArray = obj.getJSONArray("dades");
            for(int i = 1; i< listArray.length(); i++){
                //System.out.println(Professor.fromJson(listArray.get(i).toString()));
                professorsList.add(Professor.fromJson(listArray.get(i).toString()));
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


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
            
            String resposta = socket.sendMessage(JsonUtil.toJson(peticio));

            //System.out.println(JsonUtil.toJson(peticio));

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

    public void loadAula(Aula aula) {
        idTxtFld1.setText(aula.getNomAula());
        idTxtFld2.setValue(aula.getEmpleat());

        this.aulaCarregada = aula;
        idBtnAcceptar.setText("Modificar");
    }
}
