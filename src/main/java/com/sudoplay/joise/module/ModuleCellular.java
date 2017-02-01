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
import com.sudoplay.joise.module.ModuleCellGen.CellularCache;

public class ModuleCellular extends Module {

  private double[] coefficients = new double[4];
  private ModuleCellGen generator;

  public ModuleCellular() {
    this.setCoefficients(1, 0, 0, 0);
  }

  public void setCellularSource(ModuleCellGen generator) {
    this.generator = generator;
  }

  public void setCoefficients(double a, double b, double c, double d) {
    this.coefficients[0] = a;
    this.coefficients[1] = b;
    this.coefficients[2] = c;
    this.coefficients[3] = d;
  }

  @SuppressWarnings("unused")
  public void setCoefficient(int index, double val) {

    if (index > 3 || index < 0) {
      throw new IllegalArgumentException();
    }
    this.coefficients[index] = val;
  }

  @Override
  public double get(double x, double y) {

    if (this.generator == null) {
      return 0.0;
    }
    CellularCache c = this.generator.getCache(x, y);
    return c.f[0] * this.coefficients[0] + c.f[1] * this.coefficients[1] + c.f[2]
        * this.coefficients[2] + c.f[3] * this.coefficients[3];
  }

  @Override
  public double get(double x, double y, double z) {

    if (this.generator == null) {
      return 0.0;
    }
    CellularCache c = this.generator.getCache(x, y, z);
    return c.f[0] * this.coefficients[0] + c.f[1] * this.coefficients[1] + c.f[2]
        * this.coefficients[2] + c.f[3] * this.coefficients[3];
  }

  @Override
  public double get(double x, double y, double z, double w) {

    if (this.generator == null) {
      return 0.0;
    }
    CellularCache c = this.generator.getCache(x, y, z, w);
    return c.f[0] * this.coefficients[0] + c.f[1] * this.coefficients[1] + c.f[2]
        * this.coefficients[2] + c.f[3] * this.coefficients[3];
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {

    if (this.generator == null) {
      return 0.0;
    }
    CellularCache c = this.generator.getCache(x, y, z, w, u, v);
    return c.f[0] * this.coefficients[0] + c.f[1] * this.coefficients[1] + c.f[2]
        * this.coefficients[2] + c.f[3] * this.coefficients[3];
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);

    if (this.generator != null) {
      modulePropertyMap.put("generator", this.generator.getId());
      this.generator.writeToMap(moduleMap);

    } else {
      modulePropertyMap.put("generator", 0);
    }

    StringBuilder sb = new StringBuilder();

    for (double d : this.coefficients) {
      sb.append(String.valueOf(d)).append(" ");
    }
    modulePropertyMap.put("coefficients", sb.toString().trim());

    moduleMap.put(this.getId(), modulePropertyMap);

  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap modulePropertyMap, ModuleInstanceMap moduleInstanceMap) {
    String coeff = modulePropertyMap.getAsString("coefficients");
    String[] arr = coeff.split(" ");

    for (int i = 0; i < 4; i++) {
      this.coefficients[i] = Double.parseDouble(arr[i]);
    }

    String gen = modulePropertyMap.getAsString("generator");
    this.setCellularSource((ModuleCellGen) moduleInstanceMap.get(gen));
    return this;
  }

}
