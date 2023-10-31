package entitats;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**Classe per fer les proves unitaries de l'entitat Usuari
 *
 * @author Pau Castell Galtes
 */
public class UsuariTest {
    private Usuari usuari;
    /**Métode que instanciará un objecte Usuari abans de cada proba
     * 
     */
    @Before
    public void setUp(){
        
        usuari = new Usuari(1, "nomUsuari", "password", true, false);
    }
    
    
    /**Test per comprobar el getter del id del usuari
     * 
     */
    @Test
    public void testGetId(){
        assertEquals(usuari.getId(), 1);
    }
    
    /**Test per comprobar el métode setId i verificar els canvis
     * 
     */
    @Test
    public void testSetId(){
        usuari.setId(2);
        assertEquals(usuari.getId(), 2);
    }
    
    /**Test per comprobar el getter del nom del usuari
     * 
     */
    @Test
    public void testGetNomUsuari(){
        assertEquals(usuari.getNomUsuari(), "nomUsuari");
    }
    
    /**Test per probar el métode setNomUsuari i verificar els canvis.
     * 
     */
    @Test
    public void testSetNomUsuari(){
        usuari.setNomUsuari("nouNomUsuari");
        assertEquals(usuari.getNomUsuari(), "nouNomUsuari");
    }
    
    /**Test per comprobar el getter del password del usuari
     * 
     */
    @Test
    public void testGetPassword(){
        assertEquals(usuari.getPassword(), "password");
    }
    
    /**Test per probar el métode setNomUsuari i verificar els canvis.
     * 
     */
    @Test
    public void testSetPassword(){
        usuari.setNomUsuari("nouPassword");
        assertEquals(usuari.getNomUsuari(), "nouPassword");
    }
    
    /**Test per comprobar el retorn del métode isIsAdmin
     * 
     */
    @Test
    public void testIsIsAdmin(){
        assertEquals(usuari.isIsAdmin(), true);
    }
    
    /**Test per probar el métode setIsAdmin i verificar el canvis
     * 
     */
    @Test
    public void testSetIsAdmin(){
        usuari.setIsAdmin(false);
        assertEquals(usuari.isIsAdmin(), false);
    }
    
    /**Test per comprobar el retorn del métode isIsTeacher
     * 
     */
    @Test
    public void testIsIsTeacher(){
        assertEquals(usuari.isIsTeacher(), false);
    }
    
    /**Test per probar el métode setIsTeacher i verificar el canvis
     * 
     */
    @Test
    public void testSetIsTeacher(){
        usuari.setIsTeacher(true);
        assertEquals(usuari.isIsTeacher(), true);
    }

}
