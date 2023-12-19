package sol.app.quinones.solappquinones.Models;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe encarregada de realitzar probes unitaries a Message
 *
 * @author david
 */
public class MessageTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        Message message = new Message();
        assertNotNull(message);
    }

    /**
     * test crear un missatge amb dades
     */
    @Test
    public void testMissatge() {
        Persona remitent = new Persona();
        remitent.setNom("marius");
        List<Persona> destinetaris = new ArrayList<>();
        Persona destinatari = new Persona();
        destinatari.setNom("Joan");
        destinetaris.add(destinatari);
        Message message = new Message(remitent, destinetaris, "2023-12-19", "Soc un Test molt aixerit", false, false);

        assertEquals(remitent, message.getRemitentPersona());
        assertEquals(destinetaris, message.getDestinataris());
        assertEquals("2023-12-19", message.getDataEnviament());
        assertEquals("Soc un Test molt aixerit", message.getContingut());
        assertFalse(message.isDestinetariEsborrat());
        assertFalse(message.isRemitentEsborrat());
    }


    /**
     * test deserailitzar objecte json a missatge
     */
    @Test
    public void testFromJson() {
        String json = "{\"idMissatge\":1,\"RemitentPersona\":{\"nombre\":\"David\"},\"destinataris\":[{\"nombre\":\"Juan\"}],\"dataEnviament\":\"2023-12-19\",\"contingut\":\"Missatge JSON\",\"destinetariEsborrat\":false,\"remitentEsborrat\":false}";
        Message messahe = Message.fromJson(json);

        assertNotNull(messahe);
        assertEquals(1, messahe.getIdMissatge());
        assertNotNull(messahe.getRemitentPersona());
        assertNotNull(messahe.getDestinataris());
        assertEquals("2023-12-19", messahe.getDataEnviament());
        assertEquals("Missatge JSON", messahe.getContingut());
        assertFalse(messahe.isDestinetariEsborrat());
        assertFalse(messahe.isRemitentEsborrat());
    }
}
