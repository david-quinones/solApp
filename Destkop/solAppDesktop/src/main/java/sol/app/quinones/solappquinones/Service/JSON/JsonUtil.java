package sol.app.quinones.solappquinones.Service.JSON;

import java.util.ArrayList;
import com.google.gson.Gson;

public class JsonUtil {

    private static final Gson gson = new Gson();

    private static ArrayList<String> dades = new ArrayList<>();

    //añadir al array, primero convierte json
    public void afegirObjet(Object ob){
        this.dades.add(gson.toJson(ob));
    }

    //serializar a JSON (con afegir no hace falta..?)
    public static String toJson(Object ob){
        return gson.toJson(ob);
    }

}
