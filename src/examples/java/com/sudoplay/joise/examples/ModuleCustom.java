package com.sudoplay.joise.examples;

import com.sudoplay.joise.ModuleInstanceMap;
import com.sudoplay.joise.ModuleMap;
import com.sudoplay.joise.ModulePropertyMap;
import com.sudoplay.joise.module.Module;

public class ModuleCustom extends
    Module {

  private static final double DEFAULT_VALUE = 0.5;

  private double value;

  public ModuleCustom() {
    this(DEFAULT_VALUE);
  }

  public ModuleCustom(double value) {
    this.value = value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  @Override
  public double get(double x, double y) {
    return this.value;
  }

  @Override
  public double get(double x, double y, double z) {
    return this.value;
  }

  @Override
  public double get(double x, double y, double z, double w) {
    return this.value;
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    return this.value;
  }

  @Override
  public void setSeed(String seedName, long seed) {
    //
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    modulePropertyMap.writeDouble("value", this.value);
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(
      ModulePropertyMap modulePropertyMap,
      ModuleInstanceMap moduleInstanceMap
  ) {
    this.setValue(modulePropertyMap.readDouble("value"));
    return this;
  }
}
