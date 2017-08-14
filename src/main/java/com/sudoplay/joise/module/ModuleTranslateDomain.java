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

public class ModuleTranslateDomain extends
    SourcedModule {

  private ScalarParameter ax = new ScalarParameter(0);
  private ScalarParameter ay = new ScalarParameter(0);
  private ScalarParameter az = new ScalarParameter(0);
  private ScalarParameter aw = new ScalarParameter(0);
  private ScalarParameter au = new ScalarParameter(0);
  private ScalarParameter av = new ScalarParameter(0);

  public void setAxisXSource(double source) {
    this.ax.set(source);
  }

  public void setAxisYSource(double source) {
    this.ay.set(source);
  }

  @SuppressWarnings("unused")
  public void setAxisZSource(double source) {
    this.az.set(source);
  }

  @SuppressWarnings("unused")
  public void setAxisWSource(double source) {
    this.aw.set(source);
  }

  @SuppressWarnings("unused")
  public void setAxisUSource(double source) {
    this.au.set(source);
  }

  @SuppressWarnings("unused")
  public void setAxisVSource(double source) {
    this.av.set(source);
  }

  public void setAxisXSource(Module source) {
    this.ax.set(source);
  }

  public void setAxisYSource(Module source) {
    this.ay.set(source);
  }

  @SuppressWarnings("unused")
  public void setAxisZSource(Module source) {
    this.az.set(source);
  }

  @SuppressWarnings("unused")
  public void setAxisWSource(Module source) {
    this.aw.set(source);
  }

  @SuppressWarnings("unused")
  public void setAxisUSource(Module source) {
    this.au.set(source);
  }

  @SuppressWarnings("unused")
  public void setAxisVSource(Module source) {
    this.av.set(source);
  }

  @SuppressWarnings("unused")
  public void setAxisXSource(ScalarParameter scalarParameter) {
    this.ax.set(scalarParameter);
  }

  @SuppressWarnings("unused")
  public void setAxisYSource(ScalarParameter scalarParameter) {
    this.ay.set(scalarParameter);
  }

  @SuppressWarnings("unused")
  public void setAxisZSource(ScalarParameter scalarParameter) {
    this.az.set(scalarParameter);
  }

  @SuppressWarnings("unused")
  public void setAxisWSource(ScalarParameter scalarParameter) {
    this.aw.set(scalarParameter);
  }

  @SuppressWarnings("unused")
  public void setAxisUSource(ScalarParameter scalarParameter) {
    this.au.set(scalarParameter);
  }

  @SuppressWarnings("unused")
  public void setAxisVSource(ScalarParameter scalarParameter) {
    this.av.set(scalarParameter);
  }

  @Override
  public double get(double x, double y) {
    return this.source.get(x + this.ax.get(x, y), y + this.ay.get(x, y));
  }

  @Override
  public double get(double x, double y, double z) {
    return this.source.get(x + this.ax.get(x, y, z), y + this.ay.get(x, y, z), z + this.az.get(x, y, z));
  }

  @Override
  public double get(double x, double y, double z, double w) {
    return this.source.get(x + this.ax.get(x, y, z, w), y + this.ay.get(x, y, z, w),
        z + this.az.get(x, y, z, w), w + this.aw.get(x, y, z, w));
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    return this.source.get(x + this.ax.get(x, y, z, w, u, v),
        y + this.ay.get(x, y, z, w, u, v), z + this.az.get(x, y, z, w, u, v),
        w + this.aw.get(x, y, z, w, u, v), u + this.au.get(x, y, z, w, u, v),
        v + this.av.get(x, y, z, w, u, v));
  }

  @Override
  public void setSeed(String seedName, long seed) {
    super.setSeed(seedName, seed);
    this.ax.setSeed(seedName, seed);
    this.ay.setSeed(seedName, seed);
    this.az.setSeed(seedName, seed);
    this.aw.setSeed(seedName, seed);
    this.au.setSeed(seedName, seed);
    this.av.setSeed(seedName, seed);
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    modulePropertyMap
        .writeScalar("axisX", this.ax, moduleMap)
        .writeScalar("axisY", this.ay, moduleMap)
        .writeScalar("axisZ", this.az, moduleMap)
        .writeScalar("axisW", this.aw, moduleMap)
        .writeScalar("axisU", this.au, moduleMap)
        .writeScalar("axisV", this.av, moduleMap)
        .writeScalar("source", this.source, moduleMap);
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap modulePropertyMap, ModuleInstanceMap moduleInstanceMap) {
    this.setAxisXSource(modulePropertyMap.readScalar("axisX", moduleInstanceMap));
    this.setAxisYSource(modulePropertyMap.readScalar("axisY", moduleInstanceMap));
    this.setAxisZSource(modulePropertyMap.readScalar("axisZ", moduleInstanceMap));
    this.setAxisWSource(modulePropertyMap.readScalar("axisW", moduleInstanceMap));
    this.setAxisUSource(modulePropertyMap.readScalar("axisU", moduleInstanceMap));
    this.setAxisVSource(modulePropertyMap.readScalar("axisV", moduleInstanceMap));
    this.setSource(modulePropertyMap.readScalar("source", moduleInstanceMap));
    return this;
  }
}
