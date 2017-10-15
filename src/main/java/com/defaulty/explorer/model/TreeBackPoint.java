package com.defaulty.explorer.model;

import javafx.scene.control.TreeItem;

import java.io.File;

public interface TreeBackPoint {

    void accept(TreeItem<File> item);
}
