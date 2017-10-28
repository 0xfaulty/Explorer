package com.defaulty.explorer.control.rescontrol.files;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.commons.io.FilenameUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

/**
 * Класс поддерживающий контракт {@code FileOperations}.
 */
public class FileOperationsImpl implements FileOperations {

    private File buffFile;
    private boolean cutFlag;

    @Override
    public void open(File file) {
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cut(File sourceFile) {
        this.buffFile = sourceFile;
        this.cutFlag = true;
    }

    @Override
    public void copy(File sourceFile) {
        this.buffFile = sourceFile;
        this.cutFlag = false;
    }

    @Override
    public void paste(File destParentFolder) {
        if (buffFile != null && destParentFolder.isDirectory()) {
            File destFile = new File(destParentFolder.getAbsolutePath() + "\\" + buffFile.getName());
            if (!buffFile.getAbsoluteFile().equals(destFile.getAbsoluteFile())) {

                if (!destFile.exists() || warningAlert(
                        "Вставка", "\"" + buffFile.getName() + "\" уже существует, заменить?")) {
                    delete(destFile, true);
                    copyFile(buffFile, destFile);
                    if (cutFlag) {
                        delete(buffFile, true);
                        buffFile = null;
                    }
                }
            }
        }
    }

    /**
     * Метод копирования файла.
     *
     * @param sourceFile - источник.
     * @param destFile   - место назначения.
     */
    private void copyFile(File sourceFile, File destFile) {
        try {
            Files.copy(sourceFile.toPath(), destFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Вывод диалога инфомации с кнопками да и нет.
     *
     * @param header   - заголовок.
     * @param question - вопрос диалога.
     * @return нажата ли кнопка да.
     */
    private boolean warningAlert(String header, String question) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Диалог информации");
        alert.setHeaderText(header);
        alert.setContentText(question);

        Optional<ButtonType> result = alert.showAndWait();
        return result.filter(buttonType -> buttonType == ButtonType.OK).isPresent();
    }

    @Override
    public boolean rename(File sourceFile, File destFile) {
        return !sourceFile.getAbsoluteFile().equals(destFile.getAbsoluteFile()) && sourceFile.renameTo(destFile);
    }

    /**
     * Метод для поиска свободного имени файла при занятом текущем.
     *
     * @param sourceFile - файл для проверки.
     * @return - доступный для создания файл.
     */
    private File findEmptyName(File sourceFile) {
        if (sourceFile.exists()) {
            String path = sourceFile.getParentFile().getPath();
            String name = FilenameUtils.getBaseName(sourceFile.getName());
            String ext = FilenameUtils.getExtension(sourceFile.getPath());
            for (int i = 1; i < 10; i++) {
                File newFile = new File(path + "\\" + name + " (" + i + ")" + "." + ext);
                if (!newFile.exists())
                    return newFile;
            }
            return null;
        } else
            return sourceFile;
    }

    @Override
    public boolean delete(File file) {
        return delete(file, false);
    }

    /**
     * Дополнительная функция для единообразной обработки внутреннего,
     * как например при вызове "cut", так и обычного удаления.
     * При отсутствии флага форсирования удаляет без вывода предупреждений.
     *
     * @param file  - файл для удаления.
     * @param force - флаг отвечающий за форсирование, т.е. удаление без запроса.
     */
    private boolean delete(File file, boolean force) {
        if (force)
            return file.delete();
        else if (warningAlert("Удаление", "Удалить \"" + file.getName() + "\"?"))
            return file.delete();
        return false;
    }

    @Override
    public boolean createFolderIn(File rootFolder) {
        File newFolder = new File(rootFolder.getAbsolutePath() + "\\Новая папка");
        newFolder = findEmptyName(newFolder);
        return newFolder != null && newFolder.mkdirs();
    }

    @Override
    public boolean isCopyOrCut() {
        return buffFile != null;
    }

    @Override
    public File getBuffFile() {
        return buffFile;
    }

    @Override
    public boolean isCutFlag() {
        return cutFlag;
    }

}
