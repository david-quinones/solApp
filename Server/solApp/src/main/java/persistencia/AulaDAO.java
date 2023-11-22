package persistencia;

import entitats.Aula;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

/**Clase per gestionar les dades de les aules a la base de dades
 * 
 *
 * @author Pau Castell Galtes
 */
public class AulaDAO {
    private Connection conexio;
    private PreparedStatement psAula;
    private static final Logger LOGGER = Logger.getLogger(AulaDAO.class.getName());
    private static final int CORRECTE = 1;
    private static final int ERROR = -1;

    
    /**Constructor de la clase
     * 
     * @param conexio 
     */
    public AulaDAO(Connection conexio) {
        this.conexio = conexio;
    }
    
    
    public int altaAula(Aula aula){
        int id = 0;
        try {
            //Inserció a la base de dades SQL
            String altaAula = "INSERT INTO aula (nom, professor_id) "
                    + "VALUES (?,?);";
            psAula = conexio.prepareStatement(altaAula,PreparedStatement.RETURN_GENERATED_KEYS);
            
            //Afegim les dades a la inserció
            psAula.setString(1, aula.getNomAula());
            if(aula.getEmpleat() != null){
                psAula.setInt(2, aula.getEmpleat().getIdEmpleat());
            }else{
                psAula.setNull(2, Types.INTEGER);
            }
            
            //Executem la inserció i comprovem que s'ha inserit correctament
            psAula.executeUpdate();
            //Obtenim l'id de l'objecte insertat a la base de dades
            ResultSet idGenerat = psAula.getGeneratedKeys();
            if(idGenerat.next()){
                id = idGenerat.getInt(1);
                LOGGER.info("S'ha insertat la aula: " + aula.getNomAula() + " amb id: " + id);
                return id;
            }else{
                LOGGER.warning("ERROR al intentar inserir l'aula.");
                return ERROR;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AulaDAO.class.getName()).log(Level.SEVERE, 
                    "ERROR al intentar insertar l'aula a la base de dades", ex);
        }
        
        return ERROR;
    }

}
