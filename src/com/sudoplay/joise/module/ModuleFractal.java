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
import com.sudoplay.joise.module.ModuleBasisFunction.BasisType;
import com.sudoplay.joise.module.ModuleBasisFunction.InterpolationType;
import com.sudoplay.joise.noise.Util;

public class ModuleFractal extends SeedableModule {

  public static final FractalType DEFAULT_FRACTAL_TYPE = FractalType.FBM;
  public static final BasisType DEFAULT_BASIS_TYPE = BasisType.GRADVAL;
  public static final InterpolationType DEFAULT_INTERPOLATION_TYPE = InterpolationType.QUINTIC;
  public static final int DEFAULT_OCTAVES = 2;
  public static final double DEFAULT_FREQUENCY = 1.0;
  public static final double DEFAULT_LACUNARITY = 2.0;

  public static enum FractalType {
    FBM, RIDGEMULTI, BILLOW, MULTI, HYBRIDMULTI, DECARPENTIERSWISS
  }

  protected ModuleBasisFunction[] basis = new ModuleBasisFunction[MAX_SOURCES];
  protected Module[] source = new Module[MAX_SOURCES];

  protected double[] exparray = new double[MAX_SOURCES];
  protected double[][] correct = new double[MAX_SOURCES][2];

  protected double offset, gain, H;
  protected double frequency, lacunarity;
  protected int numOctaves;
  protected FractalType type;

  public ModuleFractal() {
    this(DEFAULT_FRACTAL_TYPE, DEFAULT_BASIS_TYPE, DEFAULT_INTERPOLATION_TYPE);
  }

  public ModuleFractal(FractalType type, BasisType basisType,
      InterpolationType interpolationType) {
    for (int i = 0; i < MAX_SOURCES; i++) {
      basis[i] = new ModuleBasisFunction();
    }
    setNumOctaves(2);
    setFrequency(1.0);
    setLacunarity(2.0);
    setType(type);
    setAllSourceTypes(basisType, interpolationType);
    resetAllSources();
  }

  public void setNumOctaves(long n) {
    if (n > MAX_SOURCES) {
      throw new IllegalArgumentException("number of octaves must be <= "
          + MAX_SOURCES);
    }
    numOctaves = (int) n;
  }

  public void setFrequency(double f) {
    frequency = f;
  }

  public void setLacunarity(double l) {
    lacunarity = l;
  }

  public void setGain(double g) {
    gain = g;
  }

  public void setOffset(double o) {
    offset = o;
  }

  public void setH(double h) {
    H = h;
  }

  public void setType(FractalType type) {
    this.type = type;
    switch (type) {
    case BILLOW:
      H = 1.0;
      gain = 0.5;
      offset = 0.0;
      break;
    case DECARPENTIERSWISS:
      H = 0.9;
      gain = 1.0;
      offset = 0.7;
      break;
    case FBM:
      H = 1.0;
      gain = 0.5;
      offset = 0.0;
      break;
    case HYBRIDMULTI:
      H = 0.25;
      gain = 1.0;
      offset = 0.7;
      break;
    case MULTI:
      H = 1.0;
      gain = 0.0;
      offset = 0.0;
      break;
    case RIDGEMULTI:
      H = 0.9;
      gain = 0.5;
      offset = 1.0;
      break;
    default:
      throw new AssertionError();
    }
    calcWeights(type);
  }

  public void setAllSourceTypes(BasisType basisType,
      InterpolationType interpolationType) {
    for (int i = 0; i < MAX_SOURCES; i++) {
      basis[i].setType(basisType);
      basis[i].setInterpolation(interpolationType);
    }
  }

  public void setAllSourceBasisTypes(BasisType basisType) {
    for (int i = 0; i < MAX_SOURCES; i++) {
      basis[i].setType(basisType);
    }
  }

  public void setAllSourceInterpolationTypes(InterpolationType interpolationType) {
    for (int i = 0; i < MAX_SOURCES; i++) {
      basis[i].setInterpolation(interpolationType);
    }
  }

  public void setSourceType(int index, BasisType basisType,
      InterpolationType interpolationType) {
    assertMaxSources(index);
    basis[index].setType(basisType);
    basis[index].setInterpolation(interpolationType);
  }

  public void overrideSource(int index, Module source) {
    if (index < 0 || index >= MAX_SOURCES) {
      throw new IllegalArgumentException("expecting index < " + MAX_SOURCES
          + " but was " + index);
    }
    this.source[index] = source;
  }

  public void resetSource(int index) {
    assertMaxSources(index);
    source[index] = basis[index];
  }

  public void resetAllSources() {
    for (int i = 0; i < MAX_SOURCES; i++) {
      source[i] = basis[i];
    }
  }

  @Override
  public void setSeed(long seed) {
    super.setSeed(seed);
    for (int i = 0; i < MAX_SOURCES; i++) {
      if (source[i] instanceof SeedableModule) {
        ((SeedableModule) source[i]).setSeed(seed);
      }
    }
  }

  public ModuleBasisFunction getBasis(int index) {
    assertMaxSources(index);
    return basis[index];
  }

  @Override
  public double get(double x, double y) {
    switch (type) {
    case BILLOW:
      return getBillow(x, y);
    case DECARPENTIERSWISS:
      return getDeCarpentierSwiss(x, y);
    case FBM:
      return getFBM(x, y);
    case HYBRIDMULTI:
      return getHybridMulti(x, y);
    case MULTI:
      return getMulti(x, y);
    case RIDGEMULTI:
      return getRidgedMulti(x, y);
    default:
      throw new AssertionError();
    }
  }

  @Override
  public double get(double x, double y, double z) {
    switch (type) {
    case BILLOW:
      return getBillow(x, y, z);
    case DECARPENTIERSWISS:
      return getDeCarpentierSwiss(x, y, z);
    case FBM:
      return getFBM(x, y, z);
    case HYBRIDMULTI:
      return getHybridMulti(x, y, z);
    case MULTI:
      return getMulti(x, y, z);
    case RIDGEMULTI:
      return getRidgedMulti(x, y, z);
    default:
      throw new AssertionError();
    }
  }

  @Override
  public double get(double x, double y, double z, double w) {
    switch (type) {
    case BILLOW:
      return getBillow(x, y, z, w);
    case DECARPENTIERSWISS:
      return getDeCarpentierSwiss(x, y, z, w);
    case FBM:
      return getFBM(x, y, z, w);
    case HYBRIDMULTI:
      return getHybridMulti(x, y, z, w);
    case MULTI:
      return getMulti(x, y, z, w);
    case RIDGEMULTI:
      return getRidgedMulti(x, y, z, w);
    default:
      throw new AssertionError();
    }
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    switch (type) {
    case BILLOW:
      return getBillow(x, y, z, w, u, v);
    case DECARPENTIERSWISS:
      return getDeCarpentierSwiss(x, y, z, w, u, v);
    case FBM:
      return getFBM(x, y, z, w, u, v);
    case HYBRIDMULTI:
      return getHybridMulti(x, y, z, w, u, v);
    case MULTI:
      return getMulti(x, y, z, w, u, v);
    case RIDGEMULTI:
      return getRidgedMulti(x, y, z, w, u, v);
    default:
      throw new AssertionError();
    }
  }

  protected double getFBM(double x, double y) {
    double sum = 0;
    double amp = 1.0;

    x *= frequency;
    y *= frequency;

    for (int i = 0; i < numOctaves; ++i) {
      double n = source[i].get(x, y);
      sum += n * amp;
      amp *= gain;

      x *= lacunarity;
      y *= lacunarity;
    }
    return sum;
  }

  protected double getFBM(double x, double y, double z) {
    double sum = 0;
    double amp = 1.0;

    x *= frequency;
    y *= frequency;
    z *= frequency;

    for (int i = 0; i < numOctaves; ++i) {
      double n = source[i].get(x, y, z);
      sum += n * amp;
      amp *= gain;

      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
    }
    return sum;
  }

  protected double getFBM(double x, double y, double z, double w) {
    double sum = 0;
    double amp = 1.0;

    x *= frequency;
    y *= frequency;
    z *= frequency;
    w *= frequency;

    for (int i = 0; i < numOctaves; ++i) {
      double n = source[i].get(x, y, z, w);
      sum += n * amp;
      amp *= gain;

      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
      w *= lacunarity;
    }
    return sum;
  }

  protected double getFBM(double x, double y, double z, double w, double u,
      double v) {
    double sum = 0;
    double amp = 1.0;

    x *= frequency;
    y *= frequency;
    z *= frequency;
    w *= frequency;
    u *= frequency;
    v *= frequency;

    for (int i = 0; i < numOctaves; ++i) {
      double n = source[i].get(x, y, z, w);
      sum += n * amp;
      amp *= gain;

      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
      w *= lacunarity;
      u *= lacunarity;
      v *= lacunarity;
    }
    return sum;
  }

  protected double getMulti(double x, double y) {
    double value = 1.0;
    x *= frequency;
    y *= frequency;
    for (int i = 0; i < numOctaves; ++i) {
      value *= source[i].get(x, y) * exparray[i] + 1.0;
      x *= lacunarity;
      y *= lacunarity;
    }
    return value * correct[numOctaves - 1][0] + correct[numOctaves - 1][1];
  }

  protected double getMulti(double x, double y, double z) {
    double value = 1.0;
    x *= frequency;
    y *= frequency;
    z *= frequency;
    for (int i = 0; i < numOctaves; ++i) {
      value *= source[i].get(x, y, z) * exparray[i] + 1.0;
      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
    }
    return value * correct[numOctaves - 1][0] + correct[numOctaves - 1][1];
  }

  protected double getMulti(double x, double y, double z, double w) {
    double value = 1.0;
    x *= frequency;
    y *= frequency;
    z *= frequency;
    w *= frequency;
    for (int i = 0; i < numOctaves; ++i) {
      value *= source[i].get(x, y, z, w) * exparray[i] + 1.0;
      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
      w *= lacunarity;
    }
    return value * correct[numOctaves - 1][0] + correct[numOctaves - 1][1];
  }

  protected double getMulti(double x, double y, double z, double w, double u,
      double v) {
    double value = 1.0;
    x *= frequency;
    y *= frequency;
    z *= frequency;
    w *= frequency;
    u *= frequency;
    v *= frequency;
    for (int i = 0; i < numOctaves; ++i) {
      value *= source[i].get(x, y, z, w, u, v) * exparray[i] + 1.0;
      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
      w *= lacunarity;
      u *= lacunarity;
      v *= lacunarity;
    }
    return value * correct[numOctaves - 1][0] + correct[numOctaves - 1][1];
  }

  protected double getBillow(double x, double y) {
    double sum = 0.0;
    double amp = 1.0;

    x *= frequency;
    y *= frequency;

    for (int i = 0; i < numOctaves; ++i) {
      double n = source[i].get(x, y);
      sum += (2.0 * Math.abs(n) - 1.0) * amp;
      amp *= gain;

      x *= lacunarity;
      y *= lacunarity;
    }
    return sum;
  }

  protected double getBillow(double x, double y, double z) {
    double sum = 0.0;
    double amp = 1.0;
    x *= frequency;
    y *= frequency;
    z *= frequency;
    for (int i = 0; i < numOctaves; ++i) {
      double n = source[i].get(x, y, z);
      sum += (2.0 * Math.abs(n) - 1.0) * amp;
      amp *= gain;
      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
    }
    return sum;
  }

  protected double getBillow(double x, double y, double z, double w) {
    double sum = 0.0;
    double amp = 1.0;
    x *= frequency;
    y *= frequency;
    z *= frequency;
    w *= frequency;
    for (int i = 0; i < numOctaves; ++i) {
      double n = source[i].get(x, y, z, w);
      sum += (2.0 * Math.abs(n) - 1.0) * amp;
      amp *= gain;
      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
      w *= lacunarity;
    }
    return sum;
  }

  protected double getBillow(double x, double y, double z, double w, double u,
      double v) {
    double sum = 0.0;
    double amp = 1.0;
    x *= frequency;
    y *= frequency;
    z *= frequency;
    w *= frequency;
    u *= frequency;
    v *= frequency;
    for (int i = 0; i < numOctaves; ++i) {
      double n = source[i].get(x, y, z, w, u, v);
      sum += (2.0 * Math.abs(n) - 1.0) * amp;
      amp *= gain;
      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
      w *= lacunarity;
      u *= lacunarity;
      v *= lacunarity;
    }
    return sum;
  }

  protected double getRidgedMulti(double x, double y) {
    double sum = 0;
    double amp = 1.0;
    x *= frequency;
    y *= frequency;
    for (int i = 0; i < numOctaves; ++i) {
      double n = source[i].get(x, y);
      n = 1.0 - Math.abs(n);
      sum += amp * n;
      amp *= gain;
      x *= lacunarity;
      y *= lacunarity;
    }
    return sum;
  }

  protected double getRidgedMulti(double x, double y, double z) {
    double sum = 0;
    double amp = 1.0;
    x *= frequency;
    y *= frequency;
    z *= frequency;
    for (int i = 0; i < numOctaves; ++i) {
      double n = source[i].get(x, y, z);
      n = 1.0 - Math.abs(n);
      sum += amp * n;
      amp *= gain;
      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
    }
    return sum;
  }

  protected double getRidgedMulti(double x, double y, double z, double w) {
    double result = 0.0, signal;
    x *= frequency;
    y *= frequency;
    z *= frequency;
    w *= frequency;
    for (int i = 0; i < numOctaves; ++i) {
      signal = source[i].get(x, y, z, w);
      signal = offset - Math.abs(signal);
      signal *= signal;
      result += signal * exparray[i];
      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
      w *= lacunarity;
    }
    return result * correct[numOctaves - 1][0] + correct[numOctaves - 1][1];
  }

  protected double getRidgedMulti(double x, double y, double z, double w,
      double u, double v) {
    double result = 0.0, signal;
    x *= frequency;
    y *= frequency;
    z *= frequency;
    w *= frequency;
    u *= frequency;
    v *= frequency;
    for (int i = 0; i < numOctaves; ++i) {
      signal = source[i].get(x, y, z, w, u, v);
      signal = offset - Math.abs(signal);
      signal *= signal;
      result += signal * exparray[i];
      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
      w *= lacunarity;
      u *= lacunarity;
      v *= lacunarity;
    }
    return result * correct[numOctaves - 1][0] + correct[numOctaves - 1][1];
  }

  protected double getHybridMulti(double x, double y) {
    double value, signal, weight;
    x *= frequency;
    y *= frequency;
    value = source[0].get(x, y) + offset;
    weight = gain * value;
    x *= lacunarity;
    y *= lacunarity;
    for (int i = 1; i < numOctaves; ++i) {
      if (weight > 1.0) weight = 1.0;
      signal = (source[i].get(x, y) + offset) * exparray[i];
      value += weight * signal;
      weight *= gain * signal;
      x *= lacunarity;
      y *= lacunarity;
    }
    return value * correct[numOctaves - 1][0] + correct[numOctaves - 1][1];
  }

  protected double getHybridMulti(double x, double y, double z) {
    double value, signal, weight;
    x *= frequency;
    y *= frequency;
    z *= frequency;
    value = source[0].get(x, y, z) + offset;
    weight = gain * value;
    x *= lacunarity;
    y *= lacunarity;
    z *= lacunarity;
    for (int i = 1; i < numOctaves; ++i) {
      if (weight > 1.0) weight = 1.0;
      signal = (source[i].get(x, y, z) + offset) * exparray[i];
      value += weight * signal;
      weight *= gain * signal;
      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
    }
    return value * correct[numOctaves - 1][0] + correct[numOctaves - 1][1];
  }

  protected double getHybridMulti(double x, double y, double z, double w) {
    double value, signal, weight;
    x *= frequency;
    y *= frequency;
    z *= frequency;
    w *= frequency;
    value = source[0].get(x, y, z, w) + offset;
    weight = gain * value;
    x *= lacunarity;
    y *= lacunarity;
    z *= lacunarity;
    w *= lacunarity;
    for (int i = 1; i < numOctaves; ++i) {
      if (weight > 1.0) weight = 1.0;
      signal = (source[i].get(x, y, z, w) + offset) * exparray[i];
      value += weight * signal;
      weight *= gain * signal;
      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
      w *= lacunarity;
    }
    return value * correct[numOctaves - 1][0] + correct[numOctaves - 1][1];
  }

  protected double getHybridMulti(double x, double y, double z, double w,
      double u, double v) {
    double value, signal, weight;
    x *= frequency;
    y *= frequency;
    z *= frequency;
    w *= frequency;
    u *= frequency;
    v *= frequency;
    value = source[0].get(x, y, z, w, u, v) + offset;
    weight = gain * value;
    x *= lacunarity;
    y *= lacunarity;
    z *= lacunarity;
    w *= lacunarity;
    u *= lacunarity;
    v *= lacunarity;
    for (int i = 1; i < numOctaves; ++i) {
      if (weight > 1.0) weight = 1.0;
      signal = (source[i].get(x, y, z, w, u, v) + offset) * exparray[i];
      value += weight * signal;
      weight *= gain * signal;
      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
      w *= lacunarity;
      u *= lacunarity;
      v *= lacunarity;
    }
    return value * correct[numOctaves - 1][0] + correct[numOctaves - 1][1];
  }

  protected double getDeCarpentierSwiss(double x, double y) {
    double sum = 0;
    double amp = 1.0;
    double dx_sum = 0;
    double dy_sum = 0;
    x *= frequency;
    y *= frequency;
    for (int i = 0; i < numOctaves; ++i) {
      double n = source[i].get(x + offset * dx_sum, y + offset * dy_sum);
      double dx = source[i].getDX(x + offset * dx_sum, y + offset * dy_sum);
      double dy = source[i].getDY(x + offset * dx_sum, y + offset * dy_sum);
      sum += amp * (1.0 - Math.abs(n));
      dx_sum += amp * dx * -n;
      dy_sum += amp * dy * -n;
      amp *= gain * Util.clamp(sum, 0.0, 1.0);
      x *= lacunarity;
      y *= lacunarity;
    }
    return sum;
  }

  protected double getDeCarpentierSwiss(double x, double y, double z) {
    double sum = 0;
    double amp = 1.0;
    double dx_sum = 0;
    double dy_sum = 0;
    double dz_sum = 0;
    x *= frequency;
    y *= frequency;
    z *= frequency;
    for (int i = 0; i < numOctaves; ++i) {
      double n = source[i].get(x + offset * dx_sum, y + offset * dy_sum, z
          + offset * dz_sum);
      double dx = source[i].getDX(x + offset * dx_sum, y + offset * dy_sum, z
          + offset * dz_sum);
      double dy = source[i].getDY(x + offset * dx_sum, y + offset * dy_sum, z
          + offset * dz_sum);
      double dz = source[i].getDZ(x + offset * dx_sum, y + offset * dy_sum, z
          + offset * dz_sum);
      sum += amp * (1.0 - Math.abs(n));
      dx_sum += amp * dx * -n;
      dy_sum += amp * dy * -n;
      dz_sum += amp * dz * -n;
      amp *= gain * Util.clamp(sum, 0.0, 1.0);
      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
    }
    return sum;
  }

  protected double getDeCarpentierSwiss(double x, double y, double z, double w) {
    double sum = 0;
    double amp = 1.0;
    double dx_sum = 0;
    double dy_sum = 0;
    double dz_sum = 0;
    double dw_sum = 0;
    x *= frequency;
    y *= frequency;
    z *= frequency;
    w *= frequency;
    for (int i = 0; i < numOctaves; ++i) {
      double n = source[i].get(x + offset * dx_sum, y + offset * dy_sum, z
          + offset * dz_sum, w + offset * dw_sum);
      double dx = source[i].getDX(x + offset * dx_sum, y + offset * dy_sum, z
          + offset * dz_sum, w + offset * dw_sum);
      double dy = source[i].getDY(x + offset * dx_sum, y + offset * dy_sum, z
          + offset * dz_sum, w + offset * dw_sum);
      double dz = source[i].getDZ(x + offset * dx_sum, y + offset * dy_sum, z
          + offset * dz_sum, w + offset * dw_sum);
      double dw = source[i].getDW(x + offset * dx_sum, y + offset * dy_sum, z
          + offset * dz_sum, w + offset * dw_sum);
      sum += amp * (1.0 - Math.abs(n));
      dx_sum += amp * dx * -n;
      dy_sum += amp * dy * -n;
      dz_sum += amp * dz * -n;
      dw_sum += amp * dw * -n;
      amp *= gain * Util.clamp(sum, 0.0, 1.0);
      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
      w *= lacunarity;
    }
    return sum;
  }

  protected double getDeCarpentierSwiss(double x, double y, double z, double w,
      double u, double v) {
    double sum = 0;
    double amp = 1.0;
    double dx_sum = 0;
    double dy_sum = 0;
    double dz_sum = 0;
    double dw_sum = 0;
    double du_sum = 0;
    double dv_sum = 0;
    x *= frequency;
    y *= frequency;
    z *= frequency;
    w *= frequency;
    u *= frequency;
    v *= frequency;
    for (int i = 0; i < numOctaves; ++i) {
      double n = source[i].get(x + offset * dx_sum, y + offset * dy_sum, z
          + offset * dz_sum);
      double dx = source[i].getDX(x + offset * dx_sum, y + offset * dy_sum, z
          + offset * dx_sum, w + offset * dw_sum, u + offset * du_sum, v
          + offset * dv_sum);
      double dy = source[i].getDY(x + offset * dx_sum, y + offset * dy_sum, z
          + offset * dz_sum, w + offset * dw_sum, u + offset * du_sum, v
          + offset * dv_sum);
      double dz = source[i].getDZ(x + offset * dx_sum, y + offset * dy_sum, z
          + offset * dz_sum, w + offset * dw_sum, u + offset * du_sum, v
          + offset * dv_sum);
      double dw = source[i].getDW(x + offset * dx_sum, y + offset * dy_sum, z
          + offset * dz_sum, w + offset * dw_sum, u + offset * du_sum, v
          + offset * dv_sum);
      double du = source[i].getDU(x + offset * dx_sum, y + offset * dy_sum, z
          + offset * dz_sum, w + offset * dw_sum, u + offset * du_sum, v
          + offset * dv_sum);
      double dv = source[i].getDV(x + offset * dx_sum, y + offset * dy_sum, z
          + offset * dz_sum, w + offset * dw_sum, u + offset * du_sum, v
          + offset * dv_sum);
      sum += amp * (1.0 - Math.abs(n));
      dx_sum += amp * dx * -n;
      dy_sum += amp * dy * -n;
      dz_sum += amp * dz * -n;
      dw_sum += amp * dw * -n;
      du_sum += amp * du * -n;
      dv_sum += amp * dv * -n;
      amp *= gain * Util.clamp(sum, 0.0, 1.0);
      x *= lacunarity;
      y *= lacunarity;
      z *= lacunarity;
      w *= lacunarity;
      u *= lacunarity;
      v *= lacunarity;
    }
    return sum;
  }

  protected void calcWeights(FractalType type) {
    double minvalue, maxvalue;
    switch (type) {
    case FBM:
      for (int i = 0; i < MAX_SOURCES; i++) {
        exparray[i] = Math.pow(lacunarity, -i * H);
      }
      minvalue = 0.0;
      maxvalue = 0.0;
      for (int i = 0; i < MAX_SOURCES; i++) {
        minvalue += -1.0 * exparray[i];
        maxvalue += 1.0 * exparray[i];

        double A = -1.0, B = 1.0;
        double scale = (B - A) / (maxvalue - minvalue);
        double bias = A - minvalue * scale;
        correct[i][0] = scale;
        correct[i][1] = bias;
      }
      break;

    case RIDGEMULTI:
      for (int i = 0; i < MAX_SOURCES; ++i) {
        exparray[i] = Math.pow(lacunarity, -i * H);
      }
      minvalue = 0.0;
      maxvalue = 0.0;
      for (int i = 0; i < MAX_SOURCES; ++i) {
        minvalue += (offset - 1.0) * (offset - 1.0) * exparray[i];
        maxvalue += (offset) * (offset) * exparray[i];

        double A = -1.0, B = 1.0;
        double scale = (B - A) / (maxvalue - minvalue);
        double bias = A - minvalue * scale;
        correct[i][0] = scale;
        correct[i][1] = bias;
      }
      break;

    case DECARPENTIERSWISS:
      for (int i = 0; i < MAX_SOURCES; ++i) {
        exparray[i] = Math.pow(lacunarity, -i * H);
      }
      minvalue = 0.0;
      maxvalue = 0.0;
      for (int i = 0; i < MAX_SOURCES; ++i) {
        minvalue += (offset - 1.0) * (offset - 1.0) * exparray[i];
        maxvalue += (offset) * (offset) * exparray[i];

        double A = -1.0, B = 1.0;
        double scale = (B - A) / (maxvalue - minvalue);
        double bias = A - minvalue * scale;
        correct[i][0] = scale;
        correct[i][1] = bias;
      }
      break;

    case BILLOW:
      for (int i = 0; i < MAX_SOURCES; ++i) {
        exparray[i] = Math.pow(lacunarity, -i * H);
      }
      minvalue = 0.0;
      maxvalue = 0.0;
      for (int i = 0; i < MAX_SOURCES; ++i) {
        minvalue += -1.0 * exparray[i];
        maxvalue += 1.0 * exparray[i];

        double A = -1.0, B = 1.0;
        double scale = (B - A) / (maxvalue - minvalue);
        double bias = A - minvalue * scale;
        correct[i][0] = scale;
        correct[i][1] = bias;
      }
      break;

    case MULTI:
      for (int i = 0; i < MAX_SOURCES; ++i) {
        exparray[i] = Math.pow(lacunarity, -i * H);
      }
      minvalue = 1.0;
      maxvalue = 1.0;
      for (int i = 0; i < MAX_SOURCES; ++i) {
        minvalue *= -1.0 * exparray[i] + 1.0;
        maxvalue *= 1.0 * exparray[i] + 1.0;

        double A = -1.0, B = 1.0;
        double scale = (B - A) / (maxvalue - minvalue);
        double bias = A - minvalue * scale;
        correct[i][0] = scale;
        correct[i][1] = bias;
      }
      break;

    case HYBRIDMULTI:
      for (int i = 0; i < MAX_SOURCES; ++i) {
        exparray[i] = Math.pow(lacunarity, -i * H);
      }
      minvalue = 1.0;
      maxvalue = 1.0;
      double weightmin,
      weightmax;
      double A = -1.0,
      B = 1.0,
      scale,
      bias;

      minvalue = offset - 1.0;
      maxvalue = offset + 1.0;
      weightmin = gain * minvalue;
      weightmax = gain * maxvalue;

      scale = (B - A) / (maxvalue - minvalue);
      bias = A - minvalue * scale;
      correct[0][0] = scale;
      correct[0][1] = bias;

      for (int i = 1; i < MAX_SOURCES; ++i) {
        if (weightmin > 1.0) weightmin = 1.0;
        if (weightmax > 1.0) weightmax = 1.0;

        double signal = (offset - 1.0) * exparray[i];
        minvalue += signal * weightmin;
        weightmin *= gain * signal;

        signal = (offset + 1.0) * exparray[i];
        maxvalue += signal * weightmax;
        weightmax *= gain * signal;

        scale = (B - A) / (maxvalue - minvalue);
        bias = A - minvalue * scale;
        correct[i][0] = scale;
        correct[i][1] = bias;
      }
      break;
    }

  }

  @Override
  protected void _writeToMap(ModuleMap map) {

    ModulePropertyMap props = new ModulePropertyMap(this);

    writeEnum("type", type, props);
    writeLong("octaves", numOctaves, props);
    writeDouble("frequency", frequency, props);
    writeDouble("lacunarity", lacunarity, props);
    writeDouble("gain", gain, props);
    writeDouble("H", H, props);
    writeDouble("offset", offset, props);
    for (int i = 0; i < numOctaves; i++) {
      props.put("source_" + i, source[i].getId());
      source[i]._writeToMap(map);
    }

    writeSeed(props);

    map.put(getId(), props);

  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap props,
      ModuleInstanceMap map) {

    this.setType(readEnum("type", FractalType.class, props));
    this.setNumOctaves(readLong("octaves", props));
    this.setFrequency(readDouble("frequency", props));
    this.setLacunarity(readDouble("lacunarity", props));
    this.setGain(readDouble("gain", props));
    this.setH(readDouble("H", props));
    this.setOffset(readDouble("offset", props));

    readSeed(props);

    // must override sources after seed has been set
    for (int i = 0; i < numOctaves; i++) {
      overrideSource(i, map.get(props.get("source_" + i)));
    }

    return this;
  }

}
