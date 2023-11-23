package probesIntegracio;

import com.google.gson.Gson;
import entitats.*;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import servidor.ServidorSocketListener;

/**Classe per fer el test d'integritat de la crida modificar_aula
 *
 * @author Pau Castell Galtes
 */
public class ModificarAulaTest {
    private ServidorSocketListener servidor;
    private Socket socket;
    private static final Logger LOGGER = Logger.getLogger(AltaAulaTest.class.getName());
    
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
    
    
    /**Test d'integració per comprovar el funcionament correcte de la crida a modificar_aula
     * 
     */
    @Test
    public void testModificarAula(){
        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //Preparem dades que s'han de donar d'alta
            //Empleat assignat a l'aula
            Empleat empleat = new Empleat(1,"Pau", "Castell", "Galtes", "1983-08-07",
                "46797529G", "93703532", "pau@gmail.com", 1,true, "2022-01-01", "2023-12-31");
            //Aula que es donarà d'alta
            Aula aula = new Aula();
            aula.setId(4);
            aula.setNomAula("TestIntegracio");
            aula.setEmpleat(empleat);
            //ArrayList d'alumnes
            ArrayList<Alumne> llistaAlumnes = new ArrayList<>();
            Alumne alumne1 = new Alumne(33, "Pedro", "Martinez", "Gutierrez", "2023-04-20", "98765432B", 
                "654321987", "pedro@gmail.com", 3, true, true, true);
            Alumne alumne2 = new Alumne(34, "Laura", "Garcia", "Fernandez", "2022-08-22", null, 
                "789456123", "laura@gmail.com", 4, true, false, false);
            llistaAlumnes.add(alumne1);
            llistaAlumnes.add(alumne2);
            aula.setAlumnes(llistaAlumnes);
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("MODIFICAR_AULA");
            peticio.afegirDades(numSessio);
            peticio.afegirDades(aula);

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
            
            //Si la modificació es correcte el resultat es 1
            assertEquals(1, retorn.getCodiResultat());
            LOGGER.info("Resultat esperat 1, resultat obtingut : " + retorn.getCodiResultat());
            
            socket.close();
            LOGGER.info("Socket del client tancat.");

        } catch (IOException ex) {
            Logger.getLogger(ConsultaPersonaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}