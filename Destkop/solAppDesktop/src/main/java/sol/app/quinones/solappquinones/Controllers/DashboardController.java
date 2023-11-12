package sol.app.quinones.solappquinones.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import sol.app.quinones.solappquinones.Controllers.LoginController;
import sol.app.quinones.solappquinones.Models.Usuari;
import sol.app.quinones.solappquinones.Service.SingletonConnection;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Controlador per el dashboard principal
 *
 * @author david
 */
public class DashboardController implements Initializable {
    public Text txt_user_name;
    public Label lbl_login_date;
    public ListView listview_conversations;
    public TextField txt_alumncode;
    public TextArea txtf_message;
    public Button btn_sendMessage;

    /**
     * Metode inicialitzador cridat després de carregar la finestra
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Mostra la data actual en format 01-01-1999
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        lbl_login_date.setText("Avui, " + simpleDateFormat.format(date));

        //Assigna el nom a la capálera segons l'Objecte usuari que ens ha arribat en majustucles
        txt_user_name.setText("Hola, " + SingletonConnection.getInstance().getUserConnectat().getNomUsuari().toUpperCase());


    }
}
