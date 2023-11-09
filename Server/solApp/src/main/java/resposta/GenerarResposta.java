package resposta;

import entitats.*;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private static final Logger LOGGER = Logger.getLogger(PersonaDAO.class.getName());
    
    public GenerarResposta(){
        try {
            base_dades = new ConexioBBDD();
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
        
        usuariDAO = new UsuariDAO(conexio);
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
    
    
    
    /**Métode que genera la resoposta a una solicitud d'alta d'un empleat i el seu
     * usuari associat
     * 
     * @param empleat que es vol donar d'alta
     * @return codi correcte o d'error
     */
    public RetornDades respostaAltaEmpleat(Empleat empleat, Usuari usuari){
        try {
            //Si totes les accions no son correctes no es fa cap
            conexio.setAutoCommit(false);
            //Obtenim idPersona insertada
            PersonaDAO personaDAO = new PersonaDAO(conexio);
            int idPersona = personaDAO.altaPersona(empleat);
            //Instanciem la classe EmpleatDAO
            EmpleatDAO empleatDAO = new EmpleatDAO(conexio);
            //Codi que ens indica quantes files s'han insertat
            int codiAltaEmpleat = empleatDAO.altaEmpleat(empleat, idPersona);
            //Alta del usuari asociat a l'empleat
            UsuariDAO usuariDAO = new UsuariDAO(conexio);
            int codiAltaUsuari = usuariDAO.altaUsuari(usuari, idPersona);
            //Comprobem que l'entitat Empleat s'ha insertat
            if(codiAltaEmpleat > 0 && codiAltaUsuari > 0){
                conexio.commit();
                LOGGER.info("L'empleat i l'usuari s'han insertat correctament");
                return resposta = new RetornDades(CODI_CORRECTE);
            }else{
                //Desfem tots els possibles canvis
                conexio.rollback();
                LOGGER.warning("ERROR No s'ha pogut insertar l'empleat ni l'usari associat");
                //Si no s'ha insertat cap fila retornem codi d'error
                return resposta = new RetornDades(CODI_ERROR);
            }
        } catch (SQLException ex) {
            try {
                conexio.rollback();
                Logger.getLogger(GenerarResposta.class.getName()).log(Level.SEVERE,
                        "ERROR al intentar donar d'alta un empleat i el seu usuari", ex);
            } catch (SQLException ex1) {
                Logger.getLogger(GenerarResposta.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return resposta = new RetornDades(CODI_ERROR);
    }
    
    
    
    /**Mètode que retorna les dades de la persona vinculada al usuari que ha iniciat
     * sessió.
     * @param numSessio del usuari amb la sessió activa
     * @return dades de la persona vinculada al usuari
     */
    public RetornDades respostaConsultaPersona(String numSessio){
        //Obtenim id d'usari a partir del numero de sessió
        int idUsuari = sessions.idUsuariConectat(numSessio);
        //Instanciem la classe PersonaDAO que fará la consulta
        PersonaDAO personaDAO = new PersonaDAO(conexio);
        //Fem la consulta per obtenir les dades de la persona
        Persona persona = personaDAO.consultaPersona(idUsuari);
        //Comprobem el resultat, si es correcte enviem les dades de la persona
        if(persona != null){
            resposta = new RetornDades(CODI_CORRECTE);
            resposta.afegirDades(persona);
            return resposta;
    }else{
            return resposta = new RetornDades(CODI_ERROR);
        }
    }
    
    
    
    /**Métode que generarà la resposta segons la informació obtinguda de l'execució
     * a la base de dades
     * @param personaNova noves dades del perfil 
     * @return 
     */
    public RetornDades respostaModificarPersona(Persona personaNova){
        PersonaDAO personaDAO = new PersonaDAO(conexio);
        //Solicitem la modificació
        int filesAfectades = personaDAO.modificarPerfil(personaNova);
        //Comprovem si hi ha files afectades
        if(filesAfectades > 0){
            return resposta = new RetornDades(CODI_CORRECTE);
        }else{
            return resposta = new RetornDades(CODI_ERROR);
        }
    }
    
    
    
    /**Métode per generar la resposta amb la llista d'empleats
     * 
     * @return reposta
     */
    public RetornDades respostaLlistarEmpleats(){
        ArrayList<Empleat> llistaEmpleats = new ArrayList();
        EmpleatDAO empleatDAO = new EmpleatDAO(conexio);
        //Es demana la llista d'empleats a EmpleatDAO
        llistaEmpleats = empleatDAO.llistarEmpleats();
        //Si la llista no está buida omplim la resposta amb els empleats
        if(!llistaEmpleats.isEmpty()){           
            resposta = new RetornDades(CODI_CORRECTE);
            //Afegim el tamany de l'array list per al client
            resposta.afegirDades(llistaEmpleats.size());
            for(Empleat empleat: llistaEmpleats){
                resposta.afegirDades(empleat);
            }                      
            LOGGER.info("Resposta amb la llista d'empleats");
            
        }else{
            LOGGER.info("Llista empleats buida");
            return resposta = new RetornDades(CODI_ERROR);
        }
        
        return resposta;
   
    }
}
