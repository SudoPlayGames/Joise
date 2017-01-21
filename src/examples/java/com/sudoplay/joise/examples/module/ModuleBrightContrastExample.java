package com.sudoplay.joise.examples.module;

import com.sudoplay.joise.examples.AbstractExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.*;

/**
 * Created by codetaylor on 1/9/2017.
 */
public class ModuleBrightContrastExample extends
    AbstractExample {

  static {
    EXAMPLE_CLASS = ModuleBrightContrastExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleBrightContrast Example";
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
     * We auto-correct into the range [0, 1].
     */
    ModuleAutoCorrect source = new ModuleAutoCorrect(0, 1);
    source.setSource(gen);
    source.setSamples(10000);
    source.calculate();

    ModuleBrightContrast moduleBrightContrast = new ModuleBrightContrast();
    moduleBrightContrast.setSource(source);
    moduleBrightContrast.setBrightness(-0.5);
    moduleBrightContrast.setContrastThreshold(0.25);
    moduleBrightContrast.setContrastFactor(2.0);

    canvas.updateImage(
        "source", source,
        "moduleBrightContrast", moduleBrightContrast
    );
  }
}
