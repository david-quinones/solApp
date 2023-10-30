package persistencia;

import entitats.Persona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import seguretat.Encriptar;

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

    public Connection getConexio() {
        return conexio;
    }
    
}
