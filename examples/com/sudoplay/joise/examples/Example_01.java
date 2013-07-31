package com.sudoplay.joise.examples;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sudoplay.joise.module.Module;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction.BasisType;
import com.sudoplay.joise.module.ModuleBasisFunction.InterpolationType;
import com.sudoplay.joise.module.ModuleFractal;
import com.sudoplay.joise.module.ModuleFractal.FractalType;

@SuppressWarnings("serial")
class Canvas extends JPanel {
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

public class Example_01 {

  public static void main(String[] args) {
    int width = 640;
    int height = 480;

    JFrame frame = new JFrame("Joise Example 01");
    frame.setPreferredSize(new Dimension(width, height));

    Canvas canvas = new Canvas(width, height);
    frame.add(canvas);

    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // ========================================================================
    // = Joise module chain
    // ========================================================================

    /*
     * Start with a fractal generator...
     */
    ModuleFractal gen = new ModuleFractal();
    gen.setAllSourceBasisTypes(BasisType.SIMPLEX);
    gen.setAllSourceInterpolationTypes(InterpolationType.CUBIC);
    gen.setNumOctaves(5);
    gen.setFrequency(2.34);
    gen.setType(FractalType.RIDGEMULTI);
    gen.setSeed(898456);

    /*
     * ... route it through an autocorrection module...
     * 
     * This module will sample it's source multiple times and attempt to
     * auto-correct the output to the range specified.
     */
    ModuleAutoCorrect ac = new ModuleAutoCorrect();
    ac.setSource(gen); // set source (can usually be either another Module or a
                       // double value; see specific module for details)
    ac.setRange(0.0f, 1.0f); // set the range to auto-correct to
    ac.setSamples(10000); // set how many samples to take
    ac.calculate(); // perform the caclulations

    /*
     * ... draw it.
     */
    canvas.updateImage(ac);

    frame.pack();
    frame.setLocationRelativeTo(null);
  }

}
