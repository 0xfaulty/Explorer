package com.defaulty.explorer.control.rescontrol.files;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

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
    public boolean paste(File destFile) {
        if (copyFile(buffFile, destFile)) {
            if (cutFlag) {
                delete(buffFile);
                buffFile = null;
            }
            return true;
        }
        return false;
    }

    /**
     * Метод копирования файла.
     *
     * @param sourceFile - источник.
     * @param destFile   - место назначения.
     */
    private boolean copyFile(File sourceFile, File destFile) {
        try {
            if (sourceFile.isDirectory())
                FileUtils.copyDirectory(sourceFile, destFile);
            else
                FileUtils.copyFile(sourceFile, destFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean rename(File sourceFile, File destFile) {
        if (buffFile != null) {
            if (sourceFile.getAbsoluteFile().equals(buffFile.getAbsoluteFile()))
                buffFile = null;
        }
        return sourceFile.renameTo(destFile);
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
        if (buffFile != null) {
            if (file.getAbsoluteFile().equals(buffFile.getAbsoluteFile()))
                buffFile = null;
        }
        if (file.isDirectory()) {
            try {
                FileUtils.deleteDirectory(file);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            return (file.delete());
        }
    }

    @Override
    public boolean createFolderIn(File rootFolder) {
        File newFolder = new File(rootFolder.getAbsolutePath() + "\\Новая папка");
        newFolder = findEmptyName(newFolder);
        return (newFolder != null && newFolder.mkdirs());
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
