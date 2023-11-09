package persistencia;

import entitats.Empleat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**Clase que proporciona métodes per l'alta, modificació i baixa de dades relacionades
 * amb l'entitat Empleat.
 *
 * @author Pau Castell Galtes
 */
public class EmpleatDAO {
    private Connection conexio;
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
    public int altaEmpleat(Empleat empleat, int idPersona){
        try {
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
    
    /**Métode per obtindre una llista amb tots els empleats que consten a la base
     * de dades.
     * 
     * @return llista amb els empleats
     */
    public ArrayList llistarEmpleats(){
        try {
            LOGGER.info("Consulta per llistar tots els empleats");
            //Array que contindrà la llista d'empleats
            ArrayList<Empleat> llistaEmpleats = new ArrayList();
            
            String consulta = "SELECT *" +
                       " FROM empleat e INNER JOIN persona p "
                    +  "ON e.persona_id = p.id;";
            psEmpleat = conexio.prepareStatement(consulta);
            
            ResultSet rs = psEmpleat.executeQuery();
            
            while(rs.next()){
                llistaEmpleats.add(obtindreEmpleat(rs));
            }
            
            return llistaEmpleats;
            
            
        } catch (SQLException ex) {
            Logger.getLogger(EmpleatDAO.class.getName()).log(Level.SEVERE,
                    "Error al intentar realitzar la conulta", ex);
        }  
        
        return llistarEmpleats();
    }
    
    /**Métode per obrindre un objecte Empleat a partir d'un ResultSet de la base de dades
     * 
     * @param dadesObtingudes de la base de dades
     * @return objecte Empleat
     */
    public Empleat obtindreEmpleat(ResultSet dadesObtingudes){
        Empleat empleat = new Empleat();
        try {
            empleat.setIdEmpleat(dadesObtingudes.getInt("e.id"));
            empleat.setIdPersona(dadesObtingudes.getInt("p.id"));
            empleat.setNom(dadesObtingudes.getString("nom"));
            empleat.setCognom1(dadesObtingudes.getString("cognom1"));
            empleat.setCognom2(dadesObtingudes.getString("cognom2"));
            empleat.setData_naixement(dadesObtingudes.getString("data_naixement"));
            empleat.setDni(dadesObtingudes.getString("dni"));
            empleat.setTelefon(dadesObtingudes.getString("telefon"));
            empleat.setMail(dadesObtingudes.getString("mail"));
            LOGGER.info("Obtingut empleat amb idEmpleat: " + empleat.getIdEmpleat());
            
            return empleat;
            
        } catch (SQLException ex) {
            Logger.getLogger(EmpleatDAO.class.getName()).log(Level.SEVERE,
                    "Error al obtindre l'empleat", ex);
        }
        return empleat;
    }
}
