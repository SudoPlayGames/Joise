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
import com.sudoplay.joise.module.ModuleCellGen.CellularCache;

public class ModuleCellular extends Module {

  protected double[] coefficients = new double[4];
  protected ModuleCellGen generator;

  public ModuleCellular() {
    setCoefficients(1, 0, 0, 0);
  }

  public void setCellularSource(ModuleCellGen generator) {
    this.generator = generator;
  }

  public void setCoefficients(double a, double b, double c, double d) {
    coefficients[0] = a;
    coefficients[1] = b;
    coefficients[2] = c;
    coefficients[3] = d;
  }

  public void setCoefficient(int index, double val) {
    if (index > 3 || index < 0) {
      throw new IllegalArgumentException();
    }
    coefficients[index] = val;
  }

  @Override
  public double get(double x, double y) {
    if (generator == null) {
      return 0.0;
    }
    CellularCache c = generator.getCache(x, y);
    return c.f[0] * coefficients[0] + c.f[1] * coefficients[1] + c.f[2]
        * coefficients[2] + c.f[3] * coefficients[3];
  }

  @Override
  public double get(double x, double y, double z) {
    if (generator == null) {
      return 0.0;
    }
    CellularCache c = generator.getCache(x, y, z);
    return c.f[0] * coefficients[0] + c.f[1] * coefficients[1] + c.f[2]
        * coefficients[2] + c.f[3] * coefficients[3];
  }

  @Override
  public double get(double x, double y, double z, double w) {
    if (generator == null) {
      return 0.0;
    }
    CellularCache c = generator.getCache(x, y, z, w);
    return c.f[0] * coefficients[0] + c.f[1] * coefficients[1] + c.f[2]
        * coefficients[2] + c.f[3] * coefficients[3];
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    if (generator == null) {
      return 0.0;
    }
    CellularCache c = generator.getCache(x, y, z, w, u, v);
    return c.f[0] * coefficients[0] + c.f[1] * coefficients[1] + c.f[2]
        * coefficients[2] + c.f[3] * coefficients[3];
  }

  @Override
  protected void _writeToMap(ModuleMap map) {

    ModulePropertyMap props = new ModulePropertyMap(this);

    if (generator != null) {
      props.put("generator", generator.getId());
      generator._writeToMap(map);
    } else {
      props.put("generator", 0);
    }

    StringBuilder sb = new StringBuilder();
    for (double d : coefficients) {
      sb.append(String.valueOf(d)).append(" ");
    }
    props.put("coefficients", sb.toString().trim());

    map.put(getId(), props);

  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap props,
      ModuleInstanceMap map) {

    String coeff = props.getAsString("coefficients");
    String[] arr = coeff.split(" ");
    for (int i = 0; i < 4; i++) {
      coefficients[i] = Double.parseDouble(arr[i]);
    }

    String gen = props.getAsString("generator");
    setCellularSource((ModuleCellGen) map.get(gen));

    return this;
  }

}
