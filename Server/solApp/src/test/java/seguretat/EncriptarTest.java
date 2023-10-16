package seguretat;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *Classe per fer les probes unitaries de la classe Encriptar
 * @author Pau Castell Galtes
 */
public class EncriptarTest {
    private Encriptar encriptar;
    
    @Before
    public void setUp(){
        encriptar = new Encriptar();
    }

    /**Test per comprobar el correcte funcionament del métod hashPassword
     * 
     * @throws Exception 
     */
    @Test
    public void testHashPassword() throws Exception {
        //Suposem un password
        String password = "password";
        //Calculem el hash
        String hashPassword = encriptar.hashPassword(password);
        //Comprobem que el hash no estigui buit
        assertTrue(!hashPassword.isEmpty());
        //Comprobem que el tamany sigui el apropiat per SHA-256 en Base64
        assertEquals(44, hashPassword.length());   
        
        //Dos passwords iguals tindran el mateix hash
        String password2 = "password";
        String hashPassword2 = encriptar.hashPassword(password2);
        assertEquals(hashPassword, hashPassword2);
        
        //Dos passwords diferents tindran la mateixa mida
        password = "1234";
        hashPassword = encriptar.hashPassword(password);
        assertEquals(44, hashPassword.length());
  
    }
    
}
