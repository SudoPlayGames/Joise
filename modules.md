---
layout: page
title: Modules
description: In-depth analysis of how each module works.
permalink: /modules/
---

All of the illustrations below are from examples that can be found in the source in the following location:

```
/src/examples/java/com/sudoplay/joise/examples/
```

These are the core modules of the Joise library. All modules have been ported from <a href="http://accidentalnoise.sourceforge.net/implicit.html" target="_blank">Joshua Tippetts' C++ Accidental Noise Library</a>.

## ModuleBasisFunction

The `ModuleBasisFunction` generates noise using one of five different `BasisTypes` and one of four different `InterpolationTypes`.

These examples illustrate each basis type with the four differnent interpolation types.

### BasisType.GRADIENT

Gradient noise is essentially Perlin's original noise function. This implementation of gradient noise will return the value 0 at the lattice points, therefore sampling at integer intervals (ie. 0, 1, 2, 3, etc.) will result in a consistent output of 0. This noise, depending on the application, may suffer from visually significant directional artifacts or grid-oriented artifacts.

<img src="/assets/examples/module/basis/gradient.png" class="img-responsive">

### BasisType.GRADVAL

Gradval noise is an addition of gradient and value noise designed to reduce the visual artifacts apparent in gradient noise. Since both gradient and value noise is bound to the grid, artifacts can still occur.

<img src="/assets/examples/module/basis/gradval.png" class="img-responsive">

### BasisType.SIMPLEX

Simplex noise is a form of Perlin's improved noise function.

This module ignores the interpolation parameter, therefore there is no resulting difference between the four interpolation types.

For more information, see: [Simplex Noise](https://en.wikipedia.org/wiki/Simplex_noise).

<img src="/assets/examples/module/basis/simplex.png" class="img-responsive">

### BasisType.VALUE

Value noise creates a lattice of points which are assigned random values, then returns an interpolated value based on the values of the lattice points surrounding the input coordinates.

<img src="/assets/examples/module/basis/value.png" class="img-responsive">

### BasisType.WHITE

White noise generates no coherent pattern whatsoever.

<img src="/assets/examples/module/basis/white.png" class="img-responsive">

## ModuleCellGen

<div class="bs-callout bs-callout-warning">
  <h4>This module can't be sampled directly.</h4>
  The <code>ModuleCellGen</code> acts as input to the <code>ModuleCellular</code> module and therefore can't be sampled directly.
</div>

The ModuleCellGen acts a source for the ModuleCellular module. It can be shared between more than one ModuleCellular and will cache sampled values in order to improve performance.

This module is derived from <a href="http://accidentalnoise.sourceforge.net/implicit.html#CellularGenerator" target="_blank">CCellularGenerator</a>.

## ModuleCellular

ModuleCellular is used in conjunction with the ModuleCellGen to provide values based on [Worley noise](https://en.wikipedia.org/wiki/Worley_noise).

This module is derived from <a href="http://accidentalnoise.sourceforge.net/implicit.html#Cellular" target="_blank">CImplicitCellular</a>.

<img src="/assets/examples/module/cellGen.png" class="img-responsive">

## ModuleFractal

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


## ModuleGradient

<img src="/assets/examples/module/gradient.png" class="img-responsive">

## ModuleSphere

<img src="/assets/examples/module/sphere.png" class="img-responsive">


## Simple Manipulators

Simple manipulators are modules that take a single source and perform a fairly simple mathematical function on the sampled values.

### ModuleAbs

<img src="/assets/examples/module/abs.png" class="img-responsive">

### ModuleClamp

<img src="/assets/examples/module/clamp.png" class="img-responsive">

### ModuleCos

<img src="/assets/examples/module/cos.png" class="img-responsive">

### ModuleFloor

<img src="/assets/examples/module/floor.png" class="img-responsive">

### ModuleInvert

<img src="/assets/examples/module/invert.png" class="img-responsive">

### ModulePow

<img src="/assets/examples/module/pow.png" class="img-responsive">

### ModuleSin

<img src="/assets/examples/module/sin.png" class="img-responsive">


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