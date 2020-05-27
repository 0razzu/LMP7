package service;


import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class MockFactory {
    static public File getFileMock(String fileName) {
        File file = mock(File.class);
        when(file.isFile()).thenReturn(true);
        when(file.isDirectory()).thenReturn(false);
        when(file.getName()).thenReturn(fileName);
        
        return file;
    }
    
    
    static public File getDirMock(File... files) {
        File dir = mock(File.class);
        when(dir.isFile()).thenReturn(false);
        when(dir.isDirectory()).thenReturn(true);
        when(dir.listFiles()).thenReturn(files);
        
        return dir;
    }
}
