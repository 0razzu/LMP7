package service;


import error.ErrorMessage;

import java.io.*;


public class IOService {
    private static void checkSize(int size) {
        if (size < 0)
            throw new IllegalArgumentException(ErrorMessage.NEGATIVE_SIZE);
    }
    
    
    public static void writeIntArrayToBinaryStream(int[] array, OutputStream stream, int size) throws IOException {
        checkSize(size);
        
        for (int i = 0; i < size; i++) {
            stream.write(array[i] >> 24);
            stream.write(array[i] >> 16);
            stream.write(array[i] >> 8);
            stream.write(array[i]);
        }
    }
    
    
    public static int[] readIntArrayFromBinaryStream(InputStream stream, int size) throws IOException {
        checkSize(size);
    
        int[] ints = new int[size];
        byte[] value = new byte[4];
        
        for (int i = 0; i < size; i++) {
            if (stream.read(value) < 4)
                throw new IOException();
            
            ints[i] = ((value[0] & 0xff) << 24) +
                    ((value[1] & 0xff) << 16) +
                    ((value[2] & 0xff) << 8) +
                    (value[3] & 0xff);
        }
        
        return ints;
    }
    
    
    public static void writeIntArrayToCharStream(int[] array, Writer stream, int size) throws IOException {
        checkSize(size);
    
        for (int i = 0; i < size; i++) {
            stream.write(Integer.toString(array[i]));
            stream.write(" ");
        }
    }
    
    
    public static int[] readIntArrayFromCharStream(Reader stream, int size) throws IOException {
        checkSize(size);
    
        char[] chars = new char[12 * size];
        
        if (stream.read(chars) == -1)
            throw new IOException();
        
        String[] strings = new String(chars).split(" ");
        
        int[] ints = new int[size];
        
        for (int i = 0; i < size; i++)
            ints[i] = Integer.parseInt(strings[i]);
        
        return ints;
    }
}
