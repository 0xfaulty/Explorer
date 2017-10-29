package com.defaulty.explorer.control.rescontrol;

import com.defaulty.explorer.control.rescontrol.files.FileOperations;
import com.defaulty.explorer.control.rescontrol.files.FileOperationsImpl;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileOperationsImplTest {

    private FileOperations fo = new FileOperationsImpl();

    @Ignore
    public void open() throws Exception {
        fo.open(new File("src/test/resources/test.txt"));
    }

    @Test
    public void cutPaste() throws Exception {
        File cutFile = new File("src/test/resources/test.txt");
        File pasteFile = new File("src/test/resources/folder/test.txt");

        fo.cut(cutFile);
        fo.paste(pasteFile);
        assertTrue(pasteFile.exists());

        fo.cut(pasteFile);
        fo.paste(cutFile);
        assertTrue(cutFile.exists());
    }

    @Test
    public void copyPaste() throws Exception {
        File copyFile = new File("src/test/resources/test.txt");
        File pasteFile = new File("src/test/resources/folder/test.txt");

        fo.copy(copyFile);
        fo.paste(pasteFile);
        assertTrue(pasteFile.exists());
        fo.delete(pasteFile);
        assertTrue(copyFile.exists());
        assertFalse(pasteFile.exists());
    }

    @Test
    public void rename() throws Exception {
        File renameFromFile = new File("src/test/resources/test.txt");
        File renameToFile = new File("src/test/resources/test2.txt");
        assertTrue(fo.rename(renameFromFile, renameToFile));
        assertTrue(fo.rename(renameToFile, renameFromFile));
    }

    @Test
    public void createFolder() throws Exception {
        File folder = new File("src/test/resources/createFolderIn");
        assertTrue(fo.createFolderIn(folder));
        assertTrue(folder.exists());
        assertTrue(folder.isDirectory());
        fo.delete(folder);
    }

}