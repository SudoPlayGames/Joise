package com.sudoplay.joise.examples;

import com.sudoplay.joise.ModuleInstanceMap;
import com.sudoplay.joise.ModuleMap;
import com.sudoplay.joise.ModulePropertyMap;
import com.sudoplay.joise.generator.LCG;
import com.sudoplay.joise.module.Module;
import com.sudoplay.joise.module.SeededModule;

public class ModuleCustomSeeded extends
    SeededModule {

  private static final long DEFAULT_SEED = 42;

  private LCG lcg;

  public ModuleCustomSeeded() {
    this.lcg = new LCG();
    this.setSeed(this.seed);
  }

  @Override
  public void setSeed(long seed) {
    super.setSeed(seed);
    this.lcg.setSeed(seed);
  }

  @Override
  public double get(double x, double y) {
    return this.lcg.get01();
  }

  @Override
  public double get(double x, double y, double z) {
    return this.lcg.get01();
  }

  @Override
  public double get(double x, double y, double z, double w) {
    return this.lcg.get01();
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    return this.lcg.get01();
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    this.writeSeed(modulePropertyMap);
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(
      ModulePropertyMap modulePropertyMap,
      ModuleInstanceMap moduleInstanceMap
  ) {
    this.readSeed(modulePropertyMap);
    return this;
  }
}
