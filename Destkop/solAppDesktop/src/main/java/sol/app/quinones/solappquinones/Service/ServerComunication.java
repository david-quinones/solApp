package sol.app.quinones.solappquinones.Service;

import java.io.*;
import java.net.Socket;

public class ServerComunication {

    private String serverAddress;
    private int port;
    private Socket socket;

    public ServerComunication() {
        this.serverAddress = "localhost";
        this.port = 9999;
    }

    public ServerComunication(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public void connect() throws IOException {
        socket = new Socket(serverAddress, port);
    }

    public String sendMessage (String message) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(message);

        //resposta:
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String input = in.readLine();

        boolean haveDades = input.isEmpty() || input == null ? false:true;

        if(haveDades){
            return input;
        }
        socket.close();

        return "No hi ha dades, ni respota";
    }

}
