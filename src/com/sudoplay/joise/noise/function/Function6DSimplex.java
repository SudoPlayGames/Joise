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
import com.sudoplay.joise.noise.function.spi.Function6D;
import com.sudoplay.util.Bits;

public class Function6DSimplex implements
    Function6D {

  private byte[] buffer;

  public Function6DSimplex() {
    this.buffer = new byte[32];
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v, long seed, Interpolator interpolator) {
    double F4 = (Math.sqrt(7.0) - 1.0) / 6.0;
    double G4 = F4 / (1.0 + 6.0 * F4);

    double sideLength = Math.sqrt(6.0) / (6.0 * F4 + 1.0);
    double a = Math.sqrt((sideLength * sideLength)
        - ((sideLength / 2.0) * (sideLength / 2.0)));
    double cornerFace = Math.sqrt(a * a + (a / 2.0) * (a / 2.0));

    double cornerFaceSqrd = cornerFace * cornerFace;

    double valueScaler = Math.pow(5.0, -0.5);
    valueScaler *= Math.pow(5.0, -3.5) * 100 + 13;

    double[] loc = {x, y, z, w, u, v};
    double s = 0;
    for (int c = 0; c < 6; ++c)
      s += loc[c];
    s *= F4;

    int[] skewLoc = {Noise.fastFloor(x + s), Noise.fastFloor(y + s), Noise.fastFloor(z + s),
        Noise.fastFloor(w + s), Noise.fastFloor(u + s), Noise.fastFloor(v + s)};
    int[] intLoc = {Noise.fastFloor(x + s), Noise.fastFloor(y + s), Noise.fastFloor(z + s),
        Noise.fastFloor(w + s), Noise.fastFloor(u + s), Noise.fastFloor(v + s)};
    double unskew = 0.0;
    for (int c = 0; c < 6; ++c)
      unskew += skewLoc[c];
    unskew *= G4;
    double[] cellDist = {loc[0] - (double) skewLoc[0] + unskew,
        loc[1] - (double) skewLoc[1] + unskew,
        loc[2] - (double) skewLoc[2] + unskew,
        loc[3] - (double) skewLoc[3] + unskew,
        loc[4] - (double) skewLoc[4] + unskew,
        loc[5] - (double) skewLoc[5] + unskew};
    int[] distOrder = {0, 1, 2, 3, 4, 5};
    Noise.sortBy6(cellDist, distOrder);

    int[] newDistOrder = {-1, distOrder[0], distOrder[1], distOrder[2],
        distOrder[3], distOrder[4], distOrder[5]};

    double n = 0.0;
    double skewOffset = 0.0;

    for (int c = 0; c < 7; ++c) {
      int i = newDistOrder[c];
      if (i != -1) intLoc[i] += 1;

      double[] m = new double[6];
      for (int d = 0; d < 6; ++d) {
        m[d] = cellDist[d] - (intLoc[d] - skewLoc[d]) + skewOffset;
      }

      double t = cornerFaceSqrd;

      for (int d = 0; d < 6; ++d) {
        t -= m[d] * m[d];
      }

      if (t > 0.0) {
        int h = hash(intLoc[0], intLoc[1], intLoc[2], intLoc[3],
            intLoc[4], intLoc[5], seed);
        double[] vec = NoiseLUT.gradient6DLUT[h];
        double gr = 0.0;
        for (int d = 0; d < 6; ++d) {
          gr += vec[d] * m[d];
        }

        n += gr * t * t * t * t;
      }
      skewOffset += G4;
    }
    n *= valueScaler;
    return n;
  }

  private int hash(int x, int y, int z, int w, int u, int v, long seed) {
    Bits.intToByteArray(x, this.buffer, 0);
    Bits.intToByteArray(y, this.buffer, 4);
    Bits.intToByteArray(z, this.buffer, 8);
    Bits.intToByteArray(w, this.buffer, 12);
    Bits.intToByteArray(u, this.buffer, 16);
    Bits.intToByteArray(v, this.buffer, 20);
    Bits.longToByteArray(seed, this.buffer, 24);
    return Noise.xorFoldHash(Noise.fnv32ABuf(this.buffer));
  }
}
