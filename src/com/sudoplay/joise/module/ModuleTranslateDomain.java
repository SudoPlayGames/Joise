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

public class ModuleTranslateDomain extends SourcedModule {

  protected ScalarParameter ax = new ScalarParameter(0);
  protected ScalarParameter ay = new ScalarParameter(0);
  protected ScalarParameter az = new ScalarParameter(0);
  protected ScalarParameter aw = new ScalarParameter(0);
  protected ScalarParameter au = new ScalarParameter(0);
  protected ScalarParameter av = new ScalarParameter(0);

  public void setAxisXSource(double source) {
    ax.set(source);
  }

  public void setAxisYSource(double source) {
    ay.set(source);
  }

  public void setAxisZSource(double source) {
    az.set(source);
  }

  public void setAxisWSource(double source) {
    aw.set(source);
  }

  public void setAxisUSource(double source) {
    au.set(source);
  }

  public void setAxisVSource(double source) {
    av.set(source);
  }

  public void setAxisXSource(Module source) {
    ax.set(source);
  }

  public void setAxisYSource(Module source) {
    ay.set(source);
  }

  public void setAxisZSource(Module source) {
    az.set(source);
  }

  public void setAxisWSource(Module source) {
    aw.set(source);
  }

  public void setAxisUSource(Module source) {
    au.set(source);
  }

  public void setAxisVSource(Module source) {
    av.set(source);
  }

  public void setAxisXSource(ScalarParameter scalarParameter) {
    ax.set(scalarParameter);
  }

  public void setAxisYSource(ScalarParameter scalarParameter) {
    ay.set(scalarParameter);
  }

  public void setAxisZSource(ScalarParameter scalarParameter) {
    az.set(scalarParameter);
  }

  public void setAxisWSource(ScalarParameter scalarParameter) {
    aw.set(scalarParameter);
  }

  public void setAxisUSource(ScalarParameter scalarParameter) {
    au.set(scalarParameter);
  }

  public void setAxisVSource(ScalarParameter scalarParameter) {
    av.set(scalarParameter);
  }

  @Override
  public double get(double x, double y) {
    return source.get(x + ax.get(x, y), y + ay.get(x, y));
  }

  @Override
  public double get(double x, double y, double z) {
    return source.get(x + ax.get(x, y, z), y + ay.get(x, y, z),
        z + az.get(x, y, z));
  }

  @Override
  public double get(double x, double y, double z, double w) {
    return source.get(x + ax.get(x, y, z, w), y + ay.get(x, y, z, w),
        z + az.get(x, y, z, w), w + aw.get(x, y, z, w));
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    return source.get(x + ax.get(x, y, z, w, u, v),
        y + ay.get(x, y, z, w, u, v), z + az.get(x, y, z, w, u, v),
        w + aw.get(x, y, z, w, u, v), u + au.get(x, y, z, w, u, v),
        v + av.get(x, y, z, w, u, v));
  }

  @Override
  protected void _writeToMap(ModuleMap map) {

    ModulePropertyMap props = new ModulePropertyMap(this);

    writeScalar("axisX", ax, props, map);
    writeScalar("axisY", ay, props, map);
    writeScalar("axisZ", az, props, map);
    writeScalar("axisW", aw, props, map);
    writeScalar("axisU", au, props, map);
    writeScalar("axisV", av, props, map);
    writeSource(props, map);

    map.put(getId(), props);

  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap props,
      ModuleInstanceMap map) {

    this.setAxisXSource(readScalar("axisX", props, map));
    this.setAxisYSource(readScalar("axisY", props, map));
    this.setAxisZSource(readScalar("axisZ", props, map));
    this.setAxisWSource(readScalar("axisW", props, map));
    this.setAxisUSource(readScalar("axisU", props, map));
    this.setAxisVSource(readScalar("axisV", props, map));
    readSource(props, map);

    return this;
  }

}
