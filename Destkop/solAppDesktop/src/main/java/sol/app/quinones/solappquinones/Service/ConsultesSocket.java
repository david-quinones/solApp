package sol.app.quinones.solappquinones.Service;

import sol.app.quinones.solappquinones.Models.Peticio;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;

import java.io.IOException;

public class ConsultesSocket {

    private static Peticio peticio = new Peticio();
    private static ServerComunication socket = new ServerComunication();


    public static String serverPeticioConsulta(String petition){
        peticio.dropDades();

        String resposta;

        try{
            socket.connect();
            peticio.setPeticio(petition);
            peticio.addDades(SingletonConnection.getInstance().getKey());
            resposta = socket.sendMessage(JsonUtil.toJson(peticio));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return resposta;
    }

    //
}
