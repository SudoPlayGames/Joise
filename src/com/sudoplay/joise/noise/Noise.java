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

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Noise {

  private static final long INIT32 = 0x811c9dc5L;
  private static final int PRIME32 = 0x01000193;
  private static final int FNV_MASK_8 = (1 << 8) - 1;

  private static final double INV_BYTEVAL = 1.0 / 255.0;

  private static final double F2 = 0.36602540378443864676372317075294;
  private static final double G2 = 0.21132486540518711774542560974902;
  private static final double F3 = 1.0 / 3.0;
  private static final double G3 = 1.0 / 6.0;

  private static final ThreadLocal<ByteBuffer> buf16 = new ThreadLocal<ByteBuffer>() {
    @Override
    protected ByteBuffer initialValue() {
      return ByteBuffer.allocate(16);
    }
  };

  private static final ThreadLocal<ByteBuffer> buf20 = new ThreadLocal<ByteBuffer>() {
    @Override
    protected ByteBuffer initialValue() {
      return ByteBuffer.allocate(20);
    }
  };

  private static final ThreadLocal<ByteBuffer> buf24 = new ThreadLocal<ByteBuffer>() {
    @Override
    protected ByteBuffer initialValue() {
      return ByteBuffer.allocate(24);
    }
  };

  private static final ThreadLocal<ByteBuffer> buf32 = new ThreadLocal<ByteBuffer>() {
    @Override
    protected ByteBuffer initialValue() {
      return ByteBuffer.allocate(32);
    }
  };

  private static final ThreadLocal<ByteBuffer> buf40 = new ThreadLocal<ByteBuffer>() {
    @Override
    protected ByteBuffer initialValue() {
      return ByteBuffer.allocate(40);
    }
  };

  private static final ThreadLocal<ByteBuffer> buf56 = new ThreadLocal<ByteBuffer>() {
    @Override
    protected ByteBuffer initialValue() {
      return ByteBuffer.allocate(56);
    }
  };

  // ==========================================================================
  // = Worker noise functions
  // ==========================================================================

  private static interface WorkerNoise2 {

    public static final WorkerNoise2 VALUE = new WorkerNoise2() {
      @Override
      public double calculate(double x, double y, int ix, int iy, long seed) {
        int n = (hashCoords2(ix, iy, seed));
        double noise = (double) n * INV_BYTEVAL;
        return noise * 2.0 - 1.0;
      }
    };

    public static final WorkerNoise2 GRADIENT = new WorkerNoise2() {
      @Override
      public double calculate(double x, double y, int ix, int iy, long seed) {
        int hash = hashCoords2(ix, iy, seed);
        double[] vec = NoiseLUT.gradient2DLUT[hash];
        double dx = x - (double) ix;
        double dy = y - (double) iy;
        return dx * vec[0] + dy * vec[1];
      }
    };

    public double calculate(double x, double y, int ix, int iy, long seed);
  }

  private static interface WorkerNoise3 {

    public static final WorkerNoise3 VALUE = new WorkerNoise3() {
      @Override
      public double calculate(double x, double y, double z, int ix, int iy,
          int iz, long seed) {
        int n = (hashCoords3(ix, iy, iz, seed));
        double noise = (double) n * INV_BYTEVAL;
        return noise * 2.0 - 1.0;
      }
    };

    public static final WorkerNoise3 GRADIENT = new WorkerNoise3() {
      @Override
      public double calculate(double x, double y, double z, int ix, int iy,
          int iz, long seed) {
        int hash = hashCoords3(ix, iy, iz, seed);
        double[] vec = NoiseLUT.gradient3DLUT[hash];
        double dx = x - (double) ix;
        double dy = y - (double) iy;
        double dz = z - (double) iz;
        return (dx * vec[0] + dy * vec[1] + dz * vec[2]);
      }
    };

    public double calculate(double x, double y, double z, int ix, int iy,
        int iz, long seed);
  }

  private static interface WorkerNoise4 {

    public static final WorkerNoise4 VALUE = new WorkerNoise4() {
      @Override
      public double calculate(double x, double y, double z, double w, int ix,
          int iy, int iz, int iw, long seed) {
        int n = (hashCoords4(ix, iy, iz, iw, seed));
        double noise = (double) n * INV_BYTEVAL;
        return noise * 2.0 - 1.0;
      }
    };

    public static final WorkerNoise4 GRADIENT = new WorkerNoise4() {
      @Override
      public double calculate(double x, double y, double z, double w, int ix,
          int iy, int iz, int iw, long seed) {
        int hash = hashCoords4(ix, iy, iz, iw, seed);
        double[] vec = NoiseLUT.gradient4DLUT[hash];
        double dx = x - (double) ix;
        double dy = y - (double) iy;
        double dz = z - (double) iz;
        double dw = w - (double) iw;
        return (dx * vec[0] + dy * vec[1] + dz * vec[2] + dw * vec[3]);
      }
    };

    public double calculate(double x, double y, double z, double w, int ix,
        int iy, int iz, int iw, long seed);
  }

  private static interface WorkerNoise6 {

    public static final WorkerNoise6 VALUE = new WorkerNoise6() {
      @Override
      public double calculate(double x, double y, double z, double w, double u,
          double v, int ix, int iy, int iz, int iw, int iu, int iv, long seed) {
        int n = (hashCoords6(ix, iy, iz, iw, iu, iv, seed));
        double noise = (double) n * INV_BYTEVAL;
        return noise * 2.0 - 1.0;
      }
    };

    public static final WorkerNoise6 GRADIENT = new WorkerNoise6() {
      @Override
      public double calculate(double x, double y, double z, double w, double u,
          double v, int ix, int iy, int iz, int iw, int iu, int iv, long seed) {
        int hash = hashCoords6(ix, iy, iz, iw, iu, iv, seed);
        double[] vec = NoiseLUT.gradient6DLUT[hash];

        double dx = x - (double) ix;
        double dy = y - (double) iy;
        double dz = z - (double) iz;
        double dw = w - (double) iw;
        double du = u - (double) iu;
        double dv = v - (double) iv;

        return (dx * vec[0] + dy * vec[1] + dz * vec[2] + dw * vec[3] + du
            * vec[4] + dv * vec[5]);
      }
    };

    public double calculate(double x, double y, double z, double w, double u,
        double v, int ix, int iy, int iz, int iw, int iu, int iv, long seed);
  }

  // ==========================================================================
  // = Common
  // ==========================================================================

  private static double lerp(double s, double v1, double v2) {
    return v1 + s * (v2 - v1);
  }

  private static int fastFloor(double t) {
    return t > 0 ? (int) t : (int) t - 1;
  }

  private static double arrayDot2(double[] arr, double a, double b) {
    return a * arr[0] + b * arr[1];
  }

  private static double arrayDot3(double[] arr, double a, double b, double c) {
    return a * arr[0] + b * arr[1] + c * arr[2];
  }

  private static double arrayDot4(double[] arr, double x, double y, double z,
      double w) {
    return x * arr[0] + y * arr[1] + z * arr[2] + w * arr[3];
  }

  private static void addDist(double[] f, double[] disp, double testDist,
      double testDisp) {
    int index;
    if (testDist < f[3]) {
      index = 3;
      while (index > 0 && testDist < f[index - 1])
        index--;
      for (int i = 3; i-- > index;) {
        f[i + 1] = f[i];
        disp[i + 1] = disp[i];
      }
      f[index] = testDist;
      disp[index] = testDisp;
    }
  }

  private static class SVectorOrdering implements Comparable<SVectorOrdering> {
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

  private static void sortBy6(double[] l1, int[] l2) {
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

  private static int fnv32ABuf(byte[] buf) {
    long hval = INIT32;
    for (int i = 0; i < buf.length; i++) {
      hval ^= buf[i];
      hval *= PRIME32;
    }
    return (int) (hval & 0x00000000ffffffffL);
  }

  private static int xorFoldHash(int hash) {
    return ((byte) ((hash >> 8) ^ (hash & FNV_MASK_8))) & 0xFF;
  }

  private static int hashCoords2(int x, int y, long seed) {
    ByteBuffer buf = buf16.get();
    buf.clear();
    buf.putInt(x).putInt(y).putLong(seed);
    return xorFoldHash(fnv32ABuf(buf.array()));
  }

  private static int hashCoords3(int x, int y, int z, long seed) {
    ByteBuffer buf = buf20.get();
    buf.clear();
    buf.putInt(x).putInt(y).putInt(z).putLong(seed);
    return xorFoldHash(fnv32ABuf(buf.array()));
  }

  private static int hashCoords4(int x, int y, int z, int w, long seed) {
    ByteBuffer buf = buf24.get();
    buf.clear();
    buf.putInt(x).putInt(y).putInt(z).putInt(w).putLong(seed);
    return xorFoldHash(fnv32ABuf(buf.array()));
  }

  private static int hashCoords6(int x, int y, int z, int w, int u, int v,
      long seed) {
    ByteBuffer buf = buf32.get();
    buf.clear();
    buf.putInt(x).putInt(y).putInt(z);
    buf.putInt(w).putInt(u).putInt(v).putLong(seed);
    return xorFoldHash(fnv32ABuf(buf.array()));
  }

  private static int computeHashDouble2(double x, double y, long seed) {
    ByteBuffer buf = buf24.get();
    buf.clear();
    buf.putDouble(x).putDouble(y).putLong(seed);
    return xorFoldHash(fnv32ABuf(buf.array()));
  }

  private static int computeHashDouble3(double x, double y, double z, long seed) {
    ByteBuffer buf = buf32.get();
    buf.clear();
    buf.putDouble(x).putDouble(y).putDouble(z).putLong(seed);
    return xorFoldHash(fnv32ABuf(buf.array()));
  }

  private static int computeHashDouble4(double x, double y, double z, double w,
      long seed) {
    ByteBuffer buf = buf40.get();
    buf.clear();
    buf.putDouble(x).putDouble(y).putDouble(z).putDouble(w).putLong(seed);
    return xorFoldHash(fnv32ABuf(buf.array()));
  }

  private static int computeHashDouble6(double x, double y, double z, double w,
      double u, double v, long seed) {
    ByteBuffer buf = buf56.get();
    buf.clear();
    buf.putDouble(x).putDouble(y).putDouble(z);
    buf.putDouble(w).putDouble(u).putDouble(v).putLong(seed);
    return xorFoldHash(fnv32ABuf(buf.array()));
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

  private static double interpolateXY2(double x, double y, double xs,
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

  private static double interpolateXYZ3(double x, double y, double z,
      double xs, double ys, double zs, int x0, int x1, int y0, int y1, int z0,
      int z1, long seed, WorkerNoise3 noisefunc) {
    double v1 = interpolateXY3(x, y, z, xs, ys, x0, x1, y0, y1, z0, seed,
        noisefunc);
    double v2 = interpolateXY3(x, y, z, xs, ys, x0, x1, y0, y1, z1, seed,
        noisefunc);
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
    double v1 = interpolateX4(x, y, z, w, xs, x0, x1, y0, iz, iw, seed,
        noisefunc);
    double v2 = interpolateX4(x, y, z, w, xs, x0, x1, y1, iz, iw, seed,
        noisefunc);
    return lerp(ys, v1, v2);
  }

  private static double interpolateXYZ4(double x, double y, double z, double w,
      double xs, double ys, double zs, int x0, int x1, int y0, int y1, int z0,
      int z1, int iw, long seed, WorkerNoise4 noisefunc) {
    double v1 = interpolateXY4(x, y, z, w, xs, ys, x0, x1, y0, y1, z0, iw,
        seed, noisefunc);
    double v2 = interpolateXY4(x, y, z, w, xs, ys, x0, x1, y0, y1, z1, iw,
        seed, noisefunc);
    return lerp(zs, v1, v2);
  }

  private static double interpolateXYZW4(double x, double y, double z,
      double w, double xs, double ys, double zs, double ws, int x0, int x1,
      int y0, int y1, int z0, int z1, int w0, int w1, long seed,
      WorkerNoise4 noisefunc) {
    double v1 = interpolateXYZ4(x, y, z, w, xs, ys, zs, x0, x1, y0, y1, z0, z1,
        w0, seed, noisefunc);
    double v2 = interpolateXYZ4(x, y, z, w, xs, ys, zs, x0, x1, y0, y1, z0, z1,
        w1, seed, noisefunc);
    return lerp(ws, v1, v2);
  }

  private static double interpolateX6(double x, double y, double z, double w,
      double u, double v, double xs, int x0, int x1, int iy, int iz, int iw,
      int iu, int iv, long seed, WorkerNoise6 noisefunc) {
    double v1 = noisefunc.calculate(x, y, z, w, u, v, x0, iy, iz, iw, iu, iv,
        seed);
    double v2 = noisefunc.calculate(x, y, z, w, u, v, x1, iy, iz, iw, iu, iv,
        seed);
    return lerp(xs, v1, v2);
  }

  private static double interpolateXY6(double x, double y, double z, double w,
      double u, double v, double xs, double ys, int x0, int x1, int y0, int y1,
      int iz, int iw, int iu, int iv, long seed, WorkerNoise6 noisefunc) {
    double v1 = interpolateX6(x, y, z, w, u, v, xs, x0, x1, y0, iz, iw, iu, iv,
        seed, noisefunc);
    double v2 = interpolateX6(x, y, z, w, u, v, xs, x0, x1, y1, iz, iw, iu, iv,
        seed, noisefunc);
    return lerp(ys, v1, v2);
  }

  private static double interpolateXYZ6(double x, double y, double z, double w,
      double u, double v, double xs, double ys, double zs, int x0, int x1,
      int y0, int y1, int z0, int z1, int iw, int iu, int iv, long seed,
      WorkerNoise6 noisefunc) {
    double v1 = interpolateXY6(x, y, z, w, u, v, xs, ys, x0, x1, y0, y1, z0,
        iw, iu, iv, seed, noisefunc);
    double v2 = interpolateXY6(x, y, z, w, u, v, xs, ys, x0, x1, y0, y1, z1,
        iw, iu, iv, seed, noisefunc);
    return lerp(zs, v1, v2);
  }

  private static double interpolateXYZW6(double x, double y, double z,
      double w, double u, double v, double xs, double ys, double zs, double ws,
      int x0, int x1, int y0, int y1, int z0, int z1, int w0, int w1, int iu,
      int iv, long seed, WorkerNoise6 noisefunc) {
    double v1 = interpolateXYZ6(x, y, z, w, u, v, xs, ys, zs, x0, x1, y0, y1,
        z0, z1, w0, iu, iv, seed, noisefunc);
    double v2 = interpolateXYZ6(x, y, z, w, u, v, xs, ys, zs, x0, x1, y0, y1,
        z0, z1, w1, iu, iv, seed, noisefunc);
    return lerp(ws, v1, v2);
  }

  private static double interpolateXYZWU6(double x, double y, double z,
      double w, double u, double v, double xs, double ys, double zs, double ws,
      double us, int x0, int x1, int y0, int y1, int z0, int z1, int w0,
      int w1, int u0, int u1, int iv, long seed, WorkerNoise6 noisefunc) {
    double v1 = interpolateXYZW6(x, y, z, w, u, v, xs, ys, zs, ws, x0, x1, y0,
        y1, z0, z1, w0, w1, u0, iv, seed, noisefunc);
    double v2 = interpolateXYZW6(x, y, z, w, u, v, xs, ys, zs, ws, x0, x1, y0,
        y1, z0, z1, w0, w1, u1, iv, seed, noisefunc);
    return lerp(us, v1, v2);
  }

  private static double interpolateXYZWUV6(double x, double y, double z,
      double w, double u, double v, double xs, double ys, double zs, double ws,
      double us, double vs, int x0, int x1, int y0, int y1, int z0, int z1,
      int w0, int w1, int u0, int u1, int v0, int v1, long seed,
      WorkerNoise6 noisefunc) {
    double val1 = interpolateXYZWU6(x, y, z, w, u, v, xs, ys, zs, ws, us, x0,
        x1, y0, y1, z0, z1, w0, w1, u0, u1, v0, seed, noisefunc);
    double val2 = interpolateXYZWU6(x, y, z, w, u, v, xs, ys, zs, ws, us, x0,
        x1, y0, y1, z0, z1, w0, w1, u0, u1, v1, seed, noisefunc);
    return lerp(vs, val1, val2);
  }

  // ==========================================================================
  // = 2D noise functions
  // ==========================================================================

  public static interface Function2D {

    public static Function2D VALUE = new Function2D() {
      @Override
      public double get(double x, double y, long seed, Interpolator interpolator) {
        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        double xs = interpolator.interpolate(x - (double) x0);
        double ys = interpolator.interpolate(y - (double) y0);
        return interpolateXY2(x, y, xs, ys, x0, x1, y0, y1, seed,
            WorkerNoise2.VALUE);
      }
    };

    public static Function2D GRADIENT = new Function2D() {
      @Override
      public double get(double x, double y, long seed, Interpolator interpolator) {
        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        double xs = interpolator.interpolate(x - (double) x0);
        double ys = interpolator.interpolate(y - (double) y0);
        return interpolateXY2(x, y, xs, ys, x0, x1, y0, y1, seed,
            WorkerNoise2.GRADIENT);
      }
    };

    public static Function2D GRADVAL = new Function2D() {
      @Override
      public double get(double x, double y, long seed, Interpolator interpolator) {
        return Function2D.VALUE.get(x, y, seed, interpolator)
            + Function2D.GRADIENT.get(x, y, seed, interpolator);
      }
    };

    public static Function2D WHITE = new Function2D() {
      @Override
      public double get(double x, double y, long seed, Interpolator interpolator) {
        int hash = computeHashDouble2(x, y, seed);
        return NoiseLUT.whitenoiseLUT[hash];
      }
    };

    public static Function2D SIMPLEX = new Function2D() {
      @Override
      public double get(double x, double y, long seed, Interpolator interpolator) {
        double s = (x + y) * F2;
        int i = fastFloor(x + s);
        int j = fastFloor(y + s);
        double t = (i + j) * G2;
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
        double x1 = x0 - (double) i1 + G2;
        double y1 = y0 - (double) j1 + G2;
        double x2 = x0 - 1.0 + 2.0 * G2;
        double y2 = y0 - 1.0 + 2.0 * G2;
        int h0 = hashCoords2(i, j, seed);
        int h1 = hashCoords2(i + i1, j + j1, seed);
        int h2 = hashCoords2(i + 1, j + 1, seed);
        double[] g0 = NoiseLUT.gradient2DLUT[h0];
        double[] g1 = NoiseLUT.gradient2DLUT[h1];
        double[] g2 = NoiseLUT.gradient2DLUT[h2];
        double n0, n1, n2;
        double t0 = 0.5 - x0 * x0 - y0 * y0;
        if (t0 < 0)
          n0 = 0;
        else {
          t0 *= t0;
          n0 = t0 * t0 * arrayDot2(g0, x0, y0);
        }
        double t1 = 0.5 - x1 * x1 - y1 * y1;
        if (t1 < 0)
          n1 = 0;
        else {
          t1 *= t1;
          n1 = t1 * t1 * arrayDot2(g1, x1, y1);
        }
        double t2 = 0.5 - x2 * x2 - y2 * y2;
        if (t2 < 0)
          n2 = 0;
        else {
          t2 *= t2;
          n2 = t2 * t2 * arrayDot2(g2, x2, y2);
        }
        return (70.0 * (n0 + n1 + n2)) * 1.42188695 + 0.001054489;
      }
    };

    public double get(double x, double y, long seed, Interpolator interpolator);
  }

  // ==========================================================================
  // = 3D noise functions
  // ==========================================================================

  public static interface Function3D {

    public static final Function3D VALUE = new Function3D() {
      @Override
      public double get(double x, double y, double z, long seed,
          Interpolator interpolator) {
        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int z0 = fastFloor(z);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        int z1 = z0 + 1;
        double xs = interpolator.interpolate(x - (double) x0);
        double ys = interpolator.interpolate(y - (double) y0);
        double zs = interpolator.interpolate(z - (double) z0);
        return interpolateXYZ3(x, y, z, xs, ys, zs, x0, x1, y0, y1, z0, z1,
            seed, WorkerNoise3.VALUE);
      }
    };

    public static final Function3D GRADIENT = new Function3D() {
      @Override
      public double get(double x, double y, double z, long seed,
          Interpolator interpolator) {
        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int z0 = fastFloor(z);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        int z1 = z0 + 1;
        double xs = interpolator.interpolate(x - (double) x0);
        double ys = interpolator.interpolate(y - (double) y0);
        double zs = interpolator.interpolate(z - (double) z0);
        return interpolateXYZ3(x, y, z, xs, ys, zs, x0, x1, y0, y1, z0, z1,
            seed, WorkerNoise3.GRADIENT);
      }
    };

    public static final Function3D GRADVAL = new Function3D() {
      @Override
      public double get(double x, double y, double z, long seed,
          Interpolator interpolator) {
        return Function3D.VALUE.get(x, y, z, seed, interpolator)
            + Function3D.GRADIENT.get(x, y, z, seed, interpolator);
      }
    };

    public static final Function3D WHITE = new Function3D() {
      @Override
      public double get(double x, double y, double z, long seed,
          Interpolator interpolator) {
        int hash = computeHashDouble3(x, y, z, seed);
        return NoiseLUT.whitenoiseLUT[hash];
      }
    };

    public static final Function3D SIMPLEX = new Function3D() {
      @Override
      public double get(double x, double y, double z, long seed,
          Interpolator interpolator) {
        double n0, n1, n2, n3;

        double s = (x + y + z) * F3;
        int i = fastFloor(x + s);
        int j = fastFloor(y + s);
        int k = fastFloor(z + s);

        double t = (i + j + k) * G3;
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

        double x1 = x0 - i1 + G3;
        double y1 = y0 - j1 + G3;
        double z1 = z0 - k1 + G3;
        double x2 = x0 - i2 + 2.0 * G3;
        double y2 = y0 - j2 + 2.0 * G3;
        double z2 = z0 - k2 + 2.0 * G3;
        double x3 = x0 - 1.0 + 3.0 * G3;
        double y3 = y0 - 1.0 + 3.0 * G3;
        double z3 = z0 - 1.0 + 3.0 * G3;

        int h0, h1, h2, h3;

        h0 = hashCoords3(i, j, k, seed);
        h1 = hashCoords3(i + i1, j + j1, k + k1, seed);
        h2 = hashCoords3(i + i2, j + j2, k + k2, seed);
        h3 = hashCoords3(i + 1, j + 1, k + 1, seed);

        double[] g0 = NoiseLUT.gradient3DLUT[h0];
        double[] g1 = NoiseLUT.gradient3DLUT[h1];
        double[] g2 = NoiseLUT.gradient3DLUT[h2];
        double[] g3 = NoiseLUT.gradient3DLUT[h3];

        double t0 = 0.6 - x0 * x0 - y0 * y0 - z0 * z0;
        if (t0 < 0.0)
          n0 = 0.0;
        else {
          t0 *= t0;
          n0 = t0 * t0 * arrayDot3(g0, x0, y0, z0);
        }

        double t1 = 0.6 - x1 * x1 - y1 * y1 - z1 * z1;
        if (t1 < 0.0)
          n1 = 0.0;
        else {
          t1 *= t1;
          n1 = t1 * t1 * arrayDot3(g1, x1, y1, z1);
        }

        double t2 = 0.6 - x2 * x2 - y2 * y2 - z2 * z2;
        if (t2 < 0)
          n2 = 0.0;
        else {
          t2 *= t2;
          n2 = t2 * t2 * arrayDot3(g2, x2, y2, z2);
        }

        double t3 = 0.6 - x3 * x3 - y3 * y3 - z3 * z3;
        if (t3 < 0)
          n3 = 0.0;
        else {
          t3 *= t3;
          n3 = t3 * t3 * arrayDot3(g3, x3, y3, z3);
        }

        return (32.0 * (n0 + n1 + n2 + n3)) * 1.25086885 + 0.0003194984;
      }
    };

    public double get(double x, double y, double z, long seed,
        Interpolator interpolator);
  }

  // ==========================================================================
  // = 4D noise functions
  // ==========================================================================

  public static interface Function4D {

    public static final Function4D VALUE = new Function4D() {
      @Override
      public double get(double x, double y, double z, double w, long seed,
          Interpolator interpolator) {
        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int z0 = fastFloor(z);
        int w0 = fastFloor(w);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        int z1 = z0 + 1;
        int w1 = w0 + 1;
        double xs = interpolator.interpolate(x - (double) x0);
        double ys = interpolator.interpolate(y - (double) y0);
        double zs = interpolator.interpolate(z - (double) z0);
        double ws = interpolator.interpolate(w - (double) w0);
        return interpolateXYZW4(x, y, z, w, xs, ys, zs, ws, x0, x1, y0, y1, z0,
            z1, w0, w1, seed, WorkerNoise4.VALUE);
      }
    };

    public static final Function4D GRADIENT = new Function4D() {
      @Override
      public double get(double x, double y, double z, double w, long seed,
          Interpolator interpolator) {
        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int z0 = fastFloor(z);
        int w0 = fastFloor(w);
        int x1 = x0 + 1;
        int y1 = y0 + 1;
        int z1 = z0 + 1;
        int w1 = w0 + 1;
        double xs = interpolator.interpolate(x - (double) x0);
        double ys = interpolator.interpolate(y - (double) y0);
        double zs = interpolator.interpolate(z - (double) z0);
        double ws = interpolator.interpolate(w - (double) w0);
        return interpolateXYZW4(x, y, z, w, xs, ys, zs, ws, x0, x1, y0, y1, z0,
            z1, w0, w1, seed, WorkerNoise4.GRADIENT);
      }
    };

    public static final Function4D GRADVAL = new Function4D() {
      @Override
      public double get(double x, double y, double z, double w, long seed,
          Interpolator interpolator) {
        return Function4D.VALUE.get(x, y, z, w, seed, interpolator)
            + Function4D.GRADIENT.get(x, y, z, w, seed, interpolator);
      }
    };

    public static final Function4D WHITE = new Function4D() {
      @Override
      public double get(double x, double y, double z, double w, long seed,
          Interpolator interpolator) {
        int hash = computeHashDouble4(x, y, z, w, seed);
        return NoiseLUT.whitenoiseLUT[hash];
      }
    };

    public static final Function4D SIMPLEX = new Function4D() {
      @Override
      public double get(double x, double y, double z, double w, long seed,
          Interpolator interpolator) {
        final int[][] simplex = { { 0, 1, 2, 3 }, { 0, 1, 3, 2 },
            { 0, 0, 0, 0 }, { 0, 2, 3, 1 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
            { 0, 0, 0, 0 }, { 1, 2, 3, 0 }, { 0, 2, 1, 3 }, { 0, 0, 0, 0 },
            { 0, 3, 1, 2 }, { 0, 3, 2, 1 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
            { 0, 0, 0, 0 }, { 1, 3, 2, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
            { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
            { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 1, 2, 0, 3 }, { 0, 0, 0, 0 },
            { 1, 3, 0, 2 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
            { 2, 3, 0, 1 }, { 2, 3, 1, 0 }, { 1, 0, 2, 3 }, { 1, 0, 3, 2 },
            { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 2, 0, 3, 1 },
            { 0, 0, 0, 0 }, { 2, 1, 3, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
            { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 },
            { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 2, 0, 1, 3 }, { 0, 0, 0, 0 },
            { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 3, 0, 1, 2 }, { 3, 0, 2, 1 },
            { 0, 0, 0, 0 }, { 3, 1, 2, 0 }, { 2, 1, 0, 3 }, { 0, 0, 0, 0 },
            { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 3, 1, 0, 2 }, { 0, 0, 0, 0 },
            { 3, 2, 0, 1 }, { 3, 2, 1, 0 } };

        double F4 = (Math.sqrt(5.0) - 1.0) / 4.0;
        double G4 = (5.0 - Math.sqrt(5.0)) / 20.0;
        double n0, n1, n2, n3, n4;
        double s = (x + y + z + w) * F4;
        int i = fastFloor(x + s);
        int j = fastFloor(y + s);
        int k = fastFloor(z + s);
        int l = fastFloor(w + s);
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
        i1 = simplex[c][0] >= 3 ? 1 : 0;
        j1 = simplex[c][1] >= 3 ? 1 : 0;
        k1 = simplex[c][2] >= 3 ? 1 : 0;
        l1 = simplex[c][3] >= 3 ? 1 : 0;
        i2 = simplex[c][0] >= 2 ? 1 : 0;
        j2 = simplex[c][1] >= 2 ? 1 : 0;
        k2 = simplex[c][2] >= 2 ? 1 : 0;
        l2 = simplex[c][3] >= 2 ? 1 : 0;
        i3 = simplex[c][0] >= 1 ? 1 : 0;
        j3 = simplex[c][1] >= 1 ? 1 : 0;
        k3 = simplex[c][2] >= 1 ? 1 : 0;
        l3 = simplex[c][3] >= 1 ? 1 : 0;
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
        h0 = hashCoords4(i, j, k, l, seed);
        h1 = hashCoords4(i + i1, j + j1, k + k1, l + l1, seed);
        h2 = hashCoords4(i + i2, j + j2, k + k2, l + l2, seed);
        h3 = hashCoords4(i + i3, j + j3, k + k3, l + l3, seed);
        h4 = hashCoords4(i + 1, j + 1, k + 1, l + 1, seed);
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
          n0 = t0 * t0 * arrayDot4(g0, x0, y0, z0, w0);
        }
        double t1 = 0.6 - x1 * x1 - y1 * y1 - z1 * z1 - w1 * w1;
        if (t1 < 0)
          n1 = 0.0;
        else {
          t1 *= t1;
          n1 = t1 * t1 * arrayDot4(g1, x1, y1, z1, w1);
        }
        double t2 = 0.6 - x2 * x2 - y2 * y2 - z2 * z2 - w2 * w2;
        if (t2 < 0)
          n2 = 0.0;
        else {
          t2 *= t2;
          n2 = t2 * t2 * arrayDot4(g2, x2, y2, z2, w2);
        }
        double t3 = 0.6 - x3 * x3 - y3 * y3 - z3 * z3 - w3 * w3;
        if (t3 < 0)
          n3 = 0.0;
        else {
          t3 *= t3;
          n3 = t3 * t3 * arrayDot4(g3, x3, y3, z3, w3);
        }
        double t4 = 0.6 - x4 * x4 - y4 * y4 - z4 * z4 - w4 * w4;
        if (t4 < 0)
          n4 = 0.0;
        else {
          t4 *= t4;
          n4 = t4 * t4 * arrayDot4(g4, x4, y4, z4, w4);
        }
        return 27.0 * (n0 + n1 + n2 + n3 + n4);
      }
    };

    public double get(double x, double y, double z, double w, long seed,
        Interpolator interpolator);
  }

  // ==========================================================================
  // = 6D noise functions
  // ==========================================================================

  public static interface Function6D {

    public static final Function6D VALUE = new Function6D() {
      @Override
      public double get(double x, double y, double z, double w, double u,
          double v, long seed, Interpolator interpolator) {
        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int z0 = fastFloor(z);
        int w0 = fastFloor(w);
        int u0 = fastFloor(u);
        int v0 = fastFloor(v);

        int x1 = x0 + 1;
        int y1 = y0 + 1;
        int z1 = z0 + 1;
        int w1 = w0 + 1;
        int u1 = u0 + 1;
        int v1 = v0 + 1;

        double xs = interpolator.interpolate((x - (double) x0));
        double ys = interpolator.interpolate((y - (double) y0));
        double zs = interpolator.interpolate((z - (double) z0));
        double ws = interpolator.interpolate((w - (double) w0));
        double us = interpolator.interpolate((u - (double) u0));
        double vs = interpolator.interpolate((v - (double) v0));

        return interpolateXYZWUV6(x, y, z, w, u, v, xs, ys, zs, ws, us, vs, x0,
            x1, y0, y1, z0, z1, w0, w1, u0, u1, v0, v1, seed,
            WorkerNoise6.VALUE);
      }
    };

    public static final Function6D GRADIENT = new Function6D() {
      @Override
      public double get(double x, double y, double z, double w, double u,
          double v, long seed, Interpolator interpolator) {
        int x0 = fastFloor(x);
        int y0 = fastFloor(y);
        int z0 = fastFloor(z);
        int w0 = fastFloor(w);
        int u0 = fastFloor(u);
        int v0 = fastFloor(v);

        int x1 = x0 + 1;
        int y1 = y0 + 1;
        int z1 = z0 + 1;
        int w1 = w0 + 1;
        int u1 = u0 + 1;
        int v1 = v0 + 1;

        double xs = interpolator.interpolate((x - (double) x0));
        double ys = interpolator.interpolate((y - (double) y0));
        double zs = interpolator.interpolate((z - (double) z0));
        double ws = interpolator.interpolate((w - (double) w0));
        double us = interpolator.interpolate((u - (double) u0));
        double vs = interpolator.interpolate((v - (double) v0));

        return interpolateXYZWUV6(x, y, z, w, u, v, xs, ys, zs, ws, us, vs, x0,
            x1, y0, y1, z0, z1, w0, w1, u0, u1, v0, v1, seed,
            WorkerNoise6.GRADIENT);
      }
    };

    public static final Function6D GRADVAL = new Function6D() {
      @Override
      public double get(double x, double y, double z, double w, double u,
          double v, long seed, Interpolator interpolator) {
        return Function6D.VALUE.get(x, y, z, w, u, v, seed, interpolator)
            + Function6D.GRADIENT.get(x, y, z, w, u, v, seed, interpolator);
      }
    };

    public static final Function6D WHITE = new Function6D() {
      @Override
      public double get(double x, double y, double z, double w, double u,
          double v, long seed, Interpolator interpolator) {
        int hash = computeHashDouble6(x, y, z, w, u, v, seed);
        return NoiseLUT.whitenoiseLUT[hash];
      }
    };

    public static final Function6D SIMPLEX = new Function6D() {
      @Override
      public double get(double x, double y, double z, double w, double u,
          double v, long seed, Interpolator interpolator) {

        double F4 = (Math.sqrt(7.0) - 1.0) / 6.0;
        double G4 = F4 / (1.0 + 6.0 * F4);

        double sideLength = Math.sqrt(6.0) / (6.0 * F4 + 1.0);
        double a = Math.sqrt((sideLength * sideLength)
            - ((sideLength / 2.0) * (sideLength / 2.0)));
        double cornerFace = Math.sqrt(a * a + (a / 2.0) * (a / 2.0));

        double cornerFaceSqrd = cornerFace * cornerFace;

        double valueScaler = Math.pow(5.0, -0.5);
        valueScaler *= Math.pow(5.0, -3.5) * 100 + 13;

        double[] loc = { x, y, z, w, u, v };
        double s = 0;
        for (int c = 0; c < 6; ++c)
          s += loc[c];
        s *= F4;

        int[] skewLoc = { fastFloor(x + s), fastFloor(y + s), fastFloor(z + s),
            fastFloor(w + s), fastFloor(u + s), fastFloor(v + s) };
        int[] intLoc = { fastFloor(x + s), fastFloor(y + s), fastFloor(z + s),
            fastFloor(w + s), fastFloor(u + s), fastFloor(v + s) };
        double unskew = 0.0;
        for (int c = 0; c < 6; ++c)
          unskew += skewLoc[c];
        unskew *= G4;
        double[] cellDist = { loc[0] - (double) skewLoc[0] + unskew,
            loc[1] - (double) skewLoc[1] + unskew,
            loc[2] - (double) skewLoc[2] + unskew,
            loc[3] - (double) skewLoc[3] + unskew,
            loc[4] - (double) skewLoc[4] + unskew,
            loc[5] - (double) skewLoc[5] + unskew };
        int[] distOrder = { 0, 1, 2, 3, 4, 5 };
        sortBy6(cellDist, distOrder);

        int[] newDistOrder = { -1, distOrder[0], distOrder[1], distOrder[2],
            distOrder[3], distOrder[4], distOrder[5] };

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
            int h = hashCoords6(intLoc[0], intLoc[1], intLoc[2], intLoc[3],
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
    };

    public double get(double x, double y, double z, double w, double u,
        double v, long seed, Interpolator interpolator);
  }

  // ==========================================================================
  // = Cellular functions
  // ==========================================================================

  public static void cellularFunction2D(double x, double y, long seed,
      double[] f, double[] disp) {
    int xint = fastFloor(x);
    int yint = fastFloor(y);

    for (int c = 0; c < 4; ++c) {
      f[c] = 99999.0;
      disp[c] = 0.0;
    }

    for (int ycur = yint - 3; ycur <= yint + 3; ++ycur) {
      for (int xcur = xint - 3; xcur <= xint + 3; ++xcur) {

        double xpos = (double) xcur
            + WorkerNoise2.VALUE.calculate(x, y, xcur, ycur, seed);
        double ypos = (double) ycur
            + WorkerNoise2.VALUE.calculate(x, y, xcur, ycur, seed + 1);
        double xdist = xpos - x;
        double ydist = ypos - y;
        double dist = (xdist * xdist + ydist * ydist);
        int xval = fastFloor(xpos);
        int yval = fastFloor(ypos);
        double dsp = WorkerNoise2.VALUE.calculate(x, y, xval, yval, seed + 3);
        addDist(f, disp, dist, dsp);
      }
    }

  }

  public static void cellularFunction3D(double x, double y, double z,
      long seed, double[] f, double[] disp) {
    int xint = fastFloor(x);
    int yint = fastFloor(y);
    int zint = fastFloor(z);

    for (int c = 0; c < 4; ++c) {
      f[c] = 99999.0;
      disp[c] = 0.0;
    }

    for (int zcur = zint - 2; zcur <= zint + 2; ++zcur) {
      for (int ycur = yint - 2; ycur <= yint + 2; ++ycur) {
        for (int xcur = xint - 2; xcur <= xint + 2; ++xcur) {
          double xpos = (double) xcur
              + WorkerNoise3.VALUE.calculate(x, y, z, xcur, ycur, zcur, seed);
          double ypos = (double) ycur
              + WorkerNoise3.VALUE.calculate(x, y, z, xcur, ycur, zcur,
                  seed + 1);
          double zpos = (double) zcur
              + WorkerNoise3.VALUE.calculate(x, y, z, xcur, ycur, zcur,
                  seed + 2);
          double xdist = xpos - x;
          double ydist = ypos - y;
          double zdist = zpos - z;
          double dist = (xdist * xdist + ydist * ydist + zdist * zdist);
          int xval = fastFloor(xpos);
          int yval = fastFloor(ypos);
          int zval = fastFloor(zpos);
          double dsp = WorkerNoise3.VALUE.calculate(x, y, z, xval, yval, zval,
              seed + 3);
          addDist(f, disp, dist, dsp);
        }
      }
    }
  }

  public static void cellularFunction4D(double x, double y, double z, double w,
      long seed, double[] f, double[] disp) {
    int xint = fastFloor(x);
    int yint = fastFloor(y);
    int zint = fastFloor(z);
    int wint = fastFloor(w);

    for (int c = 0; c < 4; ++c) {
      f[c] = 99999.0;
      disp[c] = 0.0;
    }

    for (int wcur = wint - 2; wcur <= wint + 2; ++wcur) {
      for (int zcur = zint - 2; zcur <= zint + 2; ++zcur) {
        for (int ycur = yint - 2; ycur <= yint + 2; ++ycur) {
          for (int xcur = xint - 2; xcur <= xint + 2; ++xcur) {
            double xpos = (double) xcur
                + WorkerNoise4.VALUE.calculate(x, y, z, w, xcur, ycur, zcur,
                    wcur, seed);
            double ypos = (double) ycur
                + WorkerNoise4.VALUE.calculate(x, y, z, w, xcur, ycur, zcur,
                    wcur, seed + 1);
            double zpos = (double) zcur
                + WorkerNoise4.VALUE.calculate(x, y, z, w, xcur, ycur, zcur,
                    wcur, seed + 2);
            double wpos = (double) wcur
                + WorkerNoise4.VALUE.calculate(x, y, z, w, xcur, ycur, zcur,
                    wcur, seed + 3);
            double xdist = xpos - x;
            double ydist = ypos - y;
            double zdist = zpos - z;
            double wdist = wpos - w;
            double dist = (xdist * xdist + ydist * ydist + zdist * zdist + wdist
                * wdist);
            int xval = fastFloor(xpos);
            int yval = fastFloor(ypos);
            int zval = fastFloor(zpos);
            int wval = fastFloor(wpos);
            double dsp = WorkerNoise4.VALUE.calculate(x, y, z, w, xval, yval,
                zval, wval, seed + 3);
            addDist(f, disp, dist, dsp);
          }
        }
      }
    }
  }

  public static void cellularFunction6D(double x, double y, double z, double w,
      double u, double v, long seed, double[] f, double[] disp) {
    int xint = fastFloor(x);
    int yint = fastFloor(y);
    int zint = fastFloor(z);
    int wint = fastFloor(w);
    int uint = fastFloor(u);
    int vint = fastFloor(v);

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
                double xpos = (double) xcur
                    + WorkerNoise6.VALUE.calculate(x, y, z, w, u, v, xcur,
                        ycur, zcur, wcur, ucur, vcur, seed);
                double ypos = (double) ycur
                    + WorkerNoise6.VALUE.calculate(x, y, z, w, u, v, xcur,
                        ycur, zcur, wcur, ucur, vcur, seed + 1);
                double zpos = (double) zcur
                    + WorkerNoise6.VALUE.calculate(x, y, z, w, u, v, xcur,
                        ycur, zcur, wcur, ucur, vcur, seed + 2);
                double wpos = (double) wcur
                    + WorkerNoise6.VALUE.calculate(x, y, z, w, u, v, xcur,
                        ycur, zcur, wcur, ucur, vcur, seed + 3);
                double upos = (double) ucur
                    + WorkerNoise6.VALUE.calculate(x, y, z, w, u, v, xcur,
                        ycur, zcur, wcur, ucur, vcur, seed + 4);
                double vpos = (double) vcur
                    + WorkerNoise6.VALUE.calculate(x, y, z, w, u, v, xcur,
                        ycur, zcur, wcur, ucur, vcur, seed + 5);
                double xdist = xpos - x;
                double ydist = ypos - y;
                double zdist = zpos - z;
                double wdist = wpos - w;
                double udist = upos - u;
                double vdist = vpos - v;
                double dist = (xdist * xdist + ydist * ydist + zdist * zdist
                    + wdist * wdist + udist * udist + vdist * vdist);
                int xval = fastFloor(xpos);
                int yval = fastFloor(ypos);
                int zval = fastFloor(zpos);
                int wval = fastFloor(wpos);
                int uval = fastFloor(upos);
                int vval = fastFloor(vpos);
                double dsp = WorkerNoise6.VALUE.calculate(x, y, z, w, u, v,
                    xval, yval, zval, wval, uval, vval, seed + 6);
                addDist(f, disp, dist, dsp);
              }
            }
          }
        }
      }
    }
  }

  public static double intValueNoise3D(int x, int y, int z, int seed) {
    int n = (1619 * x + 31337 * y + 6971 * z + 1013 * seed) & 0x7fffffff;
    n = (n >> 13) ^ n;
    return (n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff;
  }

  public static double valueNoise3D(int x, int y, int z, int seed) {
    return 1.0 - ((double) intValueNoise3D(x, y, z, seed) / 1073741824.0);
  }

}
