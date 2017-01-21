package com.sudoplay.joise.examples.module;

import com.sudoplay.joise.examples.AbstractExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.ModuleAbs;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction;
import com.sudoplay.joise.module.ModuleFractal;

/**
 * Created by codetaylor on 1/8/2017.
 */
public class ModuleAbsExample extends
    AbstractExample {

  static {
    EXAMPLE_CLASS = ModuleAbsExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleAbs Example";
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
     * We auto-correct into the range [-1, 1] to ensure we have some negative values for the abs module.
     */
    ModuleAutoCorrect source = new ModuleAutoCorrect(-1, 1);
    source.setSource(gen);
    source.setSamples(10000);
    source.calculate();

    /**
     * Performs the absolute value function on the noise.
     */
    ModuleAbs moduleAbs = new ModuleAbs();
    moduleAbs.setSource(source);

    canvas.updateImage(
        "source", source,
        "moduleAbs", moduleAbs
    );
  }
}
