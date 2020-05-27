package service;


import error.ErrorMessage;
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
        
        assertArrayEquals(expected, actual);
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
    void testInputOutputBinaryException() throws IOException {
        assertThrows(IOException.class,
                () -> IOService.readIntArrayFromBinaryStream(new ByteArrayInputStream(new byte[5]), 2));
        
        try {
            IOService.writeIntArrayToBinaryStream(new int[]{1, 2, 3}, new ByteArrayOutputStream(), -1);
            fail("Written an array of negative size");
        } catch (IllegalArgumentException e) {
            assertEquals(ErrorMessage.NEGATIVE_SIZE, e.getMessage());
        }
        
        try {
            IOService.readIntArrayFromBinaryStream(new ByteArrayInputStream(new byte[3]), -2);
            fail("Read an array of negative size");
        } catch (IllegalArgumentException e) {
            assertEquals(ErrorMessage.NEGATIVE_SIZE, e.getMessage());
        }
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
                () -> assertEquals("-1 3 -2147483648 20 141 1 ", str)
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
    
    
    @Test
    void testInputOutputCharException() throws IOException {
        assertThrows(IOException.class,
                () -> IOService.readIntArrayFromCharStream(new StringReader(""), 2));
        assertThrows(NumberFormatException.class,
                () -> IOService.readIntArrayFromCharStream(new StringReader("1 2 3 4 "), 5));
        
        try {
            IOService.writeIntArrayToCharStream(new int[]{1, 2, 3}, new StringWriter(), -1);
            fail("Written an array of negative size");
        } catch (IllegalArgumentException e) {
            assertEquals(ErrorMessage.NEGATIVE_SIZE, e.getMessage());
        }
        
        try {
            IOService.readIntArrayFromCharStream(new StringReader("1 2 3 "), -2);
            fail("Read an array of negative size");
        } catch (IllegalArgumentException e) {
            assertEquals(ErrorMessage.NEGATIVE_SIZE, e.getMessage());
        }
    }
}
