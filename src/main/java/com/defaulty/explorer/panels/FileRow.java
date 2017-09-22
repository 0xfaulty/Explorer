package com.defaulty.explorer.panels;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class FileRow extends Pane{

    private BorderPane pane;
    private final SimpleStringProperty image;
    private final SimpleStringProperty size;
    private final SimpleStringProperty type;
    private final SimpleStringProperty data;

    public FileRow(String image, String size, String type, String data) {
        this.image = new SimpleStringProperty(image);
        this.size = new SimpleStringProperty(size);
        this.type = new SimpleStringProperty(type);
        this.data = new SimpleStringProperty(data);
    }

    public String getImage() {
        return image.get();
    }

    public void setImage(String fName) {
        image.set(fName);
    }

    public String getSize() {
        return size.get();
    }

    public void setSize(String fName) {
        size.set(fName);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String fName) {
        type.set(fName);
    }

    public String getData() {
        return data.get();
    }

    public void setData(String data) {
        this.data.set(data);
    }
}
