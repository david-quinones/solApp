package probesIntegracio;

import com.google.gson.Gson;
import entitats.Alumne;
import entitats.Aula;
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

/**Classe per fer les proves d'integració de la crida llistar_aules
 *
 * @author Pau Castell Galtes
 */
public class LlistarAulesTest {
    private ServidorSocketListener servidor;
    private Socket socket;
    private static final Logger LOGGER = Logger.getLogger(LlistarAulesTest.class.getName());
    

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
    
    
    /**Test per comprobar que la crida de llistarAules funciona correctament.
     * 
     * 
     */
    @Test
    public void testLlistarAules(){
        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("LLISTAR_AULES");
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
           
            //Comprovem el resultat de la resposta
            assertEquals(1, retorn.getCodiResultat());
            LOGGER.info("Resultat esperat 1 resultat obtingut: " + retorn.getCodiResultat());
            //Comprovem la quantitat d'aules rebudes
            assertEquals(3, retorn.getDades(0, Integer.class));
            LOGGER.info("Resultat esperat 3 resultat obtingut: " + retorn.getDades(0, Integer.class));
            //Obtenim les aules
            Aula aula1 = (Aula) retorn.getDades(1, Aula.class);
            ArrayList<Alumne> llistaAlumnes = new ArrayList<>();
            //Imprimim el resultat
            System.out.println("\nAula 1: id = " +aula1.getId() + " nom: " + aula1.getNomAula());
            System.out.println("Empleat = " + aula1.getEmpleat());
            llistaAlumnes = aula1.getAlumnes();
            System.out.println("Alumnes:");
            for(Alumne alumne: llistaAlumnes){
                System.out.println(alumne);
            }
            System.out.println();
            
            Aula aula2 = (Aula) retorn.getDades(2, Aula.class);
            //Imprimim el resultat
            System.out.println("Aula 2: id = " +aula2.getId() + " nom: " + aula2.getNomAula());
            System.out.println("Empleat = " + aula2.getEmpleat());
            llistaAlumnes = aula2.getAlumnes();
            System.out.println("Alumnes:");
            for(Alumne alumne: llistaAlumnes){
                System.out.println(alumne);
            }
            System.out.println();
            
            Aula aula3 = (Aula) retorn.getDades(3, Aula.class);
            //Imprimim el resultat
            System.out.println("Aula 3: id = " +aula3.getId() + " nom: " + aula3.getNomAula());
            System.out.println("Empleat = " + aula3.getEmpleat());
            llistaAlumnes = aula3.getAlumnes();
            System.out.println("Alumnes:");
            for(Alumne alumne: llistaAlumnes){
                System.out.println(alumne);
            }
            System.out.println();          

            socket.close();
            LOGGER.info("Socket del client tancat.");

        } catch (IOException ex) {
            Logger.getLogger(ConsultaPersonaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
