package com.defaulty.explorer.model.tree;

import com.defaulty.explorer.control.observer.ViewConnectorModel;
import com.defaulty.explorer.control.rescontrol.files.FileOperations;
import com.defaulty.explorer.control.rescontrol.files.FileOperationsImpl;
import com.defaulty.explorer.control.rescontrol.image.FolderIcons;
import com.defaulty.explorer.control.rescontrol.image.ImageSetter;
import com.defaulty.explorer.model.item.FilteredTreeItem;
import com.defaulty.explorer.model.item.ItemStorageImpl;
import com.defaulty.explorer.model.search.SearchTask;
import com.defaulty.explorer.model.search.SearchTaskImpl;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.io.FileFilter;

/**
 * Класс поддерживающий контракт {@code TreeModel}.
 */
public class TreeModelImpl implements TreeModel {

    private ViewConnectorModel viewConnector;
    private FileOperations fo = new FileOperationsImpl();

    private ItemStorageImpl storage;
    private ImageSetter imageSetter = new ImageSetter();

    private boolean firstNode = true;

    public TreeModelImpl(FileFilter fileFilter) {
        storage = new ItemStorageImpl(fileFilter);
    }

    @Override
    public void setViewConnector(ViewConnectorModel viewConnector) {
        this.viewConnector = viewConnector;
    }

    @Override
    public synchronized void loadFork(File file) {
        if (file != null) {
            if (!storage.isHashed(file)) {
                imageSetter.setFolderImageView(file, FolderIcons.LOADABLE_FOLDER);
                viewConnector.changeState(storage.getTreeItem(file));
                new Thread(() -> {
                    storage.createChildren(file);
                    imageSetter.setFolderImageView(file, FolderIcons.CLOSE_FOLDER);
                    Platform.runLater(() -> {
                        viewConnector.changeState(storage.getTreeItem(file));
                        if(firstNode) {
                            firstNode = false;
                            viewConnector.changeFork(storage.getTreeItem(file));
                        }
                    });
                }).start();
            } else {
                new Thread(() -> {
                    storage.createChildren(file);
                    Platform.runLater(() -> viewConnector.changeFork(storage.getTreeItem(file)));
                }).start();
            }
        }
    }

    @Override
    public void treeSearch(File folder, String s) {
        if (s.equals(""))
            viewConnector.changeFork(storage.getTreeItem(folder));
        else {
            SearchTask searchTask = new SearchTaskImpl(folder, s, this::searchBackPoint);
            new Thread(searchTask).start();
            viewConnector.sendSearchTask(searchTask);
        }
    }

    /**
     * Точка возврата нового найденного элемента из задачи поиска .
     *
     * @param file - новый найденный элемент.
     */
    private synchronized void searchBackPoint(File file) {
        Platform.runLater(() -> viewConnector.addSearchNode(storage.getTreeItem(file)));
    }

    @Override
    public void open(File file) {
        try {
            fo.open(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cut(File sourceFile) {
        fo.cut(sourceFile);
        TreeItem<File> fork = storage.getTreeItem(sourceFile);
        removeTreeChild(fork, fork.getParent());
        loadFork(fork.getParent().getValue()); //TODO:?
    }

    @Override
    public void copy(File sourceFile) {
        fo.copy(sourceFile);
    }

    @Override
    public void paste(File destParentFolder) {
        try {
            fo.paste(destParentFolder);
            loadFork(destParentFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(File file) {
        fo.delete(file);
        TreeItem<File> fork = storage.getTreeItem(file);
        removeTreeChild(fork, fork.getParent());
        loadFork(fork.getParent().getValue()); //TODO:?
    }

    /**
     * Метод для удаления элемента из подэлементов другого элемента.
     *
     * @param item       - элемент для удаления.
     * @param itemParent - элемент контейнер.
     */
    private void removeTreeChild(TreeItem<File> item, TreeItem<File> itemParent) {
        itemParent.getChildren().remove(item);
        if (itemParent instanceof FilteredTreeItem) {
            FilteredTreeItem ftItem = (FilteredTreeItem) itemParent;
            ftItem.getFileChildren().remove(item);
        }
    }

    @Override
    public boolean rename(File sourceFile, File destFile) {
        return fo.rename(sourceFile, destFile);
    }

    @Override
    public boolean createFolderIn(File parentFolder) {
        if (parentFolder != null) {
            TreeItem<File> fork = storage.getTreeItem(parentFolder);
            if (fo.createFolderIn(fork.getValue())) {
                loadFork(parentFolder);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isCopyOrCut() {
        return fo.isCopyOrCut();
    }

}
