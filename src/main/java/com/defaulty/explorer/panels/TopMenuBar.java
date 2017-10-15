package com.defaulty.explorer.panels;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.observer.ViewConnector;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class TopMenuBar extends MenuBar {

    private final ViewConnector connector;

    public TopMenuBar(ViewConnector connector) {
        this.connector = connector;

        Menu menuFile = new Menu("Файл");
        MenuItem createNewFolder = new MenuItem("Создать папку");
        createNewFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //connector.createFolder();
            }
        });
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
}
