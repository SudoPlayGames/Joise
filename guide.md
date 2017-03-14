---
layout: page
title: Guide
description: An overview of Joise, how to download and use, examples and more.
permalink: /guide/
---

## Download

<p class="lead">Joise v{{ site.joise_version }} provides a few easy ways you can get started:</p>

<div class="row">
  <div class="col-sm-6">
    <p class="lead download">Joise</p>
    <p>Compiled code with sources and JavaDoc.</p>
    <button type="button" class="btn btn-outline btn-default btn-lg">
      <i class="fa fa-download fa-lg" aria-hidden="true"></i>
      Download Joise
    </button>
  </div>
  <div class="col-sm-6">
    <p class="lead download">Source code</p>
    <p>Just the source code.</p>
    <button type="button" class="btn btn-outline btn-default btn-lg">
      <i class="fa fa-download fa-lg" aria-hidden="true"></i>
      Download source
    </button>
  </div>
</div>

<div class="row">
  <div class="col-sm-6">
    <p class="lead download row2">Maven</p>
    <p>Joise is available via Maven Central.</p>
    <pre>
groupId=com.sudoplay.joise
artifactId=joise
version={{ site.joise_version }}</pre>
  </div>
  <div class="col-sm-6">
    <p class="lead download row2">Gradle</p>
    <p>If you're using gradle...</p>
    <pre>
com.sudoplay.joise:joise:{{ site.joise_version }}</pre>
  </div>
</div>

## What's included

<p class="lead">Joise is currently available for download in two forms: precompiled and source.</p>

### Precompiled Joise

  * joise-{{ site.joise_version }}.jar
  * joise-{{ site.joise_version }}-javadoc.jar
  * joise-{{ site.joise_version }}-sources.jar

### Joise source code

Documentation:

  * **README.md** outline of the project and files
  * **CHANGELOG.md** outline of version changes
  * **LICENSE** the license  ([link](https://github.com/SudoPlayGames/Joise/blob/master/LICENSE))

The repo contains the following packages:

  * `com.sudoplay.joise` - classes to work with module maps
  * `com.sudoplay.joise.generator` - classes to generate pseudo-random numbers
  * `com.sudoplay.joise.mapping` - classes to assist in mapping noise to arrays
  * `com.sudoplay.joise.module` - all the noise function modules
  * `com.sudoplay.joise.noise` - the core noise functions
  * `com.sudoplay.joise.util` - common utility classes

## Compiling a .jar

Joise uses Gradle for its build system.

  1. Clone the repository using one of the following methods:
    * SSH `git clone git@github.com:SudoPlayGames/Joise.git` or
    * HTTPS `git clone https://github.com/SudoPlayGames/Joise.git`.
  2. After cloning, navigate to the project folder: `cd Joise`.
  3. Next, checkout the version you want to compile, ie. `git checkout tags/{{ site.joise_version }}`.
    * You can list the version tags using `git tag -l`.
  4. Finally, compile by running `gradlew build`.
    * The compiled source will be located in `<project folder>/build/libs`.

## Sampling

Joise works by chaining stand-alone mathematical functions together to create more complex and unique noise functions. These stand-alone functions are referred to as modules. All of Joise's built-in modules can be found in the `com.sudoplay.joise.module` package.

### Chaining modules

Most modules accept one or more sources. Sources can either be a module or a double value.

```java
ModuleBasisFunction basis = new ModuleBasisFunction();
basis.setType(BasisType.SIMPLEX);
basis.setSeed(42);

ModuleAutoCorrect correct = new ModuleAutoCorrect();
correct.setSource(basis);
correct.calculateAll();

ModuleScaleDomain scaleDomain = new ModuleScaleDomain();
scaleDomain.setSource(correct);
scaleDomain.setScaleX(4.0);
scaleDomain.setScaleY(4.0);
```

### Sampling noise

<p class="lead">Module chains can be sampled in either two, three, four or six dimensions.</p>

Four and six dimensional noise is used for creating seamless two and three dimensional noise, respectively.

```java
lastModuleInChain.get(x, y);
lastModuleInChain.get(x, y, z);
lastModuleInChain.get(x, y, z, w);
lastModuleInChain.get(x, y, z, w, u, v);
```

Here is an example that demonstrates sampling a module chain with two modules:

```java
ModuleBasisFunction basis = new ModuleBasisFunction();
basis.setType(BasisType.SIMPLEX);
basis.setSeed(42);

ModuleAutoCorrect correct = new ModuleAutoCorrect();
correct.setSource(basis);
correct.calculate2D();

for (int x = 0; x < 256; x++) {
  for (int y = 0; y < 256; y++) {
    double sampledValue = correct.get(x / 256.0, y / 256.0);
    // ... do something with sampledValue here
  }
}
```

The loop at the bottom samples 65,536 values. The 256 by 256 square that is sampled is actually a 1 by 1 square, with values ranging from 0 (inclusive) to 1 (exclusive). The range is converted by division on this line:

```java
double sampledValue = correct.get(x / 256.0, y / 256.0);
```

### Sampling an infinite range

<p class="lead">Joise can be used to generate an infinite range of noise.</p>

Joise does not require the sampled coordinate range to be `[0,1)`. As long as the sampled coordinate values are fractional, the range can be anything you want.

Here is an example of sampling coordinates in the range `[0,16)`:

```java
...

for (int x = 0; x < 4096; x++) {
  for (int y = 0; y < 4096; y++) {
    double sampledValue = correct.get(x / 256.0, y / 256.0);
    // ... do something with sampledValue here
  }
}
```

If you are generating a large amount of noise, perhaps a large terrain heightmap, you will most likely encounter performance issues. To work around this limitation, sample just a subsection, or chunk, of noise.

```java
// let's say you're generating in chunks and chunk position (0,0), (0,1), (1,-1), etc.. (X,Y)
float[] generate(int chunkPositionX, int chunkPositionY) {

    // mess with this to get the scale you want
    float noiseScale = 1.0f / 256.0f;

    // chunks are 16x16 tiles
    int chunkSize = 16;

    // convert chunkPositionX and chunkPositionY to a tile offset
    float offsetX = chunkPositionX * chunkSize;
    float offsetY = chunkPositionY * chunkSize;

    // set up the array to hold the noise samples
    float[] result = new float[256];

    float sampleX, sampleY;

    // loop and sample the noise
    for (int x = 0; x < chunkSize; ++x) {
        for (int y = 0; y < chunkSize; ++y) {
            sampleX = (x + offsetX) * noiseScale;
            sampleY = (y + offsetY) * noiseScale;
            result[x + (y * chunkSize)] = joise.get(sampleX, sampleY);
        }
    }

    return result;
}
```

### Sampling seamless noise

<p class="lead">Joise can create noise that can be tiled without visible seams.</p>

To create seamless noise, noise that can tile without seams, you need to double the sample dimensions. Seamless two-dimensional noise requires sampling in four dimensions, just as seamless three-dimensional noise requires sampling in six dimensions.

To understand why higher dimensional noise is required, let's think about one-dimensional noise.

If we were take samples of one-dimensional noise, it might look like this:

![](/assets/one-dimensional-noise.png)

Tiling this noise would look like this:

![](/assets/one-dimensional-noise-tiled.png)

It creates an obvious seam in the middle. To overcome this, instead of sampling one-dimensional noise in a straight line, sample two-dimensional noise in a circle.

![](/assets/one-dimensional-noise-seamless.png)

This guarantees that your line of noise is tileable because it starts where it stops. This is the same for higher dimensional noise like two and three dimensional. Each axis sampled requires two dimensions to be sampled in a circle.

Joise provides some [optional utility classes](#sampling-utilities) that can assist in sampling seamless noise.

### Sampling utilities

<p class="lead">Joise provides optional utilities to assist in sampling and mapping noise.</p>

The following utility classes, found in the `com.sudoplay.joise.mapping` package, are provided to assist in sampling module chains:

#### Core Mapping Classes

| Class | Description |
| --- | --- |
| Mapping | This is the core mapping class. It contains static mapping methods for 2D and 3D noise. |
| MappingMode | This is an enumeration of the different mapping modes, which is passed into method calls on the Mapping class. |
| MappingRange | This defines the noise sampling bounds, passed into method calls on the Mapping class. This is the range used for sampling the noise, not the size of the final output. |
| Array2Double | This is a one-dimensional array wrapper for 2D noise. It contains convenience methods for reading and writing 2D noise in a one-dimensional array. |
| Array3Double | This is a one-dimensional array wrapper for 3D noise. It contains convenience methods for reading and writing 3D noise in a one-dimensional array. |

#### Interfaces

| Class | Description |
| --- | --- |
| IMapping2DWriter | This interface is responsible for writing the sampled 2D noise. An implementation must be passed into method calls on the Mapping class. |
| IMapping3DWriter | This interface is responsible for writing the sampled 3D noise. An implementation must be passed into method calls on the Mapping class. |
| IMappingUpdateListener | The update method in this interface is called each time a value is sampled. It is useful for status updates. |

#### Default Interface Implementations

| Class | Description |
| --- | --- |
| Array2DoubleWriter | This is a default implementation of the IMapping2DWriter interface that writes sampled noise to an Array2Double. |
| Array3DoubleWriter | This is a default implementation of the IMapping3DWriter interface that writes sampled noise to an Array3Double |
  
<div class="bs-callout bs-callout-warning">
  <h4>The Mapping class may sample in more dimensions than expected.</h4>
  If you use an auto-correction module in your module chain, make sure to call the calculate method that corresponds to the number of dimensions that the mapper will use.
</div>

#### Mapping#map2D

The following table describes the differences between mapping modes for `Mapping#map2D`.

| MappingMode  | Dimensions | Description |
| ------------ |:----------:| --- |
| NORMAL       | 3          | This mode will sample in three dimensions, on the X and Y axes, with the provided fixed value for Z. The resulting noise will not be seamless. |
| SEAMLESS_X   | 4          | This mode will sample in four dimensions, on the X and Y axes in a circle and on the Z axis, with the provided fixed value for W. The resulting noise will be seamless on the X axis. |
| SEAMLESS_Y   | 4          | This mode will sample in four dimensions, on the X axis and on the Y and Z axes in a circle, with the provided fixed value for W. The resulting noise will be seamless on the Y axis. |
| SEAMLESS_Z   | 4          | This mode will sample in four dimensions, on the X and Y axes and on the Z and W axes in a cirlce. The provided fixed value is used to select the Z axis slice. The resulting noise will be seamless on the Z axis. |
| SEAMLESS_XY  | 6          | This mode will sample in six dimensions, on the X and Y axes in a circle and on the Z and W axes in a circle, with the provided fixed value for U. The resulting noise will be seamless on the X and Y axes. |
| SEAMLESS_XZ  | 6          | This mode will sample in six dimensions, on the X and Y axes in a circle, on the Z axis, and on the W and U axes in a circle. The provided fixed value is used to select the Z axis slice. The resulting noise will be seamless on the X and Z axes. |
| SEAMLESS_YZ  | 6          | This mode will sample in six dimensions, on the X axis, on the Y and Z axes in a circle, and on the W and U axes in a circle. The provided fixed value is used to select the Z axis slice. The resulting noise will be seamless on the Y and Z axes. |
| SEAMLESS_XYZ | 6          | This mode will sample in six dimensions, on the X and Y axes in a circle, on the Z and W axes in a circle, and on the U and V axes in a circle. The provided fixed value is used to select the Z axis slice. The resulting noise will be seamless on the X, Y and Z axes. |

#### Example

Here is a snippet illustrating how to use the Mapping class:

```java
...

int width = 640;
int height = 480;

Array2Double data = new Array2Double(width, height);
Array2DoubleWriter writer = new Array2DoubleWriter(data);

// map2D samples in 3D with a fixed z value
Mapping.map2D(
    MappingMode.NORMAL, // the mapping mode
    width, // the width of the final output
    height, // the height of the final output
    module, // the module to sample
    MappingRange.DEFAULT, // the range to sample from the noise
    writer, // the IMapping2DWriter implementation
    IMappingUpdateListener.NULL_LISTENER, // the IMappingUpdateListener implementation
    0.5 // fixed Z value
);

// do something with the resulting data object here ...

...
```

This example uses the `Mapping` class to sample noise in the range [-1,1] and map it to an array in the range [640,480]. The method called samples the noise in 3D and uses the last parameter, the 0.5 double, to indicate where to slice a 2D cross section out of the 3D noise. This value could, for example, be incremented in each frame of an animation to essentially move the 2D slice through the 3D noise, animating the noise in a visually coherent fashion.

#### Mapping#map2DNoZ

The `map2DNoZ` method is almost the same as the `map2D` method above, except it does not accept a parameter for a fixed Z value.

The following table describes the differences between mapping modes for `Mapping#map2DNoZ`.

| MappingMode  | Dimensions | Description |
| ------------ |:----------:| --- |
| NORMAL       | 2          | This mode will sample in two dimensions, on the X and Y axes. The resulting noise will not be seamless. |
| SEAMLESS_X   | 3          | This mode will sample in three dimensions, on the X and Y axes in a circle and on the Z axis. The resulting noise will be seamless on the X axis. |
| SEAMLESS_Y   | 3          | This mode will sample in three dimensions, on the X axis and on the Y and Z axes in a circle. The resulting noise will be seamless on the Y axis. |
| SEAMLESS_XY  | 4          | This mode will sample in four dimensions, on the X and Y axes in a circle and on the Z and W axes in a circle. The resulting noise will be seamless on the X and Y axes. |

#### Mapping#map3D

The following table describes the differences between mapping modes for `Mapping#map3D`.

| MappingMode  | Dimensions | Description |
| ------------ |:----------:| --- |
| NORMAL       | 3          | This mode will sample in three dimensions, on the X, Y and Z axes. The resulting noise will not be seamless. |
| SEAMLESS_X   | 4          | This mode will sample in four dimensions, on the X and Y axes in a circle and on the Z and W axes. The resulting noise will be seamless on the X axis. |
| SEAMLESS_Y   | 4          | This mode will sample in four dimensions, on the X axis, on the Y and Z axes in a circle, and on the W axis. The resulting noise will be seamless on the Y axis. |
| SEAMLESS_Z   | 4          | This mode will sample in four dimensions, on the X and Y axes and on the Z and W axes in a cirlce. The resulting noise will be seamless on the Z axis. |
| SEAMLESS_XY  | 6          | This mode will sample in six dimensions, on the X and Y axes in a circle, on the Z and W axes in a circle, and on the U axis. The resulting noise will be seamless on the X and Y axes. |
| SEAMLESS_XZ  | 6          | This mode will sample in six dimensions, on the X and Y axes in a circle, on the Z axis, and on the W and U axes in a circle. The resulting noise will be seamless on the X and Z axes. |
| SEAMLESS_YZ  | 6          | This mode will sample in six dimensions, on the X axis, on the Y and Z axes in a circle, and on the W and U axes in a circle. The resulting noise will be seamless on the Y and Z axes. |
| SEAMLESS_XYZ | 6          | This mode will sample in six dimensions, on the X and Y axes in a circle, on the Z and W axes in a circle, and on the U and V axes in a circle. The resulting noise will be seamless on the X, Y and Z axes. |

### Multi-threaded sampling

<div class="bs-callout bs-callout-warning">
  <h4>Joise is not thread safe!</h4>
  A module chain that is sampled from multiple threads concurrently will produce errors.
</div>

If you create a module chain and sample that chain from multiple threads concurrently, the output will not be accurate. This is due to how some Joise classes, specifically the PRNGs, `ModuleCache` and `ModuleCellGen`, use mutable state in their calculations.

To use Joise in a multi-threaded application, use separate instances of your module chain in each thread that will be sampling it.

## Exporting / importing module chains

<div class="bs-callout bs-callout-success">
  <h4>Joise provides tools to assist with serializing module chains.</h4>
  Loading and deserializing module chains from JSON, for example, can provide modders with tools to create custom noise for your application.
</div>

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

Custom modules, modules that you've created that aren't packaged with Joise, need to have an implementation of `IModuleFactory` registered with the instance of `ModuleChainBuilder` before calling `build(ModuleMap)`. The `IModuleFactory` simply creates a new instance of the module.

```java
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
```

## Creating custom modules

<div class="bs-callout bs-callout-success">
  <h4>Joise is designed to be extensible, allowing creation of custom modules.</h4>
  Have a cool idea for a module that Joise doesn't provide? Use these examples to help implement it!
</div>

All modules extend one of three classes: `Module`, `SourcedModule`, or `SeededModule`.

### Module

<p class="lead">Module is the base class which all modules extend.</p>

Here is an example of a module that simply returns a predetermined value for all `get` calls:

```java
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
```
#### Breakdown

```java
@Override
public void setSeed(String seedName, long seed) {
  //
}
```

This method is used to do two things: set the module's seed if its seed name matches and pass the call up the chain to any sources the module may have. Here it doesn't do anything because this module doesn't have a seed, nor does it have any source modules.

```java
@Override
public void writeToMap(ModuleMap moduleMap) {
  ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
  modulePropertyMap.writeDouble("value", this.value);
  moduleMap.put(this.getId(), modulePropertyMap);
}
```

A `ModulePropertyMap` defines a module in a `ModuleMap`, which defines a module chain. This method is used to create a new `ModulePropertyMap`, write values to the property map, then add the property map to the provided `ModuleMap`.

```java
@Override
public Module buildFromPropertyMap(
    ModulePropertyMap modulePropertyMap, 
    ModuleInstanceMap moduleInstanceMap
) {
  this.setValue(modulePropertyMap.readDouble("value"));
  return this;
}
```

This method is used to read values from the provided `ModulePropertyMap` and `ModuleInstanceMap`.

### SourcedModule

<p class="lead">SourcedModule is a convenience class for subclasses that require only one source.</p>

Here is the `ModuleAbs` class as an example of a simple `SourcedModule`:

```java
package com.sudoplay.joise.module;

import com.sudoplay.joise.ModuleInstanceMap;
import com.sudoplay.joise.ModuleMap;
import com.sudoplay.joise.ModulePropertyMap;

public class ModuleAbs extends
    SourcedModule {

  @Override
  public double get(double x, double y) {
    return Math.abs(this.source.get(x, y));
  }

  @Override
  public double get(double x, double y, double z) {
    return Math.abs(this.source.get(x, y, z));
  }

  @Override
  public double get(double x, double y, double z, double w) {
    return Math.abs(this.source.get(x, y, z, w));
  }

  @Override
  public double get(double x, double y, double z, double w, double u, double v) {
    return Math.abs(this.source.get(x, y, z, w, u, v));
  }

  @Override
  public void writeToMap(ModuleMap moduleMap) {
    ModulePropertyMap modulePropertyMap = new ModulePropertyMap(this);
    modulePropertyMap.writeScalar("source", this.source, moduleMap);
    moduleMap.put(this.getId(), modulePropertyMap);
  }

  @Override
  public Module buildFromPropertyMap(
      ModulePropertyMap props, 
      ModuleInstanceMap moduleInstanceMap
  ) {
    this.setSource(props.readScalar("source", moduleInstanceMap));
    return this;
  }
}
```

The `SourcedModule` class overrides the `setSeed(String, long)` method so it doesn't need to be overridden in subclasses unless the subclass has more than one source. If the subclass had more than one source, be sure to override the method and call each source's `setSeed(String, long)` method in this overridden method:

```java
@Override
public void setSeed(String seedName, long seed) {
  super.setSeed(seedName, seed);
  this.source2.setSeed(seedName, seed);
  this.source3.setSeed(seedName, seed);
}
```

### SeededModule

<p class="lead">SeededModule is the base class for all modules that require a seed.</p>

The following is an example class to demonstrate a `SeededModule` subclass:

```java
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
```

The `SeededModule` class contains two convenience methods: 

```java
protected void readSeed(ModulePropertyMap modulePropertyMap);
protected void writeSeed(ModulePropertyMap prmodulePropertyMapops);
```

These methods handle reading and writing both the seed and seed name.

## Contributing to Joise

Before making major changes to Joise, please discuss the proposed changes with us first. If the proposed changes are accepted, you may contribute to Joise via a [pull request](https://help.github.com/articles/creating-a-pull-request/) (PR).

### Submitting a pull request

Here are a few things to keep in mind when submitting a PR:

  * A PR should be focused on content, meaning a PR in which the majority of the changes are syntax changes will not be accepted.
  * Ensure that your change isn't already being developed, or hasn't been previously rejected.
  * Ensure that your changes are on the `develop` branch.
  * Ensure that your changes are compatible with [GWT's subset of the JRE](http://www.gwtproject.org/doc/latest/RefJreEmulation.html).
  * Use the file you are editing as a style guide.

### Getting started

  1.  Fork the Joise repository.
  2.  Clone the fork using either:
    * SSH `git clone git@github.com:<your username>/Joise.git` or
    * HTTPS `git clone https://github.com/<your username>/Joise.git`.
  3.  Checkout the `develop` branch using `git checkout develop`.
  4.  Apply your changes.
  5.  Add your changes to git using `git add -A`.
  6.  Commit your changes using `git commit -m "<summary of changes>"`.
  7.  Push to your fork using `git push`
  8.  Create a pull request on GitHub.
  9.  Wait for review.
  10.  Squash commits for a clean history.
