package sol.app.quinones.solappquinones.Service;

import javafx.stage.Stage;
import sol.app.quinones.solappquinones.Models.Model;

public class MenuService {

    public static void logout(Stage stage){

        //TODO refactorizar logout, todas lo tienen solo 1 vez escrito
        //liberar token si dice ok server
        SingletonConnection.getInstance().closeConnection();
        //mostrat pantalla TODO Refactorizar metodo de cerrar ya que se utiliza muhco

        Model.getInstance().getViewFactory().closeStage(stage); //tanquem la finestra
        Model.getInstance().getViewFactory().showLoginWindow(); //mostrem la finesta nova
    }

}
