package resposta;

import entitats.*;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.ConexioBBDD;
import persistencia.*;
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
    private ConexioBBDD base_dades;
    private Connection conexio;
    
    public GenerarResposta(){
        try {
            conexio = base_dades.conectar();
        } catch (SQLException ex) {
            Logger.getLogger(GenerarResposta.class.getName()).log(Level.SEVERE,
                    "Error al conectar amb la base de dades", ex);
        }
    }
    
    
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
            //Generem número de sessió
            String numeroSessio = UUID.randomUUID().toString();
            //Emmagatzmar el número de sessió al usuari actiu
            sessions.agregarSessio(usuari, numeroSessio);
            //Afegim a la resposta codi de consulta correcte
            resposta = new RetornDades(CODI_CORRECTE);
            //Afegim a la resposta totes les dades de l'usuari validat
            resposta.afegirDades(usuari);
            //Afegim a la resposta el número de sessió generat
            resposta.afegirDades(numeroSessio);

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
     * @return resposta amb les dades
     */
    public RetornDades respostaLogout(String numSessio){
            //Eliminem número de sessió
            sessions.eliminarSessio(numSessio);
            //Generem resposta
            resposta = new RetornDades(CODI_CORRECTE);
            return resposta;

    }
    
    /**Métode que genera la resoposta a una solicitud d'alta d'un empleat
     * 
     * @param empleat que es vol donar d'alta
     * @return codi correcte o d'error
     */
    public RetornDades respostaAltaEmpleat(Empleat empleat){
            //Instanciem la classe EmpleatDAO
            EmpleatDAO empleatDAO = new EmpleatDAO(conexio);
            //Codi que ens indica quantes files s'han insertat
            int codiAltaEmpleat = empleatDAO.altaEmpleat(empleat);
            //Comprobem que l'entitat Empleat s'ha insertat
            if(codiAltaEmpleat > 0){
                return resposta = new RetornDades(CODI_CORRECTE);
            }else{
                //Si no s'ha insertat cap fila retornem codi d'error
                return resposta = new RetornDades(CODI_ERROR);
            }

    }      
}
