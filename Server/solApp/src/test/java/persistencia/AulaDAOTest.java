package persistencia;

import entitats.Aula;
import entitats.Empleat;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Pau Castell Galtes
 */
public class AulaDAOTest {
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
    
    
    /**Test per comprovar el comportament del mètode altaAula
     * 
     */
    @Test
    public void testAltaAula() {
        Aula aula = new Aula();
        //Establim nom de l'aula
        aula.setNomAula("AltaDAO");
        //Establim un empleat
        Empleat empleatAula = new Empleat(1,"Pau", "Castell", "Galtes", "1983-08-07",
                "46797529G", "93703532", "pau@gmail.com", 1,true, "2022-01-01", "2023-12-31");
        aula.setEmpleat(empleatAula);
        
        AulaDAO aulaDAO = new AulaDAO(conexio);
        int resultat = aulaDAO.altaAula(aula);
        assertTrue(resultat > 0); 
        
        //Alta sense empleat
        Aula aula2 = new Aula("NoProfessor");
        resultat = aulaDAO.altaAula(aula2);
        assertTrue(resultat > 0);
    }
    
    
    /**Test per comprovar el correcte funcinament de eliminar_aula
     * 
     */
    @Test
    public void testEliminarAula(){
        AulaDAO aulaDAO = new AulaDAO(conexio);
        //Afegim id aula amb alumnes associats
        int eliminarAula = aulaDAO.eliminarAula(4);
        //El resultat tindria que ser erroni
        assertTrue(eliminarAula < 0);
        //El resultat ha de ser correcte
        eliminarAula = aulaDAO.eliminarAula(3);
        assertTrue(eliminarAula > 0);
    }
    
}
