package sol.app.quinones.solappquinones.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sol.app.quinones.solappquinones.Controllers.MainWindow.MainWindowController;


/**
 * The type View factory.
 */
public class ViewFactory {

    private AnchorPane dashboardView;

    /**
     * Instantiates a new View factory.
     */
    public ViewFactory(){}

    /**
     * Get dashboard view anchor pane.
     *
     * @return the anchor pane
     */
    public AnchorPane getDashboardView(){
        if(dashboardView == null){
            try{
                dashboardView = new FXMLLoader(getClass().getResource("/Fxml/Dashboard.fxml")).load();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return  dashboardView;
    }

    /**
     * Show login window.
     */
    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);
    }

    /**
     * Show main window.
     *
     * @param rol the rol
     */
    public void showMainWindow(String rol){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/MainWindow/MainWindow.fxml"));
        MainWindowController mainWindowController = new MainWindowController(rol);
        loader.setController(mainWindowController);
        createStage(loader);
    }


    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        }catch(Exception e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Images/logo.png")));
        stage.setTitle("SOLAPP - ESTEL BRESSOL");
        stage.show();
    }

    /**
     * Close stage.
     *
     * @param stage the stage
     */
    public void closeStage(Stage stage){
        stage.close();
    }


}
