package sol.app.quinones.solappquinones.Controllers.Messages;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sol.app.quinones.solappquinones.Models.Message;

/***
 * @author david
 */
public class CardMessageController {

    @FXML
    private Circle idCircle;
    @FXML
    private Label IdInicials, IdNomComplet, IdCosMiss;

    private Message missatgeCard;

    /**
     * Metode per omplir la targeta del missatge
     * @param message missatge amb les dades
     * @param isSended identificador de si es rebut o enviat
     */
    public void setMessage(Message message, Boolean isSended) {

        this.missatgeCard = message;

        if(isSended == true) {
            if (message.getDestinataris().size() > 1) {
                IdInicials.setText("+1");
            }else{
                IdInicials.setText(Character.toString(message.getRemitentPersona().getNom().charAt(0)).toUpperCase() +
                        Character.toString(message.getRemitentPersona().getCognom1().charAt(0)).toUpperCase());
                idCircle.setFill(assignarColorSegonsNom(message.getRemitentPersona().getNom().charAt(0)));
            }
        }else {
            IdInicials.setText(Character.toString(message.getRemitentPersona().getNom().charAt(0)).toUpperCase() +
                    Character.toString(message.getRemitentPersona().getCognom1().charAt(0)).toUpperCase());
            idCircle.setFill(assignarColorSegonsNom(message.getRemitentPersona().getNom().charAt(0)));
        }
        IdNomComplet.setText(message.getRemitentPersona().getNom()+ " " + message.getRemitentPersona().getCognom1() + " " + message.getRemitentPersona().getCognom2());
        IdCosMiss.setText(message.getContingut());
    }

    /**
     * Algoritme: Suport de Internet per el control dels colors i fer-los unics
     *
     * Metode per assignar un color al cercle basat amb la inicial del nom
     * Genera el color de manera predeterminada amb un resultat unic per cada lletra
     * @param inicialNom inicial del nom
     * @return color RGB
     */
    public Color assignarColorSegonsNom(char inicialNom) {

        //convertim la inicial a majuscula i calculem valor unic
        int hash = (Character.toUpperCase(inicialNom) - 'A') * 10;
        // calcula el color vermell, sumant 55 assegurem que no sigui molt fosc, el modul 256 ens assegura que sigui un color RGB
        int vermell = (hash + 55) % 256;
        // calcula el color verd, Multipliat 3 i syumat 25 assegurem que no esta a vermell
        int verd = (hash * 3 + 25) % 256;
        // calculem el blau, multiplicat per 7 i sumat 5, asegura que el color sigui unic
        int blau = (hash * 7 + 5) % 256;

        // retorna el color rgb extret amb el calcul
        return Color.rgb(vermell, verd, blau);
    }

    public Message getMissatgeCard() {
        return missatgeCard;
    }
}
