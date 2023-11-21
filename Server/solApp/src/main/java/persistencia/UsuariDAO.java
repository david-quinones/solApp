package persistencia;

import entitats.Usuari;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import seguretat.Encriptar;

/**
 *Classe que proporciona métodes per accedir i manipular dades relacionades amb
 * l'entitat usuari de la base de dades.
 * @author Pau Castell Galtes
 */
public class UsuariDAO {
    private ConexioBBDD base_dades;
    private Connection conexio;
    private Encriptar seguretat;
    private PreparedStatement ps;
    private static final Logger LOGGER = Logger.getLogger(UsuariDAO.class.getName());
    private static final int CORRECTE = 1;
    private static final int ERROR = -1;
    
    /**Constructor de la classe UsuariDAO
     * 
     */
    public UsuariDAO(Connection conexio) {
        this.conexio = conexio;
        seguretat = new Encriptar();

    }
    
    /**Métode per validar el nom d'usuari i el password rebut amb les dades 
     * emmagatzemades.
     * @param usuari dades de l'usuari a validar
     * @return Usuari amb les totes les dades del usuari validat 
     */
    public Usuari validarUsuari(Usuari usuari){

        try {
            //Consulta a la base de dades
            String consulta = "SELECT * FROM usuari WHERE nom_usuari = ? AND actiu = TRUE";
            ps = conexio.prepareStatement(consulta);
            
            //Establim el nom d'usuari rebut a la consulta
            ps.setString(1, usuari.getNomUsuari());
            
            //Executem la consulta
            ResultSet rs = ps.executeQuery();
            
            //Si rebem dades de la consulta comprobem el password
            if(rs.next()){
                //Recuperació del password de la consulta
                String passwordBBDD = rs.getString("password");
                
                //Obtenim el hash del password rebut de la petició
                String hashPasswordRebut = seguretat.hashPassword(usuari.getPassword());
                
                //Si el password es correcte generem l'usuari amb totes les dades
                if(MessageDigest.isEqual(hashPasswordRebut.getBytes(),passwordBBDD.getBytes())){
                    usuari.setId(rs.getInt("id"));
                    usuari.setNomUsuari(rs.getString("nom_usuari"));
                    usuari.setPassword(rs.getString("password"));
                    usuari.setIsAdmin(rs.getBoolean("is_admin"));
                    usuari.setIsTeacher(rs.getBoolean("is_teacher"));
                    //TODO falta afegir la relació amb la persona
                    return usuari;
                }
            }
            
            //Si no hi ha dades o el password es incorrecte retornem null
            return null;
            
        } catch (SQLException ex) {
            Logger.getLogger(UsuariDAO.class.getName()).log(Level.SEVERE, 
                    "Error al conectar amb la base de dades", ex);
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UsuariDAO.class.getName()).log(Level.SEVERE, null, ex);
            
        }            
        return null;
    }
    
    public int altaUsuari(Usuari usuari, int personaId){
        try {
            //Instrucció sql per afegir un nou usuari
            String insertUsuari = "INSERT INTO usuari (nom_usuari, password, is_admin,"
                    + " is_teacher, persona_id, actiu) VALUES (?,?,?,?,?,?) ";
            ps = conexio.prepareStatement(insertUsuari);
            
            //Establim dades per a la instrucció
            ps.setString(1, usuari.getNomUsuari());
            //Abans d'insertar el password l'encriptem
            Encriptar encriptar= new Encriptar();
            String password = encriptar.hashPassword(usuari.getPassword());
            ps.setString(2, password);
            ps.setBoolean(3, usuari.isIsAdmin());
            ps.setBoolean(4, usuari.isIsTeacher());
            ps.setInt(5, personaId);
            ps.setBoolean(6, usuari.isIsActive());
            
            //Comprovem si s'ha insertat correctament
            int fileAfectades = ps.executeUpdate();
            if(fileAfectades > 0){
                LOGGER.info("L'usuari " + usuari.getNomUsuari() + " s'ha insertat correctament.");
                return CORRECTE;
            }else{
                LOGGER.warning("ERROR al insertar l'usuari amb nom: " + usuari.getNomUsuari());
                return ERROR;
            }
            
        } catch (SQLException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UsuariDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
             return -1; 
    }
    

    /**Mètode per desactivar usuaris, no podrán accedir a l'aplicació
     * 
     * @param idPersona vinculada al usuari que es vol desactivar
     * @return codi amb el resultat
     */
    public int eliminarUsuari(int idPersona){
        try {
            //Sentència per actualizar les dades de l'usuari
            String desactivarEmpleat = "UPDATE usuari SET actiu = false "
                    + "WHERE persona_id = ?;";
            ps = conexio.prepareStatement(desactivarEmpleat);
            
            //Establim les dades per realitzar l'actualització
            ps.setInt(1, idPersona);
            
            //Comprovem si l'execució es correcte
            int filesAfectades = ps.executeUpdate();
            if(filesAfectades > 0){
                LOGGER.info("L'usuari ha sigut desactivat, idPersona: " + idPersona);
                return CORRECTE;
            }else{
                LOGGER.warning("ERROR al intentar desactivar l'usuari, amb id persona: " +
                        idPersona);
                return ERROR;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(EmpleatDAO.class.getName()).log(Level.SEVERE,
                    "ERROR al intntar actulitzar l'usuari associat al empleat", ex);
        }
        return ERROR;
    }
    
    
    /**Mètode que retorna una llista d'usuaris de la base de dades
     * 
     * @return llista d'usuaris.
     */
    public ArrayList llistarUsuaris(){
        //Array que contindrà la llista d'usuaris
        ArrayList<Usuari> llistaUsuaris = new ArrayList();
        try {
            //Consulta a la base de dades
            String consultaSQL = "SELECT * FROM usuari;";
            //Executem la consulta
            ps = conexio.prepareStatement(consultaSQL);
            ResultSet rs = ps.executeQuery();
            //Omplim l'array amb les dades rebudes
            while(rs.next()){
                llistaUsuaris.add(obtindreUsuari(rs));
            }
            
            return llistaUsuaris;
            
        } catch (SQLException ex) {
            Logger.getLogger(UsuariDAO.class.getName()).log(Level.SEVERE, 
                    "ERROR al intentar obtindre una llista d'usuaris", ex);
        }
        
        return llistarUsuaris();
    }
    
    
    
    /**Mètode per modificar les dades d'un usuari existent a la base de dades.
     * 
     * @param usuari amb les dades noves
     * @return codi amb el resultat
     */
    public int modificarUsuari(Usuari usuari){
        try {
            //Ordre SQL per modificar un usuari de la bbdd
            String updateUsuari = "UPDATE usuari SET nom_usuari = ?, password = ?, "
                    + "is_admin = ?, is_teacher = ?, actiu = ? WHERE id = ?;";
            ps = conexio.prepareStatement(updateUsuari);
            
            //Si la contraseña es la mateixa no fem un hash
                String password = "";
                Usuari usuariOriginal = consultaUsuari(usuari.getId());
                if(!usuariOriginal.getPassword().equals(usuari.getPassword())){
                    //Obtenim el hash del password rebut
                    Encriptar encriptar= new Encriptar();
                    password = encriptar.hashPassword(usuari.getPassword());
                } else{
                    password = usuariOriginal.getPassword();
                }
            
            if(usuari != null){
                //Establim les dades per a la modificació
                ps.setString(1, usuari.getNomUsuari());               
                ps.setString(2, password);
                ps.setBoolean(3, usuari.isIsAdmin());
                ps.setBoolean(4, usuari.isIsTeacher());
                ps.setBoolean(5, usuari.isIsActive());
                ps.setInt(6, usuari.getId());
                
            }else{
                LOGGER.warning("L'objecte usuari es null");
                return ERROR;
            }
            
            //Comprovem les files afectades
            int filesAfectades = ps.executeUpdate();
            if(filesAfectades > 0){
                LOGGER.info("L'usuari " + usuari.getId() + " s'ha modificat correctament.");
                return CORRECTE;
            }else{
                LOGGER.warning("L'usuari no s'ha modificat, el id no existeix o es null");
                return ERROR;
            }

        } catch (SQLException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UsuariDAO.class.getName()).log(Level.SEVERE,
                    "Error al modificar l'usuari: " + usuari.getId(), ex);
        }
        
        return ERROR;
    }
    
    
    
    /**Mètode per obtenir objecte Usuari a partir d'un ResultSet de la base de dades
     * 
     * @param dades obtingudes de la bbdd
     * @return objecte Usuari
     */
    public Usuari obtindreUsuari(ResultSet dades){
        //Instanciem un Usuari sense paràmetres
        Usuari usuari = new Usuari();
        try {            
            usuari.setId(dades.getInt("id"));
            usuari.setNomUsuari(dades.getString("nom_usuari"));
            usuari.setPassword(dades.getString("password"));
            usuari.setIsAdmin(dades.getBoolean("is_admin"));
            usuari.setIsTeacher(dades.getBoolean("is_teacher"));
            usuari.setIdPersona(dades.getInt("persona_id"));
            usuari.setIsActive(dades.getBoolean("actiu"));
            LOGGER.info("Obtingut usuari amb idUsuari: " + usuari.getId());
            
            return usuari;
            
        } catch (SQLException ex) {
            Logger.getLogger(UsuariDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuari;
    }
    
    
    
    /**Mètode per obtindre un usuari de la base de dades a partir del seu id
     * 
     * @param idUsuari
     * @return 
     */
    public Usuari consultaUsuari(int idUsuari){
        Usuari usuari = null;
        try {
            //Consulta per obtenir un usuari
            String consultaUsuari = "SELECT * FROM usuari WHERE id = ?;";
            PreparedStatement psConsulta = conexio.prepareStatement(consultaUsuari);
            
            //Establim dades per a la consulta
            psConsulta.setInt(1, idUsuari);
            //Obtenim el resultat
            ResultSet rs = psConsulta.executeQuery();
            //Comprovem dades
            if(rs.next()){
                LOGGER.info("L'usuari s'ha obtingut correctament");
                usuari = obtindreUsuari(rs);
            }else{
                LOGGER.warning("ERROR no s'ha trobat cap usuari amb aquest id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuariDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return usuari;
    }
     
}
