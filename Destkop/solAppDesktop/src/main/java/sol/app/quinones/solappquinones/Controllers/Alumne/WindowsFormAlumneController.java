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

/**
 * Controlador del formulari d'alumne
 * Gestió de la logica de la UI a la creació i edició d'un alumne
 *
 * @author david
 */
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

    /**
     * Estableix el controladr d'alumnes
     * @param alumneController controlador d'alumne a assignar
     */
    public void setUsuariController(AlumneController alumneController) {
        this.alumneController = alumneController;
    }


    /**
     * Inicizlita el controlador. Configura camps
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //amagar txtFld
        idTxtFld10.setVisible(false);
        //Posar datePicker no editable (aixi no es pot escriure i no hi ha errors, (menys controls))
        idTxtFld4.setEditable(false);

        //aplicar syle clen error
        aplicarCleanStyle();

        //assigna accio de guardar al boto d'acceptar
        idBtnAcceptar.setOnAction(e -> saveObject());
    }

    /**
     * Aplica estil al camps per resejetr errors
     */
    private void aplicarCleanStyle() {
        //Configura events per netejar l'esticl
        //dni
        idTxtFld5.setOnMouseClicked(event -> { //event cliar
            idTxtFld5.setStyle("");
        });
        idTxtFld5.focusedProperty().addListener((obs, oldVal, newVal) -> { //event rebre focus
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

    /**
     * Guarda l'objecte alumne creat/modificat
     * Realitza una previa validació de dades i gestiona la crida al servidor
     */
    private void saveObject() {
        String tipusPeticio;

        //validar dades i establir estil d'error
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

        //crear objecte amb els camps del formulari
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

        //controlar si es crear o editar per afegir usuari
        if (!idBtnAcceptar.getText().equals("Modificar")) {
            //controlar si estan vacios, no hacer nada?? mandar un objeto vacio? siempre obligatorio?
            this.u = new Usuari(
                    idTxtFld11.getText(),
                    idTxtFld12.getText()

            );
            //set usuari non rols
            this.u.setAdmin(false);
            this.u.setTeacher(false);

            //establim tipus petició si hem entrat es crear (perque hi ha usuari) sino, modificar
            tipusPeticio = "ALTA_ALUMNE";
        } else {
            tipusPeticio = "MODIFICAR_ALUMNE";
            a.setIdPersona(alumneCarregat.getIdPersona()); //assignem el idPersona, de quan hem carregat l'alumne
        }

        //fem la crida al servidor per guardar
        boolean guardat = saveObjectDb(tipusPeticio);

        //control i tractat de la respota de intentar guardar al servidor
        if(guardat){
            tancarFinestra();
        }else{
            ErrorController.showErrorAlert(
                    "ERROR AL TRACTAR ALUMNE",
                    tipusPeticio.equalsIgnoreCase("ALTA_ALUMNE")
                            ? " No s'ha pogut guardar l'alumne"
                            : " No s'ah pogut modificar l'alumne",
                    "El DNI " + idTxtFld5.getText() + " ja existeix al sistema",
                    Alert.AlertType.INFORMATION
            );

        }
    }

    /**
     * Tanca finestra actual i recarrega la llista d'alumnes del controlador que li hem passat
     */
    public void tancarFinestra(){
        Stage stage = (Stage) idTxtFld10.getScene().getWindow();
        stage.close();
        alumneController.loadAlumnes();
    }

    /**
     *Fa la crida per guardar l'objcet
     * @param tipusPeticio tipus petició (alta o modificació)
     * @return true si es exitos
     */
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

    /**
     * Carregem l'alumne a la finestra quan s'ha de modificar
     * @param alum alumne a carregar
     */
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

    /**
     * metode per controlar si el camp esta ple (date)
     * @return true si es valid
     */
    private boolean isDatePickerValid(){
        if(idTxtFld4.getValue() == null) return false;
        return true;
    }
}
