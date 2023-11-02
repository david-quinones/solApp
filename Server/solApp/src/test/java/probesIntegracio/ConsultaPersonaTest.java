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

/**Classe per realitzar el test d'integració de la consulta, consulta Persona
 *
 * @author Pau Castell Galtes
 */
public class ConsultaPersonaTest {
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
    
    /**Test per comprobar que es retorna correctament les dades de la persona relacionada
     * amb l'usuari rebut. 
     */
    @Test
    public void testConsultaPersona(){
        
        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("CONSULTA_PERFIL");
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
            Persona persona = (Persona) retorn.getDades(0, Persona.class);
            LOGGER.info("Dades rebudes per part del servidor.");
            
            socket.close();
            LOGGER.info("Socket del client tancat.");
            
            System.out.println("Comprovem les dades rebudes");
            assertEquals(1, retorn.getCodiResultat());
            LOGGER.info("Codi del resultat esperat 1 " + retorn.getCodiResultat());
            assertEquals(1, persona.getId());
            LOGGER.info("Dada esperada 1, dada rebuda " + persona.getId());
            assertEquals("Pau", persona.getNom());
            LOGGER.info("Dada esperada Pau, dada rebuda " + persona.getNom());
            assertEquals("933703532", persona.getTelefon());
            LOGGER.info("Dada esperada 933703532, dada rebuda " + persona.getTelefon());
            
        } catch (IOException ex) {
            Logger.getLogger(ConsultaPersonaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**Test per fer una consulta de les dades de la persona amb un codi de sessió
     * no vàlid.
     */
    @Test
    public void testConsultaPersonaError(){
        try {
            Socket socket2 = new Socket("localhost",9999);
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioErronea";
            PeticioClient peticio  = new PeticioClient("CONSULTA_PERFIL");
            peticio.afegirDades(numSessio);
            
            //Enviem la petició al servidor en format JSON
            Gson gson = new Gson();
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket2.getOutputStream()),true);
            output.println(gson.toJson(peticio));
            LOGGER.info("Petició enviada al servidor.");
            
            //RESPOSTA DEL SERVIDOR QUE REP EL CLIENT
            //Legim les dades rebudes del servidor
            BufferedReader input = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
            String llegir = input.readLine();
            RetornDades retorn = gson.fromJson(llegir, RetornDades.class);
            LOGGER.info("Dades rebudes per part del servidor.");
            
            socket2.close();
            LOGGER.info("Socket del client tancat.");
            
            System.out.println("Comprovem les dades rebudes");
            assertEquals(0, retorn.getCodiResultat());
            LOGGER.info("Resultat d'error esperat 0, resultat rebut " + retorn.getCodiResultat());
            
        } catch (IOException ex) {
            Logger.getLogger(ConsultaPersonaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
