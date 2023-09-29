package lermitage.intellij.ilovedevtoys.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONStringToolsTest {

    @Test
    void should_jsonToString_convert_minimal_string() {
        assertEquals(
            "\"{\\\"browsers\\\":2}\"",
            JSONStringTools.jsonToString("{\"browsers\": 2}"));
    }

    @Test
    void should_jsonToString_convert_simple_string() {
        assertEquals(
            "\"{\\\"browsers\\\":\\\"1\\\\\\\\2\\\"}\"",
            JSONStringTools.jsonToString("{\"browsers\": \"1\\\\2\"}"));
    }

    @Test
    void should_jsonToString_convert_complex_string() {
        assertEquals(
            "\"{\\\"browsers\\\":{\\\"firefox\\\":{\\\"name\\\":\\\"Firefox\\\\\\\\\\\",\\\"pref_url\\\":\\\"about:config\\\",\\\"releases\\\":{\\\"1\\\":{\\\"release_date\\\":\\\"2004-11-09\\\",\\\"status\\\":\\\"retired\\\",\\\"engine\\\":\\\"Gecko\\\",\\\"engine_version\\\":\\\"1.7\\\"}}}}}\"",
            JSONStringTools.jsonToString("""
                {
                  "browsers": {
                    "firefox": {
                      "name": "Firefox\\\\",
                      "pref_url": "about:config",
                      "releases": {
                        "1": {
                          "release_date": "2004-11-09",
                          "status": "retired",
                          "engine": "Gecko",
                          "engine_version": "1.7"
                        }
                      }
                    }
                  }
                }
                """));
    }

    @Test
    public void shouldPrettyPrintJson() {
        String uglyJsonString = "{\"one\":\"AAA\",\"two\":[\"BBB\",\"CCC\"],\"three\":{\"four\":\"DDD\",\"five\":[\"EEE\",\"FFF\"]}}";
        String formattedJsonString = JSONStringTools.prettyPrintJson(uglyJsonString);
        String expectedPrettyJson = """
            {
              "one": "AAA",
              "two": [
                "BBB",
                "CCC"
              ],
              "three": {
                "four": "DDD",
                "five": [
                  "EEE",
                  "FFF"
                ]
              }
            }""";
        assertEquals(expectedPrettyJson, formattedJsonString);

        String uglyJsonString2 = "\"{\\\"one\\\":\\\"AAA\\\",\\\"two\\\":[\\\"BBB\\\",\\\"CCC\\\"],\\\"three\\\":{\\\"four\\\":\\\"DDD\\\",\\\"five\\\":[\\\"EEE\\\",\\\"FFF\\\"]}}\"";
        String formattedJsonString2 = JSONStringTools.prettyPrintJson(uglyJsonString2);
        assertEquals(expectedPrettyJson, formattedJsonString2);

        String uglyJsonString3 = """
            {'one': 'one one', 'two': 'two\\'s two\\'\\'s two', 'three': ''}
            """;
        String expected = """
            {
              "one": "one one",
              "two": "two\\u0027s two\\u0027\\u0027s two",
              "three": ""
            }""";
        String formattedJsonString3 = JSONStringTools.prettyPrintJson(uglyJsonString3);
        assertEquals(expected, formattedJsonString3);
    }

    @Test
    public void shouldPrettyPrintJsonOutputError() {
        String invalidJsonString = "{\"one\":\"AAA\",\"two\":[\"BBB\",\"CCC\"],\"three\":{\"four\":\"DDD\",\"five\":[\"EEE\",\"FFF\"]}";
        String formattedJsonString = JSONStringTools.prettyPrintJson(invalidJsonString);
        assertEquals("Error: java.io.EOFException: End of input at line 1 column 77 path $.three", formattedJsonString);
    }

    @Test
    public void shouldPrettyPrintJsonOutputBlank() {
        String blankJsonString = "";
        String formattedJsonString = JSONStringTools.prettyPrintJson(blankJsonString);
        assertEquals("", formattedJsonString);
    }

    @Test
    public void shouldParseJsonError() {
        String error = "Error: com.google.gson.stream.MalformedJsonException: Unterminated object at line 1 column 33 path $.two";
        int[] result = JSONStringTools.parseError(error);
        assert result != null;
        assertEquals(1, result[0]);
        assertEquals(33, result[1]);
    }

    @Test
    public void shouldParseJsonError2() {
        String error = "Error: com.google.gson.stream.MalformedJsonException: Unterminated object at line 1 column path $.two";
        int[] result = JSONStringTools.parseError(error);
        assert result == null;
    }

    @Test
    public void shouldNotParseJsonError() {
        String error = "some other error at line 1 column 33 path $.two";
        int[] result = JSONStringTools.parseError(error);
        assert result == null;
    }
}
