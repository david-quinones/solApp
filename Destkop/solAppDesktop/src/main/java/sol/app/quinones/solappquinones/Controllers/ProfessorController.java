package sol.app.quinones.solappquinones.Controllers;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sol.app.quinones.solappquinones.Models.Model;
import sol.app.quinones.solappquinones.Models.Persona;
import sol.app.quinones.solappquinones.Models.Peticio;
import sol.app.quinones.solappquinones.Models.Professor;
import sol.app.quinones.solappquinones.Service.ConsultesSocket;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProfessorController implements Initializable, ITopMenuDelegation {

    @FXML
    private TableColumn idNIFProfe;
    @FXML
    private TableColumn idTelfProfe;
    @FXML
    private TableColumn idMailProfe;
    @FXML
    private TableColumn idDataNeixProfe;
    @FXML
    private TableColumn idIniciContrcateProfe;
    @FXML
    private VBox vBoxMainProfessor;
    @FXML
    private TableView tableProfe;
    @FXML
    private TableColumn idNomProfe;
    @FXML
    private TableColumn idCognomProfe;
    @FXML
    private TableColumn idCognom2Profe;
    @FXML
    private TableColumn idFiContrcateProfe;
    @FXML
    private AnchorPane mainProfessor;


    private Peticio peticio = new Peticio();
    private ServerComunication socket = new ServerComunication();

    private ArrayList<Professor> professorArrayList = new ArrayList<>();

    private ObservableList<Professor> professorArrayListTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vBoxMainProfessor.getChildren().add(0, Model.getInstance().getViewFactory().getMenuTopViewr(this));
        carregarProfessors();
        tableProfe.setItems(professorArrayListTable);
        assignarColumnesTaula();


        //Detectar doble click taula --> inicialitzar
        //enviar a editar
        tableProfe.setRowFactory(e -> {
            TableRow<Professor> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                   Professor profeClicat = row.getItem();
                   editarProfessor(profeClicat);
                }
            });
            return row;
        });
    }

    public void editarProfessor(Professor professor){
        Model.getInstance().getViewFactory().showWindowForm(" - Editar Professor", professor);
    }

    private void assignarColumnesTaula(){
        idNomProfe.setCellValueFactory(new PropertyValueFactory<>("nom"));
        idCognomProfe.setCellValueFactory(new PropertyValueFactory<>("cognom1"));
        idCognom2Profe.setCellValueFactory(new PropertyValueFactory<>("cognom2"));
        idDataNeixProfe.setCellValueFactory(new PropertyValueFactory<>("data_naixement"));
        idNIFProfe.setCellValueFactory(new PropertyValueFactory<>("dni"));
        idTelfProfe.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        idMailProfe.setCellValueFactory(new PropertyValueFactory<>("mail"));
        idIniciContrcateProfe.setCellValueFactory(new PropertyValueFactory<>("iniciContracte"));
        idFiContrcateProfe.setCellValueFactory(new PropertyValueFactory<>("finalContracte"));
    }

    public void carregarProfessors() {

        String resposta = ConsultesSocket.serverPeticioConsulta("LLISTAR_EMPLEATS");
        if(resposta != null){
            try {

                JSONObject jsonObject = new JSONObject(resposta);
                if(jsonObject.getInt("codiResultat") != 0){
                    JSONArray arrayProfessors = jsonObject.getJSONArray("dades");
                    for(int i = 1; i < jsonObject.getJSONArray("dades").length(); i++){
                        professorArrayList.add(Professor.fromJson(arrayProfessors.get(i).toString()));
                    }

                    //TODO Delete
                    StringBuilder stringBuilder = new StringBuilder();
                    for(Professor p : professorArrayList) {
                        stringBuilder.append(p.getNom());
                        stringBuilder.append(", ");
                        //System.out.println(p.getIniciContracte());
                        //System.out.println(p.getFinalContracte());
                    }

                    professorArrayListTable = FXCollections.observableArrayList(professorArrayList);

                    //idTextMostra.setText(stringBuilder.toString());




                }else{
                    //lanzar excpt
                    return;
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }else{
            //leno error
        }


    }

    @Override
    public void onBtnCrear() {
        //levantar ventana:
        Model.getInstance().getViewFactory().showWindowForm(" - Crear Empleat", null);



        //capturar respuesta
        //actualizar vista

        // test de màs (cargar de nuevo vista, hay el elemento creado?)
    }

    @Override
    public void onBtnEditar() {
        Professor profesorSeleccionat = (Professor) tableProfe.getSelectionModel().getSelectedItem();
        if(profesorSeleccionat != null){
            editarProfessor(profesorSeleccionat);
        }

    }

    @Override
    public void onBtnEliminar() {
        //action delete perfil
    }

    public void crearProfessor(Professor p){
        //actualitzara vista amb el professor creat
    }

}
