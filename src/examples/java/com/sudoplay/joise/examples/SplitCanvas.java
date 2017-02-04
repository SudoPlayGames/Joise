package com.sudoplay.joise.examples;

import com.sudoplay.joise.module.Module;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SplitCanvas extends JPanel {

  private static final float SCALE = 1.0f;

  private BufferedImage image;
  private List<LabelProperties> labelPropertiesList;

  private class LabelProperties {
    String text;
    int x;
    int y;

    LabelProperties(String text, int x, int y) {
      this.text = text;
      this.x = x;
      this.y = y;
    }
  }

  SplitCanvas(int width, int height) {
    this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    this.setPreferredSize(new Dimension(width, height));
    this.labelPropertiesList = new ArrayList<LabelProperties>();
  }

  public void updateImage(
      String label, Module module
  ) {
    int width = this.image.getWidth();
    int height = this.image.getHeight();

    this.draw(module, 0, width, 0, height, width, height);

    this.labelPropertiesList.add(new LabelProperties(label, 0, 0));

    this.repaint();
  }

  public void updateImage(
      String leftLabel, Module leftModule,
      String rightLabel, Module rightModule
  ) {
    int width = this.image.getWidth();
    int height = this.image.getHeight();

    this.draw(leftModule, 0, width >> 1, 0, height, width, height);
    this.draw(rightModule, width >> 1, width, 0, height, width, height);

    this.labelPropertiesList.add(new LabelProperties(leftLabel, 0, 0));
    this.labelPropertiesList.add(new LabelProperties(rightLabel, width >> 1, 0));

    this.repaint();
  }

  public void updateImage(
      String leftTopLabel, Module leftTopModule,
      String leftBottomLabel, Module leftBottomModule,
      String rightLabel, Module rightModule
  ) {
    int width = this.image.getWidth();
    int height = this.image.getHeight();

    this.draw(leftTopModule, 0, width >> 1, 0, height >> 1, width, height);
    this.draw(leftBottomModule, 0, width >> 1, height >> 1, height, width, height);
    this.draw(rightModule, width >> 1, width, 0, height, width, height);

    this.labelPropertiesList.add(new LabelProperties(leftTopLabel, 0, 0));
    this.labelPropertiesList.add(new LabelProperties(leftBottomLabel, 0, height >> 1));
    this.labelPropertiesList.add(new LabelProperties(rightLabel, width >> 1, 0));

    this.repaint();
  }

  public void updateImage(
      String leftTopLabel, Module leftTopModule,
      String leftBottomLabel, Module leftBottomModule,
      String rightTopLabel, Module rightTopModule,
      String rightBottomLabel, Module rightBottomModule
  ) {
    int width = this.image.getWidth();
    int height = this.image.getHeight();

    this.draw(leftTopModule, 0, width >> 1, 0, height >> 1, width, height);
    this.draw(leftBottomModule, 0, width >> 1, height >> 1, height, width, height);

    this.draw(rightTopModule, width >> 1, width, 0, height >> 1, width, height);
    this.draw(rightBottomModule, width >> 1, width, height >> 1, height, width, height);

    this.labelPropertiesList.add(new LabelProperties(leftTopLabel, 0, 0));
    this.labelPropertiesList.add(new LabelProperties(leftBottomLabel, 0, height >> 1));

    this.labelPropertiesList.add(new LabelProperties(rightTopLabel, width >> 1, 0));
    this.labelPropertiesList.add(new LabelProperties(rightBottomLabel, width >> 1, height >> 1));

    this.repaint();
  }

  private void draw(Module module, int xStart, int xEnd, int yStart, int yEnd, int width, int height) {
    float px;
    float py;
    float r;
    for (int x = xStart; x < xEnd; x++) {

      for (int y = yStart; y < yEnd; y++) {
        px = x / (float) width * SCALE;
        py = y / (float) height * SCALE;
        r = (float) module.get(px, py);

        if (r < 0) {
          r = Math.min(Math.abs(r), 1);
          this.image.setRGB(x, y, new Color(r, 0, 0).getRGB());

        } else if (r > 1) {
          r = Math.min(r - 1, 1);
          this.image.setRGB(x, y, new Color(0, 0, r).getRGB());

        } else {
          r = Math.max(0, Math.min(1, r));
          this.image.setRGB(x, y, new Color(r, r, r).getRGB());
        }
      }
    }
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.drawImage(this.image, null, null);

    for (LabelProperties labelProperties : this.labelPropertiesList) {
      this.drawLabel(g2, labelProperties.text, labelProperties.x, labelProperties.y);
    }

    g2.dispose();
  }

  private void drawLabel(Graphics2D g2, String label, int x, int y) {
    int padding = 4;
    FontMetrics fontMetrics = g2.getFontMetrics();
    Rectangle2D rectangle2D = fontMetrics.getStringBounds(label, g2);

    x += 10;
    y += 24;

    g2.setColor(Color.BLACK);
    g2.fillRect(
        x - padding,
        y - fontMetrics.getAscent() - padding,
        (int) rectangle2D.getWidth() + (padding << 1),
        (int) rectangle2D.getHeight() + (padding << 1)
    );

    g2.setColor(Color.WHITE);
    g2.drawRect(
        x - padding,
        y - fontMetrics.getAscent() - padding,
        (int) rectangle2D.getWidth() + (padding << 1),
        (int) rectangle2D.getHeight() + (padding << 1)
    );

    g2.setColor(Color.WHITE);
    g2.drawString(label, x, y);
  }

}
