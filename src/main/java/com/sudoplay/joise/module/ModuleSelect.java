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

public class ModuleSelect extends Module {

  private ScalarParameter low = new ScalarParameter(0);
  private ScalarParameter high = new ScalarParameter(0);
  private ScalarParameter control = new ScalarParameter(0);
  private ScalarParameter threshold = new ScalarParameter(0);
  private ScalarParameter falloff = new ScalarParameter(0);

  public void setLowSource(double source) {
    this.low.set(source);
  }

  public void setHighSource(double source) {
    this.high.set(source);
  }

  public void setControlSource(double source) {
    this.control.set(source);
  }

  public void setThreshold(double source) {
    this.threshold.set(source);
  }

  public void setFalloff(double source) {
    this.falloff.set(source);
  }

  public void setLowSource(Module source) {
    this.low.set(source);
  }

  public void setHighSource(Module source) {
    this.high.set(source);
  }

  public void setControlSource(Module source) {
    this.control.set(source);
  }

  public void setThreshold(Module source) {
    this.threshold.set(source);
  }

  @SuppressWarnings("unused")
  public void setFalloff(Module source) {
    this.falloff.set(source);
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public void setLowSource(ScalarParameter scalarParameter) {
    this.low.set(scalarParameter);
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public void setHighSource(ScalarParameter scalarParameter) {
    this.high.set(scalarParameter);
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public void setControlSource(ScalarParameter scalarParameter) {
    this.control.set(scalarParameter);
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public void setThreshold(ScalarParameter scalarParameter) {
    this.threshold.set(scalarParameter);
  }

  @SuppressWarnings({"unused", "WeakerAccess"})
  public void setFalloff(ScalarParameter scalarParameter) {
    this.falloff.set(scalarParameter);
  }

  @Override
  public double get(double x, double y) {
    double c = this.control.get(x, y);
    double f = this.falloff.get(x, y);
    double t = this.threshold.get(x, y);

    if (f > 0.0) {

      if (c < (t - f)) {
        return this.low.get(x, y);

      } else if (c > (t + f)) {
        return this.high.get(x, y);

      } else {
        double lower = t - f;
        double upper = t + f;
        double blend = Util.quinticBlend((c - lower) / (upper - lower));
        return Util.lerp(blend, this.low.get(x, y), this.high.get(x, y));
      }

    } else {

      if (c < t) {
        return this.low.get(x, y);

      } else {
        return this.high.get(x, y);
      }
    }
  }

  @Override
  public double get(double x, double y, double z) {
    double c = this.control.get(x, y, z);
    double f = this.falloff.get(x, y, z);
    double t = this.threshold.get(x, y, z);

    if (f > 0.0) {

      if (c < (t - f)) {
        return this.low.get(x, y, z);

      } else if (c > (t + f)) {
        return this.high.get(x, y, z);

      } else {
        double lower = t - f;
        double upper = t + f;
        double blend = Util.quinticBlend((c - lower) / (upper - lower));
        return Util.lerp(blend, this.low.get(x, y, z), this.high.get(x, y, z));
      }

    } else {

      if (c < t) {
        return this.low.get(x, y, z);

      } else {
        return this.high.get(x, y, z);
      }
    }
  }

  @Override
  public double get(double x, double y, double z, double w) {
    double c = this.control.get(x, y, z, w);
    double f = this.falloff.get(x, y, z, w);
    double t = this.threshold.get(x, y, z, w);

    if (f > 0.0) {

      if (c < (t - f)) {
        return this.low.get(x, y, z, w);

      } else if (c > (t + f)) {
        return this.high.get(x, y, z, w);

      } else {
        double lower = t - f;
        double upper = t + f;
        double blend = Util.quinticBlend((c - lower) / (upper - lower));
        return Util.lerp(blend, this.low.get(x, y, z, w), this.high.get(x, y, z, w));
      }

    } else {

      if (c < t) {
        return this.low.get(x, y, z, w);

      } else {
        return this.high.get(x, y, z, w);
      }
    }
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    double c = this.control.get(x, y, z, w, u, v);
    double f = this.falloff.get(x, y, z, w, u, v);
    double t = this.threshold.get(x, y, z, w, u, v);

    if (f > 0.0) {

      if (c < (t - f)) {
        return this.low.get(x, y, z, w, u, v);

      } else if (c > (t + f)) {
        return this.high.get(x, y, z, w, u, v);

      } else {
        double lower = t - f;
        double upper = t + f;
        double blend = Util.quinticBlend((c - lower) / (upper - lower));
        return Util.lerp(blend, this.low.get(x, y, z, w, u, v), this.high.get(x, y, z, w, u, v));
      }

    } else {

      if (c < t) {
        return this.low.get(x, y, z, w, u, v);

      } else {
        return this.high.get(x, y, z, w, u, v);
      }
    }
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    modulePropertyMap
        .writeScalar("low", this.low, moduleMap)
        .writeScalar("high", this.high, moduleMap)
        .writeScalar("control", this.control, moduleMap)
        .writeScalar("threshold", this.threshold, moduleMap)
        .writeScalar("falloff", this.falloff, moduleMap);
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap modulePropertyMap, ModuleInstanceMap moduleInstanceMap) {
    this.setLowSource(modulePropertyMap.readScalar("low", moduleInstanceMap));
    this.setHighSource(modulePropertyMap.readScalar("high", moduleInstanceMap));
    this.setControlSource(modulePropertyMap.readScalar("control", moduleInstanceMap));
    this.setThreshold(modulePropertyMap.readScalar("threshold", moduleInstanceMap));
    this.setFalloff(modulePropertyMap.readScalar("falloff", moduleInstanceMap));
    return this;
  }
}
