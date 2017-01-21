package com.sudoplay.joise.examples.module;

import com.sudoplay.joise.examples.AbstractExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.*;

/**
 * Created by codetaylor on 1/9/2017.
 */
public class ModuleFloorExample extends
    AbstractExample {

  static {
    EXAMPLE_CLASS = ModuleFloorExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleFloor Example";
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
     * Since ModuleFloor returns the largest integer value smaller than the value sampled, we need to use the
     * ModuleCombiner to multiply the sampled values by a number in order to have a larger range that contains more
     * possible integer values.
     */
    ModuleCombiner source = new ModuleCombiner();
    source.setType(ModuleCombiner.CombinerType.MULT);
    source.setSource(0, moduleAutoCorrect);
    source.setSource(1, 8.0);

    ModuleFloor moduleFloor = new ModuleFloor();
    moduleFloor.setSource(source);

    /**
     * We auto-correct from the range [0, 8] back into the range of [0, 1] to be able to visualize the results.
     */
    ModuleAutoCorrect moduleFloorAutoCorrect = new ModuleAutoCorrect();
    moduleFloorAutoCorrect.setSource(moduleFloor);
    moduleFloorAutoCorrect.setSamples(10000);
    moduleFloorAutoCorrect.calculate();

    canvas.updateImage(
        "original source", moduleAutoCorrect,
        "source (x8)", source,
        "moduleFloor (auto-corrected)", moduleFloorAutoCorrect
    );
  }
}
