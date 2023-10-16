package seguretat;

import entitats.Usuari;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**Classe per fer les proves de la classe GestorSessions
 *
 * @author Pau Castell Galtes
 */
public class GestorSessionsTest {
    GestorSessions gestor;
    String numSessio;
    Usuari usuari;
    
    
    @Before
    public void setUp(){
        gestor = GestorSessions.obtindreInstancia();
        numSessio = UUID.randomUUID().toString();
        usuari = new Usuari("nomUsuari", "password");
        
    }
    
    /**Test per comprobar que la instància no es null
     * 
     */
    @Test
    public void testObtindreInstancia() {
        assertNotNull(gestor);
    }

    
    /**Test per comprobar que el número de sessió s'inserta correctament
     * 
     */
    @Test
    public void testAgregarSessio() {
        //Agregar número de sessió
        gestor.agregarSessio(usuari, numSessio);
        
        //Verificar número de sessió
        assertTrue(gestor.verificarSessio(numSessio));
    }

    
    /**Test per validar que la sessió es activa
     * 
     */
    @Test
    public void testVerificarSessio() {
        //Verifica que una sessió no està activa
        assertFalse(gestor.verificarSessio(numSessio));
        
        //Agreguem la sessió
        gestor.agregarSessio(usuari, numSessio);
        //Verifiquem sessió
        assertTrue(gestor.verificarSessio(numSessio));
    }

    
    /**Test per comprobar que una sessió s'elimina correctament
     * 
     */
    @Test
    public void testEliminarSessio() {
        //Agregar sessió
        gestor.agregarSessio(usuari, numSessio);
        //Comprobem que la sessió es activa
        assertTrue(gestor.verificarSessio(numSessio));
        
        //Eliminem la sessió
        gestor.eliminarSessio(numSessio);
        //Comprobem que la sessió ja no está activa
        assertFalse(gestor.verificarSessio(numSessio));
    }
    
}
