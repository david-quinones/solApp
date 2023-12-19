package sol.app.quinones.solappquinones.Controllers.Aula;

import javafx.beans.property.SimpleStringProperty;
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
import sol.app.quinones.solappquinones.Models.*;
import sol.app.quinones.solappquinones.Service.ConsultesSocket;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador de la vista de l'aula
 *
 * Encarregada de gestoinar les interacions de l'usuari amb la vista
 * Realitza les coperacions correponents amb l'informació de l'aula
 * i la gestió dels professors i alumnes assignat
 *
 * @author david
 */
public class AulaController implements Initializable, ITopMenuDelegation {

    @FXML
    private VBox vBoxMainAula;
    @FXML
    private TableView<Aula> tableAula;
    @FXML
    private TableColumn<Aula, String> idNomAula, idProfessor, idNumAlumns;
    @FXML
    private AnchorPane mainAula;

    private ObservableList<Aula> aulaArrayList = FXCollections.observableArrayList();

    /**
     * Inicialitza el controlador configurant la UI
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        VistaController<TopMenuController> vistaController = Model.getInstance().getViewFactory().getMenuTopViewr(this);
        HBox topMenuView = vistaController.getView();
        TopMenuController topMenuController = vistaController.getController();
        topMenuController.disableCrearBoto(false);
        vBoxMainAula.getChildren().add(0, topMenuView);

        //asjutar columnes
        tableAula.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //carrega Aules
        carregarAules();

        //carrega array to table
        tableAula.setItems(aulaArrayList);

        // assign columns to table
        assignarColumnesTaula();

        //addlistener double click on row
        tableAula.setRowFactory(e -> {
            TableRow<Aula> row = new TableRow<>();
            row.setOnMouseClicked( ev -> {
                if(!row.isEmpty() && ev.getButton() == MouseButton.PRIMARY && ev.getClickCount() == 2){
                    Aula aulaClidacada =  row.getItem();
                    editarAula(aulaClidacada);
                }
            });
            return row;
        });

    }

    /**
     * metode per editar un aula
     * Obre la finestra d'editar amb la informació de l'aula pasada per parametre
     * @param aulaClidacada
     */
    private void editarAula(Aula aulaClidacada) {
        //obrir finestra pasant objecte
        Model.getInstance().getViewFactory().showWindowFormAula(" - Editar Aula", aulaClidacada, this);
    }

    /**
     * Assignació de les columnes a la taula d'aula
     */
    private void assignarColumnesTaula() {
        idNomAula.setCellValueFactory(new PropertyValueFactory<>("nomAula"));
        //tractar la columna del nom del professor, ja que s un objecte i s'ha d'extreure la info
        idProfessor.setCellValueFactory(cellObject -> {
            Aula aula = cellObject.getValue();
            return new SimpleStringProperty(aula.getEmpleat() != null ? aula.getEmpleat().getNom().toUpperCase() + " " + aula.getEmpleat().getCognom1().toUpperCase() : "");
        });
        //num alumnes aula
        idNumAlumns.setCellValueFactory(cell -> {
            Aula aula = cell.getValue();
            return new SimpleStringProperty(aula.getAlumnes() != null ? String.valueOf(aula.getAlumnes().size()) : "");
        });

    }

    /**
     * Carrega la llista d'aulas des del servidor fent crida a l'API
     */
    public void carregarAules() {
        //clean array
        aulaArrayList.clear();

        //fem consulta a l'API
        String resposta = ConsultesSocket.serverPeticioConsulta("LLISTAR_AULES");

        //llegim respota i tractem
        if(resposta != null){
            try {
                JSONObject jO = new JSONObject(resposta);

                if(jO.getInt("codiResultat") != 0){
                    JSONArray arrayAules = jO.getJSONArray("dades");

                    for(int i = 1; i < arrayAules.length(); i++){
                        aulaArrayList.add(Aula.fromJson(arrayAules.get(i).toString()));
                    }

                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }


    }

    /**
     * Acció en premer el boto de brear
     * Obre finestra per crear una nova aula i li pasem el controlador de l'aUala
     */
    @Override
    public void onBtnCrear() {
        Model.getInstance().getViewFactory().showWindowFormAula(" - Crear Aula", null, this);
    }

    /**
     * Acció al premer el boto d'editar
     * Capturem la linia seleccionada  i li passem
     */
    @Override
    public void onBtnEditar() {
        Aula aulaClic = (Aula) tableAula.getSelectionModel().getSelectedItem();
        if(aulaClic != null){
            editarAula(aulaClic);
        }
    }

    /**
     * Gestiona l'acció del boto eliminar
     * en premer el boto, pasa al metode d'eliminar l'objecte clicat
     */
    @Override
    public void onBtnEliminar() {
        Aula aulaClidad = (Aula) tableAula.getSelectionModel().getSelectedItem();
        if(aulaClidad != null){
            deleteAula(aulaClidad);
        }
    }

    /**
     * Metode que fa la crida al servdor per eliminar l'aula
     * @param aulaClidad Objecte aula seleccionat
     */
    private void deleteAula(Aula aulaClidad) {
        ServerComunication socket = new ServerComunication();
        Peticio peticio = new Peticio("ELIMINAR_AULA");

        try{
            socket.connect();
            peticio.addDades(SingletonConnection.getInstance().getKey());
            peticio.addDades(JsonUtil.toJson(aulaClidad));

            String resposta = socket.sendMessage(JsonUtil.toJson(peticio));

            if(new JSONObject(resposta).getInt("codiResultat") == 1){
                carregarAules();
            }else{
                ErrorController.showErrorAlert(
                        "ELIMINAR AULA",
                        "",
                        "Error en eliminar l'aula " + aulaClidad.getNomAula() + " pot contenir informació relacionada.",
                        Alert.AlertType.ERROR );
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }
}
