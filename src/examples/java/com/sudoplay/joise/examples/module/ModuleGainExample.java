package com.sudoplay.joise.examples.module;

import com.sudoplay.joise.examples.AbstractExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.*;

/**
 * Created by codetaylor on 1/11/2017.
 */
public class ModuleGainExample extends
    AbstractExample {

  static {
    EXAMPLE_CLASS = ModuleGainExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleGain Example";
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

    canvas.updateImage(
        "source", moduleAutoCorrect,
        "gain = 0.2", getModule(moduleAutoCorrect, 0.2),
        "gain = 0.8", getModule(moduleAutoCorrect, 0.8)
    );
  }

  private ModuleGain getModule(Module source, double gain) {
    ModuleGain moduleGain = new ModuleGain();
    moduleGain.setSource(source);
    moduleGain.setGain(gain);
    return moduleGain;
  }
}
