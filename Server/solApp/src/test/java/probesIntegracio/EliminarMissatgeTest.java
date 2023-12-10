package probesIntegracio;

import com.google.gson.Gson;
import entitats.Missatge;
import entitats.Persona;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import servidor.ServidorSocketListener;

/**Classe per realitzar el test d'integració de la crida Eliminar_missatge
 *
 * @author Pau Castell Galtes
 */
public class EliminarMissatgeTest {
    private ServidorSocketListener servidor;
    private Socket socket;
    private static final Logger LOGGER = Logger.getLogger(ConsultaPersonaTest.class.getName());
    
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
    
    /**Test d'integració de la crida enviar_missatge
     * 
     */
    @Test
    public void testEliminarMissatge(){
        
        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("ELIMINAR_MISSATGE");
            peticio.afegirDades(numSessio);
            //Dades per la prova
            //Remitent
            Persona remitent = new Persona();
            remitent.setIdPersona(1);
            //Destinatari:
            Persona persona = new Persona();
            persona.setIdPersona(2);
            //Array amb destinataris
            ArrayList<Persona> destinataris = new ArrayList<>();
            destinataris.add(persona);
            //Missatge
            Missatge missatge = new Missatge(destinataris,
                "Missatge prova integració.");
            missatge.setIdMissatge(7);
            missatge.setRemitentPersona(remitent);
            //Afegim les dades a la petició
            peticio.afegirDades(missatge);
            
            //Enviem la petició al servidor en format JSON
            Gson gson = new Gson();
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            output.println(gson.toJson(peticio));
            LOGGER.info("Petició enviada al servidor.");
            
            //RESPOSTA DEL SERVIDOR QUE REP EL CLIENT
            //Llegim les dades rebudes del servidor
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String llegir = input.readLine();
            RetornDades retorn = gson.fromJson(llegir, RetornDades.class);
           
            //Comprovem el resultat de la resposta
            assertEquals(1, retorn.getCodiResultat());
        } catch (IOException ex) {
            Logger.getLogger(ConsultaPersonaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
