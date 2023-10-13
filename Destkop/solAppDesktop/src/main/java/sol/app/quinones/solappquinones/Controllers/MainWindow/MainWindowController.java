package sol.app.quinones.solappquinones.Controllers.MainWindow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    private final String rol;

    public MainWindowController(String rol){
        this.rol = rol;
    }

    @FXML
    private MenuController menuController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuController.setRol(this.rol);
    }
}
