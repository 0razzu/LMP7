package service.json_deserializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import model.Person;

import java.io.IOException;
import java.util.Date;


public class PersonJsonDeserializer extends StdDeserializer<Person> {
    protected PersonJsonDeserializer() {
        super(Person.class);
    }
    
    
    @Override
    public Person deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonToken token = jsonParser.nextToken();
        
        String fullName = "";
        long dateOfBirth = 0;
        
        while (token != JsonToken.END_OBJECT) {
            String currName = jsonParser.currentName();
            
            if ("fullName".equals(currName)) {
                token = jsonParser.nextToken();
                fullName = jsonParser.getText();
            }
            
            else if ("dateOfBirth".equals(currName)) {
                token = jsonParser.nextToken();
                dateOfBirth = jsonParser.getLongValue();
            }
            
            token = jsonParser.nextToken();
        }
        
        Person person = new Person();
        String[] names = fullName.split(" ");
        
        person.setFirstName(names[0]);
        
        if (names.length == 2)
            person.setLastName(names[1]);
        
        else {
            person.setPatronymicName(names[1]);
            person.setLastName(names[2]);
        }
        
        person.setDateOfBirth(new Date(dateOfBirth));
        
        return person;
    }
}
