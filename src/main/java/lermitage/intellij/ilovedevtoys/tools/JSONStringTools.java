package lermitage.intellij.ilovedevtoys.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;

public class JSONStringTools {

    public static String jsonToString(String json) {
        try {
            if (json.isBlank()) {
                return "";
            }
            JsonNode jsonNodeTree = new ObjectMapper().readTree(json);
            String escapedJson = jsonNodeTree.toString()
                .replaceAll("\\\\", "\\\\\\\\")
                .replaceAll("\"", "\\\\\"");
            return "\"" + escapedJson + "\"";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public static String prettyPrintJson(String jsonString) {
        try {
            if (jsonString.isBlank()) return "";
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            jsonString = jsonString.replaceAll("\\\\\"", "\"");
            jsonString = StringUtils.strip(jsonString, "\"");
            Object jsonObject = gson.fromJson(jsonString, Object.class);
            return gson.toJson(jsonObject);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
