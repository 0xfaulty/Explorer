package com.defaulty.explorer.control;

import com.defaulty.explorer.control.observer.ViewObserver;
import javafx.scene.control.TreeItem;

import java.io.File;

public class LoadTreeItem {

    private TreeItem<File> loadItem;
    private ViewObserver observer;

    public LoadTreeItem(TreeItem<File> loadItem, ViewObserver observer) {
        this.loadItem = loadItem;
        this.observer = observer;
    }

    public void run(){
        new Thread(this::runLoad).start();
    }

    private void runLoad(){
        loadItem.setExpanded(true);
        observer.changeFork(loadItem);
    }


}
