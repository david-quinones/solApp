
import servidor.ServidorSocketListener;


/**
 *
 * @author pau
 */
public class serverMainProba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        ServidorSocketListener servidor = new ServidorSocketListener(9999);
        servidor.escoltarClients();
    }
    
}
