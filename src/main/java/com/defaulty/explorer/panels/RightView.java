package com.defaulty.explorer.panels;

import com.defaulty.explorer.control.ViewType;
import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import javafx.scene.layout.StackPane;

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
    public void receiveEvent(ViewEvent event) {
        switch (event.getEventType()) {
            case SET_RIGHT_VIEW:
                setRightView(event.getViewType());
                break;
        }
    }

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

}
