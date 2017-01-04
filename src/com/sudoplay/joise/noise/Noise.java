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

import com.sudoplay.joise.noise.worker.spi.WorkerNoise2;
import com.sudoplay.joise.noise.worker.spi.WorkerNoise3;
import com.sudoplay.joise.noise.worker.spi.WorkerNoise4;
import com.sudoplay.joise.noise.worker.spi.WorkerNoise6;

import java.util.Arrays;

public class Noise {

  private static final long INIT32 = 0x811c9dc5L;
  private static final int PRIME32 = 0x01000193;
  private static final int FNV_MASK_8 = (1 << 8) - 1;

  public static final double INV_BYTEVAL = 1.0 / 255.0;

  public static final double F2 = 0.36602540378443864676372317075294;
  public static final double G2 = 0.21132486540518711774542560974902;
  public static final double F3 = 1.0 / 3.0;
  public static final double G3 = 1.0 / 6.0;

  // ==========================================================================
  // = Common
  // ==========================================================================

  private static double lerp(double s, double v1, double v2) {
    return v1 + s * (v2 - v1);
  }

  public static int fastFloor(double t) {
    return t > 0 ? (int) t : (int) t - 1;
  }

  public static double arrayDot2(double[] arr, double a, double b) {
    return a * arr[0] + b * arr[1];
  }

  public static double arrayDot3(double[] arr, double a, double b, double c) {
    return a * arr[0] + b * arr[1] + c * arr[2];
  }

  public static double arrayDot4(double[] arr, double x, double y, double z,
                                 double w) {
    return x * arr[0] + y * arr[1] + z * arr[2] + w * arr[3];
  }

  public static void addDist(double[] f, double[] disp, double testDist,
                             double testDisp) {
    int index;
    if (testDist < f[3]) {
      index = 3;
      while (index > 0 && testDist < f[index - 1])
        index--;
      for (int i = 3; i-- > index; ) {
        f[i + 1] = f[i];
        disp[i + 1] = disp[i];
      }
      f[index] = testDist;
      disp[index] = testDisp;
    }
  }

  private static class SVectorOrdering implements
      Comparable<SVectorOrdering> {
    public double val;
    public int axis;

    public SVectorOrdering(double v, int a) {
      val = v;
      axis = a;
    }

    @Override
    public int compareTo(SVectorOrdering o) {
      if (val > o.val) {
        return 1;
      }
      return -1;
    }
  }

  public static void sortBy6(double[] l1, int[] l2) {
    SVectorOrdering[] a = new SVectorOrdering[6];
    for (int i = 0; i < 6; i++) {
      a[i] = new SVectorOrdering(l1[i], l2[i]);
    }
    Arrays.sort(a);
    for (int i = 0; i < 6; i++) {
      l2[i] = a[i].axis;
    }
  }

  // ==========================================================================
  // = Hashing functions
  // ==========================================================================

  public static int fnv32ABuf(byte[] buf) {
    long hval = INIT32;
    for (int i = 0; i < buf.length; i++) {
      hval ^= buf[i];
      hval *= PRIME32;
    }
    return (int) (hval & 0x00000000ffffffffL);
  }

  public static int xorFoldHash(int hash) {
    return ((byte) ((hash >> 8) ^ (hash & FNV_MASK_8))) & 0xFF;
  }

  // ==========================================================================
  // = Interpolation functions
  // ==========================================================================

  private static double interpolateX2(double x, double y, double xs, int x0,
                                      int x1, int iy, long seed, WorkerNoise2 noisefunc) {
    double v1 = noisefunc.calculate(x, y, x0, iy, seed);
    double v2 = noisefunc.calculate(x, y, x1, iy, seed);
    return lerp(xs, v1, v2);
  }

  public static double interpolateXY2(double x, double y, double xs,
                                      double ys, int x0, int x1, int y0, int y1, long seed,
                                      WorkerNoise2 noisefunc) {
    double v1 = interpolateX2(x, y, xs, x0, x1, y0, seed, noisefunc);
    double v2 = interpolateX2(x, y, xs, x0, x1, y1, seed, noisefunc);
    return lerp(ys, v1, v2);
  }

  private static double interpolateX3(double x, double y, double z, double xs,
                                      int x0, int x1, int iy, int iz, long seed, WorkerNoise3 noisefunc) {
    double v1 = noisefunc.calculate(x, y, z, x0, iy, iz, seed);
    double v2 = noisefunc.calculate(x, y, z, x1, iy, iz, seed);
    return lerp(xs, v1, v2);
  }

  private static double interpolateXY3(double x, double y, double z, double xs,
                                       double ys, int x0, int x1, int y0, int y1, int iz, long seed,
                                       WorkerNoise3 noisefunc) {
    double v1 = interpolateX3(x, y, z, xs, x0, x1, y0, iz, seed, noisefunc);
    double v2 = interpolateX3(x, y, z, xs, x0, x1, y1, iz, seed, noisefunc);
    return lerp(ys, v1, v2);
  }

  public static double interpolateXYZ3(double x, double y, double z,
                                       double xs, double ys, double zs, int x0, int x1, int y0, int y1, int z0,
                                       int z1, long seed, WorkerNoise3 noisefunc) {
    double v1 = interpolateXY3(x, y, z, xs, ys, x0, x1, y0, y1, z0, seed, noisefunc);
    double v2 = interpolateXY3(x, y, z, xs, ys, x0, x1, y0, y1, z1, seed, noisefunc);
    return lerp(zs, v1, v2);
  }

  private static double interpolateX4(double x, double y, double z, double w,
                                      double xs, int x0, int x1, int iy, int iz, int iw, long seed,
                                      WorkerNoise4 noisefunc) {
    double v1 = noisefunc.calculate(x, y, z, w, x0, iy, iz, iw, seed);
    double v2 = noisefunc.calculate(x, y, z, w, x1, iy, iz, iw, seed);
    return lerp(xs, v1, v2);
  }

  private static double interpolateXY4(double x, double y, double z, double w,
                                       double xs, double ys, int x0, int x1, int y0, int y1, int iz, int iw,
                                       long seed, WorkerNoise4 noisefunc) {
    double v1 = interpolateX4(x, y, z, w, xs, x0, x1, y0, iz, iw, seed, noisefunc);
    double v2 = interpolateX4(x, y, z, w, xs, x0, x1, y1, iz, iw, seed, noisefunc);
    return lerp(ys, v1, v2);
  }

  private static double interpolateXYZ4(double x, double y, double z, double w,
                                        double xs, double ys, double zs, int x0, int x1, int y0, int y1, int z0,
                                        int z1, int iw, long seed, WorkerNoise4 noisefunc) {
    double v1 = interpolateXY4(x, y, z, w, xs, ys, x0, x1, y0, y1, z0, iw, seed, noisefunc);
    double v2 = interpolateXY4(x, y, z, w, xs, ys, x0, x1, y0, y1, z1, iw, seed, noisefunc);
    return lerp(zs, v1, v2);
  }

  public static double interpolateXYZW4(double x, double y, double z,
                                        double w, double xs, double ys, double zs, double ws, int x0, int x1,
                                        int y0, int y1, int z0, int z1, int w0, int w1, long seed,
                                        WorkerNoise4 noisefunc) {
    double v1 = interpolateXYZ4(x, y, z, w, xs, ys, zs, x0, x1, y0, y1, z0, z1, w0, seed, noisefunc);
    double v2 = interpolateXYZ4(x, y, z, w, xs, ys, zs, x0, x1, y0, y1, z0, z1, w1, seed, noisefunc);
    return lerp(ws, v1, v2);
  }

  private static double interpolateX6(double x, double y, double z, double w,
                                      double u, double v, double xs, int x0, int x1, int iy, int iz, int iw,
                                      int iu, int iv, long seed, WorkerNoise6 noisefunc) {
    double v1 = noisefunc.calculate(x, y, z, w, u, v, x0, iy, iz, iw, iu, iv, seed);
    double v2 = noisefunc.calculate(x, y, z, w, u, v, x1, iy, iz, iw, iu, iv, seed);
    return lerp(xs, v1, v2);
  }

  private static double interpolateXY6(double x, double y, double z, double w,
                                       double u, double v, double xs, double ys, int x0, int x1, int y0, int y1,
                                       int iz, int iw, int iu, int iv, long seed, WorkerNoise6 noisefunc) {
    double v1 = interpolateX6(x, y, z, w, u, v, xs, x0, x1, y0, iz, iw, iu, iv, seed, noisefunc);
    double v2 = interpolateX6(x, y, z, w, u, v, xs, x0, x1, y1, iz, iw, iu, iv, seed, noisefunc);
    return lerp(ys, v1, v2);
  }

  private static double interpolateXYZ6(double x, double y, double z, double w,
                                        double u, double v, double xs, double ys, double zs, int x0, int x1,
                                        int y0, int y1, int z0, int z1, int iw, int iu, int iv, long seed,
                                        WorkerNoise6 noisefunc) {
    double v1 = interpolateXY6(x, y, z, w, u, v, xs, ys, x0, x1, y0, y1, z0, iw, iu, iv, seed, noisefunc);
    double v2 = interpolateXY6(x, y, z, w, u, v, xs, ys, x0, x1, y0, y1, z1, iw, iu, iv, seed, noisefunc);
    return lerp(zs, v1, v2);
  }

  private static double interpolateXYZW6(double x, double y, double z,
                                         double w, double u, double v, double xs, double ys, double zs, double ws,
                                         int x0, int x1, int y0, int y1, int z0, int z1, int w0, int w1, int iu,
                                         int iv, long seed, WorkerNoise6 noisefunc) {
    double v1 = interpolateXYZ6(x, y, z, w, u, v, xs, ys, zs, x0, x1, y0, y1, z0, z1, w0, iu, iv, seed, noisefunc);
    double v2 = interpolateXYZ6(x, y, z, w, u, v, xs, ys, zs, x0, x1, y0, y1, z0, z1, w1, iu, iv, seed, noisefunc);
    return lerp(ws, v1, v2);
  }

  private static double interpolateXYZWU6(double x, double y, double z,
                                          double w, double u, double v, double xs, double ys, double zs, double ws,
                                          double us, int x0, int x1, int y0, int y1, int z0, int z1, int w0,
                                          int w1, int u0, int u1, int iv, long seed, WorkerNoise6 noisefunc) {
    double v1 = interpolateXYZW6(x, y, z, w, u, v, xs, ys, zs, ws, x0, x1, y0, y1, z0, z1, w0, w1, u0, iv, seed, noisefunc);
    double v2 = interpolateXYZW6(x, y, z, w, u, v, xs, ys, zs, ws, x0, x1, y0, y1, z0, z1, w0, w1, u1, iv, seed, noisefunc);
    return lerp(us, v1, v2);
  }

  public static double interpolateXYZWUV6(double x, double y, double z,
                                          double w, double u, double v, double xs, double ys, double zs, double ws,
                                          double us, double vs, int x0, int x1, int y0, int y1, int z0, int z1,
                                          int w0, int w1, int u0, int u1, int v0, int v1, long seed,
                                          WorkerNoise6 noisefunc) {
    double val1 = interpolateXYZWU6(x, y, z, w, u, v, xs, ys, zs, ws, us, x0, x1, y0, y1, z0, z1, w0, w1, u0, u1, v0, seed, noisefunc);
    double val2 = interpolateXYZWU6(x, y, z, w, u, v, xs, ys, zs, ws, us, x0, x1, y0, y1, z0, z1, w0, w1, u0, u1, v1, seed, noisefunc);
    return lerp(vs, val1, val2);
  }

  // ==========================================================================
  // = Cellular functions
  // ==========================================================================


  public static double intValueNoise3D(int x, int y, int z, int seed) {
    int n = (1619 * x + 31337 * y + 6971 * z + 1013 * seed) & 0x7fffffff;
    n = (n >> 13) ^ n;
    return (n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff;
  }

  public static double valueNoise3D(int x, int y, int z, int seed) {
    return 1.0 - (intValueNoise3D(x, y, z, seed) / 1073741824.0);
  }

}
