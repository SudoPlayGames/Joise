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
import com.sudoplay.joise.module.ModuleBasisFunction.BasisType;
import com.sudoplay.joise.module.ModuleBasisFunction.InterpolationType;
import com.sudoplay.joise.noise.Util;

public class ModuleFractal extends
    SeededModule {

  public enum FractalType {
    FBM, RIDGEMULTI, BILLOW, MULTI, HYBRIDMULTI, DECARPENTIERSWISS
  }

  private static final FractalType DEFAULT_FRACTAL_TYPE = FractalType.FBM;
  private static final BasisType DEFAULT_BASIS_TYPE = BasisType.GRADVAL;
  private static final InterpolationType DEFAULT_INTERPOLATION_TYPE = InterpolationType.QUINTIC;
  private static final int DEFAULT_OCTAVES = 2;
  private static final double DEFAULT_FREQUENCY = 1.0;
  private static final double DEFAULT_LACUNARITY = 2.0;
  private static final double DEFAULT_SPACING = 0.0001;

  private ModuleBasisFunction[] basis = new ModuleBasisFunction[MAX_SOURCES];
  private Module[] source = new Module[MAX_SOURCES];
  private double[] derivativeSpacing = new double[MAX_SOURCES];
  private double[] exparray = new double[MAX_SOURCES];
  private double[][] correct = new double[MAX_SOURCES][2];

  private double offset;
  private double gain;
  private double H;
  private double frequency;
  private double lacunarity;
  private int numOctaves;
  private FractalType type;

  public ModuleFractal() {
    this(DEFAULT_FRACTAL_TYPE, DEFAULT_BASIS_TYPE, DEFAULT_INTERPOLATION_TYPE);
  }

  public ModuleFractal(
      FractalType type,
      BasisType basisType,
      InterpolationType interpolationType
  ) {

    for (int i = 0; i < MAX_SOURCES; i++) {
      this.basis[i] = new ModuleBasisFunction();
      this.derivativeSpacing[i] = DEFAULT_SPACING;
    }
    this.setNumOctaves(DEFAULT_OCTAVES);
    this.setFrequency(DEFAULT_FREQUENCY);
    this.setLacunarity(DEFAULT_LACUNARITY);
    this.setType(type);
    this.setAllSourceTypes(basisType, interpolationType);
    this.resetAllSources();
  }

  public void setNumOctaves(long n) {

    if (n > MAX_SOURCES) {
      throw new IllegalArgumentException("number of octaves must be <= " + MAX_SOURCES);
    }
    this.numOctaves = (int) n;
  }

  @SuppressWarnings("WeakerAccess")
  public void setFrequency(double f) {
    this.frequency = f;
  }

  @SuppressWarnings("WeakerAccess")
  public void setLacunarity(double l) {
    this.lacunarity = l;
  }

  @SuppressWarnings("WeakerAccess")
  public void setGain(double g) {
    this.gain = g;
  }

  @SuppressWarnings("WeakerAccess")
  public void setOffset(double o) {
    this.offset = o;
  }

  @SuppressWarnings("WeakerAccess")
  public void setH(double h) {
    this.H = h;
  }

  public void setType(FractalType type) {
    this.type = type;

    switch (type) {
      case BILLOW:
        this.H = 1.0;
        this.gain = 0.5;
        this.offset = 0.0;
        break;

      case DECARPENTIERSWISS:
        this.H = 0.9;
        this.gain = 1.0;
        this.offset = 0.7;
        break;

      case FBM:
        this.H = 1.0;
        this.gain = 0.5;
        this.offset = 0.0;
        break;

      case HYBRIDMULTI:
        this.H = 0.25;
        this.gain = 1.0;
        this.offset = 0.7;
        break;

      case MULTI:
        this.H = 1.0;
        this.gain = 0.0;
        this.offset = 0.0;
        break;

      case RIDGEMULTI:
        this.H = 0.9;
        this.gain = 0.5;
        this.offset = 1.0;
        break;

      default:
        throw new AssertionError();
    }
    this.calcWeights(type);
  }

  @SuppressWarnings("WeakerAccess")
  public void setAllSourceTypes(
      BasisType basisType,
      InterpolationType interpolationType
  ) {

    for (int i = 0; i < MAX_SOURCES; i++) {
      this.basis[i].setType(basisType);
      this.basis[i].setInterpolation(interpolationType);
    }
  }

  @SuppressWarnings("WeakerAccess")
  public void setAllSourceBasisTypes(BasisType basisType) {

    for (int i = 0; i < MAX_SOURCES; i++) {
      this.basis[i].setType(basisType);
    }
  }

  @SuppressWarnings("WeakerAccess")
  public void setAllSourceInterpolationTypes(InterpolationType interpolationType) {

    for (int i = 0; i < MAX_SOURCES; i++) {
      this.basis[i].setInterpolation(interpolationType);
    }
  }

  @SuppressWarnings({"WeakerAccess", "unused"})
  public void setSourceType(
      int index,
      BasisType basisType,
      InterpolationType interpolationType
  ) {
    this.assertMaxSources(index);
    this.basis[index].setType(basisType);
    this.basis[index].setInterpolation(interpolationType);
  }

  /**
   * Sets the derivative spacing. This parameter is only used when calculating the DeCarpentierSwiss fractal type.
   *
   * @param derivativeSpacing the derivative spacing
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  public void setSourceDerivativeSpacing(int index, double derivativeSpacing) {
    this.derivativeSpacing[index] = derivativeSpacing;
  }

  @SuppressWarnings({"WeakerAccess", "unused"})
  public void overrideSource(int index, Module source) {

    if (index < 0 || index >= MAX_SOURCES) {
      throw new IllegalArgumentException("expecting index < " + MAX_SOURCES + " but was " + index);
    }
    this.source[index] = source;
  }

  @SuppressWarnings({"WeakerAccess", "unused"})
  public void resetSource(int index) {
    this.assertMaxSources(index);
    this.source[index] = this.basis[index];
  }

  @SuppressWarnings({"WeakerAccess", "unused"})
  public void resetAllSources() {
    System.arraycopy(this.basis, 0, this.source, 0, MAX_SOURCES);
  }

  @Override
  public void setSeed(long seed) {
    super.setSeed(seed);

    for (int i = 0; i < MAX_SOURCES; i++) {

      if (this.source[i] instanceof SeededModule) {
        ((SeededModule) this.source[i]).setSeed(seed);
      }
    }
  }

  @SuppressWarnings({"WeakerAccess", "unused"})
  public ModuleBasisFunction getBasis(int index) {
    this.assertMaxSources(index);
    return this.basis[index];
  }

  @Override
  public double get(double x, double y) {

    switch (this.type) {
      case BILLOW:
        return this.getBillow(x, y);

      case DECARPENTIERSWISS:
        return this.getDeCarpentierSwiss(x, y);

      case FBM:
        return this.getFBM(x, y);

      case HYBRIDMULTI:
        return this.getHybridMulti(x, y);

      case MULTI:
        return this.getMulti(x, y);

      case RIDGEMULTI:
        return this.getRidgedMulti(x, y);

      default:
        throw new AssertionError();
    }
  }

  @Override
  public double get(double x, double y, double z) {

    switch (this.type) {
      case BILLOW:
        return this.getBillow(x, y, z);

      case DECARPENTIERSWISS:
        return this.getDeCarpentierSwiss(x, y, z);

      case FBM:
        return this.getFBM(x, y, z);

      case HYBRIDMULTI:
        return this.getHybridMulti(x, y, z);

      case MULTI:
        return this.getMulti(x, y, z);

      case RIDGEMULTI:
        return this.getRidgedMulti(x, y, z);

      default:
        throw new AssertionError();
    }
  }

  @Override
  public double get(double x, double y, double z, double w) {

    switch (this.type) {
      case BILLOW:
        return this.getBillow(x, y, z, w);

      case DECARPENTIERSWISS:
        return this.getDeCarpentierSwiss(x, y, z, w);

      case FBM:
        return this.getFBM(x, y, z, w);

      case HYBRIDMULTI:
        return this.getHybridMulti(x, y, z, w);

      case MULTI:
        return this.getMulti(x, y, z, w);

      case RIDGEMULTI:
        return this.getRidgedMulti(x, y, z, w);

      default:
        throw new AssertionError();
    }
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {

    switch (this.type) {
      case BILLOW:
        return this.getBillow(x, y, z, w, u, v);

      case DECARPENTIERSWISS:
        return this.getDeCarpentierSwiss(x, y, z, w, u, v);

      case FBM:
        return this.getFBM(x, y, z, w, u, v);

      case HYBRIDMULTI:
        return this.getHybridMulti(x, y, z, w, u, v);

      case MULTI:
        return this.getMulti(x, y, z, w, u, v);

      case RIDGEMULTI:
        return this.getRidgedMulti(x, y, z, w, u, v);

      default:
        throw new AssertionError();
    }
  }

  private double getFBM(double x, double y) {
    double sum = 0;
    double amp = 1.0;

    x *= this.frequency;
    y *= this.frequency;

    for (int i = 0; i < this.numOctaves; ++i) {
      double n = this.source[i].get(x, y);
      sum += n * amp;
      amp *= this.gain;

      x *= this.lacunarity;
      y *= this.lacunarity;
    }
    return sum;
  }

  private double getFBM(double x, double y, double z) {
    double sum = 0;
    double amp = 1.0;

    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;

    for (int i = 0; i < this.numOctaves; ++i) {
      double n = this.source[i].get(x, y, z);
      sum += n * amp;
      amp *= this.gain;

      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
    }
    return sum;
  }

  private double getFBM(double x, double y, double z, double w) {
    double sum = 0;
    double amp = 1.0;

    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;
    w *= this.frequency;

    for (int i = 0; i < this.numOctaves; ++i) {
      double n = this.source[i].get(x, y, z, w);
      sum += n * amp;
      amp *= this.gain;

      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
      w *= this.lacunarity;
    }
    return sum;
  }

  private double getFBM(double x, double y, double z, double w, double u, double v) {
    double sum = 0;
    double amp = 1.0;

    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;
    w *= this.frequency;
    u *= this.frequency;
    v *= this.frequency;

    for (int i = 0; i < this.numOctaves; ++i) {
      double n = this.source[i].get(x, y, z, w);
      sum += n * amp;
      amp *= this.gain;

      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
      w *= this.lacunarity;
      u *= this.lacunarity;
      v *= this.lacunarity;
    }
    return sum;
  }

  private double getMulti(double x, double y) {
    double value = 1.0;
    x *= this.frequency;
    y *= this.frequency;

    for (int i = 0; i < this.numOctaves; ++i) {
      value *= this.source[i].get(x, y) * this.exparray[i] + 1.0;
      x *= this.lacunarity;
      y *= this.lacunarity;
    }
    return value * this.correct[this.numOctaves - 1][0] + this.correct[this.numOctaves - 1][1];
  }

  private double getMulti(double x, double y, double z) {
    double value = 1.0;
    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;

    for (int i = 0; i < this.numOctaves; ++i) {
      value *= this.source[i].get(x, y, z) * this.exparray[i] + 1.0;
      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
    }
    return value * this.correct[this.numOctaves - 1][0] + this.correct[this.numOctaves - 1][1];
  }

  private double getMulti(double x, double y, double z, double w) {
    double value = 1.0;
    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;
    w *= this.frequency;

    for (int i = 0; i < this.numOctaves; ++i) {
      value *= this.source[i].get(x, y, z, w) * this.exparray[i] + 1.0;
      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
      w *= this.lacunarity;
    }
    return value * this.correct[this.numOctaves - 1][0] + this.correct[this.numOctaves - 1][1];
  }

  private double getMulti(double x, double y, double z, double w, double u, double v) {
    double value = 1.0;
    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;
    w *= this.frequency;
    u *= this.frequency;
    v *= this.frequency;

    for (int i = 0; i < this.numOctaves; ++i) {
      value *= this.source[i].get(x, y, z, w, u, v) * this.exparray[i] + 1.0;
      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
      w *= this.lacunarity;
      u *= this.lacunarity;
      v *= this.lacunarity;
    }
    return value * this.correct[this.numOctaves - 1][0] + this.correct[this.numOctaves - 1][1];
  }

  private double getBillow(double x, double y) {
    double sum = 0.0;
    double amp = 1.0;

    x *= this.frequency;
    y *= this.frequency;

    for (int i = 0; i < this.numOctaves; ++i) {
      double n = this.source[i].get(x, y);
      sum += (2.0 * Math.abs(n) - 1.0) * amp;
      amp *= this.gain;

      x *= this.lacunarity;
      y *= this.lacunarity;
    }
    return sum;
  }

  private double getBillow(double x, double y, double z) {
    double sum = 0.0;
    double amp = 1.0;
    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;

    for (int i = 0; i < this.numOctaves; ++i) {
      double n = this.source[i].get(x, y, z);
      sum += (2.0 * Math.abs(n) - 1.0) * amp;
      amp *= this.gain;
      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
    }
    return sum;
  }

  private double getBillow(double x, double y, double z, double w) {
    double sum = 0.0;
    double amp = 1.0;
    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;
    w *= this.frequency;

    for (int i = 0; i < this.numOctaves; ++i) {
      double n = this.source[i].get(x, y, z, w);
      sum += (2.0 * Math.abs(n) - 1.0) * amp;
      amp *= this.gain;
      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
      w *= this.lacunarity;
    }
    return sum;
  }

  private double getBillow(double x, double y, double z, double w, double u, double v) {
    double sum = 0.0;
    double amp = 1.0;
    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;
    w *= this.frequency;
    u *= this.frequency;
    v *= this.frequency;

    for (int i = 0; i < this.numOctaves; ++i) {
      double n = this.source[i].get(x, y, z, w, u, v);
      sum += (2.0 * Math.abs(n) - 1.0) * amp;
      amp *= this.gain;
      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
      w *= this.lacunarity;
      u *= this.lacunarity;
      v *= this.lacunarity;
    }
    return sum;
  }

  private double getRidgedMulti(double x, double y) {
    double sum = 0;
    double amp = 1.0;
    x *= this.frequency;
    y *= this.frequency;

    for (int i = 0; i < this.numOctaves; ++i) {
      double n = this.source[i].get(x, y);
      n = 1.0 - Math.abs(n);
      sum += amp * n;
      amp *= this.gain;
      x *= this.lacunarity;
      y *= this.lacunarity;
    }
    return sum;
  }

  private double getRidgedMulti(double x, double y, double z) {
    double sum = 0;
    double amp = 1.0;
    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;

    for (int i = 0; i < this.numOctaves; ++i) {
      double n = this.source[i].get(x, y, z);
      n = 1.0 - Math.abs(n);
      sum += amp * n;
      amp *= this.gain;
      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
    }
    return sum;
  }

  private double getRidgedMulti(double x, double y, double z, double w) {
    double result = 0.0, signal;
    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;
    w *= this.frequency;

    for (int i = 0; i < this.numOctaves; ++i) {
      signal = this.source[i].get(x, y, z, w);
      signal = this.offset - Math.abs(signal);
      signal *= signal;
      result += signal * this.exparray[i];
      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
      w *= this.lacunarity;
    }
    return result * this.correct[this.numOctaves - 1][0] + this.correct[this.numOctaves - 1][1];
  }

  private double getRidgedMulti(double x, double y, double z, double w, double u, double v) {
    double result = 0.0, signal;
    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;
    w *= this.frequency;
    u *= this.frequency;
    v *= this.frequency;

    for (int i = 0; i < this.numOctaves; ++i) {
      signal = this.source[i].get(x, y, z, w, u, v);
      signal = this.offset - Math.abs(signal);
      signal *= signal;
      result += signal * this.exparray[i];
      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
      w *= this.lacunarity;
      u *= this.lacunarity;
      v *= this.lacunarity;
    }
    return result * this.correct[this.numOctaves - 1][0] + this.correct[this.numOctaves - 1][1];
  }

  private double getHybridMulti(double x, double y) {
    double value, signal, weight;
    x *= this.frequency;
    y *= this.frequency;
    value = this.source[0].get(x, y) + this.offset;
    weight = this.gain * value;
    x *= this.lacunarity;
    y *= this.lacunarity;

    for (int i = 1; i < this.numOctaves; ++i) {

      if (weight > 1.0) {
        weight = 1.0;
      }
      signal = (this.source[i].get(x, y) + this.offset) * this.exparray[i];
      value += weight * signal;
      weight *= this.gain * signal;
      x *= this.lacunarity;
      y *= this.lacunarity;
    }
    return value * this.correct[this.numOctaves - 1][0] + this.correct[this.numOctaves - 1][1];
  }

  private double getHybridMulti(double x, double y, double z) {
    double value, signal, weight;
    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;
    value = this.source[0].get(x, y, z) + this.offset;
    weight = this.gain * value;
    x *= this.lacunarity;
    y *= this.lacunarity;
    z *= this.lacunarity;

    for (int i = 1; i < this.numOctaves; ++i) {

      if (weight > 1.0) {
        weight = 1.0;
      }
      signal = (this.source[i].get(x, y, z) + this.offset) * this.exparray[i];
      value += weight * signal;
      weight *= this.gain * signal;
      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
    }
    return value * this.correct[this.numOctaves - 1][0] + this.correct[this.numOctaves - 1][1];
  }

  private double getHybridMulti(double x, double y, double z, double w) {
    double value, signal, weight;
    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;
    w *= this.frequency;
    value = this.source[0].get(x, y, z, w) + this.offset;
    weight = this.gain * value;
    x *= this.lacunarity;
    y *= this.lacunarity;
    z *= this.lacunarity;
    w *= this.lacunarity;

    for (int i = 1; i < this.numOctaves; ++i) {

      if (weight > 1.0) {
        weight = 1.0;
      }
      signal = (this.source[i].get(x, y, z, w) + this.offset) * this.exparray[i];
      value += weight * signal;
      weight *= this.gain * signal;
      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
      w *= this.lacunarity;
    }
    return value * this.correct[this.numOctaves - 1][0] + this.correct[this.numOctaves - 1][1];
  }

  private double getHybridMulti(double x, double y, double z, double w, double u, double v) {
    double value, signal, weight;
    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;
    w *= this.frequency;
    u *= this.frequency;
    v *= this.frequency;
    value = this.source[0].get(x, y, z, w, u, v) + this.offset;
    weight = this.gain * value;
    x *= this.lacunarity;
    y *= this.lacunarity;
    z *= this.lacunarity;
    w *= this.lacunarity;
    u *= this.lacunarity;
    v *= this.lacunarity;

    for (int i = 1; i < this.numOctaves; ++i) {

      if (weight > 1.0) {
        weight = 1.0;
      }
      signal = (this.source[i].get(x, y, z, w, u, v) + this.offset) * this.exparray[i];
      value += weight * signal;
      weight *= this.gain * signal;
      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
      w *= this.lacunarity;
      u *= this.lacunarity;
      v *= this.lacunarity;
    }
    return value * this.correct[this.numOctaves - 1][0] + this.correct[this.numOctaves - 1][1];
  }

  private double getDeCarpentierSwiss(double x, double y) {
    double sum = 0;
    double amp = 1.0;
    double dx_sum = 0;
    double dy_sum = 0;
    x *= this.frequency;
    y *= this.frequency;

    for (int sourceIndex = 0; sourceIndex < this.numOctaves; ++sourceIndex) {
      double n = this.source[sourceIndex].get(x + this.offset * dx_sum, y + this.offset * dy_sum);
      double dx = this.getDX(
          this.source[sourceIndex],
          this.derivativeSpacing[sourceIndex],
          x + this.offset * dx_sum,
          y + this.offset * dy_sum
      );
      double dy = this.getDY(
          this.source[sourceIndex],
          this.derivativeSpacing[sourceIndex],
          x + this.offset * dx_sum,
          y + this.offset * dy_sum
      );
      sum += amp * (1.0 - Math.abs(n));
      dx_sum += amp * dx * -n;
      dy_sum += amp * dy * -n;
      amp *= this.gain * Util.clamp(sum, 0.0, 1.0);
      x *= this.lacunarity;
      y *= this.lacunarity;
    }
    return sum;
  }

  private double getDeCarpentierSwiss(double x, double y, double z) {
    double sum = 0;
    double amp = 1.0;
    double dx_sum = 0;
    double dy_sum = 0;
    double dz_sum = 0;
    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;

    for (int sourceIndex = 0; sourceIndex < this.numOctaves; ++sourceIndex) {
      double n = this.source[sourceIndex].get(
          x + this.offset * dx_sum,
          y + this.offset * dy_sum,
          z + this.offset * dz_sum
      );
      double dx = this.getDX(
          this.source[sourceIndex],
          this.derivativeSpacing[sourceIndex],
          x + this.offset * dx_sum,
          y + this.offset * dy_sum,
          z + this.offset * dz_sum
      );
      double dy = this.getDY(
          this.source[sourceIndex],
          this.derivativeSpacing[sourceIndex],
          x + this.offset * dx_sum,
          y + this.offset * dy_sum,
          z + this.offset * dz_sum
      );
      double dz = this.getDZ(
          this.source[sourceIndex],
          this.derivativeSpacing[sourceIndex],
          x + this.offset * dx_sum,
          y + this.offset * dy_sum,
          z + this.offset * dz_sum
      );
      sum += amp * (1.0 - Math.abs(n));
      dx_sum += amp * dx * -n;
      dy_sum += amp * dy * -n;
      dz_sum += amp * dz * -n;
      amp *= this.gain * Util.clamp(sum, 0.0, 1.0);
      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
    }
    return sum;
  }

  private double getDeCarpentierSwiss(double x, double y, double z, double w) {
    double sum = 0;
    double amp = 1.0;
    double dx_sum = 0;
    double dy_sum = 0;
    double dz_sum = 0;
    double dw_sum = 0;
    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;
    w *= this.frequency;

    for (int sourceIndex = 0; sourceIndex < this.numOctaves; ++sourceIndex) {
      double n = this.source[sourceIndex].get(
          x + this.offset * dx_sum,
          y + this.offset * dy_sum,
          z + this.offset * dz_sum,
          w + this.offset * dw_sum
      );
      double dx = this.getDX(
          this.source[sourceIndex],
          this.derivativeSpacing[sourceIndex],
          x + this.offset * dx_sum,
          y + this.offset * dy_sum,
          z + this.offset * dz_sum,
          w + this.offset * dw_sum
      );
      double dy = this.getDY(
          this.source[sourceIndex],
          this.derivativeSpacing[sourceIndex],
          x + this.offset * dx_sum,
          y + this.offset * dy_sum,
          z + this.offset * dz_sum,
          w + this.offset * dw_sum
      );
      double dz = this.getDZ(
          this.source[sourceIndex],
          this.derivativeSpacing[sourceIndex],
          x + this.offset * dx_sum,
          y + this.offset * dy_sum,
          z + this.offset * dz_sum,
          w + this.offset * dw_sum
      );
      double dw = this.getDW(
          this.source[sourceIndex],
          this.derivativeSpacing[sourceIndex],
          x + this.offset * dx_sum,
          y + this.offset * dy_sum,
          z + this.offset * dz_sum,
          w + this.offset * dw_sum
      );
      sum += amp * (1.0 - Math.abs(n));
      dx_sum += amp * dx * -n;
      dy_sum += amp * dy * -n;
      dz_sum += amp * dz * -n;
      dw_sum += amp * dw * -n;
      amp *= this.gain * Util.clamp(sum, 0.0, 1.0);
      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
      w *= this.lacunarity;
    }
    return sum;
  }

  private double getDeCarpentierSwiss(
      double x,
      double y,
      double z,
      double w,
      double u,
      double v
  ) {
    double sum = 0;
    double amp = 1.0;
    double dx_sum = 0;
    double dy_sum = 0;
    double dz_sum = 0;
    double dw_sum = 0;
    double du_sum = 0;
    double dv_sum = 0;
    x *= this.frequency;
    y *= this.frequency;
    z *= this.frequency;
    w *= this.frequency;
    u *= this.frequency;
    v *= this.frequency;

    for (int sourceIndex = 0; sourceIndex < this.numOctaves; ++sourceIndex) {
      double n = this.source[sourceIndex].get(
          x + this.offset * dx_sum,
          y + this.offset * dy_sum,
          z + this.offset * dz_sum
      );
      double dx = this.getDX(
          this.source[sourceIndex],
          this.derivativeSpacing[sourceIndex],
          x + this.offset * dx_sum,
          y + this.offset * dy_sum,
          z + this.offset * dx_sum,
          w + this.offset * dw_sum,
          u + this.offset * du_sum,
          v + this.offset * dv_sum
      );
      double dy = this.getDY(
          this.source[sourceIndex],
          this.derivativeSpacing[sourceIndex],
          x + this.offset * dx_sum,
          y + this.offset * dy_sum,
          z + this.offset * dz_sum,
          w + this.offset * dw_sum,
          u + this.offset * du_sum,
          v + this.offset * dv_sum
      );
      double dz = this.getDZ(
          this.source[sourceIndex],
          this.derivativeSpacing[sourceIndex],
          x + this.offset * dx_sum,
          y + this.offset * dy_sum,
          z + this.offset * dz_sum,
          w + this.offset * dw_sum,
          u + this.offset * du_sum,
          v + this.offset * dv_sum
      );
      double dw = this.getDW(
          this.source[sourceIndex],
          this.derivativeSpacing[sourceIndex],
          x + this.offset * dx_sum,
          y + this.offset * dy_sum,
          z + this.offset * dz_sum,
          w + this.offset * dw_sum,
          u + this.offset * du_sum,
          v + this.offset * dv_sum
      );
      double du = this.getDU(
          this.source[sourceIndex],
          this.derivativeSpacing[sourceIndex],
          x + this.offset * dx_sum,
          y + this.offset * dy_sum,
          z + this.offset * dz_sum,
          w + this.offset * dw_sum,
          u + this.offset * du_sum,
          v + this.offset * dv_sum
      );
      double dv = this.getDV(
          this.source[sourceIndex],
          this.derivativeSpacing[sourceIndex],
          x + this.offset * dx_sum,
          y + this.offset * dy_sum,
          z + this.offset * dz_sum,
          w + this.offset * dw_sum,
          u + this.offset * du_sum,
          v + this.offset * dv_sum
      );
      sum += amp * (1.0 - Math.abs(n));
      dx_sum += amp * dx * -n;
      dy_sum += amp * dy * -n;
      dz_sum += amp * dz * -n;
      dw_sum += amp * dw * -n;
      du_sum += amp * du * -n;
      dv_sum += amp * dv * -n;
      amp *= this.gain * Util.clamp(sum, 0.0, 1.0);
      x *= this.lacunarity;
      y *= this.lacunarity;
      z *= this.lacunarity;
      w *= this.lacunarity;
      u *= this.lacunarity;
      v *= this.lacunarity;
    }
    return sum;
  }

  private void calcWeights(FractalType type) {
    double minvalue, maxvalue;

    switch (type) {
      case FBM:

        for (int i = 0; i < MAX_SOURCES; i++) {
          this.exparray[i] = Math.pow(this.lacunarity, -i * this.H);
        }
        minvalue = 0.0;
        maxvalue = 0.0;

        for (int i = 0; i < MAX_SOURCES; i++) {
          minvalue += -1.0 * this.exparray[i];
          maxvalue += 1.0 * this.exparray[i];

          double A = -1.0, B = 1.0;
          double scale = (B - A) / (maxvalue - minvalue);
          double bias = A - minvalue * scale;
          this.correct[i][0] = scale;
          this.correct[i][1] = bias;
        }
        break;

      case RIDGEMULTI:

        for (int i = 0; i < MAX_SOURCES; ++i) {
          this.exparray[i] = Math.pow(this.lacunarity, -i * this.H);
        }
        minvalue = 0.0;
        maxvalue = 0.0;

        for (int i = 0; i < MAX_SOURCES; ++i) {
          minvalue += (this.offset - 1.0) * (this.offset - 1.0) * this.exparray[i];
          maxvalue += (this.offset) * (this.offset) * this.exparray[i];

          double A = -1.0, B = 1.0;
          double scale = (B - A) / (maxvalue - minvalue);
          double bias = A - minvalue * scale;
          this.correct[i][0] = scale;
          this.correct[i][1] = bias;
        }
        break;

      case DECARPENTIERSWISS:

        for (int i = 0; i < MAX_SOURCES; ++i) {
          this.exparray[i] = Math.pow(this.lacunarity, -i * this.H);
        }
        minvalue = 0.0;
        maxvalue = 0.0;

        for (int i = 0; i < MAX_SOURCES; ++i) {
          minvalue += (this.offset - 1.0) * (this.offset - 1.0) * this.exparray[i];
          maxvalue += (this.offset) * (this.offset) * this.exparray[i];

          double A = -1.0, B = 1.0;
          double scale = (B - A) / (maxvalue - minvalue);
          double bias = A - minvalue * scale;
          this.correct[i][0] = scale;
          this.correct[i][1] = bias;
        }
        break;

      case BILLOW:

        for (int i = 0; i < MAX_SOURCES; ++i) {
          this.exparray[i] = Math.pow(this.lacunarity, -i * this.H);
        }
        minvalue = 0.0;
        maxvalue = 0.0;

        for (int i = 0; i < MAX_SOURCES; ++i) {
          minvalue += -1.0 * this.exparray[i];
          maxvalue += 1.0 * this.exparray[i];

          double A = -1.0, B = 1.0;
          double scale = (B - A) / (maxvalue - minvalue);
          double bias = A - minvalue * scale;
          this.correct[i][0] = scale;
          this.correct[i][1] = bias;
        }
        break;

      case MULTI:

        for (int i = 0; i < MAX_SOURCES; ++i) {
          this.exparray[i] = Math.pow(this.lacunarity, -i * this.H);
        }
        minvalue = 1.0;
        maxvalue = 1.0;

        for (int i = 0; i < MAX_SOURCES; ++i) {
          minvalue *= -1.0 * this.exparray[i] + 1.0;
          maxvalue *= 1.0 * this.exparray[i] + 1.0;

          double A = -1.0, B = 1.0;
          double scale = (B - A) / (maxvalue - minvalue);
          double bias = A - minvalue * scale;
          this.correct[i][0] = scale;
          this.correct[i][1] = bias;
        }
        break;

      case HYBRIDMULTI:

        for (int i = 0; i < MAX_SOURCES; ++i) {
          this.exparray[i] = Math.pow(this.lacunarity, -i * this.H);
        }
        double A = -1.0,
            B = 1.0,
            scale,
            bias,
            weightMin,
            weightMax;

        minvalue = this.offset - 1.0;
        maxvalue = this.offset + 1.0;
        weightMin = this.gain * minvalue;
        weightMax = this.gain * maxvalue;

        scale = (B - A) / (maxvalue - minvalue);
        bias = A - minvalue * scale;
        this.correct[0][0] = scale;
        this.correct[0][1] = bias;

        for (int i = 1; i < MAX_SOURCES; ++i) {
          if (weightMin > 1.0) weightMin = 1.0;
          if (weightMax > 1.0) weightMax = 1.0;

          double signal = (this.offset - 1.0) * this.exparray[i];
          minvalue += signal * weightMin;
          weightMin *= this.gain * signal;

          signal = (this.offset + 1.0) * this.exparray[i];
          maxvalue += signal * weightMax;
          weightMax *= this.gain * signal;

          scale = (B - A) / (maxvalue - minvalue);
          bias = A - minvalue * scale;
          this.correct[i][0] = scale;
          this.correct[i][1] = bias;
        }
        break;
    }

  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);

    modulePropertyMap
        .writeEnum("type", this.type)
        .writeLong("octaves", this.numOctaves)
        .writeDouble("frequency", this.frequency)
        .writeDouble("lacunarity", this.lacunarity)
        .writeDouble("gain", this.gain)
        .writeDouble("H", this.H)
        .writeDouble("offset", this.offset);

    for (int i = 0; i < this.numOctaves; i++) {
      modulePropertyMap.writeDouble("spacing_" + i, this.derivativeSpacing[i]);
    }

    for (int i = 0; i < this.numOctaves; i++) {
      modulePropertyMap.put("source_" + i, this.source[i].getId());
      this.source[i].writeToMap(moduleMap);
    }

    this.writeSeed(modulePropertyMap);
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap modulePropertyMap, ModuleInstanceMap moduleInstanceMap) {

    this.setType(modulePropertyMap.readEnum("type", FractalType.class));
    this.setNumOctaves(modulePropertyMap.readLong("octaves"));
    this.setFrequency(modulePropertyMap.readDouble("frequency"));
    this.setLacunarity(modulePropertyMap.readDouble("lacunarity"));
    this.setGain(modulePropertyMap.readDouble("gain"));
    this.setH(modulePropertyMap.readDouble("H"));
    this.setOffset(modulePropertyMap.readDouble("offset"));

    for (int i = 0; i < this.numOctaves; i++) {
      this.setSourceDerivativeSpacing(i, modulePropertyMap.readDouble("spacing_" + i, DEFAULT_SPACING));
    }

    this.readSeed(modulePropertyMap);

    // must override sources after seed has been set
    for (int i = 0; i < this.numOctaves; i++) {
      // this is intended, not suspicious
      //noinspection SuspiciousMethodCalls
      this.overrideSource(i, moduleInstanceMap.get(modulePropertyMap.get("source_" + i)));
    }

    return this;
  }

  /**
   * Throws an {@link IllegalArgumentException} if the supplied index is less than zero or greater than or
   * equal to the max sources.
   *
   * @param index the index to test bounds
   */
  private void assertMaxSources(int index) {

    if (index < 0 || index >= Module.MAX_SOURCES) {
      throw new IllegalArgumentException("expected index < " + Module.MAX_SOURCES + ", got " + index);
    }
  }

  private double getDX(
      Module module,
      double derivativeSpacing,
      double x,
      double y
  ) {
    return (module.get(x - derivativeSpacing, y)
        - module.get(x + derivativeSpacing, y)) / derivativeSpacing;
  }

  private double getDY(
      Module module,
      double derivativeSpacing,
      double x,
      double y
  ) {
    return (module.get(x, y - derivativeSpacing)
        - module.get(x, y + derivativeSpacing)) / derivativeSpacing;
  }

  private double getDX(
      Module module,
      double derivativeSpacing,
      double x,
      double y,
      double z
  ) {
    return (module.get(x - derivativeSpacing, y, z)
        - module.get(x + derivativeSpacing, y, z)) / derivativeSpacing;
  }

  private double getDY(
      Module module,
      double derivativeSpacing,
      double x,
      double y,
      double z
  ) {
    return (module.get(x, y - derivativeSpacing, z)
        - module.get(x, y + derivativeSpacing, z)) / derivativeSpacing;
  }

  private double getDZ(
      Module module,
      double derivativeSpacing,
      double x,
      double y,
      double z
  ) {
    return (module.get(x, y, z - derivativeSpacing)
        - module.get(x, y, z + derivativeSpacing)) / derivativeSpacing;
  }

  private double getDX(
      Module module,
      double derivativeSpacing,
      double x,
      double y,
      double z,
      double w
  ) {
    return (module.get(x - derivativeSpacing, y, z, w)
        - module.get(x + derivativeSpacing, y, z, w)) / derivativeSpacing;
  }

  private double getDY(
      Module module,
      double derivativeSpacing,
      double x,
      double y,
      double z,
      double w
  ) {
    return (module.get(x, y - derivativeSpacing, z, w)
        - module.get(x, y + derivativeSpacing, z, w)) / derivativeSpacing;
  }

  private double getDZ(
      Module module,
      double derivativeSpacing,
      double x,
      double y,
      double z,
      double w
  ) {
    return (module.get(x, y, z - derivativeSpacing, w)
        - module.get(x, y, z + derivativeSpacing, w)) / derivativeSpacing;
  }

  private double getDW(
      Module module,
      double derivativeSpacing,
      double x,
      double y,
      double z,
      double w
  ) {
    return (module.get(x, y, z, w - derivativeSpacing)
        - module.get(x, y, z, w + derivativeSpacing)) / derivativeSpacing;
  }

  private double getDX(
      Module module,
      double derivativeSpacing,
      double x,
      double y,
      double z,
      double w,
      double u,
      double v
  ) {
    return (module.get(x - derivativeSpacing, y, z, w, u, v)
        - module.get(x + derivativeSpacing, y, z, w, u, v)) / derivativeSpacing;
  }

  private double getDY(
      Module module,
      double derivativeSpacing,
      double x,
      double y,
      double z,
      double w,
      double u,
      double v
  ) {
    return (module.get(x, y - derivativeSpacing, z, w, u, v)
        - module.get(x, y + derivativeSpacing, z, w, u, v)) / derivativeSpacing;
  }

  private double getDZ(
      Module module,
      double derivativeSpacing,
      double x,
      double y,
      double z,
      double w,
      double u,
      double v
  ) {
    return (module.get(x, y, z - derivativeSpacing, w, u, v)
        - module.get(x, y, z + derivativeSpacing, w, u, v)) / derivativeSpacing;
  }

  private double getDW(
      Module module,
      double derivativeSpacing,
      double x,
      double y,
      double z,
      double w,
      double u,
      double v
  ) {
    return (module.get(x, y, z, w - derivativeSpacing, u, v)
        - module.get(x, y, z, w + derivativeSpacing, u, v)) / derivativeSpacing;
  }

  private double getDU(
      Module module,
      double derivativeSpacing,
      double x,
      double y,
      double z,
      double w,
      double u,
      double v
  ) {
    return (module.get(x, y, z, w, u - derivativeSpacing, v)
        - module.get(x, y, z, w, u + derivativeSpacing, v)) / derivativeSpacing;
  }

  private double getDV(
      Module module,
      double derivativeSpacing,
      double x,
      double y,
      double z,
      double w,
      double u,
      double v
  ) {
    return (module.get(x, y, z, w, u, v - derivativeSpacing)
        - module.get(x, y, z, w, u, v + derivativeSpacing)) / derivativeSpacing;
  }

}
