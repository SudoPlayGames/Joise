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

public class Array3Double {

  public static final byte X = 0;
  public static final byte Y = 1;
  public static final byte Z = 2;
  public static final byte XY = 3;

  private int[] size = new int[4];
  private double[] data;

  @SuppressWarnings("unused")
  private Array3Double() {}

  public Array3Double(int x, int y, int z) {
    size[X] = x;
    size[Y] = y;
    size[Z] = z;
    size[XY] = x * y;
    data = new double[x * y * z];
  }

  public void set(int x, int y, int z, double v) {
    data[x + (size[X] * y) + (size[XY] * z)] = v;
  }

  public double get(int x, int y, int z) {
    return data[x + (size[X] * y) + (size[XY] * z)];
  }

  public double[] getData() {
    return data;
  }

  public int[] getSize() {
    return size;
  }

  public int getWidth() {
    return size[X];
  }

  public int getHeight() {
    return size[Y];
  }

  public int getDepth() {
    return size[Z];
  }

}
