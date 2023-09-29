package lermitage.intellij.ilovedevtoys.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private final static String errorPattern = "Error: com\\.google\\.gson\\.stream\\.MalformedJsonException: Unterminated object at line (?<line>\\d+) column (?<column>\\d+) path";
    private final static Pattern pattern = Pattern.compile(errorPattern);

    public static int @Nullable [] parseError(String errorString) {
        Matcher matcher = pattern.matcher(errorString);
        if (matcher.find()) {
            String line = matcher.group("line");
            String column = matcher.group("column");
            if (!line.isEmpty() && !column.isEmpty()) {
                return new int[]{Integer.parseInt(line), Integer.parseInt(column)};
            }
        }
        return null;
    }
}
