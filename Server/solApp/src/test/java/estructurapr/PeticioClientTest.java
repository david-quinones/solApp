package estructurapr;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**Classe per fer les proves de la classe PeticioClient
 *
 * @author Pau Castell Galtes
 */
public class PeticioClientTest {
    private PeticioClient peticio;
    
    /**Inicialitzem la variable amb una ordre
     * 
     */
    @Before
    public void setUp(){
        peticio = new PeticioClient("PROBA");
    }
    

    /**Test per comprobar que es rep correctament l'ordre
     * 
     */
    @Test
    public void testGetPeticio() {
        assertEquals(peticio.getPeticio(), "PROBA");
    }
    
}
