package service;


import model.Flat;
import model.House;
import model.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


public class TestHouseSerializer {
    @TempDir
    static File tempDir;
    static Person person1, person2, person3, person4;
    static Flat flat1, flat2, flat3, flat4, flat5, flat6;
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
        
        flat1 = new Flat(1, 60.25, Arrays.asList(person1, person2));
        flat2 = new Flat(2, 40, Collections.singletonList(person3));
        flat3 = new Flat(10, 100, Arrays.asList(person1, person2, person3));
        flat4 = new Flat(9, 25, null);
        flat5 = new Flat(285, 99.85, Collections.singletonList(person4));
        flat6 = new Flat(286, 50.5, Arrays.asList(person2, person3));
        
        house1 = new House("55:36:050208:11906", "ул. Кого-то там, 13", person3, Arrays.asList(flat1, flat2, flat3, flat4));
        house2 = new House(null, "пр. Лиговский, 232", null, Arrays.asList(flat5, flat6));
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
    
    
    @Test
    void testSerializeHouseToCsv1() throws IOException {
        HouseSerializer.serializeHouseToCsv(house1, tempDir, StandardCharsets.UTF_8);
        
        File file = new File(tempDir, "house_55.36.050208.11906.csv");
        
        assertTrue(file.exists());
        
        String expected = "Данные о доме;;\n" +
                "Кадастровый номер;55:36:050208:11906;\n" +
                "Адрес;\"ул. Кого-то там, 13\";\n" +
                "Старший по дому;Воронцова Агафья Фёдоровна;\n" +
                ";;\n" +
                "Данные о квартирах;;\n" +
                "№;\"Площадь, кв. м\";Владельцы\n" +
                "1;\"60,25\";\"Петров И. И., Иванова А. С.\"\n" +
                "2;\"40,0\";\"Воронцова А. Ф.\"\n" +
                "10;\"100,0\";\"Петров И. И., Иванова А. С., Воронцова А. Ф.\"\n" +
                "9;\"25,0\";\"\"\n";
        StringBuilder actual = new StringBuilder("");
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            
            while ((line = br.readLine()) != null)
                actual.append(line).append('\n');
        }
        
        assertEquals(expected, actual.toString());
    }
    
    
    @Test
    void testSerializeHouseToCsv2() throws IOException {
        HouseSerializer.serializeHouseToCsv(house2, tempDir, StandardCharsets.UTF_8);
        
        File file = new File(tempDir, "house_no_cadastral_number.csv");
        
        assertTrue(file.exists());
        
        String expected = "Данные о доме;;\n" +
                "Кадастровый номер;;\n" +
                "Адрес;\"пр. Лиговский, 232\";\n" +
                "Старший по дому;;\n" +
                ";;\n" +
                "Данные о квартирах;;\n" +
                "№;\"Площадь, кв. м\";Владельцы\n" +
                "285;\"99,85\";\"Roux A.\"\n" +
                "286;\"50,5\";\"Иванова А. С., Воронцова А. Ф.\"\n";
        StringBuilder actual = new StringBuilder("");
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            
            while ((line = br.readLine()) != null)
                actual.append(line).append('\n');
        }
        
        assertEquals(expected, actual.toString());
    }
}
