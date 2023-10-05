module sol.app.quinones.solappquinones {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;

    opens sol.app.quinones.solappquinones to javafx.fxml;
    exports sol.app.quinones.solappquinones;
    exports sol.app.quinones.solappquinones.Controllers;
    exports sol.app.quinones.solappquinones.Controllers.admin;
    exports sol.app.quinones.solappquinones.Controllers.user;
    exports sol.app.quinones.solappquinones.Models;
    exports sol.app.quinones.solappquinones.Views;
}