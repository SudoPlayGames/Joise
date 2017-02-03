### Change Log

#### 1.1.0
  * changed project structure to adhere to gradle conventions (#12)
  * deprecated method `calculate()` in ModuleAutoCorrect class (replace with `calculateAll()` method) (#16)
  * added dimension respective calculate methods in `ModuleAutoCorrect` class: `calculate2D()`, `calculate3D()`, `calculate4D()`, `calculate6D()` (#16)
  * added `calculateAll()` method in `ModuleAutoCorrect` class to replace deprecated `calculate()` method (#16)
  * fixed unused constant defaults in `ModuleFractal` class (#17)
  * moved derivative spacing field and related methods from `Module` base class into `ModuleFractal` class since the field and related methods are only ever used for deCarpentierSwiss type fractal noise (#18)
  * added methods in `Module` base class that read a property from a `ModulePropertyMap` and return a default value if the property map doesn't contain the provided key; useful for reading new properties from older maps that may not contain the new properties (#18)
  * moved all module property map accessor methods from the `Module` base class into the `ModulePropertyMap` class; refactored modules accordingly (#18)
  * changed name of `SeedableModule` to `SeededModule` (#18)
  * added method `Module#setSeed(String seedName, long seed)` to replace similar method in `Joise` class
  * added `ModuleChainBuilder` class to build module chains from `ModuleMap`s; replaces similar functionality in `Joise` class
  * removed `Joise` class
  * removed `Assert` utility class
  * moved package `com.sudoplay.util` to `com.sudoplay.joise.util`

#### 1.0.5

  * fixed: 1.0.4 fails to compile on GWT (#15)

#### 1.0.4

  * fixed scalar modules not being recognized as modules
  * implemented ModuleFactoryRegistry to allow custom modules

#### 1.0.3

  * refactored Noise class to remove usage of ByteBuffer and ThreadLocal while retaining thread safety
  * replaced usage of AtomicInteger in Module class with UUID string generator
  * eliminated use of reflection in Module class and Joise class
  * removed unused util methods in Assert class
  * added XML file required to build with GWT

#### 1.0.2

  * added gradle.properties file with dummy vars to fix #7