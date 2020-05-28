package model;


import error.ErrorMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class House {
    private String cadastralNumber;
    private String address;
    private Person head;
    private List<Flat> flats;
    
    
    public House(String cadastralNumber, String address, Person head, List<Flat> flats) {
        setCadastralNumber(cadastralNumber);
        setAddress(address);
        setHead(head);
        setFlats(flats);
    }
    
    
    public void setCadastralNumber(String cadastralNumber) {
        if (cadastralNumber == null || cadastralNumber.length() == 0)
            throw new IllegalArgumentException(ErrorMessage.NULL_CADASTRAL_NUMBER);
        
        this.cadastralNumber = cadastralNumber;
    }
    
    
    public void setAddress(String address) {
        if (address == null || address.length() == 0)
            throw new IllegalArgumentException(ErrorMessage.NULL_ADDRESS);
        
        this.address = address;
    }
    
    
    public void setHead(Person head) {
        if (head == null)
            throw new IllegalArgumentException(ErrorMessage.NULL_HEAD);
        
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
        return cadastralNumber.equals(house.cadastralNumber) &&
                address.equals(house.address) &&
                head.equals(house.head) &&
                flats.equals(house.flats);
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(cadastralNumber, address, head, flats);
    }
}
