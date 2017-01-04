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
import com.sudoplay.joise.noise.function.spi.Function2D;
import com.sudoplay.util.Bits;

public class Function2DSimplex implements
    Function2D {

  private byte[] buffer;

  public Function2DSimplex() {
    this.buffer = new byte[16];
  }

  @Override
  public double get(double x, double y, long seed, Interpolator interpolator) {
    double s = (x + y) * Noise.F2;
    int i = Noise.fastFloor(x + s);
    int j = Noise.fastFloor(y + s);
    double t = (i + j) * Noise.G2;
    double X0 = i - t;
    double Y0 = j - t;
    double x0 = x - X0;
    double y0 = y - Y0;
    int i1, j1;
    if (x0 > y0) {
      i1 = 1;
      j1 = 0;
    } else {
      i1 = 0;
      j1 = 1;
    }
    double x1 = x0 - (double) i1 + Noise.G2;
    double y1 = y0 - (double) j1 + Noise.G2;
    double x2 = x0 - 1.0 + 2.0 * Noise.G2;
    double y2 = y0 - 1.0 + 2.0 * Noise.G2;
    int h0 = hash(i, j, seed);
    int h1 = hash(i + i1, j + j1, seed);
    int h2 = hash(i + 1, j + 1, seed);
    double[] g0 = NoiseLUT.gradient2DLUT[h0];
    double[] g1 = NoiseLUT.gradient2DLUT[h1];
    double[] g2 = NoiseLUT.gradient2DLUT[h2];
    double n0, n1, n2;
    double t0 = 0.5 - x0 * x0 - y0 * y0;
    if (t0 < 0)
      n0 = 0;
    else {
      t0 *= t0;
      n0 = t0 * t0 * Noise.arrayDot2(g0, x0, y0);
    }
    double t1 = 0.5 - x1 * x1 - y1 * y1;
    if (t1 < 0)
      n1 = 0;
    else {
      t1 *= t1;
      n1 = t1 * t1 * Noise.arrayDot2(g1, x1, y1);
    }
    double t2 = 0.5 - x2 * x2 - y2 * y2;
    if (t2 < 0)
      n2 = 0;
    else {
      t2 *= t2;
      n2 = t2 * t2 * Noise.arrayDot2(g2, x2, y2);
    }
    return (70.0 * (n0 + n1 + n2)) * 1.42188695 + 0.001054489;
  }

  private int hash(int x, int y, long seed) {
    Bits.intToByteArray(x, this.buffer, 0);
    Bits.intToByteArray(y, this.buffer, 4);
    Bits.longToByteArray(seed, this.buffer, 8);
    return Noise.xorFoldHash(Noise.fnv32ABuf(this.buffer));
  }
}
