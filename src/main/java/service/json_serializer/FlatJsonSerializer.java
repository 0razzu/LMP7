package service.json_serializer;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import model.Flat;
import model.Person;

import java.io.IOException;


public class FlatJsonSerializer extends StdSerializer<Flat> {
    protected FlatJsonSerializer() {
        super(Flat.class);
    }
    
    
    @Override
    public void serialize(Flat flat, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        
        jsonGenerator.writeNumberField("number", flat.getNumber());
        jsonGenerator.writeNumberField("area", flat.getArea());
        
        jsonGenerator.writeArrayFieldStart("owners");
        for (Person person: flat.getOwners())
            jsonGenerator.writeObject(person);
        jsonGenerator.writeEndArray();
        
        jsonGenerator.writeEndObject();
    }
}
