package sol.app.quinones.solappquinones.Controllers.Alumne;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
import sol.app.quinones.solappquinones.Models.Alumne;
import sol.app.quinones.solappquinones.Models.Model;
import sol.app.quinones.solappquinones.Models.Professor;
import sol.app.quinones.solappquinones.Models.VistaController;
import sol.app.quinones.solappquinones.Service.ConsultesSocket;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de la vista d'alumne
 *
 * Encarregada de gestionar les interacions de l'usuari amb la vista
 * Realitzar les operacions corresponents amb la infromació
 *
 * @author david
 */
public class AlumneController implements Initializable, ITopMenuDelegation {

    @FXML
    private TableColumn idNomAlumne, idCognomAlumne, idCognom2Alumne, idDataNeixAlumne, idNIFAlumne, idTelfAlumne, idMailAlumne;
    @FXML
    private TableView tableAlumne;
    @FXML
    private VBox vBoxMainAlumne;

    private ObservableList<Alumne> alumneArrayListTable = FXCollections.observableArrayList();

    /**
     * Inicilaitza el controlador configurant UI
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Obtenim el controlador i vista del menu superior, pasant el controlador de la clase com a paramtere
        VistaController<TopMenuController> vistaController = Model.getInstance().getViewFactory().getMenuTopViewr(this);
        //Extreiem la vista del menu superior
        HBox topMenuView = vistaController.getView();
        //Obtenim el controlador de la vista superior
        TopMenuController topMenuController = vistaController.getController();
        //Indiquem si s'ha de deshabilitar el boto superior de crear
        topMenuController.disableCrearBoto(false);
        //afeguime l menu suoperior a la vista principal
        vBoxMainAlumne.getChildren().add(0, topMenuView);

        //Load array for table
        loadAlumnes();

        //load array in table
        tableAlumne.setItems(alumneArrayListTable);

        //assign columns table
        assignarColumnesTable();

        //add action double click on grid table
        tableAlumne.setRowFactory(e -> {
            TableRow<Alumne> row = new TableRow<>();
            row.setOnMouseClicked(ev -> {
                //seleccio no es vuida, boto esquerra, dos clics ? --> entrem
                if(!row.isEmpty() && ev.getButton() == MouseButton.PRIMARY && ev.getClickCount() == 2) {
                    Alumne alumClicat = row.getItem();
                    editAlumne(alumClicat);
                }
            });

            return row;
        });



    }

    /**
     * Obre editor(view) amb les dades de un alumne seleccionat
     * @param alumClicat alumne seleccionat per editar
     */
    private void editAlumne(Alumne alumClicat) {
        Model.getInstance().getViewFactory().showWindowFormAlumne(" - Editar Alumne", alumClicat, this);
    }

    /**
     * Assignació de les columnes a la taula d'alumnes
     */
    private void assignarColumnesTable() {
        //Configurem la relació amb l'objecte del que ha de mostrar
        idNomAlumne.setCellValueFactory(new PropertyValueFactory<>("nom"));
        idCognomAlumne.setCellValueFactory(new PropertyValueFactory<>("cognom1"));
        idCognom2Alumne.setCellValueFactory(new PropertyValueFactory<>("cognom2"));
        idDataNeixAlumne.setCellValueFactory(new PropertyValueFactory<>("data_naixement"));
        idNIFAlumne.setCellValueFactory(new PropertyValueFactory<>("dni"));
        idTelfAlumne.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        idMailAlumne.setCellValueFactory(new PropertyValueFactory<>("mail"));
    }

    /**
     * Carrega la llista d'alumnes des del Servidor (realitza una crida "API)
     *
     */
    public void loadAlumnes() {
        //netejem array
        alumneArrayListTable.clear();
        //fem consula "API"
        String resposta = ConsultesSocket.serverPeticioConsulta("LLISTAR_ALUMNES");
        //llegim respota i tractem, afegim a l'array cada objecte que ens retorna
        if(resposta != null ) {
            try{
                JSONObject jsonObject = new JSONObject(resposta);

                if(jsonObject.getInt("codiResultat") != 0 ){
                    JSONArray arrayAlumnes = jsonObject.getJSONArray("dades");
                    for(int i = 1; i<arrayAlumnes.length(); i++){
                        alumneArrayListTable.add(Alumne.fromJson(arrayAlumnes.get(i).toString()));
                    }
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Acció en premer el boto crear
     * Obre editor per tal de crear un nou Alumne
     */
    @Override
    public void onBtnCrear() {

        Model.getInstance().getViewFactory().showWindowFormAlumne(" - Crear Alumne", null, this);

    }

    /**
     * Gestionar l'acció del boto editar
     * S'executa quan es prem el boto amb un alumne seleccionat de la tuala
     */
    @Override
    public void onBtnEditar() {
        Alumne alumneClicat = (Alumne) tableAlumne.getSelectionModel().getSelectedItem();
        if(alumneClicat != null){
            editAlumne(alumneClicat);
        }

    }

    /**
     * Gestiona l'accio del boto d'eliminar
     * En premer el boto, passa al metode l'alumne a elminar
     */
    @Override
    public void onBtnEliminar() {
        Alumne alumneClicat = (Alumne) tableAlumne.getSelectionModel().getSelectedItem();
        if(alumneClicat != null){
            deleteAlumne(alumneClicat);
        }
    }

    /**
     * Elimina (inactiva) l'alumne seleccionat a la llista
     *
     * @param alumneClicat alumne que es vol inactivar
     */
    private void deleteAlumne(Alumne alumneClicat) {
        UsuariController usuariController = new UsuariController();
        boolean borrat = usuariController.deleteUser(alumneClicat);

        if(borrat){
            ErrorController.showErrorAlert(
                    "DESACTIVAR USUARI",
                    "",
                    "Usuari inactivat",
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
