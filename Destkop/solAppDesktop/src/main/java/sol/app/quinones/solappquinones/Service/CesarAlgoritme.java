package sol.app.quinones.solappquinones.Service;

/**
 * Classe amb els metodes per encriptar i desencriptar la contrasenya
 *
 * @author David
 */

public class CesarAlgoritme {
    /**
     * Metode que encripta la contrasenya movent el caracter n posicions segons ASCII
     * @param password contrasenya sense encriptar
     * @return contrasenya encripada
     */
    public static String codificar(String password) {
        int desplacament = 4; //moviments que realitzarem al carcater dins de ASCII
        //construir la cadena codificada
        StringBuilder passwordCodificat = new StringBuilder();
        //recorrem cada caracter del password
        for (char c : password.toCharArray()) {
            //convertim el caracter actual a ASCII
            int caracterAscii = (int) c;
            //movem el caracter segons la variable "desplacament", asegurant que sigui un ASCII imprimible
            caracterAscii = (caracterAscii + desplacament) % 128;
            //agregem el caracter desplaçat a la cadna
            passwordCodificat.append((char) caracterAscii);
        }
        //retornem el StringBuild pero a String
        return passwordCodificat.toString();
    }

    /**
     * Metode que desencripta la contrasenya movent el caracter n posicions enrera segons ASCII
     * @param passwordCodificat contrasenya encriptada
     * @return contrasenya sense encriptar
     */
    public static String descodificar(String passwordCodificat) {
        int desplacament = 4; //definim el numero de caracters a mourens
        //variable per construir la contrasenya desencriptada
        StringBuilder originalPassword = new StringBuilder();
        //Recorrem cada caracter de la contrasenya
        for (char c : passwordCodificat.toCharArray()) {
            //pasem a ascii cada caracter
            int caracterAscii = (int) c;
            //movem elscaracters enrerra, les posicions per saber quin es el caracter original
            caracterAscii = (caracterAscii - desplacament + 128) % 128;//+128 per controlar els negatius
            //afegim el caracter a la cadena
            originalPassword.append((char) caracterAscii);
        }
        //retornem la cadena en String
        return originalPassword.toString();
    }
}
