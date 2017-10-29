package com.defaulty.explorer.panels.center;

import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.StackPane;

import java.io.File;

/**
 * Панель включающая в себя представления {@code FileGrid} и {@code FileTable}.
 * Предназначена для переключения этих панелей при получение события переключения.
 */
public class RightView extends StackPane implements ViewObserver {

    private final FileTable fileTable;
    private final FileGrid fileGrid;

    private ViewType cType = ViewType.TABLE;

    private ViewObserver currentPanel;

    private ViewEvent currentRoot;

    public RightView(ViewConnector connector) {
        connector.register(this);
        fileTable = new FileTable(connector);
        fileGrid = new FileGrid(connector);

        currentPanel = fileTable;

        getChildren().add(fileTable);
    }

    @Override
    public void receiveEvent(ViewEvent event) {
        switch (event.getEventType()) {
            case SET_RIGHT_VIEW:
                setRightView(event);
                break;
            case CHANGE_FORK:
                currentRoot = event;
                break;
        }
        currentPanel.receiveEvent(event);
    }

    /**
     * Переключение видимости панелей.
     *
     * @param event - событие переключения.
     */
    private void setRightView(ViewEvent event) {
        if (cType != event.getViewType()) {
            cType = event.getViewType();
            getChildren().clear();
            if (cType == ViewType.TABLE) {
                currentPanel = fileTable;
                fileTable.setVisible(true);
                fileGrid.setVisible(false);
                getChildren().add(fileTable);
                currentPanel.receiveEvent(currentRoot);
            } else {
                currentPanel = fileGrid;
                fileTable.setVisible(false);
                fileGrid.setVisible(true);
                getChildren().add(fileGrid);
                currentPanel.receiveEvent(currentRoot);
            }
        }
    }

}
