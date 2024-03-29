package sol.app.quinones.solappquinones.Controllers.Usuari;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;
import sol.app.quinones.solappquinones.Controllers.ITopMenuDelegation;
import sol.app.quinones.solappquinones.Controllers.TopMenuController;
import sol.app.quinones.solappquinones.Models.*;
import sol.app.quinones.solappquinones.Service.ConsultesSocket;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador per la gesitó s'usuaris
 *
 * Realitza les operacions corresponents amb lea informació
 *
 * @author david
 */
public class UsuariController implements Initializable, ITopMenuDelegation {

    @FXML
    private TableColumn idNomUser, idIsAdmin, idIsTeacher, idIsActive;
    @FXML
    private VBox vBoxMainUsuaris;
    @FXML
    private TableView tableUsers;

    private Peticio peticio = new Peticio();
    private ServerComunication socket = new ServerComunication();
    private ObservableList<Usuari> usersArrayListTable = FXCollections.observableArrayList();

    /**
     * Inicilaiza el controlador configrant UI
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Obtenim controlador i vista menu superior i li passem el controlador
        VistaController<TopMenuController> vistaController = Model.getInstance().getViewFactory().getMenuTopViewr(this);
        //extreiem la vista del menu superior
        HBox topMenuView = vistaController.getView();
        //obtenim controlador
        TopMenuController topMenuController = vistaController.getController();
        //deshabiltem boto crear
        topMenuController.disableCrearBoto(true);
        //add vista principal menu superior
        vBoxMainUsuaris.getChildren().add(0, topMenuView);
        //carregem usuaris a array
        carregarUsuaris();
        //assigem a la taula
        tableUsers.setItems(usersArrayListTable);
        //establim relació taula -> columna -> object usuer
        assignarColumnesTaula();


        //Detectar doble click taula --> inicialitzar
        //enviar a editar
        tableUsers.setRowFactory(e -> {
            TableRow<Usuari> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Usuari userClicat = row.getItem();
                    editarUsuari(userClicat);
                }
            });
            return row;
        });

        //ajustar columnes
        tableUsers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    /**
     * Edita usuari seleccionat
     * Obre finestra amb l'info de l'usuari seleccionat
     * @param u Usuari seleccionat per editar
     */
    private void editarUsuari(Usuari u) {
        Model.getInstance().getViewFactory().showWindowFormUser(" - Editar Usuari", u, this);
    }

    /**
     * Assigna les columnes a la tauala
     * Mapeja la relació columnes -> atributs objecte
     */
    private void assignarColumnesTaula() {
        idNomUser.setCellValueFactory(new PropertyValueFactory<>("nomUsuari"));
        idIsAdmin.setCellValueFactory(new PropertyValueFactory<>("isAdmin"));
        idIsTeacher.setCellValueFactory(new PropertyValueFactory<>("isTeacher"));
        idIsActive.setCellValueFactory(new PropertyValueFactory<>("isActive"));

        //assignar checkbox segons la info
        idIsAdmin.setCellFactory(col -> new TableCell<Usuari, Boolean>() {
            private final CheckBox checkBox = new CheckBox();
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null){
                    setGraphic(null);
                } else {
                    setGraphic(checkBox);
                    checkBox.setSelected(item);
                    setAlignment(Pos.CENTER);
                }
                checkBox.setDisable(true);
            }
        });

        idIsTeacher.setCellFactory(col -> new TableCell<Usuari, Boolean>() {
            private final CheckBox checkBox = new CheckBox();
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null){
                    setGraphic(null);
                } else {
                    setGraphic(checkBox);
                    checkBox.setSelected(item);
                    setAlignment(Pos.CENTER);
                }
                checkBox.setDisable(true);
            }
        });

        idIsActive.setCellFactory(col -> new TableCell<Usuari, Boolean>() {
            private final CheckBox checkBox = new CheckBox();
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null){
                    setGraphic(null);
                } else {
                    setGraphic(checkBox);
                    checkBox.setSelected(item);
                    setAlignment(Pos.CENTER);
                }
                checkBox.setDisable(true);
            }
        });

    }


    /**
     * Carrega els usuaris fent la crida al server i mostra taula,
     */
    public void carregarUsuaris() {
        //netegem llista
        usersArrayListTable.clear();

        //fer la peticio
        String resposta = ConsultesSocket.serverPeticioConsulta("LLISTAR_USUARIS");

        //controlar la resposta
        if(resposta != null){
            try {
                //pasem respota a JSON
                JSONObject jsonObject = new JSONObject(resposta);

                //comprovem el codi resultat de la resposta
                if(jsonObject.getInt("codiResultat") != 0){
                    //si es ok, recorrem l'array creant objecte del tipus usuari i passant al arrayList de la taula
                    JSONArray arrayUsers = jsonObject.getJSONArray("dades");

                    for(int i = 1; i < arrayUsers.length(); i ++){
                        usersArrayListTable.add(Usuari.fromJson(arrayUsers.get(i).toString()));
                    }

                } else{
                    //TODO
                }

            }catch(Exception e) {

            }
        }

    }

    /**
     * desus, acció crear
     */
    @Override
    public void onBtnCrear() {

    }

    /**
     * Acció a executar el clicar sobre editar
     * Obte l'usuari seleccionat de la tuala i crida ediarUsuari
     */
    @Override
    public void onBtnEditar() {
        Usuari userSeleccionat = (Usuari) tableUsers.getSelectionModel().getSelectedItem();
        if(userSeleccionat != null){
            editarUsuari(userSeleccionat);
        }
    }

    /**
     * No implementada (funcioal)
     * Acció que executa el cliar el boto d'eliminar
     */
    @Override
    public void onBtnEliminar() {
        Usuari userSeleccionat = (Usuari) tableUsers.getSelectionModel().getSelectedItem();
        if(userSeleccionat != null) {
            //deleteUser(userSeleccionat);
            //TODO quan hi hagi cerques
        }

    }

    /**
     * Elimina (inactiva) usuari seleccionat
     * Realitza la logica necessaria per eliminar un usuari del sistema (inactivar)
     * @param persona Persona a eliminar (convertida a usuari)
     * @return true si ha estat exitos
     */
    public boolean deleteUser(Persona persona) {

        peticio.dropDades();

        try {
            socket.connect();
            peticio.setPeticio("ELIMINAR_USUARI");
            peticio.addDades(SingletonConnection.getInstance().getKey());
            //add object
            peticio.addDades(JsonUtil.toJson(persona));

            //System.out.println(JsonUtil.toJson(peticio));
            String resposta = socket.sendMessage(JsonUtil.toJson(peticio));
            //si es 1 return true
            // else false

        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
