package service.json_deserializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import model.Flat;
import model.House;
import model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HouseJsonDeserializer extends StdDeserializer<House> {
    protected HouseJsonDeserializer() {
        super(House.class);
    }
    
    
    @Override
    public House deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonToken token = jsonParser.nextToken();
        
        String cadastralNumber = null;
        String address = null;
        Person head = null;
        List<Flat> flats = new ArrayList<>();
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        while (token != JsonToken.END_OBJECT) {
            String currName = jsonParser.currentName();
            
            if ("cadastralNumber".equals(currName)) {
                token = jsonParser.nextToken();
                
                if (token != JsonToken.VALUE_NULL)
                    cadastralNumber = jsonParser.getText();
            }
            
            else if ("address".equals(currName)) {
                token = jsonParser.nextToken();
                
                if (token != JsonToken.VALUE_NULL)
                    address = jsonParser.getText();
            }
            
            else if ("head".equals(currName)) {
                token = jsonParser.nextToken();
                
                if (token != JsonToken.VALUE_NULL)
                    head = objectMapper.readValue(jsonParser, Person.class);
            }
            
            else if ("flats".equals(currName)) {
                jsonParser.nextToken();
                token = jsonParser.nextToken();
                
                while (token != JsonToken.END_ARRAY) {
                    flats.add(objectMapper.readValue(jsonParser, Flat.class));
                    token = jsonParser.nextToken();
                }
            }
            
            token = jsonParser.nextToken();
        }
        
        return new House(cadastralNumber, address, head, flats);
    }
}
