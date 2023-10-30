package sol.app.quinones.solappquinones.Service;

import java.io.*;
import java.net.Socket;

/**
 * Classe que proporciona una interficia per establir una connexió amb el servidor usant SOCKET
 * Permet connectar al servidor, enviar missatges i rebre respostes
 *
 * @author david
 */
public class ServerComunication {

    private String serverAddress;
    private int port;
    private Socket socket;

    /**
     * Constructor que inicializa la comunicació amb el servidor a la direcció i port establers
     */
    public ServerComunication() {
        this.serverAddress = "localhost";
        this.port = 9999;
    }

    /**
     * Contructor que inicializa la comunicació amb el servidor utilizant els parametres que rep de direcció i port
     * @param serverAddress
     * @param port
     */
    public ServerComunication(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    /**
     * Metode que estableix la connexió amb el servidor utilitzant l'adreça i port
     * @throws IOException En cas d'error durant la connexió li pasaem al pare que l'ha cridat
     */
    public void connect() throws IOException {
        socket = new Socket(serverAddress, port);
    }

    /**
     * Envia missatge al servidor i espera respota
     * @param message missatge que ha d'enviar
     * @return respota del servidor
     * @throws IOException En cas d'errr durant la comunicació
     */
    public String sendMessage (String message) throws IOException {
        //Obrim connexió i enviem
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(message);

        //Obtenim resposta
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String input = in.readLine();

        boolean haveDades = (input != null && !input.isEmpty()); // ni null ni buida (blanc)

        if(haveDades){
            socket.close();
            return input;
        }

        socket.close();

        return null; //ja captura l'excepció el JSON
    }

    /**
     * Estableix el socket utilitza per la comunicació
     * Metode per probes unitaries, ja que s'indentifica com a protected per accedir des de subclases
     *
     * @param socket
     */
    protected void setSocket(Socket socket){
        this.socket = socket;
    }

}
