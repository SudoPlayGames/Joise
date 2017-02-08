package com.sudoplay.joise.examples.module;

import com.sudoplay.joise.examples.AbstractSplitExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.*;

/**
 * Created by codetaylor on 1/9/2017.
 */
public class ModuleCosExample extends
    AbstractSplitExample {

  static {
    EXAMPLE_CLASS = ModuleCosExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleCos Example";
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

    ModuleCos moduleCos = new ModuleCos();
    moduleCos.setSource(source);

    canvas.updateImage(
        "source", source,
        "moduleCos", moduleCos
    );
  }
}
