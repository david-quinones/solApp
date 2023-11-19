package sol.app.quinones.solappquinones.Service;

/**
 * Classe utilitzada per validació de dades als camps d¡entrada
 *
 * Ofereix metodes estatics per les diferents validacions
 *
 * @author david
 */
public class ValidadorCamps {

    /**
     * Validar un DNI amb una expressió regular
     * Comprovem que si el DNI completix el pattro (8 Digits + lletra)
     * @param dni DNI a validar
     * @return true si es ok, false en cas contrari
     */
    public static boolean validarDNI(String dni){
        return dni.matches("[0-9]{8}[A-Za-z]"); //41548690
    }

    /**
     * Valida un numero de teledon amb una expressió regular
     * Comprovem que el telefon compleixi un patro de 9 digits
     * @param telf Telf a valida
     * @return true si es ok, false en cas contrari
     */
    public static boolean validarTelf(String telf){
        return telf.matches("[0-9]{9}");
    }

    /**
     * Valida una diresccio de correu electronica mb una expressió regular
     * Comprtova si el correu cumplex un patro standard de e-mail
     * @param mail e-mail a comprovar
     * @return true si es ok, false en cas contrari
     */
    //emil?
    public static boolean validarMail(String mail){
        return mail.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

}
