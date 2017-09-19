package com.defaulty.explorer.control;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * The class {@code ImageIconWrapper} представляет собой оболочку класса {@code ImageIcon}
 * с добавлением метода изменения размера изображения.
 */
public class ImageIconWrapper extends ImageIcon implements Icon {

    private ImageIcon icon;

    public ImageIconWrapper(ImageIcon icon) {
        super.setImage(icon.getImage());
        this.icon = icon;
    }

    public ImageIconWrapper(URL uri) {
        ImageIcon ii = new ImageIcon(uri);
        super.setImage(ii.getImage());
        this.icon = ii;
    }

    public ImageIconWrapper(InputStream is) throws IOException {
        icon = new ImageIcon(ImageIO.read(is));
    }

    public void setSize(int width, int height) {
        if (width < 0 || height < 0) throw new IllegalArgumentException("Width or height less zero");
        Dimension dimension = getNewDimension(icon.getIconWidth(), icon.getIconHeight(), width, height);
        super.setImage(new ImageIcon(
                icon.getImage().getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH)).getImage());
    }

    private Dimension getNewDimension(int sWidth, int sHeight, int width, int height) {
        int x, y;
        double a = (double) sWidth / (double) sHeight;
        double b = (double) width / (double) height;
        if (a > b) {
            x = width;
            y = (int) ((double) width / a);
        } else {
            x = (int) (height * a);
            y = height;
        }
        return new Dimension(x, y);
    }
}
