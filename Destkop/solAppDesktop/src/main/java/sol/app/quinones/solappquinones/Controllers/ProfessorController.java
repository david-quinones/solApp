package sol.app.quinones.solappquinones.Controllers;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProfessorController implements Initializable, ITopMenuDelegation {

    public TableColumn idNIFProfe;
    public TableColumn idTelfProfe;
    public TableColumn idMailProfe;
    @FXML
    private VBox vBoxMainProfessor;
    @FXML
    private TableView tableProfe;
    @FXML
    private TableColumn idNomProfe;
    @FXML
    private TableColumn idCognomProfe;
    @FXML
    private AnchorPane mainProfessor;
    @FXML
    private TextField idTextMostra;

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

    }

    private void assignarColumnesTaula(){
        idNomProfe.setCellValueFactory(new PropertyValueFactory<>("nom"));
        idCognomProfe.setCellValueFactory(new PropertyValueFactory<>("cognom1"));
    }

    private void carregarProfessors() {

        String resposta = ConsultesSocket.serverPeticioConsulta("LLISTAR_EMPLEATS");
        if(resposta != null){
            try {
                JSONObject jsonObject = new JSONObject(resposta);
                if(jsonObject.getInt("codiResultat") != 0){
                    JSONArray arrayProfessors = jsonObject.getJSONArray("dades");
                    for(int i = 1; i < jsonObject.getJSONArray("dades").length(); i++){
                        professorArrayList.add(Professor.fromJson(arrayProfessors.get(i).toString()));
                    }

                    StringBuilder stringBuilder = new StringBuilder();
                    for(Professor p : professorArrayList) {
                        stringBuilder.append(p.getNom());
                        stringBuilder.append(", ");
                        //System.out.println(p.getNom());
                    }

                    professorArrayListTable = FXCollections.observableArrayList(professorArrayList);

                    idTextMostra.setText(stringBuilder.toString());




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
        idTextMostra.setText("hola soc Professor");
    }

    @Override
    public void onBtnEditar() {

    }

    @Override
    public void onBtnEliminar() {

    }

}
