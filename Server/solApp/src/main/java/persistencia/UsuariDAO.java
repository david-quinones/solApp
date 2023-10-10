package persistencia;

import entitats.Usuari;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import seguretat.Encriptar;

/**
 *Classe que proporciona métodes per accedir i manipular dades relacionades amb
 * l'entitat usuari de la base de dades.
 * @author pau
 */
public class UsuariDAO {
    private ConexioBBDD base_dades;
    private Connection conexio;
    private Encriptar seguretat;

    /**Constructor de la classe UsuariDAO
     * 
     * @param conexio a la base de dades per realitzar les operacions.
     */
    public UsuariDAO() {
        try {
            
            base_dades = new ConexioBBDD();
            
            conexio = base_dades.conectar();
            
            seguretat = new Encriptar();
            
        } catch (SQLException ex) {
            Logger.getLogger(UsuariDAO.class.getName()).log(Level.SEVERE,
                    "Error al conectar amb la base de dades", ex);
        }
    }
    
    
    
    /**Métode per validar el nom d'usuari i el password rebut amb les dades 
     * emmagatzemades.
     * @param usuari dades de l'usuari a validar
     * @return Usuari amb les totes les dades del usuari validat
     * @throws SQLException
     * @throws NoSuchAlgorithmException 
     */
    public Usuari validarUsuari(Usuari usuari){

        try {
            //Consulta a la base de dades
            String consulta = "SELECT * FROM usuari WHERE nom_usuari = ?";
            PreparedStatement ps = conexio.prepareStatement(consulta);
            
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
            
        }finally{          
            base_dades.tancarConexio();
        }
        
        return null;
    }
     
}