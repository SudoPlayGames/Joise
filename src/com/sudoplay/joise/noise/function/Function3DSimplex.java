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

package com.sudoplay.joise.noise.function;

import com.sudoplay.joise.noise.Interpolator;
import com.sudoplay.joise.noise.Noise;
import com.sudoplay.joise.noise.NoiseLUT;
import com.sudoplay.joise.noise.function.spi.Function3D;
import com.sudoplay.util.Bits;

public class Function3DSimplex implements
    Function3D {

  private byte[] buffer;

  public Function3DSimplex() {
    this.buffer = new byte[20];
  }

  @Override
  public double get(double x, double y, double z, long seed, Interpolator interpolator) {
    double n0, n1, n2, n3;

    double s = (x + y + z) * Noise.F3;
    int i = Noise.fastFloor(x + s);
    int j = Noise.fastFloor(y + s);
    int k = Noise.fastFloor(z + s);

    double t = (i + j + k) * Noise.G3;
    double X0 = i - t;
    double Y0 = j - t;
    double Z0 = k - t;

    double x0 = x - X0;
    double y0 = y - Y0;
    double z0 = z - Z0;

    int i1, j1, k1;
    int i2, j2, k2;

    if (x0 >= y0) {
      if (y0 >= z0) {
        i1 = 1;
        j1 = 0;
        k1 = 0;
        i2 = 1;
        j2 = 1;
        k2 = 0;
      } else if (x0 >= z0) {
        i1 = 1;
        j1 = 0;
        k1 = 0;
        i2 = 1;
        j2 = 0;
        k2 = 1;
      } else {
        i1 = 0;
        j1 = 0;
        k1 = 1;
        i2 = 1;
        j2 = 0;
        k2 = 1;
      }
    } else {
      if (y0 < z0) {
        i1 = 0;
        j1 = 0;
        k1 = 1;
        i2 = 0;
        j2 = 1;
        k2 = 1;
      } else if (x0 < z0) {
        i1 = 0;
        j1 = 1;
        k1 = 0;
        i2 = 0;
        j2 = 1;
        k2 = 1;
      } else {
        i1 = 0;
        j1 = 1;
        k1 = 0;
        i2 = 1;
        j2 = 1;
        k2 = 0;
      }
    }

    double x1 = x0 - i1 + Noise.G3;
    double y1 = y0 - j1 + Noise.G3;
    double z1 = z0 - k1 + Noise.G3;
    double x2 = x0 - i2 + 2.0 * Noise.G3;
    double y2 = y0 - j2 + 2.0 * Noise.G3;
    double z2 = z0 - k2 + 2.0 * Noise.G3;
    double x3 = x0 - 1.0 + 3.0 * Noise.G3;
    double y3 = y0 - 1.0 + 3.0 * Noise.G3;
    double z3 = z0 - 1.0 + 3.0 * Noise.G3;

    int h0, h1, h2, h3;

    h0 = hash(i, j, k, seed);
    h1 = hash(i + i1, j + j1, k + k1, seed);
    h2 = hash(i + i2, j + j2, k + k2, seed);
    h3 = hash(i + 1, j + 1, k + 1, seed);

    double[] g0 = NoiseLUT.gradient3DLUT[h0];
    double[] g1 = NoiseLUT.gradient3DLUT[h1];
    double[] g2 = NoiseLUT.gradient3DLUT[h2];
    double[] g3 = NoiseLUT.gradient3DLUT[h3];

    double t0 = 0.6 - x0 * x0 - y0 * y0 - z0 * z0;
    if (t0 < 0.0)
      n0 = 0.0;
    else {
      t0 *= t0;
      n0 = t0 * t0 * Noise.arrayDot3(g0, x0, y0, z0);
    }

    double t1 = 0.6 - x1 * x1 - y1 * y1 - z1 * z1;
    if (t1 < 0.0)
      n1 = 0.0;
    else {
      t1 *= t1;
      n1 = t1 * t1 * Noise.arrayDot3(g1, x1, y1, z1);
    }

    double t2 = 0.6 - x2 * x2 - y2 * y2 - z2 * z2;
    if (t2 < 0)
      n2 = 0.0;
    else {
      t2 *= t2;
      n2 = t2 * t2 * Noise.arrayDot3(g2, x2, y2, z2);
    }

    double t3 = 0.6 - x3 * x3 - y3 * y3 - z3 * z3;
    if (t3 < 0)
      n3 = 0.0;
    else {
      t3 *= t3;
      n3 = t3 * t3 * Noise.arrayDot3(g3, x3, y3, z3);
    }

    return (32.0 * (n0 + n1 + n2 + n3)) * 1.25086885 + 0.0003194984;
  }

  private int hash(int x, int y, int z, long seed) {
    Bits.intToByteArray(x, this.buffer, 0);
    Bits.intToByteArray(y, this.buffer, 4);
    Bits.intToByteArray(z, this.buffer, 8);
    Bits.longToByteArray(seed, this.buffer, 12);
    return Noise.xorFoldHash(Noise.fnv32ABuf(this.buffer));
  }
}
