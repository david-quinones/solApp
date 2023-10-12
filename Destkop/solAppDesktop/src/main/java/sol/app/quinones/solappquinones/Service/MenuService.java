package sol.app.quinones.solappquinones.Service;

import javafx.stage.Stage;
import sol.app.quinones.solappquinones.Models.Model;
import sol.app.quinones.solappquinones.Models.Peticio;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;

public class MenuService {

    /*CLOSE CONNECTION ATRIBUTES*/
    private ServerComunication socket = new ServerComunication();



    public void logout(Stage stage){
        try{
            //TODO
            socket.connect();
            Peticio peticio = new Peticio("LOGOUT");
            //peticio.addDades(SingletonConnection.getInstance().getUserConnectat().toString());
            peticio.addDades(SingletonConnection.getInstance().getKey().replace("\"",""));

            String resposta = socket.sendMessage(JsonUtil.toJson(peticio));

            System.out.println(resposta);


        }catch(Exception e){
            e.printStackTrace();
        }

        //TODO refactorizar logout, todas lo tienen solo 1 vez escrito
        //liberar token si dice ok server
        SingletonConnection.getInstance().closeConnection();
        //mostrat pantalla TODO Refactorizar metodo de cerrar ya que se utiliza muhco

        Model.getInstance().getViewFactory().closeStage(stage); //tanquem la finestra
        Model.getInstance().getViewFactory().showLoginWindow(); //mostrem la finesta nova
    }

}
