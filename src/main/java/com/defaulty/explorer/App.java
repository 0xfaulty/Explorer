package com.defaulty.explorer;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewConnectorImpl;
import com.defaulty.explorer.model.tree.TreeModel;
import com.defaulty.explorer.model.tree.TreeModelImpl;
import com.defaulty.explorer.panels.bottom.BottomBar;
import com.defaulty.explorer.panels.center.FolderTree;
import com.defaulty.explorer.panels.center.RightView;
import com.defaulty.explorer.panels.top.TopMenuBar;
import com.defaulty.explorer.panels.top.TopToolBar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileFilter;

/**
 * @author 0xFaulty
 * @version 0.2
 * <p>
 * Простой файловый менеджер с различными видами отображения
 * файловой системы. Имеет дополнительную историю навигации.
 * Включены базовые операции с элементами такие как: открытие,
 * копирование, вырезка, вставка, удаление, создание папки.
 * Дополнен поиском по элементам, отображает процесс поиска.
 * <p>
 * Добавлены кастомные иконки состояний папок а также иконок
 * ассоциирующихся с некоторыми типами файлов.
 * <p>
 * Подгрузка папок осуществляется по требованию, то есть первое
 * открытие загружает содержимое, не открывая директорию.
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FileFilter fileFilter = e -> !e.isHidden();

            TreeModel treeModel = new TreeModelImpl(fileFilter);
            ViewConnector connector = new ViewConnectorImpl(treeModel);

            VBox menus = new VBox();
            menus.getChildren().addAll(new TopMenuBar(connector), new TopToolBar(connector));

            SplitPane splitView = new SplitPane();
            splitView.getItems().addAll(new FolderTree(connector), new RightView(connector));
            splitView.setDividerPositions(0.25);

            BorderPane rootPane = new BorderPane();
            rootPane.setTop(menus);
            rootPane.setCenter(splitView);
            rootPane.setBottom(new BottomBar(connector));

            connector.changeTheme(ThemeType.LIGHT);
            treeModel.loadFork(new File("/"));

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

