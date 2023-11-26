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

/**
 * Controlador per la vista de professor
 *
 * Encarregada de gestionar les interaccions de l'usuari amb la vista
 * Realitza operacions correcsponents amb la informaicó
 *
 * @author david
 */
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


    private ObservableList<Professor> professorArrayListTable = FXCollections.observableArrayList();

    /**
     * Inicialitza el controlador configurant UI
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Obtenim el controlador i la vista del menu superior, pasaem al gestor el controlador d'aquesta clase
        VistaController<TopMenuController> vistaController = Model.getInstance().getViewFactory().getMenuTopViewr(this);
        //Extraem la vista del menu sueperoir
        HBox topMenuView = vistaController.getView();
        //Obtenim el controlader de la vista superior
        TopMenuController topMenuController = vistaController.getController();
        //Indiquem si hem deshabilitar el bnoto de crear
        topMenuController.disableCrearBoto(false);
        //add menu superior a la main view
        vBoxMainProfessor.getChildren().add(0, topMenuView);
        //carregem els professors per la taula
        carregarProfessors();
        //load professors a la taual
        tableProfe.setItems(professorArrayListTable);
        //assign columns
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

        //Ajustar columnes
        tableProfe.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Obre editor amb dades del professor seleccionat
     * @param professor
     */
    public void editarProfessor(Professor professor){
        Model.getInstance().getViewFactory().showWindowFormProfessor(" - Editar Professor", professor, this);
    }

    /**
     * Assignació de les columnes de la taula professor
     * Configura la relació de l'objecte amb els camps a mostarr
     */
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

    /**
     * Carrega la llista de professor des del Servidor (realitza una crida "API)
     */
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

    /**
     *  Acció en premer el boto crear
     *  Obre editor per tal de crear un nou Professor
     */
    @Override
    public void onBtnCrear() {
        //levantar ventana:
        Model.getInstance().getViewFactory().showWindowFormProfessor(" - Crear Empleat", null, this);
    }

    /**
     * Gestionar l'acció del boto editar
     * S'executa quan es prem el boto amb un professor seleccionat de la tuala
     */
    @Override
    public void onBtnEditar() {
        Professor profesorSeleccionat = (Professor) tableProfe.getSelectionModel().getSelectedItem();
        if(profesorSeleccionat != null){
            editarProfessor(profesorSeleccionat);
        }

    }

    /**
     * Gestiona l'accio del boto d'eliminar
     * En premer el boto, passa al metode professor a elminar
     */
    @Override
    public void onBtnEliminar() {
        Professor profesorSeleccionat = (Professor) tableProfe.getSelectionModel().getSelectedItem();
        if(profesorSeleccionat != null) {
            deleteProfessor(profesorSeleccionat);
        }
    }

    /**
     * Elimina el professor seleccionat de la llista
     * @param p professor que es vol inactivar
     */
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
