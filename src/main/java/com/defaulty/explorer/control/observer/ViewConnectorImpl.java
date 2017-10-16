package com.defaulty.explorer.control.observer;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.ViewType;
import com.defaulty.explorer.control.events.EventType;
import com.defaulty.explorer.control.events.ViewEvent;
import com.defaulty.explorer.control.events.ViewEventImpl;
import com.defaulty.explorer.model.TreeModel;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ViewConnectorImpl extends Observable implements ViewConnectorModel {

    private List<ViewObserver> observers = new ArrayList<>();

    private TreeModel treeModel;

    public void loadFork(TreeItem<File> fork, boolean checkout) {
        treeModel.loadFork(fork, checkout);
    }

    public void changeFork(TreeItem<File> fork) {
        ViewEvent viewEvent = new ViewEventImpl(EventType.CHANGE_FORK);
        viewEvent.setFork(fork);
        for (ViewObserver outlet : this.observers) {
            outlet.receiveEvent(viewEvent);
        }
    }

    @Override
    public void changeState(TreeItem<File> fork) {
        ViewEvent viewEvent = new ViewEventImpl(EventType.CHANGE_STATE);
        viewEvent.setFork(fork);
        for (ViewObserver outlet : this.observers) {
            outlet.receiveEvent(viewEvent);
        }
    }

    public void changeTheme(ThemeType type) {
        ViewEvent viewEvent = new ViewEventImpl(EventType.SET_THEME);
        viewEvent.setThemeType(type);
        for (ViewObserver outlet : this.observers) {
            outlet.receiveEvent(viewEvent);
        }
    }

    @Override
    public void changeRightView(ViewType type) {
        ViewEvent viewEvent = new ViewEventImpl(EventType.SET_RIGHT_VIEW);
        viewEvent.setViewType(type);
        for (ViewObserver outlet : this.observers) {
            outlet.receiveEvent(viewEvent);
        }
    }

    @Override
    public void treeSearch(TreeItem<File> item, String s) {
        treeModel.treeSearch(item, s);
    }

    public void createFolder() {
        ViewEvent viewEvent = new ViewEventImpl(EventType.CREATE_FOLDER);
        for (ViewObserver outlet : this.observers) {
            outlet.receiveEvent(viewEvent);
        }
    }

    @Override
    public void register(ViewObserver outlet) {
        observers.add(outlet);
    }

    @Override
    public void createModel(TreeModel treeModel) {
        this.treeModel = treeModel;
        this.treeModel.setViewConnector(this);
    }

}
