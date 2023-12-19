package sol.app.quinones.solappquinones.Controllers.Messages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sol.app.quinones.solappquinones.Models.Message;
import sol.app.quinones.solappquinones.Models.Persona;
import sol.app.quinones.solappquinones.Models.Peticio;
import sol.app.quinones.solappquinones.Service.ConsultesSocket;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *Controlador de la vista de missatge
 *
 * S'encarrega de gestionar les interaccions de l'usuari amb la vista
 * Gestiona les comunicacions
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
    private Label IdInicialsMiss, IdNomCorreuMiss, IdDestinetariMiss, idContingutMiss, idDateEnviat;
    @FXML
    private Circle idCircle;
    @FXML
    private VBox IdCosMissatge;

    /**
     * Menu esquerra i finestra centreal
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


    /**
     * Variables per els missatges (tots) i les persones (totes)
     */
    private ArrayList<Message> messageRecived = new ArrayList<>();
    private ArrayList<Message> messageSended = new ArrayList<>();
    private ArrayList<Persona> llistatPersones = new ArrayList<>();
    
    private Persona personaRemitent;

    /**
     * Comunicació
     */
    ServerComunication socket = new ServerComunication();
    Peticio peticio = new Peticio();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**
         * Carregar persones que hi ha missatges
         */
        loadPersonsMessages(); //carregar els missatges i destinetaris
        callRemitentPerson(); //load person logging

        //assignar el correu de l'usuari al menu esquerra
        emailLabel.setText(personaRemitent.getMail());


        //Carregar els missatges rebuts en l'entrada de l'apliació, passant la llista de missatges i el tipus
        carregarEmailView(messageRecived, false);


        /**
         * Acció de ocultar el menu lateral
         */
        btnMenuLateral.setOnMouseClicked(e -> changeStateMenu());

        /**
         * Accio dels submenus de rebuts i enviats
         */
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
            newMessageView();
        });


    }

    /**
     * Metode per eliminar un missatge
     * En eliminar, recarrega la vista retornan a esafata d'entrada en ambdos casos
     */
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

    }


    /***
     * Metode per carregar les targetees de missatges a la vista principal
     * Recorre cada missatge, crea una targeta amb la info, i linserta
     *
     * @param messages Llitsa de missatges
     * @param isSended Identificados si es enviat o rebut
     */
    private void carregarEmailView(ArrayList<Message> messages, Boolean isSended) {

        // eliminem els VBox apartir del 3 (deixant la part de la capçalera)
        VBoxMail.getChildren().removeIf(node -> VBoxMail.getChildren().indexOf(node) > 1);
        //assignem nom a la capçalera depenen de quin tipus es
        if(isSended == true) {
            idTipusMissatge.setText("Sortida");
        }else{
            idTipusMissatge.setText("Entrada");
        }

        try {

            //recorrem tots els missatges i anem creant la vista de les targetes
            for(Message message : messages ) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/Fxml/CardMessage.fxml"));
                HBox messageCard = loader.load();
                CardMessageController cardMessageController= loader.getController();
                cardMessageController.setMessage(message, isSended);

                //comportament al clicar una targeta (part dreta)
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

                    //data
                    idDateEnviat.setText(cardMessageController.getMissatgeCard().getDataEnviament());

                    cardMessageControllerSelected = cardMessageController;

                });
                //afegim missatge
                VBoxMail.getChildren().add(messageCard);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Metode per crear un nou missatge a la vista
     * Obre la finestra del missatge i precarrega informació
     */
    private void newMessageView() {
        //load FXML (no utilitzo la fabrica perque no m'interesa)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ReadNewMessage.fxml"));
        try {
            Parent parent = loader.load();
            //instanciem i obtenim el controlador de la finestra nova
            ShowerMessageController showerMessageController = loader.getController();
            //passema a la finestra, persona qui envia (sempre sera connectat) i llistat de persones
            showerMessageController.setPersonaEnviaMiss(personaRemitent);
            showerMessageController.assignarDadesFinestra(llistatPersones);

            //Mostrar finestra
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("Enviar Missatge");
            stage.initModality(Modality.APPLICATION_MODAL); //bloquejm pantalla prinicpal
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    /**
     * Metode inicial que crida els submetodes per emplenar les llistes de:
     * * Persones
     * * Enviats
     * * Rebuts
     */
    private void loadPersonsMessages() {
        callAllPersons(); //add to array all persons on system
        callRecivedMessages(); //add to array messages to distenatary its user loggin
        callSendedMessages(); //add to array messages to remitent its user logging
    }


    /**
     * Metode per afegir totes les persones a un array - NO TENIM CRIDA, ENS ADAPTEM
     */
    private void callAllPersons() {
        processarPersones("LLISTAR_EMPLEATS");
        processarPersones("LLISTAR_ALUMNES");
    }

    /**
     * Metode per fer les crides de persones al servidor (reutilització de codi)
     * @param tipusPeticio Tipus de petició (empleat o aluimne)
     */
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


    /**
     * Metode per extreure la informació de l'usuari connectat, el Singleton, te l'usuari pero no la persona.
     */
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
        //pasem resposta al metode que ho tractarà
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


    /**
     *  Metode que deserialitza els missatges del servidor, utilitzat per les crides de missatges (reutilització de codi=)
     * @param typeMessage
     * @param response
     */
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
                // no hi ha else, sino retorna res, ens quedem tan panxos
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
