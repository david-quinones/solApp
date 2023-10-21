package sol.app.quinones.solappquinones.Service;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class ServerComunicationTest  {

    @Test
    public void testSendMessage() throws IOException {
        //Configurem els mocks (simular connexió)
        Socket socketMock = mock(Socket.class);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("Respotsa".getBytes());

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

        //capturar respota
        assertEquals("Respotsa", resposta);
        assertEquals("envio missatge, arriba?\r\n", byteArrayOutputStream.toString());

    }



}
