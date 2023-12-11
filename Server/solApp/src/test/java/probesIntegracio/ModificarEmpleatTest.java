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

/**Test d'integració per comprovar el comportament de la crida modificarEmpleat
 *
 * @author Pau Castell Galtes
 */
public class ModificarEmpleatTest {
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
    public void testModificarEmpleatCorrecte(){
        //Empleat original a la base de dades
        Empleat empleat = new Empleat("TestEmpleatIntegracio", "TestEMpleatIntegracio", "TestIntegracio",
                "1983-02-06", "1111111G", "587458745", "TestIntegracio@gmail.com", true, "2000-01-01", "9999-12-31");
        //Es modifica telefon i final contracte
        Empleat empleatNovesDades = new Empleat(74,"TestEmpleatIntegracioModificat", "TestEMpleatIntegracio", "TestIntegracio",
                "1983-02-06", "1111111G", "587458745", "TestIntegracio@gmail.com",33, true, "2000-01-01", "2024-01-01");

        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("MODIFICAR_EMPLEAT");
            peticio.afegirDades(numSessio);
            peticio.afegirDades(empleatNovesDades);
            
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
        //Empleat original a la base de dades
        Empleat empleatOriginal = new Empleat(1,"Pau", "Castell", "Galtes", "1983-08-07",
                "46797529G", "93703532", "pau@gmail.com", 1,true, "2022-01-01", "2023-12-31");
        //Afegim un id d'empleat inexistent
        Empleat empleatNovesDades = new Empleat(1,"Pau", "Castell", "Galtes", "1983-08-07",
                "46797529G", "645878955", "pau@gmail.com", 45,true, "2022-01-01", "9999-12-31");

        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("MODIFICAR_EMPLEAT");
            peticio.afegirDades(numSessio);
            peticio.afegirDades(empleatNovesDades);
            
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
