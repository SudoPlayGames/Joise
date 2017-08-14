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

public class ModuleFunctionGradient extends
    SourcedModule {

  private static final double DEFAULT_SPACING = 0.01;

  public enum FunctionGradientAxis {
    X_AXIS, Y_AXIS, Z_AXIS, W_AXIS, U_AXIS, V_AXIS
  }

  private FunctionGradientAxis axis;
  private double spacing;
  private double invSpacing;

  public ModuleFunctionGradient() {
    this.setAxis(FunctionGradientAxis.X_AXIS);
    this.setSpacing(DEFAULT_SPACING);
  }

  public void setAxis(FunctionGradientAxis axis) {
    this.axis = axis;
  }

  public void setSpacing(double s) {
    this.spacing = s;
    this.invSpacing = 1.0 / this.spacing;
  }

  @Override
  public double get(double x, double y) {

    switch (this.axis) {
      case X_AXIS:
        return (this.source.get(x - this.spacing, y) - this.source.get(x + this.spacing, y)) * this.invSpacing;

      case Y_AXIS:
        return (this.source.get(x, y - this.spacing) - this.source.get(x, y + this.spacing)) * this.invSpacing;

      case Z_AXIS:
        return 0.0;

      case W_AXIS:
        return 0.0;

      case U_AXIS:
        return 0.0;

      case V_AXIS:
        return 0.0;
    }
    return 0.0;
  }

  @Override
  public double get(double x, double y, double z) {
    switch (this.axis) {
      case X_AXIS:
        return (this.source.get(x - this.spacing, y, z) - this.source.get(x + this.spacing, y, z)) * this.invSpacing;

      case Y_AXIS:
        return (this.source.get(x, y - this.spacing, z) - this.source.get(x, y + this.spacing, z)) * this.invSpacing;

      case Z_AXIS:
        return (this.source.get(x, y, z - this.spacing) - this.source.get(x, y, z + this.spacing)) * this.invSpacing;

      case W_AXIS:
        return 0.0;

      case U_AXIS:
        return 0.0;

      case V_AXIS:
        return 0.0;
    }
    return 0.0;
  }

  @Override
  public double get(double x, double y, double z, double w) {
    switch (this.axis) {
      case X_AXIS:
        return (this.source.get(x - this.spacing, y, z, w) - this.source.get(x + this.spacing, y, z, w))
            * this.invSpacing;

      case Y_AXIS:
        return (this.source.get(x, y - this.spacing, z, w) - this.source.get(x, y + this.spacing, z, w))
            * this.invSpacing;

      case Z_AXIS:
        return (this.source.get(x, y, z - this.spacing, w) - this.source.get(x, y, z + this.spacing, w))
            * this.invSpacing;

      case W_AXIS:
        return (this.source.get(x, y, z, w - this.spacing) - this.source.get(x, y, z, w + this.spacing))
            * this.invSpacing;

      case U_AXIS:
        return 0.0;

      case V_AXIS:
        return 0.0;
    }
    return 0.0;
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {

    switch (this.axis) {
      case X_AXIS:
        return (this.source.get(x - this.spacing, y, z, w, u, v) - this.source.get(x + this.spacing, y, z, w, u, v))
            * this.invSpacing;

      case Y_AXIS:
        return (this.source.get(x, y - this.spacing, z, w, u, v) - this.source.get(x, y + this.spacing, z, w, u, v))
            * this.invSpacing;

      case Z_AXIS:
        return (this.source.get(x, y, z - this.spacing, w, u, v) - this.source.get(x, y, z + this.spacing, w, u, v))
            * this.invSpacing;

      case W_AXIS:
        return (this.source.get(x, y, z, w - this.spacing, u, v) - this.source.get(x, y, z, w + this.spacing, u, v))
            * this.invSpacing;

      case U_AXIS:
        return (this.source.get(x, y, z, w, u - this.spacing, v) - this.source.get(x, y, z, w, u + this.spacing, v))
            * this.invSpacing;

      case V_AXIS:
        return (this.source.get(x, y, z, w, u, v - this.spacing) - this.source.get(x, y, z, w, u, v + this.spacing))
            * this.invSpacing;
    }
    return 0.0;
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    modulePropertyMap
        .writeEnum("axis", this.axis)
        .writeDouble("spacing", this.spacing)
        .writeScalar("source", this.source, moduleMap);
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap modulePropertyMap, ModuleInstanceMap moduleInstanceMap) {
    this.setAxis(modulePropertyMap.readEnum("axis", FunctionGradientAxis.class));
    this.setSpacing(modulePropertyMap.readDouble("spacing"));
    this.setSource(modulePropertyMap.readScalar("source", moduleInstanceMap));
    return this;
  }

}
