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

public class ModuleBrightContrast extends SourcedModule {

  protected ScalarParameter bright = new ScalarParameter(0.0);
  protected ScalarParameter threshold = new ScalarParameter(0.0);
  protected ScalarParameter factor = new ScalarParameter(1.0);

  public void setBrightness(double b) {
    bright.set(b);
  }

  public void setContrastThreshold(double t) {
    threshold.set(t);
  }

  public void setContrastFactor(double f) {
    factor.set(f);
  }

  public void setBrightness(Module source) {
    bright.set(source);
  }

  public void setContrastThreshold(Module source) {
    threshold.set(source);
  }

  public void setContrastFactor(Module source) {
    factor.set(source);
  }

  public void setBrightness(ScalarParameter scalarParameter) {
    bright.set(scalarParameter);
  }

  public void setContrastThreshold(ScalarParameter scalarParameter) {
    threshold.set(scalarParameter);
  }

  public void setContrastFactor(ScalarParameter scalarParameter) {
    factor.set(scalarParameter);
  }

  @Override
  public double get(double x, double y) {
    double val = source.get(x, y);
    // apply brightness
    val += bright.get(x, y);
    // subtract threshold, scale by factor, add threshold
    double t = threshold.get(x, y);
    val -= t;
    val *= factor.get(x, y);
    val += t;
    return val;
  }

  @Override
  public double get(double x, double y, double z) {
    double val = source.get(x, y, z);
    // apply brightness
    val += bright.get(x, y, z);
    // subtract threshold, scale by factor, add threshold
    double t = threshold.get(x, y, z);
    val -= t;
    val *= factor.get(x, y, z);
    val += t;
    return val;
  }

  @Override
  public double get(double x, double y, double z, double w) {
    double val = source.get(x, y, z, w);
    // apply brightness
    val += bright.get(x, y, z, w);
    // subtract threshold, scale by factor, add threshold
    double t = threshold.get(x, y, z, w);
    val -= t;
    val *= factor.get(x, y, z, w);
    val += t;
    return val;
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    double val = source.get(x, y, z, w, u, v);
    // apply brightness
    val += bright.get(x, y, z, w, u, v);
    // subtract threshold, scale by factor, add threshold
    double t = threshold.get(x, y, z, w, u, v);
    val -= t;
    val *= factor.get(x, y, z, w, u, v);
    val += t;
    return val;
  }

  @Override
  protected void _writeToMap(ModuleMap map) {

    ModulePropertyMap props = new ModulePropertyMap(this);

    writeScalar("brightness", bright, props, map);
    writeScalar("contrastFactor", factor, props, map);
    writeScalar("contrastThreshold", threshold, props, map);
    writeSource(props, map);

    map.put(getId(), props);

  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap props,
      ModuleInstanceMap map) {

    this.setBrightness(readScalar("brightness", props, map));
    this.setContrastFactor(readScalar("contrastFactor", props, map));
    this.setContrastThreshold(readScalar("contrastThreshold", props, map));
    readSource(props, map);

    return this;
  }

}
