package persistencia;

import entitats.Alumne;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**Classe que proporciona els mètodes per gestionar l'entitat Alumne a la base
 * de dades.
 *
 * @author Pau Castell Galtes
 */
public class AlumneDAO {
    private Connection conexio;
    private PreparedStatement psAlumne;
    private static final Logger LOGGER = Logger.getLogger(PersonaDAO.class.getName());
    private static final int CORRECTE = 1;
    private static final int ERROR = -1;

    /**Constructor de la classe Alumne
     * 
     * @param conexio a la base de dades
     */
    public AlumneDAO(Connection conexio) {
        this.conexio = conexio;
    }
    
    /**Mètode que retorna la llista de tots el alumnes a la base de dades
     * 
     * @return llista d'alumnes
     */
    public ArrayList llistarAlumnes(){
        //Array que contindrà la llista d'Alumnes
        ArrayList<Alumne> llistaAlumnes = new ArrayList<>();
        
        try {
            //Consulta a la base de dades
            String consultaSQL = "SELECT * FROM persona p"
                    + " INNER JOIN alumne a "
                    + "ON p.id = a.persona_id;";
            
            //Executem la consulta
            psAlumne = conexio.prepareStatement(consultaSQL);
            ResultSet rs = psAlumne.executeQuery();
            //Omplim l'array amb les dades rebudes
            while(rs.next()){
                llistaAlumnes.add(obtindreAlumne(rs));
            }
            
            return llistaAlumnes;
            
        } catch (SQLException ex) {
            Logger.getLogger(AlumneDAO.class.getName()).log(Level.SEVERE, 
                    "ERROR al obtindre un llistat d'alumnes", ex);
        }
        return llistaAlumnes;
    }
    
    
    /**Mètode per insertar entitats de la classe Alumne a la base de dades
     * 
     * @param alumne que hi ha que insertar
     * @param idPersona associada a l'alumne
     * @return codi estat
     */
    public int altaAlumne(Alumne alumne, int idPersona){
        try {
            String insertAlumne = "INSERT INTO alumne (actiu, menjador, acollida, persona_id)"
                    + " VALUES (?,?,?,?);";
            psAlumne = conexio.prepareStatement(insertAlumne);
            
            //Establim les dades que cal insertar
            psAlumne.setBoolean(1, alumne.isActiu());
            psAlumne.setBoolean(2,alumne.isMenjador());
            psAlumne.setBoolean(3, alumne.isAcollida());
            psAlumne.setInt(4, idPersona);
            
            //Comprovem s'hi s'ha insertat correctament
            int filesAfectades = psAlumne.executeUpdate();
            if(filesAfectades > 0){
                LOGGER.info("S'ha insertat " + filesAfectades + " alumnes");
                return CORRECTE;
            }else{
                LOGGER.warning("Error al insertar l'alumne");
                return ERROR;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AlumneDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ERROR;
    }
 
    
    /**Mètode per obtindre un objecte alumne a partir del ResultSet d'una consulta
     * a la base de dades
     * @param dades de la consulta SQL
     * @return objecte Alumne
     */
    public Alumne obtindreAlumne(ResultSet dades){
        //Instanciem la classe Alumne
        Alumne alumne = new Alumne();
        try {            
            alumne.setIdPersona(dades.getInt("p.id"));
            alumne.setNom(dades.getString("nom"));
            alumne.setCognom1(dades.getString("cognom1"));
            alumne.setCognom2(dades.getString("cognom2"));
            alumne.setData_naixement(dades.getString("data_naixement"));
            alumne.setDni(dades.getString("dni"));
            alumne.setTelefon(dades.getString("telefon"));
            alumne.setMail(dades.getString("mail"));
            alumne.setIdAlumne(dades.getInt("a.id"));
            alumne.setActiu(dades.getBoolean("actiu"));
            alumne.setMenjador(dades.getBoolean("menjador"));
            alumne.setAcollida(dades.getBoolean("acollida"));
            LOGGER.info("Obtingut alumne amb id: " + alumne.getIdAlumne());
            
            return alumne;
 
        } catch (SQLException ex) {
            Logger.getLogger(AlumneDAO.class.getName()).log(Level.SEVERE,
                    "ERROR al obtindre les dades d'alumnes del ResultSet", ex);
        }
        return alumne;
    }
    
    
}
