package estel.solapp.common;

import static estel.solapp.common.Utility.showToast;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import estel.solapp.models.Alumne;
import estel.solapp.models.Aula;
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
    public static final String MODIFICAR_USUARI = "MODIFICAR_USUARI";
    public static final String AFEGIR_USUARI = "AFEGIR_USUARI";
    public static final String CONSULTA_PERFIL = "CONSULTA_PERFIL";
    public static final String MODIFICAR_PERFIL = "MODIFICAR_PERFIL";
    public static final String BUSCA_USUARI = "BUSCA_USUARI";
    public static final String ALTA_EMPLEAT = "ALTA_EMPLEAT";
    public static final String MODIFICAR_EMPLEAT = "MODIFICAR_EMPLEAT";
    public static final String ELIMINAR_EMPLEAT = "ELIMINAR_USUARI";
    public static final String LLISTAR_EMPLEATS = "LLISTAR_EMPLEATS";
    public static final String ALTA_ALUMNE = "ALTA_ALUMNE";
    public static final String LLISTAR_ALUMNES = "LLISTAR_ALUMNES";
    public static final String MODIFICAR_ALUMNE = "MODIFICAR_ALUMNE";
    public static final String ELIMINAR_ALUMNE = "ELIMINAR_USUARI";
    public static final String ELIMINAR_AULA = "ELIMINAR_AULA";
    public static final String LLISTAR_AULES = "LLISTAR_AULES";
    public static final String MODIFICAR_AULA = "MODIFICAR_AULA";
    public static final String ALTA_AULA = "ALTA_AULA";

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

    /********************************************************************************************************************************************
     LOGIN LOGOUT
     ********************************************************************************************************************************************/

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

    /********************************************************************************************************************************************
                PERFIL
     ********************************************************************************************************************************************/

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

    /********************************************************************************************************************************************
                        USUARIS
     ********************************************************************************************************************************************/

    /*************************************
     * Petició de llista d'usuaris al servidor
     *
     * @return resposta del servidor
     *************************************/
    public static ValorsResposta llistarUsuaris(){

        PeticioClient listarUsuaris = new PeticioClient(LLISTAR_USUARIS);
        listarUsuaris.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        ValorsResposta resposta=talkToServer(listarUsuaris);

        if(resposta==null) return null;

        return resposta;

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

    /*************************************************
     * Petició modificar usuari al servidor
     * @param user
     * * @return resultat OK/NOK; null si error.
     *************************************************/
    public static ValorsResposta modificarUsuari(User user){

        PeticioClient modificausuari = new PeticioClient(MODIFICAR_USUARI);
        modificausuari.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        modificausuari.addDataObject(user);
        Gson gson= new Gson();
        Log.d("PETICIO MODIFICA USER", gson.toJson(modificausuari));
        ValorsResposta resposta=talkToServer(modificausuari);

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

    /********************************************************************************************************************************************
                EMPLEATS (PROFESSORS)
     ********************************************************************************************************************************************/

    /*************************************************
     * Petició de alta de professors al servidor
     * @param empleat
     * @param user
     * * @return resultat OK/NOK; null si error.
     *************************************************/
    public static ValorsResposta afegirEmpleat(Empleat empleat, User user){

        PeticioClient altaEmpleat = new PeticioClient(ALTA_EMPLEAT);
        altaEmpleat.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        altaEmpleat.addDataObject(empleat);
        altaEmpleat.addDataObject(user);
        Gson gson= new Gson();
        Log.d("PETICIO ALTA PROFE", gson.toJson(altaEmpleat));
        ValorsResposta resposta=talkToServer(altaEmpleat);

        if(resposta==null) return null;

        return resposta;

    }

    /*************************************************
     * Petició modificar professor al servidor
     * @param empleat
     * * @return resultat OK/NOK; null si error.
     *************************************************/
    public static ValorsResposta modificarEmpleat(Empleat empleat){

        PeticioClient modificaEmpleat = new PeticioClient(MODIFICAR_EMPLEAT);
        modificaEmpleat.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        modificaEmpleat.addDataObject(empleat);
        Gson gson= new Gson();
        Log.d("PETICIO MODIFICA PROFE", gson.toJson(modificaEmpleat));
        ValorsResposta resposta=talkToServer(modificaEmpleat);

        if(resposta==null) return null;

        return resposta;

    }

    /*************************************************
     * Petició d'eliminar professor al servidor
     * @param empleat
     * * @return resultat OK/NOK; null si error.
     *************************************************/
    public static ValorsResposta eliminarEmpleat(Empleat empleat){

        PeticioClient eliminaEmpleat = new PeticioClient(ELIMINAR_EMPLEAT);
        eliminaEmpleat.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        eliminaEmpleat.addDataObject(empleat);
        Gson gson= new Gson();
        Log.d("PETICIO ELIMINA PROFE", gson.toJson(eliminaEmpleat));
        ValorsResposta resposta=talkToServer(eliminaEmpleat);

        if(resposta==null) return null;

        return resposta;

    }
    /*************************************************
     * Petició de llista de professors al servidor
     * Només enviem el codi de sesseió al servidor
     * @return resultat empleats array; null si error.
     *************************************************/
    public static ValorsResposta llistarEmpleats(){

        PeticioClient listEmpleats = new PeticioClient(LLISTAR_EMPLEATS);
        listEmpleats.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        ValorsResposta resposta=talkToServer(listEmpleats);

        if(resposta==null) return null;

        return resposta;

    }

    /********************************************************************************************************************************************
                ALUMNES
     ********************************************************************************************************************************************/

    /*************************************************
     * Petició de alta d'alumnes al servidor
     * @param alumne
     * @param user
     * * @return resultat OK/NOK; null si error.
     *************************************************/
    public static ValorsResposta afegirAlumne(Alumne alumne, User user){

        PeticioClient altaAlumne = new PeticioClient(ALTA_ALUMNE);
        altaAlumne.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        altaAlumne.addDataObject(alumne);
        altaAlumne.addDataObject(user);
        Gson gson= new Gson();
        Log.d("PETICIO ALTA PROFE", gson.toJson(altaAlumne));
        ValorsResposta resposta=talkToServer(altaAlumne);

        if(resposta==null) return null;

        return resposta;

    }

    /*************************************************
     * Petició de llista d'alumnes al servidor
     * Només enviem el codi de sesseió al servidor
     * @return resultat alumnes list; null si error.
     *************************************************/
    public static ValorsResposta llistarAlumnes(){

        PeticioClient llistarAlumnes = new PeticioClient(LLISTAR_ALUMNES);
        llistarAlumnes.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        ValorsResposta resposta=talkToServer(llistarAlumnes);

        if(resposta==null) return null;

        return resposta;

    }

    /*************************************************
     * Petició modificar alumne al servidor
     * @param alumne
     * * @return resultat OK/NOK; null si error.
     *************************************************/
    public static ValorsResposta modificarAlumne(Alumne alumne){

        PeticioClient modificaAlumne = new PeticioClient(MODIFICAR_ALUMNE);
        modificaAlumne.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        modificaAlumne.addDataObject(alumne);
        Gson gson= new Gson();
        Log.d("PETICIO MODIFICA PROFE", gson.toJson(modificaAlumne));
        ValorsResposta resposta=talkToServer(modificaAlumne);

        if(resposta==null) return null;

        return resposta;

    }
    /*************************************************
     * Petició d'eliminar alumne al servidor
     * @param alumne
     * * @return resultat OK/NOK; null si error.
     *************************************************/
    public static ValorsResposta eliminarAlumne(Alumne alumne){

        PeticioClient eliminaAlumne = new PeticioClient(ELIMINAR_ALUMNE);
        eliminaAlumne.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        eliminaAlumne.addDataObject(alumne);
        Gson gson= new Gson();
        Log.d("PETICIO ELIMINA ALUMNE", gson.toJson(eliminaAlumne));
        ValorsResposta resposta=talkToServer(eliminaAlumne);

        if(resposta==null) return null;

        return resposta;

    }

    /********************************************************************************************************************************************
                AULES
     ********************************************************************************************************************************************/

    /*************************************************
     * Petició de llista d'aules al servidor
     * Només enviem el codi de sesseió al servidor
     * @return resultat alumnes list; null si error.
     *************************************************/
    public static ValorsResposta llistarAules(){

        PeticioClient llistarAules = new PeticioClient(LLISTAR_AULES);
        llistarAules.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        Gson gson= new Gson();
        Log.d("PETICIO LLISTAR AULES", gson.toJson(llistarAules));
        ValorsResposta resposta=talkToServer(llistarAules);


        if(resposta==null) return null;

        return resposta;

    }

    /*************************************
     * Petició d'afegir aula al servidor
     * @param aula que afegirem
     * @return resposta del servidor
     *************************************/
    public static ValorsResposta afegirAula(Aula aula){

        PeticioClient afegirAula = new PeticioClient(ALTA_AULA);
        afegirAula.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        afegirAula.addDataObject(aula);

        ValorsResposta resposta=talkToServer(afegirAula);

        if(resposta==null) return null;

        return resposta;

    }
    /*************************************************
     * Petició modificar alumne al servidor
     * @param aula
     * * @return resultat OK/NOK; null si error.
     *************************************************/
    public static ValorsResposta modificarAula(Aula aula){

        PeticioClient modificaAula = new PeticioClient(MODIFICAR_AULA);
        modificaAula.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        modificaAula.addDataObject(aula);
        Gson gson= new Gson();
        Log.d("PETICIO MODIFICA PROFE", gson.toJson(modificaAula));
        ValorsResposta resposta=talkToServer(modificaAula);

        if(resposta==null) return null;

        return resposta;

    }

    /*************************************************
     * Petició d'eliminar alumne al servidor
     * @param aula
     * * @return resultat OK/NOK; null si error.
     *************************************************/
    public static ValorsResposta eliminarAula(Aula aula){

        PeticioClient eliminaAula = new PeticioClient(ELIMINAR_AULA);
        eliminaAula.addPrimitiveData(SingletonSessio.getInstance().getKey().replace("\"",""));
        eliminaAula.addDataObject(aula);
        Gson gson= new Gson();
        Log.d("PETICIO ELIMINA AULA", gson.toJson(eliminaAula));
        ValorsResposta resposta=talkToServer(eliminaAula);

        if(resposta==null) return null;

        return resposta;

    }





    /********************************************************************************************************************************************
                        PETICIO / RESPOSTA (TALKTOSERVER)
     ********************************************************************************************************************************************/

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
