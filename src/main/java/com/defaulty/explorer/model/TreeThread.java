package com.defaulty.explorer.model;

import javafx.scene.control.TreeItem;

import java.io.File;

public class TreeThread extends Thread {

    public TreeThread(TreeItem<File> item, Runnable runnable, TreeBackPoint backPoint) {
        super(()->{
            runnable.run();
            backPoint.accept(item);
        });
    }

    public TreeThread(TreeItem<File> item, TreeBackPoint runnable, TreeBackPoint backPoint) {
        super(()-> runnable.accept(item));
    }

}
