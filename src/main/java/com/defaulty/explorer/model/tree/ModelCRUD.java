package com.defaulty.explorer.model.tree;

import com.defaulty.explorer.control.rescontrol.files.FileOperations;
import javafx.scene.control.TreeItem;

import java.io.File;

/**
 * Контракт для модели, включающий операции с файлами.
 */
public interface ModelCRUD extends FileOperations {

    /**
     * Вызвов подгрузки к дереву указанной ветки.
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

}
