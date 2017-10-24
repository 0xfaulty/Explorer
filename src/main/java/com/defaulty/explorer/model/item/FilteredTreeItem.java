package com.defaulty.explorer.model.item;

import com.defaulty.explorer.control.rescontrol.image.FolderIcons;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.File;

/**
 * Контракт для класса расширяющего функционал элемента дерева.
 */
public interface FilteredTreeItem {

    /**
     * Удалить дочерний элемент текущего.
     *
     * @param item - удаляемый элемент.
     */
    void removeFileChildren(TreeItem<File> item);

    /**
     * Создать дочерние элементы текущего.
     */
    void createChildren();

    /**
     * Получить дочерние директории.
     *
     * @return - список элеметов директорий.
     */
    ObservableList<TreeItem<File>> getFolderChildren();

    /**
     * Получить дочерние файлы.
     *
     * @return - список элеметов файлов.
     */
    ObservableList<TreeItem<File>> getFileChildren();

    /**
     * Установить дочерние элементы.
     *
     * @param list - дочерние элементы.
     */
    void setChildren(ObservableList<TreeItem<File>> list);

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

}
