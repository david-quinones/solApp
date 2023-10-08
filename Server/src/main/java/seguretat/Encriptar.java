package seguretat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 *Classe per millorar la segureta encriptant les dades
 * @author pau
 */
public class Encriptar {
    
      /**Métode que calcula el hash SHA-256 d'un password i el retorna en format
     * Base64
     * @param password que s'ha de calcular el hash
     * @return del hash del password en format Base64
     * @throws NoSuchAlgorithmException si hi ha algun error en el procés
     */        
    public String hashPassword(String password) throws NoSuchAlgorithmException{
        
        //Obtenim una instancia de MessageDigest SHA-256
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        
        //Transformem el password a bytes i fem el hash
        byte[] hashBytes = md.digest(password.getBytes());
        
        //Codificar el hash en format Base64 per emmagatzemar-lo a la base de dades
        String hashBase64 = Base64.getEncoder().encodeToString(hashBytes);

        //Retorn del hash amb les dades en String
        return hashBase64;
        
    }
}
