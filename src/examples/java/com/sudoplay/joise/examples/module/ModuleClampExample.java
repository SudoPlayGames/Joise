package com.sudoplay.joise.examples.module;

import com.sudoplay.joise.examples.AbstractExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.*;

/**
 * Created by codetaylor on 1/9/2017.
 */
public class ModuleClampExample extends
    AbstractExample {

  static {
    EXAMPLE_CLASS = ModuleClampExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleClamp Example";
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
    ModuleAutoCorrect source = new ModuleAutoCorrect(0, 1);
    source.setSource(gen);
    source.setSamples(10000);
    source.calculate();

    ModuleClamp moduleClamp = new ModuleClamp();
    moduleClamp.setSource(source);
    moduleClamp.setLow(0.25);
    moduleClamp.setHigh(0.5);

    canvas.updateImage(
        "source", source,
        "moduleClamp", moduleClamp
    );
  }
}
