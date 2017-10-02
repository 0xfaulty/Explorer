package com.defaulty.explorer.panels;

import com.defaulty.explorer.control.IconSetter;
import com.defaulty.explorer.control.ThemeType;
import com.defaulty.explorer.control.ViewObserver;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.io.File;

public class FolderTree extends BorderPane implements ViewObserver {

    private final ViewObserver viewObserver;
    private final TreeTableView<File> treeTableView = new TreeTableView<>();

    public FolderTree(ViewObserver observer) {
        this.viewObserver = observer;
    }

    public void init() {
        TreeItem<File> root = new FolderTreeItemImpl(new File("/"));
        root.setExpanded(true);

        treeTableView.getStylesheets().addAll("css/hidden-headers.css");
        treeTableView.setShowRoot(true);
        treeTableView.setRoot(root);
        treeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        treeTableView.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (isArrowKeys(event)) {
                TreeItem<File> item = treeTableView.getSelectionModel().getSelectedItem();
                if (item != null && item instanceof FolderTreeItem) {
                    FolderTreeItem ftItem = (FolderTreeItem) item;
                    ftItem.createExpandTreeFork();
                    viewObserver.changeNode(item);
                }
            }
        });
        treeTableView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 1) {
                TreeItem<File> item = treeTableView.getSelectionModel().getSelectedItem();
                if (item != null && item instanceof FolderTreeItem) {
                    FolderTreeItem ftItem = (FolderTreeItem) item;
                    ftItem.createExpandTreeFork();
                    viewObserver.changeNode(item);
                }
            }
        });

        TreeTableColumn<File, TreeItem<File>> nameColumn = new TreeTableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue()));

        nameColumn.setCellFactory(new Callback<TreeTableColumn<File, TreeItem<File>>, TreeTableCell<File, TreeItem<File>>>() {
            @Override
            public TreeTableCell<File, TreeItem<File>> call(TreeTableColumn<File, TreeItem<File>> column) {
                return new TreeTableCell<File, TreeItem<File>>() {
                    @Override
                    protected void updateItem(TreeItem<File> item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty || item.getValue() == null) {
                            setText(null);
                            setGraphic(null);
                            setStyle("");
                        } else {
                            File f = item.getValue();
                            setText(f.getParentFile() == null ? f.getAbsolutePath() : f.getName());
                            setGraphic(IconSetter.getImageView(item));
                        }
                    }
                };
            }
        });
        nameColumn.setPrefWidth(300);
        nameColumn.setSortable(false);

        treeTableView.getColumns().add(nameColumn);

        viewObserver.changeNode(root);

        SplitPane.setResizableWithParent(this, false);

        this.setCenter(treeTableView);
        this.setMinWidth(200);
    }

    private boolean isArrowKeys(KeyEvent event) {
        return event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP ||
                event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT;
    }

    @Override
    public void changeNode(TreeItem<File> item) {
        //treeTableView.getSelectionModel().select(item.getFileTreeItem());
        //treeTableView.getFocusModel().focus(0);
        //System.out.println(treeNode.toString());
    }

    @Override
    public void setTheme(ThemeType t) {
        switch (t) {
            case DARK:
                //treeTableView.getStylesheets().setAll("css/table-dark.css");
                break;
            case LIGHT:
                //treeTableView.getStylesheets().setAll("css/table-light.css");
                break;
        }
    }

    @Override
    public void createFolder() {
    }

}
