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

/**
 *
 * @author david
 */

public class MessageController implements Initializable {


    @FXML
    private VBox VBoxMail;
    @FXML
    private HBox newConversation;
    @FXML
    private FontAwesomeIconView btnMenuLateral;
    @FXML
    private VBox menuLateral;
    @FXML
    private Label emailLabel;
    /**
     * En carregar la finestra, busca els usuaris els converteix en persona a la dreta i els posa a la tula
     */

    private HBox selectedHBox;

    /*Organitzar menu lateral
    * Agrupar per missatge, idPersona
    * */
    private Map<Integer, ArrayList<Message>> messagePerson = new HashMap<>();


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



        /**
         * Posar a la vista esquerra les persones
         */




        //TODO temporal
        //btn_sendMessage.setOnAction(event -> sendMessage());

        //menu lateral esquerra, Persones
        //en clicar una persona --> mostrat missatges al mig
        // enviar missatge en conversa
        //boto per fer un missatge nou oi llavors actualitzar tot

        for(Map.Entry<Integer, ArrayList<Message>> message : messagePerson.entrySet()) {
            System.out.println("Id: " + message.getKey() + " , Message: " + message.getValue());
            for(Persona p : llistatPersones) {
                if(p.getIdPersona() == message.getKey()) {
                    System.out.println(p.getNom());
                }
            }
        }
    }

    /***
     *
     * @param messages
     * @param isSended
     */
    private void carregarEmailView(ArrayList<Message> messages, Boolean isSended) {
        //VBoxMail.getChildren().clear();
        VBoxMail.getChildren().removeIf(node -> VBoxMail.getChildren().indexOf(node) > 1);
        try {


            for(Message message : messages ) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/Fxml/CardMessage.fxml"));
                HBox messageCard = loader.load();
                CardMessageController cardMessageController = loader.getController();
                cardMessageController.setMessage(message, isSended);

                messageCard.setOnMouseClicked(e -> {
                    if(selectedHBox != null) {
                        selectedHBox.getStyleClass().clear();
                    }
                    messageCard.getStyleClass().add("selected_vbox");
                    selectedHBox = messageCard;

                    System.out.println("print");
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
        System.out.println(response);

        callMessageServer(messageRecived, response);


    }

    /**
     * Call sending messages
     */
    public void callSendedMessages(){
        //MISSATGES_REBUTS
        String response = ConsultesSocket.serverPeticioConsulta("MISSATGES_ENVIATS");
        System.out.println(response);
        //deserialitzar aquests misatges

        callMessageServer(messageSended, response);
    }


    private void callMessageServer(ArrayList<Message> typeMessage, String response){
        //test deserialitzar
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getInt("codiResultat") == 1){
                JSONArray jsonArray = jsonObject.getJSONArray("dades");

                //clean Map -->
                messagePerson.clear();

                for(int i = 1; i < jsonArray.length();i++){
                    Message message = Message.fromJson(jsonArray.get(i).toString());
                    typeMessage.add(message);

                    //add to Map
                    message.setRebut(false);
                    message.setEnviat(true);

                    //mirem si existeix la idPersona al Map, sino existeix l'Id executem la funcio per generar-la, si existeix, retorna la clau
                    //per acabar insereix el missatge a la llista existent (perque la clau ja hi es) o la nova creada
                    messagePerson.computeIfAbsent(message.getRemitentPersona().getIdPersona(), e -> new ArrayList<>()).add(message);


                    for(Persona persona : message.getDestinataris()) {
                        message.setRebut(true);
                        message.setEnviat(false);
                        messagePerson.computeIfAbsent(persona.getIdPersona(), e -> new ArrayList<>()).add(message);
                    }



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
