package com.defaulty.explorer.model.tree;

import com.defaulty.explorer.control.observer.ViewConnectorModel;
import com.defaulty.explorer.control.rescontrol.files.FileOperations;
import com.defaulty.explorer.control.rescontrol.files.FileOperationsImpl;
import com.defaulty.explorer.control.rescontrol.image.FolderIcons;
import com.defaulty.explorer.model.item.FilteredTreeItem;
import com.defaulty.explorer.model.search.SearchTask;
import com.defaulty.explorer.model.search.SearchTaskImpl;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;

import java.io.File;

/**
 * Класс поддерживающий контракт {@code TreeModel}.
 */
public class TreeModelImpl implements TreeModel {

    private ViewConnectorModel viewConnector;
    private FileOperations fo = new FileOperationsImpl();

    private boolean loadedRoot;

    @Override
    public void setViewConnector(ViewConnectorModel viewConnector) {
        this.viewConnector = viewConnector;
    }

    @Override
    public synchronized void loadFork(TreeItem<File> fork) {
        if (fork != null && fork instanceof FilteredTreeItem) {
            FilteredTreeItem ftItem = (FilteredTreeItem) fork;
            if (ftItem.getIconType().lessThan(FolderIcons.LOADABLE_FOLDER)) {
                ftItem.setIconType(FolderIcons.LOADABLE_FOLDER);
                viewConnector.changeState(fork);
                new Thread(() -> {
                    ftItem.createChildren();
                    finishBackPoint(fork);
                }).start();
            }
            if (ftItem.getIconType() != FolderIcons.LOADABLE_FOLDER) {
                new Thread(() -> {
                    ftItem.createChildren();
                    finishBackPoint(fork);
                }).start();
            }
        }
    }

    /**
     * Точка возврата из задачи загрузки подэлементов некоторого узла.
     * Загруженные в отдельном потоке элементы добавляются в главный для них сектр.
     * Используется для избежания Concurrent Modification Exception.
     *
     * @param loadedFork - узел с загруженными подэлементами.
     */
    private synchronized void finishBackPoint(TreeItem<File> loadedFork) {
        Platform.runLater(() -> {
            if (loadedFork != null && loadedFork instanceof FilteredTreeItem) {
                FilteredTreeItem ftItem = (FilteredTreeItem) loadedFork;
                ftItem.setChildren(ftItem.getFolderChildren());
                if (ftItem.getIconType().lessThan(FolderIcons.CLOSE_FOLDER)) {
                    ftItem.setIconType(FolderIcons.CLOSE_FOLDER);
                    viewConnector.changeState(loadedFork);
                    if (!loadedRoot) {
                        loadedRoot = true;
                        viewConnector.changeFork(loadedFork);
                    }
                } else
                    viewConnector.changeFork(loadedFork);
            }
        });
    }

    @Override
    public void treeSearch(TreeItem<File> currentFork, String s) {
        if (s.equals(""))
            viewConnector.changeFork(currentFork);
        else {
            System.out.println("Search: " + s);
            SearchTask searchTask = new SearchTaskImpl(currentFork, s, this::searchBackPoint);
            new Thread(searchTask).start();
            viewConnector.sendSearchTask(searchTask);
        }
    }

    /**
     * Точка возврата нового найденного элемента из задачи поиска .
     *
     * @param item - новый найденный элемент.
     */
    private synchronized void searchBackPoint(TreeItem<File> item) {
        Platform.runLater(() -> viewConnector.addSearchNode(item));
    }

    @Override
    public void createFolderIn(TreeItem<File> parentItem) {
        if (parentItem != null) {
            if (fo.createFolderIn(parentItem.getValue()))
                loadFork(parentItem);
        }
    }

    @Override
    public void open(TreeItem<File> item) {
        try {
            fo.open(item.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cut(TreeItem<File> item, TreeItem<File> itemParent) {
        fo.cut(item.getValue());
        removeTreeChild(item, itemParent);
        loadFork(itemParent);
    }

    @Override
    public void copy(TreeItem<File> item) {
        fo.copy(item.getValue());
    }

    @Override
    public void paste(TreeItem<File> itemParent) {
        try {
            fo.paste(itemParent.getValue());
            loadFork(itemParent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(TreeItem<File> item, TreeItem<File> itemParent) {
        fo.delete(item.getValue());
        removeTreeChild(item, itemParent);
        loadFork(itemParent);
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
            ftItem.removeFileChildren(item);
        }
    }

    @Override
    public boolean isCopyOrCut() {
        return fo.isCopyOrCut();
    }

}
