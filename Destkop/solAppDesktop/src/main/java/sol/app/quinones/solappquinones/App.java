package sol.app.quinones.solappquinones;

import javafx.application.Application;
import javafx.stage.Stage;
import sol.app.quinones.solappquinones.Models.Model;


/**
 * Clase principal de l'aplicació, exten de "Application" per utilitzar JavaFX
 * S'encarrega d'inicialitzar l'aplicació i mostrar la finestra de login
 *
 * @author david
 */

public class App extends Application {
    /**
     * Punt d'entrada de l'apliació. El metode s'executa quan s'inicialitza
     * @param stage Escenari principal de l'aplicació
     */
    @Override
    public void start(Stage stage) {
        //Obtenim una instancia del model
        //obtenim atraves de l'instacnia de model una instancia de ViewFactory i moestrem la finestra
        Model.getInstance().getViewFactory().showLoginWindow();
    }
}
