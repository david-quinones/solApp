package sol.app.quinones.solappquinones.Models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsariTest {

    @Test
    public void testContructor(){
        Usuari usuari = new Usuari(1,"IOC", "Ioc2023", true, false);

        assertEquals(1,usuari.getId());
        assertEquals("IOC", usuari.getNomUsuari());
        assertEquals("Ioc2023", usuari.getPassword());
        assertTrue(usuari.isTeacher());
        assertFalse(usuari.isAdmin());
    }

    @Test
    public void testGetAndSet(){
        Usuari usuari = new Usuari();

        usuari.setId(20231020);
        usuari.setNomUsuari("david");
        usuari.setPassword("quinones");
        usuari.setTeacher(true);
        usuari.setAdmin(true);

        assertEquals(20231020,usuari.getId());
        assertEquals("david", usuari.getNomUsuari());
        assertEquals("quinones", usuari.getPassword());
        assertTrue(usuari.isTeacher());
        assertTrue(usuari.isAdmin());
    }

    @Test
    public void testFromJson(){
        String json = "{\"id\":1992,\"nomUsuari\":\"david\",\"password\":\"david\",\"isAdmin\":true,\"isTeacher\":false}";
        Usuari usuari = Usuari.fromJson(json);

        assertEquals(1992,usuari.getId());
        assertEquals("david", usuari.getNomUsuari());
        assertEquals("david", usuari.getPassword());
        assertFalse(usuari.isTeacher());
        assertTrue(usuari.isAdmin());
    }

}
