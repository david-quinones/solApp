package sol.app.quinones.solappquinones.Controllers.MainWindow;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.json.JSONObject;
import sol.app.quinones.solappquinones.Models.Model;
import sol.app.quinones.solappquinones.Models.Peticio;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;

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


    private String rol;

    private ServerComunication socket = new ServerComunication();

    /**
     * Metode inicialitzador cridat després de carregar la finestra
     * Estableix les accions dels botons
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_logout.setOnAction(event -> logout());
        btn_perfil.setOnAction(event -> openPerfil());
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

            //TODO
            socket.connect();
            Peticio peticio = new Peticio("LOGOUT");
            peticio.addDades(SingletonConnection.getInstance().getKey().replace("\"",""));
            String resposta = socket.sendMessage(JsonUtil.toJson(peticio));

            JSONObject jsonObject = new JSONObject(resposta);
            if(jsonObject.getInt("codiResultat") == 1){

                //liberar token si dice ok server
                SingletonConnection.getInstance().closeConnection();
                //Tancar finestra actual i obrir login
                Model.getInstance().getViewFactory().closeStage((Stage) btn_perfil.getScene().getWindow());
                Model.getInstance().getViewFactory().showLoginWindow();

            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Missatge informatiu");
                alert.setHeaderText(null);
                alert.setContentText("No es pot desvincular amb el servidor, es força tancar l'aplicació");
                alert.showAndWait();
                Platform.exit();

            }

            System.out.println(resposta);


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Obre el perfil de l'usuari per veure i editar les seves dades principals
     */
    public void openPerfil(){
        //TODO
        //dashboard de perfil (poder editar dades) --> panell superior de editar (aqui també el menu superior)
    }



}
