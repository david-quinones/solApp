package sol.app.quinones.solappquinones.Views;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import sol.app.quinones.solappquinones.Controllers.Alumne.AlumneController;
import sol.app.quinones.solappquinones.Controllers.Alumne.WindowsFormAlumneController;
import sol.app.quinones.solappquinones.Controllers.Aula.AulaController;
import sol.app.quinones.solappquinones.Controllers.Aula.WindowsFormAulaController;
import sol.app.quinones.solappquinones.Controllers.MainWindow.MainWindowController;
import sol.app.quinones.solappquinones.Controllers.Professor.ProfessorController;
import sol.app.quinones.solappquinones.Controllers.Professor.WindowFormProfessorController;
import sol.app.quinones.solappquinones.Controllers.Usuari.UsuariController;
import sol.app.quinones.solappquinones.Controllers.Usuari.WindowsFormUsuariController;
import sol.app.quinones.solappquinones.Models.*;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;

import java.io.IOException;


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
    private AnchorPane userView;
    private AnchorPane messageView;

    //controlar que clico al menu
    private final StringProperty seleccioClientItemMenu;

    private TopMenuController topMenuController;
    private ServerComunication socket = new ServerComunication();

    /**
     * Construcctor del ViewFactory
     * Inicialitza la propietat de seleccio del item del menu
     */
    public ViewFactory(){
        this.seleccioClientItemMenu = new SimpleStringProperty("");
    }

    /**
     * Obte la propietat de seleccio del item del menu
     *
      * @return respresencació de la seleccio del item del menu
     */
    public StringProperty getSeleccioClientItemMenu() {
        return seleccioClientItemMenu;
    }

    /**
     *  Obte i crea la vista del perfil d'usuari
     *  @return Inciancia corresponent a la vista del peril
     */
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

    /**
     *  Carrega i retorna el controlador de la vista al menu superior
     * @param accions Implementació de ItopMenuController per manejar les accions dels botons
     * @return Vsta i controlador del menu superior
     */
    public VistaController<TopMenuController> getMenuTopViewr(ITopMenuDelegation accions) {
       // if(menuTopViewr == null){
        HBox view;
        TopMenuController controller;

            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/MainWindow/MenuAction.fxml"));
                menuTopViewr = fxmlLoader.load();
                //view = fxmlLoader.load();
                controller = fxmlLoader.getController();
                controller.setTopMenuDelegation(accions);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
      //  }
        return new VistaController<>(menuTopViewr, controller);
    }

    /**
     *  Obte i crea la vista de l'aula
     *  @return Inciancia corresponent a la vista de l'aula
     */
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

    /**
     *  Obte i crea la vista de professors
     *  @return Inciancia corresponent a la vista del profesor
     */
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
    /**
     *  Obte i crea la vista de uusuaris
     *  @return Inciancia corresponent a la vista d'usauris
     */
    public AnchorPane getUserView() {
        //if(aulaView == null){
        try {
            userView = new FXMLLoader(getClass().getResource("/Fxml/Usuari.fxml")).load();
        }catch(Exception e){
            e.printStackTrace();
        }
        //}
        return userView;
    }

    /**
     *  Obte i crea la vista dels alumnes
     *  @return Inciancia corresponent a la vista de l'alumne
     */
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

    /**
     *  Obte i crea la vista de missatges
     *  @return Instancia corresponent a la vista de missatges
     */
    public AnchorPane getMessageView() {
        //if(aulaView == null){
        try {
            messageView = new FXMLLoader(getClass().getResource("/Fxml/DashboardMessage.fxml")).load();
        }catch(Exception e){
            e.printStackTrace();
        }
        //}
        return messageView;
    }


    /**
     *  Mostra la finestra del formulari del professor
     *  Crea i mostra una finestra per l'edidicó o creació
     * @param title Titul de la finestra
     * @param professor Objecte professor per carregar a la finestra
     * @param professorController Controlador associat a la finestra de professor
     */
    public void showWindowFormProfessor(String title, Professor professor, ProfessorController professorController){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/WindowFormProfessor.fxml"));
        createStage(loader, true, title, true, professor, null, null);
        //una vez cargada la stage
        WindowFormProfessorController windowFormProfessorController = loader.getController();
        windowFormProfessorController.setProfessorController(professorController);

        //TODO
        //pasar aqui professor? carrega bé?¿ truere de stage, aixi només controla finestres
    }

    /**
     * Mostra la finestra del formulari de l'alumne
     * Crea i mostra una finestra per l'edidicó o creació
     * @param title titul de la finestra
     * @param a Objecte Alumne per accaregar a la finestra
     * @param alumneController Controlador associat a lka finestra de l'laumne
     */
    public void showWindowFormAlumne(String title, Alumne a, AlumneController alumneController){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/WindowFormAlumne.fxml"));
        createStage(loader, true, title, true, null, a, null);
        //una vez cargada la stage
        WindowsFormAlumneController windowsFormAlumneController = loader.getController();
        windowsFormAlumneController.setUsuariController(alumneController);

    }

    /**
     * Mostra la finestra del formulari de l'aula
     * Crea i mostra una finestra per l'edidicó o creació
     * @param title titul de la finestra
     * @param a Objecte Aula per accaregar a la finestra
     * @param aulaController Controlador associat a lka finestra de l'aula
     */
    public void showWindowFormAula(String title, Aula a, AulaController aulaController){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/WindowFormAula.fxml"));
        createStage(loader, true, title, true, null, null,a);
        //una vez cargada la stage
        WindowsFormAulaController windowsFormAulaController = loader.getController();
        windowsFormAulaController.setAulaController(aulaController);

    }

    /**
     * Motsra la finestra del formulari dels usuaris
     * Crea i mostra una finestra per l'dificó
     * @param title Titul de la finestra
     * @param u Objecte usuari per carregar a la finestra
     * @param usuariController Controladro associat a la finesra usuari
     */
    public void showWindowFormUser(String title, Usuari u, UsuariController usuariController){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/WindowFormUser.fxml"));
        createStage(loader, true, title, true, null, null, null);
        //una vez cargada la stage

        WindowsFormUsuariController windowsFormUsuariController = loader.getController();
        windowsFormUsuariController.setUsuariController(usuariController);

        windowsFormUsuariController.loadObject(u);

    }


    /**
     * Obte i crea la vista Dashboard
     *
     * @return instancia de {@link AnchorPane}
     */
    public AnchorPane getDashboardView(){
        if(dashboardView == null){
            try{
                dashboardView = new FXMLLoader(getClass().getResource("/Fxml/MainWindow/DashboardEmty.fxml")).load();
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
        createStage(loader, true, "", false, null, null, null);
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
        createStage(loader, false, "", false, null, null, null);

    }



    /**
     * Crea i mostra una nova finestra basada amb el FXML proporcionat
     * També te control al tancar per la X l'aplicació
     * @param loader FXMLLoader per carregar la vista
     * @param login identifica si es la finestra de login o no per realitzar n accio
     */
    private void createStage(FXMLLoader loader, boolean login, String title, boolean block, Professor professor, Alumne alumne, Aula aula) {
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        }catch(Exception e){
            e.printStackTrace();
        }

        //

        if(professor != null) {
            WindowFormProfessorController windowFormProfessorController = loader.getController();
            windowFormProfessorController.loadObject(professor);
        }
        if(alumne != null) {
            WindowsFormAlumneController windowsFormAlumneController = loader.getController();
            windowsFormAlumneController.loadAlumne(alumne);
        }
        if(aula != null){
            WindowsFormAulaController windowsFormAulaController = loader.getController();
            windowsFormAulaController.loadAula(aula);
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
                            Platform.exit();
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

    /**
     * Retorna el titul actual a la finestra
     * @return
     */
    public String getTitleApp() {
        return titleApp;
    }


}
