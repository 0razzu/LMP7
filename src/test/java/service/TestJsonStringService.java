package service;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class TestJsonStringService {
    @Test
    void testCompareJsonStrings() {
        String jsonString1 = "{\"color\":\"red\", \"weight\":123}";
        String jsonString2 = "{\"weight\":123, \"color\":\"red\"}";
        String jsonString3 = "{\"weight\":124, \"color\":\"red\"}";
        String jsonString4 = "{\"color\":\"pink\", \"things\":[\"something\", \"something else\"], \"value\":1.25}";
        String jsonString5 = "{\"color\":\"pink\", \"things\":[\"something else\", \"something\"], \"value\":1.25}";
        String jsonString6 = ("{" +
                "'str1': ''," +
                "'str2': 'what'," +
                "'obj': {" +
                "         'data': [1, 2, 3]," +
                "         'info': 'important'," +
                "         'nestedObj': {" +
                "                        'something': 1," +
                "                        'array': ['a', 'b', 'c']" +
                "                      }" +
                "       }" +
                "}").replace('\'', '\"');
        String jsonString7 = ("{" +
                "'str1': ''," +
                "'str2': 'what'," +
                "'obj': {" +
                "         'info': 'important'," +
                "         'nestedObj': {" +
                "                        'array': ['a', 'b', 'c']," +
                "                        'something': 1" +
                "                      }," +
                "         'data': [1, 2, 3]" +
                "       }" +
                "}").replace('\'', '\"');
        String jsonString8 = ("{" +
                "'str1': ''," +
                "'str2': 'what'," +
                "'obj': {" +
                "         'data': [1, 2, 3]," +
                "         'info': 'important'," +
                "         'nestedObj': {" +
                "                        'something': 1," +
                "                        'array': ['a', 'b', 'z']" +
                "                      }" +
                "       }" +
                "}").replace('\'', '\"');
        
        assertAll(
                () -> assertTrue(JsonStringService.compareJsonStrings(jsonString1, jsonString2)),
                () -> assertFalse(JsonStringService.compareJsonStrings(jsonString1, jsonString3)),
                () -> assertFalse(JsonStringService.compareJsonStrings(jsonString3, jsonString4)),
                () -> assertFalse(JsonStringService.compareJsonStrings(jsonString4, jsonString5)),
                () -> assertFalse(JsonStringService.compareJsonStrings(jsonString4, jsonString6)),
                () -> assertTrue(JsonStringService.compareJsonStrings(jsonString6, jsonString7)),
                () -> assertFalse(JsonStringService.compareJsonStrings(jsonString6, jsonString8))
        );
    }
}
