/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.utils.ClientBoardManager;
import client.utils.ServerUtils;
import client.utils.TaskWrapper;
import commons.UpdateModel;
import commons.entities.Board;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.Getter;

public class MainCtrl {

    @Getter
    private ServerUtils server;

    private Stage primaryStage;

    private Scene enter;

    @Getter
    private BoardOverviewCtrl boardOverviewCtrl;

    @Getter
    private CreateOpenCtrl createOpenCtrl;

    @Getter
    private EnterSceneCtrl enterSceneCtrl;

    @Getter
    private Scene board;
    private Scene createOpen;
    @Getter
    private boolean admin;

    @Getter
    private ClientBoardManager clientBoardManager;

    /**
     * Initializes the scene
     *
     * @param primaryStage the default stage before entering the server
     * @param enter        the scene prompting for entering the server
     * @param board        the scene showing the board overview
     * @param createOpen   the scene presenting the options of creating a new board or opening an existing one
     */
    public void initialize(
        Stage primaryStage,
        Pair<EnterSceneCtrl, Parent> enter,
        Pair<BoardOverviewCtrl, Parent> board,
        Pair<CreateOpenCtrl, Parent> createOpen) {
        this.primaryStage = primaryStage;

        this.enterSceneCtrl = enter.getKey();
        this.enter = new Scene(enter.getValue());

        enter.getKey().setHoverEffects();

        this.boardOverviewCtrl = board.getKey();
        this.board = new Scene(board.getValue());

        this.createOpenCtrl = createOpen.getKey();
        this.createOpen = new Scene(createOpen.getValue());

        Stream.of(enter, board, createOpen)
            .map(Pair::getKey)
            .forEach(ButtonCtrl::setHoverEffects);

        showInitialScene();
    }

    /**
     * Loads the board overview with the specified board
     *
     * @param board board
     */
    public void showOverview(Board board) {
        establishWebSocketConnection(board.getId()).thenAccept(v -> {
            primaryStage.setTitle("Overview");
            boardOverviewCtrl.setBoard(board);
            primaryStage.setScene(this.board);
        });
    }

    /**
     * Accesses the specified server and switches scenes to the workspace
     *
     * @param host  - the host of the application (has already been verified)
     * @param admin indicator of whether the user is using admin mode
     */
    public void enter(String host, boolean admin) {
        if(admin) {
            host = host.replace("/admin", "");
        }
        this.admin = admin;
        if(!isAdmin()) {
            if(host.endsWith("/"))
                host = host.substring(0, host.length() - 1);
            clientBoardManager = new ClientBoardManager();
            clientBoardManager.loadContent(new File("client/log.txt"), host);
        }
        this.server = new ServerUtils(host);
        showOpenCreateScene();
    }

    /**
     * Establishes connection through web sockets
     */
    private CompletableFuture<Void> establishWebSocketConnection(Long boardId) {
        // Wrap in Task to allow for UI interaction on complete
        return TaskWrapper.wrap(() -> {
            try {
                return this.server.connect().get();
            } catch (Exception e) {
                return null;
            }
        }).thenAccept(session -> this.server.registerForMessages(
            "/message/receive/" + boardId,
            UpdateModel.class,
            updateModel -> checkForUpdates(boardId)
        ));
    }

    /**
     * Checks for current updates on the board and sets it to the changed version
     */
    private void checkForUpdates(Long boardId) {
        server.getBoard(boardId).thenAccept(board ->
                boardOverviewCtrl.setBoard(board))
            .exceptionally(e -> {
                e.printStackTrace();
                primaryStage.close();
                return null;
            });

    }

    /**
     * Shows the scene prompting for a server to be specified
     */
    public void showInitialScene() {
        enterSceneCtrl.getError().setVisible(false);
        enterSceneCtrl.getEnterPassword().setVisible(false);
        enterSceneCtrl.getPassword().setVisible(false);
        enterSceneCtrl.getConnectButton().setVisible(false);
        enterSceneCtrl.getPasswordEye().setVisible(false);
        enterSceneCtrl.getPasswordVisible().setVisible(false);
        primaryStage.setTitle("Server selection");
        primaryStage.setScene(enter);
        primaryStage.show();
    }

    /**
     * Shows the scene that allows the user to either create a new board or open an existing one
     */
    public void showOpenCreateScene() {
        server.disconnect();
        createOpenCtrl.initializeBoardList()
            .thenAccept(v -> {
                primaryStage.setTitle("Board selection");
                primaryStage.setScene(createOpen);
            }).exceptionally(e -> {
                e.printStackTrace();
                return null;
            });
    }

}