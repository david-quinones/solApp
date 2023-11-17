package sol.app.quinones.solappquinones.Controllers.Usuari;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sol.app.quinones.solappquinones.Models.Usuari;

import java.net.URL;
import java.util.ResourceBundle;

public class WindowsFormUsuariController implements Initializable {

    @FXML
    private TextField idTxtFld1;

    @FXML
    private Button idBtnAcceptar;
    private UsuariController usuariController;


    public void setUsuariController(UsuariController usuariController) {
        this.usuariController = usuariController;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idBtnAcceptar.setOnAction(event -> System.out.println("boto"));
    }

    public void loadObject(Usuari u) {
        //carregar als camps l'objecte
        idTxtFld1.setText(u.getNomUsuari());

        //modificar el nom
        idBtnAcceptar.setText("Modificar");
    }
}
