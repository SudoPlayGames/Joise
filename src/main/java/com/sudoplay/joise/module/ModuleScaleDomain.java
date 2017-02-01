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

public class ModuleScaleDomain extends
    SourcedModule {

  private static final double DEFAULT_SCALE = 1.0;

  private ScalarParameter sx = new ScalarParameter(DEFAULT_SCALE);
  private ScalarParameter sy = new ScalarParameter(DEFAULT_SCALE);
  private ScalarParameter sz = new ScalarParameter(DEFAULT_SCALE);
  private ScalarParameter sw = new ScalarParameter(DEFAULT_SCALE);
  private ScalarParameter su = new ScalarParameter(DEFAULT_SCALE);
  private ScalarParameter sv = new ScalarParameter(DEFAULT_SCALE);

  public void setScaleX(double x) {
    this.sx.set(x);
  }

  public void setScaleY(double y) {
    this.sy.set(y);
  }

  @SuppressWarnings("unused")
  public void setScaleZ(double z) {
    this.sz.set(z);
  }

  @SuppressWarnings("unused")
  public void setScaleW(double w) {
    this.sw.set(w);
  }

  @SuppressWarnings("unused")
  public void setScaleU(double u) {
    this.su.set(u);
  }

  @SuppressWarnings("unused")
  public void setScaleV(double v) {
    this.sv.set(v);
  }

  @SuppressWarnings("unused")
  public void setScaleX(Module x) {
    this.sx.set(x);
  }

  public void setScaleY(Module y) {
    this.sy.set(y);
  }

  @SuppressWarnings("unused")
  public void setScaleZ(Module z) {
    this.sz.set(z);
  }

  @SuppressWarnings("unused")
  public void setScaleW(Module w) {
    this.sw.set(w);
  }

  @SuppressWarnings("unused")
  public void setScaleU(Module u) {
    this.su.set(u);
  }

  @SuppressWarnings("unused")
  public void setScaleV(Module v) {
    this.sv.set(v);
  }

  @SuppressWarnings("unused")
  public void setScaleX(ScalarParameter scalarParameter) {
    this.sx.set(scalarParameter);
  }

  @SuppressWarnings("unused")
  public void setScaleY(ScalarParameter scalarParameter) {
    this.sy.set(scalarParameter);
  }

  @SuppressWarnings("unused")
  public void setScaleZ(ScalarParameter scalarParameter) {
    this.sz.set(scalarParameter);
  }

  @SuppressWarnings("unused")
  public void setScaleW(ScalarParameter scalarParameter) {
    this.sw.set(scalarParameter);
  }

  @SuppressWarnings("unused")
  public void setScaleU(ScalarParameter scalarParameter) {
    this.su.set(scalarParameter);
  }

  @SuppressWarnings("unused")
  public void setScaleV(ScalarParameter scalarParameter) {
    this.sv.set(scalarParameter);
  }

  @Override
  public double get(double x, double y) {
    return this.source.get(x * this.sx.get(x, y), y * this.sy.get(x, y));
  }

  @Override
  public double get(double x, double y, double z) {
    return this.source.get(x * this.sx.get(x, y, z), y * this.sy.get(x, y, z), z * this.sz.get(x, y, z));
  }

  @Override
  public double get(double x, double y, double z, double w) {
    return this.source.get(x * this.sx.get(x, y, z, w), y * this.sy.get(x, y, z, w),
        z * this.sz.get(x, y, z, w), w * this.sw.get(x, y, z, w));
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    return this.source.get(x * this.sx.get(x, y, z, w, u, v),
        y * this.sy.get(x, y, z, w, u, v), z * this.sz.get(x, y, z, w, u, v),
        w * this.sw.get(x, y, z, w, u, v), u * this.su.get(x, y, z, w, u, v),
        v * this.sv.get(x, y, z, w, u, v));
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    modulePropertyMap
        .writeScalar("x", this.sx, moduleMap)
        .writeScalar("y", this.sy, moduleMap)
        .writeScalar("z", this.sz, moduleMap)
        .writeScalar("w", this.sw, moduleMap)
        .writeScalar("u", this.su, moduleMap)
        .writeScalar("v", this.sv, moduleMap)
        .writeScalar("source", this.source, moduleMap);
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap modulePropertyMap, ModuleInstanceMap moduleInstanceMap) {
    this.setScaleX(modulePropertyMap.readScalar("x", moduleInstanceMap));
    this.setScaleY(modulePropertyMap.readScalar("y", moduleInstanceMap));
    this.setScaleZ(modulePropertyMap.readScalar("z", moduleInstanceMap));
    this.setScaleW(modulePropertyMap.readScalar("w", moduleInstanceMap));
    this.setScaleU(modulePropertyMap.readScalar("u", moduleInstanceMap));
    this.setScaleV(modulePropertyMap.readScalar("v", moduleInstanceMap));
    this.setSource(modulePropertyMap.readScalar("source", moduleInstanceMap));
    return this;
  }
}
