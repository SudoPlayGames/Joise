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

public class ModuleBias extends
    SourcedModule {

  private ScalarParameter bias = new ScalarParameter(0.5);

  public ModuleBias() {
  }

  public ModuleBias(double bias) {
    this.bias.set(bias);
  }

  @SuppressWarnings("unused")
  public ModuleBias(Module bias) {
    this.bias.set(bias);
  }

  public void setBias(double bias) {
    this.bias.set(bias);
  }

  public void setBias(Module bias) {
    this.bias.set(bias);
  }

  @SuppressWarnings("unused")
  public void setBias(ScalarParameter scalarParameter) {
    this.bias.set(scalarParameter);
  }

  @Override
  public double get(double x, double y) {
    double val = this.source.get(x, y);
    return Util.bias(this.bias.get(x, y), val);
  }

  @Override
  public double get(double x, double y, double z) {
    double val = this.source.get(x, y, z);
    return Util.bias(this.bias.get(x, y, z), val);
  }

  @Override
  public double get(double x, double y, double z, double w) {
    double val = this.source.get(x, y, z, w);
    return Util.bias(this.bias.get(x, y, z, w), val);
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    double val = this.source.get(x, y, z, w, u, v);
    return Util.bias(this.bias.get(x, y, z, w, u, v), val);
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    modulePropertyMap
        .writeScalar("source", this.source, moduleMap)
        .writeScalar("bias", this.bias, moduleMap);
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap modulePropertyMap, ModuleInstanceMap moduleInstanceMap) {
    this.setBias(modulePropertyMap.readScalar("bias", moduleInstanceMap));
    this.setSource(modulePropertyMap.readScalar("source", moduleInstanceMap));
    return this;
  }

}
