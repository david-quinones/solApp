package estel.solapp.common;

import static estel.solapp.common.Utility.showToast;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import estel.solapp.models.User;
/**
 * Classe de comunicació amb el servidor
 * @author Juan Antonio
 */
public class CommController {

    private static int port = 9999;
    private static String serverName = "192.168.1.131";
    public static final int OK_RETURN_CODE = 1;

    // Noms de les peticions
    public static final String LOGIN = "LOGIN";
    public static final String LOGOUT = "LOGOUT";
    public static final String LIST_USERS = "LIST_USERS";
    public static final String ADD_USER = "ADD_USER";


    //Conexió amb el servidor
    private static Socket connect() {
        Socket s;

        try {
            s= new Socket();
            s.connect(new InetSocketAddress(serverName,port),4000);

            return s;
        } catch (IOException ex) {

            return null;
        }

    }

    /**
     * Retorna true si hi ha sessio i false si no
     * @return
     */

    public static boolean isLogged() {return SingletonSessio.getInstance().getKey() != null;}


    /**
     * Petició de login al servidor
     * @param user  username
     * @param password password
     * @return resposta del servidor
     */
    public static ValorsResposta doLogin(String user, String password){
        User usuari = new User(user,password);
        PeticioClient login = new PeticioClient(LOGIN);
        login.addDataObject(usuari);
        ValorsResposta resposta=talkToServer(login);

        if(resposta==null) {return null;}

        return resposta;
    }

    /**
     * Petició de logout servidor
     * @return resposta del sevidor
     */
    public static ValorsResposta doLogout(){

        PeticioClient logout = new PeticioClient(LOGOUT);

        //Afegim el codi de sessió a la petició
        logout.addDataObject(SingletonSessio.getInstance().getKey().replace("\"",""));

        ValorsResposta resposta = talkToServer(logout);

        if(resposta==null) return null;

        return resposta;

    }

    /**
     * Petició de llista d'usuaris al servidor
     * @return result users array; null if error.
     */
    public static User[] doListUsers(){



        PeticioClient listUsers = new PeticioClient(LIST_USERS);

        listUsers.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));

        ValorsResposta resposta=talkToServer(listUsers);

        if(resposta==null) return null;

        int returnCode=resposta.getReturnCode();

        if(returnCode==OK_RETURN_CODE){
            User [] users={};
            return ( User []) resposta.getData(0, users.getClass());
        }
        else {
            return null;
        }

    }

    /**
     * Petició d'afegir usuari al servidor
     * @param user  Usiari que afegirem
     * @return resposta del servidor
     */
    public static ValorsResposta doAddUser(User user){

        PeticioClient addUser = new PeticioClient(ADD_USER);
        addUser.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        addUser.addDataObject(user);

        ValorsResposta resposta=talkToServer(addUser);

        if(resposta==null) return null;

        return resposta;

    }
    /**
     * Fa la petició de recerca d'usuari al servidor
     * @param username  Nom d'usuari a buscar
     * @return resposta del servidor
     */
    public static User doQueryUser(String username){
        if(username.equals("99")) { // simulates non-existent key
            return null;
        }else{
            return new User(username,"Mock value");
        }

    }
    /**
    * Envia missatge al servidor i rep la resposta d'aquest.
    * Transformació dels missatges en Json avans i després de l'enviament
    * @param peticio Petició al sevidor
    * @return resposta Resposta del servidor.
    */
    private static ValorsResposta talkToServer(PeticioClient peticio){
        try {
            Socket socket = connect();//Conexió amb el sevidor

            Gson gson= new Gson();

            if(socket==null) return null;

            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);


            output.println(gson.toJson(peticio));

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String data = input.readLine();

            ValorsResposta resposta=gson.fromJson(data,ValorsResposta.class);

            socket.close();

            return resposta;

        } catch (IOException ex) {
            return null;
        }

    }

}
