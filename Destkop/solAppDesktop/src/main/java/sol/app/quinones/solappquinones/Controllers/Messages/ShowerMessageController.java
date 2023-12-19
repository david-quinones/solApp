package sol.app.quinones.solappquinones.Controllers.Messages;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.json.JSONException;
import org.json.JSONObject;
import sol.app.quinones.solappquinones.Models.Message;
import sol.app.quinones.solappquinones.Models.Persona;
import sol.app.quinones.solappquinones.Models.Peticio;
import sol.app.quinones.solappquinones.Models.Professor;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Classe per controlar la finestra flotant d'enviament de missatge
 *
 * @author david
 */
public class ShowerMessageController implements Initializable {
    /**
     * Variables del fitxer FXML
     */
    @FXML
    private Text objDestinatari;
    @FXML
    private ComboBox listDestinetari;
    @FXML
    private TextArea messageId;
    @FXML
    private Button btnEnviar;

    /**
     * Variables de classe
     */
    private ServerComunication socket = new ServerComunication();
    private Peticio peticio = new Peticio();
    private Persona personaEnviaMiss;
    private ArrayList<Persona> persones = new ArrayList<>();
    private Persona personaRemitent;

    /**
     * Inicialitzador de la classe
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //assignem l'acció del boto en ser clicat
        btnEnviar.setOnMouseClicked(e -> {
            sendMessage();
        });
    }

    /**
     * Metode per assignar les dades a la finestra en ser oberta
     *
     * @param totalPeople llistat de persones destinetaries
     */
    public void assignarDadesFinestra(ArrayList<Persona> totalPeople) {
        objDestinatari.setText(personaEnviaMiss.getNom());
        this.persones = totalPeople;
        loadComboBox();
    }

    /**
     * configurar el combobox de persones
     */
    private void loadComboBox() {
        listDestinetari.setCellFactory(cf -> new ListCell<Persona>(){
            @Override
            protected void updateItem(Persona item, boolean empty){
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNom());
            }
        });

        listDestinetari.setConverter(new StringConverter<Persona>() {
            @Override
            public String toString(Persona persona) {
                return persona != null ? persona.getNom() + " " + persona.getCognom1() : "";
            }
            @Override
            public Persona fromString(String s) {
                return null;
            }
        });

        //add to comboBox
        listDestinetari.getItems().addAll(persones);

        //Listener Canvis Persona
        listDestinetari.valueProperty().addListener((obs, oldValue, newValue) -> {
            personaRemitent = (Persona) newValue;
        });

        //listener deselccionar en cliar
        listDestinetari.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            Persona seleccionat = (Persona) listDestinetari.getSelectionModel().getSelectedItem();

            if(seleccionat != null) {
                listDestinetari.getSelectionModel().clearSelection();
                e.consume();
            }
        });
    }


    /**
     * Metode d'enviament del missatge
     */
    //enviar missatge
    private void sendMessage() {

        //recuperar missatge de la vista
        ArrayList<Persona> p = new ArrayList<>();
        p.add(personaRemitent);

        Message message = new Message();
        message.setRemitentPersona(personaEnviaMiss);
        message.setDestinataris(p);
        message.setContingut(messageId.getText());
        message.setDataEnviament(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        //crear peticio i enviar missatge
        peticio.dropDades();
        try {
            socket.connect();
            peticio.setPeticio("ENVIAR_MISSATGE");
            peticio.addDades(SingletonConnection.getInstance().getKey());
            peticio.addDades(JsonUtil.toJson(message));

            JSONObject jsonObject = new JSONObject(socket.sendMessage(JsonUtil.toJson(peticio)));
            if(jsonObject.getInt("codiResultat") == 1) {
                Stage stage = (Stage) messageId.getScene().getWindow();
                stage.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Gets obj destinatari.
     *
     * @return the obj destinatari
     */
    public Text getObjDestinatari() {
        return objDestinatari;
    }

    /**
     * Sets obj destinatari.
     *
     * @param objDestinatari the obj destinatari
     */
    public void setObjDestinatari(Text objDestinatari) {
        this.objDestinatari = objDestinatari;
    }

    /**
     * Gets list destinetari.
     *
     * @return the list destinetari
     */
    public ComboBox getListDestinetari() {
        return listDestinetari;
    }

    /**
     * Gets message id.
     *
     * @return the message id
     */
    public TextArea getMessageId() {
        return messageId;
    }

    /**
     * Sets message id.
     *
     * @param messageId the message id
     */
    public void setMessageId(TextArea messageId) {
        this.messageId = messageId;
    }

    /**
     * Gets btn enviar.
     *
     * @return the btn enviar
     */
    public Button getBtnEnviar() {
        return btnEnviar;
    }

    /**
     * Sets btn enviar.
     *
     * @param btnEnviar the btn enviar
     */
    public void setBtnEnviar(Button btnEnviar) {
        this.btnEnviar = btnEnviar;
    }

    /**
     * Sets persona envia miss.
     *
     * @param personaEnviaMiss the persona envia miss
     */
    public void setPersonaEnviaMiss(Persona personaEnviaMiss) {
        this.personaEnviaMiss = personaEnviaMiss;
    }
}
