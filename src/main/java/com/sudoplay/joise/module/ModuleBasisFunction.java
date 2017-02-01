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
import com.sudoplay.joise.noise.Interpolator;
import com.sudoplay.joise.noise.function.*;
import com.sudoplay.joise.noise.function.spi.Function2D;
import com.sudoplay.joise.noise.function.spi.Function3D;
import com.sudoplay.joise.noise.function.spi.Function4D;
import com.sudoplay.joise.noise.function.spi.Function6D;

public class ModuleBasisFunction extends
    SeededModule {

  public enum BasisType {
    VALUE, GRADIENT, GRADVAL, SIMPLEX, WHITE
  }

  public enum InterpolationType {
    NONE, LINEAR, CUBIC, QUINTIC
  }

  private double[] scale = new double[4];
  private double[] offset = new double[4];
  private double[][] rotMatrix = new double[3][3];
  private double cos2d, sin2d;

  private Function2D func2D;
  private Function3D func3D;
  private Function4D func4D;
  private Function6D func6D;

  private Interpolator interpolator;
  private BasisType basisType;
  private InterpolationType interpolationType;

  public ModuleBasisFunction() {
    this(BasisType.GRADIENT, InterpolationType.QUINTIC, 10000);
  }

  public ModuleBasisFunction(BasisType type) {
    this(type, InterpolationType.QUINTIC, 10000);
  }

  public ModuleBasisFunction(BasisType type, InterpolationType interpolationType) {
    this(type, interpolationType, 10000);
  }

  public ModuleBasisFunction(BasisType type, InterpolationType interpolationType, long seed) {
    this.setType(type);
    this.setInterpolation(interpolationType);
    this.setSeed(seed);
  }

  public void setType(BasisType type) {
    this.basisType = type;

    switch (type) {
      case GRADVAL:
        this.func2D = new Function2DGradVal();
        this.func3D = new Function3DGradVal();
        this.func4D = new Function4DGradVal();
        this.func6D = new Function6DGradVal();
        break;

      case SIMPLEX:
        this.func2D = new Function2DSimplex();
        this.func3D = new Function3DSimplex();
        this.func4D = new Function4DSimplex();
        this.func6D = new Function6DSimplex();
        break;

      case VALUE:
        this.func2D = new Function2DValue();
        this.func3D = new Function3DValue();
        this.func4D = new Function4DValue();
        this.func6D = new Function6DValue();
        break;

      case WHITE:
        this.func2D = new Function2DWhite();
        this.func3D = new Function3DWhite();
        this.func4D = new Function4DWhite();
        this.func6D = new Function6DWhite();
        break;

      case GRADIENT:
        // fallthrough intentional

      default:
        this.func2D = new Function2DGradient();
        this.func3D = new Function3DGradient();
        this.func4D = new Function4DGradient();
        this.func6D = new Function6DGradient();
        break;
    }
    this.setMagicNumbers(type);
  }

  public BasisType getBasisType() {
    return this.basisType;
  }

  public void setInterpolation(InterpolationType type) {
    this.interpolationType = type;

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
    return this.interpolationType;
  }

  /**
   * Set the rotation axis and angle to use for 3D, 4D and 6D noise.
   *
   * @param x     x value of axis vector
   * @param y     y value of axis vector
   * @param z     z value of axis vector
   * @param angle angle in radians
   */
  @SuppressWarnings("WeakerAccess")
  public void setRotationAngle(double x, double y, double z, double angle) {
    double sin = Math.sin(angle);
    double cos = Math.cos(angle);

    this.rotMatrix[0][0] = 1 + (1 - cos) * (x * x - 1);
    this.rotMatrix[1][0] = -z * sin + (1 - cos) * x * y;
    this.rotMatrix[2][0] = y * sin + (1 - cos) * x * z;

    this.rotMatrix[0][1] = z * sin + (1 - cos) * x * y;
    this.rotMatrix[1][1] = 1 + (1 - cos) * (y * y - 1);
    this.rotMatrix[2][1] = -x * sin + (1 - cos) * y * z;

    this.rotMatrix[0][2] = -y * sin + (1 - cos) * x * z;
    this.rotMatrix[1][2] = x * sin + (1 - cos) * y * z;
    this.rotMatrix[2][2] = 1 + (1 - cos) * (z * z - 1);
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
    this.setRotationAngle(ax, ay, az, lcg.get01() * 3.141592 * 2.0);
    double angle = lcg.get01() * 3.141592 * 2.0;
    this.cos2d = Math.cos(angle);
    this.sin2d = Math.sin(angle);
  }

  @Override
  public double get(double x, double y) {
    double nx, ny;
    nx = x * this.cos2d - y * this.sin2d;
    ny = y * this.cos2d + x * this.sin2d;
    return this.func2D.get(nx, ny, this.seed, this.interpolator);
  }

  @Override
  public double get(double x, double y, double z) {
    double nx, ny, nz;
    nx = (this.rotMatrix[0][0] * x) + (this.rotMatrix[1][0] * y) + (this.rotMatrix[2][0] * z);
    ny = (this.rotMatrix[0][1] * x) + (this.rotMatrix[1][1] * y) + (this.rotMatrix[2][1] * z);
    nz = (this.rotMatrix[0][2] * x) + (this.rotMatrix[1][2] * y) + (this.rotMatrix[2][2] * z);
    return this.func3D.get(nx, ny, nz, this.seed, this.interpolator);
  }

  @Override
  public double get(double x, double y, double z, double w) {
    double nx, ny, nz;
    nx = (this.rotMatrix[0][0] * x) + (this.rotMatrix[1][0] * y) + (this.rotMatrix[2][0] * z);
    ny = (this.rotMatrix[0][1] * x) + (this.rotMatrix[1][1] * y) + (this.rotMatrix[2][1] * z);
    nz = (this.rotMatrix[0][2] * x) + (this.rotMatrix[1][2] * y) + (this.rotMatrix[2][2] * z);
    return this.func4D.get(nx, ny, nz, w, this.seed, this.interpolator);
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    double nx, ny, nz;
    nx = (this.rotMatrix[0][0] * x) + (this.rotMatrix[1][0] * y) + (this.rotMatrix[2][0] * z);
    ny = (this.rotMatrix[0][1] * x) + (this.rotMatrix[1][1] * y) + (this.rotMatrix[2][1] * z);
    nz = (this.rotMatrix[0][2] * x) + (this.rotMatrix[1][2] * y) + (this.rotMatrix[2][2] * z);
    return this.func6D.get(nx, ny, nz, w, u, v, this.seed, this.interpolator);
  }

  private void setMagicNumbers(BasisType type) {

    switch (type) {
      case VALUE:
        this.scale[0] = 1.0;
        this.offset[0] = 0.0;
        this.scale[1] = 1.0;
        this.offset[1] = 0.0;
        this.scale[2] = 1.0;
        this.offset[2] = 0.0;
        this.scale[3] = 1.0;
        this.offset[3] = 0.0;
        break;

      case GRADIENT:
        this.scale[0] = 1.86848;
        this.offset[0] = -0.000118;
        this.scale[1] = 1.85148;
        this.offset[1] = -0.008272;
        this.scale[2] = 1.64127;
        this.offset[2] = -0.01527;
        this.scale[3] = 1.92517;
        this.offset[3] = 0.03393;
        break;

      case GRADVAL:
        this.scale[0] = 0.6769;
        this.offset[0] = -0.00151;
        this.scale[1] = 0.6957;
        this.offset[1] = -0.133;
        this.scale[2] = 0.74622;
        this.offset[2] = 0.01916;
        this.scale[3] = 0.7961;
        this.offset[3] = -0.0352;
        break;

      case WHITE:
        this.scale[0] = 1.0;
        this.offset[0] = 0.0;
        this.scale[1] = 1.0;
        this.offset[1] = 0.0;
        this.scale[2] = 1.0;
        this.offset[2] = 0.0;
        this.scale[3] = 1.0;
        this.offset[3] = 0.0;
        break;

      default:
        this.scale[0] = 1.0;
        this.offset[0] = 0.0;
        this.scale[1] = 1.0;
        this.offset[1] = 0.0;
        this.scale[2] = 1.0;
        this.offset[2] = 0.0;
        this.scale[3] = 1.0;
        this.offset[3] = 0.0;
        break;
    }
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    modulePropertyMap
        .writeEnum("basis", this.getBasisType())
        .writeEnum("interpolation", this.getInterpolationType());

    this.writeSeed(modulePropertyMap);
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap modulePropertyMap, ModuleInstanceMap moduleInstanceMap) {
    this.setType(modulePropertyMap.readEnum("basis", BasisType.class));
    this.setInterpolation(modulePropertyMap.readEnum("interpolation", InterpolationType.class));
    this.readSeed(modulePropertyMap);
    return this;
  }

}
