package com.defaulty.explorer.panels;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;

import java.io.File;

public class FileTreeItem extends TreeItem<File> {
    private boolean expanded = false;
    private boolean directory;
    private boolean hidden;
    private long length;
    private long lastModified;

    public FileTreeItem(File file) {
        super(file);
        EventHandler<TreeModificationEvent<File>> eventHandler = event -> changeExpand();
        addEventHandler(TreeItem.branchExpandedEvent(), eventHandler);
        addEventHandler(TreeItem.branchCollapsedEvent(), eventHandler);

        directory = getValue().isDirectory();
        hidden = getValue().isHidden();
        length = getValue().length();
        lastModified = getValue().lastModified();
    }

    private void changeExpand() {
        if (expanded != isExpanded()) {
            expanded = isExpanded();
            if (expanded) {
                createChildren();
            } else {
                getChildren().clear();
            }
            if (getChildren().size() == 0)
                Event.fireEvent(this,
                        new TreeItem.TreeModificationEvent<>(TreeItem.valueChangedEvent(), this, getValue()));
        }
    }

    @Override
    public boolean isLeaf() {
        return !isDirectory();
    }

    public boolean isDirectory() {
        return directory;
    }

    public long lastModified() {
        return lastModified;
    }

    public long length() {
        return length;
    }

    public boolean isHidden() {
        return hidden;
    }

    private void createChildren() {
        if (isDirectory() && getValue() != null) {
            File[] files = getValue().listFiles();
            if (files != null && files.length > 0) {
                getChildren().clear();
                for (File childFile : files) {
                    getChildren().add(new FileTreeItem(childFile));
                }
                getChildren().sort((ti1, ti2) -> ((FileTreeItem) ti1).isDirectory() == ((FileTreeItem) ti2).isDirectory() ?
                        ti1.getValue().getName().compareToIgnoreCase(ti2.getValue().getName()) :
                        ((FileTreeItem) ti1).isDirectory() ? -1 : 1);
            }
        }
    }
}
