package service;


import error.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class TestIOService {
    @TempDir
    File tempDir;
    
    
    @Test
    void testInputOutputBinary1() throws IOException {
        int[] array = new int[]{-1, 3, -2147483648, 20, 141, 1, 2};
        int[] expected = new int[]{-1, 3, -2147483648, 20, 141};
        
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            IOService.writeIntArrayToBinaryStream(array, out, 6);
            
            try (ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray())) {
                int[] actual = IOService.readIntArrayFromBinaryStream(in, 5);
                
                assertArrayEquals(expected, actual);
            }
        }
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
        try (ByteArrayInputStream in = new ByteArrayInputStream(new byte[5]);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            assertAll(
                    () -> assertThrows(IOException.class,
                            () -> IOService.readIntArrayFromBinaryStream(in, 2)),
                    () -> assertThrows(NegativeArraySizeException.class,
                            () -> IOService.writeIntArrayToBinaryStream(new int[]{1, 2}, out, -1)),
                    () -> assertThrows(NegativeArraySizeException.class,
                            () -> IOService.readIntArrayFromBinaryStream(in, -2))
            );
        }
    }
    
    
    @Test
    void testInputOutputChar1() throws IOException {
        int[] array = new int[]{-1, 3, -2147483648, 20, 141, 1, 2};
        int[] expected = new int[]{-1, 3, -2147483648, 20, 141};
        
        try (StringWriter out = new StringWriter()) {
            IOService.writeIntArrayToCharStream(array, out, 6);
            String str = out.toString();
            
            try (StringReader in = new StringReader(str)) {
                int[] actual = IOService.readIntArrayFromCharStream(in, 5);
                
                assertAll(
                        () -> assertArrayEquals(expected, actual),
                        () -> assertEquals("-1 3 -2147483648 20 141 1 ", str)
                );
            }
        }
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
        try (StringReader in1 = new StringReader("");
             StringReader in2 = new StringReader("1 2 3 4 ");
             StringWriter out = new StringWriter()) {
            assertAll(
                    () -> assertThrows(IOException.class,
                            () -> IOService.readIntArrayFromCharStream(in1, 2)),
                    () -> assertThrows(NumberFormatException.class,
                            () -> IOService.readIntArrayFromCharStream(in2, 5)),
                    () -> assertThrows(NegativeArraySizeException.class,
                            () -> IOService.writeIntArrayToCharStream(new int[]{1, 2, 3}, out, -1)),
                    () -> assertThrows(NegativeArraySizeException.class,
                            () -> IOService.readIntArrayFromCharStream(in2, -2))
            );
        }
    }
    
    
    @Test
    void testReadIntArrayFromRandomAccessFile() throws IOException {
        File file = new File(tempDir, "test.bin");
        
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            for (int i = 0; i < 10; i++)
                raf.writeInt(i);
            
            assertAll(
                    () -> assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
                            IOService.readIntArrayFromRandomAccessFile(raf, 0, 10)),
                    () -> assertArrayEquals(new int[]{1, 2, 3},
                            IOService.readIntArrayFromRandomAccessFile(raf, 4, 3)),
                    () -> assertThrows(IllegalArgumentException.class,
                            () -> IOService.readIntArrayFromRandomAccessFile(raf, -1, 2)),
                    () -> assertThrows(NegativeArraySizeException.class,
                            () -> IOService.readIntArrayFromRandomAccessFile(raf, 1, -2))
            );
        }
    }
}
