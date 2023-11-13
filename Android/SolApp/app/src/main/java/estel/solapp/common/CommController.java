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

import estel.solapp.models.Empleat;
import estel.solapp.models.Persona;
import estel.solapp.models.User;
/****************************************
 * Classe de comunicacions amb el servidor
 * Peticions i rebuda de respostes
 * @author Juan Antonio
 ****************************************/
public class CommController {

    //Dades de conexió
    private static int port = 9999;
    private static String serverName = "192.168.1.131";
    public static final int OK_RETURN_CODE = 1;

    // Noms de les peticions
    public static final String LOGIN = "LOGIN";
    public static final String LOGOUT = "LOGOUT";
    public static final String LLISTAR_USUARIS = "LLISTAR_USUARIS";
    public static final String AFEGIR_USUARI = "AFEGIR_USUARI";
    public static final String CONSULTA_PERFIL = "CONSULTA_PERFIL";
    public static final String MODIFICAR_PERFIL = "MODIFICAR_PERFIL";
    public static final String BUSCA_USUARI = "BUSCA_USUARI";
    public static final String LLISTAR_EMPLEATS = "LLISTAR_EMPLEATS";

    /***********************************
    * Conexió amb el servidor per socket
    ************************************/
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

    /***************************************************
     * Métode retorna true si hi ha sessio i false si no
     * @return
     ***************************************************/
    public static boolean isLogged() {return SingletonSessio.getInstance().getKey() != null;}

    /******************************
     * Petició de login al servidor
     * @param user  username
     * @param password password
     * @return resposta del servidor
     *******************************/
    public static ValorsResposta doLogin(String user, String password){

        User usuari = new User(user,password);
        PeticioClient login = new PeticioClient(LOGIN);
        login.addDataObject(usuari);
        ValorsResposta resposta=talkToServer(login);

        if(resposta==null) {return null;}

        return resposta;
    }

    /******************************
     * Petició de logout servidor
     * @return resposta del sevidor
     ******************************/
    public static ValorsResposta doLogout(){

        PeticioClient logout = new PeticioClient(LOGOUT);

        //Afegim el codi de sessió a la petició
        logout.addDataObject(SingletonSessio.getInstance().getKey().replace("\"",""));

        ValorsResposta resposta = talkToServer(logout);

        if(resposta==null) return null;

        return resposta;

    }

    /*****************************************
     * Petició d'informació de perfil servidor
     * @return resposta del sevidor
     *****************************************/
    public static ValorsResposta consultaPerfil(){

        PeticioClient consultaPerfil = new PeticioClient(CONSULTA_PERFIL);

        //Afegim el codi de sessió a la petició
        consultaPerfil.addDataObject(SingletonSessio.getInstance().getKey().replace("\"",""));

        ValorsResposta resposta = talkToServer(consultaPerfil);

        if(resposta==null) return null;

        return resposta;

    }

    /**********************************************
     * Petició de modificació de perfil al servidor
     * @param persona
     * @return resposta del sevidor
     **********************************************/
    public static ValorsResposta modificaPerfil(Persona persona){


        PeticioClient modificaPerfil = new PeticioClient(MODIFICAR_PERFIL);

        //Afegim el codi de sessió a la petició
        modificaPerfil.addDataObject(SingletonSessio.getInstance().getKey().replace("\"",""));

        //Afegim les dades de persona llegides
        modificaPerfil.addDataObject(persona);

        ValorsResposta resposta = talkToServer(modificaPerfil);

        if(resposta==null) return null;

        return resposta;

    }

    /*******************************************
     * Petició de llista d'usuaris al servidor
     * @return result users array; null if error.
     ********************************************/
    public static User[] llistarUsuaris(){

        PeticioClient listUsers = new PeticioClient(LLISTAR_USUARIS);
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

    /*************************************
     * Petició d'afegir usuari al servidor
     * @param user  Usiari que afegirem
     * @return resposta del servidor
     *************************************/
    public static ValorsResposta afegirUsuari(User user){

        PeticioClient afegirUsuari = new PeticioClient(AFEGIR_USUARI);
        afegirUsuari.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        afegirUsuari.addDataObject(user);

        ValorsResposta resposta=talkToServer(afegirUsuari);

        if(resposta==null) return null;

        return resposta;

    }


    /***********************************************
     * Fa la petició de recerca d'usuari al servidor
     * @param username  Nom d'usuari a buscar
     * @return resposta del servidor
     ************************************************/
    public static ValorsResposta buscaUsuari(String username){

        PeticioClient afegirUsuari = new PeticioClient(BUSCA_USUARI);
        afegirUsuari.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        afegirUsuari.addPrimitiveData(username);
        ValorsResposta resposta=talkToServer(afegirUsuari);

        if(resposta==null) return null;

        return resposta;

    }

    /*************************************************
     * Petició de llista de professors al servidor
     * @return resultat empleats array; null si error.
     *************************************************/
    public static ValorsResposta llistarEmpleats(){

        PeticioClient listEmpleats = new PeticioClient(LLISTAR_EMPLEATS);
        listEmpleats.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        ValorsResposta resposta=talkToServer(listEmpleats);

        if(resposta==null) return null;

        return resposta;

    }

    /********************************************************************
    * Envia missatge al servidor i rep la resposta d'aquest.
    * Transformació dels missatges en Json avans i després de l'enviament
    * @param peticio Petició al sevidor
    * @return resposta Resposta del servidor.
    *********************************************************************/
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
