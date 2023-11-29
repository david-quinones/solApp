package persistencia;

import entitats.Alumne;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
    private static final Logger LOGGER = Logger.getLogger(AlumneDAO.class.getName());
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
            String insertAlumne = "INSERT INTO alumne (actiu, menjador, acollida, persona_id, aula_id)"
                    + " VALUES (?,?,?,?,?);";
            psAlumne = conexio.prepareStatement(insertAlumne);
            
            //Establim les dades que cal insertar
            psAlumne.setBoolean(1, alumne.isActiu());
            psAlumne.setBoolean(2,alumne.isMenjador());
            psAlumne.setBoolean(3, alumne.isAcollida());
            Integer idAula = alumne.getIdAula();
            psAlumne.setInt(4, idPersona);
            if(idAula > 0){
                psAlumne.setInt(5, alumne.getIdAula());
            }else{
                psAlumne.setNull(5, Types.INTEGER);
            }
            
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
    
    
    /**Mètode per modificar un alumne existent a la base de dades
     * 
     * @param alumne que s'ha de modificar
     * @return codi amb el resultat
     */
    public int modificarAlumne(Alumne alumne){
        try {
            //Ordre sql per modificar les dades del alumne
            String updateAlumne = "UPDATE alumne SET menjador = ?,"
                    + " acollida = ? , aula_id = ? WHERE id = ?;";
            psAlumne = conexio.prepareStatement(updateAlumne);
            
            //Establim les dades per a la ordre sql
            if(alumne != null){
                psAlumne.setBoolean(1, alumne.isMenjador());
                psAlumne.setBoolean(2, alumne.isAcollida());
                //Comprovem si l'id de l'aula es null
                Integer aulaId = alumne.getIdAula();
                System.out.println(aulaId + "  " + alumne.getIdAula());
                if(aulaId > 0){
                    psAlumne.setInt(3, alumne.getIdAula());
                }else{
                    psAlumne.setNull(3, Types.INTEGER);
                }               
                psAlumne.setInt(4, alumne.getIdAlumne());
            }else{
                LOGGER.warning("L'objecte alumne es null");
                return ERROR;
            }

            //Comprovem que s'ha modificat alguna fila
            int filesAfectades = psAlumne.executeUpdate();
            int filesAfectadeUsuariAssociat = modificarUsuariAsociat(alumne.getIdPersona(),alumne.isActiu());
            if(filesAfectades > 0 && filesAfectadeUsuariAssociat > 0){
                LOGGER.info("L'alumne amb id " + alumne.getIdAlumne()+
                        " s'ha modificat correctament.");
                return CORRECTE;
            }else{
                LOGGER.warning("L'id no existeix en la base de dades o es null");
                return ERROR;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(EmpleatDAO.class.getName()).log(Level.SEVERE,
                    "ERROR al intentar modificar l'alumne", ex);
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
            if(dades.getObject("aula_id") != null){
                alumne.setIdAula(dades.getInt("aula_id"));
            }else{
                alumne.setIdAula(0);
            }
            LOGGER.info("Obtingut alumne amb id: " + alumne.getIdAlumne());
            
            return alumne;
 
        } catch (SQLException ex) {
            Logger.getLogger(AlumneDAO.class.getName()).log(Level.SEVERE,
                    "ERROR al obtindre les dades d'alumnes del ResultSet", ex);
        }
        return alumne;
    }
    
    /**Mètode per modificar l'estat actiu de l'usuari associat a l'alumne
     * 
     * @param personaId
     * @param actiu
     * @return 
     */
    public int modificarUsuariAsociat (int personaId, boolean actiu){
        try {
            String modificarUsuari = "UPDATE usuari SET actiu = ?"
                    + " WHERE persona_id = ?;";
            psAlumne = conexio.prepareStatement(modificarUsuari);
            //Establim les dades per a la consulta
            psAlumne.setBoolean(1, actiu);
            psAlumne.setInt(2, personaId);
            
            int filesAfectades = psAlumne.executeUpdate();
            //Comprovem les files afectades
            if(filesAfectades > 0){
                LOGGER.info("S'ha modificat l'usuari associat a l'alumne");
                return CORRECTE;
            }else{
                LOGGER.warning("ERROR al intentar modificar l'usuari associat a l'alumne");
                return ERROR;
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmpleatDAO.class.getName()).log(Level.SEVERE, null, ex);
        }     
        return ERROR;
    }
    
    
    
    /**Mètode per afegir alumnes a una aula
     * 
     * @param alumne
     * @param idAula
     * @return 
     */
    public int afegirAlumneAula (Alumne alumne, int idAula){
        try {
            //Instrucció per modificar la base de dades
            String afegirAlumne = "UPDATE alumne SET aula_id = ? WHERE id = ?;";
            psAlumne = conexio.prepareStatement(afegirAlumne);
            
            //Establim les dades per fer la modificació
            psAlumne.setInt(1, idAula);
            if(alumne.getIdAlumne() > 0){
                psAlumne.setInt(2, alumne.getIdAlumne());
            }else{
                LOGGER.warning("El camp idAlumne està buit.");
                return ERROR;
            }

            //Comprovem que la execució es correcte
            int filesAfectades = psAlumne.executeUpdate();
            if(filesAfectades > 0){
                LOGGER.info("Alumne amb id " + alumne.getIdAlumne() + " afegit a aula amb id: " + idAula);
                return CORRECTE;
            }else{
                LOGGER.warning("ERROR al itentar afegir alumne a l'aula");
                return ERROR;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AlumneDAO.class.getName()).log(Level.SEVERE,
                    "ERROR al intentar afegir un alumne a una aula", ex);
        }
        return ERROR;
    }
    
    
    
    /**Mètode per llistar el alumnes que hi ha en una aula en concret
     * 
     * @param idAula dels alumnes que s'han de llistar
     * @return ArrayLista amb la llista d'alumnes
     */
    public ArrayList llistaAlumnesAula(int idAula){
        ArrayList<Alumne> llistaAlumnes = new ArrayList<>();
        try {           
            String alumnesAula = "SELECT * FROM alumne a INNER JOIN persona p"
                    + " ON a.persona_id  = p.id WHERE aula_id = ?;";
            psAlumne = conexio.prepareStatement(alumnesAula);
            
            //Establim dades per a la consulta
            psAlumne.setInt(1, idAula);
            
            //Obtenim ResultSet amb el resultat de la consulta
            ResultSet rs = psAlumne.executeQuery();
            //Afegim les dades obtingudes a l'array
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    llistaAlumnes.add(obtindreAlumne(rs));
                }
            }else{
                LOGGER.info("No hi ha alumnes associats a l'aula amb id: " + idAula);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AlumneDAO.class.getName()).log(Level.SEVERE, 
                    "ERROR al intentar llistar els alumnes per aula", ex);
        }
        return llistaAlumnes;
    }
    
    
    /**Mètode per eliminar les aules associades a un alumne
     * 
     * @param idAula que cal elimnar
     * @return codi resultat
     */
    public int eliminarAlumneAula(int idAula){
        try {
            String eliminarAlumneAula = "UPDATE alumne SET aula_id = NULL "
                    + "WHERE aula_id = ?;";
            psAlumne = conexio.prepareStatement(eliminarAlumneAula);
            
            //Establim dades per a la consulta
            psAlumne.setInt(1, idAula);
            
            int filesAfectades = psAlumne.executeUpdate();
            //Comprovem el resultat
            if(filesAfectades > 0){
                LOGGER.info("Aula eliminada.");
                return CORRECTE;
            }else{
                LOGGER.warning("No s'ha pogut eliminar l'aula: " + idAula + " dels alumne");
                return ERROR;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AlumneDAO.class.getName()).log(Level.SEVERE, 
                    "ERROR al intentar eliminar la aula del alumne", ex);
        }
        
        return ERROR;
    }
}
