package sol.app.quinones.solappquinones.Controllers.Messages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sol.app.quinones.solappquinones.Models.Message;
import sol.app.quinones.solappquinones.Models.Persona;
import sol.app.quinones.solappquinones.Models.Peticio;
import sol.app.quinones.solappquinones.Models.Professor;
import sol.app.quinones.solappquinones.Service.ConsultesSocket;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * @author david
 */

public class MessageController implements Initializable {


    /**
     * Missatge Sencer
     */
    @FXML
    private HBox btnBasura, btnrespon;
    @FXML
    private Label IdInicialsMiss, IdNomCorreuMiss, IdDestinetariMiss, idContingutMiss;
    @FXML
    private Circle idCircle;
    @FXML
    private VBox IdCosMissatge;

    /**
     *
      */
    @FXML
    private VBox VBoxMail;
    @FXML
    private HBox newConversation, btnRebuts, btnEnviats;
    @FXML
    private FontAwesomeIconView btnMenuLateral, btnRefresh;
    @FXML
    private VBox menuLateral;
    @FXML
    private Label emailLabel, idTipusMissatge;

    private HBox selectedHBox;
    private CardMessageController cardMessageControllerSelected;


    private ArrayList<Message> messageRecived = new ArrayList<>();
    private ArrayList<Message> messageSended = new ArrayList<>();
    private ArrayList<Persona> llistatPersones = new ArrayList<>();
    
    private Persona personaRemitent;

    ServerComunication socket = new ServerComunication();
    Peticio peticio = new Peticio();

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

        /**
         * Carregar persones que hi ha missatges
         */
        loadPersonsMessages(); //carregar els missatges i destinetaris
        callRemitentPerson(); //load person logging

        emailLabel.setText(personaRemitent.getMail());


        btnMenuLateral.setOnMouseClicked(e -> changeStateMenu());
        newConversation.setOnMouseClicked(e -> newMessageView());

        //load conversations (recived)
        carregarEmailView(messageRecived, false);
        //carregarEmailView(messageSended, true);

        btnRebuts.setOnMouseClicked(e -> carregarEmailView(messageRecived, false));
        btnEnviats.setOnMouseClicked(e -> carregarEmailView(messageSended, true));

        /**
         * En arrancar missatge ocult
         */
        IdCosMissatge.setVisible(false);
        IdCosMissatge.setManaged(false);

        /**
         * Eliminar
         */
        btnBasura.setOnMouseClicked(e -> deleteMail());

        /**
         * refresh
         */
        btnRefresh.setOnMouseClicked(e -> {
            loadPersonsMessages();
            carregarEmailView(messageRecived, false);
        });

        /**
         * New Message
         */
        newConversation.setOnMouseClicked(e -> {
            //obrir nova finestra missatge:

        });


    }

    private void deleteMail() {
        //llamada servidor
        try {
            peticio.dropDades();
            socket.connect();
            peticio.setPeticio("ELIMINAR_MISSATGE");
            peticio.addDades(SingletonConnection.getInstance().getKey());
            peticio.addDades(JsonUtil.toJson(cardMessageControllerSelected.getMissatgeCard()));

            JSONObject jsonObject = new JSONObject(socket.sendMessage(JsonUtil.toJson(peticio)));

            if(jsonObject.getInt("codiResultat") == 1){
               //actualitzo la vista de missastges i poso en blanc la vista dreta
                loadPersonsMessages();
                carregarEmailView(messageRecived, false);
                IdCosMissatge.setVisible(false);
                IdCosMissatge.setManaged(false);

            } else {
                //no faig res, perque no l'ha pogut eliminar
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //refrescar vista
    }

    /***
     *
     * @param messages
     * @param isSended
     */
    private void carregarEmailView(ArrayList<Message> messages, Boolean isSended) {
        //VBoxMail.getChildren().clear();
        VBoxMail.getChildren().removeIf(node -> VBoxMail.getChildren().indexOf(node) > 1);

        if(isSended == true) {
            idTipusMissatge.setText("Sortida");
        }else{
            idTipusMissatge.setText("Entrada");
        }

        try {


            for(Message message : messages ) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/Fxml/CardMessage.fxml"));
                HBox messageCard = loader.load();
                CardMessageController cardMessageController= loader.getController();
                cardMessageController.setMessage(message, isSended);

                //comportament al clicar una targeta
                //mirar si es ocult o no, per mostrar-ho
                messageCard.setOnMouseClicked(e -> {
                    if(selectedHBox != null) {
                        selectedHBox.getStyleClass().clear();
                    }
                    messageCard.getStyleClass().add("selected_vbox");
                    selectedHBox = messageCard;

                    //carregar info a la vista lateral dreta:
                    if (!IdCosMissatge.isVisible()) {
                        IdCosMissatge.setVisible(true);
                        IdCosMissatge.setManaged(true);
                    }
                    //Inicial remitent i color
                    IdInicialsMiss.setText(cardMessageController.getMissatgeCard().getRemitentPersona().getNom().toUpperCase().charAt(0) +
                            Character.toString(cardMessageController.getMissatgeCard().getRemitentPersona().getCognom1().toUpperCase().charAt(0)));
                    idCircle.setFill(cardMessageController.assignarColorSegonsNom(cardMessageController.getMissatgeCard().getRemitentPersona().getNom().charAt(0)));

                    //Nom  i correu
                    IdNomCorreuMiss.setText(cardMessageController.getMissatgeCard().getRemitentPersona().getNom() +
                            " " + cardMessageController.getMissatgeCard().getRemitentPersona().getCognom1() +
                            "<" + cardMessageController.getMissatgeCard().getRemitentPersona().getMail() + ">");
                    //Destinataris
                    IdDestinetariMiss.setText(cardMessageController.getMissatgeCard()
                                    .getDestinataris()
                                    .stream()
                                    .map(destinatari -> destinatari.getMail())
                                            .collect(Collectors.joining("; ")));
                    //Cos
                    idContingutMiss.setText(cardMessageController.getMissatgeCard().getContingut());

                    cardMessageControllerSelected = cardMessageController;

                });


                VBoxMail.getChildren().add(messageCard);


            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Metode per crear un nou missatge a la vista
     */
    private void newMessageView() {
    }

    /**
     * Metode per mostrar o ocultar la vista lateral
     */
    private void changeStateMenu() {
        if (menuLateral.isVisible()) {
            menuLateral.setVisible(false);
            menuLateral.setManaged(false);
        } else {
            menuLateral.setVisible(true);
            menuLateral.setManaged(true);
        }
    }

    private void loadPersonsMessages() {
        callAllPersons(); //add to array all persons on system
        callRecivedMessages(); //add to array messages to distenatary its user loggin
        callSendedMessages(); //add to array messages to remitent its user logging

    }


    //Afegir totes les persones a un array
    private void callAllPersons() {
        processarPersones("LLISTAR_EMPLEATS");
        processarPersones("LLISTAR_ALUMNES");
    }
    private void processarPersones(String tipusPeticio) {
        try {
            JSONObject jsonObject = new JSONObject(ConsultesSocket.serverPeticioConsulta(tipusPeticio));
            if(jsonObject.getInt("codiResultat") == 1) {
                //convertim en persones i add to ArrayList
                JSONArray arrayPersona = jsonObject.getJSONArray("dades");
                for(int i = 1; i < jsonObject.getJSONArray("dades").length(); i++){
                    llistatPersones.add(Persona.fromJson(arrayPersona.get(i).toString()));
                }
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }




    private void callRemitentPerson() {
        String resposta = ConsultesSocket.serverPeticioConsulta("CONSULTA_PERFIL");
        try {
            JSONObject jsonObject = new JSONObject(resposta);
            if(jsonObject.getInt("codiResultat") == 1) {
                personaRemitent = Persona.fromJson(jsonObject.getJSONArray("dades").get(0).toString());
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }



    /**
     * Call recived messages
     */
    public void callRecivedMessages(){
        //MISSATGES_REBUTS
        String response = ConsultesSocket.serverPeticioConsulta("MISSATGES_REBUTS");
        callMessageServer(messageRecived, response);


    }

    /**
     * Call sending messages
     */
    public void callSendedMessages(){
        //MISSATGES_REBUTS
        String response = ConsultesSocket.serverPeticioConsulta("MISSATGES_ENVIATS");
        //deserialitzar aquests misatges

        callMessageServer(messageSended, response);
    }


    private void callMessageServer(ArrayList<Message> typeMessage, String response){
        //test deserialitzar
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getInt("codiResultat") == 1){
                JSONArray jsonArray = jsonObject.getJSONArray("dades");

                //netejem la llista
                typeMessage.clear();

                for(int i = 1; i < jsonArray.length();i++){
                    typeMessage.add(Message.fromJson(jsonArray.get(i).toString()));
                }

            } else{
                //TODO --> controlar si rertorna un error, si es vuit?
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }



    //enviar missatge
    private void sendMessage() {
        //recuperar missatge
        ArrayList<Persona> p = new ArrayList<>();
        p.add(personaRemitent);

        Message message = new Message();
        message.setRemitentPersona(personaRemitent);
        message.setDestinataris(p);
        //message.setContingut(txtf_message.getText());
        message.setDataEnviament(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        //crear peticio i enviar missatge
        peticio.dropDades();
        try {
            socket.connect();
            peticio.setPeticio("ENVIAR_MISSATGE");
            peticio.addDades(SingletonConnection.getInstance().getKey());
            peticio.addDades(JsonUtil.toJson(message));

            String resposta = socket.sendMessage(JsonUtil.toJson(peticio));

            //controlar resposta per netejar i actualiar o no TODO
            System.out.println(resposta);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
