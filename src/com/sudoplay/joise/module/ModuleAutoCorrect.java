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

import static com.sudoplay.joise.noise.Util.clamp;

import com.sudoplay.joise.ModuleInstanceMap;
import com.sudoplay.joise.ModuleMap;
import com.sudoplay.joise.ModulePropertyMap;
import com.sudoplay.joise.generator.LCG;
import com.sudoplay.util.Checked;

public class ModuleAutoCorrect extends SourcedModule {

  public static final double DEFAULT_LOW = 0.0;
  public static final double DEFAULT_HIGH = 1.0;
  public static final int DEFAULT_SAMPLES = 100;
  public static final double DEFAULT_SAMPLE_SCALE = 1.0;

  protected double low;
  protected double high;

  protected double sampleScale = DEFAULT_SAMPLE_SCALE;

  protected double scale2, offset2;
  protected double scale3, offset3;
  protected double scale4, offset4;
  protected double scale6, offset6;

  protected boolean locked;

  protected int samples = DEFAULT_SAMPLES;

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

  public void setSamples(long s) {
    samples = Checked.safeLongToInt(s);
  }

  public void setSampleScale(double s) {
    sampleScale = s;
  }

  public void setLocked(boolean lock) {
    locked = lock;
  }

  @Override
  public void setSource(double source) {
    super.setSource(source);
  }

  @Override
  public void setSource(Module source) {
    super.setSource(source);
  }

  public void calculate() {
    if (!source.isModule() || locked) return;

    double mn, mx;
    LCG lcg = new LCG();

    // Calculate 2D
    mn = 10000.0;
    mx = -10000.0;
    for (int c = 0; c < samples; c++) {
      double nx = (lcg.get01() * 4.0 - 2.0) * sampleScale;
      double ny = (lcg.get01() * 4.0 - 2.0) * sampleScale;

      double v = source.get(nx, ny);
      if (v < mn) mn = v;
      if (v > mx) mx = v;
    }
    scale2 = (high - low) / (mx - mn);
    offset2 = low - mn * scale2;

    // Calculate 3D
    mn = 10000.0;
    mx = -10000.0;
    for (int c = 0; c < samples; c++) {
      double nx = (lcg.get01() * 4.0 - 2.0) * sampleScale;
      double ny = (lcg.get01() * 4.0 - 2.0) * sampleScale;
      double nz = (lcg.get01() * 4.0 - 2.0) * sampleScale;

      double v = source.get(nx, ny, nz);
      if (v < mn) mn = v;
      if (v > mx) mx = v;
    }
    scale3 = (high - low) / (mx - mn);
    offset3 = low - mn * scale3;

    // Calculate 4D
    mn = 10000.0;
    mx = -10000.0;
    for (int c = 0; c < samples; c++) {
      double nx = (lcg.get01() * 4.0 - 2.0) * sampleScale;
      double ny = (lcg.get01() * 4.0 - 2.0) * sampleScale;
      double nz = (lcg.get01() * 4.0 - 2.0) * sampleScale;
      double nw = (lcg.get01() * 4.0 - 2.0) * sampleScale;

      double v = source.get(nx, ny, nz, nw);
      if (v < mn) mn = v;
      if (v > mx) mx = v;
    }
    scale4 = (high - low) / (mx - mn);
    offset4 = low - mn * scale4;

    // Calculate 6D
    mn = 10000.0;
    mx = -10000.0;
    for (int c = 0; c < samples; c++) {
      double nx = (lcg.get01() * 4.0 - 2.0) * sampleScale;
      double ny = (lcg.get01() * 4.0 - 2.0) * sampleScale;
      double nz = (lcg.get01() * 4.0 - 2.0) * sampleScale;
      double nw = (lcg.get01() * 4.0 - 2.0) * sampleScale;
      double nu = (lcg.get01() * 4.0 - 2.0) * sampleScale;
      double nv = (lcg.get01() * 4.0 - 2.0) * sampleScale;

      double v = source.get(nx, ny, nz, nw, nu, nv);
      if (v < mn) mn = v;
      if (v > mx) mx = v;
    }
    scale6 = (high - low) / (mx - mn);
    offset6 = low - mn * scale6;

  }

  @Override
  public double get(double x, double y) {
    double v = source.get(x, y);
    return clamp(v * scale2 + offset2, low, high);
  }

  @Override
  public double get(double x, double y, double z) {
    double v = source.get(x, y, z);
    return clamp(v * scale3 + offset3, low, high);
  }

  @Override
  public double get(double x, double y, double z, double w) {
    double v = source.get(x, y, z, w);
    return clamp(v * scale4 + offset4, low, high);
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    double val = source.get(x, y, z, w, u, v);
    return clamp(val * scale6 + offset6, low, high);
  }

  public double getOffset2D() {
    return offset2;
  }

  public double getOffset3D() {
    return offset3;
  }

  public double getOffset4D() {
    return offset4;
  }

  public double getOffset6D() {
    return offset6;
  }

  public double getScale2D() {
    return scale2;
  }

  public double getScale3D() {
    return scale3;
  }

  public double getScale4D() {
    return scale4;
  }

  public double getScale6D() {
    return scale6;
  }

  @Override
  protected void _writeToMap(ModuleMap map) {

    ModulePropertyMap props = new ModulePropertyMap(this);

    writeDouble("low", low, props);
    writeDouble("high", high, props);
    writeLong("samples", samples, props);
    writeDouble("sampleScale", sampleScale, props);
    writeBoolean("locked", locked, props);

    if (locked) {
      writeDouble("scale2", scale2, props);
      writeDouble("offset2", offset2, props);
      writeDouble("scale3", scale3, props);
      writeDouble("offset3", offset3, props);
      writeDouble("scale4", scale4, props);
      writeDouble("offset4", offset4, props);
      writeDouble("scale6", scale6, props);
      writeDouble("offset6", offset6, props);
    }

    writeSource(props, map);

    map.put(getId(), props);
  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap props,
      ModuleInstanceMap map) {

    this.setLow(readDouble("low", props));
    this.setHigh(readDouble("high", props));
    this.setSamples(readLong("samples", props));
    this.setSampleScale(readDouble("sampleScale", props));
    this.setLocked(readBoolean("locked", props));

    if (locked) {
      scale2 = props.getAsDouble("scale2");
      offset2 = props.getAsDouble("offset2");
      scale3 = props.getAsDouble("scale3");
      offset3 = props.getAsDouble("offset3");
      scale4 = props.getAsDouble("scale4");
      offset4 = props.getAsDouble("offset4");
      scale6 = props.getAsDouble("scale6");
      offset6 = props.getAsDouble("offset6");
    }

    readSource(props, map);
    calculate();

    return this;
  }

}
