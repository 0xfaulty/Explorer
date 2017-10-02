package com.defaulty.explorer.panels;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.File;

public interface FolderTreeItem {

    boolean isLeaf();

    boolean isDirectory();

    long lastModified();

    long length();

    boolean isHidden();

    boolean isLazyLoadFlag();

    File getValue();

    boolean isExpanded();

    void createExpandTreeFork();

    ObservableList<TreeItem<File>> getFileChildrenList();
}
