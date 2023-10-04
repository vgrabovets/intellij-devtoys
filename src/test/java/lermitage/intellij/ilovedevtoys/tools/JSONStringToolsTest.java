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
            {'one': 'one one', 'two': 'two's two's two', 'three': ''}
            """;
        String expected = """
            {
              "one": "one one",
              "two": "two's two's two",
              "three": ""
            }""";
        String formattedJsonString3 = JSONStringTools.prettyPrintJson(uglyJsonString3);
        assertEquals(expected, formattedJsonString3);
    }

    @Test
    public void shouldPrettyPrintComplexJson() {
        String uglyString = """
            [{website:'domain.com',employees_number:0,company_name:'Company LTD',source_id:'15815362',shorthand_name:'company-ltd',canonical_shorthand_name:'company-ltd',industry:'Information Services',founded:'',description:'Tech industry has increased competition between businesses. To stand out in today's crowded landscape, you need to have a clear edge over others. Access to authentic, reliable data has never been more critical than it is now.  \s

            At our company's, we give'}]
            """;

        String formattedJsonString = JSONStringTools.prettyPrintJson(uglyString);
        String expectedPrettyJson = """
            [
              {
                "website": "domain.com",
                "employees_number": 0.0,
                "company_name": "Company LTD",
                "source_id": "15815362",
                "shorthand_name": "company-ltd",
                "canonical_shorthand_name": "company-ltd",
                "industry": "Information Services",
                "founded": "",
                "description": "Tech industry has increased competition between businesses. To stand out in today's crowded landscape, you need to have a clear edge over others. Access to authentic, reliable data has never been more critical than it is now.  \s

            At our company's, we give"
              }
            ]""";
        assertEquals(expectedPrettyJson, formattedJsonString);
    }

    @Test
    public void shouldPrettyPrintTextWithQuote() {
        String uglyString = "{meta:'H&M is your shopping destination for fashion, home, beauty, kids' clothes and more.'}";
        String formattedJsonString = JSONStringTools.prettyPrintJson(uglyString);
        String expectedPrettyJson = """
            {
              "meta": "H&M is your shopping destination for fashion, home, beauty, kids' clothes and more."
            }""";
        assertEquals(expectedPrettyJson, formattedJsonString);
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
