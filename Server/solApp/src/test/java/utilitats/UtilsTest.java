package utilitats;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**Classe per probar els métodes de la classe Utils
 *
 * @author Pau Castell Galtes
 */
public class UtilsTest {
    
    /**Test per comprobar el mètode FormatData
     * 
     * @throws ParseException 
     */
    @Test
    public void testFormatData() throws ParseException {
        String data = "2023-11-01";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = new Date(format.parse(data).getTime());
        String dataFormatejada = Utils.formatData(fecha);
        assertEquals("2023-11-01", dataFormatejada);
    }
    
}
