package com.defaulty.explorer.model.tree;

import com.defaulty.explorer.control.observer.ViewConnectorModel;
import com.defaulty.explorer.control.rescontrol.files.FileOperations;
import com.defaulty.explorer.control.rescontrol.files.FileOperationsImpl;
import com.defaulty.explorer.control.rescontrol.image.FolderIcons;
import com.defaulty.explorer.control.rescontrol.image.ImageSetter;
import com.defaulty.explorer.model.item.FilteredTreeItem;
import com.defaulty.explorer.model.search.SearchTask;
import com.defaulty.explorer.model.search.SearchTaskImpl;
import com.defaulty.explorer.model.storage.ItemStorageImpl;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.io.FileFilter;
import java.util.Optional;

/**
 * Класс поддерживающий контракт {@code TreeModel}.
 */
public class TreeModelImpl implements TreeModel {

    private ViewConnectorModel viewConnector;
    private FileOperations fo = new FileOperationsImpl();

    private ItemStorageImpl storage;
    private ImageSetter imageSetter = new ImageSetter();

    private boolean firstNode = true;
    private FileFilter fileFilter;

    public TreeModelImpl(FileFilter fileFilter) {
        this.fileFilter = fileFilter;
        storage = new ItemStorageImpl(fileFilter, this::itemChange);
    }

    @Override
    public void setViewConnector(ViewConnectorModel viewConnector) {
        this.viewConnector = viewConnector;
    }

    /**
     * Слушатель изменений в элементе.
     *
     * @param file - изменившийся элемент.
     */
    private void itemChange(File file) {
        TreeItem<File> item = storage.getTreeItem(file);
        if (item instanceof FilteredTreeItem)
            imageSetter.setFolderImageView(file, ((FilteredTreeItem) item).getIconType());
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
                        if (firstNode) {
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
            SearchTask searchTask = new SearchTaskImpl(folder, s, fileFilter, storage, this::searchBackPoint);
            new Thread(searchTask).start();
            viewConnector.sendSearchTask(searchTask);
        }
    }

    @Override
    public void loadSearchResults(SearchTask task) {
        viewConnector.changeFork(task.getResults());
    }

    /**
     * Точка возврата нового найденного элемента из задачи поиска .
     *
     * @param file - новый найденный элемент.
     */
    private synchronized void searchBackPoint(File file) {
        Platform.runLater(() -> viewConnector.addNode(storage.getTreeItem(file)));
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
    public void open(File file) {
        fo.open(file);
    }

    @Override
    public void cut(File sourceFile) {
        fo.cut(sourceFile);
    }

    @Override
    public void copy(File sourceFile) {
        fo.copy(sourceFile);
    }

    @Override
    public boolean paste(File destParentFolder) {
        File buffFile = fo.getBuffFile();
        File destFile = new File(destParentFolder.getAbsolutePath() + "\\" + buffFile.getName());

        if (!buffFile.getAbsoluteFile().equals(destFile.getAbsoluteFile())) {
            if (destFile.exists() && warningAlert(
                    "Вставка", "\"" + buffFile.getName() + "\" уже существует, заменить?")) {
                fo.delete(destFile);
            }
            if (fo.paste(destFile)) {
                if (fo.isCutFlag()) storage.removeItem(buffFile);
            }
            loadFork(destParentFolder);
        }

        return true;
    }

    @Override
    public boolean delete(File file) {
        if (warningAlert("Удаление", "Удалить \"" + file.getName() + "\"?")) {
            if (fo.delete(file)) {
                TreeItem<File> fork = storage.getTreeItem(file);
                File reloadFolder = fork.getParent().getValue();
                removeTreeChild(fork, fork.getParent());
                storage.removeItem(file);
                loadFork(reloadFolder);
                return true;
            }
        }
        return false;
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
        fo.rename(sourceFile, destFile);
        if (destFile.exists()) {
            storage.changeKey(sourceFile, destFile);
            TreeItem<File> item = storage.getTreeItem(destFile);
            if (item instanceof FilteredTreeItem)
                ((FilteredTreeItem) item).updateItem(destFile);
            else
                item.setValue(destFile);
            loadFork(sourceFile.getParentFile());
            return true;
        }
        return false;
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

    @Override
    public File getBuffFile() {
        return fo.getBuffFile();
    }

    @Override
    public boolean isCutFlag() {
        return fo.isCutFlag();
    }

}
