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

    this.moduleFactoryRegistry.register(ModuleAbs.class, IModuleFactory.MODULE_ABS_FACTORY);
    this.moduleFactoryRegistry.register(ModuleAutoCorrect.class, IModuleFactory.MODULE_AUTO_CORRECT_FACTORY);
    this.moduleFactoryRegistry.register(ModuleBasisFunction.class, IModuleFactory.MODULE_BASIS_FUNCTION_FACTORY);
    this.moduleFactoryRegistry.register(ModuleBias.class, IModuleFactory.MODULE_BIAS_FACTORY);
    this.moduleFactoryRegistry.register(ModuleBlend.class, IModuleFactory.MODULE_BLEND_FACTORY);
    this.moduleFactoryRegistry.register(ModuleBrightContrast.class, IModuleFactory.MODULE_BRIGHT_CONTRAST_FACTORY);
    this.moduleFactoryRegistry.register(ModuleCache.class, IModuleFactory.MODULE_CACHE_FACTORY);
    this.moduleFactoryRegistry.register(ModuleCellGen.class, IModuleFactory.MODULE_CELL_GEN_FACTORY);
    this.moduleFactoryRegistry.register(ModuleCellular.class, IModuleFactory.MODULE_CELLULAR_FACTORY);
    this.moduleFactoryRegistry.register(ModuleClamp.class, IModuleFactory.MODULE_CLAMP_FACTORY);
    this.moduleFactoryRegistry.register(ModuleCombiner.class, IModuleFactory.MODULE_COMBINER_FACTORY);
    this.moduleFactoryRegistry.register(ModuleCos.class, IModuleFactory.MODULE_COS_FACTORY);
    this.moduleFactoryRegistry.register(ModuleFloor.class, IModuleFactory.MODULE_FLOOR_FACTORY);
    this.moduleFactoryRegistry.register(ModuleFractal.class, IModuleFactory.MODULE_FRACTAL_FACTORY);
    this.moduleFactoryRegistry.register(ModuleFunctionGradient.class, IModuleFactory.MODULE_FUNCTION_GRADIENT_FACTORY);
    this.moduleFactoryRegistry.register(ModuleGain.class, IModuleFactory.MODULE_GAIN_FACTORY);
    this.moduleFactoryRegistry.register(ModuleGradient.class, IModuleFactory.MODULE_GRADIENT_FACTORY);
    this.moduleFactoryRegistry.register(ModuleInvert.class, IModuleFactory.MODULE_INVERT_FACTORY);
    this.moduleFactoryRegistry.register(ModuleMagnitude.class, IModuleFactory.MODULE_MAGNITUDE_FACTORY);
    this.moduleFactoryRegistry.register(ModuleNormalizedCoords.class, IModuleFactory.MODULE_NORMALIZED_COORDS_FACTORY);
    this.moduleFactoryRegistry.register(ModulePow.class, IModuleFactory.MODULE_POW_FACTORY);
    this.moduleFactoryRegistry.register(ModuleRotateDomain.class, IModuleFactory.MODULE_ROTATE_DOMAIN_FACTORY);
    this.moduleFactoryRegistry.register(ModuleSawtooth.class, IModuleFactory.MODULE_SAWTOOTH_FACTORY);
    this.moduleFactoryRegistry.register(ModuleScaleDomain.class, IModuleFactory.MODULE_SCALE_DOMAIN_FACTORY);
    this.moduleFactoryRegistry.register(ModuleScaleOffset.class, IModuleFactory.MODULE_SCALE_OFFSET_FACTORY);
    this.moduleFactoryRegistry.register(ModuleSelect.class, IModuleFactory.MODULE_SELECT_FACTORY);
    this.moduleFactoryRegistry.register(ModuleSin.class, IModuleFactory.MODULE_SIN_FACTORY);
    this.moduleFactoryRegistry.register(ModuleSphere.class, IModuleFactory.MODULE_SPHERE_FACTORY);
    this.moduleFactoryRegistry.register(ModuleTiers.class, IModuleFactory.MODULE_TIERS_FACTORY);
    this.moduleFactoryRegistry.register(ModuleTranslateDomain.class, IModuleFactory.MODULE_TRANSLATE_DOMAIN_FACTORY);
    this.moduleFactoryRegistry.register(ModuleTriangle.class, IModuleFactory.MODULE_TRIANGLE_FACTORY);
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
