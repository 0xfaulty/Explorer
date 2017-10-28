package com.defaulty.explorer.model.storage;

import javafx.scene.control.TreeItem;

import java.io.File;

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
     * Сделать копию указанного элемента с другим идентификатором.
     *
     * @param source - копируемый элемент.
     * @param dest   - новый элемент.
     */
    void copyItem(File source, File dest);

}
