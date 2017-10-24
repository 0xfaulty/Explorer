package com.defaulty.explorer.model.tree;

import javafx.scene.control.TreeItem;

import java.io.File;

/**
 * Контракт для модели, включающий операции с файлами.
 */
public interface ModelCRUD {

    /**
     * Вызвов подгрузки к дереву указанной ветки.
     *
     * @param fork - ветка необходимая для подгрузки.
     */
    void loadFork(TreeItem<File> fork);

    /**
     * Поиск элемента
     *
     * @param currentFork - текущая ветка для поиска.
     * @param s           - ключевое слово поиска.
     */
    void treeSearch(TreeItem<File> currentFork, String s);

    /**
     * Вызов добавления новой папки.
     *
     * @param parentItem - родительская папка для новой папки.
     */
    void createFolderIn(TreeItem<File> parentItem);

    /**
     * Открыть с помощью ассоциативной для него в системе программы.
     *
     * @param item - узел содержащий элемент для открытия.
     */
    void open(TreeItem<File> item);

    /**
     * Вырезать элемент для предпологаемой последующей вставки.
     *
     * @param item       - вырезаемый элемент.
     * @param itemParent - родительский элемент для вырезаемого.
     */
    void cut(TreeItem<File> item, TreeItem<File> itemParent);

    /**
     * Копировать элемент для предпологаемой последующей вставки.
     *
     * @param item - копируемый элемент.
     */
    void copy(TreeItem<File> item);

    /**
     * Вставка элемента.
     *
     * @param parentItem - узел для вставки
     */
    void paste(TreeItem<File> parentItem);

    /**
     * Удалить элемент.
     *
     * @param item       - элемент для удаления.
     * @param itemParent - родительский элемент для удаляемого.
     */
    void delete(TreeItem<File> item, TreeItem<File> itemParent);

    /**
     * Проверка на вырезанный или скопированный элемент в буфере.
     *
     * @return - наличие элемента.
     */
    boolean isCopyOrCut();

}
