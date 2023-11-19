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

/**
 * Controlador per la finestra del formulari dels usuaris
 *
 * Implementa la finestra dels usuaris per la modificació del mateix
 *
 * @author david
 */
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


    /**
     * Estableix controlador dels usuaris
     * @param usuariController Controlador de usuari principal
     */
    public void setUsuariController(UsuariController usuariController) {
        this.usuariController = usuariController;
    }

    /**
     * Inicializador del controlador i configuració de botons, dades UI
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idBtnAcceptar.setOnAction(event -> guardarObjecte());
    }

    /**
     * Desa objetce usuari
     * Logica per agafar les dades de la finestra i convertir en objecte i cridar metode que fa petició servidor
     */
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

    /**
     * Fa crida al API per guardar la modificacions de l'usuari
     * @param u Usuari a guardar
     * @return true si es existos
     */

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

    /**
     * Carrega a la finestra l'usuari passat
     * @param u Usuari que s'ha de carregar
     */
    public void loadObject(Usuari u) {
        //carregar als camps l'objecte
        idTxtFld1.setText(u.getNomUsuari());
        idTxtFld2.setText(u.getPassword());
        idBoxAdmin.setSelected(u.getIsAdmin());
        idBoxProfessor.setSelected(u.getIsTeacher());
        idBoxActiu.setSelected(u.getIsActive());

        //modificar el nom
        idBtnAcceptar.setText("Modificar");

        //Assignem objecte per tindre la infor completa si s'escau (la principal)
        userLoad = u;
    }

}
