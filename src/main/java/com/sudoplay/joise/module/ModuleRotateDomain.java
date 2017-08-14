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

public class ModuleRotateDomain extends
    SourcedModule {

  private double[][] rotmatrix = new double[3][3];
  private ScalarParameter ax = new ScalarParameter(0);
  private ScalarParameter ay = new ScalarParameter(0);
  private ScalarParameter az = new ScalarParameter(0);
  private ScalarParameter axisangle = new ScalarParameter(0);

  public void setAxis(double ax, double ay, double az) {
    this.ax.set(ax);
    this.ay.set(ay);
    this.az.set(az);
  }

  @SuppressWarnings("unused")
  public void setAxis(Module ax, Module ay, Module az) {
    this.ax.set(ax);
    this.ay.set(ay);
    this.az.set(az);
  }

  @SuppressWarnings("unused")
  public void setAxisX(double ax) {
    this.ax.set(ax);
  }

  @SuppressWarnings("unused")
  public void setAxisY(double ay) {
    this.ay.set(ay);
  }

  @SuppressWarnings("unused")
  public void setAxisZ(double az) {
    this.az.set(az);
  }

  public void setAngle(double a) {
    this.axisangle.set(a);
  }

  @SuppressWarnings("unused")
  public void setAxisX(Module ax) {
    this.ax.set(ax);
  }

  @SuppressWarnings("unused")
  public void setAxisY(Module ay) {
    this.ay.set(ay);
  }

  @SuppressWarnings("unused")
  public void setAxisZ(Module az) {
    this.az.set(az);
  }

  @SuppressWarnings("unused")
  public void setAngle(Module a) {
    this.axisangle.set(a);
  }

  @SuppressWarnings("unused")
  public void setAxisX(ScalarParameter scalarParameter) {
    this.ax.set(scalarParameter);
  }

  @SuppressWarnings("unused")
  public void setAxisY(ScalarParameter scalarParameter) {
    this.ay.set(scalarParameter);
  }

  @SuppressWarnings("unused")
  public void setAxisZ(ScalarParameter scalarParameter) {
    this.az.set(scalarParameter);
  }

  @SuppressWarnings("unused")
  public void setAngle(ScalarParameter scalarParameter) {
    this.axisangle.set(scalarParameter);
  }

  @Override
  public double get(double x, double y) {
    double nx, ny;
    double angle = this.axisangle.get(x, y) * Util.TWO_PI;
    double cos2d = Math.cos(angle);
    double sin2d = Math.sin(angle);
    nx = x * cos2d - y * sin2d;
    ny = y * cos2d + x * sin2d;
    return this.source.get(nx, ny);
  }

  @Override
  public double get(double x, double y, double z) {
    this.calculateRotMatrix(x, y, z);
    double nx, ny, nz;
    nx = (this.rotmatrix[0][0] * x) + (this.rotmatrix[1][0] * y) + (this.rotmatrix[2][0] * z);
    ny = (this.rotmatrix[0][1] * x) + (this.rotmatrix[1][1] * y) + (this.rotmatrix[2][1] * z);
    nz = (this.rotmatrix[0][2] * x) + (this.rotmatrix[1][2] * y) + (this.rotmatrix[2][2] * z);
    return this.source.get(nx, ny, nz);
  }

  @Override
  public double get(double x, double y, double z, double w) {
    this.calculateRotMatrix(x, y, z, w);
    double nx, ny, nz;
    nx = (this.rotmatrix[0][0] * x) + (this.rotmatrix[1][0] * y) + (this.rotmatrix[2][0] * z);
    ny = (this.rotmatrix[0][1] * x) + (this.rotmatrix[1][1] * y) + (this.rotmatrix[2][1] * z);
    nz = (this.rotmatrix[0][2] * x) + (this.rotmatrix[1][2] * y) + (this.rotmatrix[2][2] * z);
    return this.source.get(nx, ny, nz, w);
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    this.calculateRotMatrix(x, y, z, w, u, v);
    double nx, ny, nz;
    nx = (this.rotmatrix[0][0] * x) + (this.rotmatrix[1][0] * y) + (this.rotmatrix[2][0] * z);
    ny = (this.rotmatrix[0][1] * x) + (this.rotmatrix[1][1] * y) + (this.rotmatrix[2][1] * z);
    nz = (this.rotmatrix[0][2] * x) + (this.rotmatrix[1][2] * y) + (this.rotmatrix[2][2] * z);
    return this.source.get(nx, ny, nz, w, u, v);
  }

  private void calculateRotMatrix(double x, double y, double z) {
    double angle = this.axisangle.get(x, y, z) * Util.TWO_PI;
    double ax = this.ax.get(x, y, z);
    double ay = this.ay.get(x, y, z);
    double az = this.az.get(x, y, z);
    this.calc(angle, ax, ay, az);
  }

  private void calculateRotMatrix(double x, double y, double z, double w) {
    double angle = this.axisangle.get(x, y, z, w) * Util.TWO_PI;
    double ax = this.ax.get(x, y, z, w);
    double ay = this.ay.get(x, y, z, w);
    double az = this.az.get(x, y, z, w);
    this.calc(angle, ax, ay, az);
  }

  private void calculateRotMatrix(double x, double y, double z, double w, double u, double v) {
    double angle = this.axisangle.get(x, y, z, w, u, v) * Util.TWO_PI;
    double ax = this.ax.get(x, y, z, w, u, v);
    double ay = this.ay.get(x, y, z, w, u, v);
    double az = this.az.get(x, y, z, w, u, v);
    this.calc(angle, ax, ay, az);
  }

  private void calc(double angle, double ax, double ay, double az) {
    double cosangle = Math.cos(angle);
    double sinangle = Math.sin(angle);

    this.rotmatrix[0][0] = 1.0 + (1.0 - cosangle) * (ax * ax - 1.0);
    this.rotmatrix[1][0] = -az * sinangle + (1.0 - cosangle) * ax * ay;
    this.rotmatrix[2][0] = ay * sinangle + (1.0 - cosangle) * ax * az;

    this.rotmatrix[0][1] = az * sinangle + (1.0 - cosangle) * ax * ay;
    this.rotmatrix[1][1] = 1.0 + (1.0 - cosangle) * (ay * ay - 1.0);
    this.rotmatrix[2][1] = -ax * sinangle + (1.0 - cosangle) * ay * az;

    this.rotmatrix[0][2] = -ay * sinangle + (1.0 - cosangle) * ax * az;
    this.rotmatrix[1][2] = ax * sinangle + (1.0 - cosangle) * ay * az;
    this.rotmatrix[2][2] = 1.0 + (1.0 - cosangle) * (az * az - 1.0);
  }

  @Override
  public void setSeed(String seedName, long seed) {
    super.setSeed(seedName, seed);
    this.ax.setSeed(seedName, seed);
    this.ay.setSeed(seedName, seed);
    this.az.setSeed(seedName, seed);
    this.axisangle.setSeed(seedName, seed);
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    modulePropertyMap
        .writeScalar("axisX", this.ax, moduleMap)
        .writeScalar("axisY", this.ay, moduleMap)
        .writeScalar("axisZ", this.az, moduleMap)
        .writeScalar("angle", this.axisangle, moduleMap)
        .writeScalar("source", this.source, moduleMap);
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap modulePropertyMap, ModuleInstanceMap moduleInstanceMap) {
    this.setAxisX(modulePropertyMap.readScalar("axisX", moduleInstanceMap));
    this.setAxisY(modulePropertyMap.readScalar("axisY", moduleInstanceMap));
    this.setAxisZ(modulePropertyMap.readScalar("axisZ", moduleInstanceMap));
    this.setAngle(modulePropertyMap.readScalar("angle", moduleInstanceMap));
    this.setSource(modulePropertyMap.readScalar("source", moduleInstanceMap));
    return this;
  }
}
