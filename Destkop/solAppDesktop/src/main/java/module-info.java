module sol.app.quinones.solappquinones {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires de.jensd.fx.glyphs.fontawesome;
    requires com.google.gson;
    requires org.json.chargebee;

    opens sol.app.quinones.solappquinones to javafx.fxml;
    opens sol.app.quinones.solappquinones.Controllers.MainWindow to javafx.fxml;
    opens sol.app.quinones.solappquinones.Controllers to javafx.fxml;
    opens sol.app.quinones.solappquinones.Models to com.google.gson;
    exports sol.app.quinones.solappquinones;
    exports sol.app.quinones.solappquinones.Controllers;
    exports sol.app.quinones.solappquinones.Controllers.MainWindow;
    exports sol.app.quinones.solappquinones.Models;
    exports sol.app.quinones.solappquinones.Views;
    exports sol.app.quinones.solappquinones.Controllers.Professor;
    opens sol.app.quinones.solappquinones.Controllers.Professor to javafx.fxml;
    exports sol.app.quinones.solappquinones.Controllers.Alumne;
    opens sol.app.quinones.solappquinones.Controllers.Alumne to javafx.fxml;
    exports sol.app.quinones.solappquinones.Controllers.Usuari;
    opens sol.app.quinones.solappquinones.Controllers.Usuari to javafx.fxml;
    exports sol.app.quinones.solappquinones.Controllers.Aula;
    opens sol.app.quinones.solappquinones.Controllers.Aula to javafx.fxml;
    exports sol.app.quinones.solappquinones.Controllers.Messages;
    opens sol.app.quinones.solappquinones.Controllers.Messages to javafx.fxml;

}