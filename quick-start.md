---
layout: page
title: Quick Start
permalink: /quick-start/
---

Joise works by chaining stand-alone mathematical functions together to create more complex and unique noise functions. These stand-alone functions are referred to as modules.


## Chaining Modules

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


## Sampling Chained Modules

A module chain can be sampled in either two, three, four or six dimensions. Four and six dimensional noise is used for creating seamless two and three dimensional noise, respectively.

```java
lastModuleInChain.get(x, y);
lastModuleInChain.get(x, y, z);
lastModuleInChain.get(x, y, z, w);
lastModuleInChain.get(x, y, z, w, u, v);
```


## Exporting a Module Chain

Module chains can be converted to a `ModuleMap` by calling `ModuleMap#getModuleMap()` on the last module in the chain. Convert the module map back to a module again by calling `ModuleChainBuilder#build(ModuleMap)`.

It is advised to first convert a module chain to a `ModuleMap` and then serialize the module map as opposed to serializing the module chain directly.

```java
// convert to a ModuleMap
ModuleMap moduleMap = lastModuleInChain.getModuleMap();

// convert from a ModuleMap
Module module = new ModuleChainBuilder().build(moduleMap);

// sample the resulting chain
module.get(x, y, z);
```


## Naming Seeds

Seeds can be named by calling `SeededModule#setSeedName(String)`. This is convenient if you want to load a module chain from an external format, and set seeds programatically.

```java
ModuleBasisFunction basis = new ModuleBasisFunction();
basis.setSeedName("worldseed");
ModuleMap moduleMap = basis.getModuleMap();

// ... save the module map to some external format ...

// ... load the module map back in later ...

Module module = new ModuleChainBuilder().build(loadedModuleMap);
module.setSeed("worldseed", 42);
```