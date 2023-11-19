package sol.app.quinones.solappquinones.Controllers.Alumne;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import sol.app.quinones.solappquinones.Controllers.ErrorController;
import sol.app.quinones.solappquinones.Models.Alumne;
import sol.app.quinones.solappquinones.Models.Peticio;
import sol.app.quinones.solappquinones.Models.Usuari;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;
import sol.app.quinones.solappquinones.Service.ValidadorCamps;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class WindowsFormAlumneController implements Initializable {

    @FXML
    private Label idLbl11, idLbl12;
    @FXML
    private AnchorPane mainWindowForm;
    @FXML
    private TextField idTxtFld1, idTxtFld2, idTxtFld3, idTxtFld5, idTxtFld6, idTxtFld7, idTxtFld10, idTxtFld11;
    @FXML
    private PasswordField idTxtFld12;
    @FXML
    private DatePicker idTxtFld4;
    @FXML
    private Button idBtnAcceptar;

    private AlumneController alumneController;
    private Alumne a;
    private Usuari u;
    private Alumne alumneCarregat;
    private int idAlumne;
    private Peticio peticio = new Peticio();
    private ServerComunication socket = new ServerComunication();

    public void setUsuariController(AlumneController alumneController) {
        this.alumneController = alumneController;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idTxtFld10.setVisible(false);
        idTxtFld4.setEditable(false);

        //aplicar syle error DNI
        aplicarCleanStyle();

        idBtnAcceptar.setOnAction(e -> saveObject());
    }

    private void aplicarCleanStyle() {
        //dni
        idTxtFld5.setOnMouseClicked(event -> {
            idTxtFld5.setStyle("");
        });
        idTxtFld5.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal){idTxtFld5.setStyle("");}
        });
        //tlf
        idTxtFld6.setOnMouseClicked(event -> {
            idTxtFld6.setStyle("");
        });
        idTxtFld6.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal){idTxtFld6.setStyle("");}
        });
        //email
        idTxtFld7.setOnMouseClicked(event -> {
            idTxtFld7.setStyle("");
        });
        idTxtFld7.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal){idTxtFld7.setStyle("");}
        });

        //TODO
    }

    private void saveObject() {
        String tipusPeticio;

        //validar dades
        if(!ValidadorCamps.validarDNI(idTxtFld5.getText())){
            System.out.println("DNI INCORRECTE");
            idTxtFld5.setStyle("-fx-border-color: red;");
            return;
        }
        if(!isDatePickerValid()){
            System.out.println("fechas incorrecteas");
            return;
        }
        if(!ValidadorCamps.validarTelf(idTxtFld6.getText())){
            idTxtFld6.setStyle("-fx-border-color: red");
            return;
        }
        if(!ValidadorCamps.validarMail(idTxtFld7.getText())){
            idTxtFld7.setStyle("-fx-border-color: red");
            return;
        }

        //crear objecte
        this.a = new Alumne(

                idBtnAcceptar.getText().equalsIgnoreCase("Crear") ? 0 : alumneCarregat.getIdPersona(),
                idTxtFld1.getText(),
                idTxtFld2.getText(),
                idTxtFld3.getText(),
                idTxtFld4.getValue().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                idTxtFld5.getText(),
                idTxtFld6.getText(),
                idTxtFld7.getText(),
                idAlumne
                //idBtnAcceptar.getText().equalsIgnoreCase("Crear") ? 0 : alumneCarregat.getIdAlumne()
        );

        if (!idBtnAcceptar.getText().equals("Modificar")) {
            //controlar si estan vacios, no hacer nada?? mandar un objeto vacio? siempre obligatorio?
            this.u = new Usuari(
                    idTxtFld11.getText(),
                    idTxtFld12.getText()

            );
            //set usuari non rols
            this.u.setAdmin(false);
            this.u.setTeacher(false);

            tipusPeticio = "ALTA_ALUMNE";
        } else {
            tipusPeticio = "MODIFICAR_ALUMNE";
            a.setIdPersona(alumneCarregat.getIdPersona());
        }

        boolean guardat = saveObjectDb(tipusPeticio);

        if(guardat){
            tancarFinestra();
        }else{
            ErrorController.showErrorAlert(
                    "ERROR AL TRACTAR ALUMNE",
                    "",
                    tipusPeticio.equalsIgnoreCase("ALTA_ALUMNE")
                            ? " No s'ha pogut guardar l'alumne"
                            : " No s'ah pogut modificar l'alumne",
                    //"No s'ha pogut guardar / Modificar",
                    Alert.AlertType.INFORMATION
            );
            tancarFinestra();

        }
    }

    public void tancarFinestra(){
        Stage stage = (Stage) idTxtFld10.getScene().getWindow();
        stage.close();
        alumneController.loadAlumnes();
    }

    private boolean saveObjectDb(String tipusPeticio) {
        peticio.dropDades();

        try {
            socket.connect();
            peticio.setPeticio(tipusPeticio);
            peticio.addDades(SingletonConnection.getInstance().getKey());
            peticio.addDades(JsonUtil.toJson(a));

            if(tipusPeticio.equalsIgnoreCase("ALTA_ALUMNE")){
                peticio.addDades(JsonUtil.toJson(u));
            }

            String resposta = socket.sendMessage(JsonUtil.toJson(peticio));

            JSONObject jsOb = new JSONObject(resposta);

            if(jsOb.getInt("codiResultat") != 0){
                return true;
            }else{
                return false;
            }

        } catch (IOException e) {
            return false;
        } catch (JSONException e) {
            return false;
        }
    }

    //
    public void loadAlumne(Alumne alum) {
        idTxtFld1.setText(alum.getNom());
        idTxtFld2.setText(alum.getCognom1());
        idTxtFld3.setText(alum.getCognom2());
        idTxtFld4.setValue(LocalDate.parse(alum.getData_naixement()));
        idTxtFld5.setText(alum.getDni());
        idTxtFld6.setText(alum.getTelefon());
        idTxtFld7.setText(alum.getMail());

        //ocultar botons de usuari
        idTxtFld11.setManaged(false);
        idTxtFld12.setManaged(false);
        idLbl11.setManaged(false);
        idLbl12.setManaged(false);

        //change name button
        idBtnAcceptar.setText("Modificar");

        //assignar alumne a variable
        alumneCarregat = alum;
        idAlumne = alum.getIdAlumne();
    }

    private boolean isDatePickerValid(){
        if(idTxtFld4.getValue() == null) return false;
        return true;
    }
}
