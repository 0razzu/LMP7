package service;


import model.House;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class HouseSerializer {
    public static void serializeHouseToObjectStream(House house, ObjectOutputStream stream) throws IOException {
        stream.writeObject(house);
    }
    
    
    public static House deserializeHouseFromObjectStream(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        return (House) stream.readObject();
    }
}
