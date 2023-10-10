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
public class DadesPRTest {
    private DadesPR dadesPR;
    
    /**Inicialitzem l'objecte dadesPR abans de cada proba
     * 
     */
    @Before
    public void setUp(){
        dadesPR = new DadesPR();
    }

    /**Test per comprobar que les dades s'introdueixen correctament
     * 
     */
    @Test
    public void testAfegirDades() {
        dadesPR.afegirDades("Dada1");
        dadesPR.afegirDades("Dada2");
        
        assertEquals(dadesPR.getDades(0, String.class),"Dada1");
        assertEquals(dadesPR.getDades(1, String.class),"Dada2");
    }

    /**Test per comprobar el retorn correcte de les dades.
     * 
     */
    @Test
    public void testGetDades() {
        dadesPR.afegirDades("Dades de proba");
        
        assertEquals(dadesPR.getDades(0, String.class), "Dades de proba"); 
    }

    /**Test per comprobar que les dades primteives s'afegeixen correctament
     * 
     */
    @Test
    public void testAfegirDadesPrimitives() {
        dadesPR.afegirDadesPrimitives(1);
        dadesPR.afegirDadesPrimitives(true);
        
        assertEquals(dadesPR.getDades(0, Integer.class), 1);
        assertEquals(dadesPR.getDades(1, Boolean.class), true);
    }
    
}
