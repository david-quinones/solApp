package persistencia;

import entitats.Missatge;
import entitats.Persona;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import utilitats.Utils;

/**Classe per fer les proves unitaries de MissatgeDAO
 *
 * @author Pau Castell Galtes
 */
public class MissatgeDAOTest {
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

    @Test
    public void testAltaMissatge() {
        //Establim les dades per fer la prova
        Persona persona = new Persona();
        persona.setIdPersona(1);
        ArrayList<Persona> destinataris = new ArrayList<>(); 
        destinataris.add(persona);
        String contingut = "Estic fent una prova d'un missatge.";
        String data = Utils.formatDataHoraMinuts(LocalDateTime.now());
        Missatge missatge = new Missatge(2, destinataris, data, contingut);
        
        //Donem d'alta el nou missatge
        MissatgeDAO missatgeDAO = new MissatgeDAO(conexio);
        int resultat = missatgeDAO.altaMissatge(missatge,destinataris.get(0).getIdPersona());
        assertTrue(resultat > 0);

    }
    
}
