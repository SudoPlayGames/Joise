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

package com.sudoplay.joise.module;

import com.sudoplay.joise.ModuleInstanceMap;
import com.sudoplay.joise.ModuleMap;
import com.sudoplay.joise.ModulePropertyMap;

public class ModuleCombiner extends Module {

  public static enum CombinerType {
    ADD, MULT, MAX, MIN, AVG
  }

  protected ScalarParameter[] sources = new ScalarParameter[MAX_SOURCES];
  protected CombinerType type;

  public ModuleCombiner(CombinerType type) {
    setType(type);
  }

  public ModuleCombiner() {
    // serialization
  }

  public void setType(CombinerType type) {
    this.type = type;
  }

  public void setSource(int index, Module source) {
    sources[index] = new ScalarParameter(source);
  }

  public void setSource(int index, double source) {
    sources[index] = new ScalarParameter(source);
  }

  public void clearAllSources() {
    for (int i = 0; i < MAX_SOURCES; i++) {
      sources[i] = null;
    }
  }

  @Override
  public double get(double x, double y) {
    switch (type) {
    case ADD:
      return addGet(x, y);
    case AVG:
      return avgGet(x, y);
    case MAX:
      return maxGet(x, y);
    case MIN:
      return minGet(x, y);
    case MULT:
      return multGet(x, y);
    default:
      return 0.0;
    }
  }

  @Override
  public double get(double x, double y, double z) {
    switch (type) {
    case ADD:
      return addGet(x, y, z);
    case AVG:
      return avgGet(x, y, z);
    case MAX:
      return maxGet(x, y, z);
    case MIN:
      return minGet(x, y, z);
    case MULT:
      return multGet(x, y, z);
    default:
      return 0.0;
    }
  }

  @Override
  public double get(double x, double y, double z, double w) {
    switch (type) {
    case ADD:
      return addGet(x, y, z, w);
    case AVG:
      return avgGet(x, y, z, w);
    case MAX:
      return maxGet(x, y, z, w);
    case MIN:
      return minGet(x, y, z, w);
    case MULT:
      return multGet(x, y, z, w);
    default:
      return 0.0;
    }
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    switch (type) {
    case ADD:
      return addGet(x, y, z, w, u, v);
    case AVG:
      return avgGet(x, y, z, w, u, v);
    case MAX:
      return maxGet(x, y, z, w, u, v);
    case MIN:
      return minGet(x, y, z, w, u, v);
    case MULT:
      return multGet(x, y, z, w, u, v);
    default:
      return 0.0;
    }
  }

  // ==========================================================================
  // = ADD
  // ==========================================================================

  protected double addGet(double x, double y) {
    double value = 0.0;
    for (int i = 0; i < MAX_SOURCES; i++) {
      if (sources[i] != null) value += sources[i].get(x, y);
    }
    return value;
  }

  protected double addGet(double x, double y, double z) {
    double value = 0.0;
    for (int i = 0; i < MAX_SOURCES; i++) {
      if (sources[i] != null) value += sources[i].get(x, y, z);
    }
    return value;
  }

  protected double addGet(double x, double y, double z, double w) {
    double value = 0.0;
    for (int i = 0; i < MAX_SOURCES; i++) {
      if (sources[i] != null) value += sources[i].get(x, y, z, w);
    }
    return value;
  }

  protected double addGet(double x, double y, double z, double w, double u,
      double v) {
    double value = 0.0;
    for (int i = 0; i < MAX_SOURCES; i++) {
      if (sources[i] != null) value += sources[i].get(x, y, z, w, u, v);
    }
    return value;
  }

  // ==========================================================================
  // = AVG
  // ==========================================================================

  protected double avgGet(double x, double y) {
    int count = 0;
    double value = 0;
    for (int i = 0; i < MAX_SOURCES; i++) {
      if (sources[i] != null) {
        value += sources[i].get(x, y);
        count++;
      }
    }
    if (count == 0) return 0.0;
    return value / (double) count;
  }

  protected double avgGet(double x, double y, double z) {
    int count = 0;
    double value = 0;
    for (int i = 0; i < MAX_SOURCES; i++) {
      if (sources[i] != null) {
        value += sources[i].get(x, y, z);
        count++;
      }
    }
    if (count == 0) return 0.0;
    return value / (double) count;
  }

  protected double avgGet(double x, double y, double z, double w) {
    int count = 0;
    double value = 0;
    for (int i = 0; i < MAX_SOURCES; i++) {
      if (sources[i] != null) {
        value += sources[i].get(x, y, z, w);
        count++;
      }
    }
    if (count == 0) return 0.0;
    return value / (double) count;
  }

  protected double avgGet(double x, double y, double z, double w, double u,
      double v) {
    int count = 0;
    double value = 0;
    for (int i = 0; i < MAX_SOURCES; i++) {
      if (sources[i] != null) {
        value += sources[i].get(x, y, z, w, u, v);
        count++;
      }
    }
    if (count == 0) return 0.0;
    return value / (double) count;
  }

  // ==========================================================================
  // = MAX
  // ==========================================================================

  protected double maxGet(double x, double y) {
    double mx;
    int c = 0;
    while (c < MAX_SOURCES && sources[c] == null) {
      c++;
    }
    if (c == MAX_SOURCES) return 0.0;
    mx = sources[c].get(x, y);

    for (int d = c; d < MAX_SOURCES; d++) {
      if (sources[d] != null) {
        double val = sources[d].get(x, y);
        if (val > mx) mx = val;
      }
    }

    return mx;
  }

  protected double maxGet(double x, double y, double z) {
    double mx;
    int c = 0;
    while (c < MAX_SOURCES && sources[c] == null) {
      c++;
    }
    if (c == MAX_SOURCES) return 0.0;
    mx = sources[c].get(x, y, z);

    for (int d = c; d < MAX_SOURCES; d++) {
      if (sources[d] != null) {
        double val = sources[d].get(x, y, z);
        if (val > mx) mx = val;
      }
    }

    return mx;
  }

  protected double maxGet(double x, double y, double z, double w) {
    double mx;
    int c = 0;
    while (c < MAX_SOURCES && sources[c] == null) {
      c++;
    }
    if (c == MAX_SOURCES) return 0.0;
    mx = sources[c].get(x, y, z, w);

    for (int d = c; d < MAX_SOURCES; d++) {
      if (sources[d] != null) {
        double val = sources[d].get(x, y, z, w);
        if (val > mx) mx = val;
      }
    }

    return mx;
  }

  protected double maxGet(double x, double y, double z, double w, double u,
      double v) {
    double mx;
    int c = 0;
    while (c < MAX_SOURCES && sources[c] == null) {
      c++;
    }
    if (c == MAX_SOURCES) return 0.0;
    mx = sources[c].get(x, y, z, w, u, v);

    for (int d = c; d < MAX_SOURCES; d++) {
      if (sources[d] != null) {
        double val = sources[d].get(x, y, z, w, u, v);
        if (val > mx) mx = val;
      }
    }

    return mx;
  }

  // ==========================================================================
  // = MIN
  // ==========================================================================

  protected double minGet(double x, double y) {
    double mn;
    int c = 0;
    while (c < MAX_SOURCES && sources[c] == null) {
      c++;
    }
    if (c == MAX_SOURCES) return 0.0;
    mn = sources[c].get(x, y);

    for (int d = c; d < MAX_SOURCES; d++) {
      if (sources[d] != null) {
        double val = sources[d].get(x, y);
        if (val < mn) mn = val;
      }
    }

    return mn;
  }

  protected double minGet(double x, double y, double z) {
    double mn;
    int c = 0;
    while (c < MAX_SOURCES && sources[c] == null) {
      c++;
    }
    if (c == MAX_SOURCES) return 0.0;
    mn = sources[c].get(x, y, z);

    for (int d = c; d < MAX_SOURCES; d++) {
      if (sources[d] != null) {
        double val = sources[d].get(x, y, z);
        if (val < mn) mn = val;
      }
    }

    return mn;
  }

  protected double minGet(double x, double y, double z, double w) {
    double mn;
    int c = 0;
    while (c < MAX_SOURCES && sources[c] == null) {
      c++;
    }
    if (c == MAX_SOURCES) return 0.0;
    mn = sources[c].get(x, y, z, w);

    for (int d = c; d < MAX_SOURCES; d++) {
      if (sources[d] != null) {
        double val = sources[d].get(x, y, z, w);
        if (val < mn) mn = val;
      }
    }

    return mn;
  }

  protected double minGet(double x, double y, double z, double w, double u,
      double v) {
    double mn;
    int c = 0;
    while (c < MAX_SOURCES && sources[c] == null) {
      c++;
    }
    if (c == MAX_SOURCES) return 0.0;
    mn = sources[c].get(x, y, z, w, u, v);

    for (int d = c; d < MAX_SOURCES; d++) {
      if (sources[d] != null) {
        double val = sources[d].get(x, y, z, w, u, v);
        if (val < mn) mn = val;
      }
    }

    return mn;
  }

  // ==========================================================================
  // = MULT
  // ==========================================================================

  protected double multGet(double x, double y) {
    double value = 1.0;
    for (int i = 0; i < MAX_SOURCES; i++) {
      if (sources[i] != null) value *= sources[i].get(x, y);
    }
    return value;
  }

  protected double multGet(double x, double y, double z) {
    double value = 1.0;
    for (int i = 0; i < MAX_SOURCES; i++) {
      if (sources[i] != null) value *= sources[i].get(x, y, z);
    }
    return value;
  }

  protected double multGet(double x, double y, double z, double w) {
    double value = 1.0;
    for (int i = 0; i < MAX_SOURCES; i++) {
      if (sources[i] != null) value *= sources[i].get(x, y, z, w);
    }
    return value;
  }

  protected double multGet(double x, double y, double z, double w, double u,
      double v) {
    double value = 1.0;
    for (int i = 0; i < MAX_SOURCES; i++) {
      if (sources[i] != null) value *= sources[i].get(x, y, z, w, u, v);
    }
    return value;
  }

  @Override
  protected void _writeToMap(ModuleMap map) {

    ModulePropertyMap props = new ModulePropertyMap(this);

    writeEnum("type", type, props);

    for (int i = 0; i < MAX_SOURCES; i++) {
      writeScalar("source" + i, sources[i], props, map);
    }

    map.put(getId(), props);

  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap props,
      ModuleInstanceMap map) {

    readEnum("type", "setType", CombinerType.class, props);

    String name;
    Object o;
    for (int i = 0; i < MAX_SOURCES; i++) {
      o = props.get("source" + i);
      if (o != null) {
        name = o.toString();
        setSource(i, map.get(name));
      }
    }

    return this;
  }

}
