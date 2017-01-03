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
import com.sudoplay.joise.noise.Util;

public class ModuleSelect extends Module {

  protected ScalarParameter low = new ScalarParameter(0);
  protected ScalarParameter high = new ScalarParameter(0);
  protected ScalarParameter control = new ScalarParameter(0);
  protected ScalarParameter threshold = new ScalarParameter(0);
  protected ScalarParameter falloff = new ScalarParameter(0);

  public void setLowSource(double source) {
    low.set(source);
  }

  public void setHighSource(double source) {
    high.set(source);
  }

  public void setControlSource(double source) {
    control.set(source);
  }

  public void setThreshold(double source) {
    threshold.set(source);
  }

  public void setFalloff(double source) {
    falloff.set(source);
  }

  public void setLowSource(Module source) {
    low.set(source);
  }

  public void setHighSource(Module source) {
    high.set(source);
  }

  public void setControlSource(Module source) {
    control.set(source);
  }

  public void setThreshold(Module source) {
    threshold.set(source);
  }

  public void setFalloff(Module source) {
    falloff.set(source);
  }

  public void setLowSource(ScalarParameter scalarParameter) {
    low.set(scalarParameter);
  }

  public void setHighSource(ScalarParameter scalarParameter) {
    high.set(scalarParameter);
  }

  public void setControlSource(ScalarParameter scalarParameter) {
    control.set(scalarParameter);
  }

  public void setThreshold(ScalarParameter scalarParameter) {
    threshold.set(scalarParameter);
  }

  public void setFalloff(ScalarParameter scalarParameter) {
    falloff.set(scalarParameter);
  }

  @Override
  public double get(double x, double y) {
    double c = control.get(x, y);
    double f = falloff.get(x, y);
    double t = threshold.get(x, y);

    if (f > 0.0) {

      if (c < (t - f)) {
        return low.get(x, y);
      } else if (c > (t + f)) {
        return high.get(x, y);
      } else {
        double lower = t - f;
        double upper = t + f;
        double blend = Util.quinticBlend((c - lower) / (upper - lower));
        return Util.lerp(blend, low.get(x, y), high.get(x, y));
      }

    } else {
      if (c < t) {
        return low.get(x, y);
      } else {
        return high.get(x, y);
      }
    }
  }

  @Override
  public double get(double x, double y, double z) {
    double c = control.get(x, y, z);
    double f = falloff.get(x, y, z);
    double t = threshold.get(x, y, z);

    if (f > 0.0) {

      if (c < (t - f)) {
        return low.get(x, y, z);
      } else if (c > (t + f)) {
        return high.get(x, y, z);
      } else {
        double lower = t - f;
        double upper = t + f;
        double blend = Util.quinticBlend((c - lower) / (upper - lower));
        return Util.lerp(blend, low.get(x, y, z), high.get(x, y, z));
      }

    } else {
      if (c < t) {
        return low.get(x, y, z);
      } else {
        return high.get(x, y, z);
      }
    }
  }

  @Override
  public double get(double x, double y, double z, double w) {
    double c = control.get(x, y, z, w);
    double f = falloff.get(x, y, z, w);
    double t = threshold.get(x, y, z, w);

    if (f > 0.0) {

      if (c < (t - f)) {
        return low.get(x, y, z, w);
      } else if (c > (t + f)) {
        return high.get(x, y, z, w);
      } else {
        double lower = t - f;
        double upper = t + f;
        double blend = Util.quinticBlend((c - lower) / (upper - lower));
        return Util.lerp(blend, low.get(x, y, z, w), high.get(x, y, z, w));
      }

    } else {
      if (c < t) {
        return low.get(x, y, z, w);
      } else {
        return high.get(x, y, z, w);
      }
    }
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    double c = control.get(x, y, z, w, u, v);
    double f = falloff.get(x, y, z, w, u, v);
    double t = threshold.get(x, y, z, w, u, v);

    if (f > 0.0) {

      if (c < (t - f)) {
        return low.get(x, y, z, w, u, v);
      } else if (c > (t + f)) {
        return high.get(x, y, z, w, u, v);
      } else {
        double lower = t - f;
        double upper = t + f;
        double blend = Util.quinticBlend((c - lower) / (upper - lower));
        return Util.lerp(blend, low.get(x, y, z, w, u, v),
            high.get(x, y, z, w, u, v));
      }

    } else {
      if (c < t) {
        return low.get(x, y, z, w, u, v);
      } else {
        return high.get(x, y, z, w, u, v);
      }
    }
  }

  @Override
  protected void _writeToMap(ModuleMap map) {

    ModulePropertyMap props = new ModulePropertyMap(this);

    writeScalar("low", low, props, map);
    writeScalar("high", high, props, map);
    writeScalar("control", control, props, map);
    writeScalar("threshold", threshold, props, map);
    writeScalar("falloff", falloff, props, map);

    map.put(getId(), props);

  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap props,
      ModuleInstanceMap map) {

    this.setLowSource(readScalar("low", props, map));
    this.setHighSource(readScalar("high", props, map));
    this.setControlSource(readScalar("control", props, map));
    this.setThreshold(readScalar("threshold", props, map));
    this.setFalloff(readScalar("falloff", props, map));

    return this;
  }

}
