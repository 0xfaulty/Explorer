package com.defaulty.explorer.control.rescontrol.image;

import javafx.scene.image.Image;

/**
 * Контейнер изображения трёх размеров.
 */
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

    public void setSmall(Image small) {
        this.small = small;
    }

    public void setMedium(Image medium) {
        this.medium = medium;
    }

    public void setBig(Image big) {
        this.big = big;
    }
}