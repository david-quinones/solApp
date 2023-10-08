package estructurapr;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;

/**Clase que contindrà les dades de les peticions i respostes de les crides
 * al servidor
 * @author pau
 */
public class DadesPR {
    
    private ArrayList<String> dades = new ArrayList<>();
    
    
    /**Afegeix les dades a la petició o resposta dins un ArrayList
     * 
     * @param dades 
     */
    public void affegirDades(Object dades){
        Gson gson = new Gson();
        this.dades.add(gson.toJson(dades));
    }
    
    
    /**Recupera les dades de la petició o resposta com un objecte de la clase
     * especificada.
     * @param posicio, posició de les dades dins l'ArrayList
     * @param classe, classe de l'ojecte que s'ha de recuperar
     * @return objecte de la classe especificada
     */
    public Object getDades(int posicio, Class classe){
        try{
            Gson gson = new Gson();
            return gson.fromJson(dades.get(posicio), classe);
        }catch(JsonSyntaxException ex){return null;}
    }
    
    
    
    /**Afegeix dades primitives al array de dades
     * 
     * @param dades , dades que s'afegiran.
     */
    public void afegirDadesPrimitives(Object dades){
        this.dades.add(dades+"");
    }
    
}
