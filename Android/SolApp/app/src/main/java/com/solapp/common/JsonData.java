package com.solapp.common;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
public class JsonData {


    private ArrayList<String> data = new ArrayList<>();


    /**
     * Adds a data to the order at the end.
     * @param data data to be added
     */

    public void addDataObject(Object data){

        Gson gson = new Gson();
        this.data.add(gson.toJson(data));
    }

    /**
     * Adds a data to the order at the end.
     * @param data data to be added
     */
    public void addPrimitiveData(Object data){

        this.data.add(data+"");

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
            return gson.fromJson(data.get(DataNumber),clasz);
        }catch(JsonSyntaxException ex){return null;}
    }






}

