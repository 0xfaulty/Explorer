package com.defaulty.explorer.panels;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.ViewType;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;

import java.io.File;

public class BottomBar extends BorderPane implements ViewObserver{

    private final ViewConnector connector;

    public BottomBar(ViewConnector connector) {
        this.connector = connector;

        Button tableViewButt = new Button("T");
        tableViewButt.setOnAction(event -> connector.changeRightView(ViewType.TABLE));
        Button gridViewButt = new Button("G");
        gridViewButt.setOnAction(event -> connector.changeRightView(ViewType.GRID));

        BorderPane pane = new BorderPane();
        pane.setLeft(tableViewButt);
        pane.setRight(gridViewButt);

        this.setRight(pane);
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

    }

    @Override
    public void createFolder() {

    }
}
