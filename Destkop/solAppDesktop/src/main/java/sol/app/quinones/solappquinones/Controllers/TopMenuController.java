package sol.app.quinones.solappquinones.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class TopMenuController {

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;



    @FXML
    private VBox mainAula;
    private ITopMenuDelegation accions;

    public void setTopMenuDelegation(ITopMenuDelegation accions){
        this.accions = accions;
    }

    @FXML
    private void add(){
        if(accions != null){
            accions.onBtnCrear();
        }
    }

    @FXML
    private void edit(){
        if(accions != null){
            accions.onBtnEditar();
        }
    }

    @FXML
    private void delete(){
        if(accions != null){
            accions.onBtnEliminar();
        }
    }


    public TopMenuController() {
    }


    @FXML
    public void initialize() {


    }

    // Métodos de eventos para cada botón según se necesite
}