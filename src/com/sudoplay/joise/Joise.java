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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.sudoplay.joise.module.*;
import com.sudoplay.util.Assert;

public class Joise {

  private Module module;
  private ModuleMap moduleMap;
  private ModuleFactoryRegistry moduleFactoryRegistry;
  private HashMap<String, ArrayList<SeedableModule>> seedMap = new HashMap<String, ArrayList<SeedableModule>>();

  /**
   * Common initialization.
   */
  private Joise() {
    this.moduleFactoryRegistry = new ModuleFactoryRegistry();

    this.moduleFactoryRegistry.register(ModuleAbs.class, new IModuleFactory<ModuleAbs>() {
      @Override
      public ModuleAbs create() {
        return new ModuleAbs();
      }
    });

    this.moduleFactoryRegistry.register(ModuleAutoCorrect.class, new IModuleFactory<ModuleAutoCorrect>() {
      @Override
      public ModuleAutoCorrect create() {
        return new ModuleAutoCorrect();
      }
    });

    this.moduleFactoryRegistry.register(ModuleBasisFunction.class, new IModuleFactory<ModuleBasisFunction>() {
      @Override
      public ModuleBasisFunction create() {
        return new ModuleBasisFunction();
      }
    });

    this.moduleFactoryRegistry.register(ModuleBias.class, new IModuleFactory<ModuleBias>() {
      @Override
      public ModuleBias create() {
        return new ModuleBias();
      }
    });

    this.moduleFactoryRegistry.register(ModuleBlend.class, new IModuleFactory<ModuleBlend>() {
      @Override
      public ModuleBlend create() {
        return new ModuleBlend();
      }
    });

    this.moduleFactoryRegistry.register(ModuleBrightContrast.class, new IModuleFactory<ModuleBrightContrast>() {
      @Override
      public ModuleBrightContrast create() {
        return new ModuleBrightContrast();
      }
    });

    this.moduleFactoryRegistry.register(ModuleCache.class, new IModuleFactory<ModuleCache>() {
      @Override
      public ModuleCache create() {
        return new ModuleCache();
      }
    });

    this.moduleFactoryRegistry.register(ModuleCellGen.class, new IModuleFactory<ModuleCellGen>() {
      @Override
      public ModuleCellGen create() {
        return new ModuleCellGen();
      }
    });

    this.moduleFactoryRegistry.register(ModuleCellular.class, new IModuleFactory<ModuleCellular>() {
      @Override
      public ModuleCellular create() {
        return new ModuleCellular();
      }
    });

    this.moduleFactoryRegistry.register(ModuleClamp.class, new IModuleFactory<ModuleClamp>() {
      @Override
      public ModuleClamp create() {
        return new ModuleClamp();
      }
    });

    this.moduleFactoryRegistry.register(ModuleCombiner.class, new IModuleFactory<ModuleCombiner>() {
      @Override
      public ModuleCombiner create() {
        return new ModuleCombiner();
      }
    });

    this.moduleFactoryRegistry.register(ModuleCos.class, new IModuleFactory<ModuleCos>() {
      @Override
      public ModuleCos create() {
        return new ModuleCos();
      }
    });

    this.moduleFactoryRegistry.register(ModuleFloor.class, new IModuleFactory<ModuleFloor>() {
      @Override
      public ModuleFloor create() {
        return new ModuleFloor();
      }
    });

    this.moduleFactoryRegistry.register(ModuleFractal.class, new IModuleFactory<ModuleFractal>() {
      @Override
      public ModuleFractal create() {
        return new ModuleFractal();
      }
    });

    this.moduleFactoryRegistry.register(ModuleFunctionGradient.class, new IModuleFactory<ModuleFunctionGradient>() {
      @Override
      public ModuleFunctionGradient create() {
        return new ModuleFunctionGradient();
      }
    });

    this.moduleFactoryRegistry.register(ModuleGain.class, new IModuleFactory<ModuleGain>() {
      @Override
      public ModuleGain create() {
        return new ModuleGain();
      }
    });

    this.moduleFactoryRegistry.register(ModuleGradient.class, new IModuleFactory<ModuleGradient>() {
      @Override
      public ModuleGradient create() {
        return new ModuleGradient();
      }
    });

    this.moduleFactoryRegistry.register(ModuleInvert.class, new IModuleFactory<ModuleInvert>() {
      @Override
      public ModuleInvert create() {
        return new ModuleInvert();
      }
    });

    this.moduleFactoryRegistry.register(ModuleMagnitude.class, new IModuleFactory<ModuleMagnitude>() {
      @Override
      public ModuleMagnitude create() {
        return new ModuleMagnitude();
      }
    });

    this.moduleFactoryRegistry.register(ModuleNormalizedCoords.class, new IModuleFactory<ModuleNormalizedCoords>() {
      @Override
      public ModuleNormalizedCoords create() {
        return new ModuleNormalizedCoords();
      }
    });

    this.moduleFactoryRegistry.register(ModulePow.class, new IModuleFactory<ModulePow>() {
      @Override
      public ModulePow create() {
        return new ModulePow();
      }
    });

    this.moduleFactoryRegistry.register(ModuleRotateDomain.class, new IModuleFactory<ModuleRotateDomain>() {
      @Override
      public ModuleRotateDomain create() {
        return new ModuleRotateDomain();
      }
    });

    this.moduleFactoryRegistry.register(ModuleSawtooth.class, new IModuleFactory<ModuleSawtooth>() {
      @Override
      public ModuleSawtooth create() {
        return new ModuleSawtooth();
      }
    });

    this.moduleFactoryRegistry.register(ModuleScaleDomain.class, new IModuleFactory<ModuleScaleDomain>() {
      @Override
      public ModuleScaleDomain create() {
        return new ModuleScaleDomain();
      }
    });

    this.moduleFactoryRegistry.register(ModuleScaleOffset.class, new IModuleFactory<ModuleScaleOffset>() {
      @Override
      public ModuleScaleOffset create() {
        return new ModuleScaleOffset();
      }
    });

    this.moduleFactoryRegistry.register(ModuleSelect.class, new IModuleFactory<ModuleSelect>() {
      @Override
      public ModuleSelect create() {
        return new ModuleSelect();
      }
    });

    this.moduleFactoryRegistry.register(ModuleSin.class, new IModuleFactory<ModuleSin>() {
      @Override
      public ModuleSin create() {
        return new ModuleSin();
      }
    });

    this.moduleFactoryRegistry.register(ModuleSphere.class, new IModuleFactory<ModuleSphere>() {
      @Override
      public ModuleSphere create() {
        return new ModuleSphere();
      }
    });

    this.moduleFactoryRegistry.register(ModuleTiers.class, new IModuleFactory<ModuleTiers>() {
      @Override
      public ModuleTiers create() {
        return new ModuleTiers();
      }
    });

    this.moduleFactoryRegistry.register(ModuleTranslateDomain.class, new IModuleFactory<ModuleTranslateDomain>() {
      @Override
      public ModuleTranslateDomain create() {
        return new ModuleTranslateDomain();
      }
    });

    this.moduleFactoryRegistry.register(ModuleTriangle.class, new IModuleFactory<ModuleTriangle>() {
      @Override
      public ModuleTriangle create() {
        return new ModuleTriangle();
      }
    });
  }

  /**
   * Creates a new instance of Joise with the supplied module chain.
   * <p>
   * This method duplicates the module chain by first converting the chain to a
   * {@link ModuleMap}, then converting it back to a {@link Module} while
   * mapping any seed names to the seedmap.
   * <p>
   * Changes made to the original module passed in will not be reflected in this
   * instance of Joise.
   *
   * @param module
   */
  public Joise(Module module) {
    this();
    Assert.notNull(module);
    this.moduleMap = module.getModuleMap();
    this.module = fromModuleMap(this.moduleMap);
  }

  /**
   * Creates a new instance of Joise from the supplied {@link ModuleMap}.
   * <p>
   * This method duplicates the module map by first converting the map to a
   * {@link Module}, then converting it back to a module map. Seed names are
   * mapped during the conversion from map to module.
   * <p>
   * Changes made to the original module map passed in will not be reflected in
   * this instance of Joise.
   *
   * @param moduleMap
   */
  public Joise(ModuleMap moduleMap) {
    this();
    Assert.notNull(moduleMap);
    this.module = fromModuleMap(moduleMap);
    this.moduleMap = this.module.getModuleMap();
  }

  private Module fromModuleMap(ModuleMap map) {
    try {
      ModuleInstanceMap im = new ModuleInstanceMap();
      Iterator<Entry<String, ModulePropertyMap>> it = map.mapIterator();
      Module module = null;
      while (it.hasNext()) {
        Entry<String, ModulePropertyMap> e = it.next();
        ModulePropertyMap props = e.getValue();
        module = this.moduleFactoryRegistry.get(props.get("module").toString()).create();
        module.buildFromPropertyMap(props, im);
        if (module instanceof SeedableModule
            && ((SeedableModule) module).hasSeedName()) {
          SeedableModule sm = (SeedableModule) module;
          String seedName = sm.getSeedName();

          ArrayList<SeedableModule> list = seedMap.get(seedName);
          if (list == null) {
            list = new ArrayList<SeedableModule>();
            seedMap.put(seedName, list);
          }
          list.add(sm);
        }
        im.put(e.getKey(), module);
      }
      return module;
    } catch (Exception e) {
      throw new JoiseException(e);
    }
  }

  /**
   * Use this to register any custom modules.
   * <p>
   * Custom modules must be registered before converting to a {@link ModuleMap}.
   *
   * @param moduleClass
   * @param moduleFactory
   * @param <M>
   */
  public <M extends Module> void registerModuleFactory(Class<M> moduleClass, IModuleFactory<M> moduleFactory) {
    this.moduleFactoryRegistry.register(moduleClass, moduleFactory);
  }

  /**
   * Sets the seed of the module linked by seedName.
   *
   * @param seedName
   * @param seed
   * @throws IllegalStateException
   *           if the seed name is not found in the seed map
   */
  public void setSeed(String seedName, long seed) {
    ArrayList<SeedableModule> list = seedMap.get(seedName);
    if (list == null || list.isEmpty()) {
      throw new IllegalStateException("Seed name not found: " + seedName);
    }
    for (SeedableModule sm : list) {
      sm.setSeed(seed);
    }
  }

  public boolean hasSeed(String seedName) {
    return seedMap.get(seedName) != null;
  }

  /**
   * @return the stored module map for this Joise
   */
  public ModuleMap getModuleMap() {
    return moduleMap;
  }

  public double get(double x, double y) {
    return module.get(x, y);
  }

  public double get(double x, double y, double z) {
    return module.get(x, y, z);
  }

  public double get(double x, double y, double z, double w) {
    return module.get(x, y, z, w);
  }

  public double get(double x, double y, double z, double w, double u, double v) {
    return module.get(x, y, z, w, u, v);
  }

}
