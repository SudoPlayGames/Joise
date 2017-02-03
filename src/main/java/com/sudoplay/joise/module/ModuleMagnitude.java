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

public class ModuleMagnitude extends
    Module {

  private ScalarParameter sX = new ScalarParameter(0);
  private ScalarParameter sY = new ScalarParameter(0);
  private ScalarParameter sZ = new ScalarParameter(0);
  private ScalarParameter sW = new ScalarParameter(0);
  private ScalarParameter sU = new ScalarParameter(0);
  private ScalarParameter sV = new ScalarParameter(0);

  public void setX(double source) {
    this.sX.set(source);
  }

  public void setY(double source) {
    this.sY.set(source);
  }

  public void setZ(double source) {
    this.sZ.set(source);
  }

  public void setW(double source) {
    this.sW.set(source);
  }

  public void setU(double source) {
    this.sU.set(source);
  }

  public void setV(double source) {
    this.sV.set(source);
  }

  public void setX(Module source) {
    this.sX.set(source);
  }

  public void setY(Module source) {
    this.sY.set(source);
  }

  public void setZ(Module source) {
    this.sZ.set(source);
  }

  public void setW(Module source) {
    this.sW.set(source);
  }

  public void setU(Module source) {
    this.sU.set(source);
  }

  public void setV(Module source) {
    this.sV.set(source);
  }

  public void setX(ScalarParameter scalarParameter) {
    this.sX.set(scalarParameter);
  }

  public void setY(ScalarParameter scalarParameter) {
    this.sY.set(scalarParameter);
  }

  public void setZ(ScalarParameter scalarParameter) {
    this.sZ.set(scalarParameter);
  }

  public void setW(ScalarParameter scalarParameter) {
    this.sW.set(scalarParameter);
  }

  public void setU(ScalarParameter scalarParameter) {
    this.sU.set(scalarParameter);
  }

  public void setV(ScalarParameter scalarParameter) {
    this.sV.set(scalarParameter);
  }

  @Override
  public double get(double x, double y) {
    double xx = this.sX.get(x, y);
    double yy = this.sY.get(x, y);
    return Math.sqrt(xx * xx + yy * yy);
  }

  @Override
  public double get(double x, double y, double z) {
    double xx = this.sX.get(x, y, z);
    double yy = this.sY.get(x, y, z);
    double zz = this.sZ.get(x, y, z);
    return Math.sqrt(xx * xx + yy * yy + zz * zz);
  }

  @Override
  public double get(double x, double y, double z, double w) {
    double xx = this.sX.get(x, y, z, w);
    double yy = this.sY.get(x, y, z, w);
    double zz = this.sZ.get(x, y, z, w);
    double ww = this.sW.get(x, y, z, w);
    return Math.sqrt(xx * xx + yy * yy + zz * zz + ww * ww);
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    double xx = this.sX.get(x, y, z, w, u, v);
    double yy = this.sY.get(x, y, z, w, u, v);
    double zz = this.sZ.get(x, y, z, w, u, v);
    double ww = this.sW.get(x, y, z, w, u, v);
    double uu = this.sU.get(x, y, z, w, u, v);
    double vv = this.sV.get(x, y, z, w, u, v);
    return Math.sqrt(xx * xx + yy * yy + zz * zz + ww * ww + uu * uu + vv * vv);
  }

  @Override
  public void setSeed(String seedName, long seed) {
    this.sX.setSeed(seedName, seed);
    this.sY.setSeed(seedName, seed);
    this.sZ.setSeed(seedName, seed);
    this.sW.setSeed(seedName, seed);
    this.sU.setSeed(seedName, seed);
    this.sV.setSeed(seedName, seed);
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    modulePropertyMap
        .writeScalar("X", this.sX, moduleMap)
        .writeScalar("Y", this.sY, moduleMap)
        .writeScalar("Z", this.sZ, moduleMap)
        .writeScalar("W", this.sW, moduleMap)
        .writeScalar("U", this.sU, moduleMap)
        .writeScalar("V", this.sV, moduleMap);
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap modulePropertyMap, ModuleInstanceMap moduleInstanceMap) {
    this.setX(modulePropertyMap.readScalar("X", moduleInstanceMap));
    this.setY(modulePropertyMap.readScalar("Y", moduleInstanceMap));
    this.setZ(modulePropertyMap.readScalar("Z", moduleInstanceMap));
    this.setW(modulePropertyMap.readScalar("W", moduleInstanceMap));
    this.setU(modulePropertyMap.readScalar("U", moduleInstanceMap));
    this.setV(modulePropertyMap.readScalar("V", moduleInstanceMap));
    return this;
  }

}
