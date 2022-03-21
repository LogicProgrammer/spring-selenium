package com.automation.framework.utilities;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonUtilities {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static JsonObject getJsonObjectFromFile(String filePath){
        JsonElement element = null;
        try {
            element = JsonParser.parseReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert element != null;
        return element.getAsJsonObject();
    }

    public static String getJsonStringFromFile(String filePath,boolean format){
        JsonElement element = null;
        try {
            element = JsonParser.parseReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert element != null;
        if(format)
            return gson.toJson(element.getAsJsonObject());
        else
            return element.getAsString();
    }

    public static String getJsonStringFromObject(Object object,boolean format){
        if(format)
            return gson.toJson(object);
        else
            return new Gson().toJson(object);
    }

}
