package service;


import model.Flat;
import model.House;
import model.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestHouseSerializer {
    @TempDir
    static File tempDir;
    static Person person1, person2, person3, person4, person5;
    static Flat flat1, flat2, flat3, flat4, flat5;
    static House house1, house2;
    
    
    @BeforeAll
    static void initModels() {
        Calendar calendar = Calendar.getInstance();
        
        calendar.set(1980, Calendar.AUGUST, 1);
        person1 = new Person("Иван", "Иванович", "Петров", calendar.getTime());
        calendar.set(2001, Calendar.JANUARY, 8);
        person2 = new Person("Анастасия", "Сергеевна", "Иванова", calendar.getTime());
        calendar.set(1935, Calendar.MARCH, 31);
        person3 = new Person("Агафья", "Фёдоровна", "Воронцова", calendar.getTime());
        calendar.set(1972, Calendar.NOVEMBER, 15);
        person4 = new Person("Antoine", "Roux", calendar.getTime());
        calendar.set(1996, Calendar.FEBRUARY, 29);
        person5 = new Person("広明", "小山", calendar.getTime());
        
        flat1 = new Flat(1, 60.25, Arrays.asList(person1, person2));
        flat2 = new Flat(2, 40, Collections.singletonList(person3));
        flat3 = new Flat(10, 100, Arrays.asList(person1, person2, person3));
        flat4 = new Flat(285, 99.85, Collections.singletonList(person4));
        flat5 = new Flat(286, 50.5, Arrays.asList(person2, person3));
        
        house1 = new House("55:36:050208:11906", "ул. Кого-то там, 13", person3, Arrays.asList(flat1, flat2, flat3));
        house2 = new House(null, "пр. Лиговский, 232", null, Arrays.asList(flat4, flat5));
    }
    
    
    @Test
    void testSerializeDeserializeHouse1() throws IOException, ClassNotFoundException {
        byte[] array;
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            HouseSerializer.serializeHouseToObjectStream(house1, oos);
            array = baos.toByteArray();
        }
    
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(array))) {
            House deserializedHouse = HouseSerializer.deserializeHouseFromObjectStream(ois);
            
            assertEquals(house1, deserializedHouse);
        }
    }
    
    
    @Test
    void testSerializeDeserializeHouse2() throws IOException, ClassNotFoundException {
        File file = new File(tempDir, "test.bin");
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            HouseSerializer.serializeHouseToObjectStream(house2, oos);
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            House deserializedHouse = HouseSerializer.deserializeHouseFromObjectStream(ois);
            
            assertEquals(house2, deserializedHouse);
        }
    }
}
