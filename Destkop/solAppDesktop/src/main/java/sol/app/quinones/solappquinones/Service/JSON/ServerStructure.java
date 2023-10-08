package sol.app.quinones.solappquinones.Service.JSON;

import java.util.ArrayList;
import com.google.gson.Gson;

public class ServerStructure {

    private Gson gson = new Gson();

    private ArrayList<String> dades = new ArrayList<>();

    //añadir al array una vez deserializado
    public void afegirObjet(Object ob){
        this.dades.add(gson.toJson(ob));
    }

    //serializar a JSON
    public String toJson(Object ob){
        return gson.toJson(ob);
    }

}
