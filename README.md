## Joise
Joise is a 2D, 3D, 4D and 6D modular noise library written in Java.

Joise is derived from Joshua Tippetts' [Accidental Noise Library](http://accidentalnoise.sourceforge.net/index.html) written in C++.

### Why Joise?
Joise was created to facilitate the creation of complex noise functions from chained modules, and 
represent the chained modules in a consistent format.

Joise was originally created to power the procedural world-building algorithms behind the PC game, 
[Lodestar: Stygian Skies](https://lodestargame.com/home). 

### What is in this repository?
Documentation:
* **README.md** this file
* **LICENSE** the license  ([link](https://github.com/codetaylor/Juple/blob/master/LICENSE))

The repo contains the following packages:
* `com.sudoplay.joise` contains classes to work with module maps.
* `com.sudoplay.joise.generator` contains classes to generate pseudo-random numbers.
* `com.sudoplay.joise.mapping` contains classes assist in mapping noise to arrays.
* `com.sudoplay.joise.module` contains all the noise function modules.
* `com.sudoplay.joise.noise` contains the core noise functions.
* `com.sudoplay.util` contains common utility classes.

The following packages are provided separately to reduce dependencies:
* [JoisePlugin-TMLConverter](https://github.com/codetaylor/JoisePlugin-TMLConverter) converts module chains to and from TML using [Juple](https://github.com/codetaylor/Juple).
* [JoisePlugin-JSONConverter](https://github.com/codetaylor/JoisePlugin-JSONConverter) converts module chains to and from JSON using [Gson](https://code.google.com/p/google-gson/).

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
Module chains can be converted to and from a `ModuleMap`. This is convenient for serializing module chains, as serializers have a consistent format to rely on.
```java
// convert to a ModuleMap
ModuleMap moduleMap = lastModuleInChain.getModuleMap();
// convert from a ModuleMap
Joise joise = new Joise(moduleMap);
// sample the resulting chain
joise.get(x, y, z);
```
###Naming Seeds
Seeds can be named. This is convenient if you want to load a module chain from an external format, and set seeds programatically.
```java
ModuleBasisFunction basis = new ModuleBasisFunction();
basis.setSeedName("worldseed");
ModuleMap moduleMap = basis.getModuleMap();

// ... save the module map to some external format ...

// ... load the module map back in later ...

Joise joise = new Joise(loadedModuleMap);
if (joise.hasSeed("worldseed")) {
  joise.setSeed("worldseed", 42);
}
```
##License

Copyright (C) 2013 Jason Taylor. Released as open-source under [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
