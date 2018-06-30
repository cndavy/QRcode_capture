package com.han;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by han on 2018/6/30.
 */
public class Cavas extends JPanel {
    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    BufferedImage bufferedImage=null;

    public Cavas(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    public Cavas() {
    }

    public Cavas(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public Cavas(LayoutManager layout) {
        super(layout);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(bufferedImage,0,0, null);


    }
}
