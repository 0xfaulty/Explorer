package com.defaulty.explorer.model.item;

import com.defaulty.explorer.control.rescontrol.files.TypeSetter;
import com.defaulty.explorer.control.rescontrol.image.FolderIcons;
import com.defaulty.explorer.model.tree.TreeBackPoint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.File;
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

    private ObservableList<TreeItem<File>> fileChildren = FXCollections.observableArrayList();

    private FolderIcons iconType = FolderIcons.UNLOAD_FOLDER;

    private String itemSize;
    private String itemType;
    private String itemData;

    public FilteredTreeItemImpl(File file, TreeBackPoint changeBackPoint) {
        super(file);

        updateItem(file);

        Runnable eventHandler = () -> {
            if (isExpanded())
                setIconType(FolderIcons.OPEN_FOLDER);
            else
                setIconType(FolderIcons.CLOSE_FOLDER);
            changeBackPoint.accept(getValue());
        };

        addEventHandler(TreeItem.branchExpandedEvent(), event -> eventHandler.run());
        addEventHandler(TreeItem.branchCollapsedEvent(), event -> eventHandler.run());
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
        int result = (getValue().isDirectory() ? 1 : 0);
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        return result;
    }

    @Override
    public ObservableList<TreeItem<File>> getFileChildren() {
        return fileChildren;
    }

    @Override
    public FolderIcons getIconType() {
        return iconType;
    }

    @Override
    public void setIconType(FolderIcons iconType) {
        this.iconType = iconType;

    }

    @Override
    public void updateItem(File file) {
        setValue(file);
        itemSize = file.isDirectory() ? "" : readableSize(file.length());
        itemType = TypeSetter.getFileType(file);
        itemData = new SimpleDateFormat().format(new Date(file.lastModified()));
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
