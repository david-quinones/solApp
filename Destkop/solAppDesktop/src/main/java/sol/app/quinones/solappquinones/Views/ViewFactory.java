package sol.app.quinones.solappquinones.Views;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sol.app.quinones.solappquinones.Controllers.*;
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

    private static final String titleApp = "SOLAPP - ESTEL BRESSOL";
    private AnchorPane dashboardView;
    private AnchorPane perfilView;
    private HBox menuTopViewr;
    private AnchorPane aulaView;
    private AnchorPane alumneView;
    private AnchorPane professorView;

    //controlar que clico al menu
    private final StringProperty seleccioClientItemMenu;

    private TopMenuController topMenuController;
    private ServerComunication socket = new ServerComunication();

    /**
     *
     * TODO
     */
    public ViewFactory(){
        this.seleccioClientItemMenu = new SimpleStringProperty("");
    }

    public StringProperty getSeleccioClientItemMenu() {
        return seleccioClientItemMenu;
    }

    public AnchorPane getPerfilView() {
        //if(perfilView == null){
            try {
                perfilView = new FXMLLoader(getClass().getResource("/Fxml/PerfilUsuari.fxml")).load();
            }catch(Exception e){
                e.printStackTrace();
            }
        //}
        return perfilView;
    }

    public HBox getMenuTopViewr(ITopMenuDelegation accions) {
       // if(menuTopViewr == null){
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/MainWindow/MenuAction.fxml"));
                menuTopViewr = fxmlLoader.load();
                TopMenuController controller = fxmlLoader.getController();
                controller.setTopMenuDelegation(accions);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
      //  }
        return menuTopViewr;
    }

    public AnchorPane getAulaView() {
        //if(aulaView == null){
            try {
                aulaView = new FXMLLoader(getClass().getResource("/Fxml/Aula.fxml")).load();
            }catch(Exception e){
                e.printStackTrace();
            }
        //}
        return aulaView;
    }

    public AnchorPane getProfessorView() {
        //if(aulaView == null){
        try {
            professorView = new FXMLLoader(getClass().getResource("/Fxml/Professor.fxml")).load();
        }catch(Exception e){
            e.printStackTrace();
        }
        //}
        return professorView;
    }

    public AnchorPane getAlumneView() {
        //if(aulaView == null){
        try {
            alumneView = new FXMLLoader(getClass().getResource("/Fxml/Alumne.fxml")).load();
        }catch(Exception e){
            e.printStackTrace();
        }
        //}
        return alumneView;
    }

    public void showWindowForm(String title){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/WindowFormP.fxml"));
        createStage(loader, true, title, true);
    }



    /**
     * Obte i crea la vista Dashboard
     *
     * @return instancia de {@link AnchorPane}
     */
    public AnchorPane getDashboardView(){
        if(dashboardView == null){
            try{
                dashboardView = new FXMLLoader(getClass().getResource("/Fxml/Dashboard_.fxml")).load();
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
        createStage(loader, true, "", false);
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
        createStage(loader, false, "", false);

    }



    /**
     * Crea i mostra una nova finestra basada amb el FXML proporcionat
     * També te control al tancar per la X l'aplicació
     * @param loader FXMLLoader per carregar la vista
     * @param login identifica si es la finestra de login o no per realitzar n accio
     */
    private void createStage(FXMLLoader loader, boolean login, String title, boolean block) {
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        }catch(Exception e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/Images/logo.png")));
        stage.setTitle(titleApp + title);
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

        if(block){
            stage.initModality(Modality.APPLICATION_MODAL);
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

    public String getTitleApp() {
        return titleApp;
    }


}
