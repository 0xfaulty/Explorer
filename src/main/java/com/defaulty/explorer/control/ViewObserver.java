package com.defaulty.explorer.control;

import javafx.scene.control.TreeItem;

import java.io.File;

public interface ViewObserver {

    void changeNode(TreeItem<File> item);

    void setTheme(ThemeType t);

    void createFolder();
}
