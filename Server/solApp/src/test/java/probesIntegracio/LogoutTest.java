
package probesIntegracio;

import com.google.gson.Gson;
import entitats.Usuari;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import java.io.*;
import java.net.Socket;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import seguretat.GestorSessions;
import servidor.ServidorSocketListener;

/**
 *
 * @author Pau Castell Galtes
 */
public class LogoutTest {
    private ServidorSocketListener servidor;
    private GestorSessions gestorSessions;
    private Socket socket;
    private PeticioClient peticio;
    private Usuari usuari;
    private String sessioActiva;
    private RetornDades retorn;
    
     /**Iniciem el servidor en un fil diferent al del client
     * per poder fer el test
     * 
     */
    @Before
    public void setUp(){
        gestorSessions = GestorSessions.obtindreInstancia();
        usuari = new Usuari(1,"nom_usuari", "password",true,false);

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
    
     /**Test per comprobar que es realitza la desconexió d'un usuari de forma correcte.
      * L'usuari que s'utilitzarà serà el següent:
     * id: 1
     * Nom d'usuari: "nom_usuari"
     * password: "password" (hash:XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=)
     * isAdmin: true
     * isTeacher: false
     */
    @Test
    public void testLogoutCorrecte(){
        
        try {
            //Socket client
            socket = new Socket("localhost",9999);
            //Simulem el número de sessió activa
            sessioActiva = numSessio(usuari);
            
            //Comprobem que la sessió es activa
            assertEquals(true, gestorSessions.verificarSessio(sessioActiva));
            
            //PETICIO CLIENT LOGOUT
            peticio = new PeticioClient("LOGOUT");
            peticio.afegirDades(sessioActiva);
            
            //Enviem la petició de logout al servidor en format JSON
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            Gson gson = new Gson();
            output.println(gson.toJson(peticio));
            
            //Llegim les dades retornades pel servidor amb la resposta
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String llegir = input.readLine();
            retorn = gson.fromJson(llegir, RetornDades.class);
            
            socket.close();
            
            //Comprobem que el resultat es l'esperat, 1 si s'ha tancat la sessió correctament
            assertEquals(1, retorn.getCodiResultat());
            //Comprobem que s'ha esborrat la sessió activa
            assertEquals(false, gestorSessions.verificarSessio(llegir));
            
        } catch (IOException ex) {
            Logger.getLogger(LogoutTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String numSessio(Usuari usuari){
        //Es crea un número de sessió per fer la proba
        String numeroSessio = UUID.randomUUID().toString();
        //La guardem dins el controlador de sessions
        gestorSessions.agregarSessio(usuari, numeroSessio);
        //Retornem el número de sessió per fer la proba
        return numeroSessio;
    }
    
    public void testLogoutIncorrecte(){
        try {
            //Socket client
            socket = new Socket("localhost",9999);
            
            //Simulem un número de sessió incorrecte
            sessioActiva = "sessió_falsa";
            
            //PETICIO CLIENT LOGOUT
            peticio = new PeticioClient("LOGOUT");
            peticio.afegirDades(sessioActiva);
            
            //Enviem la petició de logout al servidor en format JSON
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            Gson gson = new Gson();
            output.println(gson.toJson(peticio));
            
            //Llegim les dades retornades pel servidor amb la resposta
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String llegir = input.readLine();
            retorn = gson.fromJson(llegir, RetornDades.class);
            
            socket.close();
            
            //Comprobem que el resultat es 0, error al tancar la sessió
            assertEquals(0, retorn.getCodiResultat());
            
        } catch (IOException ex) {
            Logger.getLogger(LogoutTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
