package servidor;

import com.google.gson.Gson;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.ConexioBBDD;
import resposta.ControladorResposta;
import resposta.EnviarResposta;

/**
 *Classe que implemente un servidor amb sockets.
 * @author pau
 */
public class ServidorSocketListener {
    
    private ServerSocket servidor_socket;
    private Socket conexio_socket;
    private ConexioBBDD base_dades;
    private int port;
    private boolean activat = false;
    public static final int MAX_CLIENTS = 50;
    
    
    /**Constructor que inicialitza el socket del servidor amb el port especificat
     * i inicialitzem la class per la base de dades.
     * 
     * @param port , enter amb el port que s'utilitzará
     */
    public ServidorSocketListener(int port){       
        try {
            this.port = port;
            
            servidor_socket = new ServerSocket(port);
            
            base_dades = new ConexioBBDD();
            
        } catch (IOException ex) {
            Logger.getLogger(ServidorSocketListener.class.getName()).log(Level.SEVERE,
                    "Error en el constructor al crear el server socket amb el port " + port, ex);
        }
    }
    
    
    /**Métode per escoltar clients que volen conectar-se amb el servidor. 
     * El servidor es manté a l'escolta fins que es cridi al métode tancarServidor.
     * 
     */
    public void escoltarClients(){
        
        activat = true;
        //Per poder controlar diferentes clients de forma concurrent
        ExecutorService executor = Executors.newFixedThreadPool(MAX_CLIENTS);
        
        while(activat){
            try {
                //Acceptem conexions amb el servidor
                conexio_socket = servidor_socket.accept();
                
                /*Inicialitzem el GestorClients per realitzar les comunicacions 
                amb diferents clients*/
                GestorClients gestorClients = new GestorClients(conexio_socket);
                
                //Executem la piscina de fils
                executor.execute(gestorClients);
                
            } catch (IOException ex) {
                
                Logger.getLogger(ServidorSocketListener.class.getName()).log(Level.SEVERE, 
                        "Error al intentar escoltar clients en el métode escoltarClients", ex);
            }
            
        }
      
    }
    
    /**Métode per tancar el servidor de forma segura
     * 
     */
    public void tancarServidor(){
        if(servidor_socket != null && !servidor_socket.isClosed()){
            try{
                servidor_socket.close();
                
            }catch(IOException ex){
                
                Logger.getLogger(ServidorSocketListener.class.getName()).log(Level.SEVERE, 
                        "Error al intentar tancar el servidor", ex);
            }finally{
                activat = false;
            }
        }
    }
    
    
    /**Classe interna per gestionar les peticions del servidor en fils separats entre
     * les peticions dels clients i el servidor
     * 
     */
    private class GestorClients implements Runnable{
        private Socket client;
        ControladorResposta controlador;
        RetornDades dadesResposta;
        EnviarResposta output;
        
        public GestorClients(Socket socket){
            client = socket;
            
        }

        /**Métode on es realitzaran totes les gestions de cada fil creat.
         * 
         */
        @Override
        public void run() {
            /*try-with_resources que permet gestionar el tancament del
            BufferedReader de forma automàtica*/
            try(
                    BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream())))
            {
                //Llegim dades rebudes del client
                String dades = input.readLine();
                
                //Deserialització de les dades en un objecte PeticioClient
                Gson gson = new Gson();
                PeticioClient peticio = gson.fromJson(dades, PeticioClient.class);
                
                //Instanciem ControladorRespostes amb la petició del client
                controlador = new ControladorResposta(peticio);
                
                //Afegim les dades de retorn al objecte RetornDades
                dadesResposta = controlador.gestionarResposta();
                
                //Instanciem l'objecte EnviarResposta amb el socket de comunicació
                output = new EnviarResposta(client);
                
                //Enviem resposta al client amb les dades
                output.enviarResposta(dadesResposta);
                             
            } catch (IOException ex) {
                Logger.getLogger(ServidorSocketListener.class.getName()).log(Level.SEVERE,
                        "Error al conectar amb el client en el métode run de GestorClients", ex);
                
            }finally{
                try {
                    client.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServidorSocketListener.class.getName()).log(Level.SEVERE,
                            "Error al tancar el socket", ex);
                }
            }
        }
        
    }
}
