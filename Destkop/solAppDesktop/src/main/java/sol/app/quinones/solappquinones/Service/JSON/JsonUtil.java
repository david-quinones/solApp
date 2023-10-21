package sol.app.quinones.solappquinones.Service.JSON;

import java.util.ArrayList;
import com.google.gson.Gson;

/**
 * Classe per facilitar les operacions de serialització JSON
 *
 * @author david
 */
public class JsonUtil {

    /** Instancia Gson utilitzada per les operacions de serialització*/
    private static final Gson gson = new Gson();
    /** Llista que guarda representacions JSON d'objecte en String... */
    private static ArrayList<String> dades = new ArrayList<>();

    /**
     * Afageix un objecte a la llista interna després de convertir a la seva representació "JSON"
     * @param ob Objecte afegir
     */
    public void afegirObjet(Object ob){
        this.dades.add(gson.toJson(ob));
    }

    /**
     * Converteix un objecte a representacó JSON
     * @param ob Objecte a serialitzar
     * @return Representació JSON de l'objecte
     */
    public static String toJson(Object ob){
        return gson.toJson(ob);
    }

}
