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
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

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
    private Persona mPersona =  new Persona();

    private Peticio peticio = new Peticio();
    private ServerComunication socket = new ServerComunication();


    // Métodos para manejar acciones, como actualizar los datos
    @FXML
    private void actualitzarDades() {

        peticio.dropDades();

        //peticio a servidor
        try {
            socket.connect();
            peticio.setPeticio("MODIFICAR_PERFIL");
            peticio.addDades(SingletonConnection.getInstance().getKey());
            setNewPerson();
            peticio.addDades(JsonUtil.toJson(mPersona));

            System.out.println(JsonUtil.toJson(peticio));

            //respota
            String resposta = socket.sendMessage(JsonUtil.toJson(peticio));

            System.out.println(resposta);


            JSONObject jsonObject = new JSONObject(resposta);

            if(jsonObject.getInt("codiResultat") == 0){
                ErrorController.showErrorAlert("Error","Error al desar dades", "No s'ha pogut modificar les dades", Alert.AlertType.ERROR);
                initData();

            }else{
                //vcreem el nou objetcte persona
                persona = mPersona;
                mPersona = null;
                initData();







                //instanciar el controlador de la vista para passar los datos y recuperar-los
            }




        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void cancelarActualitzarDades() {
        // Lógica para manejar la cancelación
        initData();


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //mainPerfil.getChildren().add(0, Model.getInstance().getViewFactory().getMenuTopViewr(this));
        startDate();
    }

    private void startDate(){

        peticio.dropDades();

        //peticio a servidor
        try {
            socket.connect();
            peticio.setPeticio("CONSULTA_PERFIL");
            peticio.addDades(SingletonConnection.getInstance().getKey());

            //respota
            String resposta = socket.sendMessage(JsonUtil.toJson(peticio));

            JSONObject jsonObject = new JSONObject(resposta);

            if(jsonObject.getInt("codiResultat") == 0){
                return;
            }else{
                //vcreem el nou objetcte persona
                persona = Persona.fromJson(jsonObject.getJSONArray("dades").get(0).toString());

                initData();
                //mostrar a finestra les dades






                //instanciar el controlador de la vista para passar los datos y recuperar-los
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    // Método para inicializar los datos en los campos
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
