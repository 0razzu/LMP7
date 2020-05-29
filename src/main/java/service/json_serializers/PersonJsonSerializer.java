package service.json_serializers;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import model.Person;

import java.io.IOException;


public class PersonJsonSerializer extends StdSerializer<Person> {
    protected PersonJsonSerializer() {
        super(Person.class);
    }
    
    
    @Override
    public void serialize(Person person, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        
        String patronymicName = person.getPatronymicName();
        jsonGenerator.writeStringField("fullName",
                String.format("%s%s %s",
                        person.getFirstName(),
                        patronymicName == null? "" : " " + patronymicName,
                        person.getLastName()));
        
        jsonGenerator.writeNumberField("dateOfBirth", person.getDateOfBirth().getTime());
        
        jsonGenerator.writeEndObject();
    }
}
