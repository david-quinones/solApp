package persistencia;

import entitats.Empleat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**Clase que proporciona métodes per l'alta, modificació i baixa de dades relacionades
 * amb l'entitat Empleat.
 *
 * @author Pau Castell Galtes
 */
public class EmpleatDAO {
    private Connection conexio;
    private PersonaDAO personaDAO;
    private PreparedStatement psEmpleat;
    private static final Logger LOGGER = Logger.getLogger(PersonaDAO.class.getName());
    private static final int CORRECTE = 1;
    private static final int ERROR = -1;
    
    /**Constructor heredat de la classe PersonaDAO
     * 
     * @param conexio
     */
    public EmpleatDAO(Connection conexio){
        this.conexio = conexio;
        
    }
    
    /**Métode per insertar entitats de la clase Empleat a la base de dades.
     * 
     * @param empleat dades a insertar
     * @return codi del resultat > 0 insert correcte
     */
    public int altaEmpleat(Empleat empleat){
        try {
            PersonaDAO personaDAO = new PersonaDAO(conexio);
            int idPersona = personaDAO.altaPersona(empleat);
            
            //Ordre per insertar l'empleat a la base de dades
            String insertarEmpleat = "INSERT INTO empleat (actiu, inici_contracte, final_contracte, persona_id)"
                    + "VALUES (?,?,?,?)";
            psEmpleat = conexio.prepareStatement(insertarEmpleat);
            
            //Establim les dades que cal insertar
            psEmpleat.setBoolean(1, empleat.isActiu());
            psEmpleat.setString(2, empleat.getIniciContracte());
            psEmpleat.setString(3, empleat.getFinalContracte());
            psEmpleat.setInt(4, idPersona);
            
            //Comprobem s'hi s'ha insertat correctement
            int filesAfectades = psEmpleat.executeUpdate();
            if(filesAfectades > 0){
                LOGGER.log(Level.INFO, "S'ha insertat " + filesAfectades + " empleats");
                return CORRECTE;     
        }else{
                LOGGER.info("Error al insertar l'empleat");
                return ERROR;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(EmpleatDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
    }
}