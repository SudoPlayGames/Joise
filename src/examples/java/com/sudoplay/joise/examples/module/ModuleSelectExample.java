package com.sudoplay.joise.examples.module;

import com.sudoplay.joise.examples.AbstractSplitExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.*;

/**
 * Created by codetaylor on 1/21/2017.
 */
public class ModuleSelectExample extends
    AbstractSplitExample {

  static {
    EXAMPLE_CLASS = ModuleSelectExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleSelect Example";
  }

  @Override
  protected void run(SplitCanvas canvas) {

    ModuleFractal source1 = new ModuleFractal();
    source1.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.SIMPLEX);
    source1.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
    source1.setNumOctaves(5);
    source1.setFrequency(2.34);
    source1.setType(ModuleFractal.FractalType.RIDGEMULTI);
    source1.setSeed(42);

    /**
     * We auto-correct into the range [0, 1], performing 10,000 samples to calculate the auto-correct values.
     */
    ModuleAutoCorrect moduleAutoCorrect1 = new ModuleAutoCorrect(0, 1);
    moduleAutoCorrect1.setSource(source1);
    moduleAutoCorrect1.setSamples(10000);
    moduleAutoCorrect1.calculate();

    ModuleFractal source2 = new ModuleFractal();
    source2.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.SIMPLEX);
    source2.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
    source2.setNumOctaves(5);
    source2.setFrequency(2.34);
    source2.setType(ModuleFractal.FractalType.DECARPENTIERSWISS);
    source2.setSeed(42);

    /**
     * We auto-correct into the range [0, 1], performing 10,000 samples to calculate the auto-correct values.
     */
    ModuleAutoCorrect moduleAutoCorrect2 = new ModuleAutoCorrect(0, 1);
    moduleAutoCorrect2.setSource(source2);
    moduleAutoCorrect2.setSamples(10000);
    moduleAutoCorrect2.calculate();

    ModuleGradient moduleGradient = new ModuleGradient();
    moduleGradient.setGradient(0, 0, 0, 1);

    ModuleSelect moduleSelect = new ModuleSelect();
    moduleSelect.setLowSource(moduleAutoCorrect1);
    moduleSelect.setHighSource(moduleAutoCorrect2);
    moduleSelect.setControlSource(moduleGradient);
    moduleSelect.setFalloff(0.5);
    moduleSelect.setThreshold(0.5);

    canvas.updateImage(
        "source1", moduleAutoCorrect1,
        "source2", moduleAutoCorrect2,
        "select", moduleSelect
    );
  }

}
