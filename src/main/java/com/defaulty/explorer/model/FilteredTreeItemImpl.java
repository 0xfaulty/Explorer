package com.defaulty.explorer.model;

import com.defaulty.explorer.control.rescontrol.image.CustomIcons;
import com.defaulty.explorer.control.rescontrol.image.ImageSetter;
import com.defaulty.explorer.control.rescontrol.TypeSetter;
import com.defaulty.explorer.control.rescontrol.image.ImageSizePack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FilteredTreeItemImpl extends TreeItem<File> implements FilteredTreeItem {

    private SimpleDateFormat dateFormat = new SimpleDateFormat();

    private boolean directory;

    private IconType iconType = IconType.UNLOAD;

    private FileFilter itemsFilter;

    private ObservableList<TreeItem<File>> folderChildren = FXCollections.observableArrayList();
    private ObservableList<TreeItem<File>> fileChildren = FXCollections.observableArrayList();

    private File itemFile;
    private String itemSize;
    private String itemType;
    private String itemData;

    public FilteredTreeItemImpl(File file, FileFilter filter) {
        this.itemsFilter = filter;

        setValue(file);

        addEventHandler(TreeItem.branchCollapsedEvent(), event -> setIconType(IconType.EXPANDED));
        addEventHandler(TreeItem.branchCollapsedEvent(), event -> setIconType(IconType.LOADED));

        directory = file.isDirectory();

        itemFile = getValue();
        itemSize = file.isDirectory() ? "" : readableSize(file.length());
        itemType = TypeSetter.getFileType(file);
        itemData = getModifiedDate(file);
    }

    public FilteredTreeItemImpl(File file) {
        this(file, file1 -> true);
    }

    @Override
    public synchronized void createChildren() {
        if (isDirectory() && getValue() != null) {
            File[] files = getValue().listFiles();
            if (files != null && files.length > 0) {
                for (File childFile : files) {
                    if (itemsFilter.accept(childFile)) {
                        if (childFile.isDirectory()) {
                            if (!fileContains(folderChildren, childFile))
                                folderChildren.add(new FilteredTreeItemImpl(childFile));
                        } else {
                            if (!fileContains(fileChildren, childFile))
                                fileChildren.add(new FilteredTreeItemImpl(childFile));
                        }
                    }
                }
            }
            setIconType(IconType.LOADED);
        }
    }

    @Override
    public void setChildren(ObservableList<TreeItem<File>> list) {
        getChildren().setAll(list);
        getChildren().sort((ti1, ti2) ->
                ((FilteredTreeItemImpl) ti1).isDirectory() == ((FilteredTreeItemImpl) ti2).isDirectory() ?
                        ti1.getValue().getName().compareToIgnoreCase(ti2.getValue().getName()) :
                        ((FilteredTreeItemImpl) ti1).isDirectory() ? -1 : 1);
    }


    private boolean fileContains(ObservableList<TreeItem<File>> list, File file) {
        for (TreeItem<File> item : list) {
            if (item.getValue() != null && item.getValue().equals(file))
                return true;
        }
        return false;
    }

    private String getModifiedDate(File file) {
        return dateFormat.format(new Date(file.lastModified()));
    }

    private String readableSize(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " Б";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = ("КМГТПЭ").charAt(exp - 1) + "";
        return String.format("%.1f %sБ", bytes / Math.pow(unit, exp), pre);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilteredTreeItemImpl that = (FilteredTreeItemImpl) o;

        return that.getValue() != null && getValue().getName().equals(that.getValue().getName());
    }

    @Override
    public int hashCode() {
        int result = (directory ? 1 : 0);
        result = 31 * result + (itemFile != null ? itemFile.hashCode() : 0);
        return result;
    }

    public ObservableList<TreeItem<File>> getFolderChildren() {
        return folderChildren;
    }

    public ObservableList<TreeItem<File>> getFileChildren() {
        return fileChildren;
    }

    public File getItemFile() {
        return itemFile;
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

    private boolean isDirectory() {
        return directory;
    }

    public IconType getIconType() {
        return iconType;
    }

    @Override
    public void setIconType(IconType iconType) {
        this.iconType = iconType;
        setImageView();
    }

    private void setImageView() {
        switch (iconType) {
            case UNLOAD:
                new ImageSetter().setImageView(this, CustomIcons.FolderIcons.UNLOAD_FOLDER, ImageSizePack.ImageSize.SMALL);
                break;
            case LOADABLE:
                new ImageSetter().setImageView(this, CustomIcons.FolderIcons.LOADABLE_FOLDER, ImageSizePack.ImageSize.SMALL);
                break;
            case LOADED:
                new ImageSetter().setImageView(this, CustomIcons.FolderIcons.CLOSE_FOLDER, ImageSizePack.ImageSize.SMALL);
                break;
            case EXPANDED:
                new ImageSetter().setImageView(this, CustomIcons.FolderIcons.OPEN_FOLDER, ImageSizePack.ImageSize.SMALL);
                break;
            case FILE:
                break;
        }
    }

}
