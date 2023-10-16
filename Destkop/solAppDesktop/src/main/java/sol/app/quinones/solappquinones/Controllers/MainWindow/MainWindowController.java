package sol.app.quinones.solappquinones.Controllers.MainWindow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    private final String rol;

    //contructor
    public MainWindowController(String rol){
        this.rol = rol;
    }

    //injectem dependencia per tindre total accés(es el id de FXML + Controller)
    @FXML
    private MenuController mainMenuController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainMenuController.setRol(this.rol);
    }
}
