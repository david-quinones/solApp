package resposta;

import entitats.*;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencia.ConexioBBDD;
import persistencia.*;
import seguretat.GestorSessions;
import utilitats.Utils;

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
    private static final Logger LOGGER = Logger.getLogger(GenerarResposta.class.getName());
    
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
     * @param usuari
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
    
    /**Mètode per generar la resposta de la crida eliminar usuari
     * 
     * @param persona associada al usuari que s'ha d'eliminar
     * @return codi amb el resultat
     */
    public RetornDades respostaEliminarUsuari(Persona persona){      
        //Comprovem que les dades rebudes són vàlides
        if(persona != null ){
            //Obtenim id de la persona associada a l'empleat
            int idPersona = persona.getIdPersona();
            UsuariDAO usuariDAO = new UsuariDAO(conexio);
            int resultat = usuariDAO.eliminarUsuari(idPersona);
            if(resultat > 0){
                LOGGER.info("Usuari amb id persona " + persona.getIdPersona() + " eliminat.");
                return resposta = new RetornDades(CODI_CORRECTE);
            }else{
                LOGGER.warning("ERROR id no existeix o es cero.");
                return resposta = new RetornDades(CODI_ERROR);
            }
        }else{
            LOGGER.warning("ERROR: Persona null");
            return resposta = new RetornDades(CODI_ERROR);
        }
    }
    
    
    /**Mètode per generar la resposta i modificar el empleat
     * 
     * @param empleat noves dades del empleat
     * @return resposta amb el codi corresponent
     */
    public RetornDades respostaModificarEmpleat(Empleat empleat){
        try {
            //Si totes les accions no son correctes no es fa cap
            conexio.setAutoCommit(false);
            //Executem la modificació
            EmpleatDAO empleatDAO = new EmpleatDAO(conexio);
            int modificarEmpleat = empleatDAO.modificarEmpleat(empleat);
            PersonaDAO personaDAO = new PersonaDAO(conexio);
            int modificarPersona = personaDAO.modificarPerfil(empleat);
            //Comprovem el resultat obtingut
            if(modificarEmpleat > 0 && modificarPersona > 0){
                conexio.commit();
                LOGGER.info("La resposta s'ha generat correctament amb codi " + CODI_CORRECTE);
                return resposta = new RetornDades(CODI_CORRECTE);
            }else{
                conexio.rollback();
                LOGGER.warning("No s'ha pogut modificar el empleat. Desfem possibles canvis");
                return resposta = new RetornDades(CODI_ERROR);
            }
        } catch (SQLException ex) {
            try {
                conexio.rollback();
                Logger.getLogger(GenerarResposta.class.getName()).log(Level.SEVERE,
                        "Error en el commit de la base de dades, desfem possibles canvis", ex);
            } catch (SQLException ex1) {
                Logger.getLogger(GenerarResposta.class.getName()).log(Level.SEVERE,
                        "Error en el rollback de la base de dades, desfem possibles canvis", ex1);
            }
        }
        return resposta = new RetornDades(CODI_ERROR);
    }
    
    /**Mètode per obtindre la resposat a la crida llistar alumnes
     * 
     * @return llista amb tots els alumnes
     */
    public RetornDades respostaLlistarAlumnes(){
        ArrayList<Alumne> llistaAlumnes = new ArrayList();
        AlumneDAO alumneDAO = new AlumneDAO(conexio);
        //Es demana la llista d'alumnes a AlumneDAO
        llistaAlumnes= alumneDAO.llistarAlumnes();
        //Si la llista no está buida omplim la resposta amb els alumnes
        if(!llistaAlumnes.isEmpty()){           
            resposta = new RetornDades(CODI_CORRECTE);
            //Afegim el tamany de l'array list per al client
            resposta.afegirDades(llistaAlumnes.size());
            for(Alumne alumne: llistaAlumnes){
                resposta.afegirDades(alumne);
            }                      
            LOGGER.info("Resposta amb la llista d'alumnes");
            
        }else{
            LOGGER.info("Llista d'alumnes buida");
            return resposta = new RetornDades(CODI_ERROR);
        }
        
        return resposta;
    }
    
    
    /**Mètode per afegir a la base de dades un alumne, la persona associada i l'usuari
     * que tindrà i generar la resposta.
     * @param alumne que s'ha d'insertar
     * @param usuari associat a l'alumne
     * @return 
     */
    public RetornDades respostaAltaAlumne(Alumne alumne, Usuari usuari){
        try {
            //Si totes les accions no son correctes no es fa cap
            conexio.setAutoCommit(false);
            //Obtenim idPersona insertada
            PersonaDAO personaDAO = new PersonaDAO(conexio);
            int idPersona = personaDAO.altaPersona(alumne);
            //Instanciem la classe AlumneDAO
            AlumneDAO alumneDAO = new AlumneDAO(conexio);
            //Codi que ens indica quantes files s'han insertat
            int codiAltaAlumne = alumneDAO.altaAlumne(alumne, idPersona);
            //Alta del usuari asociat a l'alumne
            UsuariDAO usuariDAO = new UsuariDAO(conexio);
            int codiAltaUsuari = usuariDAO.altaUsuari(usuari, idPersona);
            //Comprobem que l'entitat Alumne s'ha insertat
            if(codiAltaAlumne > 0 && codiAltaUsuari > 0){
                conexio.commit();
                LOGGER.info("L'alumne i l'usuari s'han insertat correctament");
                return resposta = new RetornDades(CODI_CORRECTE);
            }else{
                //Desfem tots els possibles canvis
                conexio.rollback();
                LOGGER.warning("ERROR No s'ha pogut insertar l'alumne ni l'usari associat");
                //Si no s'ha insertat cap fila retornem codi d'error
                return resposta = new RetornDades(CODI_ERROR);
            }
        } catch (SQLException ex) {
            try {
                conexio.rollback();
                Logger.getLogger(GenerarResposta.class.getName()).log(Level.SEVERE,
                        "ERROR al intentar donar d'alta un alumne i el seu usuari", ex);
            } catch (SQLException ex1) {
                Logger.getLogger(GenerarResposta.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

        return resposta = new RetornDades(CODI_ERROR);
    }
    
    
    
    /**Mètode que genera la resposta que s'enviarà al client a la crida modificar_alumne
     * 
     * @param alumne que s'ha de modificar
     * @return codi amb el resultat
     */
    public RetornDades respostaModificarAlumne(Alumne alumne){
        try {
            //Si totes les accions no son correctes no es fa cap
            conexio.setAutoCommit(false);
            //Executem la modificació
            AlumneDAO alumneDAO = new AlumneDAO(conexio);
            int modificarAlumne = alumneDAO.modificarAlumne(alumne);
            PersonaDAO personaDAO = new PersonaDAO(conexio);
            int modificarPersona = personaDAO.modificarPerfil(alumne);
            //Comprovem el resultat obtingut
            if(modificarAlumne > 0 && modificarPersona > 0){
                conexio.commit();
                LOGGER.info("La resposta s'ha generat correctament amb codi " + CODI_CORRECTE);
                return resposta = new RetornDades(CODI_CORRECTE);
            }else{
                conexio.rollback();
                LOGGER.warning("No s'ha pogut modificar l'alumne. Desfem possibles canvis");
                return resposta = new RetornDades(CODI_ERROR);
            }
        } catch (SQLException ex) {
            try {
                conexio.rollback();
                Logger.getLogger(GenerarResposta.class.getName()).log(Level.SEVERE,
                        "Error en el commit de la base de dades, desfem possibles canvis", ex);
            } catch (SQLException ex1) {
                Logger.getLogger(GenerarResposta.class.getName()).log(Level.SEVERE,
                        "Error en el rollback de la base de dades, desfem possibles canvis", ex1);
            }
        }
        return resposta = new RetornDades(CODI_ERROR);
    }
    
    
    
    /**Mètode per generar la resposat a la crida llistar usuaris
     * 
     * @return llista amb els usuaris de la bbdd
     */
    public RetornDades respostaLlistarUsuaris(){
        ArrayList<Usuari> llistaUsuaris = new ArrayList();
        UsuariDAO usuariDAO = new UsuariDAO(conexio);
        //Es demana la llista d'usuaris a UsuariDAO
        llistaUsuaris= usuariDAO.llistarUsuaris();
        //Si la llista no está buida omplim la resposta amb els alumnes
        if(!llistaUsuaris.isEmpty()){           
            resposta = new RetornDades(CODI_CORRECTE);
            //Afegim el tamany de l'array list per al client
            resposta.afegirDades(llistaUsuaris.size());
            for(Usuari usuari: llistaUsuaris){
                resposta.afegirDades(usuari);
            }                      
            LOGGER.info("Resposta amb la llista d'usuaris");
            
        }else{
            LOGGER.info("Llista d'usuaris buida");
            return resposta = new RetornDades(CODI_ERROR);
        }
        
        return resposta;
    }
    
    
    /**Mètode per generar la resposta a la crida modificar usuari
     * 
     * @param usuari amb les dades noves
     * @return codi del resultat
     */
    public RetornDades respostaModificarUsuari(Usuari usuari){
        //Executem la modificació
        UsuariDAO usuariDAO = new UsuariDAO(conexio);
        int modificarUsuari = usuariDAO.modificarUsuari(usuari);
        //Comprovem el resultat obtingut
        if(modificarUsuari > 0){
            LOGGER.info("La resposta s'ha generat correctament amb codi " + CODI_CORRECTE);
            return resposta = new RetornDades(CODI_CORRECTE);
        }else{
            LOGGER.warning("No s'ha pogut modificar l'usuari.");
            return resposta = new RetornDades(CODI_ERROR);
        }
    }
    
    
    
    /**Mètode per retornar les dades a la crida altaAula
     * 
     * @param aula que s'ha de donar d'alta
     * @return resposta
     */
    public RetornDades respostaAltaAula(Aula aula){
        //Executem l'alta del aula
        AulaDAO aulaDAO = new AulaDAO(conexio);
        int altaAula = aulaDAO.altaAula(aula);
        //Comprovem el resultat
        if(altaAula > 0){
            ArrayList<Alumne> llistaAlumnes = aula.getAlumnes();
            if(!llistaAlumnes.isEmpty()){
                AlumneDAO alumneDAO = new AlumneDAO(conexio);
                for(Alumne alumne: llistaAlumnes){
                    alumneDAO.afegirAlumneAula(alumne, altaAula);
                }
            }else{
                LOGGER.info("La llista d'alumnes està buida");
            }            
        }else{
            LOGGER.warning("L'alta de l'aula no s'ha realitzat");
            return resposta = new RetornDades(CODI_ERROR);
        }
        return resposta = new RetornDades(CODI_CORRECTE);
    }
    
    
    /**Mètode per generar la resposta corresponent a la crida eliminar_aula
     * 
     * @param aula que s'ha d'eliminar
     * @return resposta amb codi del resultat
     */
    public RetornDades respostaEliminarAula(Aula aula){
        AulaDAO aulaDAO = new AulaDAO(conexio);
        //Executem la consulta
        int eliminarAula = aulaDAO.eliminarAula(aula.getId());
        //Comprovem resultat i enviem resposta corresponent.
        if(eliminarAula > 0){
            LOGGER.info("S'ha genereat la resposta eliminarAula aula eliminada.");
            return resposta = new RetornDades(CODI_CORRECTE);
        }else{
            LOGGER.warning("No s'ha pogut eliminar l'aula");
            return resposta = new RetornDades(CODI_ERROR);
        }
    }
    
    
    /**Mètode per generar la resposta corresponent a la crida modificar_aula
     * 
     * @param aula que s'ha de modificar
     * @return resposta amb el resultat
     */
    public RetornDades respostaModificarAula(Aula aula){
        try {
            conexio.setAutoCommit(false);
            ArrayList<Alumne> llistaAlumnes = new ArrayList<>();
            AulaDAO aulaDAO = new AulaDAO(conexio);
            //Executem la modificació
            int modificarAula = aulaDAO.modificarAula(aula);
            if(modificarAula > 0){
                llistaAlumnes = aula.getAlumnes();
                if(!llistaAlumnes.isEmpty()){
                    AlumneDAO alumneDAO = new AlumneDAO(conexio);
                    alumneDAO.eliminarAlumneAula(aula.getId());
                    for(Alumne alumne: llistaAlumnes){
                        alumneDAO.afegirAlumneAula(alumne, aula.getId());               
                    }
                }else{
                    LOGGER.info("La llista d'alumnes està buida");
                }
            }else{
                LOGGER.warning("La modificació no s'ha pogut realitzar");
                //Desfem posibles canvis
                conexio.rollback();
                return resposta = new RetornDades(CODI_ERROR);
            }
            //Acceptem els canvis
            conexio.commit();
            return resposta = new RetornDades(CODI_CORRECTE);
        } catch (SQLException ex) {
            Logger.getLogger(GenerarResposta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resposta = new RetornDades(CODI_ERROR);
        }
    
    
    /**Mètode per restornar la resposta a la crida llista_aules
     * 
     * @return resposta
     */
    public RetornDades respostaLlistaAules(){
        AulaDAO aulaDAO = new AulaDAO(conexio);
        //Array amb la llista d'aules
        ArrayList<Aula> llistaAules = aulaDAO.llistaAula();
        if(!llistaAules.isEmpty()){
            resposta = new RetornDades(CODI_CORRECTE);
            //Afegim el tamany de l'array d'aules
            resposta.afegirDades(llistaAules.size());
            //Afegim les aules a la resposta
            for(Aula aula: llistaAules){
                resposta.afegirDades(aula);
            }
            LOGGER.info("Resposta amb una llista d'aules");
            //Retornem resposta
            return resposta;
            
        }else{
            LOGGER.warning("La llista d'aules està buida");
            return resposta = new RetornDades(CODI_ERROR);
        }
    }
    
    
    /**Mètode per gestionar la resposta a la crida enviar_missatge
     * 
     * @param missatge que s'ha d'enviar
     * @return resposta 
     */
    public RetornDades respostaEnviarMissatge(Missatge missatge, String numSessio){
        try {
            //Obtenim l'id del remitent a partir del número de sessió
            UsuariDAO usuariDAO = new UsuariDAO(conexio);
            Usuari usuari = usuariDAO.consultaUsuari(sessions.idUsuariConectat(numSessio));
            PersonaDAO personaDAO = new PersonaDAO(conexio);
            missatge.setRemitentPersona(personaDAO.consultaPersona(usuari.getId()));
            
            int filesAfectades = 0;
            //Si no s'envien tots els missatges no s'envia cap
            conexio.setAutoCommit(false);
            MissatgeDAO missatgeDAO = new MissatgeDAO(conexio);
            //Array amb els destinataris
            ArrayList<Persona> destinataris = missatge.getDestinataris();
            //Guardem un missatge per cada destinatari
            for(Persona persona: destinataris){
                String data = Utils.formatDataHoraMinuts(LocalDateTime.now());
                missatge.setDataEnviament(data);
                filesAfectades += missatgeDAO.altaMissatge(missatge, persona.getIdPersona());
            }
            //Comprovem que s'hane enviat tots els missatges
            if(filesAfectades == destinataris.size()){
                LOGGER.info("Tots els missatges s'han enviat correctament");
                conexio.commit();
                return resposta = new RetornDades(CODI_CORRECTE);
            }else{
                LOGGER.warning("ERROR al enviar els missatges");      
            }
        } catch (SQLException ex) {
            Logger.getLogger(GenerarResposta.class.getName()).log(Level.SEVERE,
                    "ERROR al intentar generar la resposta, missatges no enviats", ex);
        }
        
        return resposta = new RetornDades(CODI_ERROR);
    }
    
    
    
    /**Mètode per generar la resposta a la crida llistarMissatgesRebuts
     * 
     * @param numSessio
     * @return 
     */
    public RetornDades respostaLlistarMissatgesRebuts(String numSessio){
        //Obtenim les dades de l'usuari conectat
        UsuariDAO usuariDAO = new UsuariDAO(conexio);
        Usuari usuari = usuariDAO.consultaUsuari(sessions.idUsuariConectat(numSessio));
        //Obtenim les dades de Persona de l'usuari connectat
        PersonaDAO personaDAO = new PersonaDAO(conexio);
        Persona persona = personaDAO.consultaPersona(usuari.getId());
        //Obtenim llista dels missatges rebuts de la Persona
        MissatgeDAO missatgeDAO = new MissatgeDAO(conexio);
        ArrayList<Missatge> llistaMissatges = missatgeDAO.llistarMissatgesRebuts(persona.getIdPersona());
        //Afegim el número de resultat obtinguts a la resposta
        if(!llistaMissatges.isEmpty()){
            resposta = new RetornDades(CODI_CORRECTE);
            //Afegim tamany de la llista
            resposta.afegirDades(llistaMissatges.size());
            //Afegim tots els missatges
            for(Missatge missatge: llistaMissatges){
                resposta.afegirDades(missatge);
            }
            LOGGER.info("Resposta amb la llista de missatges");
            return resposta;
        }else{
            LOGGER.warning("La llista de missatges està buida.");
            return resposta = new RetornDades(CODI_ERROR);
        }
    }
    
    
    
    /**Mètode per generar la resposta a la crida llistarMissatgesEnviats
     * 
     * @param numSessio
     * @return 
     */
    public RetornDades respostaLlistarMissatgesEnviats(String numSessio){
        //Obtenim les dades de l'usuari conectat
        UsuariDAO usuariDAO = new UsuariDAO(conexio);
        Usuari usuari = usuariDAO.consultaUsuari(sessions.idUsuariConectat(numSessio));
        //Obtenim les dades de Persona de l'usuari connectat
        PersonaDAO personaDAO = new PersonaDAO(conexio);
        Persona persona = personaDAO.consultaPersona(usuari.getId());
        //Obtenim llista dels missatges enviats de la Persona
        MissatgeDAO missatgeDAO = new MissatgeDAO(conexio);
        ArrayList<Missatge> llistaMissatges = missatgeDAO.llistarMissatgesEnviats(persona.getIdPersona());
        //Afegim el número de resultat obtinguts a la resposta
        if(!llistaMissatges.isEmpty()){
            resposta = new RetornDades(CODI_CORRECTE);
            //Afegim tamany de la llista
            resposta.afegirDades(llistaMissatges.size());
            //Afegim tots els missatges
            for(Missatge missatge: llistaMissatges){
                resposta.afegirDades(missatge);
            }
            LOGGER.info("Resposta amb la llista de missatges");
            return resposta;
        }else{
            LOGGER.warning("La llista de missatges està buida.");
            return resposta = new RetornDades(CODI_ERROR);
        }
    }
    
    
    
    /**Mètode per generar la resposta corresponent a la crida eliminarMissatge
     * 
     */
    public RetornDades respostaEliminarMissatge(Missatge missatge, String numSessio){
        MissatgeDAO missatgeDAO = new MissatgeDAO(conexio);
        int resultat = 0;
        //Obtenim les dades de l'usuari conectat
        UsuariDAO usuariDAO = new UsuariDAO(conexio);
        Usuari usuari = usuariDAO.consultaUsuari(sessions.idUsuariConectat(numSessio));
        //Obtenim les dades de Persona de l'usuari connectat
        PersonaDAO personaDAO = new PersonaDAO(conexio);
        Persona persona = personaDAO.consultaPersona(usuari.getId());
        //Comprovem si l'usuari es el remitent o destinatari del missatge
        if(missatge.getRemitentPersona().getIdPersona() == persona.getIdPersona()){
            //L'usuari es el remitent del missatge
            missatge.setRemitentEsborrat(true);
            missatge.setDestinatariEsborrat(false);
            //Executem l'esborrat
            resultat = missatgeDAO.eliminarMissatge(missatge);
        }else{
            //L'usuari es el remitent del missatge
            missatge.setRemitentEsborrat(false);
            missatge.setDestinatariEsborrat(true);
            //Executem l'esborrat
            resultat = missatgeDAO.eliminarMissatge(missatge);
        }
        //Comprovem el resultat de l'esborrat i generem resposta
        if(resultat > 0){
            LOGGER.info("Generem resposta missatge eliminat");
            return resposta = new RetornDades(CODI_CORRECTE);
        }else{
            LOGGER.warning("Generem resposta no s'ha eliminat el missatge");
            return resposta = new RetornDades(CODI_ERROR);
        }
    }
    }
        

