package sol.app.quinones.solappquinones.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import sol.app.quinones.solappquinones.Models.*;
import sol.app.quinones.solappquinones.Service.JSON.JsonUtil;
import sol.app.quinones.solappquinones.Service.ServerComunication;
import sol.app.quinones.solappquinones.Service.SingletonConnection;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WindowFormController implements Initializable {
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

    private Professor p;
    private Usuari u;
    private Peticio peticio = new Peticio();
    private ServerComunication socket = new ServerComunication();


    //si es ok respuesta -> close + update grid (done)

    //contolar tot els camps
    //bloquear ventana de atras mientras esta està abierta (done)

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idTxtFld10.setVisible(false);
        idBtnAcceptar.setOnAction(event -> saveObject());
    }

    //validar que todos los campos son correctos 1
    //guardar objeto
    private void saveObject() {

        String peticioType = "";

        this.p = new Professor(
                //obligatoris: Nom, Cognom, isActive, dataNeixament (9999/09/09) --> controlador
                idTxtFld1.getText(),
                idTxtFld2.getText(),
                idTxtFld3.getText(),
                idTxtFld4.getValue().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                idTxtFld5.getText(),
                idTxtFld6.getText(),
                idTxtFld7.getText(),
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

        //TODO
        ProfessorController pc = new ProfessorController();
        pc.crearProfessor(p);

        if(isOk){
            Stage actual = (Stage)idTxtFld12.getScene().getWindow();
            actual.close();
            pc.carregarProfessors();
        }


    }

    public WindowFormController() {
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

                //idTxtFld8.setValue(LocalDate.parse(obj.getIniciContracte()));
                //idTxtFld9.setValue(LocalDate.parse(obj.getFinalContracte()));

                //ocultar i guardar change
                idTxtFld11.setManaged(false);
                idTxtFld12.setManaged(false);
                idLbl11.setManaged(false);
                idLbl12.setManaged(false);

                idBtnAcceptar.setText("Modificar");
    }
}
