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

import static com.sudoplay.joise.noise.Util.bias;

import com.sudoplay.joise.ModuleInstanceMap;
import com.sudoplay.joise.ModuleMap;
import com.sudoplay.joise.ModulePropertyMap;

public class ModuleBias extends SourcedModule {

  protected ScalarParameter bias = new ScalarParameter(0.5);

  public ModuleBias() {}

  public ModuleBias(double bias) {
    this.bias.set(bias);
  }

  public ModuleBias(Module bias) {
    this.bias.set(bias);
  }

  public void setBias(double bias) {
    this.bias.set(bias);
  }

  public void setBias(Module bias) {
    this.bias.set(bias);
  }

  public void setBias(ScalarParameter scalarParameter) {
    this.bias.set(scalarParameter);
  }

  @Override
  public double get(double x, double y) {
    double val = source.get(x, y);
    return bias(bias.get(x, y), val);
  }

  @Override
  public double get(double x, double y, double z) {
    double val = source.get(x, y, z);
    return bias(bias.get(x, y, z), val);
  }

  @Override
  public double get(double x, double y, double z, double w) {
    double val = source.get(x, y, z, w);
    return bias(bias.get(x, y, z, w), val);
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    double val = source.get(x, y, z, w, u, v);
    return bias(bias.get(x, y, z, w, u, v), val);
  }

  @Override
  protected void _writeToMap(ModuleMap map) {

    ModulePropertyMap props = new ModulePropertyMap(this);

    writeScalar("bias", bias, props, map);
    writeSource(props, map);

    map.put(getId(), props);

  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap props,
      ModuleInstanceMap map) {

    this.setBias(readScalar("bias", props, map));
    readSource(props, map);

    return this;
  }

}
