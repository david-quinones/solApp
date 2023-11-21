package sol.app.quinones.solappquinones.Models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe encarregada de realitzar probes unitaries i d'integració entre alumne i persona
 */
public class ProfessorTest {

    /**
     * Test constructor
     */
    @Test
    public void testContructorBuit(){
        Professor p = new Professor();
        assertNotNull(p);
    }

    /**
     * Test contruct parameteres
     */
    @Test
    public void testContructorParms(){
        Professor p = new Professor(5, "1992", "2018", true);
        assertNotNull(p);
        assertEquals(5, p.getIdEmpleat());
        assertEquals("1992", p.getIniciContracte());
        assertEquals("2018", p.getFinalContracte());
        assertTrue(p.isActiu());
    }

    /**
     * Test deserialitzar un objecte professor
     */
    @Test
    public void testDeserialitzacio(){
        String jsonProfe = "{\"idEmpleat\":5,\"iniciContracte\":\"1992\",\"finalContracte\":\"2018\",\"actiu\":true}";
        Professor professor = Professor.fromJson(jsonProfe);

        assertEquals(5, professor.getIdEmpleat());
        assertEquals("1992", professor.getIniciContracte());
        assertEquals("2018", professor.getFinalContracte());
        assertTrue(professor.isActiu());
    }

}
