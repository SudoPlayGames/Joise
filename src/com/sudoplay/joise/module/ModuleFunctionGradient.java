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

public class ModuleFunctionGradient extends SourcedModule {

  public static final double DEFAULT_SPACING = 0.01;

  public static enum FunctionGradientAxis {
    X_AXIS, Y_AXIS, Z_AXIS, W_AXIS, U_AXIS, V_AXIS
  }

  protected FunctionGradientAxis axis;
  protected double spacing;
  protected double invSpacing;

  public ModuleFunctionGradient() {
    setAxis(FunctionGradientAxis.X_AXIS);
    setSpacing(DEFAULT_SPACING);
  }

  public void setAxis(FunctionGradientAxis axis) {
    this.axis = axis;
  }

  public void setSpacing(double s) {
    spacing = s;
    invSpacing = 1.0 / spacing;
  }

  @Override
  public double get(double x, double y) {
    switch (axis) {
    case X_AXIS:
      return (source.get(x - spacing, y) - source.get(x + spacing, y))
          * invSpacing;
    case Y_AXIS:
      return (source.get(x, y - spacing) - source.get(x, y + spacing))
          * invSpacing;
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
    switch (axis) {
    case X_AXIS:
      return (source.get(x - spacing, y, z) - source.get(x + spacing, y, z))
          * invSpacing;
    case Y_AXIS:
      return (source.get(x, y - spacing, z) - source.get(x, y + spacing, z))
          * invSpacing;
    case Z_AXIS:
      return (source.get(x, y, z - spacing) - source.get(x, y, z + spacing))
          * invSpacing;
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
    switch (axis) {
    case X_AXIS:
      return (source.get(x - spacing, y, z, w) - source.get(x + spacing, y, z,
          w)) * invSpacing;

    case Y_AXIS:
      return (source.get(x, y - spacing, z, w) - source.get(x, y + spacing, z,
          w)) * invSpacing;

    case Z_AXIS:
      return (source.get(x, y, z - spacing, w) - source.get(x, y, z + spacing,
          w)) * invSpacing;

    case W_AXIS:
      return (source.get(x, y, z, w - spacing) - source.get(x, y, z, w
          + spacing))
          * invSpacing;
    case U_AXIS:
      return 0.0;
    case V_AXIS:
      return 0.0;
    }
    return 0.0;
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    switch (axis) {
    case X_AXIS:
      return (source.get(x - spacing, y, z, w, u, v) - source.get(x + spacing,
          y, z, w, u, v)) * invSpacing;

    case Y_AXIS:
      return (source.get(x, y - spacing, z, w, u, v) - source.get(x, y
          + spacing, z, w, u, v))
          * invSpacing;
    case Z_AXIS:
      return (source.get(x, y, z - spacing, w, u, v) - source.get(x, y, z
          + spacing, w, u, v))
          * invSpacing;
    case W_AXIS:
      return (source.get(x, y, z, w - spacing, u, v) - source.get(x, y, z, w
          + spacing, u, v))
          * invSpacing;
    case U_AXIS:
      return (source.get(x, y, z, w, u - spacing, v) - source.get(x, y, z, w, u
          + spacing, v))
          * invSpacing;
    case V_AXIS:
      return (source.get(x, y, z, w, u, v - spacing) - source.get(x, y, z, w,
          u, v + spacing)) * invSpacing;

    }
    return 0.0;
  }

  @Override
  protected void _writeToMap(ModuleMap map) {

    ModulePropertyMap props = new ModulePropertyMap(this);

    writeEnum("axis", axis, props);
    writeDouble("spacing", spacing, props);
    writeSource(props, map);

    map.put(getId(), props);

  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap props,
      ModuleInstanceMap map) {

    this.setAxis(readEnum("axis", FunctionGradientAxis.class, props));
    this.setSpacing(readDouble("spacing", props));
    readSource(props, map);

    return this;
  }

}
