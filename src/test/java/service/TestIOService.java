package service;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static service.MockFactory.getDirMock;
import static service.MockFactory.getFileMock;


public class TestIOService {
    @TempDir
    static File tempDir;
    static File emptyDir, dir1, dir2, dir3, dir4;
    static File file1, file2, file3, file4, file5, file6;
    static Comparator<File> mockFileComparator = (o1, o2) -> o1.getName().compareTo(o2.getName());
    
    
    @BeforeAll
    static void initMocks() {
        emptyDir = getDirMock();
        
        file1 = getFileMock("text.txt");
        file2 = getFileMock("binary.bin");
        file3 = getFileMock("image.png");
        file4 = getFileMock("data.txt.bin");
        file5 = getFileMock("readme.txt");
        file6 = getFileMock("no_extension");
        
        dir1 = getDirMock(file1, file2, file3, file4, file5, file6);
        dir2 = getDirMock(file1, file5, dir1);
        dir3 = getDirMock(dir1, dir2, emptyDir);
        dir4 = getDirMock(emptyDir, file4, dir3);
    }
    
    
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
        
        int[] actual;
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
        
        int[] actual;
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
    
    
    @Test
    void testGetFilesByExtensionFile1() {
        assertThrows(IllegalArgumentException.class, () -> IOService.getFilesByExtension(file1, "txt", false));
    }
    
    
    @Test
    void testGetFilesByExtensionDir1() {
        List<File> files1 = IOService.getFilesByExtension(dir1, "txt", false);
        List<File> files2 = IOService.getFilesByExtension(dir1, "bin", false);
        List<File> files3 = IOService.getFilesByExtension(dir1, "", false);
        List<File> files1Subdirs = IOService.getFilesByExtension(dir1, "txt", true);
        List<File> files2Subdirs = IOService.getFilesByExtension(dir1, "bin", true);
        
        files1.sort(mockFileComparator);
        files2.sort(mockFileComparator);
        files1Subdirs.sort(mockFileComparator);
        files2Subdirs.sort(mockFileComparator);
        
        assertAll(
                () -> assertEquals(Arrays.asList(file5, file1), files1),
                () -> assertEquals(Arrays.asList(file2, file4), files2),
                () -> assertEquals(Collections.singletonList(file6), files3),
                () -> assertEquals(files1, files1Subdirs),
                () -> assertEquals(files2, files2Subdirs)
        );
    }
    
    
    @Test
    void testGetFilesByExtensionDir4WithoutSubdirs() {
        List<File> files1 = IOService.getFilesByExtension(dir4, "png", false);
        List<File> files2 = IOService.getFilesByExtension(dir4, "bin", false);
        
        assertAll(
                () -> assertEquals(Collections.emptyList(), files1),
                () -> assertEquals(Collections.singletonList(file4), files2)
        );
    }
    
    
    @Test
    void testGetFilesByExtensionDir4WithSubdirs() {
        List<File> files1 = IOService.getFilesByExtension(dir4, "png", true);
        List<File> files2 = IOService.getFilesByExtension(dir4, "bin", true);
        List<File> files3 = IOService.getFilesByExtension(dir4, "", true);
        
        files2.sort(mockFileComparator);
        
        assertAll(
                () -> assertEquals(Arrays.asList(file3, file3), files1),
                () -> assertEquals(Arrays.asList(file2, file2, file4, file4, file4), files2),
                () -> assertEquals(Arrays.asList(file6, file6), files3)
        );
    }
}
