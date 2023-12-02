package probesIntegracio;

import com.google.gson.Gson;
import entitats.Missatge;
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
import servidor.ServidorSocketListener;

/**Classe per fer la prova d'integració a la crida llistar missatges enviats
 *
 * @author Pau Castell Galtes
 */
public class LlistarMissatgesEnviatsTest {
    private ServidorSocketListener servidor;
    private Socket socket;
    private static final Logger LOGGER = Logger.getLogger(LlistarMissatgesEnviatsTest.class.getName());
    

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
    
    

    /**Test per comprovar que es reben els missatges rebuts d'un usuari al fer
     * la crida
     */
    @Test
    public void testLlistarMissatgesEnviats(){
        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("MISSATGES_ENVIATS");
            peticio.afegirDades(numSessio);
            
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
            //Obtenim de la resposta la llista d'empleats
            LOGGER.info("Dades rebudes per part del servidor.");
            

            //Comprovem el codi del resultat rebut
            assertEquals(1, retorn.getCodiResultat());
            LOGGER.info("Resultat esperat 1 resultat obtingut: " + retorn.getCodiResultat());
            //Comprovem que la llista de missatges sigui més gran de zero
            int missatgesEnviats = (Integer)retorn.getDades(0, Integer.class);
            assertTrue(missatgesEnviats > 0);
            LOGGER.info("S'han llistat " + missatgesEnviats + " missatges");
            //Comprovem missatges rebuts
            for(int i=1; i<=missatgesEnviats; i++){
                Missatge missatge = (Missatge) retorn.getDades(i, Missatge.class);
                System.out.println("Missatge amb id: " + missatge.getIdMissatge());
            }                
            socket.close();
            LOGGER.info("Socket del client tancat.");

        } catch (IOException ex) {
            Logger.getLogger(ConsultaPersonaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
