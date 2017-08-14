/*
 * Copyright (C) 2016 Jason Taylor.
 * Released as open-source under the Apache License, Version 2.0.
 *
 * ============================================================================
 * | Joise
 * ============================================================================
 *
 * Copyright (C) 2016 Jason Taylor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============================================================================
 * | Accidental Noise Library
 * | --------------------------------------------------------------------------
 * | Joise is a derivative work based on Josua Tippetts' C++ library:
 * | http://accidentalnoise.sourceforge.net/index.html
 * ============================================================================
 *
 * Copyright (C) 2011 Joshua Tippetts
 *
 *   This software is provided 'as-is', without any express or implied
 *   warranty.  In no event will the authors be held liable for any damages
 *   arising from the use of this software.
 *
 *   Permission is granted to anyone to use this software for any purpose,
 *   including commercial applications, and to alter it and redistribute it
 *   freely, subject to the following restrictions:
 *
 *   1. The origin of this software must not be misrepresented; you must not
 *      claim that you wrote the original software. If you use this software
 *      in a product, an acknowledgment in the product documentation would be
 *      appreciated but is not required.
 *   2. Altered source versions must be plainly marked as such, and must not be
 *      misrepresented as being the original software.
 *   3. This notice may not be removed or altered from any source distribution.
 */

package com.sudoplay.joise;

import com.sudoplay.joise.module.*;

public interface IModuleFactory<M extends Module> {

  M create();

  IModuleFactory<ModuleAbs> MODULE_ABS_FACTORY = new IModuleFactory<ModuleAbs>() {
    @Override
    public ModuleAbs create() {
      return new ModuleAbs();
    }
  };

  IModuleFactory<ModuleAutoCorrect> MODULE_AUTO_CORRECT_FACTORY = new IModuleFactory<ModuleAutoCorrect>() {
    @Override
    public ModuleAutoCorrect create() {
      return new ModuleAutoCorrect();
    }
  };

  IModuleFactory<ModuleBasisFunction> MODULE_BASIS_FUNCTION_FACTORY = new IModuleFactory<ModuleBasisFunction>() {
    @Override
    public ModuleBasisFunction create() {
      return new ModuleBasisFunction();
    }
  };

  IModuleFactory<ModuleBias> MODULE_BIAS_FACTORY = new IModuleFactory<ModuleBias>() {
    @Override
    public ModuleBias create() {
      return new ModuleBias();
    }
  };

  IModuleFactory<ModuleBlend> MODULE_BLEND_FACTORY = new IModuleFactory<ModuleBlend>() {
    @Override
    public ModuleBlend create() {
      return new ModuleBlend();
    }
  };

  IModuleFactory<ModuleBrightContrast> MODULE_BRIGHT_CONTRAST_FACTORY = new IModuleFactory<ModuleBrightContrast>() {
    @Override
    public ModuleBrightContrast create() {
      return new ModuleBrightContrast();
    }
  };

  IModuleFactory<ModuleCache> MODULE_CACHE_FACTORY = new IModuleFactory<ModuleCache>() {
    @Override
    public ModuleCache create() {
      return new ModuleCache();
    }
  };

  IModuleFactory<ModuleCellGen> MODULE_CELL_GEN_FACTORY = new IModuleFactory<ModuleCellGen>() {
    @Override
    public ModuleCellGen create() {
      return new ModuleCellGen();
    }
  };

  IModuleFactory<ModuleCellular> MODULE_CELLULAR_FACTORY = new IModuleFactory<ModuleCellular>() {
    @Override
    public ModuleCellular create() {
      return new ModuleCellular();
    }
  };

  IModuleFactory<ModuleClamp> MODULE_CLAMP_FACTORY = new IModuleFactory<ModuleClamp>() {
    @Override
    public ModuleClamp create() {
      return new ModuleClamp();
    }
  };

  IModuleFactory<ModuleCombiner> MODULE_COMBINER_FACTORY = new IModuleFactory<ModuleCombiner>() {
    @Override
    public ModuleCombiner create() {
      return new ModuleCombiner();
    }
  };

  IModuleFactory<ModuleCos> MODULE_COS_FACTORY = new IModuleFactory<ModuleCos>() {
    @Override
    public ModuleCos create() {
      return new ModuleCos();
    }
  };

  IModuleFactory<ModuleFloor> MODULE_FLOOR_FACTORY = new IModuleFactory<ModuleFloor>() {
    @Override
    public ModuleFloor create() {
      return new ModuleFloor();
    }
  };

  IModuleFactory<ModuleFractal> MODULE_FRACTAL_FACTORY = new IModuleFactory<ModuleFractal>() {
    @Override
    public ModuleFractal create() {
      return new ModuleFractal();
    }
  };

  IModuleFactory<ModuleFunctionGradient> MODULE_FUNCTION_GRADIENT_FACTORY = new IModuleFactory<ModuleFunctionGradient>() {
    @Override
    public ModuleFunctionGradient create() {
      return new ModuleFunctionGradient();
    }
  };

  IModuleFactory<ModuleGain> MODULE_GAIN_FACTORY = new IModuleFactory<ModuleGain>() {
    @Override
    public ModuleGain create() {
      return new ModuleGain();
    }
  };

  IModuleFactory<ModuleGradient> MODULE_GRADIENT_FACTORY = new IModuleFactory<ModuleGradient>() {
    @Override
    public ModuleGradient create() {
      return new ModuleGradient();
    }
  };

  IModuleFactory<ModuleInvert> MODULE_INVERT_FACTORY = new IModuleFactory<ModuleInvert>() {
    @Override
    public ModuleInvert create() {
      return new ModuleInvert();
    }
  };

  IModuleFactory<ModuleMagnitude> MODULE_MAGNITUDE_FACTORY = new IModuleFactory<ModuleMagnitude>() {
    @Override
    public ModuleMagnitude create() {
      return new ModuleMagnitude();
    }
  };

  IModuleFactory<ModuleNormalizedCoords> MODULE_NORMALIZED_COORDS_FACTORY = new IModuleFactory<ModuleNormalizedCoords>() {
    @Override
    public ModuleNormalizedCoords create() {
      return new ModuleNormalizedCoords();
    }
  };

  IModuleFactory<ModulePow> MODULE_POW_FACTORY = new IModuleFactory<ModulePow>() {
    @Override
    public ModulePow create() {
      return new ModulePow();
    }
  };

  IModuleFactory<ModuleRotateDomain> MODULE_ROTATE_DOMAIN_FACTORY = new IModuleFactory<ModuleRotateDomain>() {
    @Override
    public ModuleRotateDomain create() {
      return new ModuleRotateDomain();
    }
  };

  IModuleFactory<ModuleSawtooth> MODULE_SAWTOOTH_FACTORY = new IModuleFactory<ModuleSawtooth>() {
    @Override
    public ModuleSawtooth create() {
      return new ModuleSawtooth();
    }
  };

  IModuleFactory<ModuleScaleDomain> MODULE_SCALE_DOMAIN_FACTORY = new IModuleFactory<ModuleScaleDomain>() {
    @Override
    public ModuleScaleDomain create() {
      return new ModuleScaleDomain();
    }
  };

  IModuleFactory<ModuleScaleOffset> MODULE_SCALE_OFFSET_FACTORY = new IModuleFactory<ModuleScaleOffset>() {
    @Override
    public ModuleScaleOffset create() {
      return new ModuleScaleOffset();
    }
  };

  IModuleFactory<ModuleSelect> MODULE_SELECT_FACTORY = new IModuleFactory<ModuleSelect>() {
    @Override
    public ModuleSelect create() {
      return new ModuleSelect();
    }
  };

  IModuleFactory<ModuleSin> MODULE_SIN_FACTORY = new IModuleFactory<ModuleSin>() {
    @Override
    public ModuleSin create() {
      return new ModuleSin();
    }
  };

  IModuleFactory<ModuleSphere> MODULE_SPHERE_FACTORY = new IModuleFactory<ModuleSphere>() {
    @Override
    public ModuleSphere create() {
      return new ModuleSphere();
    }
  };

  IModuleFactory<ModuleTiers> MODULE_TIERS_FACTORY = new IModuleFactory<ModuleTiers>() {
    @Override
    public ModuleTiers create() {
      return new ModuleTiers();
    }
  };

  IModuleFactory<ModuleTranslateDomain> MODULE_TRANSLATE_DOMAIN_FACTORY = new IModuleFactory<ModuleTranslateDomain>() {
    @Override
    public ModuleTranslateDomain create() {
      return new ModuleTranslateDomain();
    }
  };

  IModuleFactory<ModuleTriangle> MODULE_TRIANGLE_FACTORY = new IModuleFactory<ModuleTriangle>() {
    @Override
    public ModuleTriangle create() {
      return new ModuleTriangle();
    }
  };
}
