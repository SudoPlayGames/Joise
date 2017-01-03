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

public class ModuleMagnitude extends Module {

  protected ScalarParameter sX = new ScalarParameter(0);
  protected ScalarParameter sY = new ScalarParameter(0);
  protected ScalarParameter sZ = new ScalarParameter(0);
  protected ScalarParameter sW = new ScalarParameter(0);
  protected ScalarParameter sU = new ScalarParameter(0);
  protected ScalarParameter sV = new ScalarParameter(0);

  public void setX(double source) {
    sX.set(source);
  }

  public void setY(double source) {
    sY.set(source);
  }

  public void setZ(double source) {
    sZ.set(source);
  }

  public void setW(double source) {
    sW.set(source);
  }

  public void setU(double source) {
    sU.set(source);
  }

  public void setV(double source) {
    sV.set(source);
  }

  public void setX(Module source) {
    sX.set(source);
  }

  public void setY(Module source) {
    sY.set(source);
  }

  public void setZ(Module source) {
    sZ.set(source);
  }

  public void setW(Module source) {
    sW.set(source);
  }

  public void setU(Module source) {
    sU.set(source);
  }

  public void setV(Module source) {
    sV.set(source);
  }

  public void setX(ScalarParameter scalarParameter) {
    sX.set(scalarParameter);
  }

  public void setY(ScalarParameter scalarParameter) {
    sY.set(scalarParameter);
  }

  public void setZ(ScalarParameter scalarParameter) {
    sZ.set(scalarParameter);
  }

  public void setW(ScalarParameter scalarParameter) {
    sW.set(scalarParameter);
  }

  public void setU(ScalarParameter scalarParameter) {
    sU.set(scalarParameter);
  }

  public void setV(ScalarParameter scalarParameter) {
    sV.set(scalarParameter);
  }

  @Override
  public double get(double x, double y) {
    double xx = sX.get(x, y);
    double yy = sY.get(x, y);
    return Math.sqrt(xx * xx + yy * yy);
  }

  @Override
  public double get(double x, double y, double z) {
    double xx = sX.get(x, y, z);
    double yy = sY.get(x, y, z);
    double zz = sZ.get(x, y, z);
    return Math.sqrt(xx * xx + yy * yy + zz * zz);
  }

  @Override
  public double get(double x, double y, double z, double w) {
    double xx = sX.get(x, y, z, w);
    double yy = sY.get(x, y, z, w);
    double zz = sZ.get(x, y, z, w);
    double ww = sW.get(x, y, z, w);
    return Math.sqrt(xx * xx + yy * yy + zz * zz + ww * ww);
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    double xx = sX.get(x, y, z, w, u, v);
    double yy = sY.get(x, y, z, w, u, v);
    double zz = sZ.get(x, y, z, w, u, v);
    double ww = sW.get(x, y, z, w, u, v);
    double uu = sU.get(x, y, z, w, u, v);
    double vv = sV.get(x, y, z, w, u, v);
    return Math.sqrt(xx * xx + yy * yy + zz * zz + ww * ww + uu * uu + vv * vv);
  }

  @Override
  protected void _writeToMap(ModuleMap map) {

    ModulePropertyMap props = new ModulePropertyMap(this);

    writeScalar("X", sX, props, map);
    writeScalar("Y", sY, props, map);
    writeScalar("Z", sZ, props, map);
    writeScalar("W", sW, props, map);
    writeScalar("U", sU, props, map);
    writeScalar("V", sV, props, map);

    map.put(getId(), props);

  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap props,
      ModuleInstanceMap map) {

    this.setX(readScalar("X", props, map));
    this.setY(readScalar("Y", props, map));
    this.setZ(readScalar("Z", props, map));
    this.setW(readScalar("W", props, map));
    this.setU(readScalar("U", props, map));
    this.setV(readScalar("V", props, map));

    return this;
  }

}
