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
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
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
    

    
    /**Test d'integració de la crida eliminar_missatge
     * 
     */
    @Test
    public void testEliminarMissatge(){
        
        try {
            System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\pau\\Documents\\GitHub\\solApp\\Destkop\\solAppDesktop\\mykeystore2.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "ioc2023");
            SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket)ssf.createSocket("localhost", 9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("ELIMINAR_MISSATGE");
            peticio.afegirDades(numSessio);
            //Dades per la prova
            //Remitent
            Persona remitent = new Persona();
            remitent.setIdPersona(2);
            //Destinatari:
            Persona persona = new Persona();
            persona.setIdPersona(1);
            //Array amb destinataris
            ArrayList<Persona> destinataris = new ArrayList<>();
            destinataris.add(persona);
            //Missatge
            Missatge missatge = new Missatge(destinataris,
                "Missatge prova integració.");
            missatge.setIdMissatge(8);
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

