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

package com.sudoplay.joise.module;

import com.sudoplay.joise.ModuleInstanceMap;
import com.sudoplay.joise.ModuleMap;
import com.sudoplay.joise.ModulePropertyMap;
import com.sudoplay.util.ModuleID;

public abstract class Module {

  @SuppressWarnings("WeakerAccess")
  public static final long DEFAULT_SEED = 10000;

  @SuppressWarnings("WeakerAccess")
  public static final int MAX_SOURCES = 10;

  public abstract double get(double x, double y);

  public abstract double get(double x, double y, double z);

  public abstract double get(double x, double y, double z, double w);

  public abstract double get(double x, double y, double z, double w, double u, double v);

  /**
   * The globally unique string id for this module, used in text serialization.
   */
  private final String id = ModuleID.create();

  /**
   * @return the globally unique string id for this module
   */
  public String getId() {
    return this.id;
  }

  /**
   * @return a new {@link ModuleMap} of the module chain using this module as the entry point
   */
  public ModuleMap getModuleMap() {
    ModuleMap map = new ModuleMap();
    this.writeToMap(map);
    return map;
  }

  public abstract void writeToMap(ModuleMap map);

  public abstract Module buildFromPropertyMap(ModulePropertyMap props, ModuleInstanceMap map);

}
