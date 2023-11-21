package seguretat;

import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import java.io.*;
import java.security.*;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *Classe per millorar la seguretat encriptant les dades
 * @author pau
 */
public class Encriptar {
    private Cipher cipher;
    private static final Logger LOGGER = Logger.getLogger(Encriptar.class.getName());
    public static final String password = "Password"; 
    private static final byte[] IV_ESTATIC = {
            0x01, 0x02, 0x03, 0x04,
            0x05, 0x06, 0x07, 0x08,
            0x09, 0x0A, 0x0B, 0x0C,
            0x0D, 0x0E, 0x0F, 0x10
    };
    
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
    
    
    /**Mètode per crear una clau a partir d'una contrasenya
     * 
     * @param password contrasenya llegible
     * @return clau criptogràfica
     * @throws NoSuchAlgorithmException 
     */
    public SecretKeySpec clauSimetrica(String password) throws NoSuchAlgorithmException{
        LOGGER.info("Obtenint clau secreta.");
        SecretKeySpec clau = null;
        //Obtenim una instanica de MessageDigest amb l'algoritme SHA-256
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        //Pasem el password rebut a bytes
        byte[] passwordBytes = password.getBytes();
        //Apliquem l'algoritme de hash als bytes del password
        byte[] passwordHash = md.digest(passwordBytes);
        //Crera una SecretKeySpec a partir del hash per utilitzarla com a clau
        clau = new SecretKeySpec(passwordHash, "AES");
        
        return clau;
    }
    
    
    
    /**Mètode per encriptar la resposta que s'enviarà al client
     * 
     * @param resposta que s'ha d'encriptar
     * @return resposta encriptada
     */
    public byte[] encriptarResposta (RetornDades resposta){
        LOGGER.info("Encriptant");
        byte[] respostaEncriptada = null;
        try {           
            //Obtenim la clau per poder encriptar
            SecretKeySpec clauSecreta = clauSimetrica(password);
            if(clauSecreta != null){
                //Obtenim el vector d'inicialització IV
                IvParameterSpec iv = new IvParameterSpec(IV_ESTATIC);
                //Es crea una instància de Cipher
                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                //Inicialitzem cipher en mode encriptar amb la clau i el IV
                cipher.init(Cipher.ENCRYPT_MODE, clauSecreta, iv);
                //Transformem l'objecte RetornDades en un array de bytes
                byte[] respostaBytes = objecteBytesResposta(resposta);
                if(respostaBytes != null){
                    //Encriptem les dades
                    respostaEncriptada = cipher.doFinal(respostaBytes);
                }else{
                    LOGGER.warning("Error, l'array de bytes de l'objecte es null");
                }
            }else{
                LOGGER.warning("Error, la clauSimetrica no es null");
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(Encriptar.class.getName()).log(Level.SEVERE,
                    "ERROR al intentar encriptar la resposta", ex);
        }
        return respostaEncriptada;
    }
    
    
    /**Mètode per desencriptar una petició rebuda del client
     * 
     * @param peticioBytes que s'han de desencriptar
     * @return objecte PeticioClient
     */
    public PeticioClient desencriptarPetició(byte[] peticioBytes){
        PeticioClient peticio = null;
        byte[] peticioDesencriptada = null;
        LOGGER.info("Desencriptant");
        try {            
            //Obtenim la clau per poder desencriptar
            SecretKeySpec clauSecreta = clauSimetrica(password);
            if(clauSecreta != null){
                //Obtenim el vector d'inicialització IV
                IvParameterSpec iv = new IvParameterSpec(IV_ESTATIC);
                //Es crea una instància de Cipher
                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                //Inicialitzem cipher en mode desencriptar amb la clau i el IV
                cipher.init(Cipher.DECRYPT_MODE, clauSecreta, iv);
                //Desencripyem els bytes rebuts
                peticioDesencriptada = cipher.doFinal(peticioBytes);
                //Transformem els byte al objecte PeticioClient
                peticio = BytesAObjectePeticio(peticioDesencriptada);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(Encriptar.class.getName()).log(Level.SEVERE, "ERROR al intentar desencriptar la petició", ex);
        }
        
        return peticio;
    }
    
    
    /**Mètode per transformar un objecte RetornDades en un array de bytes
     * 
     * @param resposta que s'ha de transformar
     * @return array de bytes
     */
    public byte[] objecteBytesResposta(RetornDades resposta){
        LOGGER.info("Transforman un objecte a bytes.");
        //try on inicialitzem el bos i el oos
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            
            //Escribim l'objecte en el ObjectOutputStream
            oos.writeObject(resposta);
            
            return bos.toByteArray();
            
        } catch (IOException ex) {
            Logger.getLogger(Encriptar.class.getName()).log(Level.SEVERE,
                    "ERROR al transformar l'objecte en un array de bytes", ex);
        }
        
        return null;
    }
    
    
    
    /**Mètode que transformar un array de byte en un objcte PeticioClient
     * 
     * @param bytesDesencriptats bytes que cal transformar
     * @return objecte PeticioClient
     */
    public PeticioClient BytesAObjectePeticio (byte[] bytesDesencriptats){
        LOGGER.info("Transforman array de bytes a un objecte");
        PeticioClient peticio = null;
        try(ByteArrayInputStream bais = new ByteArrayInputStream(bytesDesencriptats);
                ObjectInputStream ois = new ObjectInputStream(bais)) {
            
            //Transformem el bytes rebuts en un objecte PeticioClient
            peticio = (PeticioClient) ois.readObject();
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Encriptar.class.getName()).log(Level.SEVERE,
                    "ERROR al intentar obtindre un objecte a partir del array de bytes", ex);
        }
        return peticio;
    }
    
    
    
    
    
    
    //****************************************************************************************************
    
     /**Mètode per encriptar una petició que s'enviarà al servidor
     * 
     * @param peticio que s'ha d'encriptar
     * @return resposta encriptada
     */
    public byte[] encriptarPeticio(PeticioClient peticio){
        LOGGER.info("Encriptant peticio");
        byte[] peticioEncriptada = null;
        try {           
            //Obtenim la clau per poder encriptar
            SecretKeySpec clauSecreta = clauSimetrica(password);
            if(clauSecreta != null){
                //Obtenim el vector d'inicialització IV
                IvParameterSpec iv = new IvParameterSpec(IV_ESTATIC);
                //Es crea una instància de Cipher
                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                //Inicialitzem cipher en mode encriptar amb la clau i el IV
                cipher.init(Cipher.ENCRYPT_MODE, clauSecreta, iv);
                //Transformem l'objecte RetornDades en un array de bytes
                byte[] respostaBytes = objecteBytesPeticio(peticio);
                if(respostaBytes != null){
                    //Encriptem les dades
                    peticioEncriptada = cipher.doFinal(respostaBytes);
                }else{
                    LOGGER.warning("Error, l'array de bytes de l'objecte es null");
                }
            }else{
                LOGGER.warning("Error, la clauSimetrica no es null");
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(Encriptar.class.getName()).log(Level.SEVERE,
                    "ERROR al intentar encriptar la resposta", ex);
        }
        return peticioEncriptada;
    }
    
    
    /**Mètode per transformar un objecte PeticioClient en un array de bytes
     * 
     * @param resposta que s'ha de transformar
     * @return array de bytes
     */
    public byte[] objecteBytesPeticio(PeticioClient peticio){
        LOGGER.info("Transforman un objecte a bytes.");
        //try on inicialitzem el bos i el oos
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            
            //Escribim l'objecte en el ObjectOutputStream
            oos.writeObject(peticio);
            
            return bos.toByteArray();
            
        } catch (IOException ex) {
            Logger.getLogger(Encriptar.class.getName()).log(Level.SEVERE,
                    "ERROR al transformar l'objecte en un array de bytes", ex);
        }
        
        return null;
    }
    
    
    
    /**Mètode per desencriptar una resposta rebuda del servidor
     * 
     * @param peticioBytes que s'han de desencriptar
     * @return objecte PeticioClient
     */
    public RetornDades desencriptarResposta(byte[] respostaBytes){
        RetornDades resposta = null;
        byte[] respostaDesencriptada = null;
        LOGGER.info("Desencriptant");
        try {            
            //Obtenim la clau per poder desencriptar
            SecretKeySpec clauSecreta = clauSimetrica(password);
            if(clauSecreta != null){
                //Obtenim el vector d'inicialització IV
                IvParameterSpec iv = new IvParameterSpec(IV_ESTATIC);
                //Es crea una instància de Cipher
                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                //Inicialitzem cipher en mode desencriptar amb la clau i el IV
                cipher.init(Cipher.DECRYPT_MODE, clauSecreta, iv);
                //Desencripyem els bytes rebuts
                respostaDesencriptada = cipher.doFinal(respostaBytes);
                //Transformem els byte al objecte PeticioClient
                resposta = BytesAObjecteResposta(respostaDesencriptada);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(Encriptar.class.getName()).log(Level.SEVERE, "ERROR al intentar desencriptar la petició", ex);
        }
        
        return resposta;
    }
    
    
    
    /**Mètode que transformar un array de byte en un objcte RetornDades
     * 
     * @param bytesDesencriptats bytes que cal transformar
     * @return objecte PeticioClient
     */
    public RetornDades BytesAObjecteResposta (byte[] bytesDesencriptats){
        LOGGER.info("Transforman array de bytes a un objecte");
        RetornDades resposta = null;
        try(ByteArrayInputStream bais = new ByteArrayInputStream(bytesDesencriptats);
                ObjectInputStream ois = new ObjectInputStream(bais)) {
            
            //Transformem el bytes rebuts en un objecte PeticioClient
            resposta = (RetornDades) ois.readObject();
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Encriptar.class.getName()).log(Level.SEVERE,
                    "ERROR al intentar obtindre un objecte a partir del array de bytes", ex);
        }
        return resposta;
    }
}
