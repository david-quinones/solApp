package persistencia;

import entitats.Alumne;
import entitats.Missatge;
import entitats.Persona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**Classe que porporciona els mètodes per gestionar l'entitat Missatge a la 
 * base de dades
 *
 * @author Pau Castell Galtes
 */
public class MissatgeDAO {
    private Connection conexio;
    private PreparedStatement psMissatge;
    private static final Logger LOGGER = Logger.getLogger(MissatgeDAO.class.getName());
    private static final int CORRECTE = 1;
    private static final int ERROR = -1;
    
    
    /**Constructor de la clase
     * 
     * @param conexio 
     */
    public MissatgeDAO(Connection conexio) {
        this.conexio = conexio;
    }
    
    
    /**Mètode per afegir un nou missatge a la base de dades que rebrà el remitent
     * del mateix.
     * @return codi del resultat
     */
    public int altaMissatge(Missatge missatge, int destinatari){
        try {
            //Ordre SQL per guardar el nou missatge
            String altaMissatge = "INSERT INTO missatge (remitent_id, destinatari_id,"
                    + " contingut, data_enviament) VALUES (?,?,?,?);";
            psMissatge = conexio.prepareStatement(altaMissatge);
            
            //Establim les dades per executar l'ordre
            psMissatge.setInt(1, missatge.getRemitentPersona().getIdPersona());
            psMissatge.setInt(2, destinatari);
            psMissatge.setString(3, missatge.getContingut());
            psMissatge.setString(4, missatge.getDataEnviament());
            
            //Executem la consulta i comprovem si hi ha files afectades
            int filesAfectades = psMissatge.executeUpdate();
            if(filesAfectades > 0){
                LOGGER.info("Missatge guardat a la base de dades, destinatari id: " + destinatari);
                return CORRECTE;
            }else{
                LOGGER.warning("El missatge no s'ha pogut donar d'alta");
                return ERROR;
            }
        } catch (SQLException ex) {
            Logger.getLogger(MissatgeDAO.class.getName()).log(Level.SEVERE,
                    "ERROR al intentar donar d'alta un nou missatge", ex);
        }
        
        return ERROR;
    }
    
    /**Mètode per llistar els missatges que ha rebut un usuari
     * 
     * @param idPersona que ha rebut els missatges
     * @return Array amb la llista de missatges
     */
    public ArrayList llistarMissatgesRebuts(int idPersona){
        ArrayList<Missatge> llistaMissatges = new ArrayList<>();
        try {
            String llistarMissatgesRebuts = "SELECT * FROM missatge WHERE destinatari_id = ? "
                    + "AND destinatari_esborrat = false;";
            psMissatge = conexio.prepareStatement(llistarMissatgesRebuts);
            
            //Establim les dades per a la consulta
            psMissatge.setInt(1, idPersona);
            //Obtenim les dades de la bd
            ResultSet rs = psMissatge.executeQuery();
            while(rs.next()){
                llistaMissatges.add(obtindreMissatge(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MissatgeDAO.class.getName()).log(Level.SEVERE, 
                    "ERROR al intentar llistar els missatges rebuts", ex);
        }
        
        return llistaMissatges;
    }
    
    
    /**Mètode per obtindre un Missatge a partir del resultSet d'una consulta
     * 
     * @param rs de la consulta
     * @return objecte Missatge
     */
    public Missatge obtindreMissatge(ResultSet rs){
        Missatge missatge = new Missatge();
        try {           
            missatge.setIdMissatge(rs.getInt("id"));
            //Obtenim el remitent a partir del id de la persona
            int idPersona = rs.getInt("remitent_id");
            PersonaDAO personaDAO = new PersonaDAO(conexio);
            Persona persona = personaDAO.consultaPersonaIdPersona(idPersona);
            missatge.setRemitentPersona(persona);
            //Obtenim el destinatari a partir del id
            idPersona = rs.getInt("destinatari_id");
            persona = personaDAO.consultaPersonaIdPersona(idPersona);
            missatge.getDestinataris().add(persona);
            //Obtenim el contingut
            missatge.setContingut(rs.getString("contingut"));
            //Obtenim data del missatge
            missatge.setDataEnviament("data_enviament");
            
            LOGGER.info("S'ha obtingut el missatge amb id: " + missatge.getIdMissatge());           
            
        } catch (SQLException ex) {
            Logger.getLogger(MissatgeDAO.class.getName()).log(Level.SEVERE, 
                    "ERROR al obtindre el missatge", ex);
        }
        return missatge;
    }
    
    
    
    
    /**Mètode per llistar els missatges enviats per un usuari
     * 
     * @param idPersona associada al usuari
     * @return ArrayList amb la llista de missatges
     */
    public ArrayList llistarMissatgesEnviats(int idPersona){
        ArrayList<Missatge> llistaMissatges = new ArrayList<>();
        try {
            String llistarMissatgesRebuts = "SELECT * FROM missatge WHERE remitent_id = ? "
                    + "AND remitent_esborrat = false;";
            psMissatge = conexio.prepareStatement(llistarMissatgesRebuts);
            
            //Establim les dades per a la consulta
            psMissatge.setInt(1, idPersona);
            //Obtenim les dades de la bd
            ResultSet rs = psMissatge.executeQuery();
            while(rs.next()){
                llistaMissatges.add(obtindreMissatge(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MissatgeDAO.class.getName()).log(Level.SEVERE, 
                    "ERROR al intentar llistar els missatges rebuts", ex);
        }
        
        return llistaMissatges;
    }
    
    
    
    /**Mètode per eliminar els missatges d'un usuari
     * 
     * @param missatge que s'ha d'esborrar
     * @return codi del resultat
     */
    public int eliminarMissatge (Missatge missatge){
        try {
            String eliminarMissatge = "UPDATE missatge SET remitent_esborrat = ?, destinatari_esborrat = ? "
                    + "WHERE id = ?;";
            psMissatge = conexio.prepareStatement(eliminarMissatge);
            
            //Establim les dade per a la modificació
            psMissatge.setBoolean(1, missatge.isRemitentEsborrat());
            psMissatge.setBoolean(2, missatge.isDestinatariEsborrat());
            psMissatge.setInt(3, missatge.getIdMissatge());
            //Comprovem si s'ha esborrat alguna fila
            int filesAfectades = psMissatge.executeUpdate();
            if(filesAfectades > 0){
                LOGGER.info("Missatge esborrat correctament amb id: " + missatge.getIdMissatge());
                return CORRECTE;
            }else{
                LOGGER.info("No s'ha pogut eliminar el missatge amb id: " + missatge.getIdMissatge());
            }
        } catch (SQLException ex) {
            Logger.getLogger(MissatgeDAO.class.getName()).log(Level.SEVERE,
                    "ERROR al intentar esborrar el missatge", ex);
        }
        return ERROR;
    }
}
