package com.sudoplay.joise.examples.module;

import com.sudoplay.joise.examples.AbstractSplitExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.*;

/**
 * Created by codetaylor on 1/21/2017.
 */
public class ModuleSphereExample extends
    AbstractSplitExample {

  static {
    EXAMPLE_CLASS = ModuleSphereExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleSphere Example";
  }

  @Override
  protected void run(SplitCanvas canvas) {

    ModuleSphere moduleSphere = new ModuleSphere();
    moduleSphere.setCenterX(0.5);
    moduleSphere.setCenterY(0.5);
    moduleSphere.setRadius(0.5);

    canvas.updateImage(
        "source", moduleSphere
    );
  }

}
