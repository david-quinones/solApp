package probesIntegracio;

import com.google.gson.Gson;
import entitats.Persona;
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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import persistencia.PersonaDAO;
import servidor.ServidorSocketListener;

/**Classe per fer el test d'integració de la modificació d'un perfil
 *
 * @author Pau Castell Galtes
 */
public class ModificacióPerfilTest {
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
    
    /**Test per comprovar la correcta execució de la petició al modificar les dades
     * de perfil.
     */
    @Test
    public void testModificarPerfilCorrecte(){
        Persona original = new Persona(2, "NomPersona", "CognomPersona1", "CognomPersona2",
                "2000-01-01", "1111111H", "999999999", "provapersona@gmail.com");
        Persona modificacio = new Persona(2, "PauCastellGaltes", "CognomPersona1", "CognomPersona2",
                "2000-01-01", "1111111H", "999999999", "provapersona@gmail.com");
        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("MODIFICAR_PERFIL");
            peticio.afegirDades(numSessio);
            peticio.afegirDades(modificacio);
            
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
    
    /**Test per comprobar l'error en cas de rebre dades incorrectes al servidor, 
     * les dades de Persona utilitzada no existeixen
     * 
     */
    
    @Test
    public void testtestModificarPerfilError(){
        //La persona amb id=50 no existeix
        Persona error = new Persona(50, "NomPersona", "CognomPersona1", "CognomPersona2",
                "2000-01-01", "1111111H", "999999999", "provapersona@gmail.com");
        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("MODIFICAR_PERFIL");
            peticio.afegirDades(numSessio);
            peticio.afegirDades(error);
            
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
