package resposta;

import entitats.Alumne;
import entitats.Aula;
import entitats.Empleat;
import entitats.Missatge;
import entitats.Persona;
import entitats.Usuari;
import estructurapr.RetornDades;
import java.time.LocalDateTime;
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
        usuariCorrecte = new Usuari(18, "testResposta", "password", true, false,true);
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
        //comprobem part de la llista
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
        Usuari usuari = new Usuari("testResposta10", "password", true, false, true);
        Empleat empleat = new Empleat("testResposta", "cognomResposta1", "cognomResposta2",
                "1983-02-06", "1111111T", "587458746", "resposta@gmail.com", true, "2000-01-01", "9999-12-31");
        GenerarResposta generarResposta = new GenerarResposta();
        RetornDades retornDades = generarResposta.respostaAltaEmpleat(empleat, usuari);
        //Comprovem que el codi es 1, operació correcte
        assertEquals(1, retornDades.getCodiResultat());
    }
    
    
    /**Test per comprovar que es genera la resposta adient al desactivar un usuari
     * 
     */
    @Test
    public void testEliminarUsuari(){
        Persona persona = new Persona(1, "testResposta", "cognomResposta1", "cognomResposta2",
                "1983-02-06", "1111111G", "587458745", "resposta@gmail.com");
        GenerarResposta generarResposta = new GenerarResposta();
        //Generem la resposta
        RetornDades retornDades = generarResposta.respostaEliminarUsuari(persona);
        //Comprovem que el codi es correcte
        assertEquals(1, retornDades.getCodiResultat());
        //Comprovem possible error
        Persona persona2 = null;
        retornDades = generarResposta.respostaEliminarUsuari(persona2);
        assertEquals(0, retornDades.getCodiResultat());
    }
    
    
    /**Test per verificar el comportament del mètode que genera la resposta modificarEmpleat
     * 
     */
    @Test
    public void testModificarEmpleat(){
        //Empleat original a la base de dades
        Empleat empleatOriginal = new Empleat(1,"Pau", "Castell", "Galtes", "1983-08-07",
                "46797529G", "93703532", "pau@gmail.com", 1,true, "2022-01-01", "2023-12-31");
        //Es modifica telefon i final contracte
        Empleat empleatNovesDades = new Empleat(1,"Pau", "Castell", "Galtes", "1983-08-07",
                "46797529G", "645878955", "pau@gmail.com", 1,true, "2022-01-01", "9999-12-31");
        //Generem la resposta
        GenerarResposta generarResposta = new GenerarResposta();
        RetornDades retornDades = generarResposta.respostaModificarEmpleat(empleatOriginal);
        //Comprovem que el resultat es l'esperat
        assertEquals(1, retornDades.getCodiResultat());
        //Fem el mateix amb un empleat que no existeix
        Empleat empleatError= new Empleat(55,"Pau", "Castell", "Galtes", "1983-08-07",
                "46797529G", "645878955", "pau@gmail.com", 1,true, "2022-01-01", "9999-12-31");
        retornDades = generarResposta.respostaModificarEmpleat(null);
        assertEquals(0, retornDades.getCodiResultat()); 
    }
    
    
    /**Test per comprobar que la resposta es genera correctament amb la llista
     * dels alumnes
     * 
     */
    @Test
    public void testLlistarAlumnes(){
        GenerarResposta generarResposta = new GenerarResposta();
        RetornDades retornDades = generarResposta.respostaLlistarAlumnes();
        //Comprobem codi resultat correcte
        assertEquals(1, retornDades.getCodiResultat());
        //comprobem part de llista
        Alumne alumne = (Alumne) retornDades.getDades(1, Alumne.class);
        assertNotNull(alumne);
        alumne = (Alumne) retornDades.getDades(2, Alumne.class);
        assertNotNull(alumne);
        alumne = (Alumne) retornDades.getDades(3, Alumne.class);
        assertNotNull(alumne);
        alumne = (Alumne) retornDades.getDades(4, Alumne.class);
        assertNotNull(alumne);
    }
    
    
    /**Test per comprovar el comportament al generar una resposta alta alumne.
     * 
     */
    @Test
    public void testAltaAlumne(){
        //Preparem dades que s'han de donar d'alta
        Usuari usuari = new Usuari("testAlumne", "password", true, false, true);
        Alumne alumne = new Alumne("testAlumne", "cognomAlumne", "cognomAlumne2",
                "2021-08-06", "8748555H", "111111111", "alumne@gmail.com", true, false, false,1);
        GenerarResposta generarResposta = new GenerarResposta();
        RetornDades retornDades = generarResposta.respostaAltaAlumne(alumne, usuari);
        //Comprovem que el codi es 1, operació correcte
        assertEquals(1, retornDades.getCodiResultat());
    }
    
    
    /**Test per comprovar la resposta generada a la crida modificar_alumne
     * 
     */
    @Test
    public void testModificarAlumne(){
        //Dades que utilitzarem per la prova
        Alumne alumneOriginal = new Alumne(31, "Juan", "Gomez", "Lopez", "2022-02-15",
                null, "123456789", "juan@gmail.com",1 , true, true, false,1);
        //Modifiquem el teléfon i menjador ara serà false
        Alumne alumneModificat = new Alumne(31, "Juan", "Gomez", "Lopez", "2022-02-15",
                null, "999999999", "juan@gmail.com",1 , true, false, false,1);
        //Generem la resposta
        GenerarResposta generarResposta = new GenerarResposta();
        RetornDades retornDades = generarResposta.respostaModificarAlumne(alumneModificat);
        //Comprovem que el resultat es l'esperat
        assertEquals(1, retornDades.getCodiResultat());
        //Fem el mateix amb un alumne que no existeix
        Alumne alumneError= new Alumne(80, "Juan", "Gomez", "Lopez", "2022-02-15",
                null, "123456789", "juan@gmail.com",1 , true, true, false,1);
        retornDades = generarResposta.respostaModificarAlumne(alumneError);
        assertEquals(0, retornDades.getCodiResultat()); 
    }
    
    
    
    /**Test per comprovar el comportament del mètode respostaModificarUsuari
     * 
     */
    @Test
    public void testModificarUsuari(){
        //Dades que utilitzarem per la prova
        Usuari usuariOriginal = new Usuari(1, "nom_usuari", "password", true, false, true);
        //Modifiquem nom usuari i password
        Usuari usuariModificat = new Usuari(1, "nom_usuariProva", "prova", true, false, true);
        //Generem la resposta
        GenerarResposta generarResposta = new GenerarResposta();
        RetornDades retornDades = generarResposta.respostaModificarUsuari(usuariModificat);
        //Comprovem que el resultat es l'esperat
        assertEquals(1, retornDades.getCodiResultat());
        //Fem el mateix amb un usuari que no existeix
        Usuari usuariError = new Usuari(155, "nom", "password", true, false, true);
        retornDades = generarResposta.respostaModificarUsuari(usuariError);
        assertEquals(0, retornDades.getCodiResultat()); 
    }
    
    
    /**Test per comprovar el funcionament del mètode respostaLlistarUsuari
     * 
     */
    @Test
    public void testLlistarUsuaris(){
        GenerarResposta generarResposta = new GenerarResposta();
        RetornDades retornDades = generarResposta.respostaLlistarUsuaris();
        //Comprobem codi resultat correcte
        assertEquals(1, retornDades.getCodiResultat());
        //comprobem part de la llista
        Usuari usuari = (Usuari) retornDades.getDades(1, Usuari.class);
        assertNotNull(usuari);
        usuari = (Usuari) retornDades.getDades(2, Usuari.class);
        assertNotNull(usuari);
        usuari = (Usuari) retornDades.getDades(3, Usuari.class);
        assertNotNull(usuari);

    }
    
    
    //***************************** TEA 4 *********************************************
    
    @Test
    public void testAltaAula(){
        GenerarResposta generarResposta = new GenerarResposta();
        //Es crea una aula
        Aula aula = new Aula();
        aula.setNomAula("TestResposta");
        Empleat empleat = new Empleat(1,"Pau", "Castell", "Galtes", "1983-08-07",
                "46797529G", "93703532", "pau@gmail.com", 1,true, "2022-01-01", "2023-12-31");
        aula.setEmpleat(empleat);
        //ArrayList d'alumnes
        ArrayList<Alumne> llistaAlumnes = new ArrayList<>();
        Alumne alumne1 = new Alumne(33, "Pedro", "Martinez", "Gutierrez", "2023-04-20", "98765432B", 
                "654321987", "pedro@gmail.com", 3, true, true, true);
        Alumne alumne2 = new Alumne(34, "Laura", "Garcia", "Fernandez", "2022-08-22", null, 
                "789456123", "laura@gmail.com", 4, true, false, false);
        llistaAlumnes.add(alumne1);
        llistaAlumnes.add(alumne2);
        aula.setAlumnes(llistaAlumnes);
        
        RetornDades resposta = generarResposta.respostaAltaAula(aula);
        assertEquals(1, resposta.getCodiResultat());
        
    }
    
    
    
    /**Test per comprovar la resposta de la crida eliminar_aula
     * 
     */
    @Test
    public void testEliminarAula(){
        GenerarResposta generarResposta = new GenerarResposta();
        Aula aula = new Aula();
        aula.setId(4);
        //No es pot esborrar l'aula
        RetornDades resposta = generarResposta.respostaEliminarAula(aula);
        assertEquals(0, resposta.getCodiResultat());
        //L'aula es pot esborrar
        aula.setId(5);
        resposta = generarResposta.respostaEliminarAula(aula);
        assertEquals(1, resposta.getCodiResultat());
    }
    
    
    /**Test per comprovar la resposta a la crida modificar_aula
     * 
     */
    @Test
    public void testModificarAula(){
        GenerarResposta generarResposta = new GenerarResposta();
        //Dades de la nova Aula
        Aula aula = new Aula();
        aula.setNomAula("ProvaModificar");
        aula.setId(2);
        Empleat empleat = new Empleat();
        empleat.setIdEmpleat(1);
        aula.setEmpleat(empleat);
        //Generem la resposta
        RetornDades resposta = generarResposta.respostaModificarAula(aula);
        assertEquals(1, resposta.getCodiResultat());
    }
    
    
    
    /**Test per comprovar la resposta a la crida llista_aules
     * 
     */
    @Test
    public void testLlistaAules(){
        GenerarResposta generarResposta = new GenerarResposta();
        RetornDades resposta = generarResposta.respostaLlistaAules();
        //Comprovem que el resultat es l'esperat en cas de retornar una llista d'aules
        assertEquals(1, resposta.getCodiResultat());
        assertTrue((Integer)resposta.getDades(0, Integer.class) > 0);
    }
    
    
    /**Test per comprovar la resposta a la crida enviar_missatge
     * 
     */
    @Test
    public void testEnviarMissatge(){
        GenerarResposta generarResposta = new GenerarResposta();
        //Dades per la prova
        //Destinataris:
        Persona persona = new Persona();
        persona.setIdPersona(1);
        Persona persona2 = new Persona();
        persona2.setIdPersona(2);
        //Array amb destinataris
        ArrayList<Persona> destinataris = new ArrayList<>();
        destinataris.add(persona);
        destinataris.add(persona2);
        //Missatge
        Missatge missatge = new Missatge(destinataris, 
                "Prova enviar missatge diferents destinataris");
        //Generem resposta
        RetornDades resposta = generarResposta.respostaEnviarMissatge(missatge,"sessioProves");
        //Comprovem el resultat
        assertEquals(1, resposta.getCodiResultat());
        
    }
}
