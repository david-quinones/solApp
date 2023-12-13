package probesIntegracio;

import com.google.gson.Gson;
import entitats.Empleat;
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
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import persistencia.PersonaDAO;
import servidor.ServidorSocketListener;

/**Classe per fer les proves d'integració de la crida llistarMissatgesRebuts
 *
 * @author Pau Castell Galtes
 */
public class LlistarMissatgesRebutsTest {
    private ServidorSocketListener servidor;
    private Socket socket;
    private static final Logger LOGGER = Logger.getLogger(LlistarMissatgesRebutsTest.class.getName());
    

    
    /**Test per comprovar que es reben els missatges rebuts d'un usuari al fer
     * la crida
     */
    @Test
    public void testLlistarMissatgesRebuts(){
        try {
            System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\pau\\Documents\\GitHub\\solApp\\Destkop\\solAppDesktop\\mykeystore2.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "ioc2023");
            SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket)ssf.createSocket("localhost", 9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("MISSATGES_REBUTS");
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
            int missatgesRebuts = (Integer)retorn.getDades(0, Integer.class);
            assertTrue(missatgesRebuts > 0);
            LOGGER.info("S'han llistat " + missatgesRebuts + " missatges");
            //Comprovem missatges rebuts
            for(int i=1; i<=missatgesRebuts; i++){
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

