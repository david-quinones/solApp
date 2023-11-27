package persistencia;

import entitats.Aula;
import entitats.Empleat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
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
    
    
    
    /**Mètode per eliminar una aula de la base de dades sempre que no tingui alumnes associats
     * 
     * @param idAula que s'ha d'eliminar
     * @return codi del resultat
     */
    public int eliminarAula(int idAula){
        try {
            //Delete de la base de dades sempre que l'aula no tingui alumnes associats
            String eliminarAula = "DELETE FROM aula WHERE id = ? "
                    + "AND (SELECT COUNT(*) FROM ALUMNE WHERE aula_id = ?) = 0;";
            psAula = conexio.prepareStatement(eliminarAula);
            
            //Establim les dades per al delete
            psAula.setInt(1, idAula);
            psAula.setInt(2, idAula);
            
            //Comprovem execució
            int filesAfectades = psAula.executeUpdate();
            if(filesAfectades > 0){
                LOGGER.info("L'aula amb id " + idAula + " s'ha eliminat correctament");
                return CORRECTE;
                
            }else{
                LOGGER.warning("L'aula no s'ha pogut eliminar, te alumnes associats o l'id no es correcte");
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(AulaDAO.class.getName()).log(Level.SEVERE, 
                    "ERROR al intentar eliminar l'aula de la base de dades", ex);
        }
        return ERROR;
    }
    
    
    /**Mètode per modifcar les dades d'una aula a la base de dades
     * 
     * @param aula que s'ha de modificar
     * @return resultat de la crida
     */
    public int modificarAula(Aula aula){
        try {
            //Sentència SQL
            String modificarAula = "UPDATE aula SET nom = ?, professor_id = ? "
                    + "WHERE id = ?;";
            psAula = conexio.prepareStatement(modificarAula);
            
            //Establim les dades per fer la modificació
            psAula.setString(1, aula.getNomAula());
            if(aula.getEmpleat() != null){
                psAula.setInt(2, aula.getEmpleat().getIdEmpleat());
            }else{
                psAula.setNull(2, Types.INTEGER);
            }
            psAula.setInt(3, aula.getId());
            
            //Comprovem execució
            int filesAfectades = psAula.executeUpdate();
            if(filesAfectades > 0){
                LOGGER.info("L'aula amb id " + aula.getId() + " s'ha modificat correctament");
                return CORRECTE;
            }else{
                LOGGER.warning("No s'ha pogut modifcar l'aula id no existeix o es null");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AulaDAO.class.getName()).log(Level.SEVERE, 
                    "ERROR al intentar modifcar l'aula", ex);
        }
        return ERROR;
    }
    
    
    
    /**Mètode per llistar les aules de la base de dades amb el professor assignat i
     * alumnes associats
     * @return ArrayList amb totes les aules
     */
    public ArrayList llistaAula (){
        //ArrayList amb la que contindrà la llista d'aules
        ArrayList<Aula> llistaAules = new ArrayList<>();
        try {
            String llistaAulesSQL = "SELECT * FROM aula;";
            psAula = conexio.prepareStatement(llistaAulesSQL);
            
            //Executem la consulta
            ResultSet rs = psAula.executeQuery();
            //Obtenim totes les aules
            while(rs.next()){
                llistaAules.add(obtindreAula(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AulaDAO.class.getName()).log(Level.SEVERE, 
                    "ERROR al intentar llistar les aules", ex);
        }
        return llistaAules;
    }
    
    
    /**Mètode per obrtindre una aula a partir d'un ResultSet
     * 
     * @param dades resultSet que es rep
     * @return objecte Aula
     */
    public Aula obtindreAula(ResultSet dades){
        Aula aula = new Aula();
        try {            
            aula.setId(dades.getInt("id"));
            aula.setNomAula(dades.getString("nom"));
            //Obtindre les dades de l'empleat
            EmpleatDAO empleatDAO = new EmpleatDAO(conexio);
            Empleat empleat = new Empleat();
            Object idEmpleat = dades.getInt("professor_id");
            //Comprovem que l'aula té un professor associat
            if(idEmpleat != null){
                empleat = empleatDAO.consultaEmpleat(dades.getInt("professor_id"));
            }else{
                empleat = null;
            }
            aula.setEmpleat(empleat);
            //Obtindre la llista d'alumnes associats a l'aula
            AlumneDAO alumneDAO = new AlumneDAO(conexio);
            aula.setAlumnes(alumneDAO.llistaAlumnesAula(aula.getId()));
            LOGGER.info("S'ha obtingut l'aula amb id: " + aula.getId());
            
        } catch (SQLException ex) {
            Logger.getLogger(AulaDAO.class.getName()).log(Level.SEVERE,
                    "ERROR al obtindre les dades de una aula", ex);
        }
        return aula;
    }
    

}
