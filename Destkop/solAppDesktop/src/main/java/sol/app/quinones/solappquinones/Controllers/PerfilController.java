package sol.app.quinones.solappquinones.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.JSONException;
import org.json.JSONObject;
import sol.app.quinones.solappquinones.Models.Model;
import sol.app.quinones.solappquinones.Models.Persona;
import sol.app.quinones.solappquinones.Models.Peticio;
import sol.app.quinones.solappquinones.Service.ConsultesSocket;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;
import sol.app.quinones.solappquinones.Service.ValidadorCamps;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controla la gestió del perfil de l'usuari que esta loginat
 *
 * Poder visualitzar i ediar la informaicó
 *
 * @author david
 */
public class PerfilController implements Initializable {

    @FXML
    public VBox mainPerfil;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtCognom1;

    @FXML
    private TextField txtCognom2;

    @FXML
    private DatePicker dpDataNaixement;

    @FXML
    private TextField txtDni;

    @FXML
    private TextField txtTelefon;

    @FXML
    private TextField txtMail;

    private int idPersona;

    private Persona persona;
    private Persona mPersona = new Persona();

    private Peticio peticio = new Peticio();
    private ServerComunication socket = new ServerComunication();

    /**
     * Inicializa el controlador, configura i carrega a UI les dades del perfil
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dpDataNaixement.setEditable(false);
        startDate();
    }

    /**
     * Acció Boto d'actualitzar les dades del perfil --> info acció informada al FXML
     * Fa petició api per actualitzar les dades que hi ha actualment als camps
     */
    @FXML
    private void actualitzarDades() {

        //comprovar dades:
        if(!ValidadorCamps.validarDNI(txtDni.getText())){
            System.out.println("DNI INCORRECTE");
            txtDni.setStyle("-fx-border-color: red;");
            return;
        }
        if(dpDataNaixement.getValue() == null){
            dpDataNaixement.setValue(LocalDate.parse(LocalDate.now().toString()));
            return;
        }
        if(!ValidadorCamps.validarTelf(txtTelefon.getText())){
            txtTelefon.setStyle("-fx-border-color: red");
            return;
        }
        if(!ValidadorCamps.validarMail(txtMail.getText())){
            txtMail.setStyle("-fx-border-color: red");
            return;
        }

        peticio.dropDades();

        //peticio a servidor
        try {
            socket.connect();
            peticio.setPeticio("MODIFICAR_PERFIL");
            peticio.addDades(SingletonConnection.getInstance().getKey());
            setNewPerson();
            peticio.addDades(JsonUtil.toJson(mPersona));

            //respota
            String resposta = socket.sendMessage(JsonUtil.toJson(peticio));
            
            JSONObject jsonObject = new JSONObject(resposta);

            if(jsonObject.getInt("codiResultat") == 0){
                ErrorController.showErrorAlert("Error","Error al desar dades", "No s'ha pogut modificar les dades", Alert.AlertType.ERROR);
                initData();

            }else{
                //vcreem el nou objetcte persona
                persona = mPersona;
                //mPersona = null;
                initData();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cancela l'edició dels camps --> info acció informada al FXML
     * Retorna l'objecte a l'estat inicial
     */
    @FXML
    private void cancelarActualitzarDades() {
        // Lógica para manejar la cancelación
        initData();
    }

    /**
     * Carrega les dades inicials al perfil fent contula API
     */
    private void startDate(){

        try {

            String resposta = ConsultesSocket.serverPeticioConsulta("CONSULTA_PERFIL");

            JSONObject jsonObject = new JSONObject(resposta);

            if(jsonObject.getInt("codiResultat") == 0){
                return;
            }else{
                //vcreem el nou objetcte persona
                persona = Persona.fromJson(jsonObject.getJSONArray("dades").get(0).toString());

                initData();

            }


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Retorna persona sssociada al perfil
     * @return Persona perfil
     */
    public Persona getPersona() {
        return persona;
    }

    /**
     * Estableix persona associada perfil
     * @param persona PErsona perfil
     */
    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    /**
     * Inicializa les dades de la persona a la finestra (load)
     */
    public void initData() {
        this.idPersona = persona.getIdPersona();
        txtNom.setText(persona.getNom());
        txtCognom1.setText(persona.getCognom1());
        txtCognom2.setText(persona.getCognom2());
        dpDataNaixement.setValue(LocalDate.parse(persona.getData_naixement()));
        txtDni.setText(persona.getDni());
        txtTelefon.setText(persona.getTelefon());
        txtMail.setText(persona.getMail());
    }

    /**
     * Obte els camps de la finestra i estableix la nova persona
     * Prepara l'objecvte persona amb le smodificaions
     */
    public void setNewPerson() {
        mPersona.setIdPersona(this.idPersona);
        mPersona.setNom(txtNom.getText());
        mPersona.setCognom1(txtCognom1.getText());
        mPersona.setCognom2(txtCognom2.getText());
        mPersona.setData_naixement(dpDataNaixement.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        mPersona.setDni(txtDni.getText());
        mPersona.setTelefon(txtTelefon.getText());
        mPersona.setMail(txtMail.getText());
    }

}
