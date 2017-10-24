package com.defaulty.explorer.panels;

import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import com.defaulty.explorer.model.tree.ModelCRUD;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.File;

/**
 * Панель всплывающего меню.
 */
public class FilePopupMenu extends ContextMenu implements ViewObserver {

    private TreeItem<File> currentItem;
    private MenuItem pastMenu;

    private TreeItem<File> currentRoot;

    private final ModelCRUD modelCRUD;

    public FilePopupMenu(ViewConnector connector) {
        connector.register(this);
        this.modelCRUD = connector.getModelCRUD();

        MenuItem openMenu = getMenuItem("Открыть", () -> modelCRUD.open(currentItem));
        SeparatorMenuItem separator1 = new SeparatorMenuItem();
        MenuItem cutMenu = getMenuItem("Вырезать", () -> modelCRUD.cut(currentItem, currentRoot));
        MenuItem copyMenu = getMenuItem("Копировать", () -> modelCRUD.copy(currentItem));
        pastMenu = getMenuItem("Вставить", () -> modelCRUD.paste(currentRoot));
        pastMenu.setVisible(false);
        SeparatorMenuItem separator2 = new SeparatorMenuItem();
        MenuItem deleteMenu = getMenuItem("Удалить", () -> modelCRUD.delete(currentItem, currentRoot));
        //MenuItem renameMenu = getMenuItem("Переименовать", () -> fileOperations.rename(currentItem, newFile));
        MenuItem createFolderMenu = getMenuItem("Создать папку", () -> modelCRUD.createFolderIn(currentRoot));

        getItems().addAll(openMenu, separator1, cutMenu, copyMenu, pastMenu,
                separator2, deleteMenu, createFolderMenu);
    }

    /**
     * Создание объекта меню.
     * @param name - отображаемое название.
     * @param run - вызываемый метод.
     * @return - созданный пункт меню.
     */
    private MenuItem getMenuItem(String name, Runnable run) {
        MenuItem menuItem = new Menu(name);
        menuItem.setOnAction(event -> {
            run.run();
            hide();
        });
        return menuItem;
    }

    /**
     * Показать всплывающую панель.
     * @param currentItem - элемент с которого вызвана панель.
     * @param anchor - родительское окно.
     * @param event - событие мыши для определения места отрисоки меню.
     */
    public void show(TreeItem<File> currentItem, Node anchor, MouseEvent event) {
        this.currentItem = currentItem;
        requestFocus();
        pastMenu.setVisible(modelCRUD.isCopyOrCut());
        show(anchor, event.getScreenX(), event.getScreenY());
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