package com.sudoplay.joise.examples;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractExample {

  protected static Class<? extends AbstractExample> EXAMPLE_CLASS;

  protected abstract void run(SplitCanvas canvas);

  protected abstract String getWindowTitle();

  private void _run() {
    int width = 640;
    int height = 480;

    JFrame frame = new JFrame(this.getWindowTitle());
    //frame.setPreferredSize(new Dimension(width, height));

    SplitCanvas canvas = new SplitCanvas(width, height);
    frame.add(canvas);

    frame.setResizable(false);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    this.run(canvas);

    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  public static void main(String[] args) throws IllegalAccessException, InstantiationException {
    AbstractExample example = EXAMPLE_CLASS.newInstance();
    example._run();
  }

}
