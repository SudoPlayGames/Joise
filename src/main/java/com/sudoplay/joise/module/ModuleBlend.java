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
import com.sudoplay.joise.noise.Util;

public class ModuleBlend extends
    Module {

  private ScalarParameter low = new ScalarParameter(0.0);
  private ScalarParameter high = new ScalarParameter(1.0);
  private ScalarParameter control = new ScalarParameter(0.5);

  @SuppressWarnings("unused")
  public void setLowSource(double source) {
    this.low.set(source);
  }

  @SuppressWarnings("unused")
  public void setHighSource(double source) {
    this.high.set(source);
  }

  @SuppressWarnings("unused")
  public void setControlSource(double source) {
    this.control.set(source);
  }

  @SuppressWarnings("unused")
  public void setLowSource(Module source) {
    this.low.set(source);
  }

  @SuppressWarnings("unused")
  public void setHighSource(Module source) {
    this.high.set(source);
  }

  @SuppressWarnings("unused")
  public void setControlSource(Module source) {
    this.control.set(source);
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public void setLowSource(ScalarParameter scalarParameter) {
    this.low.set(scalarParameter);
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public void setHighSource(ScalarParameter scalarParameter) {
    this.high.set(scalarParameter);
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public void setControlSource(ScalarParameter scalarParameter) {
    this.control.set(scalarParameter);
  }

  @Override
  public double get(double x, double y) {
    double v1 = this.low.get(x, y);
    double v2 = this.high.get(x, y);
    double bl = this.control.get(x, y);
    bl = (bl + 1.0) * 0.5;
    return Util.lerp(bl, v1, v2);
  }

  @Override
  public double get(double x, double y, double z) {
    double v1 = this.low.get(x, y, z);
    double v2 = this.high.get(x, y, z);
    double bl = this.control.get(x, y, z);
    return Util.lerp(bl, v1, v2);
  }

  @Override
  public double get(double x, double y, double z, double w) {
    double v1 = this.low.get(x, y, z, w);
    double v2 = this.high.get(x, y, z, w);
    double bl = this.control.get(x, y, z, w);
    return Util.lerp(bl, v1, v2);
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    double v1 = this.low.get(x, y, z, w, u, v);
    double v2 = this.high.get(x, y, z, w, u, v);
    double bl = this.control.get(x, y, z, w, u, v);
    return Util.lerp(bl, v1, v2);
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    modulePropertyMap
        .writeScalar("high", this.high, moduleMap)
        .writeScalar("low", this.low, moduleMap)
        .writeScalar("control", this.control, moduleMap);
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap modulePropertyMap, ModuleInstanceMap moduleInstanceMap) {
    this.setHighSource(modulePropertyMap.readScalar("high", moduleInstanceMap));
    this.setLowSource(modulePropertyMap.readScalar("low", moduleInstanceMap));
    this.setControlSource(modulePropertyMap.readScalar("control", moduleInstanceMap));
    return this;
  }

}
