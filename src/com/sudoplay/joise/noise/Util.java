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

package com.sudoplay.joise.noise;

public class Util {

  public static final double TWO_PI = Math.PI * 2.0;

  private static final double INV_LOG_HALF = 1.0 / Math.log(0.5);

  private Util() {}

  public static double clamp(double input, double min, double max) {
    return (input < min) ? min : (input > max) ? max : input;
  }

  public static double bias(double b, double t) {
    return Math.pow(t, Math.log(b) * INV_LOG_HALF);
  }

  public static double lerp(double t, double a, double b) {
    return a + t * (b - a);
  }

  public static double gain(double g, double t) {
    g = clamp(g, 0.0, 1.0);
    if (t < 0.5) {
      return bias(1.0 - g, 2.0 * t) * 0.5;
    } else {
      return 1.0 - bias(1.0 - g, 2.0 - 2.0 * t) * 0.5;
    }
  }

  public static double quinticBlend(double t) {
    return t * t * t * (t * (t * 6 - 15) + 10);
  }

  public static double sawtooth(double x, double p) {
    return (2.0 * (x / p - Math.floor(0.5 + x / p))) * 0.5 + 0.5;
  }

  public static class Vector3d {
    public double x, y, z;

    public Vector3d() {
      this(0, 0, 0);
    }

    public Vector3d(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }
  }
}
