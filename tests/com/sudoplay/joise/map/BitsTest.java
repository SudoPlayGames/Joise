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

package com.sudoplay.joise.map;

import com.sudoplay.util.Bits;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

public class BitsTest {

  @Test
  public void longTest() {

    long x = Long.MAX_VALUE / 2;
    long y = Long.MAX_VALUE / 4;

    ByteBuffer byteBuffer = ByteBuffer.allocate(16);

    byte[] expected = byteBuffer.putLong(x).putLong(y).array();

    byte[] actual = new byte[16];
    Bits.longToByteArray(x, actual, 0);
    Bits.longToByteArray(y, actual, 8);

    Assert.assertArrayEquals(expected, actual);
  }

  @Test
  public void intTest() {

    int x = Integer.MAX_VALUE / 2;
    int y = Integer.MAX_VALUE / 4;

    ByteBuffer byteBuffer = ByteBuffer.allocate(8);

    byte[] expected = byteBuffer.putInt(x).putInt(y).array();

    byte[] actual = new byte[8];
    Bits.intToByteArray(x, actual, 0);
    Bits.intToByteArray(y, actual, 4);

    Assert.assertArrayEquals(expected, actual);
  }

  @Test
  public void doubleTest() {

    double x = Double.MAX_VALUE / 2;
    double y = Double.MAX_VALUE / 4;

    ByteBuffer byteBuffer = ByteBuffer.allocate(16);

    byte[] expected = byteBuffer.putDouble(x).putDouble(y).array();

    byte[] actual = new byte[16];
    Bits.doubleToByteArray(x, actual, 0);
    Bits.doubleToByteArray(y, actual, 8);

    Assert.assertArrayEquals(expected, actual);
  }

  @Test
  public void comboTest() {

    long x = Long.MAX_VALUE / 2;
    int y = Integer.MAX_VALUE / 2;
    double z = Double.MAX_VALUE / 2;

    ByteBuffer byteBuffer = ByteBuffer.allocate(20);

    byte[] expected = byteBuffer.putLong(x).putInt(y).putDouble(z).array();

    byte[] actual = new byte[20];
    Bits.longToByteArray(x, actual, 0);
    Bits.intToByteArray(y, actual, 8);
    Bits.doubleToByteArray(z, actual, 12);

    Assert.assertArrayEquals(expected, actual);
  }

}
