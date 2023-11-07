package sol.app.quinones.solappquinones.Controllers.MainWindow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import sol.app.quinones.solappquinones.Models.Model;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador per la finestra principal de l'apliació
 * S'encarrega d'inicialitzar i gestionar el comportament basat amb el rol de l'usuri
 *
 * @author david
 */
public class MainWindowController implements Initializable {

    //Rol de l'usuari que ha iniciat sessió
    private final String rol;

    //Injectem dependencia del panel
    @FXML
    private BorderPane mainBorderPane;


    //Injecció dependencia controlador menu (Nom ha de ser Id *.fxml + Controller)
    @FXML
    private MenuController mainMenuController;

    /**
     * Contructor
     * @param rol Rol de l'usuari que ha iniciat
     */
    public MainWindowController(String rol){
        this.rol = rol;
    }


    /**
     * Metode inicialitzador, que es crida després de carregar la finestra
     * Estableix el rol de l'usuari al controaldor del menu i inicializa el listener per saber que prenem i realizar accions sobre el FXML (mainMenu)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mainMenuController.setRol(this.rol);

        //listener to menu acction
        Model.getInstance().getViewFactory().getSeleccioClientItemMenu().addListener((observableValue, oldV, newV) -> {
            switch (newV){
                case "Perfil":
                    mainBorderPane.setCenter(Model.getInstance().getViewFactory().getPerfilView());
                    //mainBorderPane.setTop(Model.getInstance().getViewFactory().getMenuTopViewr());
                    mainBorderPane.setTop(null);
                    break;
                case "X":
                    mainBorderPane.setCenter(Model.getInstance().getViewFactory().getDashboardView());
                    mainBorderPane.setTop(null);
                    break;
                default:
                    mainBorderPane.setCenter(Model.getInstance().getViewFactory().getDashboardView());
                    mainBorderPane.setTop(null);
                    break;
            }
        });
    }

}
