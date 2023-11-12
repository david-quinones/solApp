package probesIntegracio;

import com.google.gson.Gson;
import entitats.Alumne;
import entitats.Empleat;
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

/**
 *
 * @author Pau Castell Galtes
 */
public class LlistarAlumnesTest {
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
    
    
    /**Test per comprobar que la crida de llistarAlumnes funciona correctament.
     * Es verifica que la cantitat d'alumnes sigui la esperada i els alumnes
     * siguin els mateixos que a la base de dades.
     * 
     */
    @Test
    public void testLlistarAlumnes(){
        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
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
            assertEquals(4, numeroAlumnes);
            LOGGER.info("Número d'alumnes esperats 4, número d'alumnes rebuts: " + numeroAlumnes);
            //Comprobem que els alumnes rebuts són els esperats
            Alumne alumne = (Alumne) retorn.getDades(1, Alumne.class);
            assertEquals(1, alumne.getIdAlumne());
            LOGGER.info("Id = 1 de l'alumne esperat, rebut el id " + alumne.getIdAlumne());
            alumne = (Alumne) retorn.getDades(2, Alumne.class);
            assertEquals(2, alumne.getIdAlumne());
            LOGGER.info("Id = 2 de l'alumne esperat, rebut el id " + alumne.getIdAlumne());
            alumne = (Alumne) retorn.getDades(3, Alumne.class);
            assertEquals(3, alumne.getIdAlumne());
            LOGGER.info("Id = 3 de l'alumne esperat, rebut el id " + alumne.getIdAlumne());
            alumne = (Alumne) retorn.getDades(4, Alumne.class);
            assertEquals(4, alumne.getIdAlumne());
            LOGGER.info("Id = 4 de l'alumne esperat, rebut el id " + alumne.getIdAlumne());

            socket.close();
            LOGGER.info("Socket del client tancat.");

        } catch (IOException ex) {
            Logger.getLogger(ConsultaPersonaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
