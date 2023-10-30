package sol.app.quinones.solappquinones.Models;

import org.junit.jupiter.api.Test;
import sol.app.quinones.solappquinones.Views.ViewFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe encarregada de realitzar test sobre la classe Model per assegurar el correcte funcionament
 *
 * Conte metodes per comprovar:
 * Model Singleton
 * Fabrica vistes associada a Model no es null
 * Fabrica vista associada a Model seguiex patro Singleton
 *
 * S'utilitza JUnit per els test
 *
 * @author david
 */
public class ModelTest {

    /**
     * Test per comprovar que la clase Model segueix el patro Singleto
     * Creem dues instancies i mirem que tenen la mateixa instancia
     */
    @Test
    public void testSingleton(){
        Model modelA = Model.getInstance();
        Model modelB = Model.getInstance();

        assertSame(modelA, modelB, "Han de ser la mateix instancia");
    }

    /**
     * Test per assegurar que la fabrica de vistes associada al Model, no es null
     * Instanciem model i assegurem que la fabrica no retorna null
     */
    @Test
    public void testViewFactoryNotNull(){
        Model model = Model.getInstance();
        assertNotNull(model.getViewFactory(), "No hauria de ser null");
    }

    /**
     * Test per comprovar que la dabrica de vistes associada a Model seguiex patro Singleton
     * Creem instancia model, sobtenim dues instancies de ViewFac i asegurem que son la mateixa instancia
     */
    @Test
    public void testViewFactorySingleton(){
        Model model = Model.getInstance();
        ViewFactory viewFactoryA = model.getViewFactory();
        ViewFactory viewFactoryB = model.getViewFactory();

        assertSame(viewFactoryA, viewFactoryB, "Ha de ser la mateix instanica");
    }

}
