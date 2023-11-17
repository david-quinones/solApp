package sol.app.quinones.solappquinones.Models;

import javafx.scene.layout.HBox;

public class VistaController<T> {
    private HBox view;
    private T controller;

    public VistaController(HBox view, T controller) {
        this.view = view;
        this.controller = controller;
    }

    public HBox getView() {
        return view;
    }

    public void setView(HBox view) {
        this.view = view;
    }

    public T getController() {
        return controller;
    }

    public void setController(T controller) {
        this.controller = controller;
    }
}
