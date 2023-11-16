package probesIntegracio;

import com.google.gson.Gson;
import entitats.Empleat;
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
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import persistencia.PersonaDAO;
import servidor.ServidorSocketListener;

/**Classe per realitzar el test d'integració de la crida EliminarEmpleat
 *
 * @author Pau Castell Galtes
 */
public class EliminarUsuariTest {
    private ServidorSocketListener servidor;
    private Socket socket;
    private static final Logger LOGGER = Logger.getLogger(PersonaDAO.class.getName());
    
    
    /**Iniciem el servidor en un fil diferent al del client
     * per poder fer el test
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
    
    
    /**Test per comprovar la resposta rebuda a la petició d'eliminar un empleat.
     * 
     */
    @Test
    public void testEliminarUsuari(){
        Empleat empleat = new Empleat(1, "testResposta", "cognomResposta1", "cognomResposta2",
                "1983-02-06", "1111111G", "587458745", "resposta@gmail.com",
                1, true, "2000-01-01", "9999-12-31");
        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("ELIMINAR_USUARI");
            peticio.afegirDades(numSessio);
            peticio.afegirDades(empleat);
            
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
            
            //Comprovem el resultat de les dades rebudes
            assertEquals(1, retorn.getCodiResultat());
            LOGGER.info("Resultat esperat 1, resultat obtingut: " + retorn.getCodiResultat());
            
        } catch (IOException ex) {
            Logger.getLogger(ConsultaPersonaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**Test per comprovar error al intentar eliminar un empleat inexistent
     * 
     */
    @Test
    public void testEliminarUsuariError(){
        //Empleat inexistent
        Empleat empleat = new Empleat(55, "testResposta", "cognomResposta1", "cognomResposta2",
                "1983-02-06", "1111111G", "587458745", "resposta@gmail.com",
                55, true, "2000-01-01", "9999-12-31");
        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("ELIMINAR_USUARI");
            peticio.afegirDades(numSessio);
            peticio.afegirDades(empleat);
            
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
            
            //Comprovem el resultat de les dades rebudes
            assertEquals(0, retorn.getCodiResultat());
            LOGGER.info("Resultat esperat 0, resultat obtingut: " + retorn.getCodiResultat());
            
        } catch (IOException ex) {
            Logger.getLogger(ConsultaPersonaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
