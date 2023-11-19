package sol.app.quinones.solappquinones.Controllers.Usuari;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;
import sol.app.quinones.solappquinones.Models.Peticio;
import sol.app.quinones.solappquinones.Models.Usuari;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;

import java.net.URL;
import java.util.ResourceBundle;

public class WindowsFormUsuariController implements Initializable {

    @FXML
    private TextField idTxtFld2, idTxtFld1;
    @FXML
    private CheckBox idBoxAdmin, idBoxProfessor, idBoxActiu;
    @FXML
    private Button idBtnAcceptar;

    private UsuariController usuariController;
    private Usuari userLoad;
    private Peticio peticio = new Peticio();
    private ServerComunication socket = new ServerComunication();


    public void setUsuariController(UsuariController usuariController) {
        this.usuariController = usuariController;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idBtnAcceptar.setOnAction(event -> guardarObjecte());
    }

    private void guardarObjecte() {

        //carregar l'usuari
        Usuari u = new Usuari(
                userLoad.getId(),
                idTxtFld1.getText(),
                idTxtFld2.getText(),
                //TODO
                //idTxtFld2.getText().equals(userLoad.getPassword()) ? null : idTxtFld2.getText(),
                idBoxProfessor.isSelected(),
                idBoxAdmin.isSelected(),
                idBoxActiu.isSelected()
        );

        //cridar metode per guardar objecte
        boolean guardat = saveUserDb(u);

        if(guardat){
            Stage actual = (Stage) idTxtFld2.getScene().getWindow();
            actual.close();
            usuariController.carregarUsuaris();
        }



    }

    private boolean saveUserDb(Usuari u) {

        peticio.dropDades();

        try {

            socket.connect();
            peticio.setPeticio("MODIFICAR_USUARI");
            peticio.addDades(SingletonConnection.getInstance().getKey());
            peticio.addDades(JsonUtil.toJson(u));

            String resposta = socket.sendMessage(JsonUtil.toJson(peticio));

            if(resposta != null){
                JSONObject jsonObject = new JSONObject(resposta);
                if(jsonObject.getInt("codiResultat") == 1){
                    return true;
                }else{
                    return false;
                }

            }

        }catch(Exception e){
            return false;
        }

        return true;
    }

    public void loadObject(Usuari u) {
        //carregar als camps l'objecte
        idTxtFld1.setText(u.getNomUsuari());
        idTxtFld2.setText(u.getPassword());
        idBoxAdmin.setSelected(u.getIsAdmin());
        idBoxProfessor.setSelected(u.getIsTeacher());
        idBoxActiu.setSelected(u.getIsActive());

        //modificar el nom
        idBtnAcceptar.setText("Modificar");

        userLoad = u;
    }

}
