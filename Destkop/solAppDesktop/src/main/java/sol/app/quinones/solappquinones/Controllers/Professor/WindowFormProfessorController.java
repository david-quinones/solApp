package sol.app.quinones.solappquinones.Controllers.Professor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONObject;
import sol.app.quinones.solappquinones.Models.*;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;
import sol.app.quinones.solappquinones.Service.ValidadorCamps;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class WindowFormProfessorController implements Initializable {
    public AnchorPane mainWindowForm;
    public TextField idTextMostra;
    @FXML
    private Label idLbl1,idLbl2,idLbl3,idLbl4,idLbl5,idLbl6,idLbl7,idLbl8,idLbl9,idLbl10,idLbl11,idLbl12;
    @FXML
    private TextField idTxtFld1,idTxtFld2,idTxtFld3,idTxtFld5,idTxtFld6,idTxtFld7,idTxtFld10,idTxtFld11,idTxtFld12;
    @FXML
    private Button idBtnAcceptar;

    @FXML
    private DatePicker idTxtFld4,idTxtFld8,idTxtFld9;

    private int idEmpleat;

    private Professor p;
    private Usuari u;
    private Peticio peticio = new Peticio();
    private ServerComunication socket = new ServerComunication();

    private ProfessorController professorController;


    //si es ok respuesta -> close + update grid (done)

    //contolar tot els camps
    //bloquear ventana de atras mientras esta està abierta (done)

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idTxtFld10.setVisible(false);

        idTxtFld4.setEditable(false);
        idTxtFld8.setEditable(false);
        idTxtFld9.setEditable(false);

        aplicarNetejaStylError();

        idBtnAcceptar.setOnAction(event -> saveObject());
    }

    private void aplicarNetejaStylError() {
        //styl with error
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

        //sett promp text all camps TODO
        //idTxtFld5.setPromptText("41548690H");



    }

    //validar que todos los campos son correctos 1
    //guardar objeto
    private void saveObject() {

        //TODO validar dades
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

        String peticioType = "";

        //controlar idEmpleat --> si existeix assigno sino existeix 0
        int idPersona = idTxtFld10.getText() != null && !idTxtFld10.getText().trim().isEmpty() ? Integer.parseInt(idTxtFld10.getText().trim()) : 0;

        this.p = new Professor(
                //obligatoris: Nom, Cognom, isActive, dataNeixament (9999/09/09) --> controlador
                idPersona,
                idTxtFld1.getText(),
                idTxtFld2.getText(),
                idTxtFld3.getText(),
                idTxtFld4.getValue().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                idTxtFld5.getText(),
                idTxtFld6.getText(),
                idTxtFld7.getText(),
                idEmpleat,
                idTxtFld8.getValue().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                idTxtFld9.getValue().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                true
        );
        if(!idBtnAcceptar.getText().equals("Modificar")){
            //controlar si estan vacios, no hacer nada?? mandar un objeto vacio? siempre obligatorio?
            this.u = new Usuari(
                    idTxtFld11.getText(),
                    idTxtFld12.getText()

            );
            //set usuari non rols
            this.u.setAdmin(false);
            this.u.setTeacher(true);

            System.out.println(
                    this.u.getNomUsuari()
                            + " " + this.u.getPassword()
                            + " " + this.p.getNom()
            );

            peticioType = "ALTA_EMPLEAT";

        }else{
            peticioType = "MODIFICAR_EMPLEAT";
        }

        //peticio servidor per guardar objecte
        boolean isOk = saveDataBd(peticioType);

        if(isOk){
            Stage actual = (Stage)idTxtFld12.getScene().getWindow();
            actual.close();
            professorController.carregarProfessors();
        }


    }

    public WindowFormProfessorController() {
    }

    public boolean saveDataBd(String peticioType){

        peticio.dropDades();

        try{
            socket.connect();
            peticio.setPeticio(peticioType);
            peticio.addDades(SingletonConnection.getInstance().getKey());
            peticio.addDades(JsonUtil.toJson(p));

            if(peticioType.equalsIgnoreCase("ALTA_EMPLEAT")){
                peticio.addDades(JsonUtil.toJson(u));
            }

            System.out.println(JsonUtil.toJson(peticio));
            String resposta = socket.sendMessage(JsonUtil.toJson(peticio));

            //TODO
            //control resposta si es inserir o modificar --> resposta hauria de retron un objecte modificat
            JSONObject jsonObject = new JSONObject(JsonUtil.toJson(peticio));
            Persona persona = Persona.fromJson(jsonObject.getJSONArray("dades").get(1).toString());
            System.out.println(persona.getDni());



            System.out.println(resposta);

        }catch(Exception e){
            e.printStackTrace();
        }


        //retorn estat del guardat
        return true;
    }

    public void loadObject(Professor obj){
                idTxtFld1.setText(obj.getNom());
                idTxtFld2.setText(obj.getCognom1());
                idTxtFld3.setText(obj.getCognom2());
                idTxtFld4.setValue(LocalDate.parse(obj.getData_naixement()));
                idTxtFld5.setText(obj.getDni());
                idTxtFld6.setText(obj.getTelefon());
                idTxtFld7.setText(obj.getMail());
                idTxtFld8.setValue((obj.getIniciContracte() != null && !obj.getIniciContracte().isEmpty()) ? LocalDate.parse(obj.getIniciContracte()) : LocalDate.now());
                idTxtFld9.setValue((obj.getFinalContracte() != null && !obj.getFinalContracte().isEmpty()) ? LocalDate.parse(obj.getFinalContracte()) : LocalDate.now());
                idTxtFld10.setText(String.valueOf(obj.getIdPersona()));
                idEmpleat = obj.getIdEmpleat();

                //idTxtFld8.setValue(LocalDate.parse(obj.getIniciContracte()));
                //idTxtFld9.setValue(LocalDate.parse(obj.getFinalContracte()));

                //ocultar i guardar change
                idTxtFld11.setManaged(false);
                idTxtFld12.setManaged(false);
                idLbl11.setManaged(false);
                idLbl12.setManaged(false);

                idBtnAcceptar.setText("Modificar");
    }

    public void setProfessorController(ProfessorController professorController){
        this.professorController = professorController;
    }


    //validadors:
    private boolean isFormatValid(){
        if(
                idTxtFld1.getText().trim().isEmpty()
                || idTxtFld2.getText().trim().isEmpty()
                || idTxtFld3.getText().trim().isEmpty()
                || idTxtFld5.getText().trim().isEmpty()
                || idTxtFld6.getText().trim().isEmpty()
                || idTxtFld7.getText().trim().isEmpty()

        ) return false;

        return true;
    }

    private boolean isDatePickerValid(){

        if(idTxtFld4.getValue() == null) return false;
        if(idTxtFld8.getValue() == null) return false;
        if(idTxtFld9.getValue() == null) return false;

        return true;
    }

}
