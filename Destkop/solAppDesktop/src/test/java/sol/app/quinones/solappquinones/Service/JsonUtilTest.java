package sol.app.quinones.solappquinones.Service;

import org.junit.jupiter.api.Test;
import sol.app.quinones.solappquinones.Models.Usuari;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe que s'encarrega de comprovar el correcte funcionament de la funcionalitat per serialitzar a JSON
 *
 * Comprovem que la conversió de un objecte a format JSON es correcte
 *
 * @author david
 */
public class JsonUtilTest {

    /**
     * Metode que comprova la conversió dun objecte
     *
     * Comprovem que l'objecte no es null
     * Comrpvem la cadena JSON perque conte tots els camps i valors
     */
    @Test
    public void testToJson(){
        Usuari usuari = new Usuari(1, "user", "user", true, true);
        String json = JsonUtil.toJson(usuari);

        assertNotNull(usuari);
        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"nomUsuari\":\"user\""));
        assertTrue(json.contains("\"password\":\"user\""));
        assertTrue(json.contains("\"isTeacher\":true"));
        assertTrue(json.contains("\"isAdmin\":true"));
    }

}
