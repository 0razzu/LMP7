package service.json_deserializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import model.Flat;
import model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FlatJsonDeserializer extends StdDeserializer<Flat> {
    protected FlatJsonDeserializer() {
        super(Flat.class);
    }
    
    
    @Override
    public Flat deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonToken token = jsonParser.nextToken();
        
        int number = -1;
        double area = -1;
        List<Person> owners = new ArrayList<>();
        
        while (token != JsonToken.END_OBJECT) {
            String currName = jsonParser.currentName();
            
            if ("number".equals(currName)) {
                token = jsonParser.nextToken();
                number = jsonParser.getIntValue();
            }
            
            else if ("area".equals(currName)) {
                token = jsonParser.nextToken();
                area = jsonParser.getDoubleValue();
            }
            
            else if ("owners".equals(currName)) {
                jsonParser.nextToken();
                token = jsonParser.nextToken();
                
                ObjectMapper objectMapper = new ObjectMapper();
                
                while (token != JsonToken.END_ARRAY) {
                    owners.add(objectMapper.readValue(jsonParser, Person.class));
                    
                    token = jsonParser.nextToken();
                }
            }
            
            token = jsonParser.nextToken();
        }
        
        return new Flat(number, area, owners);
    }
}
