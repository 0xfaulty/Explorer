package com.defaulty.explorer.control;

import javafx.scene.control.TreeItem;

import java.io.File;

class CubbyHole {
    private TreeItem<File> contents;
    private boolean available = false;

    public synchronized TreeItem<File> get() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        available = false;
        notifyAll();
        return contents;
    }

    public synchronized void put(TreeItem<File> value) {
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        contents = value;
        available = true;
        notifyAll();
    }
}
