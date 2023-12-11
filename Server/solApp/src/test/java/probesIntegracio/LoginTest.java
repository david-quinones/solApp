
package probesIntegracio;

import com.google.gson.Gson;
import entitats.Usuari;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import java.io.*;
import java.net.Authenticator;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import seguretat.CesarAlgoritme;
import servidor.ServidorSocketListener;

/**Test per comprobar la crida a la petició LOGIN al servidor
 *
 * @author Pau Castell Galtes
 */
public class LoginTest {
    private ServidorSocketListener servidor;
    private Socket socket;
    private String numSessio;
    
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
    }
    
   
    /**Test per comprobar que es realitza una comprobació del login correcte
     * amb un usuari que existeix a la base de dades. L'usuari que s'utilitzara
     * serà el següent: 
     * id: 1
     * Nom d'usuari: "nom_usuari"
     * password: "password" (hash:XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=)
     * isAdmin: true
     * isTeacher: false
     */
    @Test
    public void testLoginCorrecte(){
        
        try {

            //Socket del client
            socket = new Socket("localhost",9999);
            //PETICIO DEL CLIENT AL SERVIDOR
            //Usuari que intentarem validar
            //Codifiquem password
            String password = CesarAlgoritme.codificar("password");
            Usuari usuari = new Usuari("testConsulta",password);
            //Generem la petició que farem al servidor
            PeticioClient peticio = new PeticioClient("LOGIN");
            peticio.afegirDades(usuari);
            //Enviem la petició al servidor en JSON
            Gson gson = new Gson();
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            output.println(gson.toJson(peticio));
           
            //RESPOSTA DEL SERVIDOR QUE REP EL CLIENT
            //Llegim la resposta revuda
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String llegir = input.readLine();
            RetornDades retorn = gson.fromJson(llegir, RetornDades.class);
            Usuari usuariRetorn = (Usuari) retorn.getDades(0, Usuari.class);
            numSessio = (String) retorn.getDades(1, String.class);

            socket.close();
            //Comprobem que les dades rebudes coincideixen amb un login correcte
            //Comprobem primer el codi del resultat, 1 en cas de verificació correcte
            assertEquals("Codi de resultat correcte", 1, retorn.getCodiResultat());
            assertEquals("Id de l'usuari retornat",22, usuariRetorn.getId());
            assertNotNull("Número de sessió no null",numSessio);

        } catch (IOException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    
    /**En aquest test comprobem l'exexució en cas d'una validació incorrecte.
     * 
     */
    @Test
    public void testLoginError(){
        
            try {

            //Socket del client
            socket = new Socket("localhost",9999);           
            //PETICIO DEL CLIENT AL SERVIDOR
            //Usuari que intentarem validar amb un password incorrecte
            String password = CesarAlgoritme.codificar("passwordIncorrecte");
            Usuari usuari = new Usuari("testConsulta",password);
            //Generem la petició que farem al servidor
            PeticioClient peticio = new PeticioClient("LOGIN");
            peticio.afegirDades(usuari);
            //Enviem la petició al servidor en JSON
            Gson gson = new Gson();
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            output.println(gson.toJson(peticio));
            
            //RESPOSTA DEL SERVIDOR QUE REP EL CLIENT
            //Llegim la resposta revuda
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String llegir = input.readLine();
            RetornDades retorn = gson.fromJson(llegir, RetornDades.class);
                
            

            socket.close();
            //Comprobem que les dades rebudes coincideixen amb un login correcte
            //Comprobem el codi del resultat, 0 en cas de verificació fallida
            assertEquals("Codi d'error",0, retorn.getCodiResultat());

    
        } catch (IOException ex) {
            Logger.getLogger(LoginTest.class.getName()).log(Level.SEVERE, null, ex);
    }
    }

}
