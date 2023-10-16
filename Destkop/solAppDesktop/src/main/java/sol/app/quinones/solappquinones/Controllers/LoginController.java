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
 * The type Login controller.
 */
public class LoginController implements Initializable {
    /**
     * The Lbl usuari.
     */
    public Label lbl_usuari;
    /**
     * The Fld usuari.
     */
    public TextField fld_usuari;
    /**
     * The Fld password.
     */
    public TextField fld_password;
    /**
     * The Btn accedir.
     */
    public Button btn_accedir;


    private ServerComunication socket = new ServerComunication();
    private Peticio peticio = new Peticio();

    private SingletonConnection singletonConnection = SingletonConnection.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_accedir.setOnAction(event -> onLogin()); //en apretar, accedim al metode
    }

    private void onLogin(){

        String name = fld_usuari.getText();
        String password = fld_password.getText();


        //TODO
        if(fld_usuari.getText().isEmpty() || fld_password.getText().isEmpty()){
            fld_usuari.requestFocus(); //pasar focus (selecciona todo)
            fld_usuari.positionCaret(fld_usuari.getText().length());//poner focus al final (no selecciona)
        }

        System.out.println(password);

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

                System.out.println("Error en la connexió"); //finestra floatant i netejar valors TODO
                fld_password.setText("");
                fld_password.requestFocus();

            }else{

                System.out.println("creem Objecte..");
                singletonConnection.setUserConnectat(Usuari.fromJson(jObj.getJSONArray("dades").get(0).toString()));
                singletonConnection.setKey(jObj.getJSONArray("dades").get(1).toString());

                Stage stage = (Stage) lbl_usuari.getScene().getWindow(); //obtenim la finestra del label existent
                Model.getInstance().getViewFactory().closeStage(stage); //tanquem la finestra

                if (singletonConnection.getUserConnectat().isAdmin()){
                    Model.getInstance().getViewFactory().showMainWindow("admin"); //mostrem la finesta nova
                }else if (singletonConnection.getUserConnectat().isTeacher()){
                    Model.getInstance().getViewFactory().showMainWindow("teacher"); //mostrem la finesta nova
                }else{
                    Model.getInstance().getViewFactory().showMainWindow("user"); //mostrem la finesta nova
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        /*

        }else{
            showErrorAlert("ACCESO DENEGADO", "User / Passwor bad", "El usuario o la contraseña son erroenos");
            fld_password.setText("");
        }
        */

    }

    /**
     * Show error alert.
     *
     * @param title       the title
     * @param headerText  the header text
     * @param contentText the content text
     */
    public void showErrorAlert(String title, String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
