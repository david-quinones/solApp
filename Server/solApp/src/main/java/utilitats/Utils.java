package utilitats;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**Classe que contindrá diferents utilitats per a la apalcació
 *
 * @author Pau Castell Galtes
 */
public class Utils {
    
    /**Mètode per formatejar la data rebuda en format Date a String
     * 
     * @param data que s'ha de formatejar
     * @return data en format String
     */
    public static String formatData(Date data){
        SimpleDateFormat formatData = new SimpleDateFormat("yyyy-MM-dd");
        String dataString = formatData.format(data);
        return dataString;
    }
}
