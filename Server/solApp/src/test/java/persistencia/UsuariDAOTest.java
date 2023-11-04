package persistencia;

import entitats.Usuari;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
}

