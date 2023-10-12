package sol.app.quinones.solappquinones.Controllers.admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sol.app.quinones.solappquinones.Models.Model;
import sol.app.quinones.solappquinones.Service.MenuService;
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

    private MenuService menuService = new MenuService();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btn_logout.setOnAction(event -> logout());

    }

    public void logout(){

        menuService.logout((Stage) btn_logout.getScene().getWindow());

    }

}
