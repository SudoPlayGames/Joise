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

import com.sudoplay.joise.JoiseException;
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
    this._writeToMap(map);
    return map;
  }

  /**
   * Writes the module chain to an existing {@link ModuleMap}, using this module as the entry point.
   * <p>
   * Note: This method is deprecated since version 1.1.0 and will be removed in the future.
   * <p>
   * TODO: should this be removed now?
   *
   * @param map existing {@link ModuleMap}
   */
  @Deprecated
  public void writeToMap(ModuleMap map) {

    if (map.contains(this.id)) {
      return;
    }
    this._writeToMap(map);
  }

  protected abstract void _writeToMap(ModuleMap map);

  public abstract Module buildFromPropertyMap(ModulePropertyMap props, ModuleInstanceMap map);

  /**
   * Returns a scalar parameter from the provided property map.
   *
   * @param propertyName      the name of the property
   * @param modulePropertyMap the module property map
   * @param moduleInstanceMap the module instance map
   * @throws JoiseException if there is an error with the retrieval
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  protected ScalarParameter readScalar(
      String propertyName,
      ModulePropertyMap modulePropertyMap,
      ModuleInstanceMap moduleInstanceMap
  ) {

    try {

      if (modulePropertyMap.isModuleID(propertyName)) {
        // This is correct, not a suspicious method call
        //noinspection SuspiciousMethodCalls
        return new ScalarParameter(moduleInstanceMap.get(modulePropertyMap.get(propertyName)));

      } else {
        return new ScalarParameter(modulePropertyMap.getAsDouble(propertyName));
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
   * @param modulePropertyMap the module property map
   * @param moduleInstanceMap the module instance map
   * @throws JoiseException if there is an error with the retrieval
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  protected ScalarParameter readScalar(
      String propertyName,
      ModulePropertyMap modulePropertyMap,
      ModuleInstanceMap moduleInstanceMap,
      ScalarParameter defaultValue
  ) {

    if (modulePropertyMap.contains(propertyName)) {
      return this.readScalar(propertyName, modulePropertyMap, moduleInstanceMap);

    } else {
      return defaultValue;
    }
  }

  /**
   * Write a scalar property to the provided property map. If the scalar is a
   * module, {@link #_writeToMap(ModuleMap)} is called on the scalar's module.
   *
   * @param propertyName      the name of the property
   * @param scalarParameter   the scalar parameter
   * @param modulePropertyMap the module property map
   * @param moduleMap         the module map to write to
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  protected void writeScalar(
      String propertyName,
      ScalarParameter scalarParameter,
      ModulePropertyMap modulePropertyMap,
      ModuleMap moduleMap
  ) {
    modulePropertyMap.put(propertyName, scalarParameter);

    if (scalarParameter != null && scalarParameter.isModule()) {
      scalarParameter.getModule()._writeToMap(moduleMap);
    }
  }

  /**
   * Returns an enum property from the provided property map.
   *
   * @param propertyName      the name of the property
   * @param enumClass         the class of the enum
   * @param modulePropertyMap the module property map
   * @throws JoiseException if there is an error with the retrieval
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  protected <T extends Enum<T>> T readEnum(
      String propertyName,
      Class<T> enumClass,
      ModulePropertyMap modulePropertyMap
  ) {

    try {
      return Enum.valueOf(enumClass, modulePropertyMap.get(propertyName).toString().toUpperCase());

    } catch (Exception e) {
      throw new JoiseException(e);
    }
  }

  /**
   * Returns an enum property from the provided property map. If the provided property map does not contain the
   * property name provided, the provided default value is returned instead.
   *
   * @param propertyName      the name of the property
   * @param enumClass         the class of the enum
   * @param modulePropertyMap the module property map
   * @throws JoiseException if there is an error with the retrieval
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  protected <T extends Enum<T>> T readEnum(
      String propertyName,
      Class<T> enumClass,
      ModulePropertyMap modulePropertyMap,
      T defaultValue
  ) {

    if (modulePropertyMap.contains(propertyName)) {
      return this.readEnum(propertyName, enumClass, modulePropertyMap);

    } else {
      return defaultValue;
    }
  }

  /**
   * Write an enum property to the provided property map. The enum is converted
   * to lower-case.
   *
   * @param propertyName      the name of the property
   * @param _enum             the enum
   * @param modulePropertyMap the module property map
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  protected void writeEnum(
      String propertyName,
      Enum<?> _enum,
      ModulePropertyMap modulePropertyMap
  ) {
    modulePropertyMap.put(propertyName, _enum.toString().toLowerCase());
  }

  /**
   * Returns a long property from the provided property map.
   *
   * @param propertyName      the name of the property
   * @param modulePropertyMap the module property map
   * @throws JoiseException if there is an error with the retrieval
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  protected long readLong(
      String propertyName,
      ModulePropertyMap modulePropertyMap
  ) {

    try {
      return modulePropertyMap.getAsLong(propertyName);

    } catch (Exception e) {
      throw new JoiseException(e);
    }
  }

  /**
   * Returns a long property from the provided property map. If the provided property map does not contain the
   * property name provided, the provided default value is returned instead.
   *
   * @param propertyName      the name of the property
   * @param modulePropertyMap the module property map
   * @param defaultValue      the default value
   * @throws JoiseException if there is an error with the retrieval
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  protected long readLong(
      String propertyName,
      ModulePropertyMap modulePropertyMap,
      long defaultValue
  ) {

    if (modulePropertyMap.contains(propertyName)) {
      return this.readLong(propertyName, modulePropertyMap);

    } else {
      return defaultValue;
    }
  }

  /**
   * Write a long property to the provided property map.
   *
   * @param propertyName      the name of the property
   * @param longValue         the long value
   * @param modulePropertyMap the module property map
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  protected void writeLong(
      String propertyName,
      long longValue,
      ModulePropertyMap modulePropertyMap
  ) {
    modulePropertyMap.put(propertyName, longValue);
  }

  /**
   * Returns a double property from the provided property map.
   *
   * @param propertyName      the name of the property
   * @param modulePropertyMap the module property map
   * @throws JoiseException if there is an error with the retrieval
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  protected double readDouble(
      String propertyName,
      ModulePropertyMap modulePropertyMap
  ) {

    try {
      return modulePropertyMap.getAsDouble(propertyName);

    } catch (Exception e) {
      throw new JoiseException(e);
    }
  }

  /**
   * Returns a double property from the provided property map. If the provided property map does not contain the
   * property name provided, the provided default value is returned instead.
   *
   * @param propertyName      the name of the property
   * @param modulePropertyMap the module property map
   * @param defaultValue      the default value
   * @throws JoiseException if there is an error with the retrieval
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  protected double readDouble(
      String propertyName,
      ModulePropertyMap modulePropertyMap,
      double defaultValue
  ) {

    if (modulePropertyMap.contains(propertyName)) {
      return this.readDouble(propertyName, modulePropertyMap);

    } else {
      return defaultValue;
    }
  }

  /**
   * Write a double property to the provided property map.
   *
   * @param propertyName      the name of the property
   * @param doubleValue       the double value
   * @param modulePropertyMap the module property map
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  protected void writeDouble(
      String propertyName,
      double doubleValue,
      ModulePropertyMap modulePropertyMap
  ) {
    modulePropertyMap.put(propertyName, doubleValue);
  }

  /**
   * Returns a boolean property from the provided property map.
   *
   * @param propertyName      the name of the property
   * @param modulePropertyMap the module property map
   * @throws JoiseException if there is an error with the retrieval
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  protected boolean readBoolean(
      String propertyName,
      ModulePropertyMap modulePropertyMap
  ) {

    try {
      return modulePropertyMap.getAsBoolean(propertyName);

    } catch (Exception e) {
      throw new JoiseException(e);
    }
  }

  /**
   * Returns a boolean property from the provided property map. If the provided property map does not contain the
   * property name provided, the provided default value is returned instead.
   *
   * @param propertyName      the name of the property
   * @param modulePropertyMap the module property map
   * @param defaultValue      the default value
   * @throws JoiseException if there is an error with the retrieval
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  protected boolean readBoolean(
      String propertyName,
      ModulePropertyMap modulePropertyMap,
      boolean defaultValue
  ) {

    if (modulePropertyMap.contains(propertyName)) {
      return this.readBoolean(propertyName, modulePropertyMap);

    } else {
      return defaultValue;
    }
  }

  /**
   * Write a boolean property to the provided property map.
   *
   * @param propertyName      the name of the property
   * @param booleanValue      the boolean value
   * @param modulePropertyMap the module property map
   */
  @SuppressWarnings({"WeakerAccess", "unused"})
  protected void writeBoolean(
      String propertyName,
      boolean booleanValue,
      ModulePropertyMap modulePropertyMap
  ) {
    modulePropertyMap.put(propertyName, String.valueOf(booleanValue));
  }
}
