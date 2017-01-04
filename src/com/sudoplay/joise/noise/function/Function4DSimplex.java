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
import com.sudoplay.joise.noise.function.spi.Function4D;
import com.sudoplay.util.Bits;

public class Function4DSimplex implements
    Function4D {

  private byte[] buffer;

  private static final int[][] SIMPLEX = new int[][]{{0, 1, 2, 3}, {0, 1, 3, 2},
      {0, 0, 0, 0}, {0, 2, 3, 1}, {0, 0, 0, 0}, {0, 0, 0, 0},
      {0, 0, 0, 0}, {1, 2, 3, 0}, {0, 2, 1, 3}, {0, 0, 0, 0},
      {0, 3, 1, 2}, {0, 3, 2, 1}, {0, 0, 0, 0}, {0, 0, 0, 0},
      {0, 0, 0, 0}, {1, 3, 2, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
      {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
      {0, 0, 0, 0}, {0, 0, 0, 0}, {1, 2, 0, 3}, {0, 0, 0, 0},
      {1, 3, 0, 2}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
      {2, 3, 0, 1}, {2, 3, 1, 0}, {1, 0, 2, 3}, {1, 0, 3, 2},
      {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {2, 0, 3, 1},
      {0, 0, 0, 0}, {2, 1, 3, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
      {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0},
      {0, 0, 0, 0}, {0, 0, 0, 0}, {2, 0, 1, 3}, {0, 0, 0, 0},
      {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0, 1, 2}, {3, 0, 2, 1},
      {0, 0, 0, 0}, {3, 1, 2, 0}, {2, 1, 0, 3}, {0, 0, 0, 0},
      {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 1, 0, 2}, {0, 0, 0, 0},
      {3, 2, 0, 1}, {3, 2, 1, 0}};

  public Function4DSimplex() {
    this.buffer = new byte[24];
  }


  @Override
  public double get(double x, double y, double z, double w, long seed, Interpolator interpolator) {
    double F4 = (Math.sqrt(5.0) - 1.0) / 4.0;
    double G4 = (5.0 - Math.sqrt(5.0)) / 20.0;
    double n0, n1, n2, n3, n4;
    double s = (x + y + z + w) * F4;
    int i = Noise.fastFloor(x + s);
    int j = Noise.fastFloor(y + s);
    int k = Noise.fastFloor(z + s);
    int l = Noise.fastFloor(w + s);
    double t = (i + j + k + l) * G4;
    double X0 = i - t;
    double Y0 = j - t;
    double Z0 = k - t;
    double W0 = l - t;
    double x0 = x - X0;
    double y0 = y - Y0;
    double z0 = z - Z0;
    double w0 = w - W0;
    int c1 = (x0 > y0) ? 32 : 0;
    int c2 = (x0 > z0) ? 16 : 0;
    int c3 = (y0 > z0) ? 8 : 0;
    int c4 = (x0 > w0) ? 4 : 0;
    int c5 = (y0 > w0) ? 2 : 0;
    int c6 = (z0 > w0) ? 1 : 0;
    int c = c1 + c2 + c3 + c4 + c5 + c6;
    int i1, j1, k1, l1;
    int i2, j2, k2, l2;
    int i3, j3, k3, l3;
    i1 = SIMPLEX[c][0] >= 3 ? 1 : 0;
    j1 = SIMPLEX[c][1] >= 3 ? 1 : 0;
    k1 = SIMPLEX[c][2] >= 3 ? 1 : 0;
    l1 = SIMPLEX[c][3] >= 3 ? 1 : 0;
    i2 = SIMPLEX[c][0] >= 2 ? 1 : 0;
    j2 = SIMPLEX[c][1] >= 2 ? 1 : 0;
    k2 = SIMPLEX[c][2] >= 2 ? 1 : 0;
    l2 = SIMPLEX[c][3] >= 2 ? 1 : 0;
    i3 = SIMPLEX[c][0] >= 1 ? 1 : 0;
    j3 = SIMPLEX[c][1] >= 1 ? 1 : 0;
    k3 = SIMPLEX[c][2] >= 1 ? 1 : 0;
    l3 = SIMPLEX[c][3] >= 1 ? 1 : 0;
    double x1 = x0 - i1 + G4;
    double y1 = y0 - j1 + G4;
    double z1 = z0 - k1 + G4;
    double w1 = w0 - l1 + G4;
    double x2 = x0 - i2 + 2.0 * G4;
    double y2 = y0 - j2 + 2.0 * G4;
    double z2 = z0 - k2 + 2.0 * G4;
    double w2 = w0 - l2 + 2.0 * G4;
    double x3 = x0 - i3 + 3.0 * G4;
    double y3 = y0 - j3 + 3.0 * G4;
    double z3 = z0 - k3 + 3.0 * G4;
    double w3 = w0 - l3 + 3.0 * G4;
    double x4 = x0 - 1.0 + 4.0 * G4;
    double y4 = y0 - 1.0 + 4.0 * G4;
    double z4 = z0 - 1.0 + 4.0 * G4;
    double w4 = w0 - 1.0 + 4.0 * G4;
    int h0, h1, h2, h3, h4;
    h0 = hash(i, j, k, l, seed);
    h1 = hash(i + i1, j + j1, k + k1, l + l1, seed);
    h2 = hash(i + i2, j + j2, k + k2, l + l2, seed);
    h3 = hash(i + i3, j + j3, k + k3, l + l3, seed);
    h4 = hash(i + 1, j + 1, k + 1, l + 1, seed);
    double[] g0 = NoiseLUT.gradient4DLUT[h0];
    double[] g1 = NoiseLUT.gradient4DLUT[h1];
    double[] g2 = NoiseLUT.gradient4DLUT[h2];
    double[] g3 = NoiseLUT.gradient4DLUT[h3];
    double[] g4 = NoiseLUT.gradient4DLUT[h4];
    double t0 = 0.6 - x0 * x0 - y0 * y0 - z0 * z0 - w0 * w0;
    if (t0 < 0)
      n0 = 0.0;
    else {
      t0 *= t0;
      n0 = t0 * t0 * Noise.arrayDot4(g0, x0, y0, z0, w0);
    }
    double t1 = 0.6 - x1 * x1 - y1 * y1 - z1 * z1 - w1 * w1;
    if (t1 < 0)
      n1 = 0.0;
    else {
      t1 *= t1;
      n1 = t1 * t1 * Noise.arrayDot4(g1, x1, y1, z1, w1);
    }
    double t2 = 0.6 - x2 * x2 - y2 * y2 - z2 * z2 - w2 * w2;
    if (t2 < 0)
      n2 = 0.0;
    else {
      t2 *= t2;
      n2 = t2 * t2 * Noise.arrayDot4(g2, x2, y2, z2, w2);
    }
    double t3 = 0.6 - x3 * x3 - y3 * y3 - z3 * z3 - w3 * w3;
    if (t3 < 0)
      n3 = 0.0;
    else {
      t3 *= t3;
      n3 = t3 * t3 * Noise.arrayDot4(g3, x3, y3, z3, w3);
    }
    double t4 = 0.6 - x4 * x4 - y4 * y4 - z4 * z4 - w4 * w4;
    if (t4 < 0)
      n4 = 0.0;
    else {
      t4 *= t4;
      n4 = t4 * t4 * Noise.arrayDot4(g4, x4, y4, z4, w4);
    }
    return 27.0 * (n0 + n1 + n2 + n3 + n4);
  }

  private int hash(int x, int y, int z, int w, long seed) {
    Bits.intToByteArray(x, this.buffer, 0);
    Bits.intToByteArray(y, this.buffer, 4);
    Bits.intToByteArray(z, this.buffer, 8);
    Bits.intToByteArray(w, this.buffer, 12);
    Bits.longToByteArray(seed, this.buffer, 16);
    return Noise.xorFoldHash(Noise.fnv32ABuf(this.buffer));
  }
}
