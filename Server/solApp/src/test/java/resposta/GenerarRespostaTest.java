package resposta;

import entitats.Empleat;
import entitats.Persona;
import entitats.Usuari;
import estructurapr.RetornDades;
import java.util.ArrayList;
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
        usuariCorrecte = new Usuari(1, "nom_usuari", "password", true, false,true);
        usuariFail = new Usuari(1, "nom_usuari", "error", true, false,true);
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
    
    /**Test per comprobar que la resposta es genera correctament amb la llista
     * dels empleats
     * 
     */
    @Test
    public void testLlistarEmpleats(){
        GenerarResposta generarResposta = new GenerarResposta();
        RetornDades retornDades = generarResposta.respostaLlistarEmpleats();
        //Comprobem codi resultat correcte
        assertEquals(1, retornDades.getCodiResultat());
        //Comprobem la cantitat d'empleats de la llista
        assertEquals(4, retornDades.getDades(0, Integer.class));
        //comprobem que hi ha 4 empleats
        Empleat empleat = (Empleat) retornDades.getDades(1, Empleat.class);
        assertNotNull(empleat);
        empleat = (Empleat) retornDades.getDades(2, Empleat.class);
        assertNotNull(empleat);
        empleat = (Empleat) retornDades.getDades(3, Empleat.class);
        assertNotNull(empleat);
        empleat = (Empleat) retornDades.getDades(4, Empleat.class);
        assertNotNull(empleat);
 
    }
    
    /**Test per comprovar que la resposta generada al insertar un empleat i l'usuari associat 
     * es la esperada
     * 
     */
    @Test
    public void testAltaEmpleat(){
        //Preparem dades que s'han de donar d'alta
        Usuari usuari = new Usuari("testResposta", "password", true, false, true);
        Empleat empleat = new Empleat("testResposta", "cognomResposta1", "cognomResposta2",
                "1983-02-06", "1111111G", "587458745", "resposta@gmail.com", true, "2000-01-01", "9999-12-31");
        GenerarResposta generarResposta = new GenerarResposta();
        RetornDades retornDades = generarResposta.respostaAltaEmpleat(empleat, usuari);
        //Comprovem que el codi es 1, operació correcte
        assertEquals(1, retornDades.getCodiResultat());
    }
    
    
    /**Test per comprovar que es genera la resposta adient al eliminar un empleat
     * 
     */
    @Test
    public void testEliminarEmpleat(){
        Empleat empleat = new Empleat(1, "testResposta", "cognomResposta1", "cognomResposta2",
                "1983-02-06", "1111111G", "587458745", "resposta@gmail.com",
                1, true, "2000-01-01", "9999-12-31");
        GenerarResposta generarResposta = new GenerarResposta();
        //Generem la resposta
        RetornDades retornDades = generarResposta.respostaEliminarEmpleat(empleat);
        //Comprovem que el codi es correcte
        assertEquals(1, retornDades.getCodiResultat());
        //Comprovem possible error
        Empleat empleat2 = null;
        retornDades = generarResposta.respostaEliminarEmpleat(empleat2);
        assertEquals(0, retornDades.getCodiResultat());
    }
}
