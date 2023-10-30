
import entitats.Empleat;
import entitats.Persona;
import estructurapr.PeticioClient;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pau Castell Galtes
 */
public class ProbaInsertarPersona {

    public static void main(String[] args) {
        
        try {
            Socket socket = new Socket("localhost", 9999);
            
            Empleat persona = new Empleat("Pau", "Castell", "Galtes", "1983-08-07",
                    "46797529D", "123456789", "pau@gmail.com", true, "2022-03-05", "2100-01-01");
            
            PeticioClient peticio = new PeticioClient("ALTA_EMPLEAT");
            peticio.afegirDades("proba");
            peticio.afegirDades(persona);
            
            
        } catch (IOException ex) {
            Logger.getLogger(ProbaInsertarPersona.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
