package com.defaulty.explorer.control.rescontrol.image;

import javafx.scene.image.Image;

public class ImageSizePack {
    private Image small;
    private Image medium;
    private Image big;

    public enum ImageSize {
        SMALL, MEDIUM, BIG
    }

    public Image getImage(ImageSize size) {
        switch (size) {
            case SMALL:
                return small;
            case MEDIUM:
                return medium;
            case BIG:
                return big;
        }
        return null;
    }

    public Image getSmall() {
        return small;
    }

    public void setSmall(Image small) {
        this.small = small;
    }

    public Image getMedium() {
        return medium;
    }

    public void setMedium(Image medium) {
        this.medium = medium;
    }

    public Image getBig() {
        return big;
    }

    public void setBig(Image big) {
        this.big = big;
    }
}