package sol.app.quinones.solappquinones.Controllers.MainWindow;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import sol.app.quinones.solappquinones.Controllers.ErrorController;
import sol.app.quinones.solappquinones.Controllers.PerfilController;
import sol.app.quinones.solappquinones.Models.Model;
import sol.app.quinones.solappquinones.Models.Persona;
import sol.app.quinones.solappquinones.Models.Peticio;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;
import sol.app.quinones.solappquinones.Views.ViewFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador per el menu prinicpal de l'aplicació
 * S'encarrega de gestionar la visibilitat i funcionalitats dels elements
 * segons el rol d'usuari
 *
 * @author david
 */
public class MenuController implements Initializable {
    public Button btn_user;
    public Button btn_alumne;
    public Button btn_professor;
    public Button btn_perfil;
    public Button btn_logout;
    public Button btn_report;
    public Button btn_aula;
    public Button btn_comunicacio;
    public Button btn_esdeveniment;
    public ImageView image;

    private String rol;
    private ServerComunication socket = new ServerComunication();

    private Peticio peticio;

    /**
     * Metode inicialitzador cridat després de carregar la finestra
     * Estableix les accions dels botons
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btn_logout.setOnAction(event -> logout());
        btn_perfil.setOnAction(event -> openPerfil());
        btn_aula.setOnAction(event -> openAula());
        btn_alumne.setOnAction(event -> openAlumne());
        btn_professor.setOnAction(event -> openProfessor());
        btn_user.setOnAction(event -> openUserList());
        image.setOnMouseClicked(event -> loadDashboard());
        btn_comunicacio.setOnAction(e -> openMessages());

    }

    /**
     * Obre la vista de missatges
     * Estableix la propietat de seleccio del item del menu de l'apliació, canvia el valor de seleccioClient a la fabrica de vistes
     */
    private void openMessages() {
        Model.getInstance().getViewFactory().getSeleccioClientItemMenu().set("Message");
    }

    /**
     * Metode per anar al dashboard prinicpal de l'aplicació
     * En clicar la imatge superior del menu, fara aquest canvi
     *
     */
    private void loadDashboard() {
        Model.getInstance().getViewFactory().getSeleccioClientItemMenu().set("Dashboard");
    }

    /**
     * Estableix el rol de l'usuari i asjuta la visibilitat dels elements del menu
     * @param rol
     */
    public void setRol(String rol){
        this.rol = rol;
        selectMenu(this.rol);
    }

    /**
     * Ajusta la visibilitat dels elements del menu en funcio del rol que arriba per parametre
     * @param rol
     */
    private void selectMenu(String rol){
        switch (rol){
            case "teacher":
                btn_user.setVisible(false);
                btn_user.setManaged(false);

                btn_professor.setVisible(false);
                btn_professor.setManaged(false);
                break;
            case "user":

                btn_user.setVisible(false); // no el fa visible
                btn_user.setManaged(false); // reajusta el menu, per no tindre en compte el button


                btn_professor.setVisible(false); //ocultar
                btn_professor.setManaged(false);

                btn_alumne.setVisible(false);
                btn_alumne.setManaged(false);

                btn_aula.setVisible(false);
                btn_aula.setManaged(false);

                break;
            case "admin":
                //no s'oculta res
                break;
        }
    }

    /**
     * Tanca la sessió de l'usuari actual i mostra la pantalla d'inici de sessió
     */
    public void logout(){
        try{
            socket.connect();
            peticio = new Peticio("LOGOUT");
            peticio.addDades(SingletonConnection.getInstance().getKey());
            String resposta = socket.sendMessage(JsonUtil.toJson(peticio));

            JSONObject jsonObject = new JSONObject(resposta);
            if(jsonObject.getInt("codiResultat") == 1){

                //llibrear vista anterior
                Model.getInstance().getViewFactory().getSeleccioClientItemMenu().set("Dashboard");
                //liberar token si dice ok server
                SingletonConnection.getInstance().closeConnection();
                //Tancar finestra actual i obrir login
                Model.getInstance().getViewFactory().closeStage((Stage) btn_perfil.getScene().getWindow());
                Model.getInstance().getViewFactory().showLoginWindow();
            }else{
                ErrorController.showErrorAlert("Missatge Informatiu", null, "No es pot desvincular amb el servidor, es força tancar l'aplicació", Alert.AlertType.INFORMATION);
                Platform.exit();
            }

        }catch(Exception e){
            ErrorController.showErrorAlert("Error","Error al Desconnectar", e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
            Platform.exit();
        }

    }

    /**
     * Obre la vista del perfil de l'usuari iniciat a l'aplicació
     * Estableix la propietat de seleccio del item del menu de l'apliació, canvia el valor de seleccioClient a la fabrica de vistes
     */
    public void openPerfil(){
        Model.getInstance().getViewFactory().getSeleccioClientItemMenu().set("Perfil");
    }

    /**
     * Obre la vista de l'Alumne
     * Estableix la propietat de seleccio del item del menu de l'apliació, canvia el valor de seleccioClient a la fabrica de vistes
     */
    private void openAlumne() {
        Model.getInstance().getViewFactory().getSeleccioClientItemMenu().set("Alumne");
    }
    /**
     * Obre la vista de l'Aula
     * Estableix la propietat de seleccio del item del menu de l'apliació, canvia el valor de seleccioClient a la fabrica de vistes
     */
    private void openAula() {
        Model.getInstance().getViewFactory().getSeleccioClientItemMenu().set("Aula");
    }
    /**
     * Obre la vista del professor
     * Estableix la propietat de seleccio del item del menu de l'apliació, canvia el valor de seleccioClient a la fabrica de vistes
     */
    private void openProfessor() {
        Model.getInstance().getViewFactory().getSeleccioClientItemMenu().set("Professor");
    }
    /**
     * Obre la vista del llistat d'usuaris
     * Estableix la propietat de seleccio del item del menu de l'apliació, canvia el valor de seleccioClient a la fabrica de vistes
     */
    private void openUserList() {
        Model.getInstance().getViewFactory().getSeleccioClientItemMenu().set("Usuari");
    }


}
