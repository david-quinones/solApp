package sol.app.quinones.solappquinones.Controllers.MainWindow;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sol.app.quinones.solappquinones.Controllers.TopMenuController;
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
        //estabelix el rol al contrlador del menu (per saber quins menus s'han de mostrar)
        mainMenuController.setRol(this.rol);

        //Add listener a la propietat de seleccio del menu
        Model.getInstance().getViewFactory().getSeleccioClientItemMenu().addListener((observableValue, oldV, newV) -> {
            switch (newV){
                //depenendt del valor (StringProp de View)
                case "Perfil":
                    //carregar al centre la vista
                    mainBorderPane.setCenter(Model.getInstance().getViewFactory().getPerfilView());
                    mainBorderPane.setTop(null);
                    //Actualitzar el titul de la finestra
                    setStageTitle((Stage) mainBorderPane.getScene().getWindow(), " - Perfil");
                    break;
                case "Alumne":
                    mainBorderPane.setCenter(Model.getInstance().getViewFactory().getAlumneView());
                    setStageTitle((Stage) mainBorderPane.getScene().getWindow(), " - Alumne");
                    mainBorderPane.setTop(null);
                    break;
                case "Professor":
                    mainBorderPane.setCenter(Model.getInstance().getViewFactory().getProfessorView());
                    setStageTitle((Stage) mainBorderPane.getScene().getWindow(), " - Professor");
                    mainBorderPane.setTop(null);
                    break;
                case "Aula":
                    mainBorderPane.setCenter(Model.getInstance().getViewFactory().getAulaView());
                    setStageTitle((Stage) mainBorderPane.getScene().getWindow(), " - Aula");
                    mainBorderPane.setTop(null);
                    break;
                case "Usuari":
                    mainBorderPane.setCenter(Model.getInstance().getViewFactory().getUserView());
                    setStageTitle((Stage) mainBorderPane.getScene().getWindow(), " - Usuaris");
                    mainBorderPane.setTop(null);
                    break;
                default:
                    mainBorderPane.setCenter(Model.getInstance().getViewFactory().getDashboardView());
                    setStageTitle((Stage) mainBorderPane.getScene().getWindow(), "");
                    mainBorderPane.setTop(null);
                    break;
            }
        });
    }

    /**
     * Assigna el titul a les diferents finestres depenent de la pantalla de manteniment que estem
     * @param stage
     * @param nameForm
     */
    public void setStageTitle(Stage stage, String nameForm){
        stage.setTitle(Model.getInstance().getViewFactory().getTitleApp() + nameForm);
    }

}
