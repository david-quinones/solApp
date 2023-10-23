package estel.solapp.common;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

public class JsonData {

    private ArrayList<String> dades = new ArrayList<>();


    /**
     * Adds a data to the order at the end.
     * @param data data to be added
     */

    public void addDataObject(Object data){

        Gson gson = new Gson();
        this.dades.add(gson.toJson(data));
    }

    /**
     * Adds a data to the order at the end.
     * @param data data to be added
     */
    public void addPrimitiveData(Object data){

        this.dades.add(data+"");

    }


    /**
     * Gets the data, at the specified position, as an object of the specified class.
     * @param DataNumber data position (starting with 0)
     * @param clasz data class
     * @return parameter value
     */
    public Object getData(int DataNumber, Class clasz){
        try{
            Gson gson = new Gson();
            return gson.fromJson(dades.get(DataNumber),clasz);
        }catch(JsonSyntaxException ex){return null;}
    }

}
