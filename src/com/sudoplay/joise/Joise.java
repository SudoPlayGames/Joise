/*
 * Copyright (C) 2013 Jason Taylor.
 * Released as open-source under the Apache License, Version 2.0.
 * 
 * ============================================================================
 * | Joise
 * ============================================================================
 * 
 * Copyright (C) 2013 Jason Taylor
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

import com.sudoplay.joise.module.Module;
import com.sudoplay.joise.module.SeedableModule;
import com.sudoplay.util.Assert;

public class Joise {

  private Module module;
  private ModuleMap moduleMap;
  private ModuleFactory moduleFactory;
  private HashMap<String, ArrayList<SeedableModule>> seedMap = new HashMap<String, ArrayList<SeedableModule>>();

  /**
   * Common initialization.
   */
  private Joise() {
    this.moduleFactory = new ModuleFactory();
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
        module = this.moduleFactory.create(props.get("module").toString());
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
