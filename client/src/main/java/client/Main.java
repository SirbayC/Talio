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
package client;

import static com.google.inject.Guice.createInjector;

import client.scenes.BoardOverviewCtrl;
import client.scenes.CreateOpenCtrl;
import client.scenes.EnterSceneCtrl;
import client.scenes.MainCtrl;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    /**
     * Application main
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Starts the application
     *
     * @param primaryStage the primary stage for this application, onto which the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        var enter = FXML.load(EnterSceneCtrl.class, "client", "scenes", "EnterScene.fxml");
        var board = FXML.load(BoardOverviewCtrl.class, "client", "scenes", "BoardOverviewScene.fxml");
        var createOpen = FXML.load(CreateOpenCtrl.class, "client", "scenes", "CreateOpenScene.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, enter, board, createOpen);
    }

}