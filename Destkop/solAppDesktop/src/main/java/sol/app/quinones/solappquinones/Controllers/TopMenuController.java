package sol.app.quinones.solappquinones.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Controlador menu superior
 *
 * Cotrola les interacions amb els botons del menu superior en una interficie
 *
 * @author david
 */
public class TopMenuController {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnSearch;


    private ITopMenuDelegation accions;

    /**
     * Estabelix un delegat per dur a terme les accions del menu suepeior
     * @param accions Implementacio de ITopMenuDelegation que menjrar les accions
     */
    public void setTopMenuDelegation(ITopMenuDelegation accions){
        this.accions = accions;
    }

    /**
     * Acció que s'executarà cuan es premi el boto de afegir
     * Delega l'acció a la implementació
     */
    @FXML
    private void add(){ //nom d'acció que estableix el boto al FXML
        if(accions != null){
            accions.onBtnCrear();
        }
    }

    /**
     * Acció que s'executarà cuan es premi el boto de edit
     * Delega l'acció a la implementació
     */
    @FXML
    private void edit(){ //nom d'acció que estableix el boto al FXML, per aixo s'injecta el @FXML
        if(accions != null){
            accions.onBtnEditar();
        }
    }

    /**
     * Acció que s'executarà cuan es premi el boto de eleiminar
     * Delega l'acció a la implementació
     */
    @FXML
    private void delete(){ //nom d'acció que estableix el boto al FXML
        if(accions != null){
            accions.onBtnEliminar();
        }
    }

    /**
     * Constructor
     */
    public TopMenuController() {
    }

    /**
     * Inicialitza el controlador
     *
     * Configura estat inicial de uns camps //TODO
     */
    @FXML
    public void initialize() {
        //TODO
        txtSearch.setDisable(true);
        btnSearch.setDisable(true);
    }

    /**
     * Habilita o deshabilita el boto de crear
     *
     * @param disable true-> deshabilita el botó, false -> deixa habilitat
     */
    public void disableCrearBoto(boolean disable){
        btnAdd.setDisable(disable);
    }

}