package service;


import error.ErrorMessage;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


public class IOService {
    private static void checkSize(int size) {
        if (size < 0)
            throw new NegativeArraySizeException(Integer.toString(size));
    }
    
    
    /* Филиппов А.В. 20.06.2020 Комментарий не удалять.
     Зачем передавать size? Нужно весь массив записывать же...
    */
    // fixed
    public static void writeIntArrayToBinaryStream(int[] array, OutputStream stream) throws IOException {
        for (int value: array) {
            stream.write(value >> 24);
            stream.write(value >> 16);
            stream.write(value >> 8);
            stream.write(value);
        }
    }
    
    
    /* Филиппов А.В. 20.06.2020 Комментарий не удалять.
     В задании написано "Предполагается, что до чтения массив уже создан". Почему вы передаете не массив, а размер?
     Для работы с байтами есть ByteBuffer, который умеет LowEndian и BigEndian
    */
    // fixed
    public static void readIntArrayFromBinaryStream(int[] array, InputStream stream) throws IOException {
        byte[] value = new byte[4];
        
        for (int i = 0; i < array.length; i++) {
            if (stream.read(value) < 4)
                throw new IOException();
            
            array[i] = ByteBuffer.wrap(value).getInt();
        }
    }
    
    
    public static void writeIntArrayToCharStream(int[] array, Writer stream) throws IOException {
        for (int value: array) {
            stream.write(Integer.toString(value));
            stream.write(" ");
        }
    }
    
    
    /* Филиппов А.В. 20.06.2020 Комментарий не удалять.
     Опять же массив создан и передается в функцию.
    */
    // fixed
    public static void readIntArrayFromCharStream(int[] array, Reader stream) throws IOException {
        char[] chars = new char[12 * array.length];
        
        if (stream.read(chars) == -1)
            throw new IOException();
        
        String[] strings = new String(chars).split(" ");
        
        for (int i = 0; i < array.length; i++)
            array[i] = Integer.parseInt(strings[i]);
    }
    
    
    public static void readIntArrayFromRandomAccessFile(int[] array, RandomAccessFile file, long offset) throws IOException {
        /* Филиппов А.В. 20.06.2020 Комментарий не удалять.
         Бесполезная проверка - seek кинет IOException
        */
        // fixed
        
        file.seek(offset);
        
        for (int i = 0; i < array.length; i++)
            array[i] = file.readInt();
    }
    
    
    public static List<File> getFilesByExtension(File dir, String extension) {
        if (!dir.isDirectory())
            throw new IllegalArgumentException(ErrorMessage.NOT_A_DIRECTORY);
        
        List<File> files = new ArrayList<>();
        
        if (extension != null) {
            if (extension.length() == 0) {
                for (File file: dir.listFiles()) {
                    if (file.isFile()) {
                        String fileName = file.getName();
                        
                        if (fileName.lastIndexOf('.') == -1)
                            files.add(file);
                    }
                }
            }
            else {
                String suffix = "." + extension;
                
                for (File file: dir.listFiles()) {
                    if (file.isFile()) {
                        String fileName = file.getName();
                        
                        if (fileName.endsWith(suffix))
                            files.add(file);
                    }
                }
            }
        }
        
        return files;
    }
    
    
    private static List<File> getFilesByPattern(File dir, Pattern pattern) {
        List<File> files = new ArrayList<>();
        
        for (File file: dir.listFiles()) {
            if (pattern.matcher(file.getName()).matches())
                files.add(file);
            
            if (file.isDirectory())
                files.addAll(getFilesByPattern(file, pattern));
        }
        
        return files;
    }
    
    
    public static List<File> getFilesByRegex(File dir, String regex) {
        if (!dir.isDirectory())
            throw new IllegalArgumentException(ErrorMessage.NOT_A_DIRECTORY);
        
        return regex == null?
                new ArrayList<>() :
                getFilesByPattern(dir, Pattern.compile(regex));
    }
}
