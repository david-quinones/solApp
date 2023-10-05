module sol.app.quinones.solappquinones {
    requires javafx.controls;
    requires javafx.fxml;


    opens sol.app.quinones.solappquinones to javafx.fxml;
    exports sol.app.quinones.solappquinones;
}