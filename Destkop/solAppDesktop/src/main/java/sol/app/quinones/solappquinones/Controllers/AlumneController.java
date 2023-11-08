package sol.app.quinones.solappquinones.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import sol.app.quinones.solappquinones.Models.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class AlumneController implements Initializable, ITopMenuDelegation {
    @FXML
    private AnchorPane mainAlumne;
    @FXML
    private TextField idTextMostra;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainAlumne.getChildren().add(0, Model.getInstance().getViewFactory().getMenuTopViewr(this));

    }

    @Override
    public void onBtnCrear() {
        idTextMostra.setText("hola soc Alumne");
    }

    @Override
    public void onBtnEditar() {

    }

    @Override
    public void onBtnEliminar() {

    }
}
