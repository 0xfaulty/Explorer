package com.defaulty.explorer.panels;

import com.defaulty.explorer.control.ViewType;
import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class BottomBar extends BorderPane implements ViewObserver {

    private final ViewConnector connector;

    private VBox taskPane = new VBox();

    public BottomBar(ViewConnector connector) {
        this.connector = connector;

        Button tableViewButt = new Button("T");
        tableViewButt.setOnAction(event -> connector.changeRightView(ViewType.TABLE));
        Button gridViewButt = new Button("G");
        gridViewButt.setOnAction(event -> connector.changeRightView(ViewType.GRID));

        BorderPane pane = new BorderPane();
        pane.setLeft(tableViewButt);
        pane.setRight(gridViewButt);

        this.setLeft(taskPane);
        this.setRight(pane);
    }

    @Override
    public void receiveEvent(ViewEvent event) {
    }
}
