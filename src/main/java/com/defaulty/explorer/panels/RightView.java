package com.defaulty.explorer.panels;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.ViewType;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.File;

public class RightView extends StackPane implements ViewObserver {

    private final ViewConnector connector;

    private final FileTable fileTable;
    private final FileGrid fileGrid;

    private ViewType cType = ViewType.TABLE;

    public RightView(ViewConnector connector) {
        this.connector = connector;
        this.connector.register(this);
        fileTable = new FileTable(connector);
        fileGrid = new FileGrid(connector);

        getChildren().add(fileTable);
    }

    @Override
    public void changeFork(TreeItem<File> fork) {

    }

    @Override
    public void changeState(TreeItem<File> fork) {

    }

    @Override
    public void setTheme(ThemeType t) {

    }

    @Override
    public void setRightView(ViewType t) {
        if (cType != t) {
            cType = t;
            getChildren().clear();
            if (t == ViewType.TABLE) {
                fileTable.setVisible(true);
                fileGrid.setVisible(false);
                getChildren().add(fileTable);
            } else {
                fileTable.setVisible(false);
                fileGrid.setVisible(true);
                getChildren().add(fileGrid);
            }
        }
    }

    @Override
    public void createFolder() {

    }
}
