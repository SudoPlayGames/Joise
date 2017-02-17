package com.sudoplay.joise.examples.module.basis;

import com.sudoplay.joise.examples.AbstractSplitExample;
import com.sudoplay.joise.module.Module;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction;
import com.sudoplay.joise.module.ModuleScaleDomain;

/**
 * Created by sk3lls on 2/8/2017.
 */
public abstract class AbstractModuleBasisFunctionExample extends AbstractSplitExample {

  protected Module createModuleBasisFunction(
      ModuleBasisFunction.BasisType basisType,
      ModuleBasisFunction.InterpolationType interpolationType
  ) {

    ModuleBasisFunction moduleBasisFunction = new ModuleBasisFunction();
    moduleBasisFunction.setType(basisType);
    moduleBasisFunction.setInterpolation(interpolationType);
    moduleBasisFunction.setSeed(42);

    /**
     * We auto-correct into the range [0, 1].
     */
    ModuleAutoCorrect moduleAutoCorrect = new ModuleAutoCorrect(0, 1);
    moduleAutoCorrect.setSource(moduleBasisFunction);
    moduleAutoCorrect.setSamples(10000);
    moduleAutoCorrect.calculate2D();

    /**
     * We scale the domain to see more of the noise.
     */
    ModuleScaleDomain moduleScaleDomain = new ModuleScaleDomain();
    moduleScaleDomain.setSource(moduleAutoCorrect);
    moduleScaleDomain.setScaleX(16.0);
    moduleScaleDomain.setScaleY(16.0);

    return moduleScaleDomain;
  }
}
