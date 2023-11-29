package sol.app.quinones.solappquinones.Models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe encarregada de realitzar probes unitaries a persona
 *
 * @author david
 */
public class PersonaTest {

    /**
     * Test constructor
     */
    @Test
    public void testContructor(){
        //Persona p = new Persona();
        //assertNotNull(p);
    }

    /**
     * Test gett and setter
     */
    @Test
    public void testGettAndSett(){
        /*
        Persona p = new Persona();
        p.setNom("david");
        p.setCognom1("quinones");
        p.setDni("4040");
        p.setTelefon("972440000");

        assertEquals("david",p.getNom());
        assertEquals("quinones",p.getCognom1());
        assertEquals("4040",p.getDni());
        assertEquals("972440000",p.getTelefon());

         */
    }

    /**
     * Test deserialització a objecte peronsa
     */
    @Test
    public void testDeserialitzacio(){
        String personJSON = "{\"nom\":david,\"cognom1\":quinones,\"dni\":4040,\"telefon\":972440000}";
        Persona persona = Persona.fromJson(personJSON);

        assertEquals("david", persona.getNom());
        assertEquals("quinones", persona.getCognom1());
        assertEquals("4040", persona.getDni());
        assertEquals("972440000", persona.getTelefon());

    }

}
