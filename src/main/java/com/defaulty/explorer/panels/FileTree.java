package com.defaulty.explorer.panels;

import com.defaulty.explorer.App;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.File;

public class FileTree extends BorderPane {

    public FileTree() {
        String startDir = System.getProperty("user.dir");
        FileTreeItem root = new FileTreeItem(new File(startDir));
        root.setExpanded(true);

        final TreeTableView<File> treeTableView = new TreeTableView<>();
        treeTableView.getStylesheets().addAll("css/hidden-headers.css");
        treeTableView.setShowRoot(true);
        treeTableView.setRoot(root);
        treeTableView.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

        TreeTableColumn<File, FileTreeItem> nameColumn = new TreeTableColumn<>("Name");

        nameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>((FileTreeItem) cellData.getValue())
        );

        nameColumn.setCellFactory(column -> new TreeTableCell<File, FileTreeItem>() {

            ImageView imageOpenDir = new ImageView(getImageResource("icons/closef.png"));
            ImageView imageCloseDir = new ImageView(getImageResource("icons/closef.png"));

            @Override
            protected void updateItem(FileTreeItem item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty || item.getValue() == null || !item.isDirectory()) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    File f = item.getValue();
                    String text = f.getParentFile() == null ? File.separator : f.getName();
                    setText(text);

                    setGraphic(item.isExpanded() ? imageOpenDir : imageCloseDir);
                }
            }
        });

        nameColumn.setPrefWidth(300);
        nameColumn.setSortable(false);
        treeTableView.getColumns().add(nameColumn);

        this.setCenter(treeTableView);
        this.setMinWidth(200);
    }

    private Image getImageResource(String name) {
        Image img = null;
        try {
            img = new Image(App.class.getClassLoader().getResourceAsStream(name),
                    20, 20, true, true);
        } catch (Exception e) {
        }
        return img;
    }

}
