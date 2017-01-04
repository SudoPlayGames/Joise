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
import com.sudoplay.joise.noise.Noise;
import com.sudoplay.joise.noise.worker.WorkerNoise2Value;
import com.sudoplay.joise.noise.worker.WorkerNoise3Value;
import com.sudoplay.joise.noise.worker.WorkerNoise4Value;
import com.sudoplay.joise.noise.worker.WorkerNoise6Value;
import com.sudoplay.joise.noise.worker.spi.WorkerNoise2;
import com.sudoplay.joise.noise.worker.spi.WorkerNoise3;
import com.sudoplay.joise.noise.worker.spi.WorkerNoise4;
import com.sudoplay.joise.noise.worker.spi.WorkerNoise6;

public class ModuleCellGen extends SeedableModule {

  // TODO: Fix this
  private int id = Module.nextId;//.incrementAndGet();

  private WorkerNoise2 workerNoise2;
  private WorkerNoise3 workerNoise3;
  private WorkerNoise4 workerNoise4;
  private WorkerNoise6 workerNoise6;

  public class CellularCache {
    double[] f = new double[4];
    double[] d = new double[4];
    double x, y, z, w, u, v;
    boolean valid = false;
  }

  protected CellularCache c2 = new CellularCache();
  protected CellularCache c3 = new CellularCache();
  protected CellularCache c4 = new CellularCache();
  protected CellularCache c6 = new CellularCache();

  public ModuleCellGen() {
    this.workerNoise2 = new WorkerNoise2Value();
    this.workerNoise3 = new WorkerNoise3Value();
    this.workerNoise4 = new WorkerNoise4Value();
    this.workerNoise6 = new WorkerNoise6Value();
  }

  @Override
  public String getId() {
    return "func_" + id;
  }

  @Override
  public void setSeed(long seed) {
    super.setSeed(seed);

    c2.valid = false;
    c3.valid = false;
    c4.valid = false;
    c6.valid = false;
  }

  public CellularCache getCache(double x, double y) {
    if (!c2.valid || c2.x != x || c2.y != y) {
      this.cellularFunction2D(x, y, seed, c2.f, c2.d);
      c2.x = x;
      c2.y = y;
      c2.valid = true;
    }
    return c2;
  }

  public CellularCache getCache(double x, double y, double z) {
    if (!c3.valid || c3.x != x || c3.y != y || c3.z != z) {
      this.cellularFunction3D(x, y, z, seed, c3.f, c3.d);
      c3.x = x;
      c3.y = y;
      c3.z = z;
      c3.valid = true;
    }
    return c3;
  }

  public CellularCache getCache(double x, double y, double z, double w) {
    if (!c4.valid || c4.x != x || c4.y != y || c4.z != z || c4.w != w) {
      this.cellularFunction4D(x, y, z, w, seed, c4.f, c4.d);
      c4.x = x;
      c4.y = y;
      c4.z = z;
      c4.w = w;
      c4.valid = true;
    }
    return c4;
  }

  public CellularCache getCache(double x, double y, double z, double w,
                                double u, double v) {
    if (!c6.valid || c6.x != x || c6.y != y || c6.z != z || c6.w != w
        || c6.u != u || c6.v != v) {
      this.cellularFunction6D(x, y, z, w, u, v, seed, c6.f, c6.d);
      c6.x = x;
      c6.y = y;
      c6.z = z;
      c6.w = w;
      c6.u = u;
      c6.v = v;
      c6.valid = true;
    }
    return c6;
  }

  @Override
  protected void _writeToMap(ModuleMap map) {

    ModulePropertyMap props = new ModulePropertyMap(this);

    writeSeed(props);

    map.put(getId(), props);

  }

  @Override
  public ModuleCellGen buildFromPropertyMap(ModulePropertyMap props,
                                            ModuleInstanceMap map) {

    readSeed(props);

    return this;
  }

  @Override
  public double get(double x, double y) {
    throw new UnsupportedOperationException();
  }

  @Override
  public double get(double x, double y, double z) {
    throw new UnsupportedOperationException();
  }

  @Override
  public double get(double x, double y, double z, double w) {
    throw new UnsupportedOperationException();
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    throw new UnsupportedOperationException();
  }

  protected void cellularFunction2D(double x, double y, long seed,
                                    double[] f, double[] disp) {
    int xint = Noise.fastFloor(x);
    int yint = Noise.fastFloor(y);

    for (int c = 0; c < 4; ++c) {
      f[c] = 99999.0;
      disp[c] = 0.0;
    }

    for (int ycur = yint - 3; ycur <= yint + 3; ++ycur) {
      for (int xcur = xint - 3; xcur <= xint + 3; ++xcur) {

        double xpos = (double) xcur + this.workerNoise2.calculate(x, y, xcur, ycur, seed);
        double ypos = (double) ycur + this.workerNoise2.calculate(x, y, xcur, ycur, seed + 1);
        double xdist = xpos - x;
        double ydist = ypos - y;
        double dist = (xdist * xdist + ydist * ydist);
        int xval = Noise.fastFloor(xpos);
        int yval = Noise.fastFloor(ypos);
        double dsp = this.workerNoise2.calculate(x, y, xval, yval, seed + 3);
        Noise.addDist(f, disp, dist, dsp);
      }
    }

  }

  protected void cellularFunction3D(double x, double y, double z,
                                    long seed, double[] f, double[] disp) {
    int xint = Noise.fastFloor(x);
    int yint = Noise.fastFloor(y);
    int zint = Noise.fastFloor(z);

    for (int c = 0; c < 4; ++c) {
      f[c] = 99999.0;
      disp[c] = 0.0;
    }

    for (int zcur = zint - 2; zcur <= zint + 2; ++zcur) {
      for (int ycur = yint - 2; ycur <= yint + 2; ++ycur) {
        for (int xcur = xint - 2; xcur <= xint + 2; ++xcur) {
          double xpos = (double) xcur + this.workerNoise3.calculate(x, y, z, xcur, ycur, zcur, seed);
          double ypos = (double) ycur + this.workerNoise3.calculate(x, y, z, xcur, ycur, zcur, seed + 1);
          double zpos = (double) zcur + this.workerNoise3.calculate(x, y, z, xcur, ycur, zcur, seed + 2);
          double xdist = xpos - x;
          double ydist = ypos - y;
          double zdist = zpos - z;
          double dist = (xdist * xdist + ydist * ydist + zdist * zdist);
          int xval = Noise.fastFloor(xpos);
          int yval = Noise.fastFloor(ypos);
          int zval = Noise.fastFloor(zpos);
          double dsp = this.workerNoise3.calculate(x, y, z, xval, yval, zval, seed + 3);
          Noise.addDist(f, disp, dist, dsp);
        }
      }
    }
  }

  protected void cellularFunction4D(double x, double y, double z, double w,
                                    long seed, double[] f, double[] disp) {
    int xint = Noise.fastFloor(x);
    int yint = Noise.fastFloor(y);
    int zint = Noise.fastFloor(z);
    int wint = Noise.fastFloor(w);

    for (int c = 0; c < 4; ++c) {
      f[c] = 99999.0;
      disp[c] = 0.0;
    }

    for (int wcur = wint - 2; wcur <= wint + 2; ++wcur) {
      for (int zcur = zint - 2; zcur <= zint + 2; ++zcur) {
        for (int ycur = yint - 2; ycur <= yint + 2; ++ycur) {
          for (int xcur = xint - 2; xcur <= xint + 2; ++xcur) {
            double xpos = (double) xcur + this.workerNoise4.calculate(x, y, z, w, xcur, ycur, zcur, wcur, seed);
            double ypos = (double) ycur + this.workerNoise4.calculate(x, y, z, w, xcur, ycur, zcur, wcur, seed + 1);
            double zpos = (double) zcur + this.workerNoise4.calculate(x, y, z, w, xcur, ycur, zcur, wcur, seed + 2);
            double wpos = (double) wcur + this.workerNoise4.calculate(x, y, z, w, xcur, ycur, zcur, wcur, seed + 3);
            double xdist = xpos - x;
            double ydist = ypos - y;
            double zdist = zpos - z;
            double wdist = wpos - w;
            double dist = (xdist * xdist + ydist * ydist + zdist * zdist + wdist * wdist);
            int xval = Noise.fastFloor(xpos);
            int yval = Noise.fastFloor(ypos);
            int zval = Noise.fastFloor(zpos);
            int wval = Noise.fastFloor(wpos);
            double dsp = this.workerNoise4.calculate(x, y, z, w, xval, yval, zval, wval, seed + 3);
            Noise.addDist(f, disp, dist, dsp);
          }
        }
      }
    }
  }

  protected void cellularFunction6D(double x, double y, double z, double w,
                                    double u, double v, long seed, double[] f, double[] disp) {
    int xint = Noise.fastFloor(x);
    int yint = Noise.fastFloor(y);
    int zint = Noise.fastFloor(z);
    int wint = Noise.fastFloor(w);
    int uint = Noise.fastFloor(u);
    int vint = Noise.fastFloor(v);

    for (int c = 0; c < 4; ++c) {
      f[c] = 99999.0;
      disp[c] = 0.0;
    }

    for (int vcur = vint - 1; vcur <= vint + 1; ++vcur) {
      for (int ucur = uint - 1; ucur <= uint + 1; ++ucur) {
        for (int wcur = wint - 2; wcur <= wint + 2; ++wcur) {
          for (int zcur = zint - 2; zcur <= zint + 2; ++zcur) {
            for (int ycur = yint - 2; ycur <= yint + 2; ++ycur) {
              for (int xcur = xint - 2; xcur <= xint + 2; ++xcur) {
                double xpos = (double) xcur + this.workerNoise6.calculate(x, y, z, w, u, v, xcur, ycur, zcur, wcur, ucur, vcur, seed);
                double ypos = (double) ycur + this.workerNoise6.calculate(x, y, z, w, u, v, xcur, ycur, zcur, wcur, ucur, vcur, seed + 1);
                double zpos = (double) zcur + this.workerNoise6.calculate(x, y, z, w, u, v, xcur, ycur, zcur, wcur, ucur, vcur, seed + 2);
                double wpos = (double) wcur + this.workerNoise6.calculate(x, y, z, w, u, v, xcur, ycur, zcur, wcur, ucur, vcur, seed + 3);
                double upos = (double) ucur + this.workerNoise6.calculate(x, y, z, w, u, v, xcur, ycur, zcur, wcur, ucur, vcur, seed + 4);
                double vpos = (double) vcur + this.workerNoise6.calculate(x, y, z, w, u, v, xcur, ycur, zcur, wcur, ucur, vcur, seed + 5);
                double xdist = xpos - x;
                double ydist = ypos - y;
                double zdist = zpos - z;
                double wdist = wpos - w;
                double udist = upos - u;
                double vdist = vpos - v;
                double dist = (xdist * xdist + ydist * ydist + zdist * zdist + wdist * wdist + udist * udist + vdist * vdist);
                int xval = Noise.fastFloor(xpos);
                int yval = Noise.fastFloor(ypos);
                int zval = Noise.fastFloor(zpos);
                int wval = Noise.fastFloor(wpos);
                int uval = Noise.fastFloor(upos);
                int vval = Noise.fastFloor(vpos);
                double dsp = this.workerNoise6.calculate(x, y, z, w, u, v, xval, yval, zval, wval, uval, vval, seed + 6);
                Noise.addDist(f, disp, dist, dsp);
              }
            }
          }
        }
      }
    }
  }

}
