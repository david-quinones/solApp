package sol.app.quinones.solappquinones.Service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Classe que realitza test als validados de camps publics
 *
 * @author david
 */
public class ValidarCampsTest {

    /**
     * Validacions DNI
     */
    @Test
    public void validarDNICorrecte() {
        assertTrue(ValidadorCamps.validarDNI("12345678A"));
    }

    @Test
    public void validarDNIIncorrecte() {
        assertFalse(ValidadorCamps.validarDNI("1234567A"));
        assertFalse(ValidadorCamps.validarDNI("123456780"));
        assertFalse(ValidadorCamps.validarDNI("ABCDEFGHI"));
    }

    /**
     * Validacions Telefon
     */
    @Test
    public void validarTelefonoCorrecte() {
        assertTrue(ValidadorCamps.validarTelf("972880055"));
    }

    @Test
    public void validarTelefonoIncorrecte() {
        assertFalse(ValidadorCamps.validarTelf("12345678"));
        assertFalse(ValidadorCamps.validarTelf("1234567890"));
        assertFalse(ValidadorCamps.validarTelf("abracadabra"));
    }

    /**
     * Valdiacions E-mail
     */
    @Test
    public void validarEmailCorrecte() {
        assertTrue(ValidadorCamps.validarMail("email@ioc.com"));
    }

    @Test
    public void validarEmailIncorrecte() {
        assertFalse(ValidadorCamps.validarMail("ioc@ioc"));
        assertFalse(ValidadorCamps.validarMail("ioc.com"));
        assertFalse(ValidadorCamps.validarMail("@ioc.com"));
    }
}
