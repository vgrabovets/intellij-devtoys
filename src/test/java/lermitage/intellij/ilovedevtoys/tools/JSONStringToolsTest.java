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
}
