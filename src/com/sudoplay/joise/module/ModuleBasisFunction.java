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
import com.sudoplay.joise.generator.LCG;
import com.sudoplay.joise.noise.Interpolator;
import com.sudoplay.joise.noise.Noise;

public class ModuleBasisFunction extends SeedableModule {

  public enum BasisType {
    VALUE, GRADIENT, GRADVAL, SIMPLEX, WHITE
  }

  public enum InterpolationType {
    NONE, LINEAR, CUBIC, QUINTIC
  }

  protected double[] scale = new double[4];
  protected double[] offset = new double[4];
  protected double[][] rotMatrix = new double[3][3];
  protected double cos2d, sin2d;

  protected Noise.Function2D func2D;
  protected Noise.Function3D func3D;
  protected Noise.Function4D func4D;
  protected Noise.Function6D func6D;

  protected Interpolator interpolator;

  protected BasisType basisType;
  protected InterpolationType interpolationType;

  public ModuleBasisFunction() {
    this(BasisType.GRADIENT, InterpolationType.QUINTIC, 10000);
  }

  public ModuleBasisFunction(BasisType type) {
    this(type, InterpolationType.QUINTIC, 10000);
  }

  public ModuleBasisFunction(BasisType type, InterpolationType interpolationType) {
    this(type, interpolationType, 10000);
  }

  public ModuleBasisFunction(BasisType type,
      InterpolationType interpolationType, long seed) {
    setType(type);
    setInterpolation(interpolationType);
    setSeed(seed);
  }

  public void setType(BasisType type) {
    basisType = type;
    switch (type) {
    case GRADVAL:
      func2D = Noise.Function2D.GRADVAL;
      func3D = Noise.Function3D.GRADVAL;
      func4D = Noise.Function4D.GRADVAL;
      func6D = Noise.Function6D.GRADVAL;
      break;
    case SIMPLEX:
      func2D = Noise.Function2D.SIMPLEX;
      func3D = Noise.Function3D.SIMPLEX;
      func4D = Noise.Function4D.SIMPLEX;
      func6D = Noise.Function6D.SIMPLEX;
      break;
    case VALUE:
      func2D = Noise.Function2D.VALUE;
      func3D = Noise.Function3D.VALUE;
      func4D = Noise.Function4D.VALUE;
      func6D = Noise.Function6D.VALUE;
      break;
    case WHITE:
      func2D = Noise.Function2D.WHITE;
      func3D = Noise.Function3D.WHITE;
      func4D = Noise.Function4D.WHITE;
      func6D = Noise.Function6D.WHITE;
      break;
    case GRADIENT:
      // fallthrough intentional
    default:
      func2D = Noise.Function2D.GRADIENT;
      func3D = Noise.Function3D.GRADIENT;
      func4D = Noise.Function4D.GRADIENT;
      func6D = Noise.Function6D.GRADIENT;
      break;
    }
    setMagicNumbers(type);
  }

  public BasisType getBasisType() {
    return basisType;
  }

  public void setInterpolation(InterpolationType type) {
    interpolationType = type;
    switch (type) {
    case CUBIC:
      this.interpolator = Interpolator.HERMITE;
      break;
    case LINEAR:
      this.interpolator = Interpolator.LINEAR;
      break;
    case NONE:
      this.interpolator = Interpolator.NONE;
      break;
    default:
      this.interpolator = Interpolator.QUINTIC;
      break;
    }
  }

  public InterpolationType getInterpolationType() {
    return interpolationType;
  }

  /**
   * Set the rotation axis and angle to use for 3D, 4D and 6D noise.
   * 
   * @param x
   * @param y
   * @param z
   * @param angle
   */
  public void setRotationAngle(double x, double y, double z, double angle) {
    double sin = Math.sin(angle);
    double cos = Math.cos(angle);

    rotMatrix[0][0] = 1 + (1 - cos) * (x * x - 1);
    rotMatrix[1][0] = -z * sin + (1 - cos) * x * y;
    rotMatrix[2][0] = y * sin + (1 - cos) * x * z;

    rotMatrix[0][1] = z * sin + (1 - cos) * x * y;
    rotMatrix[1][1] = 1 + (1 - cos) * (y * y - 1);
    rotMatrix[2][1] = -x * sin + (1 - cos) * y * z;

    rotMatrix[0][2] = -y * sin + (1 - cos) * x * z;
    rotMatrix[1][2] = x * sin + (1 - cos) * y * z;
    rotMatrix[2][2] = 1 + (1 - cos) * (z * z - 1);
  }

  @Override
  public void setSeed(long seed) {
    super.setSeed(seed);
    
    LCG lcg = new LCG();
    lcg.setSeed(seed);

    double ax, ay, az;
    double len;

    ax = lcg.get01();
    ay = lcg.get01();
    az = lcg.get01();
    len = Math.sqrt(ax * ax + ay * ay + az * az);
    ax /= len;
    ay /= len;
    az /= len;
    setRotationAngle(ax, ay, az, lcg.get01() * 3.141592 * 2.0);
    double angle = lcg.get01() * 3.141592 * 2.0;
    cos2d = Math.cos(angle);
    sin2d = Math.sin(angle);
  }

  @Override
  public double get(double x, double y) {
    double nx, ny;
    nx = x * cos2d - y * sin2d;
    ny = y * cos2d + x * sin2d;
    return func2D.get(nx, ny, seed, interpolator);
  }

  @Override
  public double get(double x, double y, double z) {
    double nx, ny, nz;
    nx = (rotMatrix[0][0] * x) + (rotMatrix[1][0] * y) + (rotMatrix[2][0] * z);
    ny = (rotMatrix[0][1] * x) + (rotMatrix[1][1] * y) + (rotMatrix[2][1] * z);
    nz = (rotMatrix[0][2] * x) + (rotMatrix[1][2] * y) + (rotMatrix[2][2] * z);
    return func3D.get(nx, ny, nz, seed, interpolator);
  }

  @Override
  public double get(double x, double y, double z, double w) {
    double nx, ny, nz;
    nx = (rotMatrix[0][0] * x) + (rotMatrix[1][0] * y) + (rotMatrix[2][0] * z);
    ny = (rotMatrix[0][1] * x) + (rotMatrix[1][1] * y) + (rotMatrix[2][1] * z);
    nz = (rotMatrix[0][2] * x) + (rotMatrix[1][2] * y) + (rotMatrix[2][2] * z);
    return func4D.get(nx, ny, nz, w, seed, interpolator);
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    double nx, ny, nz;
    nx = (rotMatrix[0][0] * x) + (rotMatrix[1][0] * y) + (rotMatrix[2][0] * z);
    ny = (rotMatrix[0][1] * x) + (rotMatrix[1][1] * y) + (rotMatrix[2][1] * z);
    nz = (rotMatrix[0][2] * x) + (rotMatrix[1][2] * y) + (rotMatrix[2][2] * z);
    return func6D.get(nx, ny, nz, w, u, v, seed, interpolator);
  }

  protected void setMagicNumbers(BasisType type) {
    switch (type) {
    case VALUE:
      scale[0] = 1.0;
      offset[0] = 0.0;
      scale[1] = 1.0;
      offset[1] = 0.0;
      scale[2] = 1.0;
      offset[2] = 0.0;
      scale[3] = 1.0;
      offset[3] = 0.0;
      break;

    case GRADIENT:
      scale[0] = 1.86848;
      offset[0] = -0.000118;
      scale[1] = 1.85148;
      offset[1] = -0.008272;
      scale[2] = 1.64127;
      offset[2] = -0.01527;
      scale[3] = 1.92517;
      offset[3] = 0.03393;
      break;

    case GRADVAL:
      scale[0] = 0.6769;
      offset[0] = -0.00151;
      scale[1] = 0.6957;
      offset[1] = -0.133;
      scale[2] = 0.74622;
      offset[2] = 0.01916;
      scale[3] = 0.7961;
      offset[3] = -0.0352;
      break;

    case WHITE:
      scale[0] = 1.0;
      offset[0] = 0.0;
      scale[1] = 1.0;
      offset[1] = 0.0;
      scale[2] = 1.0;
      offset[2] = 0.0;
      scale[3] = 1.0;
      offset[3] = 0.0;
      break;

    default:
      scale[0] = 1.0;
      offset[0] = 0.0;
      scale[1] = 1.0;
      offset[1] = 0.0;
      scale[2] = 1.0;
      offset[2] = 0.0;
      scale[3] = 1.0;
      offset[3] = 0.0;
      break;
    }
  }

  @Override
  protected void _writeToMap(ModuleMap map) {

    ModulePropertyMap props = new ModulePropertyMap(this);

    writeEnum("basis", getBasisType(), props);
    writeEnum("interpolation", getInterpolationType(), props);
    writeSeed(props);

    map.put(getId(), props);

  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap props,
      ModuleInstanceMap map) {

    readEnum("basis", "setType", BasisType.class, props);
    readEnum("interpolation", "setInterpolation", InterpolationType.class,
        props);
    readSeed(props);

    return this;
  }

}
