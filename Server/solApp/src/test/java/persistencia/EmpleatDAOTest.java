package persistencia;

import entitats.Empleat;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**Classe per fer les proves d'alta, modificació i eliminació de les entitats
 * Empleat
 *
 * @author Pau Castell Galtes
 */
public class EmpleatDAOTest {
    private EmpleatDAO empleatDAO;
    private PersonaDAO personaDAO;
    private Connection conexio;
    private ConexioBBDD baseDades;

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
    
    /**Test per comprobar l'alta d'un nou empleat a la base de dades
     * 
     */
    @Test
    public void testAltaEmpleat() {
        //Id persona associada
        int personaId = 7;
        //Simulem un objecte Empleat
        Empleat empleat = new Empleat("Pau", "Castell", "Galtes", "1983-08-07",
                "46797529G", "prova", "prova", true, "2023-10-23", "9999-12-31");
        empleatDAO = new EmpleatDAO(conexio);
        //Cridem al métode per donar d'alta l'empleat
        int filesAfectades = empleatDAO.altaEmpleat(empleat, personaId);
        //Si l'alta es correcte el número de files afectades será superior a 0.
        assertTrue(filesAfectades > 0);
        
    }
    
    /**Test per comprobar el métode per llistr empleats
     * 
     */
    @Test
    public void testLlistarEmpleats(){
        empleatDAO = new EmpleatDAO(conexio);
        ArrayList<Empleat> empleats= empleatDAO.llistarEmpleats();
        assertEquals(1, empleats.get(0).getIdEmpleat());
        assertEquals(true, empleats.get(0).isActiu());
        assertEquals(2, empleats.get(1).getIdEmpleat());
        assertEquals(3, empleats.get(2).getIdEmpleat());
        assertEquals(4, empleats.get(3).getIdEmpleat());
    }
    
 
    /**Test per verificar el comportament del mètode modificarEmpleat
     * 
     */
    @Test
    public void testModificarEmpleat(){
        Empleat empleatOriginal = new Empleat(1,"Pau", "Castell", "Galtes", "1983-08-07",
                "46797529G", "93703532", "pau@gmail.com", 1,true, "2022-01-01", "2023-12-31");
        //Modifiquem data final del contracte
        Empleat empleatModificat = new Empleat(1,"Pau", "Castell", "Galtes", "1983-08-07",
                "46797529G", "93703532", "pau@gmail.com", 1,true, "2022-01-01", "9999-12-31");
        //Executem la modificació
        EmpleatDAO empleatDAO = new EmpleatDAO(conexio);
        int resultat = empleatDAO.modificarEmpleat(empleatModificat);
        assertEquals(1, resultat);
    }
    
    
}
