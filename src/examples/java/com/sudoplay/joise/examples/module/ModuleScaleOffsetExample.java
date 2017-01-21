package com.sudoplay.joise.examples.module;

import com.sudoplay.joise.examples.AbstractExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction;
import com.sudoplay.joise.module.ModuleFractal;
import com.sudoplay.joise.module.ModuleScaleOffset;

/**
 * Created by codetaylor on 1/21/2017.
 */
public class ModuleScaleOffsetExample extends
    AbstractExample {

  static {
    EXAMPLE_CLASS = ModuleScaleOffsetExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleScaleOffset Example";
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
     * The {@link ModuleScaleOffset} is similar to the {@link ModuleAutoCorrect}, except you set the values manually.
     *
     * ie. the resulting sampled value is multiplied by the scale parameter then added to the offset parameter
     */
    ModuleScaleOffset moduleScaleOffset = new ModuleScaleOffset();
    moduleScaleOffset.setSource(moduleAutoCorrect);

    /**
     * The {@link ModuleAutoCorrect} has corrected the value to roughly [0,1]; the following will change the
     * range to [0.5,1].
     */
    moduleScaleOffset.setScale(0.5); // scale the value down by half
    moduleScaleOffset.setOffset(0.5); // add 0.5 to the value

    canvas.updateImage(
        "source", moduleAutoCorrect,
        "scaleOffset (scale=0.5, offset=0.5)", moduleScaleOffset
    );
  }

}
