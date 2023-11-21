package sol.app.quinones.solappquinones.Models;

import javafx.scene.layout.HBox;

/**
 * Classe generica per associar una vista amb el seu controlador
 *
 * @param <T> type controller associated viwe
 * @author david
 */
public class VistaController<T> {
    private HBox view;
    private T controller;

    /**
     * Contructor per inicialitzar una VisataController amb una vista i un controldor
     *
     * @param view       Vista (hbox) assodit controllador
     * @param controller Contolador associat a la vista
     */
    public VistaController(HBox view, T controller) {
        this.view = view;
        this.controller = controller;
    }

    /**
     * Gets view.
     *
     * @return the view
     */
    public HBox getView() {
        return view;
    }

    /**
     * Sets view.
     *
     * @param view the view
     */
    public void setView(HBox view) {
        this.view = view;
    }

    /**
     * Gets controller.
     *
     * @return the controller
     */
    public T getController() {
        return controller;
    }

    /**
     * Sets controller.
     *
     * @param controller the controller
     */
    public void setController(T controller) {
        this.controller = controller;
    }
}
