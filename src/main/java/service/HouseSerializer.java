package service;


import error.ErrorMessage;
import model.Flat;
import model.House;
import model.Person;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;


public class HouseSerializer {
    public static void serializeHouseToObjectStream(House house, ObjectOutputStream stream) throws IOException {
        stream.writeObject(house);
    }
    
    
    public static House deserializeHouseFromObjectStream(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        return (House) stream.readObject();
    }
    
    
    public static void serializeHouseToCsv(House house, File dir, Charset charset) throws IOException {
        if (!dir.isDirectory())
            throw new IllegalArgumentException(ErrorMessage.NOT_A_DIRECTORY);
        
        String cadastralNumber = house.getCadastralNumber();
        String fileName = String.format("house_%s.csv",
                cadastralNumber == null?
                        "no_cadastral_number" : cadastralNumber.replace(':', '.'));
        
        try (BufferedWriter out =
                     new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(dir, fileName)), charset))) {
            out.write("Данные о доме;;");
            out.newLine();
            out.write(String.format("Кадастровый номер;%s;", cadastralNumber == null? "" : cadastralNumber));
            out.newLine();
            out.write(String.format("Адрес;\"%s\";", house.getAddress()));
            out.newLine();
            out.write("Старший по дому;");
            
            Person head = house.getHead();
            if (head != null) {
                out.write(head.getLastName());
                out.write(" ");
                out.write(head.getFirstName());
                
                String patronymicName = head.getPatronymicName();
                if (patronymicName != null) {
                    out.write(' ');
                    out.write(patronymicName);
                }
            }
            
            out.write(";");
            out.newLine();
            out.write(";;");
            out.newLine();
            out.write("Данные о квартирах;;");
            out.newLine();
            out.write("№;\"Площадь, кв. м\";Владельцы");
            out.newLine();
            
            for (Flat flat: house.getFlats()) {
                out.write(String.format("%d;\"%s\";\"", flat.getNumber(),
                        Double.toString(flat.getArea()).replace('.', ',')));
                
                List<Person> owners = flat.getOwners();
                for (int i = 0; i < owners.size(); i++) {
                    Person owner = owners.get(i);
                    
                    out.write(String.format("%s %c.", owner.getLastName(), owner.getFirstName().charAt(0)));
                    
                    String patronymicName = owner.getPatronymicName();
                    if (patronymicName != null) {
                        out.write(String.format(" %c.", patronymicName.charAt(0)));
                    }
                    
                    if (i < owners.size() - 1)
                        out.write(", ");
                }
                
                out.write('"');
                out.newLine();
            }
        }
    }
}
