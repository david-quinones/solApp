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

    //injectem dependencia per tindre total accés
    @FXML
    private MenuController menuController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuController.setRol(this.rol);
    }
}
