package sol.app.quinones.solappquinones.Service;

import sol.app.quinones.solappquinones.Models.Peticio;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;

import java.io.IOException;

/**
 * Classe per gestionar les consultes de retorn de llistat al servidor
 *
 * @author david
 */
public class ConsultesSocket {

    private static Peticio peticio = new Peticio();
    private static ServerComunication socket = new ServerComunication();


    /**
     * Realita petició server utilitzant socket
     * Envia una petició especifica (que ve de paramtre) i retorna respota
     * @param petition tipus de peticio
     * @return resposta del servidor en String
     */
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
