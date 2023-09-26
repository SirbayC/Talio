package client.utils;

import commons.entities.Board;
import lombok.Getter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientBoardManager {

    @Getter
    private Map<String, List<Long>> boardIDs;
    private File boardLog;
    private String host;

    /**
     * Initializes the manager with the provider file path
     *
     * @param boardLog path
     * @param host     server address
     */
    public void loadContent(File boardLog, String host) {
        this.boardLog = boardLog;
        this.host = host;
        try {
            if(boardLog.createNewFile()) {
                System.out.println("File created: " + boardLog.getName());
                boardIDs = new HashMap<>();
            } else {
                System.out.println("File already exists. Reading IDs:");
                boardIDs = getFileContent(boardLog);
            }
        } catch (EOFException e) {
            System.out.println("File was empty");
            boardIDs = new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(!boardIDs.containsKey(host))
            boardIDs.put(host, new ArrayList<>());
        System.out.println(boardIDs);
    }

    /**
     * reads the content from the provided file
     *
     * @param file file to read from
     * @return map object
     * @throws IOException error in reading
     * @throws ClassNotFoundException error in converting classes
     */
    @SuppressWarnings("unchecked")
    public Map<String, List<Long>> getFileContent(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        Map<String, List<Long>> res = (Map<String, List<Long>>) in.readObject();
        in.close();
        return res;
    }

    /**
     * writes a visited board's id to the log
     *
     * @param board to write
     * @return whether the procedure has been successfully completed
     */
    public boolean write(Board board) {
        boardIDs.get(host).add(board.getId());
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(boardLog))) {
            out.writeObject(boardIDs);
            return true;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    /**
     * Checks the client's previously accessed the board
     *
     * @param serverBoards boards present on the server
     * @return boolean value
     */
    public List<Board> getClientBoardList(List<Board> serverBoards) {
        boardIDs.replace(host, new ArrayList<>(boardIDs.get(host).stream()
            .filter(id -> serverBoards.stream()
                .map(Board::getId).toList().contains(id))
            .toList()));
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(boardLog))) {
            out.writeObject(boardIDs);
        } catch (Exception e2) {
            e2.printStackTrace();
        } //remove deleted boards from log
        return serverBoards.stream().filter(board -> boardIDs.get(host).contains(board.getId())).toList();
    }

    /**
     * removes the board from client's list (marks it as unvisited)
     *
     * @param board board to remove
     * @return boolean whether the task has been successfully completed
     */
    public boolean remove(Board board) {
        boardIDs.get(host).remove(board.getId());
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(boardLog))) {
            out.writeObject(boardIDs);
            return true;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }
}
