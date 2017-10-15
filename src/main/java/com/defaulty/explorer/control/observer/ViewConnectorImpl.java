package com.defaulty.explorer.control.observer;

import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.ViewType;
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
        for (ViewObserver outlet : this.observers) {
            outlet.changeFork(fork);
        }
    }

    @Override
    public void changeState(TreeItem<File> fork) {
        for (ViewObserver outlet : this.observers) {
            outlet.changeState(fork);
        }
    }

    public void changeTheme(ThemeType type) {
        for (ViewObserver outlet : this.observers) {
            outlet.setTheme(type);
        }
    }

    @Override
    public void changeRightView(ViewType t) {
        for (ViewObserver outlet : this.observers) {
            outlet.setRightView(t);
        }
    }

    @Override
    public void treeSearch(TreeItem<File> item, String s) {
        treeModel.treeSearch(item, s);
    }

    public void createFolder() {
        for (ViewObserver outlet : this.observers) {
            outlet.createFolder();
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
