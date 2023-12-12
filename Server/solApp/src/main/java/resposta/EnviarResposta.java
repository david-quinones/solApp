package resposta;

import com.google.gson.Gson;
import estructurapr.RetornDades;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

/**Classe que enviará la resposta al client a través d'un socket
 *
 * @author Pau Castell Galtes
 */
public class EnviarResposta {
    SSLSocket socket;

    
    /**Constructor que rep el socket per poder fer l'enviament de les dades
     * 
     * @param socket per on es retornara les dades de la resposta
     */
    public EnviarResposta(SSLSocket socket) {
        this.socket = socket;
    }
    
    
    /**Métode per enviar la resposta definitiva al client a través d'un socket
     * 
     * @param resposta que enviarà al client
     */
    public void enviarResposta(RetornDades resposta){
        //Inicialitzem l'output al try-with-resources per que gestioni el tancament automàticament
        try(PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream())))
        {
            //Transformem l'objecte a enviar en format JSON
            Gson gson = new Gson();
            String respostaJson = gson.toJson(resposta);
            //Enviem l'objecte a través del socket
            output.println(respostaJson);
            output.flush();
        
    }   catch (IOException ex) {
            Logger.getLogger(EnviarResposta.class.getName()).log(Level.SEVERE, 
                    "Error al enviar la resposta al client", ex);
        }
}
}
