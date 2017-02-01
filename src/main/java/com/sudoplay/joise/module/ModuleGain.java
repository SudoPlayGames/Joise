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

public class ModuleGain extends
    SourcedModule {

  private static final double DEFAULT_GAIN = 0.5;

  private ScalarParameter gain = new ScalarParameter(DEFAULT_GAIN);

  public ModuleGain() {
    this.setGain(DEFAULT_GAIN);
  }

  @SuppressWarnings("unused")
  public ModuleGain(double gain) {
    this.setGain(gain);
  }

  public void setGain(double gain) {
    this.gain.set(gain);
  }

  public void setGain(Module gain) {
    this.gain.set(gain);
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public void setGain(ScalarParameter scalarParameter) {
    this.gain.set(scalarParameter);
  }

  @Override
  public double get(double x, double y) {
    return Util.gain(this.gain.get(x, y), this.source.get(x, y));
  }

  @Override
  public double get(double x, double y, double z) {
    return Util.gain(this.gain.get(x, y, z), this.source.get(x, y, z));
  }

  @Override
  public double get(double x, double y, double z, double w) {
    return Util.gain(this.gain.get(x, y, z, w), this.source.get(x, y, z, w));
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    return Util.gain(this.gain.get(x, y, z, w, u, v), this.source.get(x, y, z, w, u, v));
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    modulePropertyMap
        .writeScalar("gain", this.gain, moduleMap)
        .writeScalar("source", this.source, moduleMap);
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap modulePropertyMap, ModuleInstanceMap moduleInstanceMap) {
    this.setGain(modulePropertyMap.readScalar("gain", moduleInstanceMap));
    this.setSource(modulePropertyMap.readScalar("source", moduleInstanceMap));
    return this;
  }

}
