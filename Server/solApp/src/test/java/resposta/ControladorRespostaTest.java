package resposta;

import entitats.Usuari;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

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
        peticio = new PeticioClient("LOGIN");
        peticio.afegirDades(usuari);
        ControladorResposta controlador = new ControladorResposta(peticio);
        assertNotNull(controlador.gestionarResposta());
        
        peticio = new PeticioClient("LOGOUT");
        peticio.afegirDades("numSessio");
        ControladorResposta controlador2 = new ControladorResposta(peticio);
        assertNotNull(controlador2.gestionarResposta());
    }
    
}
