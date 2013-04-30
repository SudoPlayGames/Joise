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

public class ModuleGradient extends Module {

  public static final double DEFAULT_X1 = 0.0;
  public static final double DEFAULT_X2 = 1.0;
  public static final double DEFAULT_Y1 = 0.0;
  public static final double DEFAULT_Y2 = 1.0;
  public static final double DEFAULT_Z1 = 0.0;
  public static final double DEFAULT_Z2 = 0.0;
  public static final double DEFAULT_W1 = 0.0;
  public static final double DEFAULT_W2 = 0.0;
  public static final double DEFAULT_U1 = 0.0;
  public static final double DEFAULT_U2 = 0.0;
  public static final double DEFAULT_V1 = 0.0;
  public static final double DEFAULT_V2 = 0.0;

  protected double gx1, gx2, gy1, gy2, gz1, gz2, gw1, gw2, gu1, gu2, gv1, gv2;
  protected double x, y, z, w, u, v;
  protected double vlen;

  public ModuleGradient() {
    setGradient(DEFAULT_X1, DEFAULT_X2, DEFAULT_Y1, DEFAULT_Y2, DEFAULT_Z1,
        DEFAULT_Z2, DEFAULT_W1, DEFAULT_W2, DEFAULT_U1, DEFAULT_U2, DEFAULT_V1,
        DEFAULT_V2);
  }

  public void setGradient(double x1, double x2, double y1, double y2) {
    setGradient(x1, x2, y1, y2, 0, 0, 0, 0, 0, 0, 0, 0);
  }

  public void setGradient(double x1, double x2, double y1, double y2,
      double z1, double z2) {
    setGradient(x1, x2, y1, y2, z1, z2, 0, 0, 0, 0, 0, 0);
  }

  public void setGradient(double x1, double x2, double y1, double y2,
      double z1, double z2, double w1, double w2) {
    setGradient(x1, x2, y1, y2, z1, z2, w1, w2, 0, 0, 0, 0);
  }

  public void setGradient(double x1, double x2, double y1, double y2,
      double z1, double z2, double w1, double w2, double u1, double u2,
      double v1, double v2) {
    gx1 = x1;
    gx2 = x2;
    gy1 = y1;
    gy2 = y2;
    gz1 = z1;
    gz2 = z2;
    gw1 = w1;
    gw2 = w2;
    gu1 = u1;
    gu2 = u2;
    gv1 = v1;
    gv2 = v2;

    x = x2 - x1;
    y = y2 - y1;
    z = z2 - z1;
    w = w2 - w1;
    u = u2 - u1;
    v = v2 - v1;

    vlen = (x * x + y * y + z * z + w * w + u * u + v * v);
  }

  @Override
  public double get(double x, double y) {
    double dx = x - gx1;
    double dy = y - gy1;
    double dp = dx * this.x + dy * this.y;
    dp /= vlen;
    return dp;
  }

  @Override
  public double get(double x, double y, double z) {
    double dx = x - gx1;
    double dy = y - gy1;
    double dz = z - gz1;
    double dp = dx * this.x + dy * this.y + dz * this.z;
    dp /= vlen;
    return dp;
  }

  @Override
  public double get(double x, double y, double z, double w) {
    double dx = x - gx1;
    double dy = y - gy1;
    double dz = z - gz1;
    double dw = w - gw1;
    double dp = dx * this.x + dy * this.y + dz * this.z + dw * this.w;
    dp /= vlen;
    return dp;
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    double dx = x - gx1;
    double dy = y - gy1;
    double dz = z - gz1;
    double dw = w - gw1;
    double du = u - gu1;
    double dv = v - gv1;
    double dp = dx * this.x + dy * this.y + dz * this.z + dw * this.w + du
        * this.u + dv * this.v;
    dp /= vlen;
    return dp;
  }

  @Override
  protected void _writeToMap(ModuleMap map) {

    ModulePropertyMap props = new ModulePropertyMap(this);

    StringBuffer sb = new StringBuffer();
    sb.append(gx1).append(" ");
    sb.append(gx2).append(" ");
    sb.append(gy1).append(" ");
    sb.append(gy2).append(" ");
    sb.append(gz1).append(" ");
    sb.append(gz2).append(" ");
    sb.append(gw1).append(" ");
    sb.append(gw2).append(" ");
    sb.append(gu1).append(" ");
    sb.append(gu2).append(" ");
    sb.append(gv1).append(" ");
    sb.append(gv2);
    props.put("gradient", sb.toString());

    map.put(getId(), props);

  }

  @Override
  public Module buildFromPropertyMap(ModulePropertyMap props,
      ModuleInstanceMap map) {

    String buf = props.getAsString("gradient");
    String[] arr = buf.split(" ");
    double[] d = new double[12];
    for (int i = 0; i < 12; i++) {
      d[i] = Double.parseDouble(arr[i]);
    }
    setGradient(d[0], d[1], d[2], d[3], d[4], d[5], d[6], d[7], d[8], d[9],
        d[10], d[11]);

    return this;
  }

}
