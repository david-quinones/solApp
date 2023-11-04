
import com.google.gson.Gson;
import entitats.Usuari;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author pau
 */
public class Probaclient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            
            //SOCKET DEL CLIENT
            Socket socket = new Socket("localhost",9999);
            
            Usuari usuari = new Usuari("PAU", "hola");
            
            System.out.println("1- Creo l'usuari següent:");
            System.out.println(usuari.getNomUsuari());
            System.out.println(usuari.getPassword());
            
            //OBJECTE AMB LA PETICIÓ

            PeticioClient peticio = new PeticioClient("LOGIN");
            peticio.afegirDades(usuari);
            
            //ENVIEM LA PETICIÓ AL SERVIDOR
            Gson gson = new Gson();
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            output.println(gson.toJson(peticio));
            System.out.println();
            System.out.println("Creo la petició amb el següent format:");
            System.out.println(gson.toJson(peticio));
 
            
            //REVEM LA RESPOSTA DEL SERVIDOR
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String llegir = input.readLine();
            RetornDades retorn = gson.fromJson(llegir, RetornDades.class);
            System.out.println();
            System.out.println("Revem la resposta del servidor:");
            System.out.println(llegir);
            
            if(retorn.getCodiResultat() > 0){
                usuari = (Usuari) retorn.getDades(0, Usuari.class);
               System.out.println("Codi resultat: " + retorn.getCodiResultat()); 
            System.out.println("Id usuari: " + usuari.getId());
            System.out.println("Nom usuari: " + usuari.getNomUsuari());
            System.out.println("Password usuari: " + usuari.getPassword());
            System.out.println("Admin: " + usuari.isIsAdmin());
            System.out.println("Teacher: " + usuari.isIsTeacher());
            System.out.println("Número de sessió: \n" + retorn.getDades(1, String.class));
            }else{
                System.out.println("Usuari no validat");
            }
            socket.close();
            
            
            
            
            Socket socket2 = new Socket("localhost",9999);
            
            peticio = new PeticioClient("LOGOUT");
           // peticio.affegirDades(retorn.getDades(1, String.class));
            peticio.afegirDades("sessioProves");
   
            PrintWriter output2 = new PrintWriter(new OutputStreamWriter(socket2.getOutputStream()),true);
            output2.println(gson.toJson(peticio));

            BufferedReader input2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
            llegir = input2.readLine();
    
            retorn = gson.fromJson(llegir, RetornDades.class);
            
            System.out.println(retorn.getCodiResultat());
            
            socket2.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Probaclient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
