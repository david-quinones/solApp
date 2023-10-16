package persistencia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**Clase per fer les proves de la classe ConexioBBDD
 *
 * @author Pau Castell Galtes
 */
public class ConexioBBDDTest {
    private ConexioBBDD conexioBBDD;
    private Connection conexio;
    
    /**Inicialitzem la conexioBBDD abans de cada proba
     * 
     */
    @Before
    public void setUp(){
        conexioBBDD = new ConexioBBDD();
    }
    
    /**Tanca la conexió després de cada proba
     * 
     */
    @After
    public void tearDown(){
        conexioBBDD.tancarConexio();
    }

    @Test
    public void testConectar(){
        try {
            conexio = conexioBBDD.conectar();
            
            assertNotNull(conexio);
            assertFalse(conexio.isClosed());
            
        } catch (SQLException ex) {
            fail("Error en la conexió " + ex.getMessage());
        }
    }

    @Test
    public void testTancarConexio() {
        try {
            conexio = conexioBBDD.conectar();
            
            assertNotNull(conexio);
            
            conexioBBDD.tancarConexio();
            
            assertTrue(conexio.isClosed());
            
        } catch (SQLException ex) {
            fail("Error en la conexió o tancament " + ex.getMessage());
        }
    }
    
}
