package probesIntegracio;

import com.google.gson.Gson;
import entitats.Alumne;
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

/**Classe per realitzar el test d'integració de la crida modificar_alumne
 *
 * @author Pau Castell Galtes
 */
public class testModificarAlumne {
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
    
    /**Test per comprovar el comportament en cas d'una modificació vàlida.
     * 
     */
    @Test
    public void testModificarAlumneCorrecte(){
        //Dades que utilitzarem per la proba
        Alumne alumneOriginal = new Alumne(31, "Juan", "Gomez", "Lopez", "2022-02-15",
                null, "123456789", "juan@gmail.com",1 , true, true, false);
        //Modifiquem el teléfon i menjador ara serà false
        Alumne alumneModificat = new Alumne(31, "Juan", "Gomez", "Lopez", "2022-02-15",
                null, "999999999", "juan@gmail.com",1 , true, false, false);
        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("MODIFICAR_ALUMNE");
            peticio.afegirDades(numSessio);
            peticio.afegirDades(alumneModificat);
            
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
    
    
    /**Test per comprovar el comportament en cas d'una modificació erronea.
     * 
     */
    @Test
    public void testModificarEmpleatError(){
        //Dades que utilitzarem per la proba
        Alumne alumneOriginal = new Alumne(31, "Juan", "Gomez", "Lopez", "2022-02-15",
                null, "123456789", "juan@gmail.com",1 , true, true, false);
        //El id de l'alumne modificat no existeix
        Alumne alumneModificat = new Alumne(89, "Juan", "Gomez", "Lopez", "2022-02-15",
                null, "999999999", "juan@gmail.com",1 , true, false, false);

        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("MODIFICAR_ALUMNE");
            peticio.afegirDades(numSessio);
            peticio.afegirDades(alumneModificat);
            
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

