package sol.app.quinones.solappquinones.Controllers;

import com.google.gson.JsonObject;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import sol.app.quinones.solappquinones.Models.Model;
import sol.app.quinones.solappquinones.Models.Peticio;
import sol.app.quinones.solappquinones.Models.Usuari;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Controlador per la finestra de inici de sessió
 *
 * @author david
 */
public class LoginController implements Initializable {

    //Components de la inerficia grafica
    public Label lbl_usuari;
    public TextField fld_usuari;
    public TextField fld_password;
    public Button btn_accedir;


    //Objectes per la comunicació amb el servidor i construir les peticions
    private ServerComunication socket = new ServerComunication();
    private Peticio peticio = new Peticio();
    private SingletonConnection singletonConnection = SingletonConnection.getInstance();

    /**
     * Metode inicialitzador, es crida després de carregar la finestra
     * Configura les accions dels components
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_accedir.setOnAction(event -> onLogin()); //en apretar, accedim al metode
    }

    /**
     * Metode que es crida en presionar el boto d'accedir
     * Gestiona el procés d'inici de sessio
     */
    private void onLogin(){

        //Obtenim les dades dels camps
        String name = fld_usuari.getText();
        String password = fld_password.getText();

        //Mirem que usuari and password no estigui buit (sempre han de tindre dades)
        if(fld_usuari.getText().isEmpty() || fld_password.getText().isEmpty()){
            ErrorController.showErrorAlert("Error","Error de Dades", "Es necessari emplenar Usuari i Password" , Alert.AlertType.INFORMATION);
            fld_usuari.requestFocus(); //pasar focus (selecciona todo)
            fld_usuari.positionCaret(fld_usuari.getText().length());//poner focus al final (no selecciona)
            fld_password.setText("");
            return;

        }

        //netejem List (no tingui temporals d'errors anteriors)
        peticio.dropDades();

        //Connect to server and send petition
        try{
            //connect soket
            socket.connect();
            //add type peticio
            peticio.setPeticio("LOGIN");
            //create user
            Usuari user = new Usuari(name, password);
            //add Object to array - convert with serverStr to JSON
            peticio.addDades(JsonUtil.toJson(user));
            //send message (convert petition to JSON)
            String respota = socket.sendMessage(JsonUtil.toJson(peticio));

            /*RESPOSTA*/
            //convert to "JSON"
            JSONObject jObj = new JSONObject(respota);

            if(jObj.getInt("codiResultat") == 0) {
                ErrorController.showErrorAlert("Error","Error de Dades", "Usuari o Contrasenya incorrectes" , Alert.AlertType.WARNING);
                fld_password.setText(""); //netejem password
                fld_password.requestFocus(); //posem focus al password (com està net, no fa falta anar al final)

            }else{
                //Deserializem l'objecte usuari assignant-lo al Singleton directament
                singletonConnection.setUserConnectat(Usuari.fromJson(jObj.getJSONArray("dades").get(0).toString()));
                //Assignem la Key a singleton
                singletonConnection.setKey(jObj.getJSONArray("dades").get(1).toString());

                Stage stage = (Stage) lbl_usuari.getScene().getWindow(); //obtenim la finestra del label existent
                Model.getInstance().getViewFactory().closeStage(stage); //tanquem la finestra

                //mirem rol de l'usuari per obrir un menu o un altre
                if (singletonConnection.getUserConnectat().isAdmin()){
                    Model.getInstance().getViewFactory().showMainWindow("admin"); //mostrem la finesta nova
                }else if (singletonConnection.getUserConnectat().isTeacher()){
                    Model.getInstance().getViewFactory().showMainWindow("teacher"); //mostrem la finesta nova
                }else{
                    Model.getInstance().getViewFactory().showMainWindow("user"); //mostrem la finesta nova
                }
            }

        } catch (IOException e) {
            ErrorController.showErrorAlert("Error","Error de Comunicació", e.getMessage(), Alert.AlertType.ERROR);
            throw new RuntimeException(e);
        } catch (JSONException e) {
            ErrorController.showErrorAlert("Error","Error en tractar el 'JSON' de resposta", e.getMessage(), Alert.AlertType.ERROR);
            throw new RuntimeException(e);
        }

    }

}
