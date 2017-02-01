/*
 * Copyright (C) 2016 Jason Taylor.
 * Released as open-source under the Apache License, Version 2.0.
 * 
 * ============================================================================
 * | Joise
 * ============================================================================
 * 
 * Copyright (C) 2016 Jason Taylor
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

@SuppressWarnings("unused")
public class XORShift extends
    BasePRNG {

  private int x, y, z, w, v;
  private LCG lcg = new LCG();

  public XORShift() {
    this.setSeed(10000);
  }

  @Override
  public int get() {
    int t;
    t = (this.x ^ (this.x >> 7));
    // y is intentionally assigned to x here
    //noinspection SuspiciousNameCombination
    this.x = this.y;
    this.y = this.z;
    this.z = this.w;
    this.w = this.v;
    this.v = (this.v ^ (this.v << 6)) ^ (t ^ (t << 13));
    return (this.y + this.y + 1) * this.v;
  }

  @Override
  public void setSeed(long seed) {
    this.lcg.setSeed(seed);
    this.x = this.lcg.get();
    this.y = this.lcg.get();
    this.z = this.lcg.get();
    this.w = this.lcg.get();
    this.v = this.lcg.get();
  }
}
