package sol.app.quinones.solappquinones.Models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe encarregada de realitzar tests sobre la classe Usuaris per assegurar el correcte funcionament
 *
 * Amb els testos realitzas amb JUnit, comprovem
 *      Correcte funcionament de contructor
 *      Correcte funcionament de getter and setter
 *      Correcte creció de un objecte mitjançant una cadena JSON
 *
 * @author david
 */
public class UsariTest {

    /**
     * Test per comprovar el correcte funcionament del contructor amb parametres
     * Creem una instancia de Usuari i comprovm que els valors han assignat correctament e iguals
     */
    @Test
    public void testContructor(){
        Usuari usuari = new Usuari(1,"IOC", "Ioc2023", true, false);

        assertEquals(1,usuari.getId());
        assertEquals("IOC", usuari.getNomUsuari());
        assertEquals("Ioc2023", usuari.getPassword());
        assertTrue(usuari.getIsTeacher());
        assertFalse(usuari.getIsAdmin());
    }

    /**
     * Test per comprovar el correcte funcionament de getters and stters
     * Instanciem un usuari nou, assigmem valors amb setters i els recuperem amb getter, comprovant que els resultts son els esperats
     */
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
        assertTrue(usuari.getIsTeacher());
        assertTrue(usuari.getIsAdmin());
    }

    /**
     * Test per comprovar el correcte funcionament en la creació de un objecte Usuari a traves de un Json amb un string usuari
     *
     * Es crea una cadena de text, amb la estructura del JSON, es converteix utilitzant el metode de string a Usuari i es comprova que els parametres son els esperats
     */
    @Test
    public void testFromJson(){
        String json = "{\"id\":1992,\"nomUsuari\":\"david\",\"password\":\"david\",\"isAdmin\":true,\"isTeacher\":false}";
        Usuari usuari = Usuari.fromJson(json);

        assertEquals(1992,usuari.getId());
        assertEquals("david", usuari.getNomUsuari());
        assertEquals("david", usuari.getPassword());
        assertFalse(usuari.getIsTeacher());
        assertTrue(usuari.getIsAdmin());
    }

}
