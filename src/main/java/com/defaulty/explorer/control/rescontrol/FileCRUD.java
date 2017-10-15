package com.defaulty.explorer.control.rescontrol;

import org.apache.commons.io.FilenameUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FileCRUD {

    private File sourceFile = null;
    private File sourceRename = null;
    private boolean cutFlag;

    public void open(File file) throws Exception {
        Desktop.getDesktop().open(file);
    }

    public void cut(File sourceFile) throws Exception {
        this.sourceFile = sourceFile;
        this.cutFlag = true;
    }

    public void copy(File sourceFile) throws Exception {
        this.sourceFile = sourceFile;
        this.cutFlag = false;
    }

    public void past(File destFile) throws Exception {
        if (sourceFile != null) {
            copyFile(sourceFile, destFile);
            if (cutFlag) {
                delete(sourceFile);
                sourceFile = null;
            }
        }
    }

    public void rename(File destFile) throws Exception {
        if (!sourceFile.renameTo(destFile)) {
            File newFile = findEmptyName(sourceFile);
        }
    }

    public void renameSet(File renameFile) throws Exception {
        this.sourceRename = renameFile;
    }

    private File findEmptyName(File sourceFile) {
        String path = sourceFile.getParentFile().getPath();
        String name = FilenameUtils.getBaseName(sourceFile.getName());
        String ext = FilenameUtils.getExtension(sourceFile.getPath());
        for (int i = 1; i < 10; i++) {
            File newFile = new File(path + "\\" + name + " (" + i + ")" + "." + ext);
            if (!newFile.exists())
                return newFile;
        }
        return null;
    }

    public void copyFile(File sourceFile, File destFile) throws Exception {
        if (!destFile.exists()) {
            destFile.createNewFile();
        } else {
//            File exDestFilePath = new File(destFile.getPath());
//            String ext = FilenameUtils.getExtension(destFile.getPath());
//            System.out.println(exDestFilePath. +  "Adv" + ext);
        }


//        FileChannel source = null;
//        FileChannel destination = null;
//        try {
//            source = new RandomAccessFile(sourceFile, "rw").getChannel();
//            destination = new RandomAccessFile(destFile, "rw").getChannel();
//
//            long position = 0;
//            long count = source.size();
//
//            source.transferTo(position, count, destination);
//        } finally {
//            if (source != null) {
//                source.close();
//            }
//            if (destination != null) {
//                destination.close();
//            }
//        }
    }

    public void delete(File file) throws Exception {
        //if (!file.delete()) throw new IOException("Delete operation is failed.");
        System.out.println("Try delete " + file.getPath());
    }

    public void createFolder(File file) throws Exception {
        if (!file.mkdirs()) throw new IOException("Create folder operation is failed.");
    }

    public boolean isCopyOrCup() {
        return sourceFile != null;
    }

}
