package sol.app.quinones.solappquinones.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sol.app.quinones.solappquinones.Controllers.MainWindow.MainWindowController;


/**
 * Classe que gestiona i proporciona diferents vistes de l'aplicació
 *
 * @author david
 */
public class ViewFactory {

    private AnchorPane dashboardView;

    /**
     * Contructor per defecte
     */
    public ViewFactory(){}

    /**
     * Obte i crea la vista Dashboard (actual desus)
     *
     * @return instancia de {@link AnchorPane}
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
     * Mostra finestra inici sessió
     */
    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader);
    }

    /**
     * Mostra la finestra principal (finestra més menus) configurant segons el rol proporcionat
     *
     * @param rol Rol de l'usuari, afetctació com mostrarà la finestra principal (menu)
     */
    public void showMainWindow(String rol){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/MainWindow/MainWindow.fxml"));
        MainWindowController mainWindowController = new MainWindowController(rol);
        loader.setController(mainWindowController);
        createStage(loader);
    }

    /**
     * Crea i mostra una nova finestra basada amb el FXML proporcionat
     * @param loader FXMLLoader per carregar la vista
     */
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
     * Tanca la finestra propocionada
     *
     * @param stage Finestra a tancar
     */
    public void closeStage(Stage stage){
        stage.close();
    }


}
