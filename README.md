![Image](http://www.sudoplaygames.com/lss/screen-01.png)

## Joise
Joise is a 2D, 3D, 4D and 6D modular noise library written in Java.

Joise is derived from Joshua Tippetts' [Accidental Noise Library](http://accidentalnoise.sourceforge.net/index.html) written in C++.

### Maven:

```
groupId=com.sudoplay.joise
artifactId=joise
version=1.1.0
```

### Why Joise?
Joise was created to facilitate the creation of complex noise functions from chained modules, and
represent the chained modules in a flat format convenient for text serialization.

Joise was originally created to power the procedural world-building algorithms behind the PC game,
[Lodestar: Stygian Skies](http://lodestargame.com).

### What is in this repository?
Documentation:
* **README.md** this file
* **LICENSE** the license  ([link](https://github.com/SudoPlayGames/Joise/blob/master/LICENSE))

The repo contains the following packages:
* `com.sudoplay.joise` - classes to work with module maps
* `com.sudoplay.joise.generator` - classes to generate pseudo-random numbers
* `com.sudoplay.joise.mapping` - classes to assist in mapping noise to arrays
* `com.sudoplay.joise.module` - all the noise function modules
* `com.sudoplay.joise.noise` - the core noise functions
* `com.sudoplay.joise.util` - common utility classes

The following packages are provided separately to reduce dependencies:
* [JoisePlugin-TMLConverter](https://github.com/codetaylor/JoisePlugin-TMLConverter) converts module chains to and from TML using [Juple](https://github.com/codetaylor/Juple).
* [JoisePlugin-JSONConverter](https://github.com/codetaylor/JoisePlugin-JSONConverter) converts module chains to and from JSON using [Gson](https://code.google.com/p/google-gson/).

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
  * added `ModuleChainBuilder` class to build module chains from `ModuleMap`; replaces similar functionality in `Joise` class
  * removed `Joise` class
  * removed `Assert` utility class
  * moved package `com.sudoplay.util` to `com.sudoplay.joise.util`
  * moved MAX_SOURCES constant out of `Module` class; separated into two constants, one in `ModuleCombiner` and `ModuleFractal`
  * fixed ModuleFractal#calcWeights(FractalType) is only called when the FractalType is set (#23)
  * fixed ModuleBasisFunction rotation axis and angle are not written to or read from the ModuleMap (#22)
  * removed unused scale and offset fields and associated method from `ModuleBasisFunction` class; fixes #20
  * removed old util package from gwt xml
  * fixed can't set seed name with ModuleFractal (#25)
  * added more examples
  * changed interface naming convention: all interfaces are now prefixed with I to indicate interface

[View changelog for all versions](CHANGELOG.md)

## Examples

###Chaining Modules
Most modules accept one or more sources. Sources can either be a module or a double value.
```java
ModuleBasisFunction basis = new ModuleBasisFunction();
basis.setType(BasisType.SIMPLEX);
basis.setSeed(42);

ModuleAutoCorrect correct = new ModuleAutoCorrect();
correct.setSource(basis);
correct.calculate();

ModuleScaleDomain scaleDomain = new ModuleScaleDomain();
scaleDomain.setSource(correct);
scaleDomain.setScaleX(4.0);
scaleDomain.setScaleY(4.0);
```
###Sampling Chained Modules
A module chain can be sampled in either two, three, four or six dimensions. Four and six dimensional noise is used for creating seamless two and three dimensional noise, respectively.
```java
lastModuleInChain.get(x, y);
lastModuleInChain.get(x, y, z);
lastModuleInChain.get(x, y, z, w);
lastModuleInChain.get(x, y, z, w, u, v);
```
###Exporting a Module Chain
Module chains can be converted to and from a `ModuleMap`. This is convenient for serializing module chains because converting a chain to a `ModuleMap` will flatten the module chain tree.
```java
// convert to a ModuleMap
ModuleMap moduleMap = lastModuleInChain.getModuleMap();
// convert from a ModuleMap
Module module = new ModuleChainBuilder().build(moduleMap);
// sample the resulting chain
module.get(x, y, z);
```
###Naming Seeds
Seeds can be named. This is convenient if you want to load a module chain from an external format, and set seeds programatically.
```java
ModuleBasisFunction basis = new ModuleBasisFunction();
basis.setSeedName("worldseed");
ModuleMap moduleMap = basis.getModuleMap();

// ... save the module map to some external format ...

// ... load the module map back in later ...

Module module = new ModuleChainBuilder().build(loadedModuleMap);
module.setSeed("worldseed", 42);
```
##License

Copyright (C) 2016 Jason Taylor. Released as open-source under [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
