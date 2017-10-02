package com.defaulty.explorer.panels;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.ViewObserver;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class TopMenuBar extends MenuBar {

    private final ViewObserver viewObserver;

    public TopMenuBar(ViewObserver observer) {
        this.viewObserver = observer;

        Menu menuFile = new Menu("Файл");
        MenuItem createNewFolder = new MenuItem("Создать папку");
        createNewFolder.setOnAction(event -> observer.createFolder());
        menuFile.getItems().addAll(createNewFolder);

        Menu menuEdit = new Menu("Правка");

        Menu menuView = new Menu("Вид");
        MenuItem darkTheme = new MenuItem("Темная тема");
        darkTheme.setOnAction(event -> observer.setTheme(ThemeType.DARK));
        MenuItem lightTheme = new MenuItem("Светлая тема");
        lightTheme.setOnAction(event -> observer.setTheme(ThemeType.LIGHT));
        menuView.getItems().addAll(darkTheme, lightTheme);

        Menu menuHelp = new Menu("Справка");
        MenuItem about = new MenuItem("О программе");
        about.setOnAction(event -> System.out.println("about call"));
        menuHelp.getItems().add(about);
        getMenus().addAll(menuFile, menuEdit, menuView, menuHelp);
    }
}
