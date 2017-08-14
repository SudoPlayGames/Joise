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
import com.sudoplay.joise.generator.LCG;
import com.sudoplay.joise.util.Checked;

import static com.sudoplay.joise.noise.Util.clamp;

public class ModuleAutoCorrect extends
    SourcedModule {

  private static final double DEFAULT_LOW = 0.0;
  private static final double DEFAULT_HIGH = 1.0;
  private static final int DEFAULT_SAMPLES = 100;
  private static final double DEFAULT_SAMPLE_SCALE = 1.0;

  private double low;
  private double high;

  private int samples = DEFAULT_SAMPLES;
  private double sampleScale = DEFAULT_SAMPLE_SCALE;

  private double scale2, offset2;
  private double scale3, offset3;
  private double scale4, offset4;
  private double scale6, offset6;

  private boolean locked;

  public ModuleAutoCorrect() {
    this(DEFAULT_LOW, DEFAULT_HIGH);
  }

  public ModuleAutoCorrect(double low, double high) {
    this.low = low;
    this.high = high;
  }

  public void setRange(double low, double high) {
    this.low = low;
    this.high = high;
  }

  public void setLow(double low) {
    this.low = low;
  }

  public void setHigh(double high) {
    this.high = high;
  }

  public void setSamples(int s) {
    this.samples = s;
  }

  public void setSampleScale(double s) {
    this.sampleScale = s;
  }

  public void setLocked(boolean lock) {
    this.locked = lock;
  }

  @Override
  public void setSource(double source) {
    super.setSource(source);
  }

  @Override
  public void setSource(Module source) {
    super.setSource(source);
  }

  @SuppressWarnings("WeakerAccess")
  public void calculate2D() {
    if (!this.source.isModule() || this.locked) return;

    double mn, mx;
    LCG lcg = new LCG();

    // Calculate 2D
    mn = 10000.0;
    mx = -10000.0;
    for (int c = 0; c < this.samples; c++) {
      double nx = (lcg.get01() * 4.0 - 2.0) * this.sampleScale;
      double ny = (lcg.get01() * 4.0 - 2.0) * this.sampleScale;

      double v = this.source.get(nx, ny);
      if (v < mn) mn = v;
      if (v > mx) mx = v;
    }
    this.scale2 = (this.high - this.low) / (mx - mn);
    this.offset2 = this.low - mn * this.scale2;
  }

  @SuppressWarnings("WeakerAccess")
  public void calculate3D() {
    if (!this.source.isModule() || this.locked) return;

    double mn, mx;
    LCG lcg = new LCG();

    // Calculate 3D
    mn = 10000.0;
    mx = -10000.0;
    for (int c = 0; c < this.samples; c++) {
      double nx = (lcg.get01() * 4.0 - 2.0) * this.sampleScale;
      double ny = (lcg.get01() * 4.0 - 2.0) * this.sampleScale;
      double nz = (lcg.get01() * 4.0 - 2.0) * this.sampleScale;

      double v = this.source.get(nx, ny, nz);
      if (v < mn) mn = v;
      if (v > mx) mx = v;
    }
    this.scale3 = (this.high - this.low) / (mx - mn);
    this.offset3 = this.low - mn * this.scale3;
  }

  @SuppressWarnings("WeakerAccess")
  public void calculate4D() {
    if (!this.source.isModule() || this.locked) return;

    double mn, mx;
    LCG lcg = new LCG();

    // Calculate 4D
    mn = 10000.0;
    mx = -10000.0;
    for (int c = 0; c < this.samples; c++) {
      double nx = (lcg.get01() * 4.0 - 2.0) * this.sampleScale;
      double ny = (lcg.get01() * 4.0 - 2.0) * this.sampleScale;
      double nz = (lcg.get01() * 4.0 - 2.0) * this.sampleScale;
      double nw = (lcg.get01() * 4.0 - 2.0) * this.sampleScale;

      double v = this.source.get(nx, ny, nz, nw);
      if (v < mn) mn = v;
      if (v > mx) mx = v;
    }
    this.scale4 = (this.high - this.low) / (mx - mn);
    this.offset4 = this.low - mn * this.scale4;
  }

  @SuppressWarnings("WeakerAccess")
  public void calculate6D() {
    if (!this.source.isModule() || this.locked) return;

    double mn, mx;
    LCG lcg = new LCG();

    // Calculate 6D
    mn = 10000.0;
    mx = -10000.0;
    for (int c = 0; c < this.samples; c++) {
      double nx = (lcg.get01() * 4.0 - 2.0) * this.sampleScale;
      double ny = (lcg.get01() * 4.0 - 2.0) * this.sampleScale;
      double nz = (lcg.get01() * 4.0 - 2.0) * this.sampleScale;
      double nw = (lcg.get01() * 4.0 - 2.0) * this.sampleScale;
      double nu = (lcg.get01() * 4.0 - 2.0) * this.sampleScale;
      double nv = (lcg.get01() * 4.0 - 2.0) * this.sampleScale;

      double v = this.source.get(nx, ny, nz, nw, nu, nv);
      if (v < mn) mn = v;
      if (v > mx) mx = v;
    }
    this.scale6 = (this.high - this.low) / (mx - mn);
    this.offset6 = this.low - mn * this.scale6;
  }

  @SuppressWarnings("WeakerAccess")
  public void calculateAll() {
    if (!this.source.isModule() || this.locked) return;

    this.calculate2D();
    this.calculate3D();
    this.calculate4D();
    this.calculate6D();
  }

  @Deprecated
  public void calculate() {
    this.calculateAll();
  }

  @Override
  public double get(double x, double y) {
    double v = this.source.get(x, y);
    return clamp(v * this.scale2 + this.offset2, this.low, this.high);
  }

  @Override
  public double get(double x, double y, double z) {
    double v = this.source.get(x, y, z);
    return clamp(v * this.scale3 + this.offset3, this.low, this.high);
  }

  @Override
  public double get(double x, double y, double z, double w) {
    double v = this.source.get(x, y, z, w);
    return clamp(v * this.scale4 + this.offset4, this.low, this.high);
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    double val = this.source.get(x, y, z, w, u, v);
    return clamp(val * this.scale6 + this.offset6, this.low, this.high);
  }

  @SuppressWarnings("unused")
  public double getOffset2D() {
    return this.offset2;
  }

  @SuppressWarnings("unused")
  public double getOffset3D() {
    return this.offset3;
  }

  @SuppressWarnings("unused")
  public double getOffset4D() {
    return this.offset4;
  }

  @SuppressWarnings("unused")
  public double getOffset6D() {
    return this.offset6;
  }

  @SuppressWarnings("unused")
  public double getScale2D() {
    return this.scale2;
  }

  @SuppressWarnings("unused")
  public double getScale3D() {
    return this.scale3;
  }

  @SuppressWarnings("unused")
  public double getScale4D() {
    return this.scale4;
  }

  @SuppressWarnings("unused")
  public double getScale6D() {
    return this.scale6;
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    modulePropertyMap
        .writeDouble("low", this.low)
        .writeDouble("high", this.high)
        .writeLong("samples", this.samples)
        .writeDouble("sampleScale", this.sampleScale)
        .writeBoolean("locked", this.locked);

    if (this.locked) {
      modulePropertyMap
          .writeDouble("scale2", this.scale2)
          .writeDouble("offset2", this.offset2)
          .writeDouble("scale3", this.scale3)
          .writeDouble("offset3", this.offset3)
          .writeDouble("scale4", this.scale4)
          .writeDouble("offset4", this.offset4)
          .writeDouble("scale6", this.scale6)
          .writeDouble("offset6", this.offset6);
    }

    modulePropertyMap.writeScalar("source", this.source, moduleMap);

    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap modulePropertyMap, ModuleInstanceMap moduleInstanceMap) {
    this.setLow(modulePropertyMap.readDouble("low"));
    this.setHigh(modulePropertyMap.readDouble("high"));
    this.setSamples(Checked.safeLongToInt(modulePropertyMap.readLong("samples")));
    this.setSampleScale(modulePropertyMap.readDouble("sampleScale"));
    this.setLocked(modulePropertyMap.readBoolean("locked"));

    if (this.locked) {
      this.scale2 = modulePropertyMap.readDouble("scale2");
      this.offset2 = modulePropertyMap.readDouble("offset2");
      this.scale3 = modulePropertyMap.readDouble("scale3");
      this.offset3 = modulePropertyMap.readDouble("offset3");
      this.scale4 = modulePropertyMap.readDouble("scale4");
      this.offset4 = modulePropertyMap.readDouble("offset4");
      this.scale6 = modulePropertyMap.readDouble("scale6");
      this.offset6 = modulePropertyMap.readDouble("offset6");
    }

    this.setSource(modulePropertyMap.readScalar("source", moduleInstanceMap));

    if (!this.locked) {
      this.calculateAll();
    }

    return this;
  }

}
