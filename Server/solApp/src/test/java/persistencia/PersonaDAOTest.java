package persistencia;

import entitats.Persona;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**Test per fer les probes de persistencia sobre la entitat Persona
 *
 * @author Pau Castell Galtes
 */
public class PersonaDAOTest {
    private PersonaDAO personaDAO;
    private Connection conexio;
    private ConexioBBDD baseDades;
    
    /**Iniciem personaDAO abans de cada prova
     * 
     */
    @Before
    public void setUp(){
        try {
            conexio = baseDades.conectar();
            personaDAO = new PersonaDAO(conexio);
        } catch (SQLException ex) {
            Logger.getLogger(PersonaDAOTest.class.getName()).log(Level.SEVERE, 
                    "Error al conectar amb la base de dades", ex);
        }

    }
    
    /**Test per comprobar que la persona s'emmagatzema correctament
     * 
     */
    @Test
    public void testAltaPersona() {
        
            //Simulem un objecte persona
            Persona persona = new Persona("Pau", "Castell", "Galtes", "1983-08-07", "46797529G", "prova", "prova");
            //Executem el métode
            int idPersona = personaDAO.altaPersona(persona);
            //Comprobem el resultat
            assertTrue(idPersona > 0);

    }
    
}
