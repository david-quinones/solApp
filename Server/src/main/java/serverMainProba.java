
import servidor.ServidorSocketListener;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

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
