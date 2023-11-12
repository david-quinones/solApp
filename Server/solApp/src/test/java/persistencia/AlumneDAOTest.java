package persistencia;

import entitats.Alumne;
import entitats.Empleat;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**Test per comprovar el comportament dels mètodes de la classe AlumneDAO
 *
 * @author Pau Castell Galtes
 */
public class AlumneDAOTest {
    private Connection conexio;
    private ConexioBBDD baseDades;
    private AlumneDAO alumneDAO;
    
    
     /**Obrim conexió a la base de dades
     * 
     */
    @Before
    public void setUp(){
        try {
            baseDades = new ConexioBBDD();
            conexio = baseDades.conectar();
        } catch (SQLException ex) {
            Logger.getLogger(EmpleatDAOTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**Es tanca la conexió a la base de dades
     * 
     */
    @After
    public void tearDown(){
        baseDades.tancarConexio();
    }

    
     /**Test per comprobar el métode per llistr empleats
     * 
     */
    @Test
    public void testLlistarAlumnes(){
        alumneDAO = new AlumneDAO(conexio);
        ArrayList<Alumne> llistaAlumnes= alumneDAO.llistarAlumnes();
        assertEquals(1, llistaAlumnes.get(0).getIdAlumne());
        assertEquals(true, llistaAlumnes.get(0).isActiu());
        assertEquals(2, llistaAlumnes.get(1).getIdAlumne());
        assertEquals(3, llistaAlumnes.get(2).getIdAlumne());
        assertEquals(4, llistaAlumnes.get(3).getIdAlumne());
    }
    }
    
