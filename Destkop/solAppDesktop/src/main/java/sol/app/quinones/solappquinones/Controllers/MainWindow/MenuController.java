package sol.app.quinones.solappquinones.Controllers.MainWindow;

import javafx.fxml.Initializable;
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



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btn_logout.setOnAction(event -> logout());

    }

    public void setRol(String rol){
        this.rol = rol;
        selectMenu(this.rol);
    }

    private void selectMenu(String rol){
        switch (rol){
            case "teacher":
                btn_user.setDisable(true);
                btn_professor.setDisable(true);
                break;
            case "user":
                //btn_user.setDisable(true);
                btn_user.setVisible(false);
                btn_user.setManaged(false);
                //btn_professor.setDisable(true);
                btn_professor.setVisible(false); //ocultar
                btn_professor.setManaged(false); //reajustar menu (que no el tingui en compte)
                btn_alumne.setDisable(true);
                btn_aula.setDisable(true);
                break;
            case "admin":
                break;
        }
    }

    public void logout(){

        try{
            //TODO
            socket.connect();
            Peticio peticio = new Peticio("LOGOUT");
            //peticio.addDades(SingletonConnection.getInstance().getUserConnectat().toString());
            peticio.addDades(SingletonConnection.getInstance().getKey().replace("\"",""));

            String resposta = socket.sendMessage(JsonUtil.toJson(peticio));

            JSONObject jsonObject = new JSONObject(resposta);
            if(jsonObject.getInt("codiResultat") == 1){
                //TODO refactorizar logout, todas lo tienen solo 1 vez escrito
                //liberar token si dice ok server
                SingletonConnection.getInstance().closeConnection();
                //mostrat pantalla TODO Refactorizar metodo de cerrar ya que se utiliza muhco
                Model.getInstance().getViewFactory().closeStage((Stage) btn_perfil.getScene().getWindow()); //tanquem la finestra
                Model.getInstance().getViewFactory().showLoginWindow(); //mostrem la finesta nova
            }else{
                System.out.println("no se ha podido salir, "); //TODO
            }

            System.out.println(resposta);


        }catch(Exception e){
            e.printStackTrace();
        }

    }



}
