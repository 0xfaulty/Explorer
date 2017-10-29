package com.defaulty.explorer.panels;

import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import com.defaulty.explorer.model.cell.LabeledCell;
import com.defaulty.explorer.model.tree.ModelOperations;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;

import java.io.File;

/**
 * Панель всплывающего меню.
 */
public class FilePopupMenu extends ContextMenu implements ViewObserver {

    private File currentItem;
    private MenuItem pastMenu;

    private File currentRoot;

    private LabeledCell labeledCell;

    private final ModelOperations modelOperations;

    public FilePopupMenu(ViewConnector connector) {
        connector.register(this);
        this.modelOperations = connector.getModelOperations();

        MenuItem openMenu = getMenuItem("Открыть", () -> modelOperations.open(currentItem));
        SeparatorMenuItem separator1 = new SeparatorMenuItem();
        MenuItem cutMenu = getMenuItem("Вырезать", () -> modelOperations.cut(currentItem));
        MenuItem copyMenu = getMenuItem("Копировать", () -> modelOperations.copy(currentItem));
        pastMenu = getMenuItem("Вставить", () -> modelOperations.paste(currentRoot));
        pastMenu.setVisible(false);
        SeparatorMenuItem separator2 = new SeparatorMenuItem();
        MenuItem deleteMenu = getMenuItem("Удалить", () -> modelOperations.delete(currentItem));
        MenuItem renameMenu = getMenuItem("Переименовать", () -> labeledCell.startEditCell(modelOperations));
        MenuItem createFolderMenu = getMenuItem("Создать папку", () -> modelOperations.createFolderIn(currentRoot));

        getItems().addAll(openMenu, separator1, cutMenu, copyMenu, pastMenu, renameMenu,
                separator2, deleteMenu, createFolderMenu);
    }

    /**
     * Создание объекта меню.
     *
     * @param name - отображаемое название.
     * @param run  - вызываемый метод.
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
     *
     * @param labeledCell - ячейка с которой вызвана панель.
     * @param currentItem - элемент с которого вызвана панель.
     * @param anchor      - родительское окно.
     * @param event       - событие мыши для определения места отрисоки меню.
     */
    public void show(LabeledCell labeledCell, File currentItem, Node anchor, MouseEvent event) {
        this.currentItem = currentItem;
        this.labeledCell = labeledCell;
        requestFocus();
        pastMenu.setVisible(modelOperations.isCopyOrCut());
        show(anchor, event.getScreenX(), event.getScreenY());
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