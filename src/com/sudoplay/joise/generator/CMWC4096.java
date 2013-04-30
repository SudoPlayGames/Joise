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

package com.sudoplay.joise.generator;

/**
 * Complimentary multiply with carry.
 */
public class CMWC4096 extends BasePRNG {

  protected int c;
  protected int[] Q = new int[4096];
  protected LCG lcg = new LCG();
  protected int i;

  public CMWC4096() {
    setSeed(10000);
  }

  @Override
  public int get() {
    long t;
    long a = 18782L;
    long b = 4294967295L;
    int r = (int) (b - 1);
    i = (i + 1) & 4095;
    t = a * Q[i] + c;
    c = (int) (t >> 32);
    t = (t & b) + c;
    if (t > r) {
      c++;
      t = t - b;
    }
    return Q[i] = (int) (r - t);
  }

  @Override
  public void setSeed(long seed) {
    lcg.setSeed(seed);
    for (int i = 0; i < 4096; i++) {
      Q[i] = lcg.get();
    }
    c = lcg.getTarget(18781);
  }

}
