package sol.app.quinones.solappquinones.Models;

import org.junit.jupiter.api.Test;
import static  org.junit.jupiter.api.Assertions.*;

public class PeticioTest {

    @Test
    public void testGetAndSet(){
        Peticio peticio = new Peticio();

        peticio.setPeticio("Test");

        assertEquals("Test", peticio.getPeticio());
        assertTrue(peticio.getDades().isEmpty());
    }

    @Test
    public void testConstructor(){
        Peticio peticio = new Peticio("TestP");

        assertEquals("TestP", peticio.getPeticio());
    }

    @Test
    public void testAddDades(){
        Peticio peticio = new Peticio();

        peticio.addDades("usuari");
        peticio.addDades("professor");

        assertEquals(2, peticio.getDades().size());
        assertEquals("usuari", peticio.getDades().get(0));
        assertEquals("professor", peticio.getDades().get(1));
    }

    @Test
    public void testDropDades(){
        Peticio peticio = new Peticio();

        peticio.addDades("usuari");
        peticio.dropDades();

        assertTrue(peticio.getDades().isEmpty());
    }


}
