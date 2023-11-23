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

    
     /**Test per comprobar el métode per llistar alumnes
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
    
    
    /**Test per comprovar el funcionament correcte del métode altaAlumne
     * 
     */
    @Test
    public void testAltaAlumne(){
        //Id de la persona associada a l'alumne
        int idPersona = 5;
        //Simulem objecte alumne
        Alumne alumne = new Alumne(true, true, true,1);
        AlumneDAO alumneDAO = new AlumneDAO(conexio);
        //Comprovem el resultat de la execució
        assertEquals(1, alumneDAO.altaAlumne(alumne, idPersona));
        
    }
    
    
    /**Test per comprovar que la modificació s'executa correctament
     * 
     */
    @Test
    public void testModificarAlumne(){
        //Dades que utilitzarem per la proba
        Alumne alumneOriginal = new Alumne(31, "Juan", "Gomez", "Lopez", "2022-02-15",
                null, "123456789", "juan@gmail.com",1 , true, true, false,1);
        //Modifiquem el teléfon i menjador ara serà false
        Alumne alumneModificat = new Alumne(31, "Juan", "Gomez", "Lopez", "2022-02-15",
                null, "999999999", "juan@gmail.com",1 , true, false, false,1);
        //Executem la modificació
        AlumneDAO alumneDAO = new AlumneDAO(conexio);
        int filesAfectades = alumneDAO.modificarAlumne(alumneModificat);
        assertEquals(1, filesAfectades);
    }
    
    
    /**Test per comprovar el mètode llistarAlumnesAula
     * 
     */
    @Test
    public void testLlistarAlumnesAula(){
        //Id de l'aula on buscarem el alumnes
        int idAula = 54;
        AlumneDAO alumneDAO = new AlumneDAO(conexio);
        //ArrayList on guardarem les dades
        ArrayList<Alumne> llistaAlumnes = new ArrayList<>();
        //Afegim els alumnes de l'aula
        llistaAlumnes = alumneDAO.llistaAlumnesAula(idAula);
        //Comprovem que la llista no està buida        
        if(!llistaAlumnes.isEmpty()){
            for(Alumne alumne: llistaAlumnes){
                System.out.println(alumne);
            }
            assertTrue(!llistaAlumnes.isEmpty());
        }else{
            System.out.println("L'aula no té alumnes associats");
            assertTrue(llistaAlumnes.isEmpty());
        }
    }
    }
    
