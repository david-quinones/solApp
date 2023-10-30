package resposta;

import entitats.Usuari;
import estructurapr.RetornDades;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import seguretat.GestorSessions;

/**Classe per fer probes de la classe GenerarResposta
 *
 * @author Pau Castell Galtes
 */
public class GenerarRespostaTest {
    private Usuari usuariCorrecte;
    private Usuari usuariFail;
    private GestorSessions gestorSessions;
    
    /**Prparació de les proves inicialitzem usuaris i gestorSessions
     * 
     */
    @Before
    public void setUp(){
        usuariCorrecte = new Usuari(1, "nom_usuari", "password", true, false);
        usuariFail = new Usuari(1, "nom_usuari", "error", true, false);
        gestorSessions = GestorSessions.obtindreInstancia();
    }

    
    /**Test per comprobar la correcta resposta del mètode respostaLogin
     * 
     */
    @Test
    public void testRespostaLogin() {
        GenerarResposta generarResposta = new GenerarResposta();
        RetornDades retornDades = generarResposta.respostaLogin(usuariCorrecte);
        //Resposta login correcte
        assertEquals(retornDades.getCodiResultat(), 1);
        //Respota login error
        retornDades = generarResposta.respostaLogin(usuariFail);
        assertEquals(retornDades.getCodiResultat(), 0);
 
    }

    
    /**Test per comprobar la correcta resposta del mètode respostaLogout
     * 
     */
    @Test
    public void testRespostaLogout() {        
        GenerarResposta generarResposta = new GenerarResposta();
        String numSessio = "sessioActiva";
        gestorSessions.agregarSessio(usuariCorrecte, numSessio);
        RetornDades retornDades = generarResposta.respostaLogout(numSessio);
        assertEquals(retornDades.getCodiResultat(), 1);
    }
    
}
