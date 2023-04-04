package com.parallel.utils;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JsonReader {


    public static JSONObject getJSONFromFile(String fileName) throws ParseException {

        String jsonText = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(FrameworkConstant.TestDataJSON_PATH+fileName+".json"));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonText += line + "\n";
            }

            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONParser parser = new JSONParser();
        Object object;
        object = parser.parse(jsonText);
        JSONObject mainJsonObject = (JSONObject) object;
        return mainJsonObject;

    }


    public static JSONObject getJSONFromQuery(String fileType) throws ParseException {
        BufferedReader bufferedReader;
        String jsonText = "";
        try {
            if(fileType.equalsIgnoreCase("output")) {
                bufferedReader = new BufferedReader(new FileReader(FrameworkConstant.MYSQLJSON_OUTPUT_PATH));
            }
            else  {
                bufferedReader = new BufferedReader(new FileReader(FrameworkConstant.MYSQLJSON_INPUT_PATH));
            }

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonText += line + "\n";
            }

            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONParser parser = new JSONParser();
        Object object;
        object = parser.parse(jsonText);
        JSONObject mainJsonObject = (JSONObject) object;
        return mainJsonObject;

    }

    // To Iterate over a Json Array
    public static Object[][] getdata(String JSON_path, String typeData, int totalDataRow, int totalColumnEntry)
            throws JsonIOException, JsonSyntaxException, FileNotFoundException {
        @SuppressWarnings("deprecation")
        JsonParser jsonParser = new JsonParser();
        @SuppressWarnings("deprecation")
        JsonObject jsonObj = jsonParser.parse(new FileReader(JSON_path)).getAsJsonObject();
        JsonArray array = (JsonArray) jsonObj.get(typeData);
        return searchJsonElemnet(array, totalDataRow, totalColumnEntry);
    }

    public static Object[][] searchJsonElemnet(JsonArray jsonArray, int totalDataRow, int totalColumnEntry)
            throws NullPointerException {

        Object[][] matrix = new Object[totalDataRow][totalColumnEntry];
        int i = 0;
        int j = 0;
        for (JsonElement jsonElement : jsonArray) {
            for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                matrix[i][j] = entry.getValue().toString().replace("\"", "");
                j++;
            }
            i++;
            j = 0;
        }
        return matrix;
    }

    // To get the direct value from json
    public static String getJSONvalue(String fileName, String key) throws ParseException {
        String value = "";
        JSONObject mainJsonObject = getJSONFromFile(fileName);
        value = (String) mainJsonObject.get(key);
        return value;
    }

    // To get value from json object
    public static String getJSONobj(String fileName, String objKey, String key) throws ParseException {
        String value = "";
        JSONObject mainJsonObject = getJSONFromFile(fileName);
        JSONObject jsonObject = (JSONObject) mainJsonObject.get(objKey);
        value = (String) jsonObject.get(key);
        return value;
    }

    // To get value from json object
    public static void updateJSONobj(String fileName, String objKey, String key, String newValue) throws ParseException {
        String oldValue = getJSONobj(fileName,objKey,key);
        JSONObject mainJsonObject = getJSONFromFile(fileName);
        JSONObject jsonObject = (JSONObject) mainJsonObject.get(objKey);
        jsonObject.replace(key,oldValue,newValue);
    }

    public static String getJSONQuery(String objKey, String key,String FileType) throws ParseException {
        String value = "";
        JSONObject mainJsonObject = getJSONFromQuery(FileType);
        JSONObject jsonObject = (JSONObject) mainJsonObject.get(objKey);
        value = (String) jsonObject.get(key);
        return value;
    }

    // To get value from json Array
    public static String getJSONarray(String fileName, String arrayKey, String key) throws ParseException {
        String value = "";
        JSONObject mainJsonObject = getJSONFromFile(fileName);
        JSONArray jsonArray = (JSONArray) mainJsonObject.get(arrayKey);
        for (int i = 0; i < jsonArray.size()-1; i++) {
            JSONObject jsonArraydata = (JSONObject) jsonArray.get(i);
            value = (String) jsonArraydata.get(key);
        }
        return value;
    }
    public static List<String> getJSONarrayQuery(String arrayKey, String key, String FileType) throws ParseException {

        List<String> value = new ArrayList<String>();
        JSONObject mainJsonObject =  getJSONFromQuery(FileType);

        JSONArray jsonArray = (JSONArray) mainJsonObject.get(arrayKey);

        for (int i = 0; i < jsonArray.size(); i++) {

            JSONObject jsonArraydata = (JSONObject) jsonArray.get(i);
            value.add((String) jsonArraydata.get(key));
        }
        return value;
    }
}
