package resposta;

import entitats.Empleat;
import entitats.Persona;
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
    

    @Test
    public void testConsultaPersona(){
        GenerarResposta generarResposta = new GenerarResposta();
        //Generem la resposta amb el número de sessió
        RetornDades resposta = generarResposta.respostaConsultaPersona("sessioProves");
        //Obtenim les dades de la persona dins la resposta
        Persona persona = (Persona) resposta.getDades(0, Persona.class);
        assertEquals("Pau", persona.getNom());
        //Probem el test amb una sessió incorrecte, codi resultat 0
        resposta = generarResposta.respostaConsultaPersona("sessio_erronea");
        assertEquals(0, resposta.getCodiResultat());
    }
    
    /**Test per comprovar que es retorna la resposta adecuada al modificar les
     * dades de perfil
     */
    @Test
    public void testModificarPersona(){
        Persona original = new Persona(2, "NomPersona", "CognomPersona1", "CognomPersona2",
                "2000-01-01", "1111111H", "999999999", "provapersona@gmail.com");
        Persona modificacio = new Persona(2, "PauCastellGaltes", "CognomPersona1", "CognomPersona2",
                "2000-01-01", "1111111H", "999999999", "provapersona@gmail.com");
        GenerarResposta generarResposta = new GenerarResposta();
        //Generem resposta al solicitar la modificació.
        RetornDades resposta = generarResposta.respostaModificarPersona(modificacio);
        assertEquals(1, resposta.getCodiResultat());
        //Tornem a deixar les dades originals
        resposta = generarResposta.respostaModificarPersona(original);
        assertEquals(1, resposta.getCodiResultat());
    }
    
    //TEST SIMULAT LLISTAR EMPLEATS
    @Test
    public void testLlistarEmpleats(){
        GenerarResposta generarResposta = new GenerarResposta();
        RetornDades retornDades = generarResposta.respostaLlistarEmpleats();
        Empleat empleat1 = (Empleat) retornDades.getDades(0, Empleat.class);
        Empleat empleat2 = (Empleat) retornDades.getDades(1, Empleat.class);
        Empleat empleat5 = (Empleat) retornDades.getDades(4, Empleat.class);
        Empleat empleat8 = (Empleat) retornDades.getDades(7, Empleat.class);
        assertEquals(1, empleat1.getId());
        assertEquals(2, empleat2.getId());
        assertEquals(5, empleat5.getId());
        assertEquals(8, empleat8.getId());
 
    }
}
