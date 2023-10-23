package estel.solapp.common;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

public class JsonData {

    private ArrayList<String> dades = new ArrayList<>();


    /**
     * Afegeix objecte data.
     * @param data dada objecte a afegir
     */

    public void addDataObject(Object data){

        Gson gson = new Gson();
        this.dades.add(gson.toJson(data));
    }

    /**
     * Afegeig dada primitiva.
     * @param data dada a afegir
     */
    public void addPrimitiveData(Object data){

        this.dades.add(data+"");

    }


    /**
     * Agafa la dada en la posició especificada com objecte de la classe especificada.
     * @param DataNumber Posició de la dada (començant per 0)
     * @param clasz Classe de la dada
     * @return dada
     */
    public Object getData(int DataNumber, Class clasz){
        try{
            Gson gson = new Gson();
            return gson.fromJson(dades.get(DataNumber),clasz);
        }catch(JsonSyntaxException ex){return null;}
    }

}
