package com.defaulty.explorer.model.item;

import com.defaulty.explorer.control.rescontrol.image.FolderIcons;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;

/**
 * Класс поддерживающий контракт {@code ItemStorage}.
 * Обеспечивает кэширование элементов дерева и доступ к этому кэшу.
 */
public class ItemStorageImpl implements ItemStorage {

    private static HashMap<String, TreeItem<File>> treeHash = new HashMap<>();

    /**
     * Фильтр подгружаемых дочерних файлов .
     */
    private final FileFilter fileFilter;

    public ItemStorageImpl(FileFilter fileFilter) {
        this.fileFilter = fileFilter;
    }

    @Override
    public synchronized void createChildren(File file) {
        TreeItem<File> fork = getTreeItem(file);

        ObservableList<TreeItem<File>> folderChildren = FXCollections.observableArrayList();
        ObservableList<TreeItem<File>> fileChildren = FXCollections.observableArrayList();

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                TreeItem<File> parentItem = new TreeItem<>(file);
                for (File childFile : files) {
                    if (fileFilter.accept(childFile)) {
                        TreeItem<File> newFileItem = getTreeItem(childFile);
                        if (childFile.isDirectory()) {
                            folderChildren.add(newFileItem);
                        } else {
                            if (fork instanceof FilteredTreeItem) {
                                parentItem.getChildren().add(newFileItem);
                                fileChildren.add(newFileItem);
                            }
                        }
                    }
                }
            }
        }
        Platform.runLater(() -> {
            fork.getChildren().setAll(folderChildren);
            if (fork instanceof FilteredTreeItem) {
                FilteredTreeItem fti = (FilteredTreeItem) fork;
                fti.getFileChildren().setAll(fileChildren);
                fti.setIconType(FolderIcons.CLOSE_FOLDER);
            }
        });
    }

    @Override
    public TreeItem<File> getTreeItem(File file) {
        String absolutePath = file.getAbsolutePath();
        TreeItem<File> fork = treeHash.get(absolutePath);
        if (fork == null) {
            fork = new FilteredTreeItemImpl(file);
            treeHash.put(absolutePath, fork);
        }
        return fork;
    }

    @Override
    public boolean isHashed(File file) {
        TreeItem<File> item = treeHash.get(file.getAbsolutePath());
        return item != null && (!(item instanceof FilteredTreeItem)
                || !(((FilteredTreeItem) item).getIconType().lessThan(FolderIcons.CLOSE_FOLDER)));
    }

}
