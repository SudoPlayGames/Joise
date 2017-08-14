package com.sudoplay.joise.examples;

import com.sudoplay.joise.mapping.Array2Double;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@SuppressWarnings({"serial", "WeakerAccess"})
public class MappingCanvas extends
    JPanel {

  private BufferedImage image;

  MappingCanvas(int width, int height) {
    this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  }

  void updateImage(Array2Double array) {
    int width = this.image.getWidth();
    int height = this.image.getHeight();
    float r;

    for (int x = 0; x < width; x++) {

      for (int y = 0; y < height; y++) {

        /*
         * Sample the module chain like this...
         */
        r = (float) array.get(x, y);

        r = Math.max(0, Math.min(1, r));
        this.image.setRGB(x, y, new Color(r, r, r).getRGB());
      }
    }
    this.repaint();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.drawImage(this.image, null, null);
    g2.dispose();
  }

}
