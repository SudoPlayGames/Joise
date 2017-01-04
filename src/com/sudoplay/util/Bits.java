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

package com.sudoplay.util;

public class Bits {

  private Bits() {
    //
  }

  public static void longToByteArray(long value, byte[] array, int startFrom) {
    array[startFrom] = (byte) (value >> 56);
    array[startFrom + 1] = (byte) (value >> 48);
    array[startFrom + 2] = (byte) (value >> 40);
    array[startFrom + 3] = (byte) (value >> 32);
    array[startFrom + 4] = (byte) (value >> 24);
    array[startFrom + 5] = (byte) (value >> 16);
    array[startFrom + 6] = (byte) (value >> 8);
    array[startFrom + 7] = (byte) (value);
  }

  public static void intToByteArray(int value, byte[] array, int startFrom) {
    array[startFrom] = (byte) (value >> 24);
    array[startFrom + 1] = (byte) (value >> 16);
    array[startFrom + 2] = (byte) (value >> 8);
    array[startFrom + 3] = (byte) (value);
  }

  public static void doubleToByteArray(double value, byte[] array, int startFrom) {
    long l = Double.doubleToLongBits(value);
    array[startFrom] = (byte) (l >> 56);
    array[startFrom + 1] = (byte) (l >> 48);
    array[startFrom + 2] = (byte) (l >> 40);
    array[startFrom + 3] = (byte) (l >> 32);
    array[startFrom + 4] = (byte) (l >> 24);
    array[startFrom + 5] = (byte) (l >> 16);
    array[startFrom + 6] = (byte) (l >> 8);
    array[startFrom + 7] = (byte) (l);
  }
}
