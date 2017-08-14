package com.sudoplay.joise.examples.module;

import com.sudoplay.joise.examples.AbstractSplitExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.*;

/**
 * Created by codetaylor on 1/11/2017.
 */
public class ModuleInvertExample extends
    AbstractSplitExample {

  static {
    EXAMPLE_CLASS = ModuleInvertExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleInvert Example";
  }

  @Override
  protected void run(SplitCanvas canvas) {

    ModuleFractal gen = new ModuleFractal();
    gen.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.SIMPLEX);
    gen.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
    gen.setNumOctaves(5);
    gen.setFrequency(2.34);
    gen.setType(ModuleFractal.FractalType.RIDGEMULTI);
    gen.setSeed(42);

    /**
     * We auto-correct into the range [0, 1], performing 10,000 samples to calculate the auto-correct values.
     */
    ModuleAutoCorrect moduleAutoCorrect = new ModuleAutoCorrect(0, 1);
    moduleAutoCorrect.setSource(gen);
    moduleAutoCorrect.setSamples(10000);
    moduleAutoCorrect.calculate();

    /**
     * Inverting brings the range from [0, 1] into [-1, 0]
     */
    ModuleInvert moduleInvert = new ModuleInvert();
    moduleInvert.setSource(moduleAutoCorrect);

    /**
     * Add 1.0 to bring the range from [-1, 0] back to [0, 1]
     */
    ModuleCombiner moduleCombiner = new ModuleCombiner();
    moduleCombiner.setSource(0, moduleInvert);
    moduleCombiner.setSource(1, 1.0);
    moduleCombiner.setType(ModuleCombiner.CombinerType.ADD);

    canvas.updateImage(
        "source", moduleAutoCorrect,
        "invert", moduleInvert,
        "corrected invert", moduleCombiner
    );
  }

}
