package com.sudoplay.joise.examples.module;

import com.sudoplay.joise.examples.AbstractSplitExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.*;

/**
 * Created by codetaylor on 1/21/2017.
 */
public class ModuleTiersExample extends
    AbstractSplitExample {

  static {
    EXAMPLE_CLASS = ModuleTiersExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleTiers Example";
  }

  @Override
  protected void run(SplitCanvas canvas) {

    ModuleFractal source = new ModuleFractal();
    source.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.SIMPLEX);
    source.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
    source.setNumOctaves(5);
    source.setFrequency(2.34);
    source.setType(ModuleFractal.FractalType.RIDGEMULTI);
    source.setSeed(42);

    /**
     * We auto-correct into the range [0, 1], performing 10,000 samples to calculate the auto-correct values.
     */
    ModuleAutoCorrect moduleAutoCorrect = new ModuleAutoCorrect(0, 1);
    moduleAutoCorrect.setSource(source);
    moduleAutoCorrect.setSamples(10000);
    moduleAutoCorrect.calculate();

    /**
     * The {@link ModuleTiers} is very similar to what we did in the {@link ModuleFloorExample}, except it is contained
     * in one module.
     */
    ModuleTiers moduleTiers = new ModuleTiers();
    moduleTiers.setSource(moduleAutoCorrect);
    moduleTiers.setNumTiers(5);
    moduleTiers.setSmooth(false);

    ModuleTiers moduleTiersSmooth = new ModuleTiers();
    moduleTiersSmooth.setSource(moduleAutoCorrect);
    moduleTiersSmooth.setNumTiers(5);

    canvas.updateImage(
        "source", moduleAutoCorrect,
        "moduleTiers (numTiers=5, smooth=false)", moduleTiers,
        "moduleTiers (numTiers=5, smooth=true)", moduleTiersSmooth
    );
  }

}
