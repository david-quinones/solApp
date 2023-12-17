package sol.app.quinones.solappquinones.Controllers.Messages;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sol.app.quinones.solappquinones.Models.Message;

public class CardMessageController {

    @FXML
    private Label IdInicials, IdNomComplet, IdCosMiss;

    public void setMessage(Message message, Boolean isSended) {
        if(isSended == true) {
            if (message.getDestinataris().size() > 0) {
                IdInicials.setText("+1");
            }else{
                IdInicials.setText(Character.toString(message.getDestinataris().get(0).getNom().charAt(0)).toUpperCase() +
                        Character.toString(message.getDestinataris().get(0).getCognom1().charAt(0)).toUpperCase());
            }
        }else {
            IdInicials.setText(Character.toString(message.getRemitentPersona().getNom().charAt(0)).toUpperCase() +
                    Character.toString(message.getRemitentPersona().getCognom1().charAt(0)).toUpperCase());
        }
        IdNomComplet.setText(message.getRemitentPersona().getNom()+ " " + message.getRemitentPersona().getCognom1() + " " + message.getRemitentPersona().getCognom2());
        IdCosMiss.setText(message.getContingut());
    }

}
