package sol.app.quinones.solappquinones.Controllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import sol.app.quinones.solappquinones.Models.Peticio;
import sol.app.quinones.solappquinones.Models.Usuari;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe que realitza un test sobre el el metode login del servidor
 *
 * @author david
 */
public class LoginControllerTest {

    /**
     * Login correcte --> s'escriu la mateixa logica que la classe controlador. (no té sentit)
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void testLoginCorrecte() throws IOException, JSONException {
        String username = "admin";
        String password = "portaglam";

        Usuari u = new Usuari(username, password);
        Peticio p = new Peticio("LOGIN");
        p.addDades(JsonUtil.toJson(u).toString());

        ServerComunication socket = new ServerComunication();
        socket.connect();
        //String resposta = socket.sendMessage(JsonUtil.toJson(p));

        JSONObject j = new JSONObject(socket.sendMessage(JsonUtil.toJson(p)));
        assertEquals(1,j.getInt("codiResultat"));

    }

    /**
     * Logica incorrecte
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void testLoginIncorrecte() throws IOException, JSONException {
        String username = "admin";
        String password = "portaflan";

        Usuari u = new Usuari(username, password);
        Peticio p = new Peticio("LOGIN");
        p.addDades(JsonUtil.toJson(u).toString());;

        ServerComunication socket = new ServerComunication();
        socket.connect();

        JSONObject j = new JSONObject(socket.sendMessage(JsonUtil.toJson(p)));
        assertEquals(0,j.getInt("codiResultat"));

    }
}
