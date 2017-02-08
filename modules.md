---
layout: page
title: Modules
description: In-depth analysis of how each module works.
permalink: /modules/
---

## Generators

<p class="lead">Generators are modules that create noise.</p>

### ModuleBasisFunction

The `ModuleBasisFunction` generates noise using one of five different BasisTypes and one of four different InterpolationTypes.

These examples illustrate each basis type with the four differnent interpolation types and can be found in the source in the following location:

```
/src/examples/java/com/sudoplay/joise/examples/module/basis
```

#### BasisType.GRADIENT

![](/assets/examples/module/basis/gradient.png)

#### BasisType.GRADVAL

![](/assets/examples/module/basis/gradval.png)

#### BasisType.SIMPLEX

![](/assets/examples/module/basis/simplex.png)

Because simplex noise does not use interpolation, there is no difference between the four interpolation types.

#### BasisType.VALUE

![](/assets/examples/module/basis/value.png)

#### BasisType.WHITE

![](/assets/examples/module/basis/white.png)

### ModuleCellGen

![](/assets/examples/module/cellGen.png)

### ModuleCellular

<div class="bs-callout bs-callout-warning">
  <h4>This module can't be sampled directly.</h4>
  The ModuleCellular acts as input to the ModuleCellGen module and therefore can't be sampled directly.
</div>


### ModuleFractal - Billow

#### BasisType.GRADIENT

![](/assets/examples/module/fractal/gradient/billow.png)

#### BasisType.GRADVAL

![](/assets/examples/module/fractal/gradval/billow.png)

#### BasisType.SIMPLEX

![](/assets/examples/module/fractal/simplex/billow.png)

#### BasisType.VALUE

![](/assets/examples/module/fractal/value/billow.png)


### ModuleFractal - deCarpentierSwiss

#### BasisType.GRADIENT

![](/assets/examples/module/fractal/gradient/deCarpentierSwiss.png)

#### BasisType.GRADVAL

![](/assets/examples/module/fractal/gradval/deCarpentierSwiss.png)

#### BasisType.SIMPLEX

![](/assets/examples/module/fractal/simplex/deCarpentierSwiss.png)

#### BasisType.VALUE

![](/assets/examples/module/fractal/value/deCarpentierSwiss.png)


### ModuleFractal - FBM

#### BasisType.GRADIENT

![](/assets/examples/module/fractal/gradient/fbm.png)

#### BasisType.GRADVAL

![](/assets/examples/module/fractal/gradval/fbm.png)

#### BasisType.SIMPLEX

![](/assets/examples/module/fractal/simplex/fbm.png)

#### BasisType.VALUE

![](/assets/examples/module/fractal/value/fbm.png)


### ModuleFractal - HybridMulti

#### BasisType.GRADIENT

![](/assets/examples/module/fractal/gradient/hybridMulti.png)

#### BasisType.GRADVAL

![](/assets/examples/module/fractal/gradval/hybridMulti.png)

#### BasisType.SIMPLEX

![](/assets/examples/module/fractal/simplex/hybridMulti.png)

#### BasisType.VALUE

![](/assets/examples/module/fractal/value/hybridMulti.png)


### ModuleFractal - Multi

#### BasisType.GRADIENT

![](/assets/examples/module/fractal/gradient/multi.png)

#### BasisType.GRADVAL

![](/assets/examples/module/fractal/gradval/multi.png)

#### BasisType.SIMPLEX

![](/assets/examples/module/fractal/simplex/multi.png)

#### BasisType.VALUE

![](/assets/examples/module/fractal/value/multi.png)


### ModuleFractal - RidgeMulti

#### BasisType.GRADIENT

![](/assets/examples/module/fractal/gradient/ridgeMulti.png)

#### BasisType.GRADVAL

![](/assets/examples/module/fractal/gradval/ridgeMulti.png)

#### BasisType.SIMPLEX

![](/assets/examples/module/fractal/simplex/ridgeMulti.png)

#### BasisType.VALUE

![](/assets/examples/module/fractal/value/ridgeMulti.png)



### ModuleFractal - BasisType.WHITE

#### FractalType.BILLOW

![](/assets/examples/module/fractal/white/billow.png)

#### FractalType.DECARPENTIERSWISS

![](/assets/examples/module/fractal/white/deCarpentierSwiss.png)

#### FractalType.FBM

![](/assets/examples/module/fractal/white/fbm.png)

#### FractalType.HYBRIDMULTI

![](/assets/examples/module/fractal/white/hybridMulti.png)

#### FractalType.MULTI

![](/assets/examples/module/fractal/white/multi.png)

#### FractalType.RIDGEMULTI

![](/assets/examples/module/fractal/white/ridgeMulti.png)


### ModuleGradient

![](/assets/examples/module/gradient.png)

### ModuleSphere

![](/assets/examples/module/sphere.png)


## Simple Manipulators

Simple manipulators are modules that take a single source and perform a fairly simple mathematical function on the sampled values.

### ModuleAbs

![](/assets/examples/module/abs.png)

### ModuleClamp

![](/assets/examples/module/clamp.png)

### ModuleCos

![](/assets/examples/module/cos.png)

### ModuleFloor

![](/assets/examples/module/floor.png)

### ModuleInvert

![](/assets/examples/module/invert.png)

### ModulePow

![](/assets/examples/module/pow.png)

### ModuleSin

![](/assets/examples/module/sin.png)


## Complex Manipulators

Complex manipulators are modules that take one or more sources and perform mathematical functions on the sampled values that are more complex than the simple manipulators.

### ModuleAutoCorrect

![](/assets/examples/module/autoCorrect.png)

### ModuleBias

![](/assets/examples/module/bias.png)

### ModuleBlend

![](/assets/examples/module/blend.png)

### ModuleBrightContrast

![](/assets/examples/module/brightContrast.png)

### ModuleCache

<div class="bs-callout bs-callout-danger">
  <h4>// TODO</h4>
</div>

### ModuleCombiner

#### Add

![](/assets/examples/module/combiner/add.png)

#### Avg

![](/assets/examples/module/combiner/avg.png)

#### Max

![](/assets/examples/module/combiner/max.png)

#### Min

![](/assets/examples/module/combiner/min.png)

#### Mult

![](/assets/examples/module/combiner/mult.png)

### ModuleFunctionGradient

![](/assets/examples/module/functionGradient.png)

### ModuleGain

![](/assets/examples/module/gain.png)

### ModuleMagnitude

![](/assets/examples/module/magnitude.png)

### ModuleNormalizedCoords

![](/assets/examples/module/normalizedCoords.png)

### ModuleRotateDomain

![](/assets/examples/module/rotateDomain.png)

### ModuleSawtooth

![](/assets/examples/module/sawtooth.png)

### ModuleScaleDomain

![](/assets/examples/module/scaleDomain.png)

### ModuleScaleOffset

![](/assets/examples/module/scaleOffset.png)

### ModuleSelect

![](/assets/examples/module/select.png)

### ModuleTiers

![](/assets/examples/module/tiers.png)

### ModuleTranslateDomain

![](/assets/examples/module/translateDomain.png)

### ModuleTriangle

![](/assets/examples/module/triangle.png)