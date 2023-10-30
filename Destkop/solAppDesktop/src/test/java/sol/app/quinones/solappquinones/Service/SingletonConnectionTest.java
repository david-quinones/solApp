package sol.app.quinones.solappquinones.Service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import sol.app.quinones.solappquinones.Models.Usuari;

/**
 * Classe encarregada de tastejar amb jUnit la connexio singleto
 *
 * Comprovem que la connexio al singleto funciona correctament, asegurant una unica instancia del mateix en la seva execició
 *
 * @author david
 */
public class SingletonConnectionTest {

    /**
     * test per comprovar la propietat de singleto assegurant que dues instancies son la mateixa
     */
    @Test
    public void testSingletonProperty(){
        SingletonConnection singletonConnectionA = SingletonConnection.getInstance();
        SingletonConnection singletonConnectionB = SingletonConnection.getInstance();

        assertSame(singletonConnectionA, singletonConnectionB);
    }

    /**
     * Test per comprovar la correcta assignació de una key del singleto
     */
    @Test
    public void testSetKey(){
        SingletonConnection singletonConnection = SingletonConnection.getInstance();
        String key = "20231021902";
        singletonConnection.setKey(key);
        assertEquals(key, singletonConnection.getKey());
    }

    /**
     * Test que comprova el llençament de l'excepció si intenem assignar una key quan ja la te assignada
     */
    @Test
    public void testSetKeyException(){
        SingletonConnection singletonConnection = SingletonConnection.getInstance();
        String key = "20231021902";
        singletonConnection.setKey(key);
        assertThrows(UnsupportedOperationException.class, () -> singletonConnection.setKey("202310210904"),"Ha de llençar l'excepció perque estem intentant reassignar quan ja assignem");
    }

    /**
     * Test per comprovar l'assignació i recuperació de un usuari
     */
    @Test
    public void testSetAndGetUserAssigned(){
        SingletonConnection singletonConnection = SingletonConnection.getInstance();
        Usuari usuari = new Usuari("david", "david");
        singletonConnection.setUserConnectat(usuari);

        assertSame(usuari, singletonConnection.getUserConnectat());
    }

    /**
     * Test per comprovar que podem tancar la connexió de Singleto i assignar una nova instancia i son diferents
     */
    @Test
    public void testCloseConnection(){
        SingletonConnection singletonConnection = SingletonConnection.getInstance();
        singletonConnection.closeConnection();
        SingletonConnection singletonConnection1 = SingletonConnection.getInstance();
        assertNotSame(singletonConnection, singletonConnection1);
    }

}
