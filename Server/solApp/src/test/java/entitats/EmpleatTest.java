package entitats;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**Classe per fer el joc de probes de la entitat Empleat
 *
 * @author Pau Castell Galtes
 */
public class EmpleatTest {
    private Empleat empleat;
    
    /**Iniciem l'objecte empleat abans de cada test
     * 
     */
    @Before
    public void setUp(){
        empleat = new Empleat(1, "Pau", "Castell", "Galtes", "1983-08-07", "46797529G",
                "654789452", "pau@gmail.com1", 1, true, "2023-10-28", "9999-12-31");
    }

    /**Test per probar el getter del id
     * 
     */
    @Test
    public void testGetId() {
        assertEquals(1, empleat.getId());
    }

    /**Test per probar le setter del id
     * 
     */
    @Test
    public void testSetId() {
        empleat.setId(2);
        assertEquals(2, empleat.getId());
    }

    /**Test per probar el getter de isActiu
     * 
     */
    @Test
    public void testIsActiu() {
        assertEquals(true, empleat.isActiu());
    }

    /**Test per probar el setter de actiu
     * 
     */
    @Test
    public void testSetActiu() {
        empleat.setActiu(false);
        assertEquals(false, empleat.isActiu());
    }

    /**Test per probar el getter de iniciContracte
     * 
     */
    @Test
    public void testGetIniciContracte() {
        assertEquals("2023-10-28", empleat.getIniciContracte());
    }

    /**Test per probar el setter iniciContracte
     * 
     */
    @Test
    public void testSetIniciContracte() {
        empleat.setIniciContracte("2023-11-11");
        assertEquals("2023-11-11", empleat.getIniciContracte());
    }

    /**Test per probar el getter finalContracte
     * 
     */
    @Test
    public void testGetFinalContracte() {
        assertEquals("9999-12-31", empleat.getFinalContracte());
    }

    /**Test pero probar el setter finalContracte
     * 
     */
    @Test
    public void testSetFinalContracte() {
        empleat.setFinalContracte("2024-01-01");
        assertEquals("2024-01-01", empleat.getFinalContracte());
    }
    
}
