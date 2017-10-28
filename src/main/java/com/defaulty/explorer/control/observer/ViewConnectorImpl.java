package com.defaulty.explorer.control.observer;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.events.EventType;
import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.events.ViewEventImpl;
import com.defaulty.explorer.model.search.SearchTask;
import com.defaulty.explorer.model.tree.ModelOperations;
import com.defaulty.explorer.model.tree.TreeModel;
import com.defaulty.explorer.panels.center.ViewType;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс поддерживающий контракт {@code ViewConnectorModel}.
 */
public class ViewConnectorImpl implements ViewConnectorModel {

    /**
     * Список зарегестрированных наблюдателей.
     */
    private List<ViewObserver> observers = new ArrayList<>();

    /**
     * Модель работы с деревом элементов.
     */
    private TreeModel treeModel;

    public ViewConnectorImpl(TreeModel treeModel) {
        this.treeModel = treeModel;
        this.treeModel.setViewConnector(this);
    }

    /**
     * Отправить событие всем наблюдателям.
     * @param event - событие.
     */
    private void sendEvent(ViewEvent event) {
        for (ViewObserver outlet : this.observers) {
            outlet.receiveEvent(event);
        }
    }

    @Override
    public void register(ViewObserver outlet) {
        observers.add(outlet);
    }

    @Override
    public void changeFork(TreeItem<File> fork) {
        ViewEvent event = new ViewEventImpl(EventType.CHANGE_FORK);
        event.setFork(fork);
        sendEvent(event);
    }

    @Override
    public void addSearchNode(TreeItem<File> fork) {
        ViewEvent event = new ViewEventImpl(EventType.ADD_NODE);
        event.setFork(fork);
        sendEvent(event);
    }

    @Override
    public void changeState(TreeItem<File> fork) {
        ViewEvent event = new ViewEventImpl(EventType.CHANGE_STATE);
        event.setFork(fork);
        sendEvent(event);
    }

    @Override
    public void changeTheme(ThemeType type) {
        ViewEvent event = new ViewEventImpl(EventType.SET_THEME);
        event.setThemeType(type);
        sendEvent(event);
    }

    @Override
    public void sendSearchTask(SearchTask searchTask) {
        ViewEvent event = new ViewEventImpl(EventType.SEARCH_TASK);
        event.setSearchTask(searchTask);
        sendEvent(event);
    }

    @Override
    public void changeRightView(ViewType type) {
        ViewEvent event = new ViewEventImpl(EventType.SET_RIGHT_VIEW);
        event.setViewType(type);
        sendEvent(event);
    }

    @Override
    public ModelOperations getModelCRUD() {
        return treeModel;
    }

}
