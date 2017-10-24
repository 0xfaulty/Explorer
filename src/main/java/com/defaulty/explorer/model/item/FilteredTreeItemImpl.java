package com.defaulty.explorer.model.item;

import com.defaulty.explorer.control.rescontrol.files.TypeSetter;
import com.defaulty.explorer.control.rescontrol.image.FolderIcons;
import com.defaulty.explorer.control.rescontrol.image.ImageSetter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс наследуемый от {@code TreeItem<File>} поддерживающий контракт {@code FilteredTreeItem}.
 * Представляет собой версию элемента дерева с расширенным функционалом, возможностью
 * подгрузки подэлементов в стороннем потоке.
 * При создании также подгружает дополнительную информацию такую как: иконка, тип, размер
 * для содержащегося элемента.
 */
public class FilteredTreeItemImpl extends TreeItem<File> implements FilteredTreeItem {

    private boolean directory;

    private FolderIcons iconType = FolderIcons.UNLOAD_FOLDER;

    private FileFilter itemsFilter;

    private ObservableList<TreeItem<File>> folderChildren = FXCollections.observableArrayList();
    private ObservableList<TreeItem<File>> fileChildren = FXCollections.observableArrayList();

    private File itemFile;
    private String itemSize;
    private String itemType;
    private String itemData;

    private FilteredTreeItemImpl(File file) {
        this(file, file1 -> true);
    }

    public FilteredTreeItemImpl(File file, FileFilter filter) {
        this.itemsFilter = filter;

        setValue(file);

        Runnable eventHandler = () -> {
            if (isExpanded())
                setIconType(FolderIcons.OPEN_FOLDER);
            else
                setIconType(FolderIcons.CLOSE_FOLDER);
        };

        addEventHandler(TreeItem.branchExpandedEvent(), event -> eventHandler.run());
        addEventHandler(TreeItem.branchCollapsedEvent(), event -> eventHandler.run());

        directory = file.isDirectory();

        itemFile = getValue();
        itemSize = file.isDirectory() ? "" : readableSize(file.length());
        itemType = TypeSetter.getFileType(file);
        itemData = new SimpleDateFormat().format(new Date(file.lastModified()));
    }

    @Override
    public void removeFileChildren(TreeItem<File> item) {
        fileChildren.remove(item);
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
        }
    }

    private boolean fileContains(ObservableList<TreeItem<File>> list, File file) {
        for (TreeItem<File> item : list) {
            if (item.getValue() != null && item.getValue().equals(file))
                return true;
        }
        return false;
    }

    @Override
    public void setChildren(ObservableList<TreeItem<File>> list) {
        getChildren().clear();
        getChildren().setAll(list);
        getChildren().sort((ti1, ti2) ->
                ((FilteredTreeItemImpl) ti1).isDirectory() == ((FilteredTreeItemImpl) ti2).isDirectory() ?
                        ti1.getValue().getName().compareToIgnoreCase(ti2.getValue().getName()) :
                        ((FilteredTreeItemImpl) ti1).isDirectory() ? -1 : 1);
    }

    private boolean isDirectory() {
        return directory;
    }

    /**
     * Конвертирование размера файла в long в принятый строковый формат.
     *
     * @param bytes - размер.
     * @return - форматированный размер.
     */
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

    public FolderIcons getIconType() {
        return iconType;
    }

    @Override
    public void setIconType(FolderIcons iconType) {
        this.iconType = iconType;
        new ImageSetter().setFolderImageView(this, iconType);
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

}
