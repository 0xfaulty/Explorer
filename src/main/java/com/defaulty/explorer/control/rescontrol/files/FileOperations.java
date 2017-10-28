package com.defaulty.explorer.control.rescontrol.files;

import java.io.File;

public interface FileOperations {

    /**
     * Открыть с помощью ассоциативной для него в системе программы.
     *
     * @param file - открываемый файл.
     */
    void open(File file);

    /**
     * Вырезать элемент для предпологаемой последующей вставки.
     *
     * @param sourceFile - вырезаемый файл.
     */
    void cut(File sourceFile);

    /**
     * Копировать элемент для предпологаемой последующей вставки.
     *
     * @param sourceFile - копируемый файл.
     */
    void copy(File sourceFile);

    /**
     * Вставка элемента.
     *
     * @param destParentFolder - узел для вставки
     */
    void paste(File destParentFolder);

    /**
     * Удалить элемент.
     *
     * @param file - элемент для удаления.
     * @return - успех операции.
     */
    boolean delete(File file);

    /**
     * Переименовать элемент.
     *
     * @param sourceFile - файл с начальным именем.
     * @param destFile   - файл с требуемым именем.
     * @return - успех операции.
     */
    boolean rename(File sourceFile, File destFile);

    /**
     * Создать новую директорию в текущей.
     *
     * @param rootFolder - текущая директория.
     * @return - успех операции.
     */
    boolean createFolderIn(File rootFolder);

    /**
     * Проверка на вырезанный или скопированный элемент в буфере.
     *
     * @return - наличие элемента.
     */
    boolean isCopyOrCut();

    /**
     * Получить файл из буфера копирования или вырезки.
     *
     * @return - файл.
     */
    File getBuffFile();

    /**
     * Получить значение флага вырезки элемента.
     *
     * @return - значение флага.
     */
    boolean isCutFlag();

}
