package sol.app.quinones.solappquinones.Models;

import org.junit.jupiter.api.Test;
import static  org.junit.jupiter.api.Assertions.*;


/**
 * Classe encarregada de realitzar test sobre la classe Peticio per assegurar el correcte funcionament
 *
 * Els test s'encarregen de :
 *      comprobar getter and settter
 *      Contructors
 *      Afegir dades
 *      Buidar dades
 *
 * @author david
 */
public class PeticioTest {

    /**
     * Test que comprova el correcte funcionament del set i del get
     * També comprova que la llista de la clase sgui buida si no te dades
     *
     * Creem una instancia de peticio i assignem un valor, recuperem el valor veient si son identics
     * També recuperam la llisa de la isntancia creada i comprovem que es buida
     */
    @Test
    public void testGetAndSet(){
        Peticio peticio = new Peticio();
        peticio.setPeticio("Test");

        assertEquals("Test", peticio.getPeticio());
        assertTrue(peticio.getDades().isEmpty());
    }

    /**
     * Test que comprova el correcte funcionament dle constructor
     * Instanciem i pasem un valor al constructor, comprovem que ho ha fet correctament
     */
    @Test
    public void testConstructor(){
        Peticio peticio = new Peticio("TestP");

        assertEquals("TestP", peticio.getPeticio());
    }

    /**
     * Test que comprova el correcte funcionament del metodo afegir dades a l'array
     * Intanciem Peticio, afegim dades  comprovcem que te una grandaria concreta, i comprovem que els valors introduits son correctes
     */
    @Test
    public void testAddDades(){
        Peticio peticio = new Peticio();

        peticio.addDades("usuari");
        peticio.addDades("professor");

        assertEquals(2, peticio.getDades().size());
        assertEquals("usuari", peticio.getDades().get(0));
        assertEquals("professor", peticio.getDades().get(1));
    }

    /**
     * Test que comprova el correcte funcionament del metodo de buidar (eliminar) tot el contingut de l'array
     * Instanciem Petició, afegim un valor, i executem el metode, comprovem que esta buit l'array
     */
    @Test
    public void testDropDades(){
        Peticio peticio = new Peticio();

        peticio.addDades("usuari");
        peticio.dropDades();

        assertTrue(peticio.getDades().isEmpty());
    }


}
