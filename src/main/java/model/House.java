package model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import error.ErrorMessage;
import service.json_deserializer.HouseJsonDeserializer;
import service.json_serializer.HouseJsonSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@JsonSerialize(using = HouseJsonSerializer.class)
@JsonDeserialize(using = HouseJsonDeserializer.class)
public class House implements Serializable {
    private static final long serialVersionUID = 3L;
    private String cadastralNumber;
    private String address;
    private Person head;
    private List<Flat> flats;
    
    
    public House() {
    }
    
    
    public House(String cadastralNumber, String address, Person head, List<Flat> flats) {
        setCadastralNumber(cadastralNumber);
        setAddress(address);
        setHead(head);
        setFlats(flats);
    }
    
    
    public void setCadastralNumber(String cadastralNumber) {
        this.cadastralNumber = cadastralNumber;
    }
    
    
    public void setAddress(String address) {
        if (address == null || address.length() == 0)
            throw new IllegalArgumentException(ErrorMessage.NULL_ADDRESS);
        
        this.address = address;
    }
    
    
    public void setHead(Person head) {
        this.head = head;
    }
    
    
    public void setFlats(List<Flat> flats) {
        if (flats == null)
            this.flats = new ArrayList<>();
        
        else
            this.flats = flats;
    }
    
    
    public String getCadastralNumber() {
        return cadastralNumber;
    }
    
    
    public String getAddress() {
        return address;
    }
    
    
    public Person getHead() {
        return head;
    }
    
    
    public List<Flat> getFlats() {
        return flats;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        House house = (House) o;
        return Objects.equals(cadastralNumber, house.cadastralNumber) &&
                Objects.equals(address, house.address) &&
                Objects.equals(head, house.head) &&
                Objects.equals(flats, house.flats);
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(cadastralNumber, address, head, flats);
    }
}
