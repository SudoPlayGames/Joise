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
public class KISS extends
    BasePRNG {

  private LCG lcg = new LCG();
  private int z, w, jsr, jcong;

  public KISS() {
    this.setSeed(DEFAULT_SEED);
  }

  @Override
  public int get() {
    this.z = 36969 * (this.z & 65535) + (this.z >> 16);
    this.w = 18000 * (this.w & 65535) + (this.w >> 16);
    int mwc = (this.z << 16) + this.w;
    this.jcong = 69069 * this.jcong + 1234567;
    this.jsr ^= (this.jsr << 17);
    this.jsr ^= (this.jsr >> 13);
    this.jsr ^= (this.jsr << 5);
    return ((mwc ^ this.jcong) + this.jsr);
  }

  @Override
  public void setSeed(long seed) {
    this.lcg.setSeed(seed);
    this.z = this.lcg.get();
    this.w = this.lcg.get();
    this.jsr = this.lcg.get();
    this.jcong = this.lcg.get();
  }
}
