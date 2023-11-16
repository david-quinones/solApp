package persistencia;

import entitats.Alumne;
import entitats.Usuari;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**Classe per fer les proves de la classe UsuariDAO
 *
 * @author Pau Castell Galtes
 */
public class UsuariDAOTest {
    private UsuariDAO usuariDAO;
    private Connection conexio;
    private ConexioBBDD baseDades;
    /**Inicialitzem la classe UsuariDAO abans de cada proba
     * 
     */
    @Before
    public void setUp(){
        try {
            baseDades = new ConexioBBDD();
            conexio = baseDades.conectar();
            usuariDAO = new UsuariDAO(conexio);
        } catch (SQLException ex) {
            Logger.getLogger(PersonaDAOTest.class.getName()).log(Level.SEVERE, 
                    "Error al conectar amb la base de dades", ex);
        }

    }
    
    @After
    public void tearDown(){
        baseDades.tancarConexio();
    }

    

    /**Test per comprobar el métode validarUsuari, aquest métode retorna un usuari
     * amb totes les dades de la BBDD
     * A la base de dades tenim el següent usuari per a fer probes:
     * id= 1, nom_usuari = "nom_usuari", password = "password"(emmagatzemat amb hash),
     * is_admin = true, is_teacher = false
     */
    @Test
    public void testValidarUsuariValid() {
        Usuari usuari1 = new Usuari("nom_usuari", "password");

        Usuari usuariResultat1 = usuariDAO.validarUsuari(usuari1);
        
        assertEquals(usuariResultat1.getId(), 1);
        assertTrue(usuariResultat1.isIsAdmin());
   
    }
    
    /**Test per comprobar el métode en cas de error en la validació.
     * 
     */
    @Test
    public void testValidarUsuariFail(){
        Usuari usuari2 = new Usuari("nom_usuari", "proba_error");
        
        Usuari usuariResultat2 = usuariDAO.validarUsuari(usuari2);
        
        assertNull(usuariResultat2);
    }
    
    /**Test per verificar l'alta d'un usuari a la base de dades.
     * 
     */
    @Test
    public void testAltaUsuari(){
        //Establim dades per fer el test
        Usuari usuari  = new Usuari("testAlta", "password", true, false, true);
        int idPersona = 2;
        //Executem l'alta d'usuari
        UsuariDAO usuariDAO = new UsuariDAO(conexio);
        int resultat = usuariDAO.altaUsuari(usuari, idPersona);
        //Comprovem resultat
        assertEquals(1, resultat);
        
    }   
    
    /**Test per verificar el correcte funcionament del mètode eliminarUsuari
     * 
     */
    @Test
    public void testEliminarUsuari(){
        //L'id de la persona s'obtindrà del empleat rebut com a paràmetre
       int idPersona = 30;
       UsuariDAO usuariDAO = new UsuariDAO(conexio);
       int resultat = usuariDAO.eliminarUsuari(idPersona);
       //Comprovem el resultat obtingut
        assertEquals(1, resultat);
    }
    
    
    /**Test per verificar el comportament del mètode llistar_usuaris
     * 
     */
    @Test
    public void testLlistarUsuaris(){
        usuariDAO = new UsuariDAO(conexio);
        ArrayList<Usuari> llistaUsuaris= usuariDAO.llistarUsuaris();
        assertEquals(1, llistaUsuaris.get(0).getId());
        assertEquals(2, llistaUsuaris.get(1).getId());
        assertEquals(18, llistaUsuaris.get(2).getId());
 
    }
    
    
    /**Test per verificar el comportament del mètode modificar_usuari
     * 
     */
    @Test
    public void testModificarUsuari(){
        //Dades que utilitzarem per la prova
        Usuari usuariOriginal = new Usuari(1, "nom_usuari", "password", true, false, true);
        //Modifiquem nom usuari i password
        Usuari usuariModificat = new Usuari(1, "nom_usuariProva", "prova", true, false, true);
        //Executem la modificació
        UsuariDAO usuariDAO = new UsuariDAO(conexio);
        int filesAfectades = usuariDAO.modificarUsuari(usuariModificat);
        assertEquals(1, filesAfectades);
    }
}

