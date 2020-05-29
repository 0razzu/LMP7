package service.json_serializer;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import model.Flat;
import model.House;
import model.Person;

import java.io.IOException;


public class HouseJsonSerializer extends StdSerializer<House> {
    protected HouseJsonSerializer() {
        super(House.class);
    }
    
    
    @Override
    public void serialize(House house, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        
        String cadastralNumber = house.getCadastralNumber();
        if (cadastralNumber != null)
            jsonGenerator.writeStringField("cadastralNumber", cadastralNumber);
        
        jsonGenerator.writeStringField("address", house.getAddress());
    
        Person head = house.getHead();
        if (head != null)
            jsonGenerator.writeObjectField("head", house.getHead());
        
        jsonGenerator.writeArrayFieldStart("flats");
        for (Flat flat: house.getFlats())
            jsonGenerator.writeObject(flat);
        jsonGenerator.writeEndArray();
        
        jsonGenerator.writeEndObject();
    }
}
