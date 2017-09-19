package com.defaulty.explorer.panels;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class TopMenuBar extends MenuBar {

    public TopMenuBar() {
        Menu menuFile = new Menu("Файл");
        Menu menuEdit = new Menu("Правка");
        Menu menuView = new Menu("Вид");
        Menu menuHelp = new Menu("Справка");
        MenuItem about = new MenuItem("О программе");
        about.setOnAction(event -> {
            System.out.println("about call");
        });
        menuHelp.getItems().add(about);
        getMenus().addAll(menuFile, menuEdit, menuView, menuHelp);
    }
}
