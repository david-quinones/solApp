package probesIntegracio;

import com.google.gson.Gson;
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

/**Classe que realitza el test d'integració de la cirda llistar_usuaris
 * 
 *
 * @author pau
 */
public class LlistarUsuarisTest {
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
    
    
    /**Test per comprobar que la crida de llistarUsuari funciona correctament.
     * Es verifica que la cantitat d'usuaris sigui la esperada i els usuaris
     * siguin els mateixos que a la base de dades.
     * 
     */
    @Test
    public void testLlistarEmpleats(){
        try {
            socket = new Socket("localhost",9999);
            LOGGER.info("Client connectat al servidor");
            
            //PETICIO DEL CLIENT AL SERVIDOR
            String numSessio = "sessioProves";
            PeticioClient peticio  = new PeticioClient("LLISTAR_USUARIS");
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
            

            //Es verifica que el codi del resultat sigui l'esperat
            assertEquals(1, retorn.getCodiResultat());
            LOGGER.info("Codi del resultat espertat = 1, codi rebut: " + retorn.getCodiResultat());
            //Número de elements Usuari rebuts
            int numeroUsuaris = (int) retorn.getDades(0, Integer.class);
            assertEquals(3, numeroUsuaris);
            LOGGER.info("Número d'usuaris esperats 3, número d'usuaris rebuts: " + numeroUsuaris);
            //Comprobem que els empleats rebuts són els esperats
            Usuari usuari = (Usuari) retorn.getDades(1, Usuari.class);
            assertEquals(1, usuari.getId());
            LOGGER.info("Id = 1 de l'usuari esperat, rebut el id " + usuari.getId());
            usuari = (Usuari) retorn.getDades(2, Usuari.class);
            assertEquals(2, usuari.getId());
            LOGGER.info("Id = 2 de l'usuari esperat, rebut el id " + usuari.getId());
            usuari = (Usuari) retorn.getDades(3, Usuari.class);
            assertEquals(18, usuari.getId());
            LOGGER.info("Id = 3 de l'usuari esperat, rebut el id " + usuari.getId());

            socket.close();
            LOGGER.info("Socket del client tancat.");

        } catch (IOException ex) {
            Logger.getLogger(ConsultaPersonaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
