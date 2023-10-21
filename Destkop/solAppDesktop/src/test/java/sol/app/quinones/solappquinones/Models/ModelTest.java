package sol.app.quinones.solappquinones.Models;

import org.junit.jupiter.api.Test;
import sol.app.quinones.solappquinones.Views.ViewFactory;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {

    @Test
    public void testSingleton(){
        Model modelA = Model.getInstance();
        Model modelB = Model.getInstance();

        assertSame(modelA, modelB, "Han de ser la mateix instancia");
    }

    @Test
    public void testViewFactoryNotNull(){
        Model model = Model.getInstance();
        assertNotNull(model.getViewFactory(), "No hauria de ser null");
    }

    @Test
    public void testViewFactorySingleton(){
        Model model = Model.getInstance();
        ViewFactory viewFactoryA = model.getViewFactory();
        ViewFactory viewFactoryB = model.getViewFactory();

        assertSame(viewFactoryA, viewFactoryB, "Ha de ser la mateix instanica");
    }

}
