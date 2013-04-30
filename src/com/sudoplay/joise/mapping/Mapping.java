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

package com.sudoplay.joise.mapping;

import com.sudoplay.joise.module.Module;

public final class Mapping {

  private static final double TWO_PI = Math.PI * 2.0;

  private Mapping() {
    // do not instantiate
  }

  public static void map2D(MappingMode mode, int width, int height, Module m,
      MappingRange range, Mapping2DWriter writer,
      MappingUpdateListener listener, double z) {

    if (writer == null) {
      writer = Mapping2DWriter.NULL_WRITER;
    }

    if (listener == null) {
      listener = MappingUpdateListener.NULL_LISTENER;
    }

    double p, q;
    double nx, ny, nz, nw, nu, nv;
    double r;
    double zval;

    double dx, dy, dz;
    double dx_div_2pi;
    double dy_div_2pi;
    double dz_div_2pi;

    double iw = 1.0 / (double) width;
    double ih = 1.0 / (double) height;

    double total = width * height;
    double current = 0;

    switch (mode) {

    case NORMAL:

      dx = range.map1.x - range.map0.x;
      dy = range.map1.y - range.map0.y;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {

          p = (double) x * iw;
          q = (double) y * ih;

          nx = range.map0.x + p * dx;
          ny = range.map0.y + q * dy;
          nz = z;

          writer.write(x, y, m.get(nx, ny, nz));
          listener.update(++current, total);
        }
      }
      return;

    case SEAMLESS_X:

      dx = range.loop1.x - range.loop0.x;
      dy = range.map1.y - range.map0.y;

      dx_div_2pi = dx / TWO_PI;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {

          p = (double) x * iw;
          q = (double) y * ih;

          p = p * (range.map1.x - range.map0.x) / dx;

          nx = range.loop0.x + Math.cos(p * TWO_PI) * dx_div_2pi;
          ny = range.loop0.x + Math.sin(p * TWO_PI) * dx_div_2pi;
          nz = range.map0.y + q * dy;
          nw = z;

          writer.write(x, y, m.get(nx, ny, nz, nw));
          listener.update(++current, total);
        }
      }
      return;

    case SEAMLESS_Y:

      dx = range.map1.x - range.map0.x;
      dy = range.loop1.y - range.loop0.y;

      dy_div_2pi = dy / TWO_PI;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {

          p = (double) x * iw;
          q = (double) y * ih;

          q = q * (range.map1.y - range.map0.y) / dy;

          nx = range.map0.x + p * dx;
          ny = range.loop0.y + Math.cos(q * TWO_PI) * dy_div_2pi;
          nz = range.loop0.y + Math.sin(q * TWO_PI) * dy_div_2pi;
          nw = z;

          writer.write(x, y, m.get(nx, ny, nz, nw));
          listener.update(++current, total);
        }
      }
      return;

    case SEAMLESS_Z:

      dx = range.map1.x - range.map0.x;
      dy = range.map1.y - range.map0.y;
      dz = range.loop1.z - range.loop0.z;

      r = (z - range.map0.z) / (range.map1.z - range.map0.z);
      zval = r * (range.map1.z - range.map0.z) / dz;

      dz_div_2pi = dz / TWO_PI;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {

          p = (double) x * iw;
          q = (double) y * ih;

          nx = range.map0.x + p * dx;
          ny = range.map0.y + p * dx;

          nz = range.loop0.z + Math.cos(zval * TWO_PI) * dz_div_2pi;
          nw = range.loop0.z + Math.sin(zval * TWO_PI) * dz_div_2pi;

          writer.write(x, y, m.get(nx, ny, nz, nw));
          listener.update(++current, total);
        }
      }
      return;

    case SEAMLESS_XY:

      dx = range.loop1.x - range.loop0.x;
      dy = range.loop1.y - range.loop0.y;

      dx_div_2pi = dx / TWO_PI;
      dy_div_2pi = dy / TWO_PI;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {

          p = (double) x * iw;
          q = (double) y * ih;

          p = p * (range.map1.x - range.map0.x) / dx;
          q = q * (range.map1.y - range.map0.y) / dy;

          nx = range.loop0.x + Math.cos(p * TWO_PI) * dx_div_2pi;
          ny = range.loop0.x + Math.sin(p * TWO_PI) * dx_div_2pi;
          nz = range.loop0.y + Math.cos(q * TWO_PI) * dy_div_2pi;
          nw = range.loop0.y + Math.sin(q * TWO_PI) * dy_div_2pi;
          nu = z;
          nv = 0;

          writer.write(x, y, m.get(nx, ny, nz, nw, nu, nv));
          listener.update(++current, total);
        }
      }
      return;

    case SEAMLESS_XZ:

      dx = range.loop1.x - range.loop0.x;
      dy = range.map1.y - range.map0.y;
      dz = range.loop1.z - range.loop0.z;

      dx_div_2pi = dx / TWO_PI;
      dz_div_2pi = dz / TWO_PI;

      r = (z - range.map0.z) / (range.map1.z - range.map0.z);
      zval = r * (range.map1.z - range.map0.z) / dz;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {

          p = (double) x * iw;
          q = (double) y * ih;

          p = p * (range.map1.x - range.map0.x) / dz;

          nx = range.loop0.x + Math.cos(p * TWO_PI) * dx_div_2pi;
          ny = range.loop0.x + Math.sin(p * TWO_PI) * dx_div_2pi;
          nz = range.map0.y + q * dy;
          nw = range.loop0.z + Math.cos(zval * TWO_PI) * dz_div_2pi;
          nu = range.loop0.z + Math.sin(zval * TWO_PI) * dz_div_2pi;
          nv = 0;

          writer.write(x, y, m.get(nx, ny, nz, nw, nu, nv));
          listener.update(++current, total);
        }
      }
      return;

    case SEAMLESS_YZ:

      dx = range.map1.x - range.map0.x;
      dy = range.loop1.y - range.loop0.y;
      dz = range.loop1.z - range.loop0.z;

      dy_div_2pi = dy / TWO_PI;
      dz_div_2pi = dz / TWO_PI;

      r = (z - range.map0.z) / (range.map1.z - range.map0.z);
      zval = r * (range.map1.z - range.map0.z) / dz;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {

          p = (double) x * iw;
          q = (double) y * ih;

          q = q * (range.map1.y - range.map0.y) / dy;

          nx = range.map0.x + p * dx;
          ny = range.loop0.y + Math.cos(q * TWO_PI) * dy_div_2pi;
          nz = range.loop0.y + Math.sin(q * TWO_PI) * dy_div_2pi;
          nw = range.loop0.z + Math.cos(zval * TWO_PI) * dz_div_2pi;
          nu = range.loop0.z + Math.sin(zval * TWO_PI) * dz_div_2pi;
          nv = 0;

          writer.write(x, y, m.get(nx, ny, nz, nw, nu, nv));
          listener.update(++current, total);
        }
      }
      return;

    case SEAMLESS_XYZ:

      dx = range.loop1.x - range.loop0.x;
      dy = range.loop1.y - range.loop0.y;
      dz = range.loop1.z - range.loop0.z;

      dx_div_2pi = dx / TWO_PI;
      dy_div_2pi = dy / TWO_PI;
      dz_div_2pi = dz / TWO_PI;

      r = (z - range.map0.z) / (range.map1.z - range.map0.z);
      zval = r * (range.map1.z - range.map0.z) / dz;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {

          p = (double) x * iw;
          q = (double) y * ih;

          p = p * (range.map1.x - range.map0.x) / dx;
          q = q * (range.map1.y - range.map0.y) / dy;

          nx = range.loop0.x + Math.cos(p * TWO_PI) * dx_div_2pi;
          ny = range.loop0.x + Math.sin(p * TWO_PI) * dx_div_2pi;
          nz = range.loop0.y + Math.cos(q * TWO_PI) * dy_div_2pi;
          nw = range.loop0.y + Math.sin(q * TWO_PI) * dy_div_2pi;
          nu = range.loop0.z + Math.cos(zval * TWO_PI) * dz_div_2pi;
          nv = range.loop0.z + Math.sin(zval * TWO_PI) * dz_div_2pi;

          writer.write(x, y, m.get(nx, ny, nz, nw, nu, nv));
          listener.update(++current, total);
        }
      }
      return;

    default:
      throw new AssertionError();
    }

  }

  public static void map2DNoZ(MappingMode mode, int width, int height,
      Module m, MappingRange range, Mapping2DWriter writer,
      MappingUpdateListener listener) {

    if (writer == null) {
      writer = Mapping2DWriter.NULL_WRITER;
    }

    if (listener == null) {
      listener = MappingUpdateListener.NULL_LISTENER;
    }

    double p, q;
    double nx, ny, nz, nw;

    double dx, dy;
    double dx_div_2pi;
    double dy_div_2pi;

    double iw = 1.0 / (double) width;
    double ih = 1.0 / (double) height;

    double total = width * height;
    double current = 0;

    switch (mode) {

    case NORMAL:

      dx = range.map1.x - range.map0.x;
      dy = range.map1.y - range.map0.y;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {

          p = (double) x * iw;
          q = (double) y * ih;

          nx = range.map0.x + p * dx;
          ny = range.map0.y + q * dy;

          writer.write(x, y, m.get(nx, ny));
          listener.update(++current, total);
        }
      }
      return;

    case SEAMLESS_X:

      dx = range.loop1.x - range.loop0.x;
      dy = range.map1.y - range.map0.y;

      dx_div_2pi = dx / TWO_PI;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {

          p = (double) x * iw;
          q = (double) y * ih;

          p = p * (range.map1.x - range.map0.x) / dx;

          nx = range.loop0.x + Math.cos(p * TWO_PI) * dx_div_2pi;
          ny = range.loop0.x + Math.sin(p * TWO_PI) * dx_div_2pi;
          nz = range.map0.y + q * dy;

          writer.write(x, y, m.get(nx, ny, nz));
          listener.update(++current, total);
        }
      }
      return;

    case SEAMLESS_Y:

      dx = range.map1.x - range.map0.x;
      dy = range.loop1.y - range.loop0.y;

      dy_div_2pi = dy / TWO_PI;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {

          p = (double) x * iw;
          q = (double) y * ih;

          q = q * (range.map1.y - range.map0.y) / dy;

          nx = range.map0.x + p * dx;
          ny = range.loop0.y + Math.cos(q * TWO_PI) * dy_div_2pi;
          nz = range.loop0.y + Math.sin(q * TWO_PI) * dy_div_2pi;

          writer.write(x, y, m.get(nx, ny, nz));
          listener.update(++current, total);
        }
      }
      return;

    case SEAMLESS_XY:

      dx = range.loop1.x - range.loop0.x;
      dy = range.loop1.y - range.loop0.y;

      dx_div_2pi = dx / TWO_PI;
      dy_div_2pi = dy / TWO_PI;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {

          p = (double) x * iw;
          q = (double) y * ih;

          p = p * (range.map1.x - range.map0.x) / dx;
          q = q * (range.map1.y - range.map0.y) / dy;

          nx = range.loop0.x + Math.cos(p * TWO_PI) * dx_div_2pi;
          ny = range.loop0.x + Math.sin(p * TWO_PI) * dx_div_2pi;
          nz = range.loop0.y + Math.cos(q * TWO_PI) * dy_div_2pi;
          nw = range.loop0.y + Math.sin(q * TWO_PI) * dy_div_2pi;

          writer.write(x, y, m.get(nx, ny, nz, nw));
          listener.update(++current, total);
        }
      }
      return;

    case SEAMLESS_Z:
    case SEAMLESS_XZ:
    case SEAMLESS_YZ:
    case SEAMLESS_XYZ:
      throw new UnsupportedOperationException(mode.toString());
    default:
      throw new AssertionError();
    }

  }

  public static void map3D(MappingMode mode, int width, int height, int depth,
      Module m, MappingRange range, Mapping3DWriter writer,
      MappingUpdateListener listener) {

    if (writer == null) {
      writer = Mapping3DWriter.NULL_WRITER;
    }

    if (listener == null) {
      listener = MappingUpdateListener.NULL_LISTENER;
    }

    double p, q, r;
    double nx, ny, nz, nw, nu, nv;

    double dx, dy, dz;
    double dx_div_2pi;
    double dy_div_2pi;
    double dz_div_2pi;

    double iw = 1.0 / (double) width;
    double ih = 1.0 / (double) height;
    double id = 1.0 / (double) depth;

    double total = width * height * depth;
    double current = 0;

    switch (mode) {

    case NORMAL:

      dx = range.map1.x - range.map0.x;
      dy = range.map1.y - range.map0.y;
      dz = range.map1.z - range.map0.z;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          for (int z = 0; z < depth; z++) {

            p = (double) x * iw;
            q = (double) y * ih;
            r = (double) z * id;

            nx = range.map0.x + p * dx;
            ny = range.map0.y + q * dy;
            nz = range.map0.z + r * dz;

            writer.write(x, y, z, m.get(nx, ny, nz));
            listener.update(++current, total);
          }
        }
      }
      return;

    case SEAMLESS_X:

      dx = range.loop1.x - range.loop0.x;
      dy = range.map1.y - range.map0.y;
      dz = range.map1.z - range.map0.z;

      dx_div_2pi = dx / TWO_PI;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          for (int z = 0; z < depth; z++) {

            p = (double) x * iw;
            q = (double) y * ih;
            r = (double) z * id;

            p = p * (range.map1.x - range.map0.x) / dx;

            nx = range.loop0.x + Math.cos(p * TWO_PI) * dx_div_2pi;
            ny = range.loop0.x + Math.sin(p * TWO_PI) * dx_div_2pi;
            nz = range.map0.y + q * dy;
            nw = range.map0.z + r * dz;

            writer.write(x, y, z, m.get(nx, ny, nz, nw));
            listener.update(++current, total);
          }
        }
      }
      return;

    case SEAMLESS_Y:

      dx = range.map1.x - range.map0.x;
      dy = range.loop1.y - range.loop0.y;
      dz = range.map1.z - range.map0.z;

      dy_div_2pi = dy / TWO_PI;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          for (int z = 0; z < depth; z++) {

            p = (double) x * iw;
            q = (double) y * ih;
            r = (double) z * id;

            q = q * (range.map1.y - range.map0.y) / dy;

            nx = range.map0.x + p * dx;
            ny = range.loop0.y + Math.cos(q * TWO_PI) * dy_div_2pi;
            nz = range.loop0.y + Math.sin(q * TWO_PI) * dy_div_2pi;
            nw = range.map0.z + r * dz;

            writer.write(x, y, z, m.get(nx, ny, nz, nw));
            listener.update(++current, total);
          }
        }
      }
      return;

    case SEAMLESS_Z:

      dx = range.map1.x - range.map0.x;
      dy = range.map1.y - range.map0.y;
      dz = range.loop1.z - range.loop0.z;

      dz_div_2pi = dz / TWO_PI;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          for (int z = 0; z < depth; z++) {

            p = (double) x * iw;
            q = (double) y * ih;
            r = (double) z * id;

            r = r * (range.map1.z - range.map0.z) / dz;

            nx = range.map0.x + p * dx;
            ny = range.map0.y + q * dy;
            nz = range.loop0.z + Math.cos(r * TWO_PI) * dz_div_2pi;
            nw = range.loop0.z + Math.sin(r * TWO_PI) * dz_div_2pi;

            writer.write(x, y, z, m.get(nx, ny, nz, nw));
            listener.update(++current, total);
          }
        }
      }
      return;

    case SEAMLESS_XY:

      dx = range.loop1.x - range.loop0.x;
      dy = range.loop1.y - range.loop0.y;
      dz = range.map1.z - range.map0.z;

      dx_div_2pi = dx / TWO_PI;
      dy_div_2pi = dy / TWO_PI;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          for (int z = 0; z < depth; z++) {

            p = (double) x * iw;
            q = (double) y * ih;
            r = (double) z * id;

            p = p * (range.map1.x - range.map0.x) / dx;
            q = q * (range.map1.y - range.map0.y) / dy;

            nx = range.loop0.x + Math.cos(p * TWO_PI) * dx_div_2pi;
            ny = range.loop0.x + Math.sin(p * TWO_PI) * dx_div_2pi;
            nz = range.loop0.y + Math.cos(q * TWO_PI) * dy_div_2pi;
            nw = range.loop0.y + Math.sin(q * TWO_PI) * dy_div_2pi;
            nu = range.map0.z + r * dz;
            nv = 0;

            writer.write(x, y, z, m.get(nx, ny, nz, nw, nu, nv));
            listener.update(++current, total);
          }
        }
      }
      return;

    case SEAMLESS_XZ:

      dx = range.loop1.x - range.loop0.x;
      dy = range.map1.y - range.map0.y;
      dz = range.loop1.z - range.loop0.z;

      dx_div_2pi = dx / TWO_PI;
      dz_div_2pi = dz / TWO_PI;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          for (int z = 0; z < depth; z++) {

            p = (double) x * iw;
            q = (double) y * ih;
            r = (double) z * id;

            p = p * (range.map1.x - range.map0.x) / dx;
            r = r * (range.map1.z - range.map0.z) / dz;

            nx = range.loop0.x + Math.cos(p * TWO_PI) * dx_div_2pi;
            ny = range.loop0.x + Math.sin(p * TWO_PI) * dx_div_2pi;
            nz = range.map0.y + q * dy;
            nw = range.loop0.z + Math.cos(r * TWO_PI) * dz_div_2pi;
            nu = range.loop0.z + Math.sin(r * TWO_PI) * dz_div_2pi;
            nv = 0;

            writer.write(x, y, z, m.get(nx, ny, nz, nw, nu, nv));
            listener.update(++current, total);
          }
        }
      }
      return;

    case SEAMLESS_YZ:

      dx = range.map1.x - range.map0.x;
      dy = range.loop1.y - range.loop0.y;
      dz = range.loop1.z - range.loop0.z;

      dy_div_2pi = dy / TWO_PI;
      dz_div_2pi = dz / TWO_PI;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          for (int z = 0; z < depth; z++) {

            p = (double) x * iw;
            q = (double) y * ih;
            r = (double) z * id;

            q = q * (range.map1.y - range.map0.y) / dy;
            r = r * (range.map1.z - range.map0.z) / dz;

            nx = range.map0.x + p * dx;
            ny = range.loop0.y + Math.cos(q * TWO_PI) * dy_div_2pi;
            nz = range.loop0.y + Math.sin(q * TWO_PI) * dy_div_2pi;
            nw = range.loop0.z + Math.cos(r * TWO_PI) * dz_div_2pi;
            nu = range.loop0.z + Math.sin(r * TWO_PI) * dz_div_2pi;
            nv = 0;

            writer.write(x, y, z, m.get(nx, ny, nz, nw, nu, nv));
            listener.update(++current, total);
          }
        }
      }
      return;

    case SEAMLESS_XYZ:

      dx = range.loop1.x - range.loop0.x;
      dy = range.loop1.y - range.loop0.y;
      dz = range.loop1.z - range.loop0.z;

      dx_div_2pi = dx / TWO_PI;
      dy_div_2pi = dy / TWO_PI;
      dz_div_2pi = dz / TWO_PI;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          for (int z = 0; z < depth; z++) {

            p = (double) x * iw;
            q = (double) y * ih;
            r = (double) z * id;

            p = p * (range.map1.x - range.map0.x) / dx;
            q = q * (range.map1.y - range.map0.y) / dy;
            r = r * (range.map1.z - range.map0.z) / dz;

            nx = range.loop0.x + Math.cos(p * TWO_PI) * dx_div_2pi;
            ny = range.loop0.x + Math.sin(p * TWO_PI) * dx_div_2pi;
            nz = range.loop0.y + Math.cos(q * TWO_PI) * dy_div_2pi;
            nw = range.loop0.y + Math.sin(q * TWO_PI) * dy_div_2pi;
            nu = range.loop0.z + Math.cos(r * TWO_PI) * dz_div_2pi;
            nv = range.loop0.z + Math.sin(r * TWO_PI) * dz_div_2pi;

            writer.write(x, y, z, m.get(nx, ny, nz, nw, nu, nv));
            listener.update(++current, total);
          }
        }
      }
      return;

    default:
      throw new AssertionError();
    }
  }

}
