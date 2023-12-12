package seguretat;

import org.junit.Test;
import static org.junit.Assert.*;

/**Classe per realitzar els test de CesarAloritme
 *
 * @author Pau Castell Galtes
 */
public class CesarAlgoritmeTest {
    
    
    /**Test per comprobar la codificació del password amb algoritme cesar
     * 
     */
    @Test
    public void testCodificar(){
        String password = "PauCastell";
        String passwordCodificat = CesarAlgoritme.codificar(password);
        assertNotEquals(password, passwordCodificat);
        System.out.println("Password original: " + password);
        System.out.println("Password codificat: " + passwordCodificat);
    }
    
    
    /**Test per comprovar la correcta descodificació del cesar algoritme
     * 
     */
    @Test
    public void testDescodificar(){
        String passwordCodificat = "TeyGewxipp";
        String passwordDescodificat = CesarAlgoritme.descodificar(passwordCodificat);
        assertEquals("PauCastell", passwordDescodificat);
        System.out.println("Password codificat: " + passwordCodificat);
        System.out.println("Password descodificat: " + passwordDescodificat);
    }
}
