package com.defaulty.explorer.model;

import com.defaulty.explorer.control.observer.ViewConnectorModel;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TreeModelImpl implements TreeModel {

    private TreeItem<File> rootItem;
    private ViewConnectorModel viewConnector;

    public TreeModelImpl(File root) {
        rootItem = new FilteredTreeItemImpl(root, e -> !e.isHidden());
    }

    public synchronized void loadFork(TreeItem<File> fork, boolean checkout) {
        if (fork != null && fork instanceof FilteredTreeItem) {
            FilteredTreeItem ftItem = (FilteredTreeItem) fork;
            if (ftItem.getIconType().moreThen(FilteredTreeItem.IconType.LOADABLE)) {
                ftItem.setIconType(FilteredTreeItem.IconType.LOADABLE);
                viewConnector.changeState(fork);
                new TreeThread(fork, ftItem::createChildren, this::finishBackPoint).start();
            } else viewConnector.changeFork(fork);
        }
    }

    private synchronized void finishBackPoint(TreeItem<File> loadedFork) {
        Platform.runLater(() -> {
            if (loadedFork != null && loadedFork instanceof FilteredTreeItem) {
                FilteredTreeItem ftItem = (FilteredTreeItem) loadedFork;
                ftItem.setChildren(ftItem.getFolderChildren());
                ftItem.setIconType(FilteredTreeItem.IconType.LOADED);
                viewConnector.changeState(loadedFork);
            }
        });
    }

    @Override
    public void setViewConnector(ViewConnectorModel viewConnector) {
        this.viewConnector = viewConnector;
        this.viewConnector.changeFork(rootItem);
    }

    @Override
    public void treeSearch(TreeItem<File> item, String s) {
        if (s.equals(""))
            viewConnector.changeFork(item);
        else {
            System.out.println("Search: " + s);
            new TreeThread(item, (e) -> searchProcess(item, s), this::finishBackPoint).start();
        }
    }

    private void searchProcess(TreeItem<File> root, String s) {
        TreeItem<File> item = new TreeItem<>();
        List<File> fileList = recurseSearch(root.getValue(), s);
        for (File file : fileList)
            item.getChildren().add(new FilteredTreeItemImpl(file, e -> !e.isHidden()));

        searchBackPoint(item);
    }

    private List<File> recurseSearch(File node, String s) {
        List<File> res = new ArrayList<>();
        if (node != null) {
            if (node.getName().toLowerCase().indexOf(s.toLowerCase()) > 0) res.add(node);
            //if (node.getName().toLowerCase().matches("")) res.add(node);
            File[] files = node.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files)
                    res.addAll(recurseSearch(file, s));
            }
        }
        return res;
    }

    private synchronized void searchBackPoint(TreeItem<File> item) {
        Platform.runLater(() -> viewConnector.changeFork(item));
    }

}
