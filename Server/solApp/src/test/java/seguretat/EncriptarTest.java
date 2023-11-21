package seguretat;

import java.security.NoSuchAlgorithmException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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
    
    
    
    /**Test per verificar el comportament del mètode clauSimètrica
     * 
     * @throws NoSuchAlgorithmException 
     */
    @Test
    public void testClauSimetrica() throws NoSuchAlgorithmException{
        //Definim dues contrasenyes llegibles
        String passwordTest = "PasswordTest";
        String passwordTest2 = "PasswordTest2";
        //Generem una primera SecretKeySpec
        SecretKeySpec clauGenerada1 = encriptar.clauSimetrica(passwordTest);
        //Comprovem que no sigui null
        assertNotNull(clauGenerada1);
        //Generem una segona clau que tindria que ser idéntica
        SecretKeySpec clauGenerada2 = encriptar.clauSimetrica(passwordTest);
        //Comprovem que les claus siguin iguals
        assertEquals(clauGenerada1, clauGenerada2);
        //Generem una tercera clau diferent
        SecretKeySpec clauGenerada3 = encriptar.clauSimetrica(passwordTest2);
        //Comprovem que el resultat sigui diferent
        assertNotEquals(clauGenerada1, clauGenerada3);
   
    }
    
}
