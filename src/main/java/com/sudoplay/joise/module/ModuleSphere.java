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

public class ModuleSphere extends
    Module {

  private ScalarParameter cx = new ScalarParameter(0);
  private ScalarParameter cy = new ScalarParameter(0);
  private ScalarParameter cz = new ScalarParameter(0);
  private ScalarParameter cw = new ScalarParameter(0);
  private ScalarParameter cu = new ScalarParameter(0);
  private ScalarParameter cv = new ScalarParameter(0);
  private ScalarParameter radius = new ScalarParameter(1);

  public void setCenterX(double source) {
    this.cx.set(source);
  }

  public void setCenterY(double source) {
    this.cy.set(source);
  }

  @SuppressWarnings("unused")
  public void setCenterZ(double source) {
    this.cz.set(source);
  }

  @SuppressWarnings("unused")
  public void setCenterW(double source) {
    this.cw.set(source);
  }

  @SuppressWarnings("unused")
  public void setCenterU(double source) {
    this.cu.set(source);
  }

  @SuppressWarnings("unused")
  public void setCenterV(double source) {
    this.cv.set(source);
  }

  public void setRadius(double source) {
    this.radius.set(source);
  }

  @SuppressWarnings("unused")
  public void setCenterX(Module source) {
    this.cx.set(source);
  }

  @SuppressWarnings("unused")
  public void setCenterY(Module source) {
    this.cy.set(source);
  }

  public void setCenterZ(Module source) {
    this.cz.set(source);
  }

  @SuppressWarnings("unused")
  public void setCenterW(Module source) {
    this.cw.set(source);
  }

  @SuppressWarnings("unused")
  public void setCenterU(Module source) {
    this.cu.set(source);
  }

  @SuppressWarnings("unused")
  public void setCenterV(Module source) {
    this.cv.set(source);
  }

  @SuppressWarnings("unused")
  public void setRadius(Module source) {
    this.radius.set(source);
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public void setCenterX(ScalarParameter scalarParameter) {
    this.cx.set(scalarParameter);
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public void setCenterY(ScalarParameter scalarParameter) {
    this.cy.set(scalarParameter);
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public void setCenterZ(ScalarParameter scalarParameter) {
    this.cz.set(scalarParameter);
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public void setCenterW(ScalarParameter scalarParameter) {
    this.cw.set(scalarParameter);
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public void setCenterU(ScalarParameter scalarParameter) {
    this.cu.set(scalarParameter);
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public void setCenterV(ScalarParameter scalarParameter) {
    this.cv.set(scalarParameter);
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public void setRadius(ScalarParameter scalarParameter) {
    this.radius.set(scalarParameter);
  }

  @Override
  public double get(double x, double y) {
    double dx = x - this.cx.get(x, y), dy = y - this.cy.get(x, y);
    double len = Math.sqrt(dx * dx + dy * dy);
    double r = this.radius.get(x, y);
    double i = (r - len) / r;

    if (i < 0) {
      i = 0;
    }

    if (i > 1) {
      i = 1;
    }
    return i;
  }

  @Override
  public double get(double x, double y, double z) {
    double dx = x - this.cx.get(x, y, z), dy = y - this.cy.get(x, y, z), dz = z - this.cz.get(x, y, z);
    double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
    double r = this.radius.get(x, y, z);
    double i = (r - len) / r;

    if (i < 0) {
      i = 0;
    }

    if (i > 1) {
      i = 1;
    }
    return i;
  }

  @Override
  public double get(double x, double y, double z, double w) {
    double dx = x - this.cx.get(x, y, z, w), dy = y - this.cy.get(x, y, z, w), dz = z
        - this.cz.get(x, y, z, w), dw = w - this.cw.get(x, y, z, w);
    double len = Math.sqrt(dx * dx + dy * dy + dz * dz + dw * dw);
    double r = this.radius.get(x, y, z, w);
    double i = (r - len) / r;

    if (i < 0) {
      i = 0;
    }

    if (i > 1) {
      i = 1;
    }
    return i;
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    double dx = x - this.cx.get(x, y, z, w, u, v), dy = y - this.cy.get(x, y, z, w, u, v), dz = z
        - this.cz.get(x, y, z, w, u, v), dw = w - this.cw.get(x, y, z, w, u, v), du = u
        - this.cu.get(x, y, z, w, u, v), dv = v - this.cv.get(x, y, z, w, u, v);
    double len = Math.sqrt(dx * dx + dy * dy + dz * dz + dw * dw + du * du + dv * dv);
    double r = this.radius.get(x, y, z, w, u, v);
    double i = (r - len) / r;

    if (i < 0) {
      i = 0;
    }

    if (i > 1) {
      i = 1;
    }
    return i;
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    modulePropertyMap
        .writeScalar("cx", this.cx, moduleMap)
        .writeScalar("cy", this.cy, moduleMap)
        .writeScalar("cz", this.cz, moduleMap)
        .writeScalar("cw", this.cw, moduleMap)
        .writeScalar("cu", this.cu, moduleMap)
        .writeScalar("cv", this.cv, moduleMap)
        .writeScalar("radius", this.radius, moduleMap);
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap modulePropertyMap, ModuleInstanceMap moduleInstanceMap) {
    this.setCenterX(modulePropertyMap.readScalar("cx", moduleInstanceMap));
    this.setCenterY(modulePropertyMap.readScalar("cy", moduleInstanceMap));
    this.setCenterZ(modulePropertyMap.readScalar("cz", moduleInstanceMap));
    this.setCenterW(modulePropertyMap.readScalar("cw", moduleInstanceMap));
    this.setCenterU(modulePropertyMap.readScalar("cu", moduleInstanceMap));
    this.setCenterV(modulePropertyMap.readScalar("cv", moduleInstanceMap));
    this.setRadius(modulePropertyMap.readScalar("radius", moduleInstanceMap));
    return this;
  }
}
