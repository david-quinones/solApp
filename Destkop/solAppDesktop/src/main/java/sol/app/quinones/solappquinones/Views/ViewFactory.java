package sol.app.quinones.solappquinones.Views;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sol.app.quinones.solappquinones.Controllers.ErrorController;
import sol.app.quinones.solappquinones.Controllers.MainWindow.MainWindowController;
import sol.app.quinones.solappquinones.Controllers.MainWindow.MenuController;
import sol.app.quinones.solappquinones.Models.Peticio;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;

import java.io.IOException;
import java.net.ServerSocket;


/**
 * Classe que gestiona i proporciona diferents vistes de l'aplicació
 *
 * @author david
 */
public class ViewFactory {

    private AnchorPane dashboardView;

    private ServerComunication socket = new ServerComunication();

    /**
     * Contructor per defecte
     */
    public ViewFactory(){}

    /**
     * Obte i crea la vista Dashboard (actual desus)
     *
     * @return instancia de {@link AnchorPane}
     */

    /*
    public AnchorPane getDashboardView(){
        if(dashboardView == null){
            try{
                dashboardView = new FXMLLoader(getClass().getResource("/Fxml/Dashboard1.fxml")).load();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return  dashboardView;
    }
    */
    /**
     * Mostra finestra inici sessió
     */
    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
        createStage(loader, true);
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
        createStage(loader, false);
    }

    /**
     * Crea i mostra una nova finestra basada amb el FXML proporcionat
     * @param loader FXMLLoader per carregar la vista
     * @param login identifica si es la finestra de login o no per realitzar n accio
     */
    private void createStage(FXMLLoader loader, boolean login) {
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
        if(!login){
            stage.setOnCloseRequest(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmació");
                alert.setHeaderText("Estas tancant l'aplicació sense desconnectar");
                alert.setContentText("Segur que vols tancar-la?");
                alert.showAndWait().ifPresent(r -> {
                    if(r == ButtonType.OK){
                        try {
                            //enviem missatge i tanquem ens es igual el resultat
                            socket.connect();
                            Peticio peticio = new Peticio("LOGOUT");
                            peticio.addDades(SingletonConnection.getInstance().getKey().replace("\"",""));
                            socket.sendMessage(JsonUtil.toJson(peticio));
                        } catch (IOException e) {
                            //tanquem a saco l'aplicació l'usuari es vol desconnectar
                            Platform.exit();
                        }

                    }else{
                        event.consume();
                    }
                });


            });
        }

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
