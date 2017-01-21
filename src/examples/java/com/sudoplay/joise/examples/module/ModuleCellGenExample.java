package com.sudoplay.joise.examples.module;

import com.sudoplay.joise.examples.AbstractExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleCellGen;
import com.sudoplay.joise.module.ModuleCellular;
import com.sudoplay.joise.module.ModuleScaleDomain;

/**
 * Created by codetaylor on 1/9/2017.
 */
public class ModuleCellGenExample extends
    AbstractExample {

  static {
    EXAMPLE_CLASS = ModuleCellGenExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleCellGen Example";
  }

  @Override
  protected void run(SplitCanvas canvas) {

    ModuleCellGen moduleCellGen = new ModuleCellGen();
    moduleCellGen.setSeed(42);

    canvas.updateImage(
        "F1 = 1", getSource(moduleCellGen, 1, 0, 0, 0),
        "F2 = 1", getSource(moduleCellGen, 0, 1, 0, 0),
        "F1 = 1, F2 = -1", getSource(moduleCellGen, 1, -1, 0, 0),
        "F1 = -1, F2 = 1", getSource(moduleCellGen, -1, 1, 0, 0)
    );
  }

  private ModuleAutoCorrect getSource(ModuleCellGen moduleCellGen, int a, int b, int c, int d) {
    ModuleCellular moduleCellular = new ModuleCellular();
    moduleCellular.setCellularSource(moduleCellGen);
    moduleCellular.setCoefficients(a, b, c, d);

    /**
     * We scale the domain to visualize more of the noise.
     */
    ModuleScaleDomain moduleScaleDomain = new ModuleScaleDomain();
    moduleScaleDomain.setSource(moduleCellular);
    moduleScaleDomain.setScaleX(8);
    moduleScaleDomain.setScaleY(8);

    /**
     * We auto-correct into the range [0, 1].
     */
    ModuleAutoCorrect source = new ModuleAutoCorrect(0, 1);
    source.setSource(moduleScaleDomain);
    source.setSamples(10000);
    source.calculate();
    return source;
  }
}
