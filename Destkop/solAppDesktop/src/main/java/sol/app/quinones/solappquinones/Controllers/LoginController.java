package sol.app.quinones.solappquinones.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sol.app.quinones.solappquinones.Models.Model;
import sol.app.quinones.solappquinones.Models.Usuari;
import sol.app.quinones.solappquinones.Service.JSON.ServerStructure;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public Label lbl_usuari;
    public TextField fld_usuari;
    public TextField fld_password;
    public Button btn_accedir;

    private ServerStructure server = new ServerStructure();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_accedir.setOnAction(event -> onLogin()); //en apretar, accedim al metode
    }

    private void onLogin(){
        String name = fld_usuari.getText();
        String password = fld_password.getText();

        //create new instance and object User
        Usuari user = new Usuari(name, password);
        //Serialzate to JSON
        String useraiJSON = server.toJson(user);
        //print only for test (client send)
        System.out.println(useraiJSON);

        //assign Id (Simulation Server return exemple)
        user.setId(1);
        useraiJSON = server.toJson(user);
        //retorn server
        Usuari userNew = Usuari.fromJson(useraiJSON);
        System.out.println(userNew.getUsername() + ' ' + userNew.getId());

        if(name.equalsIgnoreCase("david") && password.equalsIgnoreCase("david")){
            Stage stage = (Stage) lbl_usuari.getScene().getWindow(); //obtenim la finestra del label existent
            Model.getInstance().getViewFactory().closeStage(stage); //tanquem la finestra
            Model.getInstance().getViewFactory().showAdminWindow(); //mostrem la finesta nova
        }else{
            showErrorAlert("ACCESO DENEGADO", "User / Passwor bad", "El usuario o la contraseña son erroenos");
            fld_password.setText("");
        }
    }

    public void showErrorAlert(String title, String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
