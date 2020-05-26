package service;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


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
}
