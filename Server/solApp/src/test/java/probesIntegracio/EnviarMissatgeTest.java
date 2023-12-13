package probesIntegracio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entitats.Missatge;
import entitats.Persona;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import servidor.ServidorSocketListener;

/**Classe per comprovar el funcionament de la crida enviar_missatge
 *
 * @author Pau Castell Galtes
 */
public class EnviarMissatgeTest {
    private ServidorSocketListener servidor;
    private Socket socket;
    private static final Logger LOGGER = Logger.getLogger(ConsultaPersonaTest.class.getName());
    

    /**Test d'integració de la crida enviar_missatge
     * 
     */
    @Test
    public void testEnviarMissatge(){
        
        try {
            System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\pau\\Documents\\GitHub\\solApp\\Destkop\\solAppDesktop\\mykeystore2.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "ioc2023");
            SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket)ssf.createSocket("localhost", 9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("ENVIAR_MISSATGE");
            peticio.afegirDades(numSessio);
            //Dades per la prova
            //Destinataris:
            Persona persona = new Persona();
            persona.setIdPersona(5);
            Persona persona2 = new Persona();
            persona2.setIdPersona(2);
            //Array amb destinataris
            ArrayList<Persona> destinataris = new ArrayList<>();
            destinataris.add(persona);
            destinataris.add(persona2);
            //Missatge
            Missatge missatge = new Missatge(destinataris,
                "Missatge prova integració.");
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
