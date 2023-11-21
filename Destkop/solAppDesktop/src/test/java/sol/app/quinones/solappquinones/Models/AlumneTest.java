package sol.app.quinones.solappquinones.Models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe encarregada de realitzar probes unitaries i d'integració entre alumne i persona
 *
 * @author david
 */
public class AlumneTest {

    /**
     * Test constructor
     */
    @Test
    public void testContructorBuit(){
        Alumne alumne = new Alumne();
        assertNotNull(alumne);
    }

    /**
     * Test Getter and Setter de la propia clase
     */
    @Test
    public void testGetAndSetClass(){
        Alumne alumne = new Alumne();
        alumne.setIdAlumne(1);
        alumne.setMenjador(false);
        alumne.setAcollida(true);
        alumne.setActiu(true);

        assertEquals(1, alumne.getIdAlumne());
        assertEquals(false, alumne.getIsMenjador());
        assertEquals(true, alumne.getIsAcollida());
        assertEquals(true, alumne.getIsActiu());
    }

    /**
     * Test deserialització d'objecte alumne
     */
    @Test
    public void testFromJson(){
        String alumneJSON = "{\"idAlumne\":1,\"menjador\":false,\"actiu\":true,\"acollida\":false}";
        Alumne alumne = Alumne.fromJson(alumneJSON);

        assertEquals(1, alumne.getIdAlumne());
        assertEquals(false, alumne.getIsMenjador());
        assertEquals(false, alumne.getIsAcollida());
        assertEquals(true, alumne.getIsActiu());
    }

    /*Integration with parent class*/

    /**
     * Test Integració de getter and setters
     */
    @Test
    public void testGetAndSetParentClass(){
        Alumne alumne = new Alumne();
        alumne.setNom("David");
        alumne.setCognom1("Quinones");
        alumne.setCognom2("Lopez");
        alumne.setDni("00000P");
        alumne.setData_naixement("1992");
        alumne.setIdAlumne(1);
        alumne.setMenjador(false);
        alumne.setAcollida(true);
        alumne.setActiu(true);

        assertEquals(1, alumne.getIdAlumne());
        assertEquals(false, alumne.getIsMenjador());
        assertEquals(true, alumne.getIsAcollida());
        assertEquals(true, alumne.getIsActiu());
        assertEquals("David", alumne.getNom());
        assertEquals("Quinones", alumne.getCognom1());
        assertEquals("Lopez", alumne.getCognom2());
        assertEquals("00000P", alumne.getDni());
        assertEquals("1992", alumne.getData_naixement());
        assertEquals(null, alumne.getMail());

    }

    /**
     * Test t'integració de deserialització d'un objecte
     */
    @Test
    public void testFromJsonAllClass(){
        String alumneJSON = "{\"idAlumne\":1,\"menjador\":false,\"actiu\":true,\"acollida\":false,\"nom\":david,\"dni\":000R}";
        Alumne alumne = Alumne.fromJson(alumneJSON);

        assertEquals(1, alumne.getIdAlumne());
        assertEquals(false, alumne.getIsMenjador());
        assertEquals(false, alumne.getIsAcollida());
        assertEquals(true, alumne.getIsActiu());
        assertEquals("david", alumne.getNom());
        assertEquals("000R", alumne.getDni());

    }


}
