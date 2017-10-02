package com.defaulty.explorer.panels;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;

import javax.swing.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FolderTreeItemImpl extends TreeItem<File> implements FolderTreeItem {

    private SimpleDateFormat dateFormat = new SimpleDateFormat();

    private boolean expanded = false;
    private boolean directory;
    private boolean hidden;
    private long length;
    private long lastModified;
    private boolean lazyLoadFlag;

    private ObservableList<TreeItem<File>> fileChildrenList = FXCollections.observableArrayList();

    private final TreeItem<File> itemName;
    private final String itemSize;
    private final String itemType;
    private final String itemData;

    public FolderTreeItemImpl(File file) {
        this(file, false);
    }

    public FolderTreeItemImpl(File file, boolean lazyLoadFlag) {
        this.lazyLoadFlag = lazyLoadFlag;

        setValue(file);

        EventHandler<TreeItem.TreeModificationEvent<File>> eventHandler = event -> changeExpand();
        addEventHandler(TreeItem.branchExpandedEvent(), eventHandler);
        //addEventHandler(TreeItem.branchCollapsedEvent(), eventHandler);

        directory = file.isDirectory();
        hidden = file.isHidden();
        length = file.length();
        lastModified = file.lastModified();

        itemName = this;
        itemSize = file.isDirectory() ? "" : readableSize(file.length(), false);
        this.itemType = getFileType(file);
        //itemType = "Type";
        itemData = getModifiedDate(file);
    }

    private void changeExpand() {
        if (expanded != isExpanded()) {
            expanded = isExpanded();
            if (expanded) createExpandTreeFork();
            if (getChildren().size() == 0) {
                Event.fireEvent(this,
                        new TreeItem.TreeModificationEvent<>(TreeItem.valueChangedEvent(), this, getValue()));
            }
        }
    }

    public void createExpandTreeFork(){
        if (getChildren().isEmpty() && fileChildrenList.isEmpty()) createChildren();
    }

    private void createChildren() {
        if (isDirectory() && getValue() != null) {
            File[] files = getValue().listFiles();
            if (files != null && files.length > 0) {
                for (File childFile : files) {
                    if (!childFile.isHidden() && !lazyLoadFlag) {
                        if (childFile.isDirectory())
                            getChildren().add(new FolderTreeItemImpl(childFile, lazyLoadFlag));
                        else
                            fileChildrenList.add(new FolderTreeItemImpl(childFile, lazyLoadFlag));
                    }
                }
                getChildren().sort((ti1, ti2) ->
                        ((FolderTreeItemImpl) ti1).isDirectory() == ((FolderTreeItemImpl) ti2).isDirectory() ?
                                ti1.getValue().getName().compareToIgnoreCase(ti2.getValue().getName()) :
                                ((FolderTreeItemImpl) ti1).isDirectory() ? -1 : 1);
            }
        }
    }

    private String getFileType(File file) {
        return new JFileChooser().getTypeDescription(file);
    }

    private String getModifiedDate(File file) {
        return dateFormat.format(new Date(file.lastModified()));
    }

    private String readableSize(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " Б";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "кМГТПЭ" : "КМГТПЭ").charAt(exp - 1) + ""; // + (si ? "" : "и");
        return String.format("%.1f %sБ", bytes / Math.pow(unit, exp), pre);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FolderTreeItemImpl that = (FolderTreeItemImpl) o;

        return that.getValue() != null && getValue().getName().equals(that.getValue().getName());
    }

    @Override
    public int hashCode() {
        int result = (expanded ? 1 : 0);
        result = 31 * result + (directory ? 1 : 0);
        result = 31 * result + (hidden ? 1 : 0);
        result = 31 * result + (int) (length ^ (length >>> 32));
        result = 31 * result + (int) (lastModified ^ (lastModified >>> 32));
        result = 31 * result + (lazyLoadFlag ? 1 : 0);
        return result;
    }

    public ObservableList<TreeItem<File>> getFileChildrenList() {
        return fileChildrenList;
    }

    public TreeItem<File> getItemName() {
        return itemName;
    }

    public String getItemSize() {
        return itemSize;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemData() {
        return itemData;
    }

    @Override
    public boolean isLeaf() {
        return !directory;
    }

    @Override
    public boolean isDirectory() {
        return directory;
    }

    @Override
    public long lastModified() {
        return lastModified;
    }

    @Override
    public long length() {
        return length;
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    @Override
    public boolean isLazyLoadFlag() {
        return lazyLoadFlag;
    }

}
