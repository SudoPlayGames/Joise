---
layout: page
title: Modules
description: A deeper analysis of how to use each module.
permalink: /modules/
---

All of the illustrations below are from examples that can be found in the source in the following location:

```
/src/examples/java/com/sudoplay/joise/examples/
```

These are the core modules of the Joise library. All modules have been ported from <a href="http://accidentalnoise.sourceforge.net/implicit.html" target="_blank">Joshua Tippetts' C++ Accidental Noise Library</a>.

## ModuleBasisFunction

The `ModuleBasisFunction` generates noise using one of five different `BasisTypes` and one of four different `InterpolationTypes`.

The following examples illustrate each basis type with the four differnent interpolation types.

### BasisType.GRADIENT

<img src="/assets/examples/module/basis/gradient.png" class="img-responsive">

Gradient noise is essentially Perlin's original noise function. This implementation of gradient noise will return the value 0 at the lattice points, therefore sampling at integer intervals (ie. 0, 1, 2, 3, etc.) will result in a consistent output of 0. This noise, depending on the application, may suffer from visually significant directional artifacts or grid-oriented artifacts.

### BasisType.GRADVAL

<img src="/assets/examples/module/basis/gradval.png" class="img-responsive">

Gradval noise is an addition of gradient and value noise designed to reduce the visual artifacts apparent in gradient noise. Since both gradient and value noise is bound to the grid, artifacts can still occur.

### BasisType.SIMPLEX

<img src="/assets/examples/module/basis/simplex.png" class="img-responsive">

Simplex noise is a form of Perlin's improved noise function.

For more information, see: [Simplex Noise](https://en.wikipedia.org/wiki/Simplex_noise).

### BasisType.VALUE

<img src="/assets/examples/module/basis/value.png" class="img-responsive">

Value noise creates a lattice of points which are assigned random values, then returns an interpolated value based on the values of the lattice points surrounding the input coordinates.

### BasisType.WHITE

<img src="/assets/examples/module/basis/white.png" class="img-responsive">

White noise generates no coherent pattern whatsoever.

### Example Code

Here is a snippet from the example:

```java
...

  protected Module createModuleBasisFunction(
      ModuleBasisFunction.BasisType basisType,
      ModuleBasisFunction.InterpolationType interpolationType
  ) {

    ModuleBasisFunction moduleBasisFunction = new ModuleBasisFunction();
    moduleBasisFunction.setType(basisType);
    moduleBasisFunction.setInterpolation(interpolationType);
    moduleBasisFunction.setSeed(42);

    /**
     * We auto-correct into the range [0, 1].
     */
    ModuleAutoCorrect moduleAutoCorrect = new ModuleAutoCorrect(0, 1);
    moduleAutoCorrect.setSource(moduleBasisFunction);
    moduleAutoCorrect.setSamples(10000);
    moduleAutoCorrect.calculate2D();

    /**
     * We scale the domain to see more of the noise.
     */
    ModuleScaleDomain moduleScaleDomain = new ModuleScaleDomain();
    moduleScaleDomain.setSource(moduleAutoCorrect);
    moduleScaleDomain.setScaleX(16.0);
    moduleScaleDomain.setScaleY(16.0);

    return moduleScaleDomain;
  }

...
```


## ModuleCellGen

<div class="bs-callout bs-callout-warning">
  <h4>This module can't be sampled directly.</h4>
  The <code>ModuleCellGen</code> acts as input to the <code>ModuleCellular</code> module and therefore can't be sampled directly.
</div>

The ModuleCellGen acts a source for the ModuleCellular module. It can be shared between more than one ModuleCellular and will cache sampled values in order to improve performance.

## ModuleCellular

<img src="/assets/examples/module/cellGen.png" class="img-responsive">

ModuleCellular is used in conjunction with the ModuleCellGen to provide values based on [Worley noise](/assets-3rd-party/Worley_noise).

The parameters `F1`, `F2`, `F3`, and `F4` are coefficients multiplied by the distances to the first, second, third, and fourth closest feature points, respectively. By default, the value for each of these coefficients is zero.

Here is an excerpt from <a href="http://www.rhythmiccanvas.com/research/papers/worley.pdf" target="_blank">Worley's original paper</a>: 

> We can define a new basis function based on the idea of random feature points. Imagine points randomly distributed through all of R3. For any location x, there is some feature point which lies closer to x than any other feature point. Define F1(x) as the distance from x to that closest feature point. As x is varied, F1 varies smoothly since the distance between the sample location and the fixed feature point varies smoothly. However, at certain cusp locations, the point x will be equidistant between two feature points. Here, the value of F1(x) is still well defined, since the value is the same no matter which feature point is chosen to calculate the distance. Varying the position of x will return values of F1 that are still continuous, though the derivative of F1 will change discontinuously as the distance calculation “switches” from one feature point to its neighbor.

> It can be seen that the locations where the function F1 “switches” from one feature point to the next (where its derivative is discontinuous) are along the equidistance planes that separate two points in R3. These planes are exactly the planes that are computed by a Voronoi diagram, which by definition partitions space into cellular regions where all the points within each region are closer to its defining point than any other point.

> The function F2(x) can be defined as the distance between the location x and the feature point which is the second closest to the x. With similar arguments as before, F2 is continuous everywhere, but its derivative is not at those locations where the second-closest point swaps with either the first-closest or third-closest. Similarly, we can define Fn(x) as the distance between x and the nth closest feature point.

### Example Code

Here is a snippet from the example:

```java
...

  @Override
  protected void run(SplitCanvas canvas) {

    ModuleCellGen moduleCellGen = new ModuleCellGen();
    moduleCellGen.setSeed(42);

    canvas.updateImage(
        "F1 = 1", getSource(moduleCellGen, 1, 0, 0, 0),
        "F2 = 1", getSource(moduleCellGen, 0, 1, 0, 0),
        "F1 = 1, F2 = -1", getSource(moduleCellGen, 1, -1, 0, 0),
        "F4 = 1", getSource(moduleCellGen, 0, 0, 0, 1)
    );
  }

  private Module getSource(ModuleCellGen moduleCellGen, int a, int b, int c, int d) {
    ModuleCellular moduleCellular = new ModuleCellular();
    moduleCellular.setCellularSource(moduleCellGen);
    moduleCellular.setCoefficients(a, b, c, d);

    /**
     * We scale the domain to visualize more of the noise.
     */
    ModuleScaleDomain moduleScaleDomain = new ModuleScaleDomain();
    moduleScaleDomain.setSource(moduleCellular);
    moduleScaleDomain.setScaleX(8);
    moduleScaleDomain.setScaleY(8);

    /**
     * We auto-correct into the range [0, 1].
     */
    ModuleAutoCorrect source = new ModuleAutoCorrect(0, 1);
    source.setSource(moduleScaleDomain);
    source.setSamples(10000);
    source.calculate2D();
    return source;
  }

...
```

## ModuleFractal

The fractal module combines up to ten sources, either the default sources or custom sources set by calling the `ModuleFractal#overrideSource(int, Module)` method. The basis type and interpolation type can be set for all or individual default sources by calling the appropriate methods.

### Billow

#### BasisType.GRADIENT

<img src="/assets/examples/module/fractal/gradient/billow.png" class="img-responsive">

#### BasisType.GRADVAL

<img src="/assets/examples/module/fractal/gradval/billow.png" class="img-responsive">

#### BasisType.SIMPLEX

<img src="/assets/examples/module/fractal/simplex/billow.png" class="img-responsive">

#### BasisType.VALUE

<img src="/assets/examples/module/fractal/value/billow.png" class="img-responsive">


### deCarpentierSwiss

#### BasisType.GRADIENT

<img src="/assets/examples/module/fractal/gradient/deCarpentierSwiss.png" class="img-responsive">

#### BasisType.GRADVAL

<img src="/assets/examples/module/fractal/gradval/deCarpentierSwiss.png" class="img-responsive">

#### BasisType.SIMPLEX

<img src="/assets/examples/module/fractal/simplex/deCarpentierSwiss.png" class="img-responsive">

#### BasisType.VALUE

<img src="/assets/examples/module/fractal/value/deCarpentierSwiss.png" class="img-responsive">


### FBM

#### BasisType.GRADIENT

<img src="/assets/examples/module/fractal/gradient/fbm.png" class="img-responsive">

#### BasisType.GRADVAL

<img src="/assets/examples/module/fractal/gradval/fbm.png" class="img-responsive">

#### BasisType.SIMPLEX

<img src="/assets/examples/module/fractal/simplex/fbm.png" class="img-responsive">

#### BasisType.VALUE

<img src="/assets/examples/module/fractal/value/fbm.png" class="img-responsive">


### HybridMulti

#### BasisType.GRADIENT

<img src="/assets/examples/module/fractal/gradient/hybridMulti.png" class="img-responsive">

#### BasisType.GRADVAL

<img src="/assets/examples/module/fractal/gradval/hybridMulti.png" class="img-responsive">

#### BasisType.SIMPLEX

<img src="/assets/examples/module/fractal/simplex/hybridMulti.png" class="img-responsive">

#### BasisType.VALUE

<img src="/assets/examples/module/fractal/value/hybridMulti.png" class="img-responsive">


### Multi

#### BasisType.GRADIENT

<img src="/assets/examples/module/fractal/gradient/multi.png" class="img-responsive">

#### BasisType.GRADVAL

<img src="/assets/examples/module/fractal/gradval/multi.png" class="img-responsive">

#### BasisType.SIMPLEX

<img src="/assets/examples/module/fractal/simplex/multi.png" class="img-responsive">

#### BasisType.VALUE

<img src="/assets/examples/module/fractal/value/multi.png" class="img-responsive">


### RidgeMulti

#### BasisType.GRADIENT

<img src="/assets/examples/module/fractal/gradient/ridgeMulti.png" class="img-responsive">

#### BasisType.GRADVAL

<img src="/assets/examples/module/fractal/gradval/ridgeMulti.png" class="img-responsive">

#### BasisType.SIMPLEX

<img src="/assets/examples/module/fractal/simplex/ridgeMulti.png" class="img-responsive">

#### BasisType.VALUE

<img src="/assets/examples/module/fractal/value/ridgeMulti.png" class="img-responsive">



### BasisType.WHITE

The white noise basis types for `ModuleFractal` have been grouped below for visual comparison.

#### FractalType.BILLOW

<img src="/assets/examples/module/fractal/white/billow.png" class="img-responsive">

#### FractalType.DECARPENTIERSWISS

<img src="/assets/examples/module/fractal/white/deCarpentierSwiss.png" class="img-responsive">

#### FractalType.FBM

<img src="/assets/examples/module/fractal/white/fbm.png" class="img-responsive">

#### FractalType.HYBRIDMULTI

<img src="/assets/examples/module/fractal/white/hybridMulti.png" class="img-responsive">

#### FractalType.MULTI

<img src="/assets/examples/module/fractal/white/multi.png" class="img-responsive">

#### FractalType.RIDGEMULTI

<img src="/assets/examples/module/fractal/white/ridgeMulti.png" class="img-responsive">

### Example Code

Here is a snippet from the example:

```java
...

  @Override
  protected void run(SplitCanvas canvas) {
    BasisType basisType = BasisType.SIMPLEX;
    FractalType fractalType = FractalType.HYBRIDMULTI;

    canvas.updateImage(
        "none", getModule(basisType, InterpolationType.NONE, fractalType),
        "linear", getModule(basisType, InterpolationType.LINEAR, fractalType),
        "cubic", getModule(basisType, InterpolationType.CUBIC, fractalType),
        "quintic", getModule(basisType, InterpolationType.QUINTIC, fractalType)
    );
  }

  private ModuleAutoCorrect getModule(
      BasisType basisType,
      InterpolationType interpolationType,
      FractalType fractalType
  ) {
    ModuleFractal gen = new ModuleFractal();
    gen.setAllSourceBasisTypes(basisType);
    gen.setAllSourceInterpolationTypes(interpolationType);
    gen.setNumOctaves(5);
    gen.setFrequency(2.34);
    gen.setType(fractalType);
    gen.setSeed(42);

    /**
     * We auto-correct into the range [0, 1], performing 10,000 samples 
     * to calculate the auto-correct values.
     */
    ModuleAutoCorrect source = new ModuleAutoCorrect(0, 1);
    source.setSource(gen);
    source.setSamples(10000);
    source.calculate2D();
    return source;
  }

...
```


## ModuleGradient

<img src="/assets/examples/module/gradient.png" class="img-responsive">

The `ModuleGradient` generates a linear gradient with output values ranging from 0 to 1.

### Example Code

Here is a snippet from the example:

```java
...

  @Override
  protected void run(SplitCanvas canvas) {

    canvas.updateImage(
        "(0, 0) -> (0, 1)", getModule(0, 0, 0, 1),
        "(0.5, 0) -> (1, 1)", getModule(0.5, 1, 0, 1)
    );
  }

  private ModuleGradient getModule(double x1, double x2, double y1, double y2) {
    ModuleGradient moduleGradient = new ModuleGradient();
    moduleGradient.setGradient(x1, x2, y1, y2);
    return moduleGradient;
  }

...
```

## ModuleSphere

<img src="/assets/examples/module/sphere.png" class="img-responsive">

The `ModuleSphere` generates a spherical gradient with output values ranging from 0 to 1.

### Example Code

Here is a snippet from the example:

```java
...

  @Override
  protected void run(SplitCanvas canvas) {

    ModuleSphere moduleSphere = new ModuleSphere();
    moduleSphere.setCenterX(0.5);
    moduleSphere.setCenterY(0.5);
    moduleSphere.setRadius(0.5);

    canvas.updateImage(
        "source", moduleSphere
    );
  }

...
```


## Simple Manipulators

Simple manipulators are modules that take a single source and perform a fairly simple mathematical function on the sampled values.

### ModuleAbs

<img src="/assets/examples/module/abs.png" class="img-responsive">

The absolute value module returns the absolute value of the sampled value.

Negative values are displayed in red for clarity.

```
y = |x|
```

#### Example Code

Here is a snippet from the example:

```java
...

  @Override
  protected void run(SplitCanvas canvas) {

    ModuleFractal gen = new ModuleFractal();
    gen.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.SIMPLEX);
    gen.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
    gen.setNumOctaves(5);
    gen.setFrequency(2.34);
    gen.setType(ModuleFractal.FractalType.RIDGEMULTI);
    gen.setSeed(42);

    /**
     * We auto-correct into the range [-1, 1] to ensure we have some negative values 
     * for the abs module.
     */
    ModuleAutoCorrect source = new ModuleAutoCorrect(-1, 1);
    source.setSource(gen);
    source.setSamples(10000);
    source.calculate();

    /**
     * Performs the absolute value function on the noise.
     */
    ModuleAbs moduleAbs = new ModuleAbs();
    moduleAbs.setSource(source);

    canvas.updateImage(
        "source", source,
        "moduleAbs", moduleAbs
    );
  }

...
```

### ModuleClamp

<img src="/assets/examples/module/clamp.png" class="img-responsive">

The clamp module restricts the sampled value to the range `[lo,hi]`.

```
y = min(hi, max(lo, x))
```

#### Example Code

Here is a snippet from the example:

```java
...

  @Override
  protected void run(SplitCanvas canvas) {

    ModuleFractal gen = new ModuleFractal();
    gen.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.SIMPLEX);
    gen.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
    gen.setNumOctaves(5);
    gen.setFrequency(2.34);
    gen.setType(ModuleFractal.FractalType.RIDGEMULTI);
    gen.setSeed(42);

    /**
     * We auto-correct into the range [0, 1], performing 10,000 samples to 
     * calculate the auto-correct values.
     */
    ModuleAutoCorrect source = new ModuleAutoCorrect(0, 1);
    source.setSource(gen);
    source.setSamples(10000);
    source.calculate();

    ModuleClamp moduleClamp = new ModuleClamp();
    moduleClamp.setSource(source);
    moduleClamp.setLow(0.25);
    moduleClamp.setHigh(0.5);

    canvas.updateImage(
        "source", source,
        "moduleClamp", moduleClamp
    );
  }

...
```

### ModuleCos

<img src="/assets/examples/module/cos.png" class="img-responsive">

The cosine module passes the sampled value through a cosine function.

```
y = cos(x)
```

#### Example Code

Here is a snippet from the example:

```java
...

  @Override
  protected void run(SplitCanvas canvas) {

    ModuleFractal gen = new ModuleFractal();
    gen.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.SIMPLEX);
    gen.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
    gen.setNumOctaves(5);
    gen.setFrequency(2.34);
    gen.setType(ModuleFractal.FractalType.RIDGEMULTI);
    gen.setSeed(42);

    /**
     * We auto-correct into the range [0, 1], performing 10,000 samples to 
     * calculate the auto-correct values.
     */
    ModuleAutoCorrect source = new ModuleAutoCorrect(0, 1);
    source.setSource(gen);
    source.setSamples(10000);
    source.calculate();

    ModuleCos moduleCos = new ModuleCos();
    moduleCos.setSource(source);

    canvas.updateImage(
        "source", source,
        "moduleCos", moduleCos
    );
  }

...
```

### ModuleFloor

<img src="/assets/examples/module/floor.png" class="img-responsive">

This module returns the largest integer value equal to or smaller than the value sampled.

In this example, the original source is multiplied by 8, `y = 8x`, before applying the floor function. The final output is auto-corrected back into the range [0,1].

Values above 1 are displayed in blue for clarity.

```
y = floor(x)
```

#### Example Code

Here is a snippet from the example:

```java
...

  @Override
  protected void run(SplitCanvas canvas) {

    ModuleFractal gen = new ModuleFractal();
    gen.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.SIMPLEX);
    gen.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
    gen.setNumOctaves(5);
    gen.setFrequency(2.34);
    gen.setType(ModuleFractal.FractalType.RIDGEMULTI);
    gen.setSeed(42);

    /**
     * We auto-correct into the range [0, 1], performing 10,000 samples to 
     * calculate the auto-correct values.
     */
    ModuleAutoCorrect moduleAutoCorrect = new ModuleAutoCorrect(0, 1);
    moduleAutoCorrect.setSource(gen);
    moduleAutoCorrect.setSamples(10000);
    moduleAutoCorrect.calculate();

    /**
     * Since ModuleFloor returns the largest integer value smaller than the value sampled, 
     * we need to use the ModuleCombiner to multiply the sampled values by a number in 
     * order to have a larger range that contains more possible integer values.
     */
    ModuleCombiner source = new ModuleCombiner();
    source.setType(ModuleCombiner.CombinerType.MULT);
    source.setSource(0, moduleAutoCorrect);
    source.setSource(1, 8.0);

    ModuleFloor moduleFloor = new ModuleFloor();
    moduleFloor.setSource(source);

    /**
     * We auto-correct from the range [0, 8] back into the range of [0, 1] to be 
     * able to visualize the results.
     */
    ModuleAutoCorrect moduleFloorAutoCorrect = new ModuleAutoCorrect();
    moduleFloorAutoCorrect.setSource(moduleFloor);
    moduleFloorAutoCorrect.setSamples(10000);
    moduleFloorAutoCorrect.calculate();

    canvas.updateImage(
        "original source", moduleAutoCorrect,
        "source (x8)", source,
        "moduleFloor (auto-corrected)", moduleFloorAutoCorrect
    );
  }

...
```

### ModuleInvert

<img src="/assets/examples/module/invert.png" class="img-responsive">

The invert module changes positive values to negative and negative values to positive.

Negative values are displayed in red for clarity.

```
y = -x
```

#### Example Code

Here is a snippet from the example:

```java
...

  @Override
  protected void run(SplitCanvas canvas) {

    ModuleFractal gen = new ModuleFractal();
    gen.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.SIMPLEX);
    gen.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
    gen.setNumOctaves(5);
    gen.setFrequency(2.34);
    gen.setType(ModuleFractal.FractalType.RIDGEMULTI);
    gen.setSeed(42);

    /**
     * We auto-correct into the range [0, 1], performing 10,000 samples to 
     * calculate the auto-correct values.
     */
    ModuleAutoCorrect moduleAutoCorrect = new ModuleAutoCorrect(0, 1);
    moduleAutoCorrect.setSource(gen);
    moduleAutoCorrect.setSamples(10000);
    moduleAutoCorrect.calculate();

    /**
     * Inverting brings the range from [0, 1] into [-1, 0]
     */
    ModuleInvert moduleInvert = new ModuleInvert();
    moduleInvert.setSource(moduleAutoCorrect);

    /**
     * Add 1.0 to bring the range from [-1, 0] back to [0, 1]
     */
    ModuleCombiner moduleCombiner = new ModuleCombiner();
    moduleCombiner.setSource(0, moduleInvert);
    moduleCombiner.setSource(1, 1.0);
    moduleCombiner.setType(ModuleCombiner.CombinerType.ADD);

    canvas.updateImage(
        "source", moduleAutoCorrect,
        "invert", moduleInvert,
        "corrected invert", moduleCombiner
    );
  }

...
```

### ModulePow

<img src="/assets/examples/module/pow.png" class="img-responsive">

The pow module raises the sampled value to the power given.

```
y = x^pow
```

#### Example Code

Here is a snippet from the example:

```java
...

  @Override
  protected void run(SplitCanvas canvas) {

    ModuleFractal source = new ModuleFractal();
    source.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.SIMPLEX);
    source.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
    source.setNumOctaves(5);
    source.setFrequency(2.34);
    source.setType(ModuleFractal.FractalType.RIDGEMULTI);
    source.setSeed(42);

    /**
     * We auto-correct into the range [0, 1], performing 10,000 samples to 
     * calculate the auto-correct values.
     */
    ModuleAutoCorrect moduleAutoCorrect = new ModuleAutoCorrect(0, 1);
    moduleAutoCorrect.setSource(source);
    moduleAutoCorrect.setSamples(10000);
    moduleAutoCorrect.calculate();

    ModulePow modulePow = new ModulePow();
    modulePow.setSource(moduleAutoCorrect);
    modulePow.setPower(2.0);

    canvas.updateImage(
        "source", moduleAutoCorrect,
        "pow", modulePow
    );
  }

...
```

### ModuleSin

<img src="/assets/examples/module/sin.png" class="img-responsive">

The sine module passes the sampled value through a sine function.

```
y = sin(x)
```

#### Example Code

Here is a snippet from the example:

```java
...

  @Override
  protected void run(SplitCanvas canvas) {

    ModuleFractal source = new ModuleFractal();
    source.setAllSourceBasisTypes(ModuleBasisFunction.BasisType.SIMPLEX);
    source.setAllSourceInterpolationTypes(ModuleBasisFunction.InterpolationType.CUBIC);
    source.setNumOctaves(5);
    source.setFrequency(2.34);
    source.setType(ModuleFractal.FractalType.RIDGEMULTI);
    source.setSeed(42);

    /**
     * We auto-correct into the range [0, 1], performing 10,000 samples to 
     * calculate the auto-correct values.
     */
    ModuleAutoCorrect moduleAutoCorrect = new ModuleAutoCorrect(0, 1);
    moduleAutoCorrect.setSource(source);
    moduleAutoCorrect.setSamples(10000);
    moduleAutoCorrect.calculate();

    ModuleSin moduleSin = new ModuleSin();
    moduleSin.setSource(moduleAutoCorrect);

    canvas.updateImage(
        "source", moduleAutoCorrect,
        "moduleSin", moduleSin
    );
  }

...
```

## Complex Manipulators

Complex manipulators are modules that take one or more sources and perform mathematical functions on the sampled values that are more complex than the simple manipulators.

### ModuleAutoCorrect

<img src="/assets/examples/module/autoCorrect.png" class="img-responsive">

### ModuleBias

<img src="/assets/examples/module/bias.png" class="img-responsive">

### ModuleBlend

<img src="/assets/examples/module/blend.png" class="img-responsive">

### ModuleBrightContrast

<img src="/assets/examples/module/brightContrast.png" class="img-responsive">

### ModuleCache

<div class="bs-callout bs-callout-danger">
  <h4>// TODO</h4>
</div>

### ModuleCombiner

#### Add

<img src="/assets/examples/module/combiner/add.png" class="img-responsive">

#### Avg

<img src="/assets/examples/module/combiner/avg.png" class="img-responsive">

#### Max

<img src="/assets/examples/module/combiner/max.png" class="img-responsive">

#### Min

<img src="/assets/examples/module/combiner/min.png" class="img-responsive">

#### Mult

<img src="/assets/examples/module/combiner/mult.png" class="img-responsive">

### ModuleFunctionGradient

<img src="/assets/examples/module/functionGradient.png" class="img-responsive">

### ModuleGain

<img src="/assets/examples/module/gain.png" class="img-responsive">

### ModuleMagnitude

<img src="/assets/examples/module/magnitude.png" class="img-responsive">

### ModuleNormalizedCoords

<img src="/assets/examples/module/normalizedCoords.png" class="img-responsive">

### ModuleRotateDomain

<img src="/assets/examples/module/rotateDomain.png" class="img-responsive">

### ModuleSawtooth

<img src="/assets/examples/module/sawtooth.png" class="img-responsive">

### ModuleScaleDomain

<img src="/assets/examples/module/scaleDomain.png" class="img-responsive">

### ModuleScaleOffset

<img src="/assets/examples/module/scaleOffset.png" class="img-responsive">

### ModuleSelect

<img src="/assets/examples/module/select.png" class="img-responsive">

### ModuleTiers

<img src="/assets/examples/module/tiers.png" class="img-responsive">

### ModuleTranslateDomain

<img src="/assets/examples/module/translateDomain.png" class="img-responsive">

### ModuleTriangle

<img src="/assets/examples/module/triangle.png" class="img-responsive">