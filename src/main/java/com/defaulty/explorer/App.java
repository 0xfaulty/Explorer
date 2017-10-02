package com.defaulty.explorer;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.ViewObserverImpl;
import com.defaulty.explorer.panels.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main class
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            ViewObserverImpl fileViewableImpl = new ViewObserverImpl();

            VBox menus = new VBox();
            TopToolBar topToolBar = new TopToolBar(fileViewableImpl);
            menus.getChildren().addAll(new TopMenuBar(fileViewableImpl), topToolBar);

            Image image = new Image(App.class.getClassLoader().getResourceAsStream("icons/closef.png"));
            primaryStage.getIcons().add(image);

            SplitPane splitView = new SplitPane();
            FolderTree folderTree = new FolderTree(fileViewableImpl);
            FileTable fileTable = new FileTable(fileViewableImpl);

            fileViewableImpl.register(folderTree);
            fileViewableImpl.register(fileTable);
            fileViewableImpl.register(topToolBar);

            fileViewableImpl.setTheme(ThemeType.LIGHT);

            fileTable.init();
            folderTree.init();
            topToolBar.init();

            splitView.getItems().addAll(folderTree, fileTable);
            splitView.setDividerPositions(0.25);

            BorderPane root = new BorderPane();
            root.setTop(menus);
            root.setCenter(splitView);
            root.setBottom(new BottomBar());

            Scene scene = new Scene(root, 900, 600);

            primaryStage.setScene(scene);
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

