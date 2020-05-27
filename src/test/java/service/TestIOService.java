package service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;


public class TestIOService {
    @TempDir
    File tempDir;
    
    
    @Test
    void testInputOutputBinary1() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        int[] array = new int[]{-1, 3, -2147483648, 20, 141, 1, 2};
        int[] expected = new int[]{-1, 3, -2147483648, 20, 141};
        
        IOService.writeIntArrayToBinaryStream(array, out, 6);
        int[] actual = IOService.readIntArrayFromBinaryStream(new ByteArrayInputStream(out.toByteArray()), 5);
        
        assertAll(
                () -> assertArrayEquals(expected, actual),
                () -> assertThrows(IOException.class,
                        () -> IOService.readIntArrayFromBinaryStream(new ByteArrayInputStream(new byte[5]), 2))
        );
    }
    
    
    @Test
    void testInputOutputBinary2() throws IOException {
        File file = new File(tempDir, "test.bin");
        
        int[] expected = new int[]{1, 2, -3, 2000000};
        
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            IOService.writeIntArrayToBinaryStream(expected, out, 4);
        }
        
        int[] actual = null;
        try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
            actual = IOService.readIntArrayFromBinaryStream(in, 4);
        }
        
        assertArrayEquals(expected, actual);
    }
    
    
    @Test
    void testInputOutputChar1() throws IOException {
        StringWriter out = new StringWriter();
        
        int[] array = new int[]{-1, 3, -2147483648, 20, 141, 1, 2};
        int[] expected = new int[]{-1, 3, -2147483648, 20, 141};
        
        IOService.writeIntArrayToCharStream(array, out, 6);
        String str = out.toString();
        
        int[] actual = IOService.readIntArrayFromCharStream(new StringReader(str), 5);
        
        assertAll(
                () -> assertArrayEquals(expected, actual),
                () -> assertThrows(IOException.class,
                        () -> IOService.readIntArrayFromCharStream(new StringReader(""), 3)),
                () -> assertThrows(NumberFormatException.class,
                        () -> IOService.readIntArrayFromCharStream(new StringReader(str), 7))
        );
    }
    
    
    @Test
    void testInputOutputChar2() throws IOException {
        File file = new File(tempDir, "test.txt");
        
        int[] expected = new int[]{1, 2, -3, 2000000};
        
        try (Writer out = new BufferedWriter(new FileWriter(file))) {
            IOService.writeIntArrayToCharStream(expected, out, 4);
        }
        
        int[] actual = null;
        try (Reader in = new BufferedReader(new FileReader(file))) {
            actual = IOService.readIntArrayFromCharStream(in, 4);
        }
        
        assertArrayEquals(expected, actual);
    }
}
