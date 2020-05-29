package service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonStringService {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    
    public static boolean compareJsonStrings(String jsonStr1, String jsonStr2) throws JsonProcessingException {
        return objectMapper.readTree(jsonStr1).equals(objectMapper.readTree(jsonStr2));
    }
}
