package com.defaulty.explorer.panels;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import com.defaulty.explorer.control.rescontrol.image.ImageSetter;
import com.defaulty.explorer.control.rescontrol.image.ImageSizePack;
import com.defaulty.explorer.model.FileLabeledCell;
import javafx.scene.control.Labeled;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.util.HashMap;

public class FolderTree extends BorderPane implements ViewObserver {

    private final ViewConnector connector;
    private final TreeView<File> tree = new TreeView<>();

    private HashMap<File, Labeled> cellHashMap = new HashMap<>();

    public FolderTree(ViewConnector connector) {
        this.connector = connector;
        this.connector.register(this);
        setCenter(tree);
        setMinWidth(200);
        SplitPane.setResizableWithParent(this, false);
        init();
    }

    private void init() {
        tree.getStylesheets().add("css/hide-scroll.css");
        tree.setShowRoot(true);
        tree.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (isArrowKeys(event))
                connector.loadFork(tree.getSelectionModel().getSelectedItem(), true);
        });
        tree.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 1)
                connector.loadFork(tree.getSelectionModel().getSelectedItem(), true);
        });
        tree.setCellFactory(param -> new FileLabeledCell(tree, cellHashMap).getTreeCell());
        tree.setPrefWidth(300);
    }

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

    private void changeState(TreeItem<File> fork) {
        if (fork != null && fork.getValue() != null) {
            Labeled labeled = cellHashMap.get(fork.getValue());
            if (labeled != null)
                labeled.setGraphic(new ImageSetter().getImageView(fork, ImageSizePack.ImageSize.SMALL));
        }
    }

    private void changeFork(TreeItem<File> fork) {
        if (tree.getRoot() == null) tree.setRoot(fork);
    }

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
