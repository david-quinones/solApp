package sol.app.quinones.solappquinones.Controllers.Aula;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import sol.app.quinones.solappquinones.Controllers.ITopMenuDelegation;
import sol.app.quinones.solappquinones.Controllers.TopMenuController;
import sol.app.quinones.solappquinones.Models.Model;
import sol.app.quinones.solappquinones.Models.VistaController;

import java.net.URL;
import java.util.ResourceBundle;

public class AulaController implements Initializable, ITopMenuDelegation {

    @FXML
    private AnchorPane mainAula;
    @FXML
    private TextField idTextMostra;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        VistaController<TopMenuController> vistaController = Model.getInstance().getViewFactory().getMenuTopViewr(this);
        HBox topMenuView = vistaController.getView();
        TopMenuController topMenuController = vistaController.getController();
        topMenuController.disableCrearBoto(false);
        mainAula.getChildren().add(0, topMenuView);

        //mainAula.getChildren().add(0, Model.getInstance().getViewFactory().getMenuTopViewr(this));

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
