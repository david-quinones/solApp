/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package estructurapr;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author pau
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
