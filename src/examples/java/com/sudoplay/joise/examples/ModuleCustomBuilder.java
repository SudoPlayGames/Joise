package com.sudoplay.joise.examples;

import com.sudoplay.joise.IModuleFactory;
import com.sudoplay.joise.ModuleChainBuilder;
import com.sudoplay.joise.ModuleMap;
import com.sudoplay.joise.module.Module;

public class ModuleCustomBuilder {

  public static void main(String... args) {

    ModuleCustom moduleCustom = new ModuleCustom();

    ModuleMap moduleMap = moduleCustom.getModuleMap();

    ModuleChainBuilder moduleChainBuilder = new ModuleChainBuilder();
    moduleChainBuilder.registerModuleFactory(ModuleCustom.class, new IModuleFactory<ModuleCustom>() {
      @Override
      public ModuleCustom create() {
        return new ModuleCustom();
      }
    });

    Module newModule = moduleChainBuilder.build(moduleMap);
  }
}
