package resposta;

import entitats.Usuari;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import seguretat.GestorSessions;

/**Classe per fer les prove de la classe ControladorResposta
 *
 * @author Pau Castell Galtes
 */
public class ControladorRespostaTest {
    private PeticioClient peticio;
    private Usuari usuari;
    private ControladorResposta gestionar;
    
    @Before
    public void setUp(){
        usuari = new Usuari("nomUsuari", "password");

    }

    /**Test per comprobar que el métode GestionarResposta retorna una resposta
     * 
     */
    @Test
    public void testGestionarResposta() {
        GestorSessions gestorSessions = new GestorSessions();
        //Simulem número de sessió activa
        gestorSessions.agregarSessio(usuari, "sessio_activa");
        //Generem petició
        peticio = new PeticioClient("LOGIN");
        peticio.afegirDades(usuari);
        //Comprobem l'odre de la petició per assegurar que entra dins el switch case
        ControladorResposta controlador = new ControladorResposta(peticio);
        controlador.gestionarResposta();
        assertEquals("LOGIN", peticio.getPeticio());
        
        //La mateixa comprobació amb LOGOUT
        peticio = new PeticioClient("LOGOUT");
        peticio.afegirDades("sessio_activa");
        ControladorResposta controlador2 = new ControladorResposta(peticio);
        controlador.gestionarResposta();
        assertEquals("LOGOUT", peticio.getPeticio());
    }
    
    
    /**Test per comprobar si la sessió rebuda existeix abans de gestionar la resposta
     * 
     */
    @Test
    public void testComprobarSessio(){
        //Comprobem que al indicar una sessió incorrecte retorna error
        peticio = new PeticioClient("LOGOUT");
        peticio.afegirDades("sessio_no_valida");
        ControladorResposta controlador = new ControladorResposta(peticio);
        String comprobarSessio = controlador.comprobarSessio(peticio);
        assertEquals("ERROR", comprobarSessio);
    }
    
}
