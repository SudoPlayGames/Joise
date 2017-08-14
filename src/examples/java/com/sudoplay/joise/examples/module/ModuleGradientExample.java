package com.sudoplay.joise.examples.module;

import com.sudoplay.joise.examples.AbstractSplitExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.*;

/**
 * Created by codetaylor on 1/11/2017.
 */
public class ModuleGradientExample extends
    AbstractSplitExample {

  static {
    EXAMPLE_CLASS = ModuleGradientExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleGradient Example";
  }

  @Override
  protected void run(SplitCanvas canvas) {

    canvas.updateImage(
        "(0, 0) -> (0, 1)", getModule(0, 0, 0, 1),
        "(0.5, 0) -> (1, 1)", getModule(0.5, 1, 0, 1)
    );
  }

  private ModuleGradient getModule(double x1, double x2, double y1, double y2) {
    ModuleGradient moduleGradient = new ModuleGradient();
    moduleGradient.setGradient(x1, x2, y1, y2);
    return moduleGradient;
  }

}
