package com.sudoplay.joise.examples;

import com.sudoplay.joise.module.Module;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@SuppressWarnings({"serial", "WeakerAccess"})
public class Canvas extends
    JPanel {

  private static final float SCALE = 1.0f;
  private BufferedImage image;

  Canvas(int width, int height) {
    this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  }

  void updateImage(Module mod) {
    int width = this.image.getWidth();
    int height = this.image.getHeight();
    float px, py, r;

    for (int x = 0; x < width; x++) {

      for (int y = 0; y < height; y++) {
        px = x / (float) width * SCALE;
        py = y / (float) height * SCALE;

        /*
         * Sample the module chain like this...
         */
        r = (float) mod.get(px, py);

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
