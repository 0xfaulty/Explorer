package com.defaulty.explorer.model.search;

import com.defaulty.explorer.model.storage.ItemStorage;
import com.defaulty.explorer.model.tree.TreeBackPoint;
import javafx.application.Platform;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс поддерживающий контракт {@code SearchTask} и {@code Runnable}
 */
public class SearchTaskImpl implements Runnable, SearchTask {

    private final File root;
    private final String string;
    private final FileFilter fileFilter;
    private TreeBackPoint backPoint;

    private final ItemStorage mainStorage;

    /**
     * Список слушателей события увеличения счётчика найденных элементов.
     */
    private List<Runnable> counterInputs = new ArrayList<>();

    private int counter = 0;
    private boolean stopFlag = false;

    private final TreeItem<File> results = new TreeItem<>();

    public SearchTaskImpl(File root, String string, FileFilter fileFilter, ItemStorage mainStorage, TreeBackPoint backPoint) {
        this.root = root;
        this.string = string;
        this.fileFilter = fileFilter;
        this.mainStorage = mainStorage;
        this.backPoint = backPoint;
    }

    public void run() {
        recurseSearch(root, string);
    }

    /**
     * Куруксивный поиск по дочерним элементам.
     *
     * @param node - текущий элемент.
     * @param s    - строка поиска.
     */
    private void recurseSearch(File node, String s) {
        if (stopFlag) return;
        if (node != null) {
            if (fileFilter.accept(node)) {
                if (node.getName().toLowerCase().indexOf(s.toLowerCase()) > 0) {
                    backPoint.accept(node);
                    results.getChildren().add(mainStorage.getTreeItem(node));
                    counter++;
                    Platform.runLater(() -> {
                                for (Runnable r : counterInputs)
                                    r.run();
                            }
                    );
                }
            }
            File[] files = node.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    recurseSearch(file, s);
                }
            }
        }
    }

    public String getTaskFullName() {
        return "Search:" + string;
    }

    @Override
    public int getDoneCount() {
        return counter;
    }

    @Override
    public String getKeyWord() {
        return string;
    }

    @Override
    public void stop() {
        stopFlag = true;
    }

    @Override
    public boolean isFinished() {
        return stopFlag;
    }

    @Override
    public void addCountListener(Runnable counterInput) {
        this.counterInputs.add(counterInput);
    }

    @Override
    public TreeItem<File> getResults() {
        return results;
    }

}
