package sol.app.quinones.solappquinones.Controllers.Messages;

import javafx.fxml.Initializable;
import sol.app.quinones.solappquinones.Service.ConsultesSocket;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author david
 */

public class MessageController implements Initializable {

    //variables per els arrays
    //Agrupats per usuari?
    //fer consulta i llavors ordenar per cada?
    /*
    arrancada, com estableixo?

    poner en la lista
    recorrer todos los mensajes haciendo un hasmap id la persona remitente o envio dependiendo
    poner los mensajes al clicar, buscar todos los menjsaes ordenar y pintar dependiendo de si es enviado o recibido
     */


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recivedMessages();
    }

    /**
     * Call recived messages
     */
    public void recivedMessages(){
        //MISSATGES_REBUTS
        String response = ConsultesSocket.serverPeticioConsulta("MISSATGES_REBUTS");
        System.out.println(response);
    }

    /**
     * Call sending messages
     */
    public void sendedMessages(){
        //MISSATGES_REBUTS
        String response = ConsultesSocket.serverPeticioConsulta("MISSATGES_ENVIATS");
        System.out.println(response);
        //deserialitzar aquests misatges
    }

}
