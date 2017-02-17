package com.sudoplay.joise.examples.module.fractal.value;

import com.sudoplay.joise.examples.AbstractSplitExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleFractal;

import static com.sudoplay.joise.module.ModuleBasisFunction.BasisType;
import static com.sudoplay.joise.module.ModuleBasisFunction.InterpolationType;
import static com.sudoplay.joise.module.ModuleFractal.FractalType;

/**
 * Created by codetaylor on 1/9/2017.
 */
public class ModuleFractalValueFBMExample extends
    AbstractSplitExample {

  static {
    EXAMPLE_CLASS = ModuleFractalValueFBMExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleFractal Value FBM Example";
  }

  @Override
  protected void run(SplitCanvas canvas) {
    BasisType basisType = BasisType.VALUE;
    FractalType fractalType = FractalType.FBM;

    canvas.updateImage(
        "none", getModule(basisType, InterpolationType.NONE, fractalType),
        "linear", getModule(basisType, InterpolationType.LINEAR, fractalType),
        "cubic", getModule(basisType, InterpolationType.CUBIC, fractalType),
        "quintic", getModule(basisType, InterpolationType.QUINTIC, fractalType)
    );
  }

  private ModuleAutoCorrect getModule(
      BasisType basisType,
      InterpolationType interpolationType,
      FractalType fractalType
  ) {
    ModuleFractal gen = new ModuleFractal();
    gen.setAllSourceBasisTypes(basisType);
    gen.setAllSourceInterpolationTypes(interpolationType);
    gen.setNumOctaves(5);
    gen.setFrequency(2.34);
    gen.setType(fractalType);
    gen.setSeed(42);

    /**
     * We auto-correct into the range [0, 1], performing 10,000 samples to calculate the auto-correct values.
     */
    ModuleAutoCorrect source = new ModuleAutoCorrect(0, 1);
    source.setSource(gen);
    source.setSamples(10000);
    source.calculate2D();
    return source;
  }
}
