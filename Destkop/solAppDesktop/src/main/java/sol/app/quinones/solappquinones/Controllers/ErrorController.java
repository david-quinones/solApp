package sol.app.quinones.solappquinones.Controllers;

import javafx.scene.control.Alert;

/**
 * Controlador per els errors del programa
 *
 * @author david
 */
public class ErrorController {

    /**
     * Mostra una alerta d'error
     *
     * @param title       the title
     * @param headerText  the header text
     * @param contentText the content text
     */
    public static void showErrorAlert(String title, String headerText, String contentText, Alert.AlertType ttype){
        Alert alert = new Alert(ttype);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

}
