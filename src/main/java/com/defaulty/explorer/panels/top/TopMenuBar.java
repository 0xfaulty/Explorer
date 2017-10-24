package com.defaulty.explorer.panels.top;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import com.defaulty.explorer.model.tree.ModelCRUD;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;

import java.io.File;

public class TopMenuBar extends MenuBar implements ViewObserver {

    private final ModelCRUD modelCRUD;
    private TreeItem<File> currentRoot;

    public TopMenuBar(ViewConnector connector) {
        connector.register(this);
        modelCRUD = connector.getModelCRUD();

        Menu menuFile = new Menu("Файл");
        MenuItem createNewFolder = new MenuItem("Создать папку");
        createNewFolder.setOnAction(event -> modelCRUD.createFolderIn(currentRoot));
        menuFile.getItems().addAll(createNewFolder);

        Menu menuEdit = new Menu("Правка");

        Menu menuView = new Menu("Вид");
        MenuItem darkTheme = new MenuItem("Темная тема");
        darkTheme.setOnAction(event -> connector.changeTheme(ThemeType.DARK));
        MenuItem lightTheme = new MenuItem("Светлая тема");
        lightTheme.setOnAction(event -> connector.changeTheme(ThemeType.LIGHT));
        menuView.getItems().addAll(darkTheme, lightTheme);

        Menu menuHelp = new Menu("Справка");
        MenuItem about = new MenuItem("О программе");
        about.setOnAction(event -> System.out.println("about call"));
        menuHelp.getItems().add(about);
        getMenus().addAll(menuFile, menuEdit, menuView, menuHelp);
    }

    @Override
    public void receiveEvent(ViewEvent event) {
        switch (event.getEventType()) {
            case CHANGE_FORK:
                currentRoot = event.getFork();
                break;
        }
    }
}
