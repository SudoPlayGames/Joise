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

public class ModuleSphere extends Module {

  protected ScalarParameter cx = new ScalarParameter(0);
  protected ScalarParameter cy = new ScalarParameter(0);
  protected ScalarParameter cz = new ScalarParameter(0);
  protected ScalarParameter cw = new ScalarParameter(0);
  protected ScalarParameter cu = new ScalarParameter(0);
  protected ScalarParameter cv = new ScalarParameter(0);
  protected ScalarParameter radius = new ScalarParameter(1);

  public void setCenterX(double source) {
    cx.set(source);
  }

  public void setCenterY(double source) {
    cy.set(source);
  }

  public void setCenterZ(double source) {
    cz.set(source);
  }

  public void setCenterW(double source) {
    cw.set(source);
  }

  public void setCenterU(double source) {
    cu.set(source);
  }

  public void setCenterV(double source) {
    cv.set(source);
  }

  public void setRadius(double source) {
    radius.set(source);
  }

  public void setCenterX(Module source) {
    cx.set(source);
  }

  public void setCenterY(Module source) {
    cy.set(source);
  }

  public void setCenterZ(Module source) {
    cz.set(source);
  }

  public void setCenterW(Module source) {
    cw.set(source);
  }

  public void setCenterU(Module source) {
    cu.set(source);
  }

  public void setCenterV(Module source) {
    cv.set(source);
  }

  public void setRadius(Module source) {
    radius.set(source);
  }

  public void setCenterX(ScalarParameter scalarParameter) {
    cx.set(scalarParameter);
  }

  public void setCenterY(ScalarParameter scalarParameter) {
    cy.set(scalarParameter);
  }

  public void setCenterZ(ScalarParameter scalarParameter) {
    cz.set(scalarParameter);
  }

  public void setCenterW(ScalarParameter scalarParameter) {
    cw.set(scalarParameter);
  }

  public void setCenterU(ScalarParameter scalarParameter) {
    cu.set(scalarParameter);
  }

  public void setCenterV(ScalarParameter scalarParameter) {
    cv.set(scalarParameter);
  }

  public void setRadius(ScalarParameter scalarParameter) {
    radius.set(scalarParameter);
  }

  @Override
  public double get(double x, double y) {
    double dx = x - cx.get(x, y), dy = y - cy.get(x, y);
    double len = Math.sqrt(dx * dx + dy * dy);
    double r = radius.get(x, y);
    double i = (r - len) / r;
    if (i < 0) i = 0;
    if (i > 1) i = 1;

    return i;
  }

  @Override
  public double get(double x, double y, double z) {
    double dx = x - cx.get(x, y, z), dy = y - cy.get(x, y, z), dz = z
        - cz.get(x, y, z);
    double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
    double r = radius.get(x, y, z);
    double i = (r - len) / r;
    if (i < 0) i = 0;
    if (i > 1) i = 1;

    return i;
  }

  @Override
  public double get(double x, double y, double z, double w) {
    double dx = x - cx.get(x, y, z, w), dy = y - cy.get(x, y, z, w), dz = z
        - cz.get(x, y, z, w), dw = w - cw.get(x, y, z, w);
    double len = Math.sqrt(dx * dx + dy * dy + dz * dz + dw * dw);
    double r = radius.get(x, y, z, w);
    double i = (r - len) / r;
    if (i < 0) i = 0;
    if (i > 1) i = 1;

    return i;
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    double dx = x - cx.get(x, y, z, w, u, v), dy = y - cy.get(x, y, z, w, u, v), dz = z
        - cz.get(x, y, z, w, u, v), dw = w - cw.get(x, y, z, w, u, v), du = u
        - cu.get(x, y, z, w, u, v), dv = v - cv.get(x, y, z, w, u, v);
    double len = Math.sqrt(dx * dx + dy * dy + dz * dz + dw * dw + du * du + dv
        * dv);
    double r = radius.get(x, y, z, w, u, v);
    double i = (r - len) / r;
    if (i < 0) i = 0;
    if (i > 1) i = 1;

    return i;
  }

  @Override
  protected void _writeToMap(ModuleMap map) {

    ModulePropertyMap props = new ModulePropertyMap(this);

    writeScalar("cx", cx, props, map);
    writeScalar("cy", cy, props, map);
    writeScalar("cz", cz, props, map);
    writeScalar("cw", cw, props, map);
    writeScalar("cu", cu, props, map);
    writeScalar("cv", cv, props, map);
    writeScalar("radius", radius, props, map);

    map.put(getId(), props);

  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap props,
      ModuleInstanceMap map) {

    this.setCenterX(readScalar("cx", props, map));
    this.setCenterY(readScalar("cy", props, map));
    this.setCenterZ(readScalar("cz", props, map));
    this.setCenterW(readScalar("cw", props, map));
    this.setCenterU(readScalar("cu", props, map));
    this.setCenterV(readScalar("cv", props, map));
    this.setRadius(readScalar("radius", props, map));

    return this;
  }

}
