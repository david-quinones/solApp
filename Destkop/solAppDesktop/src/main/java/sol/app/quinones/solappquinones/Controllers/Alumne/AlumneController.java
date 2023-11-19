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

public class AlumneController implements Initializable, ITopMenuDelegation {

    @FXML
    private TableColumn idNomAlumne, idCognomAlumne, idCognom2Alumne, idDataNeixAlumne, idNIFAlumne, idTelfAlumne, idMailAlumne;
    @FXML
    private TableView tableAlumne;
    @FXML
    private VBox vBoxMainAlumne;

    private ObservableList<Alumne> alumneArrayListTable = FXCollections.observableArrayList();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        VistaController<TopMenuController> vistaController = Model.getInstance().getViewFactory().getMenuTopViewr(this);
        HBox topMenuView = vistaController.getView();
        TopMenuController topMenuController = vistaController.getController();
        topMenuController.disableCrearBoto(false);
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
                if(!row.isEmpty() && ev.getButton() == MouseButton.PRIMARY && ev.getClickCount() == 2) {
                    Alumne alumClicat = row.getItem();
                    editAlumne(alumClicat);
                }
            });

            return row;
        });



    }

    private void editAlumne(Alumne alumClicat) {
        Model.getInstance().getViewFactory().showWindowFormAlumne(" - Editar Alumne", alumClicat, this);
    }

    private void assignarColumnesTable() {
        idNomAlumne.setCellValueFactory(new PropertyValueFactory<>("nom"));
        idCognomAlumne.setCellValueFactory(new PropertyValueFactory<>("cognom1"));
        idCognom2Alumne.setCellValueFactory(new PropertyValueFactory<>("cognom2"));
        idDataNeixAlumne.setCellValueFactory(new PropertyValueFactory<>("data_naixement"));
        idNIFAlumne.setCellValueFactory(new PropertyValueFactory<>("dni"));
        idTelfAlumne.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        idMailAlumne.setCellValueFactory(new PropertyValueFactory<>("mail"));
    }

    public void loadAlumnes() {

        alumneArrayListTable.clear();

        String resposta = ConsultesSocket.serverPeticioConsulta("LLISTAR_ALUMNES");

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

    @Override
    public void onBtnCrear() {

        Model.getInstance().getViewFactory().showWindowFormAlumne(" - Crear Alumne", null, this);

    }

    @Override
    public void onBtnEditar() {
        Alumne alumneClicat = (Alumne) tableAlumne.getSelectionModel().getSelectedItem();
        if(alumneClicat != null){
            editAlumne(alumneClicat);
        }

    }

    @Override
    public void onBtnEliminar() {
        Alumne alumneClicat = (Alumne) tableAlumne.getSelectionModel().getSelectedItem();
        if(alumneClicat != null){
            deleteAlumne(alumneClicat);
        }
    }

    private void deleteAlumne(Alumne alumneClicat) {
        UsuariController usuariController = new UsuariController();
        boolean borrat = usuariController.deleteUser(alumneClicat);

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
