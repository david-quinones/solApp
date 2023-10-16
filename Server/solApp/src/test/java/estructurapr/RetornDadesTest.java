package estructurapr;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**Classe per fer les proves de la classe RetornDades
 *
 * @author Pau Castell Galtes
 */
public class RetornDadesTest {
    private RetornDades retornDades;
    
    /**Inicialitzem la variable retornDades abans de cada proba
     * 
     */
    @Before
    public void setUp(){
        retornDades = new RetornDades(1);
    }

    /**Test per comprobar que es retorna correctament el codi del resultat
     * 
     */
    @Test
    public void testGetCodiResultat() {
        assertEquals(retornDades.getCodiResultat(), 1);
    }
    
}
