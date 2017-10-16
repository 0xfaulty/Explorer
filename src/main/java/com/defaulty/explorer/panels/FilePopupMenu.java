package com.defaulty.explorer.panels;

import com.defaulty.explorer.control.rescontrol.ExRunnable;
import com.defaulty.explorer.control.rescontrol.FileCRUD;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class FilePopupMenu extends ContextMenu {

    private FileCRUD fileCRUD = new FileCRUD();
    private File file;
    private MenuItem pastMenu;

    public FilePopupMenu() {
        MenuItem openMenu = getMenuItem("Открыть", () -> fileCRUD.open(file));
        SeparatorMenuItem separator1 = new SeparatorMenuItem();
        MenuItem cutMenu = getMenuItem("Вырезать", () -> fileCRUD.cut(file));
        MenuItem copyMenu = getMenuItem("Копировать", () -> fileCRUD.copy(file));
        pastMenu = getMenuItem("Вставить", () -> fileCRUD.past(file.getParentFile()));
        pastMenu.setVisible(false);
        SeparatorMenuItem separator2 = new SeparatorMenuItem();
        MenuItem deleteMenu = getMenuItem("Удалить", () -> fileCRUD.delete(file));
        MenuItem renameMenu = getMenuItem("Переименовать", () -> fileCRUD.renameSet(file));
        MenuItem createFolderMenu = getMenuItem("Создать папку", () -> fileCRUD.cut(file));

        getItems().addAll(openMenu, separator1, cutMenu, copyMenu, pastMenu,
                separator2, deleteMenu, renameMenu, createFolderMenu);
    }

    private MenuItem getMenuItem(String name, ExRunnable run) {
        MenuItem menuItem = new Menu(name);
        menuItem.setOnAction(event -> {
            try {
                run.run();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            hide();
        });
        return menuItem;
    }

    public void show(File file, Node anchor, MouseEvent event) {
        this.file = file;
        requestFocus();
        pastMenu.setVisible(fileCRUD.isCopyOrCup());
        show(anchor, event.getScreenX(), event.getScreenY());
    }

}