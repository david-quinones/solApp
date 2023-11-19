package sol.app.quinones.solappquinones.Service;

public class ValidadorCamps {


    public static boolean validarDNI(String dni){
        return dni.matches("[0-9]{8}[A-Za-z]"); //41548690
    }

    public static boolean validarTelf(String telf){
        return telf.matches("[0-9]{9}");
    }

    //emil?
    public static boolean validarMail(String mail){
        return mail.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

}
