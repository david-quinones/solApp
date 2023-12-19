package sol.app.quinones.solappquinones.Models;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Classe encarregada de realitzar probes unitaries a Aula
 *
 * @author david
 */
public class AulaTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        Aula aula = new Aula();
        assertNotNull(aula);
    }

    /**
     * Test crear aula amb informació
     */
    @Test
    public void testAulaAmbInfo() {
        List<Alumne> alumnes = new ArrayList<>();
        Alumne alumn = new Alumne();
        alumn.setNom("David");
        Alumne alumn2 = new Alumne();
        alumn2.setNom("David2");
        alumnes.add(alumn2);
        Professor profesor = new Professor();


        Aula aula = new Aula(1, "Aula 101", profesor, alumnes);
        assertEquals(1, aula.getId());
        assertEquals("Aula 101", aula.getNomAula());
        assertEquals(profesor, aula.getEmpleat());
        assertEquals(alumnes, aula.getAlumnes());
    }


    /**
     * Test deserialitzar json a objt aula
     */
    @Test
    public void testFromJson() {
        String json = "{\"id\":3,\"nomAula\":\"Aula B\",\"empleat\":{},\"alumnes\":[{}]}";
        Aula aula = Aula.fromJson(json);

        assertNotNull(aula);
        assertEquals(3, aula.getId());
        assertEquals("Aula B", aula.getNomAula());
    }

}
