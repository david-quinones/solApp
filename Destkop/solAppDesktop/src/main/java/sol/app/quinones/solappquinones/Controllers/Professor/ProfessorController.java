package sol.app.quinones.solappquinones.Controllers.Professor;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sol.app.quinones.solappquinones.Controllers.ErrorController;
import sol.app.quinones.solappquinones.Controllers.ITopMenuDelegation;
import sol.app.quinones.solappquinones.Controllers.TopMenuController;
import sol.app.quinones.solappquinones.Controllers.Usuari.UsuariController;
import sol.app.quinones.solappquinones.Models.Model;
import sol.app.quinones.solappquinones.Models.Peticio;
import sol.app.quinones.solappquinones.Models.Professor;
import sol.app.quinones.solappquinones.Models.VistaController;
import sol.app.quinones.solappquinones.Service.ConsultesSocket;
import sol.app.quinones.solappquinones.Service.ServerComunication;

import java.net.URL;
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

    private ObservableList<Professor> professorArrayListTable = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        VistaController<TopMenuController> vistaController = Model.getInstance().getViewFactory().getMenuTopViewr(this);
        HBox topMenuView = vistaController.getView();
        TopMenuController topMenuController = vistaController.getController();
        topMenuController.disableCrearBoto(false);
        vBoxMainProfessor.getChildren().add(0, topMenuView);

        //vBoxMainProfessor.getChildren().add(0, Model.getInstance().getViewFactory().getMenuTopViewr(this));
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
        Model.getInstance().getViewFactory().showWindowFormProfessor(" - Editar Professor", professor, this);
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

        professorArrayListTable.clear();

        String resposta = ConsultesSocket.serverPeticioConsulta("LLISTAR_EMPLEATS");
        if(resposta != null){
            try {

                JSONObject jsonObject = new JSONObject(resposta);
                if(jsonObject.getInt("codiResultat") != 0){
                    JSONArray arrayProfessors = jsonObject.getJSONArray("dades");
                    for(int i = 1; i < jsonObject.getJSONArray("dades").length(); i++){
                        professorArrayListTable.add(Professor.fromJson(arrayProfessors.get(i).toString()));
                    }

                    //TODO Delete

                    for(Professor p : professorArrayListTable) {
                        //System.out.println(p.getIdEmpleat());
                        //System.out.println(p.getIdPersona());
                    }

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
        Model.getInstance().getViewFactory().showWindowFormProfessor(" - Crear Empleat", null, this);
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
        Professor profesorSeleccionat = (Professor) tableProfe.getSelectionModel().getSelectedItem();
        if(profesorSeleccionat != null) {
            deleteProfessor(profesorSeleccionat);
        }
    }

    public void crearProfessor(Professor p){
        professorArrayListTable.add(p);
    }

    private void deleteProfessor(Professor p){
        UsuariController usuariController = new UsuariController();
        boolean borrat = usuariController.deleteUser(p);

        if(borrat){
            ErrorController.showErrorAlert(
                    "DESACTIVAR USUARI",
                    "",
                    "Usuari inactiu",
                    Alert.AlertType.INFORMATION
            );
        }else{
            ErrorController.showErrorAlert(
                    "DESACTIVAR USUARI",
                    "",
                    "Error al inactivar usauri",
                    Alert.AlertType.ERROR
            );
        }
    }

}
