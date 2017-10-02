package com.defaulty.explorer.control;

import javafx.scene.control.TreeItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ViewObserverImpl extends Observable implements ViewObserver {

    private List<ViewObserver> observers = new ArrayList<>();

    public void changeNode(TreeItem<File> item) {
        for (ViewObserver outlet : this.observers) {
            outlet.changeNode(item);
        }
    }

    public void setTheme(ThemeType type) {
        for (ViewObserver outlet : this.observers) {
            outlet.setTheme(type);
        }
    }

    public void createFolder() {
        for (ViewObserver outlet : this.observers) {
            outlet.createFolder();
        }
    }

    public void register(ViewObserver outlet) {
        observers.add(outlet);
    }

}
