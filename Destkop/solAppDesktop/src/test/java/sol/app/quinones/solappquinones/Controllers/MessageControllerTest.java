package sol.app.quinones.solappquinones.Controllers;

import org.junit.jupiter.api.Test;
import sol.app.quinones.solappquinones.Controllers.Messages.MessageController;

public class MessageControllerTest {

    @Test
    public void testRecivedMessage(){
        MessageController messageController = new MessageController();
        messageController.callRecivedMessages();
    }



}
