
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author pau
 */
public class HashProbes {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //Obtenim una instancia de MessageDigest SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            String password = "password";
            
            //Transformem el password a bytes i fem el hash
            byte[] hashBytes = md.digest(password.getBytes());
            
            //Codificar el hash en format Base64 per emmagatzemar-lo a la base de dades
            String hashBase64 = Base64.getEncoder().encodeToString(hashBytes);
            
            System.out.println(hashBase64);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(HashProbes.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    
}
