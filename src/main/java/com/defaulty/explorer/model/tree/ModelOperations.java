package com.defaulty.explorer.model.tree;

import com.defaulty.explorer.control.rescontrol.files.FileOperations;
import com.defaulty.explorer.model.search.SearchTask;
import javafx.scene.control.TreeItem;

import java.io.File;

/**
 * Контракт для модели, включающий операции с файлами.
 */
public interface ModelOperations extends FileOperations {

    /**
     * Вызов подгрузки к дереву указанной ветки.
     *
     * @param fork - ветка необходимая для подгрузки.
     */
    void loadFork(File fork);

    /**
     * Поиск элемента
     *
     * @param folder - текущая ветка для поиска.
     * @param s      - ключевое слово поиска.
     */
    void treeSearch(File folder, String s);

    /**
     * Запрос загрузки результатов задачи поиска.
     *
     * @param task - задача поиска.
     */
    void loadSearchResults(SearchTask task);

}
