module sol.app.quinones.solappquinones {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires com.google.gson;
    requires org.json.chargebee;

    opens sol.app.quinones.solappquinones to javafx.fxml;
    opens sol.app.quinones.solappquinones.Controllers.MainWindow to javafx.fxml;
    opens sol.app.quinones.solappquinones.Models to com.google.gson;
    exports sol.app.quinones.solappquinones;
    exports sol.app.quinones.solappquinones.Controllers;
    exports sol.app.quinones.solappquinones.Controllers.MainWindow;
    exports sol.app.quinones.solappquinones.Models;
    exports sol.app.quinones.solappquinones.Views;

}