package service;


import java.io.*;


public class IOService {
    public static void writeIntArrayToBinaryStream(int[] array, OutputStream stream, int size) throws IOException {
        for (int i = 0; i < size; i++) {
            stream.write(array[i] >> 24);
            stream.write(array[i] >> 16);
            stream.write(array[i] >> 8);
            stream.write(array[i]);
        }
    }
    
    
    public static void readIntArrayFromBinaryStream(int[] array, InputStream stream, int size) throws IOException {
        byte[] value = new byte[4];
        
        for (int i = 0; i < size; i++) {
            if (stream.read(value) < 4)
                throw new IOException();
            
            array[i] = ((value[0] & 0xff) << 24) +
                    ((value[1] & 0xff) << 16) +
                    ((value[2] & 0xff) << 8) +
                    (value[3] & 0xff);
        }
    }
    
    
    public static void writeIntArrayToCharStream(int[] array, Writer stream, int size) throws IOException {
        for (int i = 0; i < size; i++) {
            stream.write(Integer.toString(array[i]));
            stream.write(" ");
        }
    }
    
    
    public static void readIntArrayFromCharStream(int[] array, Reader stream, int size) throws IOException {
        char[] chars = new char[12 * size];
        
        if (stream.read(chars) == -1)
            throw new IOException();
        
        String[] strings = new String(chars).split(" ");
    
        for (int i = 0; i < size; i++)
            array[i] = Integer.parseInt(strings[i]);
    }
}
