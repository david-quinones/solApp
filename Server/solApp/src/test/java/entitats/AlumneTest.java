package entitats;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**Test per comprovar el comportament de la classe Alumne
 *
 * @author Pau Castell Galtes
 */
public class AlumneTest {
    private Alumne alumne;
    
    @Before
    public void setUp(){
        alumne = new Alumne();
    }
    
    @Test
    public void testIdAlumne(){
        alumne.setIdAlumne(1);
        assertEquals(1, alumne.getIdAlumne());
    }
    
    @Test
    public void testActiu(){
        alumne.setActiu(true);
        assertEquals(true, alumne.isActiu());
    }
    
    @Test
    public void testMenjador(){
        alumne.setMenjador(true);
        assertEquals(true, alumne.isMenjador());
    }
    
    @Test
    public void testAcollida(){
        alumne.setAcollida(true);
        assertEquals(true, alumne.isAcollida());
    }
    
}
