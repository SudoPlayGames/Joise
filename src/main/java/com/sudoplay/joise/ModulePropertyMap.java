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

package com.sudoplay.joise;

import com.sudoplay.joise.module.Module;
import com.sudoplay.joise.module.ScalarParameter;
import com.sudoplay.joise.util.ModuleID;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class ModulePropertyMap extends LinkedHashMap<String, Object> {

  public ModulePropertyMap() {
    // serialization
  }

  public ModulePropertyMap(Module module) {
    this.setModule(module);
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
    return super.get(key);
  }

  public String getAsString(String key) {
    return this.get(key).toString();
  }

  public long getAsLong(String key) {

    try {
      return Long.parseLong(this.getAsString(key));

    } catch (NumberFormatException e) {
      throw new JoiseException("Expecting property [" + key + ", "
          + this.getAsString(key) + "] to be a long");
    }
  }

  @SuppressWarnings("WeakerAccess")
  public double getAsDouble(String key) {

    try {
      return Double.parseDouble(this.getAsString(key));

    } catch (NumberFormatException e) {
      throw new JoiseException("Expecting property [" + key + ", " + this.getAsString(key) + "] to be a double");
    }
  }

  @SuppressWarnings("WeakerAccess")
  public boolean getAsBoolean(String key) {
    String candidate = this.getAsString(key).toLowerCase();

    if ("true".equals(candidate) || "false".equals(candidate)) {
      return Boolean.parseBoolean(this.getAsString(key));

    } else {
      throw new JoiseException("Expecting property [" + key + ", " + this.getAsString(key) + "] to be a boolean");
    }
  }

  @SuppressWarnings("WeakerAccess")
  public boolean isModuleID(String key) {
    return this.getAsString(key).startsWith(ModuleID.MODULE_ID_PREFIX);
  }

  @SuppressWarnings("WeakerAccess")
  public boolean contains(String key) {
    return super.get(key) != null;
  }

  @SuppressWarnings("WeakerAccess")
  public Iterator<Entry<String, Object>> iterator() {
    return super.entrySet().iterator();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Iterator<Entry<String, Object>> it = this.iterator();

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

  /**
   * Returns a scalar parameter from the provided property map.
   *
   * @param propertyName      the name of the property
   * @param moduleInstanceMap the module instance map
   * @throws JoiseException if there is an error with the retrieval
   */
  public ScalarParameter readScalar(
      String propertyName,
      ModuleInstanceMap moduleInstanceMap
  ) {

    try {

      if (this.isModuleID(propertyName)) {
        // This is correct, not a suspicious method call
        //noinspection SuspiciousMethodCalls
        return new ScalarParameter(moduleInstanceMap.get(this.get(propertyName)));

      } else {
        return new ScalarParameter(this.getAsDouble(propertyName));
      }

    } catch (Exception e) {
      throw new JoiseException(e);
    }
  }

  /**
   * Returns a scalar parameter from the provided property map. If the provided property map does not contain the
   * property name provided, the provided default value is returned instead.
   *
   * @param propertyName      the name of the property
   * @param moduleInstanceMap the module instance map
   * @throws JoiseException if there is an error with the retrieval
   */
  public ScalarParameter readScalar(
      String propertyName,
      ModuleInstanceMap moduleInstanceMap,
      ScalarParameter defaultValue
  ) {

    if (this.contains(propertyName)) {
      return this.readScalar(propertyName, moduleInstanceMap);

    } else {
      return defaultValue;
    }
  }

  /**
   * Write a scalar property to the provided property map. If the scalar is a
   * module, {@link Module#writeToMap(ModuleMap)} is called on the scalar's module.
   *
   * @param propertyName    the name of the property
   * @param scalarParameter the scalar parameter
   * @param moduleMap       the module map to write to
   * @return this module property map
   */
  public ModulePropertyMap writeScalar(
      String propertyName,
      ScalarParameter scalarParameter,
      ModuleMap moduleMap
  ) {
    this.put(propertyName, scalarParameter);

    if (scalarParameter != null && scalarParameter.isModule()) {
      scalarParameter.getModule().writeToMap(moduleMap);
    }
    return this;
  }

  /**
   * Returns an enum property from the provided property map.
   *
   * @param propertyName the name of the property
   * @param enumClass    the class of the enum
   * @throws JoiseException if there is an error with the retrieval
   */
  public <T extends Enum<T>> T readEnum(
      String propertyName,
      Class<T> enumClass
  ) {

    try {
      return Enum.valueOf(enumClass, this.get(propertyName).toString().toUpperCase());

    } catch (Exception e) {
      throw new JoiseException(e);
    }
  }

  /**
   * Returns an enum property from the provided property map. If the provided property map does not contain the
   * property name provided, the provided default value is returned instead.
   *
   * @param propertyName the name of the property
   * @param enumClass    the class of the enum
   * @throws JoiseException if there is an error with the retrieval
   */
  @SuppressWarnings("unused")
  public <T extends Enum<T>> T readEnum(
      String propertyName,
      Class<T> enumClass,
      T defaultValue
  ) {

    if (this.contains(propertyName)) {
      return this.readEnum(propertyName, enumClass);

    } else {
      return defaultValue;
    }
  }

  /**
   * Write an enum property to the provided property map. The enum is converted
   * to lower-case.
   *
   * @param propertyName the name of the property
   * @param _enum        the enum
   * @return this module property map
   */
  public ModulePropertyMap writeEnum(
      String propertyName,
      Enum<?> _enum
  ) {
    this.put(propertyName, _enum.toString().toLowerCase());
    return this;
  }

  /**
   * Returns a long property from the provided property map.
   *
   * @param propertyName the name of the property
   * @throws JoiseException if there is an error with the retrieval
   */
  public long readLong(
      String propertyName
  ) {

    try {
      return this.getAsLong(propertyName);

    } catch (Exception e) {
      throw new JoiseException(e);
    }
  }

  /**
   * Returns a long property from the provided property map. If the provided property map does not contain the
   * property name provided, the provided default value is returned instead.
   *
   * @param propertyName the name of the property
   * @param defaultValue the default value
   * @throws JoiseException if there is an error with the retrieval
   */
  @SuppressWarnings("unused")
  public long readLong(
      String propertyName,
      long defaultValue
  ) {

    if (this.contains(propertyName)) {
      return this.readLong(propertyName);

    } else {
      return defaultValue;
    }
  }

  /**
   * Write a long property to the provided property map.
   *
   * @param propertyName the name of the property
   * @param longValue    the long value
   * @return this module property map
   */
  public ModulePropertyMap writeLong(
      String propertyName,
      long longValue
  ) {
    this.put(propertyName, longValue);
    return this;
  }

  /**
   * Returns a double property from the provided property map.
   *
   * @param propertyName the name of the property
   * @throws JoiseException if there is an error with the retrieval
   */
  public double readDouble(
      String propertyName
  ) {

    try {
      return this.getAsDouble(propertyName);

    } catch (Exception e) {
      throw new JoiseException(e);
    }
  }

  /**
   * Returns a double property from the provided property map. If the provided property map does not contain the
   * property name provided, the provided default value is returned instead.
   *
   * @param propertyName the name of the property
   * @param defaultValue the default value
   * @throws JoiseException if there is an error with the retrieval
   */
  public double readDouble(
      String propertyName,
      double defaultValue
  ) {

    if (this.contains(propertyName)) {
      return this.readDouble(propertyName);

    } else {
      return defaultValue;
    }
  }

  /**
   * Write a double property to the provided property map.
   *
   * @param propertyName the name of the property
   * @param doubleValue  the double value
   * @return this module property map
   */
  public ModulePropertyMap writeDouble(
      String propertyName,
      double doubleValue
  ) {
    this.put(propertyName, doubleValue);
    return this;
  }

  /**
   * Returns a boolean property from the provided property map.
   *
   * @param propertyName the name of the property
   * @throws JoiseException if there is an error with the retrieval
   */
  public boolean readBoolean(
      String propertyName
  ) {

    try {
      return this.getAsBoolean(propertyName);

    } catch (Exception e) {
      throw new JoiseException(e);
    }
  }

  /**
   * Returns a boolean property from the provided property map. If the provided property map does not contain the
   * property name provided, the provided default value is returned instead.
   *
   * @param propertyName the name of the property
   * @param defaultValue the default value
   * @throws JoiseException if there is an error with the retrieval
   */
  @SuppressWarnings("unused")
  public boolean readBoolean(
      String propertyName,
      boolean defaultValue
  ) {

    if (this.contains(propertyName)) {
      return this.readBoolean(propertyName);

    } else {
      return defaultValue;
    }
  }

  /**
   * Write a boolean property to the provided property map.
   *
   * @param propertyName the name of the property
   * @param booleanValue the boolean value
   * @return this module property map
   */
  public ModulePropertyMap writeBoolean(
      String propertyName,
      boolean booleanValue
  ) {
    this.put(propertyName, String.valueOf(booleanValue));
    return this;
  }
}
