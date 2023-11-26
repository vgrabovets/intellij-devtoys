package lermitage.intellij.ilovedevtoys.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
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

    private record MalformedStringRecord(String string, int line, int column) {
    }

    public static String prettyPrintJson(String jsonString) {
        if (jsonString.isBlank()) return "";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        jsonString = jsonString.replaceAll("\\\\\"", "\"");
        jsonString = StringUtils.strip(jsonString, "\"");
        MalformedStringRecord prevMalformedStringRecord = new MalformedStringRecord("", -1, -1);
        try {
            return prettyPrintJsonWithTryCatch(jsonString, prevMalformedStringRecord, gson);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private static String prettyPrintJsonWithTryCatch(String jsonString, MalformedStringRecord prevMalformedStringRecord, Gson gson) {
        try {
            Object jsonObject = gson.fromJson(jsonString, Object.class);
            String result = gson.toJson(jsonObject);
            return StringEscapeUtils.unescapeJson(result);
        } catch (JsonSyntaxException e) {
            int[] lineColumn = parseJsonErrorString(e.getMessage());
            if (lineColumn == null) return "Error: " + e.getMessage();
            int line = lineColumn[0];
            int column = lineColumn[1];

            String modifiedJsonString;
            if (
                (line == prevMalformedStringRecord.line() && column < prevMalformedStringRecord.column())
                    || line < prevMalformedStringRecord.line()
            ) {
                modifiedJsonString = escapeChar(prevMalformedStringRecord.string(), line, column);
            } else {
                modifiedJsonString = escapeChar(jsonString, line, column);
            }
            MalformedStringRecord malformedJsonRecord = new MalformedStringRecord(jsonString, line, column);
            return prettyPrintJsonWithTryCatch(modifiedJsonString, malformedJsonRecord, gson);
        }
    }

    private final static String errorPattern = "MalformedJsonException: .+ at line (?<line>\\d+) column (?<column>\\d+) path";
    private final static Pattern pattern = Pattern.compile(errorPattern);

    private static int @Nullable [] parseJsonErrorString(String errorString) {
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

    private static String escapeChar(String jsonString, int line, int column) throws RuntimeException {
        String[] stringArray = jsonString.split("\n");
        String fixedLine = stringArray[line - 1];
        int badCharPosition = column - 2;
        for (; badCharPosition >= 0; badCharPosition--) {
            String character = fixedLine.substring(badCharPosition - 1, badCharPosition);
            if (!character.isBlank()) {
                badCharPosition--;
                break;
            }
        }
        if (badCharPosition < 0) {
            throw new RuntimeException(String.format("Malformed JSON, cannot fix it automatically at line %d column %d", line, column));
        }
        fixedLine = fixedLine.substring(0, badCharPosition) + "\\" + fixedLine.substring(badCharPosition);
        stringArray[line - 1] = fixedLine;
        return String.join("\n", stringArray);
    }
}
