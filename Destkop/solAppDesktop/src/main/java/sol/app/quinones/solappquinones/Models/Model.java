package sol.app.quinones.solappquinones.Models;

import sol.app.quinones.solappquinones.Views.ViewFactory;

public class Model {

    private static Model model;
    private final ViewFactory viewFactory;

    //singleton
    private Model(){
        this.viewFactory = new ViewFactory();
    }

    public static synchronized  Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }
}
