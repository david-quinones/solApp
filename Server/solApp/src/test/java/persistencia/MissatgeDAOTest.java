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
        Persona remitent = new Persona();
        remitent.setIdPersona(2);
        Persona persona = new Persona();
        persona.setIdPersona(1);
        ArrayList<Persona> destinataris = new ArrayList<>(); 
        destinataris.add(persona);
        String contingut = "Estic fent una prova d'un missatge.";
        String data = Utils.formatDataHoraMinuts(LocalDateTime.now());
        Missatge missatge = new Missatge(remitent, destinataris, data, contingut);
        
        //Donem d'alta el nou missatge
        MissatgeDAO missatgeDAO = new MissatgeDAO(conexio);
        int resultat = missatgeDAO.altaMissatge(missatge,destinataris.get(0).getIdPersona());
        assertTrue(resultat > 0);

    }
    
    
    /**Test per comprovar si s'han llistat els missatges rebuts d'una persona
     * 
     */
    @Test
    public void testLlistarMissatgesRebuts(){
        //IdPersona del qual volem llitar els missatges
        int idPersona = 1;
        MissatgeDAO missatgeDAO = new MissatgeDAO(conexio);
        //Obtenim la llista dels missatges
        ArrayList<Missatge> llista = missatgeDAO.llistarMissatgesRebuts(idPersona);
        //Comprovem que la llista no està buida
        assertTrue(!llista.isEmpty());
        for(Missatge missatge: llista){
            System.out.println(missatge.getIdMissatge());
        }
    }
    
}
