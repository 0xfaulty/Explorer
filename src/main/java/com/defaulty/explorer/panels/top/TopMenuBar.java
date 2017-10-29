package com.defaulty.explorer.panels.top;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import com.defaulty.explorer.model.tree.ModelOperations;
import com.defaulty.explorer.panels.center.ViewType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.io.File;

public class TopMenuBar extends MenuBar implements ViewObserver {

    private final ModelOperations modelOperations;
    private File currentRoot;

    public TopMenuBar(ViewConnector connector) {
        connector.register(this);
        modelOperations = connector.getModelOperations();

        Menu menuFile = new Menu("Файл");
        MenuItem createNewFolder = new MenuItem("Создать папку");
        createNewFolder.setOnAction(event -> modelOperations.createFolderIn(currentRoot));
        menuFile.getItems().addAll(createNewFolder);

        Menu menuEdit = new Menu("Правка");

        Menu menuView = new Menu("Вид");

        Menu menuViewViews = new Menu("Отображения");
        MenuItem tableView = new MenuItem("Таблица");
        tableView.setOnAction(event -> connector.changeRightView(ViewType.TABLE));
        MenuItem gridView = new MenuItem("Сетка");
        gridView.setOnAction(event -> connector.changeRightView(ViewType.GRID));
        menuViewViews.getItems().addAll(tableView, gridView);

        Menu menuViewThemes = new Menu("Темы");
        MenuItem darkTheme = new MenuItem("Темная тема");
        darkTheme.setOnAction(event -> connector.changeTheme(ThemeType.DARK));
        MenuItem lightTheme = new MenuItem("Светлая тема");
        lightTheme.setOnAction(event -> connector.changeTheme(ThemeType.LIGHT));
        menuViewThemes.getItems().addAll(lightTheme);

        menuView.getItems().addAll(menuViewViews, menuViewThemes);

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
                currentRoot = event.getFork().getValue();
                break;
        }
    }
}
