package persistencia;

import entitats.Aula;
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
        int eliminarAula = aulaDAO.eliminarAula(8);
        //El resultat tindria que ser erroni
        assertTrue(eliminarAula < 0);
        //El resultat ha de ser correcte
        eliminarAula = aulaDAO.eliminarAula(17);
        assertTrue(eliminarAula > 0);
    }
    
    
   /*Test per comprovar el funcionament de modificar_aula
    
    */
    @Test
    public void testModificarAula(){
        //Noves dades de l'aula
        Aula aula = new Aula();
        aula.setId(2);
        aula.setNomAula("TestModificacio");
        
        AulaDAO aulaDAO = new AulaDAO(conexio);
        int resultat = aulaDAO.modificarAula(aula);
        assertTrue(resultat > 0);
        
        //Aula amb un id que no existeix
        aula.setId(254);
        resultat = aulaDAO.modificarAula(aula);
        assertTrue(resultat < 0);
    }
    
    
    
    /**Test per comprovar el mètode per llistar les aules
     * 
     */
    @Test
    public void testLlistarAules(){
        AulaDAO aulaDAO = new AulaDAO(conexio);
        ArrayList<Aula> llistaAules = new ArrayList<>();
        //Obtenim la llista d'aules de la base de dades
        llistaAules = aulaDAO.llistaAula();
        //Comprovem si està buida
        if(!llistaAules.isEmpty()){
            assertTrue(!llistaAules.isEmpty());
            for(Aula aula: llistaAules){
                
                System.out.println(aula + " Id de l'aula: " + aula.getId());
            }
        }else{
            System.out.println("La llista d'aules està buida");
            assertTrue(llistaAules.isEmpty());
        }
    }
    
}
