package sol.app.quinones.solappquinones.Service;

import org.junit.jupiter.api.Test;
import sol.app.quinones.solappquinones.Models.Usuari;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;

import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilTest {

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
