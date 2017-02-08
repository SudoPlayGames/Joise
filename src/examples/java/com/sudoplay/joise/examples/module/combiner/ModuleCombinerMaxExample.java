package com.sudoplay.joise.examples.module.combiner;

import com.sudoplay.joise.examples.AbstractSplitExample;
import com.sudoplay.joise.examples.SplitCanvas;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction;
import com.sudoplay.joise.module.ModuleCombiner;
import com.sudoplay.joise.module.ModuleFractal;

/**
 * Created by codetaylor on 1/8/2017.
 */
public class ModuleCombinerMaxExample extends
    AbstractSplitExample {

  static {
    EXAMPLE_CLASS = ModuleCombinerMaxExample.class;
  }

  @Override
  protected String getWindowTitle() {
    return "ModuleCombiner Max Example";
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

    ModuleCombiner moduleCombiner = new ModuleCombiner();
    moduleCombiner.setType(ModuleCombiner.CombinerType.MAX);
    moduleCombiner.setSource(0, lowSource);
    moduleCombiner.setSource(1, highSource);

    canvas.updateImage(
        "lowSource", lowSource,
        "highSource", highSource,
        "moduleCombiner: max", moduleCombiner
    );
  }
}
