package persistencia;

import entitats.Persona;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
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
            baseDades = new ConexioBBDD();
            conexio = baseDades.conectar();
        } catch (SQLException ex) {
            Logger.getLogger(PersonaDAOTest.class.getName()).log(Level.SEVERE, 
                    "Error al conectar amb la base de dades", ex);
        }

    }
    
    @After
    public void tearDown(){
        baseDades.tancarConexio();
    }
    
    /**Test per comprobar que la persona s'emmagatzema correctament
     * 
     */
    @Test
    public void testAltaPersona() {
            personaDAO = new PersonaDAO(conexio);
            //Simulem un objecte persona
            Persona persona = new Persona("Pau", "Castell", "Galtes", "1983-08-07", "46797529G", "prova", "prova");
            //Executem el métode
            int idPersona = personaDAO.altaPersona(persona);
            //Comprobem el resultat
            assertTrue(idPersona > 0);

    }
    
    /**Test per comprobar que la consulta sobre una persona ha retornat les dades
     * esperades
     */
    @Test
    public void testConsultaPersona(){
        personaDAO = new PersonaDAO(conexio);
        Persona persona = personaDAO.consultaPersona(2);
        assertEquals(1, persona.getId());
        assertEquals("Castell", persona.getCognom1());
    }
    
    /**Test per comprobar que la modificació de les dades d'una Persona s'ha fet
     * correctament
     */
    @Test
    public void testModificarPerfil(){
        //Establim les dades amb les que farem la prova
        Persona original = new Persona(2, "NomPersona", "CognomPersona1", "CognomPersona2",
                "2000-01-01", "1111111H", "999999999", "provapersona@gmail.com");
        Persona modificacio = new Persona(2, "PauCastellGaltes", "CognomPersona1", "CognomPersona2",
                "2000-01-01", "1111111H", "999999999", "provapersona@gmail.com");
        PersonaDAO personaDAO = new PersonaDAO(conexio);
        int filesAfectades = personaDAO.modificarPerfil(modificacio);
        assertTrue(filesAfectades > 0);
        //Tornem a deixar les dades originals
        filesAfectades = personaDAO.modificarPerfil(original);
        assertTrue(filesAfectades > 0);    
    }
    
}
