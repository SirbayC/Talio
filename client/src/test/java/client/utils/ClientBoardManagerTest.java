package client.utils;

import commons.entities.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientBoardManagerTest {

    @Mock
    private File mockFile;
    @Mock
    private ClientBoardManager mockClientBoardManager;
    private ClientBoardManager realClientBoardManager;
    private Map<String, List<Long>> map;
    private File actualTestFile;

    @BeforeEach
    void setup() throws IOException {
        actualTestFile = File.createTempFile("test", "txt");

        lenient().doCallRealMethod().when(mockClientBoardManager).loadContent(any(), anyString());
        lenient().doCallRealMethod().when(mockClientBoardManager).getBoardIDs();

        map = new HashMap<>();
        map.put("testHost", List.of(1L,2L));

        realClientBoardManager = new ClientBoardManager();
        realClientBoardManager.loadContent(actualTestFile, "host");
    }

    @Test
    void write() {
        assertTrue(realClientBoardManager.write(new Board().setId(1L)));
        assertEquals(1L, realClientBoardManager.getBoardIDs().get("host").get(0));
        assertEquals(List.of(1L),
            realClientBoardManager.getClientBoardList(List.of(new Board().setId(1L), new Board().setId(2L))).stream().map(Board::getId).toList());
    }

    @Test
    void getClientBoardList() {
        realClientBoardManager.write(new Board().setId(1L));
        assertEquals(List.of(1L),
            realClientBoardManager.getClientBoardList(List.of(new Board().setId(1L), new Board().setId(2L))).stream().map(Board::getId).toList());
    }

    @Test
    void remove(){
        realClientBoardManager.write(new Board().setId(1L));
        assertTrue(realClientBoardManager.remove(new Board().setId(1)));
        assertEquals(0, realClientBoardManager.getBoardIDs().get("host").size());
    }

    @Test
    void getFileContent() throws ClassNotFoundException, IOException {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(actualTestFile))) {
            out.writeObject(map);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        doCallRealMethod().when(mockClientBoardManager).getFileContent(any());
        assertEquals(map, mockClientBoardManager.getFileContent(actualTestFile));
    }

    @Test
    void loadContentNewFile() throws IOException {
        doReturn(true).when(mockFile).createNewFile();
        mockClientBoardManager.loadContent(mockFile, "testHost");
        assertTrue(mockClientBoardManager.getBoardIDs().containsKey("testHost"));
    }

    @Test()
    void loadContentExistingFile() throws IOException, ClassNotFoundException {
        doReturn(false).when(mockFile).createNewFile();
        doReturn(map).when(mockClientBoardManager).getFileContent(any());
        mockClientBoardManager.loadContent(mockFile, "testHost");
        assertTrue(mockClientBoardManager.getBoardIDs().containsKey("testHost"));
        assertEquals(List.of(1L,2L), mockClientBoardManager.getBoardIDs().values().stream().flatMap(List::stream).toList());
    }

    @Test()
    void loadContentExistingEmptyFile() throws IOException, ClassNotFoundException {
        doReturn(false).when(mockFile).createNewFile();
        doThrow(EOFException.class).when(mockClientBoardManager).getFileContent(any());
        mockClientBoardManager.loadContent(mockFile, "testHost");
        assertEquals(1, mockClientBoardManager.getBoardIDs().keySet().size());
        assertTrue(mockClientBoardManager.getBoardIDs().containsKey("testHost"));
        assertEquals(0, mockClientBoardManager.getBoardIDs().get("testHost").size());
    }

    @Test()
    void invalidFileOrClass() throws IOException, ClassNotFoundException {
        doReturn(false).when(mockFile).createNewFile();
        doThrow(IOException.class).when(mockClientBoardManager).getFileContent(any());
        assertThrows(RuntimeException.class, () -> {
            mockClientBoardManager.loadContent(mockFile, "testHost");
        });
        doThrow(ClassNotFoundException.class).when(mockClientBoardManager).getFileContent(any());
        assertThrows(RuntimeException.class, () -> {
            mockClientBoardManager.loadContent(mockFile, "testHost");
        });
    }


}