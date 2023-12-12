package probesIntegracio;

import com.google.gson.Gson;
import entitats.Usuari;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import persistencia.PersonaDAO;
import servidor.ServidorSocketListener;

/**Classe per realitzar la prova d'integració de la crida modificar usuari
 * 
 *
 * @author Pau Castell Galtes
 */
public class ModificarUsuariTest {
    private ServidorSocketListener servidor;
    private Socket socket;
    private static final Logger LOGGER = Logger.getLogger(PersonaDAO.class.getName());
    
    
    /**Iniciem el servidor en un fil diferent per poder fer el test
     * 
     */
    @Before
    public void setUp(){
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
               servidor = new ServidorSocketListener(9999);
               servidor.escoltarClients();
               LOGGER.info("Servidor escoltant clients.");
            }
        });    
        serverThread.start();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown(){
        servidor.tancarServidor();
        LOGGER.info("Servidor tancat.");
    }
    
    
    /**Test per comprovar el comportament en cas d'una modificació vàlida.
     * 
     */
    @Test
    public void testModificarUsuariCorrecte(){
        //Dades que utilitzarem per la prova
        Usuari usuariOriginal = new Usuari(18, "testResposta", "password", true, false, false);
        //Modifiquem nom usuari i password
        Usuari usuariModificat = new Usuari(18, "testRespostaModificat", "Prova", false, true, true);
        //Executem la modificació

        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("MODIFICAR_USUARI");
            peticio.afegirDades(numSessio);
            peticio.afegirDades(usuariModificat);
            
            //Enviem la petició al servidor en format JSON
            Gson gson = new Gson();
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            output.println(gson.toJson(peticio));
            LOGGER.info("Petició enviada al servidor.");
            
            //RESPOSTA DEL SERVIDOR QUE REP EL CLIENT
            //Legim les dades rebudes del servidor
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String llegir = input.readLine();
            RetornDades retorn = gson.fromJson(llegir, RetornDades.class);
            LOGGER.info("Dades rebudes per part del servidor.");
            
            socket.close();
            LOGGER.info("Socket del client tancat.");
            
            System.out.println("Comprovem les dades rebudes");
            assertEquals(1, retorn.getCodiResultat());
            LOGGER.info("Codi del resultat esperat 1, codi rebut: " + retorn.getCodiResultat());
            
            
        } catch (IOException ex) {
            Logger.getLogger(ModificacióPerfilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**Test per comprovar el comportament en cas d'una modificació erronea.
     * 
     */
    @Test
    public void testModificarUsuariError(){
        //Dades que utilitzarem per la prova
        Usuari usuariOriginal = new Usuari(1, "nom_usuari", "password", true, false, true);
        //Modifiquem nom usuari i password
        Usuari usuariModificat = new Usuari(55, "nom_usuariProva", "prova", true, false, true);
        //Executem la modificació

        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("MODIFICAR_USUARI");
            peticio.afegirDades(numSessio);
            peticio.afegirDades(usuariModificat);
            
            //Enviem la petició al servidor en format JSON
            Gson gson = new Gson();
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            output.println(gson.toJson(peticio));
            LOGGER.info("Petició enviada al servidor.");
            
            //RESPOSTA DEL SERVIDOR QUE REP EL CLIENT
            //Legim les dades rebudes del servidor
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String llegir = input.readLine();
            RetornDades retorn = gson.fromJson(llegir, RetornDades.class);
            LOGGER.info("Dades rebudes per part del servidor.");
            
            socket.close();
            LOGGER.info("Socket del client tancat.");
            
            System.out.println("Comprovem les dades rebudes");
            assertEquals(0, retorn.getCodiResultat());
            LOGGER.info("Codi del resultat esperat 0, codi rebut: " + retorn.getCodiResultat());
            
            
        } catch (IOException ex) {
            Logger.getLogger(ModificacióPerfilTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
