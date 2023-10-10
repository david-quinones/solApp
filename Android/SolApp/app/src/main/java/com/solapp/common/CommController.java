package com.solapp.common;

import com.google.gson.Gson;
import com.solapp.models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class CommController {

    public static int BAD_VALUE = -1;
    private static int sessionCode=BAD_VALUE;
    private static int port = 8080;

    private static String serverName = "";


    public static final int OK_RETURN_CODE = 0;

    // Names of the requests

    public static final String LOGIN = "LOGIN";
    public static final String LOGOUT = "LOGOUT";
    public static final String LIST_USERS = "LIST_USERS";
    public static final String ADD_USER = "ADD_USER";


    //opens a connection to the server
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
     * returns true if client is logged and false otherwhise
     * @return
     */
    public static boolean isLogged(){
        return sessionCode!=BAD_VALUE;
    }

    /**
     * Set the value of port
     *
     * @param port new value of port
     */
    public static void setPort(int port) {
        CommController.port = port;
    }

    /**
     * Set the value of serverName
     *
     * @param serverName new value of serverName
     */
    public static void setServerName(String serverName) {
        CommController.serverName = serverName;
    }

    /**
     * Makes a login request to the server
     * @param user  username
     * @param password password
     * @return result code
     */
    public static int doLogin(String user, String password){
        BackendValues login = new BackendValues(LOGIN);
        login.addPrimitiveData(user);
        login.addPrimitiveData(password);
        ReturnValues ret=talkToServer(login);

        if(ret==null) return BAD_VALUE;

        int returnCode=ret.getReturnCode();

        if(returnCode==OK_RETURN_CODE){
            sessionCode=(Integer) ret.getData(0, Integer.class);
        }

        return ret.getReturnCode();
    }

    /**
     * Makes a logout request to the server
     * @return result code
     */
    public static int doLogout(){

        if(sessionCode==BAD_VALUE) return OK_RETURN_CODE;

        BackendValues logout = new BackendValues(LOGOUT);

        logout.addPrimitiveData(sessionCode);

        ReturnValues ret=talkToServer(logout);

        if(ret==null) return BAD_VALUE;

        int code= ret.getReturnCode();

        if(code==OK_RETURN_CODE){
            sessionCode=BAD_VALUE;
        }

        return code;

    }

    /**
     * Makes a "list user" request to the server
     * @return result users array; null if error.
     */
    public static User[] doListUsers(){

        if(sessionCode==BAD_VALUE) return null;

        BackendValues listUsers = new BackendValues(LIST_USERS);

        listUsers.addPrimitiveData(sessionCode);

        ReturnValues ret=talkToServer(listUsers);

        if(ret==null) return null;

        int returnCode=ret.getReturnCode();

        if(returnCode==OK_RETURN_CODE){
            User [] users={};
            return ( User []) ret.getData(0, users.getClass());
        }
        else {
            return null;
        }

    }

    /**
     * Makes an "add user" request to the server
     * @param user  user to be added
     * @return result code
     */
    public static int doAddUser(User user){

        BackendValues addUser = new BackendValues(ADD_USER);
        addUser.addPrimitiveData(sessionCode);
        addUser.addDataObject(user);

        ReturnValues ret=talkToServer(addUser);

        if(ret==null) return BAD_VALUE;

        return ret.getReturnCode();

    }
    /**
     * Makes a "query user" request to the server
     * @param username  username to search
     * @return result user with this username; null if error
     */
    public static User doQueryUser(String username){
        if(username.equals("99")) { // simulates non-existent key
            return null;
        }else{
            return new User(username,"Mock value");
        }

    }

    // Sends message to the server and returns its response.
    // messages are serialized as Json values before and after communication
    private static ReturnValues talkToServer(BackendValues message){
        try {
            Socket socket = connect();

            Gson gson= new Gson();

            if(socket==null) return null;

            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);


            output.println(gson.toJson(message));

            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String data = input.readLine();

            ReturnValues ret=gson.fromJson(data,ReturnValues.class);

            socket.close();

            return ret;

        } catch (IOException ex) {
            return null;
        }

    }

}
