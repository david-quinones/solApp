package sol.app.quinones.solappquinones.Controllers.admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sol.app.quinones.solappquinones.Models.Model;
import sol.app.quinones.solappquinones.Service.SingletonConnection;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    public Button btn_user;
    public Button btn_alumne;
    public Button btn_professor;
    public Button btn_perfil;
    public Button btn_logout;
    public Button btn_report;
    public Button btn_aula;
    public Button btn_comunicacio;
    public Button btn_esdeveniment;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btn_logout.setOnAction(event -> logout());

    }

    public void logout(){
        //TODO refactorizar logout, todas lo tienen solo 1 vez escrito
        //liberar token si dice ok server
        SingletonConnection.getInstance().closeConnection();
        //mostrat pantalla TODO Refactorizar metodo de cerrar ya que se utiliza muhco

        Stage stage = (Stage) btn_logout.getScene().getWindow(); //obtenim la finestra del label existent
        Model.getInstance().getViewFactory().closeStage(stage); //tanquem la finestra
        Model.getInstance().getViewFactory().showLoginWindow(); //mostrem la finesta nova
    }

}
