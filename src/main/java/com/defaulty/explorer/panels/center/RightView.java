package com.defaulty.explorer.panels.center;

import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.observer.ViewConnector;
import com.defaulty.explorer.control.observer.ViewObserver;
import javafx.scene.layout.StackPane;

/**
 * Панель включающая в себя представления {@code FileGrid} и {@code FileTable}.
 * Предназначена для переключения этих панелей при получение события переключения.
 */
public class RightView extends StackPane implements ViewObserver {

    private final FileTable fileTable;
    private final FileGrid fileGrid;

    private ViewType cType = ViewType.TABLE;

    public RightView(ViewConnector connector) {
        connector.register(this);
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

    /**
     * Переключение видимости панелей.
     * @param t - тип видимой панели.
     */
    private void setRightView(ViewType t) {
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
