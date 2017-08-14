package com.sudoplay.joise;

import com.sudoplay.joise.module.*;

import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class ModuleChainBuilder {

  private ModuleFactoryRegistry moduleFactoryRegistry;

  @SuppressWarnings("WeakerAccess")
  public ModuleChainBuilder() {
    this.moduleFactoryRegistry = new ModuleFactoryRegistry();

    this.moduleFactoryRegistry.register(ModuleAbs.class, IModuleFactory.MODULE_ABS_FACTORY);
    this.moduleFactoryRegistry.register(ModuleAutoCorrect.class, IModuleFactory.MODULE_AUTO_CORRECT_FACTORY);
    this.moduleFactoryRegistry.register(ModuleBasisFunction.class, IModuleFactory.MODULE_BASIS_FUNCTION_FACTORY);
    this.moduleFactoryRegistry.register(ModuleBias.class, IModuleFactory.MODULE_BIAS_FACTORY);
    this.moduleFactoryRegistry.register(ModuleBlend.class, IModuleFactory.MODULE_BLEND_FACTORY);
    this.moduleFactoryRegistry.register(ModuleBrightContrast.class, IModuleFactory.MODULE_BRIGHT_CONTRAST_FACTORY);
    this.moduleFactoryRegistry.register(ModuleCache.class, IModuleFactory.MODULE_CACHE_FACTORY);
    this.moduleFactoryRegistry.register(ModuleCellGen.class, IModuleFactory.MODULE_CELL_GEN_FACTORY);
    this.moduleFactoryRegistry.register(ModuleCellular.class, IModuleFactory.MODULE_CELLULAR_FACTORY);
    this.moduleFactoryRegistry.register(ModuleClamp.class, IModuleFactory.MODULE_CLAMP_FACTORY);
    this.moduleFactoryRegistry.register(ModuleCombiner.class, IModuleFactory.MODULE_COMBINER_FACTORY);
    this.moduleFactoryRegistry.register(ModuleCos.class, IModuleFactory.MODULE_COS_FACTORY);
    this.moduleFactoryRegistry.register(ModuleFloor.class, IModuleFactory.MODULE_FLOOR_FACTORY);
    this.moduleFactoryRegistry.register(ModuleFractal.class, IModuleFactory.MODULE_FRACTAL_FACTORY);
    this.moduleFactoryRegistry.register(ModuleFunctionGradient.class, IModuleFactory.MODULE_FUNCTION_GRADIENT_FACTORY);
    this.moduleFactoryRegistry.register(ModuleGain.class, IModuleFactory.MODULE_GAIN_FACTORY);
    this.moduleFactoryRegistry.register(ModuleGradient.class, IModuleFactory.MODULE_GRADIENT_FACTORY);
    this.moduleFactoryRegistry.register(ModuleInvert.class, IModuleFactory.MODULE_INVERT_FACTORY);
    this.moduleFactoryRegistry.register(ModuleMagnitude.class, IModuleFactory.MODULE_MAGNITUDE_FACTORY);
    this.moduleFactoryRegistry.register(ModuleNormalizedCoords.class, IModuleFactory.MODULE_NORMALIZED_COORDS_FACTORY);
    this.moduleFactoryRegistry.register(ModulePow.class, IModuleFactory.MODULE_POW_FACTORY);
    this.moduleFactoryRegistry.register(ModuleRotateDomain.class, IModuleFactory.MODULE_ROTATE_DOMAIN_FACTORY);
    this.moduleFactoryRegistry.register(ModuleSawtooth.class, IModuleFactory.MODULE_SAWTOOTH_FACTORY);
    this.moduleFactoryRegistry.register(ModuleScaleDomain.class, IModuleFactory.MODULE_SCALE_DOMAIN_FACTORY);
    this.moduleFactoryRegistry.register(ModuleScaleOffset.class, IModuleFactory.MODULE_SCALE_OFFSET_FACTORY);
    this.moduleFactoryRegistry.register(ModuleSelect.class, IModuleFactory.MODULE_SELECT_FACTORY);
    this.moduleFactoryRegistry.register(ModuleSin.class, IModuleFactory.MODULE_SIN_FACTORY);
    this.moduleFactoryRegistry.register(ModuleSphere.class, IModuleFactory.MODULE_SPHERE_FACTORY);
    this.moduleFactoryRegistry.register(ModuleTiers.class, IModuleFactory.MODULE_TIERS_FACTORY);
    this.moduleFactoryRegistry.register(ModuleTranslateDomain.class, IModuleFactory.MODULE_TRANSLATE_DOMAIN_FACTORY);
    this.moduleFactoryRegistry.register(ModuleTriangle.class, IModuleFactory.MODULE_TRIANGLE_FACTORY);
  }

  /**
   * Use this to register any custom modules.
   * <p>
   * Custom modules must be registered before converting from a {@link ModuleMap}.
   *
   * @param moduleClass   the module class
   * @param moduleFactory the module factory
   * @param <M>           extends {@link Module}
   */
  @SuppressWarnings("unused")
  public <M extends Module> ModuleChainBuilder registerModuleFactory(
      Class<M> moduleClass,
      IModuleFactory<M> moduleFactory
  ) {
    this.moduleFactoryRegistry.register(moduleClass, moduleFactory);
    return this;
  }

  /**
   * Build a module chain from the provided {@link ModuleMap}.
   *
   * @param moduleMap the module map
   * @return the last module in the chain
   */
  @SuppressWarnings("WeakerAccess")
  public Module build(ModuleMap moduleMap) {
    return this.build(moduleMap, this.moduleFactoryRegistry);
  }

  /**
   * Builds a module chain from the provided {@link ModuleMap}.
   *
   * @param moduleMap             the module map
   * @param moduleFactoryRegistry the module factory registry
   * @return the last module in the chain
   */
  private Module build(
      ModuleMap moduleMap,
      ModuleFactoryRegistry moduleFactoryRegistry
  ) {
    ModuleInstanceMap moduleInstanceMap;
    Iterator<Map.Entry<String, ModulePropertyMap>> entryIterator;
    Module module;
    Map.Entry<String, ModulePropertyMap> entry;
    ModulePropertyMap modulePropertyMap;
    String moduleName;

    try {
      moduleInstanceMap = new ModuleInstanceMap();
      entryIterator = moduleMap.mapIterator();
      module = null;

      while (entryIterator.hasNext()) {
        entry = entryIterator.next();
        modulePropertyMap = entry.getValue();
        moduleName = modulePropertyMap.get("module").toString();
        module = moduleFactoryRegistry.get(moduleName).create();
        module.buildFromPropertyMap(modulePropertyMap, moduleInstanceMap);
        moduleInstanceMap.put(entry.getKey(), module);
      }
      return module;

    } catch (Exception e) {
      throw new JoiseException(e);
    }
  }
}
