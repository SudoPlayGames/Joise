package com.sudoplay.joise.examples;

import javax.swing.*;

public abstract class AbstractSplitExample {

  protected static Class<? extends AbstractSplitExample> EXAMPLE_CLASS;

  protected abstract void run(SplitCanvas canvas);

  protected abstract String getWindowTitle();

  private void _run() {
    int width = 640;
    int height = 480;

    JFrame frame = new JFrame(this.getWindowTitle());

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
    AbstractSplitExample example = EXAMPLE_CLASS.newInstance();
    example._run();
  }

}
