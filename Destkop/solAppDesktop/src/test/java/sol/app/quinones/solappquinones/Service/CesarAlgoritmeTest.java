package sol.app.quinones.solappquinones.Service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Clase per realitzar probes sobre el xifratge de password
 *
 * @author david
 */
public class CesarAlgoritmeTest {

    /**
     * Test per mirar que tant codificant com descodificant son el mateix
     */
    @Test
    public void testCodificarDescodificar(){
        String original = "david2007";
        String codificar = CesarAlgoritme.codificar(original);
        String descodificar = CesarAlgoritme.descodificar(codificar);

        assertEquals(original, descodificar);
    }

    /**
     * test amb caracters especials
     */
    @Test
    public void testCodificarDescodificarCaractersEspecials(){
        String original = "d@v!d1=?*";
        String codificar = CesarAlgoritme.codificar(original);
        String descodificar = CesarAlgoritme.descodificar(codificar);

        assertEquals(original, descodificar);
    }



}
