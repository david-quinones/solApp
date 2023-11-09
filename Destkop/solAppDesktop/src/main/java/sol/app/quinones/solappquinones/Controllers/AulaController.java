package sol.app.quinones.solappquinones.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import sol.app.quinones.solappquinones.Models.Model;

import java.net.URL;
import java.util.ResourceBundle;

public class AulaController implements Initializable, ITopMenuDelegation {
    public AnchorPane mainAula;
    public TextField idTextMostra;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainAula.getChildren().add(0, Model.getInstance().getViewFactory().getMenuTopViewr(this));

    }

    @Override
    public void onBtnCrear() {
        idTextMostra.setText("hola soc Aula");
    }

    @Override
    public void onBtnEditar() {

    }

    @Override
    public void onBtnEliminar() {

    }
}
