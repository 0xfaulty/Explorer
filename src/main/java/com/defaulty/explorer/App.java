package com.defaulty.explorer;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.model.TreeModelImpl;
import com.defaulty.explorer.control.observer.ViewConnectorImpl;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.panels.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

/**
 * Main class
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            ViewConnector connector = new ViewConnectorImpl();

            VBox menus = new VBox();
            menus.getChildren().addAll(new TopMenuBar(connector), new TopToolBar(connector));

            SplitPane splitView = new SplitPane();
            splitView.getItems().addAll(new FolderTree(connector), new RightView(connector));
            //splitView.getItems().addAll(new FolderTree(connector), new FileGrid(connector));
            splitView.setDividerPositions(0.25);

            BorderPane rootPane = new BorderPane();
            rootPane.setTop(menus);
            rootPane.setCenter(splitView);
            rootPane.setBottom(new BottomBar(connector));

            connector.changeTheme(ThemeType.LIGHT);
            connector.createModel(new TreeModelImpl(new File("/")));

            primaryStage.getIcons().add(new Image(
                    App.class.getClassLoader().getResourceAsStream("icons/big/folder_c.png")));

            primaryStage.setScene(new Scene(rootPane, 900, 600));
            primaryStage.setTitle("Explorer");
            primaryStage.setMinWidth(400);
            primaryStage.setMinHeight(200);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}

