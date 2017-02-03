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

package com.sudoplay.joise.module;

import com.sudoplay.joise.ModuleInstanceMap;
import com.sudoplay.joise.ModuleMap;
import com.sudoplay.joise.ModulePropertyMap;

public class ModuleCombiner extends
    Module {

  public enum CombinerType {
    ADD, MULT, MAX, MIN, AVG
  }

  private ScalarParameter[] sources = new ScalarParameter[MAX_SOURCES];
  private CombinerType type;

  public ModuleCombiner(CombinerType type) {
    this.setType(type);
  }

  public ModuleCombiner() {
    // serialization
  }

  public void setType(CombinerType type) {
    this.type = type;
  }

  public void setSource(int index, Module source) {
    this.sources[index] = new ScalarParameter(source);
  }

  public void setSource(int index, double source) {
    this.sources[index] = new ScalarParameter(source);
  }

  @SuppressWarnings("unused")
  public void clearAllSources() {

    for (int i = 0; i < MAX_SOURCES; i++) {
      this.sources[i] = null;
    }
  }

  @Override
  public double get(double x, double y) {

    switch (this.type) {
      case ADD:
        return this.addGet(x, y);

      case AVG:
        return this.avgGet(x, y);

      case MAX:
        return this.maxGet(x, y);

      case MIN:
        return this.minGet(x, y);

      case MULT:
        return this.multGet(x, y);

      default:
        return 0.0;
    }
  }

  @Override
  public double get(double x, double y, double z) {

    switch (this.type) {
      case ADD:
        return this.addGet(x, y, z);

      case AVG:
        return this.avgGet(x, y, z);

      case MAX:
        return this.maxGet(x, y, z);

      case MIN:
        return this.minGet(x, y, z);

      case MULT:
        return this.multGet(x, y, z);

      default:
        return 0.0;
    }
  }

  @Override
  public double get(double x, double y, double z, double w) {

    switch (this.type) {
      case ADD:
        return this.addGet(x, y, z, w);

      case AVG:
        return this.avgGet(x, y, z, w);

      case MAX:
        return this.maxGet(x, y, z, w);

      case MIN:
        return this.minGet(x, y, z, w);

      case MULT:
        return this.multGet(x, y, z, w);

      default:
        return 0.0;
    }
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {

    switch (this.type) {
      case ADD:
        return this.addGet(x, y, z, w, u, v);

      case AVG:
        return this.avgGet(x, y, z, w, u, v);

      case MAX:
        return this.maxGet(x, y, z, w, u, v);

      case MIN:
        return this.minGet(x, y, z, w, u, v);

      case MULT:
        return this.multGet(x, y, z, w, u, v);

      default:
        return 0.0;
    }
  }

  // ==========================================================================
  // = ADD
  // ==========================================================================

  private double addGet(double x, double y) {
    double value = 0.0;

    for (int i = 0; i < MAX_SOURCES; i++) {

      if (this.sources[i] != null) {
        value += this.sources[i].get(x, y);
      }
    }
    return value;
  }

  private double addGet(double x, double y, double z) {
    double value = 0.0;

    for (int i = 0; i < MAX_SOURCES; i++) {

      if (this.sources[i] != null) {
        value += this.sources[i].get(x, y, z);
      }
    }
    return value;
  }

  private double addGet(double x, double y, double z, double w) {
    double value = 0.0;

    for (int i = 0; i < MAX_SOURCES; i++) {

      if (this.sources[i] != null) {
        value += this.sources[i].get(x, y, z, w);
      }
    }
    return value;
  }

  private double addGet(double x, double y, double z, double w, double u, double v) {
    double value = 0.0;

    for (int i = 0; i < MAX_SOURCES; i++) {

      if (this.sources[i] != null) {
        value += this.sources[i].get(x, y, z, w, u, v);
      }
    }
    return value;
  }

  // ==========================================================================
  // = AVG
  // ==========================================================================

  private double avgGet(double x, double y) {
    int count = 0;
    double value = 0;

    for (int i = 0; i < MAX_SOURCES; i++) {

      if (this.sources[i] != null) {
        value += this.sources[i].get(x, y);
        count++;
      }
    }

    if (count == 0) {
      return 0.0;
    }
    return value / (double) count;
  }

  private double avgGet(double x, double y, double z) {
    int count = 0;
    double value = 0;

    for (int i = 0; i < MAX_SOURCES; i++) {

      if (this.sources[i] != null) {
        value += this.sources[i].get(x, y, z);
        count++;
      }
    }

    if (count == 0) {
      return 0.0;
    }
    return value / (double) count;
  }

  private double avgGet(double x, double y, double z, double w) {
    int count = 0;
    double value = 0;

    for (int i = 0; i < MAX_SOURCES; i++) {

      if (this.sources[i] != null) {
        value += this.sources[i].get(x, y, z, w);
        count++;
      }
    }

    if (count == 0) {
      return 0.0;
    }
    return value / (double) count;
  }

  private double avgGet(double x, double y, double z, double w, double u,
                        double v) {
    int count = 0;
    double value = 0;
    for (int i = 0; i < MAX_SOURCES; i++) {

      if (this.sources[i] != null) {
        value += this.sources[i].get(x, y, z, w, u, v);
        count++;
      }
    }

    if (count == 0) {
      return 0.0;
    }
    return value / (double) count;
  }

  // ==========================================================================
  // = MAX
  // ==========================================================================

  private double maxGet(double x, double y) {
    double mx;
    int c = 0;

    while (c < MAX_SOURCES && this.sources[c] == null) {
      c++;
    }

    if (c == MAX_SOURCES) {
      return 0.0;
    }
    mx = this.sources[c].get(x, y);

    for (int d = c; d < MAX_SOURCES; d++) {

      if (this.sources[d] != null) {
        double val = this.sources[d].get(x, y);

        if (val > mx) {
          mx = val;
        }
      }
    }

    return mx;
  }

  private double maxGet(double x, double y, double z) {
    double mx;
    int c = 0;

    while (c < MAX_SOURCES && this.sources[c] == null) {
      c++;
    }

    if (c == MAX_SOURCES) {
      return 0.0;
    }
    mx = this.sources[c].get(x, y, z);

    for (int d = c; d < MAX_SOURCES; d++) {

      if (this.sources[d] != null) {
        double val = this.sources[d].get(x, y, z);

        if (val > mx) {
          mx = val;
        }
      }
    }

    return mx;
  }

  private double maxGet(double x, double y, double z, double w) {
    double mx;
    int c = 0;

    while (c < MAX_SOURCES && this.sources[c] == null) {
      c++;
    }

    if (c == MAX_SOURCES) {
      return 0.0;
    }
    mx = this.sources[c].get(x, y, z, w);

    for (int d = c; d < MAX_SOURCES; d++) {

      if (this.sources[d] != null) {
        double val = this.sources[d].get(x, y, z, w);

        if (val > mx) {
          mx = val;
        }
      }
    }

    return mx;
  }

  private double maxGet(double x, double y, double z, double w, double u, double v) {
    double mx;
    int c = 0;

    while (c < MAX_SOURCES && this.sources[c] == null) {
      c++;
    }

    if (c == MAX_SOURCES) {
      return 0.0;
    }
    mx = this.sources[c].get(x, y, z, w, u, v);

    for (int d = c; d < MAX_SOURCES; d++) {

      if (this.sources[d] != null) {
        double val = this.sources[d].get(x, y, z, w, u, v);

        if (val > mx) {
          mx = val;
        }
      }
    }

    return mx;
  }

  // ==========================================================================
  // = MIN
  // ==========================================================================

  private double minGet(double x, double y) {
    double mn;
    int c = 0;

    while (c < MAX_SOURCES && this.sources[c] == null) {
      c++;
    }

    if (c == MAX_SOURCES) {
      return 0.0;
    }
    mn = this.sources[c].get(x, y);

    for (int d = c; d < MAX_SOURCES; d++) {

      if (this.sources[d] != null) {
        double val = this.sources[d].get(x, y);

        if (val < mn) {
          mn = val;
        }
      }
    }

    return mn;
  }

  private double minGet(double x, double y, double z) {
    double mn;
    int c = 0;

    while (c < MAX_SOURCES && this.sources[c] == null) {
      c++;
    }

    if (c == MAX_SOURCES) {
      return 0.0;
    }
    mn = this.sources[c].get(x, y, z);

    for (int d = c; d < MAX_SOURCES; d++) {

      if (this.sources[d] != null) {
        double val = this.sources[d].get(x, y, z);

        if (val < mn) {
          mn = val;
        }
      }
    }

    return mn;
  }

  private double minGet(double x, double y, double z, double w) {
    double mn;
    int c = 0;

    while (c < MAX_SOURCES && this.sources[c] == null) {
      c++;
    }

    if (c == MAX_SOURCES) {
      return 0.0;
    }
    mn = this.sources[c].get(x, y, z, w);

    for (int d = c; d < MAX_SOURCES; d++) {

      if (this.sources[d] != null) {
        double val = this.sources[d].get(x, y, z, w);

        if (val < mn) {
          mn = val;
        }
      }
    }

    return mn;
  }

  private double minGet(double x, double y, double z, double w, double u, double v) {
    double mn;
    int c = 0;

    while (c < MAX_SOURCES && this.sources[c] == null) {
      c++;
    }

    if (c == MAX_SOURCES) {
      return 0.0;
    }
    mn = this.sources[c].get(x, y, z, w, u, v);

    for (int d = c; d < MAX_SOURCES; d++) {

      if (this.sources[d] != null) {
        double val = this.sources[d].get(x, y, z, w, u, v);

        if (val < mn) {
          mn = val;
        }
      }
    }

    return mn;
  }

  // ==========================================================================
  // = MULT
  // ==========================================================================

  private double multGet(double x, double y) {
    double value = 1.0;

    for (int i = 0; i < MAX_SOURCES; i++) {

      if (this.sources[i] != null) {
        value *= this.sources[i].get(x, y);
      }
    }
    return value;
  }

  private double multGet(double x, double y, double z) {
    double value = 1.0;

    for (int i = 0; i < MAX_SOURCES; i++) {

      if (this.sources[i] != null) {
        value *= this.sources[i].get(x, y, z);
      }
    }
    return value;
  }

  private double multGet(double x, double y, double z, double w) {
    double value = 1.0;

    for (int i = 0; i < MAX_SOURCES; i++) {

      if (this.sources[i] != null) {
        value *= this.sources[i].get(x, y, z, w);
      }
    }
    return value;
  }

  private double multGet(double x, double y, double z, double w, double u, double v) {
    double value = 1.0;

    for (int i = 0; i < MAX_SOURCES; i++) {

      if (this.sources[i] != null) {
        value *= this.sources[i].get(x, y, z, w, u, v);
      }
    }
    return value;
  }

  @Override
  public void setSeed(String seedName, long seed) {

    for (int i = 0; i < MAX_SOURCES; i++) {

      if (this.sources[i] != null) {
        this.sources[i].setSeed(seedName, seed);
      }
    }
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    modulePropertyMap.writeEnum("type", this.type);

    for (int i = 0; i < MAX_SOURCES; i++) {
      modulePropertyMap.writeScalar("source" + i, this.sources[i], moduleMap);
    }
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap modulePropertyMap, ModuleInstanceMap moduleInstanceMap) {
    this.setType(modulePropertyMap.readEnum("type", CombinerType.class));

    String name;
    Object o;

    for (int i = 0; i < MAX_SOURCES; i++) {
      o = modulePropertyMap.get("source" + i);

      if (o != null) {
        name = o.toString();
        this.setSource(i, moduleInstanceMap.get(name));
      }
    }
    return this;
  }
}
