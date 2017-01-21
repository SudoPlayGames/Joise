package com.sudoplay.joise.examples.module;

import com.sudoplay.joise.examples.AbstractExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.*;

/**
 * Created by codetaylor on 1/8/2017.
 */
public class ModuleBlendExample extends
    AbstractExample {

  static {
    EXAMPLE_CLASS = ModuleBlendExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleBlend Example";
  }

  @Override
  protected void run(SplitCanvas canvas) {

    ModuleFractal gen1 = new ModuleFractal();
    gen1.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.SIMPLEX);
    gen1.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
    gen1.setNumOctaves(5);
    gen1.setFrequency(2.34);
    gen1.setType(ModuleFractal.FractalType.RIDGEMULTI);
    gen1.setSeed(42);

    ModuleAutoCorrect lowSource = new ModuleAutoCorrect();
    lowSource.setSource(gen1);
    lowSource.setSamples(100000);
    lowSource.calculate();

    ModuleFractal gen2 = new ModuleFractal();
    gen2.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.GRADVAL);
    gen2.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
    gen2.setNumOctaves(4);
    gen2.setFrequency(3.34);
    gen2.setType(ModuleFractal.FractalType.MULTI);
    gen2.setSeed(73);

    ModuleAutoCorrect highSource = new ModuleAutoCorrect();
    highSource.setSource(gen2);
    highSource.setSamples(100000);
    highSource.calculate();

    ModuleBlend moduleBlend = new ModuleBlend();
    moduleBlend.setLowSource(lowSource);
    moduleBlend.setHighSource(highSource);
    moduleBlend.setControlSource(0.5);

    canvas.updateImage(
        "lowSource", lowSource,
        "highSource", highSource,
        "moduleBlend", moduleBlend
    );
  }
}
