package com.defaulty.explorer.model;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.File;

public interface FilteredTreeItem {

    enum IconType {
        UNLOAD(1),
        LOADABLE(2),
        LOADED(3),
        EXPANDED(4),
        FILE(5);

        public boolean moreThen(IconType iconType) {
            return iconType.status > status;
        }

        private final int status;

        IconType(int i) {
            this.status = i;
        }
    }

    void createChildren();

    ObservableList<TreeItem<File>> getFolderChildren();

    ObservableList<TreeItem<File>> getFileChildren();

    void setChildren(ObservableList<TreeItem<File>> list);

    void setIconType(IconType iconType);

    IconType getIconType();

}
