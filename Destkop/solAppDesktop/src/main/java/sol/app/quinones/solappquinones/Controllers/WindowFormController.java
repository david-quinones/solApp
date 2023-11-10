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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WindowFormController implements Initializable {
    public AnchorPane mainWindowForm;
    public TextField idTextMostra;
    @FXML
    private Label idLbl1,idLbl2,idLbl3,idLbl4,idLbl5,idLbl6,idLbl7,idLbl8,idLbl9,idLbl10,idLbl11,idLbl12;
    @FXML
    private TextField idTxtFld1,idTxtFld2,idTxtFld3,idTxtFld5,idTxtFld6,idTxtFld7,idTxtFld8,idTxtFld9,idTxtFld10,idTxtFld11,idTxtFld12;
    @FXML
    private Button idBtnAcceptar;

    @FXML
    private DatePicker idTxtFld4;

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
        this.p = new Professor(
                //obligatoris: Nom, Cognom, isActive, dataNeixament (9999/09/09) --> controlador
                idTxtFld1.getText(),
                idTxtFld2.getText(),
                idTxtFld3.getText(),
                idTxtFld4.getValue().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
                idTxtFld5.getText(),
                idTxtFld6.getText(),
                idTxtFld7.getText(),
                idTxtFld8.getText(),
                idTxtFld9.getText(),
                true
        );
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

        //peticio servidor per guardar objecte
        boolean isOk = saveDataBd();

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

    public boolean saveDataBd(){
        //proces de crida servidor
        peticio.dropDades();
        try {
            socket.connect();
            peticio.setPeticio("ALTA_EMPLEAT");
            peticio.addDades(SingletonConnection.getInstance().getKey());
            peticio.addDades(JsonUtil.toJson(p));
            peticio.addDades(JsonUtil.toJson(u));

            System.out.println(JsonUtil.toJson(peticio));
            String resposta = socket.sendMessage(JsonUtil.toJson(peticio));

            //test deserializar a persona


            JSONObject jsonObject = new JSONObject(JsonUtil.toJson(peticio));
            Persona persona = Persona.fromJson(jsonObject.getJSONArray("dades").get(1).toString());
            System.out.println(persona.getDni());



            System.out.println(resposta);

        } catch (IOException e) {
            throw new RuntimeException(e);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //retorn estat del guardat
        return true;
    }
}
