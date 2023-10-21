package sol.app.quinones.solappquinones.Service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import sol.app.quinones.solappquinones.Models.Usuari;

public class SingletonConnectionTest {

    @Test
    public void testSingletonProperty(){
        SingletonConnection singletonConnectionA = SingletonConnection.getInstance();
        SingletonConnection singletonConnectionB = SingletonConnection.getInstance();

        assertSame(singletonConnectionA, singletonConnectionB);
    }

    @Test
    public void testSetKey(){
        SingletonConnection singletonConnection = SingletonConnection.getInstance();
        String key = "20231021902";
        singletonConnection.setKey(key);
        assertEquals(key, singletonConnection.getKey());
    }

    @Test
    public void testSetKeyException(){
        SingletonConnection singletonConnection = SingletonConnection.getInstance();
        String key = "20231021902";
        singletonConnection.setKey(key);
        assertThrows(UnsupportedOperationException.class, () -> singletonConnection.setKey("202310210904"),"Ha de llençar l'excepció perque estem intentant reassignar quan ja assignem");
    }

    @Test
    public void testSetAndGetUserAssigned(){
        SingletonConnection singletonConnection = SingletonConnection.getInstance();
        Usuari usuari = new Usuari("david", "david");
        singletonConnection.setUserConnectat(usuari);

        assertSame(usuari, singletonConnection.getUserConnectat());
    }

    @Test
    public void testCloseConnection(){
        SingletonConnection singletonConnection = SingletonConnection.getInstance();
        singletonConnection.closeConnection();
        SingletonConnection singletonConnection1 = SingletonConnection.getInstance();
        assertNotSame(singletonConnection, singletonConnection1);
    }

}
