package sol.app.quinones.solappquinones.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sol.app.quinones.solappquinones.Controllers.MainWindow.MainWindowController;


public class ViewFactory {
    //Admin Views
    private AnchorPane dashboardView;

    public ViewFactory(){}

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

    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);
    }

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
        stage.setTitle("SOLAPP - ESTEL BRESSOL");
        stage.show();
    }

    public void closeStage(Stage stage){
        stage.close();
    }


}
