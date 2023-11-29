package persistencia;

import entitats.Alumne;
import entitats.Missatge;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
            psMissatge.setInt(1, missatge.getIdRemitentPersona());
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
}
