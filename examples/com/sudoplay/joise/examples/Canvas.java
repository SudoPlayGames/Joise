package com.sudoplay.joise.examples;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.sudoplay.joise.module.Module;

@SuppressWarnings("serial")
public class Canvas extends JPanel {

  private static final float SCALE = 1.0f;
  private BufferedImage image;

  Canvas(int width, int height) {
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  }

  void updateImage(Module mod) {
    int width = image.getWidth();
    int height = image.getHeight();
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
        image.setRGB(x, y, new Color(r, r, r).getRGB());
      }
    }
    repaint();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.drawImage(image, null, null);
    g2.dispose();
  }

}
