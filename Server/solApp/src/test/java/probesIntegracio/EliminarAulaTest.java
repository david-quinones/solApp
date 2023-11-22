package probesIntegracio;

import com.google.gson.Gson;
import entitats.*;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import servidor.ServidorSocketListener;

/**Classe per fer les proves d'integració de la crida eliminar_Aula
 *
 * @author Pau Castell Galtes
 */
public class EliminarAulaTest {
    private ServidorSocketListener servidor;
    private Socket socket;
    private static final Logger LOGGER = Logger.getLogger(AltaAulaTest.class.getName());
    
    /**Preparem el servidora abans de cada test
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
    
    
    /**Test d'integració per comprovar el funcionament correcte de la crida a eliminar_aula
     * amb un resultat correcte
     * 
     */
    @Test
    public void testEliminarAulaCorrecte(){
        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //Preparem dades de l'aula que s'ha d'eliminar
            Aula aula = new Aula();
            aula.setId(5);
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("ELIMINAR_AULA");
            peticio.afegirDades(numSessio);
            peticio.afegirDades(aula);

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
            //Obtenim la resposta del servidor
            LOGGER.info("Dades rebudes per part del servidor.");
            
            //Si s'elimina correctament el resultat es 1
            assertEquals(1, retorn.getCodiResultat());
            LOGGER.info("Resultat esperat 1, resultat obtingut : " + retorn.getCodiResultat());
            
            socket.close();
            LOGGER.info("Socket del client tancat.");

        } catch (IOException ex) {
            Logger.getLogger(ConsultaPersonaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    /**Test per comprovar la crida eliminar_aula amb un resultat erroni
     * 
     */
    @Test
    public void testEliminarAulaError(){
        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //Preparem dades de l'aula que s'ha d'eliminar
            Aula aula = new Aula();
            //Aquesta aula té alumnes associats
            aula.setId(4);
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("ELIMINAR_AULA");
            peticio.afegirDades(numSessio);
            peticio.afegirDades(aula);

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
            //Obtenim la resposta del servidor
            LOGGER.info("Dades rebudes per part del servidor.");
            
            //Si no es pot eliminar el resultat es 0
            assertEquals(0, retorn.getCodiResultat());
            LOGGER.info("Resultat esperat 1, resultat obtingut : " + retorn.getCodiResultat());
            
            socket.close();
            LOGGER.info("Socket del client tancat.");

        } catch (IOException ex) {
            Logger.getLogger(ConsultaPersonaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
