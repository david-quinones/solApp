package sol.app.quinones.solappquinones.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import sol.app.quinones.solappquinones.Controllers.LoginController;
import sol.app.quinones.solappquinones.Service.SingletonConnection;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Text txt_user_name;
    public Label lbl_login_date;
    public ListView listview_conversations;
    public TextField txt_alumncode;
    public TextArea txtf_message;
    public Button btn_sendMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //etiqueta del dia d'avui
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        lbl_login_date.setText("Avui, " + simpleDateFormat.format(date));

        txt_user_name.setText("Hola, " + SingletonConnection.getInstance().getUserConnectat().getNomUsuari().toUpperCase());


    }
}
