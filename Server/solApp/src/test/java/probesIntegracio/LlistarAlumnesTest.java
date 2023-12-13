package probesIntegracio;

import com.google.gson.Gson;
import entitats.Alumne;
import entitats.Empleat;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import java.io.BufferedReader;
import java.io.File;
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

/**Classe per fer les proves d'integració de la crida llistar_alumnes
 *
 * @author Pau Castell Galtes
 */
public class LlistarAlumnesTest {
    private ServidorSocketListener servidor;
    private SSLSocket socket;
    private SSLSocketFactory ssf;
    private static final Logger LOGGER = Logger.getLogger(LlistarAlumnesTest.class.getName());
    

    /**Preparem el servidora abans de cada test
     * 
     */
    /*@Before
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
        try {
            Thread.sleep(100);
            servidor.tancarServidor();
            LOGGER.info("Servidor tancat.");
        } catch (InterruptedException ex) {
            Logger.getLogger(LlistarAlumnesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
  
    
    /**Test per comprobar que la crida de llistarAlumnes funciona correctament.
     * Es verifica que la cantitat d'alumnes sigui la esperada i els alumnes
     * siguin els mateixos que a la base de dades.
     * 
     */
    @Test
    public void testLlistarAlumnes(){
        try {
            System.setProperty("javax.net.ssl.trustStore", "C:\\Users\\pau\\Documents\\GitHub\\solApp\\Destkop\\solAppDesktop\\mykeystore2.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "ioc2023");
            ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = (SSLSocket)ssf.createSocket("localhost", 9999);
            //socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            System.out.println(socket.getInetAddress());
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("LLISTAR_ALUMNES");
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
            //Obtenim de la resposta la llista d'alumnes
            LOGGER.info("Dades rebudes per part del servidor.");
            

            //Es verifica que el codi del resultat sigui l'esperat
            assertEquals(1, retorn.getCodiResultat());
            LOGGER.info("Codi del resultat espertat = 1, codi rebut: " + retorn.getCodiResultat());
            //Número de elements Empleats rebuts
            int numeroAlumnes = (int) retorn.getDades(0, Integer.class);
            assertEquals(7, numeroAlumnes);
            LOGGER.info("Número d'alumnes esperats 7, número d'alumnes rebuts: " + numeroAlumnes);

            
            LOGGER.info("Socket del client tancat.");

        } catch (IOException ex) {
            Logger.getLogger(ConsultaPersonaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
