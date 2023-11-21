package sol.app.quinones.solappquinones.Service;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe encarregada de tastejar amb JUnit el correcte funcionament en la comuncaició amb un servidor utilitzat Socket
 *
 * Proves dissenyades per validar la correcta insercio i transmissio de missatges client - servidor
 *
 * @author david
 */

public class ServerComunicationTest  {

    /**
     * Test per revisar el correcte funcionament de sendMessage, assegurant que enviem i revem missatge correctament
     *
     * Realiza les seguents accions:
     *      Configurar un objecte Mock per simular connexió en xarxa
     *      Creem una instancia de SercverConmnection i assignem el mock (ssobrescribim el metdoe)
     *      Enviemm un missatge i captrum respota
     *      Comprovem que la respota correcte es la esperada
     *      Comprovem que formem bé els missatges que enviem
     *
     * @throws IOException perque la connexio per xarxa por llenár aquesta excepcio
     */
    @Test
    public void testSendMessage() throws IOException {
        //Configurem els mocks (simular connexió)
        Socket socketMock = mock(Socket.class);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("resposta".getBytes());

        when(socketMock.getOutputStream()).thenReturn(byteArrayOutputStream);
        when(socketMock.getInputStream()).thenReturn(byteArrayInputStream);

        //creem instancia de ServerComunication i assignem el mock del coket
        ServerComunication serverComunication = new ServerComunication("localhost", 9999){
            @Override
            public void connect() throws IOException{
                setSocket(socketMock);
            }
        };

        //executar el metode en mode test
        serverComunication.connect();
        String resposta = serverComunication.sendMessage("envio missatge, arriba?");

        //capturar resposta i comprovem que ok
        assertEquals("resposta", resposta);
        assertEquals("envio missatge, arriba?\r\n", byteArrayOutputStream.toString());

    }



}
