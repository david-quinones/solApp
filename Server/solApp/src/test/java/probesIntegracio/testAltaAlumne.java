package probesIntegracio;

import com.google.gson.Gson;
import entitats.Alumne;
import entitats.Empleat;
import entitats.Usuari;
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

/**Classe per fer la prova d'integració de la crida alta alumne
 * 
 *
 * @author Pau Castell Galtes
 */
public class testAltaAlumne {
    private ServidorSocketListener servidor;
    private Socket socket;
    private static final Logger LOGGER = Logger.getLogger(PersonaDAO.class.getName());
    
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
    
    
    /**Test d'integració per comprovar el comportament de la crida alta alumne
     * 
     */
    @Test
    public void testAltaAlumne(){
        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //Preparem dades que s'han de donar d'alta
            Usuari usuari = new Usuari("testAlumneIntegracio", "password", true, false, true);
            Alumne alumne = new Alumne("AlumneIntegracio", "cognomIntegracio", "cognomIntegracio2",
                "2022-08-23", "45645645P", "111111111", "alumneInt@gmail.com", true, false, true);
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("ALTA_ALUMNE");
            peticio.afegirDades(numSessio);
            peticio.afegirDades(alumne);
            peticio.afegirDades(usuari);
            
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
            //Obtenim de la resposta el codi del resultat
            LOGGER.info("Dades rebudes per part del servidor.");
            
            //Si la inserció es correcte el resultat del codiResultat serà 1
            assertEquals(1, retorn.getCodiResultat());
            LOGGER.info("Resultat esperat 1, resultat obtingut : " + retorn.getCodiResultat());
            
            socket.close();
            LOGGER.info("Socket del client tancat.");

        } catch (IOException ex) {
            Logger.getLogger(ConsultaPersonaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**Test per comprovar el comportament en una inserció erronea, utilitzarem un usuari
     * que ja existeix nom usuari = PAU
     */
    @Test
    public void testAltaAlumneErroni(){
        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //Preparem dades que s'han de donar d'alta
            Usuari usuari = new Usuari("PAU", "password", true, false, true);
            Alumne alumne = new Alumne("AlumneIntegracio", "cognomIntegracio", "cognomIntegracio2",
                "2022-08-23", "45645645P", "111111111", "alumneInt@gmail.com", true, false, true);
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("ALTA_ALUMNE");
            peticio.afegirDades(numSessio);
            peticio.afegirDades(alumne);
            peticio.afegirDades(usuari);
            
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
            
            //Si la inserció es erronea el resultat es 0
            assertEquals(0, retorn.getCodiResultat());
            LOGGER.info("Resultat esperat 0, resultat obtingut : " + retorn.getCodiResultat());
            
            socket.close();
            LOGGER.info("Socket del client tancat.");

        } catch (IOException ex) {
            Logger.getLogger(ConsultaPersonaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }

