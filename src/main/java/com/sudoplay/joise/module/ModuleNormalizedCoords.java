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

public class ModuleNormalizedCoords extends
    SourcedModule {

  private static final double DEFAULT_LENGTH = 1.0;

  private ScalarParameter length = new ScalarParameter(DEFAULT_LENGTH);

  public ModuleNormalizedCoords() {
    this(DEFAULT_LENGTH);
  }

  @SuppressWarnings("WeakerAccess")
  public ModuleNormalizedCoords(double length) {
    this.setLength(length);
  }

  public void setLength(double source) {
    this.length.set(source);
  }

  public void setLength(Module source) {
    this.length.set(source);
  }

  @SuppressWarnings("unused")
  public void setLength(ScalarParameter scalarParameter) {
    this.length.set(scalarParameter);
  }

  @Override
  public double get(double x, double y) {

    if (x == 0 && y == 0) {
      return this.source.get(x, y);
    }

    double len = Math.sqrt(x * x + y * y);
    double r = this.length.get(x, y);
    return this.source.get(x / len * r, y / len * r);
  }

  @Override
  public double get(double x, double y, double z) {

    if (x == 0 && y == 0 && z == 0) {
      return this.source.get(x, y, z);
    }

    double len = Math.sqrt(x * x + y * y + z * z);
    double r = this.length.get(x, y, z);
    return this.source.get(x / len * r, y / len * r, z / len * r);
  }

  @Override
  public double get(double x, double y, double z, double w) {

    if (x == 0 && y == 0 && z == 0 && w == 0) {
      return this.source.get(x, y, z, w);
    }

    double len = Math.sqrt(x * x + y * y + z * z + w * w);
    double r = this.length.get(x, y, z, w);
    return this.source.get(x / len * r, y / len * r, z / len * r, w / len * r);
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {

    if (x == 0 && y == 0 && z == 0 && w == 0 && u == 0 && v == 0) {
      return this.source.get(x, y, z, w, u, v);
    }

    double len = Math.sqrt(x * x + y * y + z * z + w * w + u * u + v * v);
    double r = this.length.get(x, y, z, w, u, v);
    return this.source.get(x / len * r, y / len * r, z / len * r, w / len * r, u / len * r, v / len * r);
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    modulePropertyMap
        .writeScalar("length", this.length, moduleMap)
        .writeScalar("source", this.source, moduleMap);
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap modulePropertyMap, ModuleInstanceMap moduleInstanceMap) {
    this.setLength(modulePropertyMap.readScalar("length", moduleInstanceMap));
    this.setSource(modulePropertyMap.readScalar("source", moduleInstanceMap));
    return this;
  }
}
