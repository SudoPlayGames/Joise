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
import com.sudoplay.joise.noise.Util;

public class ModuleTriangle extends
    SourcedModule {

  private static final double DEFAULT_PERIOD = 0;
  private static final double DEFAULT_OFFSET = 0;

  private ScalarParameter period = new ScalarParameter(DEFAULT_PERIOD);
  private ScalarParameter offset = new ScalarParameter(DEFAULT_OFFSET);

  public void setPeriod(double source) {
    this.period.set(source);
  }

  public void setOffset(double source) {
    this.offset.set(source);
  }

  @SuppressWarnings("unused")
  public void setPeriod(Module source) {
    this.period.set(source);
  }

  @SuppressWarnings("unused")
  public void setOffset(Module source) {
    this.offset.set(source);
  }

  @SuppressWarnings("unused")
  public void setPeriod(ScalarParameter scalarParameter) {
    this.period.set(scalarParameter);
  }

  @SuppressWarnings("unused")
  public void setOffset(ScalarParameter scalarParameter) {
    this.offset.set(scalarParameter);
  }

  @Override
  public double get(double x, double y) {
    double val = this.source.get(x, y);
    double p = this.period.get(x, y);
    double o = this.offset.get(x, y);

    if (o >= 1) {
      return Util.sawtooth(val, p);

    } else if (o <= 0) {
      return 1.0 - Util.sawtooth(val, p);

    } else {
      double s1 = (o - Util.sawtooth(val, p)) >= 0 ? 1.0 : 0.0;
      double s2 = ((1.0 - o) - (Util.sawtooth(-val, p))) >= 0 ? 1.0 : 0.0;
      return Util.sawtooth(val, p) * s1 / o + Util.sawtooth(-val, p) * s2 / (1.0 - o);
    }
  }

  @Override
  public double get(double x, double y, double z) {
    double val = this.source.get(x, y, z);
    double p = this.period.get(x, y, z);
    double o = this.offset.get(x, y, z);

    if (o >= 1) {
      return Util.sawtooth(val, p);

    } else if (o <= 0) {
      return 1.0 - Util.sawtooth(val, p);

    } else {
      double s1 = (o - Util.sawtooth(val, p)) >= 0 ? 1.0 : 0.0;
      double s2 = ((1.0 - o) - (Util.sawtooth(-val, p))) >= 0 ? 1.0 : 0.0;
      return Util.sawtooth(val, p) * s1 / o + Util.sawtooth(-val, p) * s2 / (1.0 - o);
    }
  }

  @Override
  public double get(double x, double y, double z, double w) {
    double val = this.source.get(x, y, z, w);
    double p = this.period.get(x, y, z, w);
    double o = this.offset.get(x, y, z, w);

    if (o >= 1) {
      return Util.sawtooth(val, p);

    } else if (o <= 0) {
      return 1.0 - Util.sawtooth(val, p);

    } else {
      double s1 = (o - Util.sawtooth(val, p)) >= 0 ? 1.0 : 0.0;
      double s2 = ((1.0 - o) - (Util.sawtooth(-val, p))) >= 0 ? 1.0 : 0.0;
      return Util.sawtooth(val, p) * s1 / o + Util.sawtooth(-val, p) * s2 / (1.0 - o);
    }
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    double val = this.source.get(x, y, z, w, u, v);
    double p = this.period.get(x, y, z, w, u, v);
    double o = this.offset.get(x, y, z, w, u, v);

    if (o >= 1) {
      return Util.sawtooth(val, p);

    } else if (o <= 0) {
      return 1.0 - Util.sawtooth(val, p);

    } else {
      double s1 = (o - Util.sawtooth(val, p)) >= 0 ? 1.0 : 0.0;
      double s2 = ((1.0 - o) - (Util.sawtooth(-val, p))) >= 0 ? 1.0 : 0.0;
      return Util.sawtooth(val, p) * s1 / o + Util.sawtooth(-val, p) * s2 / (1.0 - o);
    }
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    modulePropertyMap
        .writeScalar("offset", this.offset, moduleMap)
        .writeScalar("period", this.period, moduleMap)
        .writeScalar("source", this.source, moduleMap);
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap modulePropertyMap, ModuleInstanceMap moduleInstanceMap) {
    this.setOffset(modulePropertyMap.readScalar("offset", moduleInstanceMap));
    this.setPeriod(modulePropertyMap.readScalar("period", moduleInstanceMap));
    this.setSource(modulePropertyMap.readScalar("source", moduleInstanceMap));
    return this;
  }
}
