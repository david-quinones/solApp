package sol.app.quinones.solappquinones;

import javafx.application.Application;
import javafx.stage.Stage;
import sol.app.quinones.solappquinones.Models.Model;


public class App extends Application {
    /*
        Entrada de l'aplicació, amb el recurs del login
    */
    @Override
    public void start(Stage stage) {
        Model.getInstance().getViewFactory().showLoginWindow();
    }
}
