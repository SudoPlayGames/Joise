package com.sudoplay.joise.examples.module.basis.gradient;

import com.sudoplay.joise.examples.AbstractSplitExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction;
import com.sudoplay.joise.module.ModuleFractal;

/**
 * Created by codetaylor on 1/9/2017.
 */
public class ModuleBasisFunctionGradientBillowExample extends
    AbstractSplitExample {

  static {
    EXAMPLE_CLASS = ModuleBasisFunctionGradientBillowExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleBasisFunction Gradient Billow Example";
  }

  @Override
  protected void run(SplitCanvas canvas) {

    /**
     * Interpolation: None
     */
    ModuleFractal genNone = new ModuleFractal();
    genNone.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.GRADIENT);
    genNone.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.NONE);
    genNone.setNumOctaves(5);
    genNone.setFrequency(2.34);
    genNone.setType(ModuleFractal.FractalType.BILLOW);
    genNone.setSeed(42);

    /**
     * We auto-correct into the range [0, 1].
     */
    ModuleAutoCorrect none = new ModuleAutoCorrect(0, 1);
    none.setSource(genNone);
    none.setSamples(10000);
    none.calculate();

    /**
     * Interpolation: Linear
     */
    ModuleFractal genLinear = new ModuleFractal();
    genLinear.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.GRADIENT);
    genLinear.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.LINEAR);
    genLinear.setNumOctaves(5);
    genLinear.setFrequency(2.34);
    genLinear.setType(ModuleFractal.FractalType.BILLOW);
    genLinear.setSeed(42);

    /**
     * We auto-correct into the range [0, 1].
     */
    ModuleAutoCorrect linear = new ModuleAutoCorrect(0, 1);
    linear.setSource(genLinear);
    linear.setSamples(10000);
    linear.calculate();

    /**
     * Interpolation: Cubic
     */
    ModuleFractal genCubic = new ModuleFractal();
    genCubic.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.GRADIENT);
    genCubic.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
    genCubic.setNumOctaves(5);
    genCubic.setFrequency(2.34);
    genCubic.setType(ModuleFractal.FractalType.BILLOW);
    genCubic.setSeed(42);

    /**
     * We auto-correct into the range [0, 1].
     */
    ModuleAutoCorrect cubic = new ModuleAutoCorrect(0, 1);
    cubic.setSource(genCubic);
    cubic.setSamples(10000);
    cubic.calculate();

    /**
     * Interpolation: Quintic
     */
    ModuleFractal genQuintic = new ModuleFractal();
    genQuintic.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.GRADIENT);
    genQuintic.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.QUINTIC);
    genQuintic.setNumOctaves(5);
    genQuintic.setFrequency(2.34);
    genQuintic.setType(ModuleFractal.FractalType.BILLOW);
    genQuintic.setSeed(42);

    /**
     * We auto-correct into the range [0, 1].
     */
    ModuleAutoCorrect quintic = new ModuleAutoCorrect(0, 1);
    quintic.setSource(genQuintic);
    quintic.setSamples(10000);
    quintic.calculate();

    canvas.updateImage(
        "none", none,
        "linear", linear,
        "cubic", cubic,
        "quintic", quintic
    );
  }
}
