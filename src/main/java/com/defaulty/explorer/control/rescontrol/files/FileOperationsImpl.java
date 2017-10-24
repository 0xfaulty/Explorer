package com.defaulty.explorer.control.rescontrol.files;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.commons.io.FilenameUtils;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.Optional;

/**
 * Класс поддерживающий контракт {@code FileOperations}.
 */
public class FileOperationsImpl implements FileOperations {

    private File sourceFile = null;
    private boolean cutFlag;

    @Override
    public void open(File file) throws Exception {
        Desktop.getDesktop().open(file);
    }

    @Override
    public void cut(File sourceFile) {
        this.sourceFile = sourceFile;
        this.cutFlag = true;
    }

    @Override
    public void copy(File sourceFile) {
        this.sourceFile = sourceFile;
        this.cutFlag = false;
    }

    @Override
    public void paste(File destParentFolder) throws Exception {
        if (sourceFile != null && destParentFolder.isDirectory()) {
            File destFile = new File(destParentFolder.getAbsolutePath() + "\\" + sourceFile.getName());
            if (!sourceFile.getAbsoluteFile().equals(destFile.getAbsoluteFile())) {

                if (!destFile.exists() || warningAlert(
                        "Вставка", "Файл \"" + sourceFile.getName() + "\" уже существует, заменить?")) {
                    delete(destFile, true);
                    copyFile(sourceFile, destFile);
                    if (cutFlag) {
                        delete(sourceFile, true);
                        sourceFile = null;
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
     * @throws Exception - ошибки при копировании.
     */
    private void copyFile(File sourceFile, File destFile) throws Exception {
        Files.copy(sourceFile.toPath(), destFile.toPath());
    }

    private boolean warningAlert(String header, String question) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Диалог информации");
        alert.setHeaderText(header);
        alert.setContentText(question);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    @Override
    public boolean rename(File sourceFile, File destFile) {
        File newDestFile = findEmptyName(destFile);
        return newDestFile != null && sourceFile.renameTo(newDestFile);
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
    public void delete(File file) {
        delete(file, false);
    }

    /**
     * Дополнительная функция для единообразной обработки внутреннего,
     * как например при вызове "cut", так и обычного удаления.
     * При отсутствии флага форсирования удаляет без вывода предупреждений.
     *
     * @param file  - файл для удаления.
     * @param force - флаг отвечающий за форсирование, т.е. удаление без запроса.
     */
    private void delete(File file, boolean force) {
        if (force)
            file.delete();
        else if (warningAlert("Удаление", "Удалить \"" + file.getName() + "\"?"))
            file.delete();
    }

    @Override
    public boolean createFolderIn(File rootFolder) {
        File newFolder = new File(rootFolder.getAbsolutePath() + "\\Новая папка");
        newFolder = findEmptyName(newFolder);
        return newFolder != null && newFolder.mkdirs();
    }

    @Override
    public boolean isCopyOrCut() {
        return sourceFile != null;
    }

}
