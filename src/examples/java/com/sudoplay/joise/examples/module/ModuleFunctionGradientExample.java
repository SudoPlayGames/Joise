package com.sudoplay.joise.examples.module;

import com.sudoplay.joise.examples.AbstractSplitExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction;
import com.sudoplay.joise.module.ModuleFractal;
import com.sudoplay.joise.module.ModuleFunctionGradient;

/**
 * Created by codetaylor on 1/11/2017.
 */
public class ModuleFunctionGradientExample extends
    AbstractSplitExample {

  static {
    EXAMPLE_CLASS = ModuleFunctionGradientExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleFunctionGradient Example";
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

    ModuleFunctionGradient moduleFunctionGradient = new ModuleFunctionGradient();
    moduleFunctionGradient.setSource(gen);
    moduleFunctionGradient.setSpacing(0.01);

    ModuleAutoCorrect moduleAutoCorrect2 = new ModuleAutoCorrect(0, 1);
    moduleAutoCorrect2.setSource(moduleFunctionGradient);
    moduleAutoCorrect2.setSamples(10000);
    moduleAutoCorrect2.calculate();

    canvas.updateImage(
        "source", moduleAutoCorrect,
        "moduleFunctionGradient", moduleAutoCorrect2
    );
  }
}
