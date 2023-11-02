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
    
    public RetornDades respostaLlistarEmpleats(){
        //METODE SIMULAT PER LLISTAR EMPLEATS
        Empleat empleat1 = new Empleat(1, "Juan", "García", "Pérez", "1990-05-20", "12345678A", "123456789", "juan@email.com", 1, true, "2022-01-01", "2023-12-31");
        Empleat empleat2 = new Empleat(2, "María", "López", "González", "1988-07-15", "23456789B", "987654321", "maria@email.com", 2, true, "2021-12-01", "2023-11-30");
        Empleat empleat3 = new Empleat(3, "Pedro", "Martínez", "Sánchez", "1992-03-10", "34567890C", "456789123", "pedro@email.com", 3, true, "2022-02-15", "2023-12-15");
        Empleat empleat4 = new Empleat(4, "Ana", "Rodríguez", "Fernández", "1995-11-08", "45678901D", "987654321", "ana@email.com", 4, true, "2022-03-10", "2023-12-10");
        Empleat empleat5 = new Empleat(5, "Luis", "Pérez", "Gómez", "1991-09-25", "56789012E", "123456789", "luis@email.com", 5, true, "2022-04-05", "2023-12-05");
        Empleat empleat6 = new Empleat(6, "Carmen", "Sánchez", "Martínez", "1989-12-12", "67890123F", "987654321", "carmen@email.com", 6, true, "2022-05-01", "2023-12-01");
        Empleat empleat7 = new Empleat(7, "Javier", "Gómez", "Rodríguez", "1993-06-30", "78901234G", "456789123", "javier@email.com", 7, true, "2022-06-15", "2023-11-15");
        Empleat empleat8 = new Empleat(8, "Sofía", "Fernández", "López", "1996-02-18", "89012345H", "123456789", "sofia@email.com", 8, true, "2022-07-10", "2023-11-10");
        Empleat empleat9 = new Empleat(9, "Diego", "González", "Pérez", "1994-08-05", "90123456I", "987654321", "diego@email.com", 9, true, "2022-08-05", "2023-11-05");
        Empleat empleat10 = new Empleat(10, "Lucía", "Pérez", "Sánchez", "1990-04-22", "01234567J", "456789123", "lucia@email.com", 10, true, "2022-09-01", "2023-11-01");
        
        resposta = new RetornDades(CODI_CORRECTE);
        resposta.afegirDades(empleat1);
        resposta.afegirDades(empleat2);
        resposta.afegirDades(empleat3);
        resposta.afegirDades(empleat4);
        resposta.afegirDades(empleat5);
        resposta.afegirDades(empleat6);
        resposta.afegirDades(empleat7);
        resposta.afegirDades(empleat8);
        resposta.afegirDades(empleat9);
        resposta.afegirDades(empleat10);
        
        return resposta;
        
    }
}
