package com.defaulty.explorer.model.storage;

import javafx.scene.control.TreeItem;

import java.io.File;
import java.util.Collection;

/**
 * Интерфейс описывает главное хранилище элементов дерева соответствующее
 * элементам файловой системы из которого эти элементы можно получать по
 * ключу типа {@code File}.
 */
public interface ItemStorage {

    /**
     * Подгрузить файлы в директории.
     *
     * @param file - директория.
     */
    void createChildren(File file);

    /**
     * Получить элемент дерева по объекту типа {@code File}
     * из хранилища. При отсутствии в хранилище элемента таковой
     * должен быть создан.
     *
     * @param file - элемент файловой системы.
     * @return - элемент дерева.
     */
    TreeItem<File> getTreeItem(File file);

    /**
     * Возвращает статус наличия в хранилище объекта.
     * Не создаёт новый при отсутствии.
     *
     * @param file - элемент файловой системы.
     * @return - статус наличия элемента.
     */
    boolean isHashed(File file);

    /**
     * Удалить элемент из хранилища.
     *
     * @param file - удаляемый элемент.
     */
    void removeItem(File file);

    /**
     * Получить список всех хранимыеъ элементов.
     *
     * @return список элементов.
     */
    Collection<TreeItem<File>> getValues();

    /**
     * Очистить все хранимые элементы.
     */
    void clearStorage();

    /**
     * Поменять ключ доступа к элементу хранилища.
     *
     * @param oldKey - старый ключ.
     * @param newKey - новый ключ.
     */
    void changeKey(File oldKey, File newKey);

}
