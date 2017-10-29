package com.defaulty.explorer.model.item;

import com.defaulty.explorer.control.rescontrol.image.FolderIcons;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.util.List;

/**
 * Контракт для класса расширяющего функционал элемента дерева.
 */
public interface FilteredTreeItem extends Cloneable {

    /**
     * Получить дочерние файлы.
     *
     * @return - список элеметов файлов.
     */
    ObservableList<TreeItem<File>> getFileChildren();

    /**
     * Установить тип иконки элемента
     *
     * @param iconType - тип иконки.
     */
    void setIconType(FolderIcons iconType);

    /**
     * Получить текущий тип иконки.
     *
     * @return - тип иконки.
     */
    FolderIcons getIconType();

    TreeItem<File> getClone();

    void updateItem(File file);

}
