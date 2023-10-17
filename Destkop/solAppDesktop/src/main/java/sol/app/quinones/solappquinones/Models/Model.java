package sol.app.quinones.solappquinones.Models;

import sol.app.quinones.solappquinones.Views.ViewFactory;

/**
 * Clase que representa una implementació de Singleton del model de l'aplicació
 * Proporciona un punt d'acces global a la unica instancia i a la fabrica de vistes (ViewFactory)
 *
 * @author david
 */
public class Model {

    //unica instancia de la clase Model
    private static Model model;
    //referencia a la "fabrica de vistes"
    private final ViewFactory viewFactory;

    /**
     * Contrutor privat per asegurar que no es creein instancies de la clase sino es de si mateixa
     * Inicialitza la fabrica de vistes
     */
    private Model(){
        this.viewFactory = new ViewFactory();
    }

    /**
     * Proporciona el punt d'accés global per obtenir/crear l'instancia
     * @return instancia Model
     */
    public static synchronized  Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return model;
    }

    /**
     * Retorna l'instancia de la fabrica de vistes
     * @return ViewFactory
     */
    public ViewFactory getViewFactory() {
        return viewFactory;
    }
}
