package resposta;

import entitats.Usuari;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import persistencia.ConexioBBDD;
import persistencia.UsuariDAO;
import seguretat.GestorSessions;

/**
 *Classe que genera i construeix les respostes que s'enviaran al client
 * @author pau
 */
public class GenerarResposta {
    private static final int CODI_CORRECTE = 1;
    private static final int CODI_ERROR = 0;
    private RetornDades resposta;
    private UsuariDAO usuariDAO;
    private GestorSessions sessions = GestorSessions.obtindreInstancia();
    
    
    /**Métode que contrueix la resposta que s'enviará al client per validar 
     * 
     * @param usuari 
     * @return 
     */
    public RetornDades respostaLogin(Usuari usuari){ 
        
        usuariDAO = new UsuariDAO();
        //Validació de l'usuari i password
        usuari = usuariDAO.validarUsuari(usuari);
        
        //Si l'usuri no es null
        if(usuari != null){
            //Generem un número de sessió
            sessions.agregarSessio(usuari);
            //Afegim a la resposta codi de consulta correcte
            resposta = new RetornDades(CODI_CORRECTE);
            //Afegim a la resposta totes les dades de l'usuari validat
            resposta.afegirDades(usuari);
            //Afegim a la resposta el número de sessió generat
            resposta.afegirDades(sessions.retornaSessio(usuari.getId()));

            //Retornem el paquet de les dades en l'objecte RetornDades
            return resposta;
            
        }else{
            //Si l'usuari es null enviem el codi d'error dins el paquet RetornDades
            return resposta = new RetornDades(CODI_ERROR);
        }
        
    }
    
    /**Métode que realitza el logout d'un usuari eliminan la sessió activa.
     * 
     * @param numSessio número de sessió activa
     * @param usuariId usuari amb la sessió activa
     * @return resposta amb les dades
     */
    public RetornDades respostaLogout(String numSessio, int usuariId){
        //Comprobem si el número de sessió está activa
        if(sessions.retornaSessio(usuariId).equals(numSessio)){
            //Eliminem número de sessió
            sessions.eliminarSessio(usuariId);
            //Generem resposta
            resposta = new RetornDades(CODI_CORRECTE);
            return resposta;
        }else{
            //Resposta en cas de sessió no activa
            resposta = new RetornDades(CODI_ERROR);
            return resposta;
        }
    }
        
}
