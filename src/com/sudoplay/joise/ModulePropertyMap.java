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

package com.sudoplay.joise;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.sudoplay.joise.module.Module;

@SuppressWarnings("serial")
public class ModulePropertyMap extends LinkedHashMap<String, Object> {

  public ModulePropertyMap() {
    // serialization
  }

  public ModulePropertyMap(Module module) {
    setModule(module);
  }

  public void setModule(Module module) {
    super.put("module", module.getClass().getSimpleName());
  }

  @Override
  public Object put(String key, Object value) {
    if (key == null) {
      throw new NullPointerException();
    }
    if (value == null) {
      return super.put(key, null);
    } else {
      return super.put(key, value.toString());
    }
  }

  @Override
  public Object get(Object key) {
    if (key == null) {
      throw new NullPointerException();
    }
    Object value = super.get(key);
    return value;
  }

  public String getAsString(String key) {
    return get(key).toString();
  }

  public long getAsLong(String key) {
    try {
      return Long.parseLong(getAsString(key));
    } catch (NumberFormatException e) {
      throw new JoiseException("Expecting property [" + key + ", "
          + getAsString(key) + "] to be a long");
    }
  }

  public double getAsDouble(String key) {
    try {
      return Double.parseDouble(getAsString(key));
    } catch (NumberFormatException e) {
      throw new JoiseException("Expecting property [" + key + ", "
          + getAsString(key) + "] to be a double");
    }
  }

  public boolean getAsBoolean(String key) {
    String candidate = getAsString(key).toLowerCase();
    if ("true".equals(candidate) || "false".equals(candidate)) {
      return Boolean.parseBoolean(getAsString(key));
    } else {
      throw new JoiseException("Expecting property [" + key + ", "
          + getAsString(key) + "] to be a boolean");
    }
  }

  public boolean isModuleID(String key) {
    return getAsString(key).startsWith("func_");
  }

  public boolean contains(String key) {
    return super.get(key) != null;
  }

  public Iterator<Entry<String, Object>> iterator() {
    return super.entrySet().iterator();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    Iterator<Entry<String, Object>> it = iterator();
    while (it.hasNext()) {
      Entry<String, Object> e = it.next();
      sb.append("[");
      sb.append(e.getKey());
      sb.append("|");
      sb.append(e.getValue());
      sb.append("]");
    }

    return sb.toString();
  }

}
