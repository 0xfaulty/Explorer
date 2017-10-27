package com.defaulty.explorer.panels.center;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import com.defaulty.explorer.control.rescontrol.image.ImageSetter;
import com.defaulty.explorer.control.rescontrol.image.ImageSizePack;
import com.defaulty.explorer.model.item.FileLabeledCell;
import com.defaulty.explorer.model.tree.ModelCRUD;
import javafx.scene.control.Labeled;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.util.HashMap;

/**
 * Панель представляющая отображение системы в виде дерева.
 */
public class FolderTree extends BorderPane implements ViewObserver {

    private final ModelCRUD modelCRUD;
    private final TreeView<File> tree = new TreeView<>();

    private HashMap<File, FileLabeledCell> cellHashMap = new HashMap<>();

    public FolderTree(ViewConnector connector) {
        connector.register(this);
        this.modelCRUD = connector.getModelCRUD();
        setCenter(tree);
        setMinWidth(200);
        SplitPane.setResizableWithParent(this, false);
        init();
    }

    /**
     * Инициализация дерева.
     */
    private void init() {
        tree.getStylesheets().add("css/hide-scroll.css");
        tree.setShowRoot(true);
        tree.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (isArrowKeys(event)) {
                TreeItem<File> item = tree.getSelectionModel().getSelectedItem();
                if (item != null) modelCRUD.loadFork(item.getValue());
            }
        });
        tree.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 1){
                TreeItem<File> item = tree.getSelectionModel().getSelectedItem();
                if (item != null) modelCRUD.loadFork(item.getValue());
            }
        });
        tree.setCellFactory(param -> new FileLabeledCell(tree, cellHashMap).getTreeCell());
        tree.setPrefWidth(300);
    }

    /**
     * Проверяет является ли нажатая клавиша кнопкой стрелок.
     * @param event - событие нажатия.
     * @return - результат проверки.
     */
    private boolean isArrowKeys(KeyEvent event) {
        return event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP ||
                event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT;
    }

    @Override
    public void receiveEvent(ViewEvent event) {
        switch (event.getEventType()) {
            case CHANGE_FORK:
                changeFork(event.getFork());
                break;
            case CHANGE_STATE:
                changeState(event.getFork());
                break;
            case SET_THEME:
                setTheme(event.getThemeType());
                break;
        }
    }

    /**
     * Обновить состояние объекта
     *
     * @param fork - обновляемый объект.
     */
    private void changeState(TreeItem<File> fork) {
        if (fork != null && fork.getValue() != null) {
            FileLabeledCell cell = cellHashMap.get(fork.getValue());
            if (cell != null) {
                Labeled labeled = cell.getLabeled();
                if (labeled != null)
                    labeled.setGraphic(new ImageSetter().getImageView(fork, ImageSizePack.ImageSize.SMALL));
            }
        }
    }

    /**
     * Сменить отображаемую ветку
     *
     * @param fork - новая ветка.
     */
    private void changeFork(TreeItem<File> fork) {
        if (tree.getRoot() == null) tree.setRoot(fork);
    }

    /**
     * Сметить оформление панели.
     *
     * @param t тип офорления.
     */
    private void setTheme(ThemeType t) {
        switch (t) {
            case DARK:
                //tree.getStylesheets().setAll("css/table-dark.css");
                break;
            case LIGHT:
                //tree.getStylesheets().setAll("css/table-light.css");
                break;
        }
    }

}
