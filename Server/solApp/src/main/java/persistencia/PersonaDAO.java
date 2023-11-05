package persistencia;

import entitats.Persona;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import seguretat.Encriptar;
import utilitats.Utils;

/**Classe que proporciona métodes per donar d'alta, modificar i eliminar dades
 * relacionades amb l'entitat Persona
 *
 * @author Pau Castell Galtes
 */
public class PersonaDAO {
    private Connection conexio;
    PreparedStatement ps;
    private static final Logger LOGGER = Logger.getLogger(PersonaDAO.class.getName());
    
    public PersonaDAO(Connection conexio){
        this.conexio = conexio;
    }
    
    /**Métode per insertar entitats de la classe Persona a la base de dades, es
     * retorna l'id generat per poder insertar altres entitats relacionades.
     * @param persona
     * @return id de la entitat insertada
     */
    public int altaPersona (Persona persona){
        int id = 0;
        try {
            //Odre sql per insertar la persona a la base de dades
            String insertarPersona = "INSERT INTO persona (id,nom,cognom1,cognom2,data_naixement,dni,telefon,mail)"
                    + " VALUES(NULL,?,?,?,?,?,?,?)";
            ps = conexio.prepareStatement(insertarPersona, PreparedStatement.RETURN_GENERATED_KEYS);
            
            //Establim les dades que cal insertar
            ps.setString(1, persona.getNom());
            ps.setString(2, persona.getCognom1());
            ps.setString(3, persona.getCognom2());
            ps.setString(4, persona.getData_naixement());
            ps.setString(5, persona.getDni());
            ps.setString(6, persona.getTelefon());
            ps.setString(7, persona.getMail());
            
            //Executem insert
            ps.executeUpdate();
            //Obtenim l'id de l'objecte insertat a la base de dades
            ResultSet idGenerat = ps.getGeneratedKeys();
            if(idGenerat.next()){
                id = idGenerat.getInt(1); 
                LOGGER.log(Level.INFO, "Persona insertada correctament amb id: {0}", id);
                return id;
            }else{
                LOGGER.info("Error al insertar l'objectePersona");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(PersonaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return id;
    }
    
    /**Métode que obté les dades de la persona a partir del id d'un usuari, nor-
     * malment per poder gestionar les dades del perfil.
     * @param idUsuari del qual es volen obtindre les dades.
     * @return 
     */
    public Persona consultaPersona(int idUsuari){
        Persona persona = null;
        try {
            LOGGER.info("Obtenim dades de la persona a partir de l'usuari amb idUsuari: " + idUsuari);
            
            //Consulta per obtindre les dades de la persona a partir de l'usuari
            String consulta = "SELECT persona.* FROM persona "
                    + "INNER JOIN usuari ON persona.id = usuari.persona_id "
                    + "WHERE usuari.id = ?;"; 
            ps = conexio.prepareStatement(consulta);
            
            //Establim les dades per a la consulta
            ps.setInt(1, idUsuari);
            
            //Executem la consulta
            ResultSet dadesObtingudes = ps.executeQuery();
            
            //Creem l'objecte persona
            persona = obtindrePersona(dadesObtingudes);

        } catch (SQLException ex) {
            Logger.getLogger(PersonaDAO.class.getName()).log(Level.SEVERE,
                    "Error al intentar fer la consulta amb la base de dades.", ex);
        }
        return persona;
    }
    
    
    /**Métode per construir l'objecte Persona a partir de les dades obtingudes a
     * la base de dades
     * @param dadesObingudes en la consulta a la base de dades
     * @return 
     */
    public Persona obtindrePersona(ResultSet dadesObingudes){
        Persona persona = null;
        try {
            //Comprobem si hi han dades al resultSet
            while(dadesObingudes.next()){               
                persona = new Persona();
                persona.setIdPersona(dadesObingudes.getInt("id"));
                persona.setNom(dadesObingudes.getString("nom"));
                persona.setCognom1(dadesObingudes.getString("cognom1"));
                persona.setCognom2(dadesObingudes.getString("cognom2"));
                persona.setData_naixement(Utils.formatData(dadesObingudes.getDate("data_naixement")));
                persona.setDni(dadesObingudes.getString("dni"));
                persona.setTelefon(dadesObingudes.getString("telefon"));
                persona.setMail(dadesObingudes.getString("mail"));
                LOGGER.info("Obtinguda persona amb idPersona: " + persona.getIdPersona());
            }
            
            return persona;
            
        } catch (SQLException ex) {
            Logger.getLogger(PersonaDAO.class.getName()).log(Level.SEVERE, 
                    "Error al obtindre dades del ResultSet", ex);
        }
        return persona;
    }
    
    public int modificarPerfil(Persona persona){
        
        try {
            LOGGER.info("Modifiquem les dades de la Perosna amb idPersona " + persona.getIdPersona());
            //Consulta per modificar le dades
            String modificarSQL = "UPDATE persona SET nom = ?, cognom1 = ?, cognom2 = ?, "
                    + "data_naixement = ?, dni = ?, telefon = ?, mail = ? WHERE id = ?;";
            ps = conexio.prepareStatement(modificarSQL);
            
            //Establim les dades per realitzar la modificació.
            ps.setString(1, persona.getNom());
            ps.setString(2, persona.getCognom1());
            ps.setString(3, persona.getCognom2());
            ps.setString(4, persona.getData_naixement());
            ps.setString(5, persona.getDni());
            ps.setString(6, persona.getTelefon());
            ps.setString(7, persona.getMail());
            ps.setInt(8, persona.getIdPersona());
            
            //Executem la modificació i obtenim el número de files afectades
            int filesAfectades = ps.executeUpdate();
            LOGGER.info("Número de files afectades modificació Perfil: " + filesAfectades);
            
            return filesAfectades;
            
            
        } catch (SQLException ex) {
            Logger.getLogger(PersonaDAO.class.getName()).log(Level.SEVERE,
                    "Error al intentar modificar el perfil de la Persona", ex);
        }
        
        //En cas d'error retornem 0 files afectades
        return 0;     
    }
    
}
