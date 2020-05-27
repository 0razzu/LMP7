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
        int[] actual = new int[]{-1, 3, -2147483648, 20, 141};
        int[] expected = new int[5];
        
        IOService.writeIntArrayToBinaryStream(array, out, 6);
        IOService.readIntArrayFromBinaryStream(expected, new ByteArrayInputStream(out.toByteArray()), 5);
        
        assertAll(
                () -> assertArrayEquals(actual, expected),
                () -> assertThrows(ArrayIndexOutOfBoundsException.class,
                        () -> IOService.readIntArrayFromBinaryStream(expected, new ByteArrayInputStream(out.toByteArray()), 7)),
                () -> assertThrows(IOException.class,
                        () -> IOService.readIntArrayFromBinaryStream(expected, new ByteArrayInputStream(new byte[5]), 2))
        );
    }
    
    
    @Test
    void testInputOutputBinary2() throws IOException {
        File file = new File(tempDir, "test.bin");
    
        int[] actual = new int[]{1, 2, -3, 2000000};
        int[] expected = new int[4];
        
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            IOService.writeIntArrayToBinaryStream(actual, out, 4);
        }
        
        try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
            IOService.readIntArrayFromBinaryStream(expected, in, 4);
        }
        
        assertArrayEquals(actual, expected);
    }
    
    
    @Test
    void testInputOutputChar1() throws IOException {
        StringWriter out = new StringWriter();
        
        int[] array = new int[]{-1, 3, -2147483648, 20, 141, 1, 2};
        int[] actual = new int[]{-1, 3, -2147483648, 20, 141};
        int[] expected = new int[5];
        
        IOService.writeIntArrayToCharStream(array, out, 6);
        IOService.readIntArrayFromCharStream(expected, new StringReader(out.toString()), 5);
        
        assertAll(
                () -> assertArrayEquals(actual, expected),
                () -> assertThrows(ArrayIndexOutOfBoundsException.class,
                        () -> IOService.readIntArrayFromCharStream(expected, new StringReader(out.toString()), 7))
        );
    }
    
    
    @Test
    void testInputOutputChar2() throws IOException {
        File file = new File(tempDir, "test.txt");
        
        int[] actual = new int[]{1, 2, -3, 2000000};
        int[] expected = new int[4];
        
        try (Writer out = new BufferedWriter(new FileWriter(file))) {
            IOService.writeIntArrayToCharStream(actual, out, 4);
        }
        
        try (Reader in = new BufferedReader(new FileReader(file))) {
            IOService.readIntArrayFromCharStream(expected, in, 4);
        }
        
        assertArrayEquals(actual, expected);
    }
}
