/*
 * Copyright (C) 2013 Jason Taylor.
 * Released as open-source under the Apache License, Version 2.0.
 *
 * ============================================================================
 * | Joise
 * ============================================================================
 *
 * Copyright (C) 2013 Jason Taylor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ============================================================================
 * | Accidental Noise Library
 * | --------------------------------------------------------------------------
 * | Joise is a derivative work based on Josua Tippetts' C++ library:
 * | http://accidentalnoise.sourceforge.net/index.html
 * ============================================================================
 *
 * Copyright (C) 2011 Joshua Tippetts
 *
 *   This software is provided 'as-is', without any express or implied
 *   warranty.  In no event will the authors be held liable for any damages
 *   arising from the use of this software.
 *
 *   Permission is granted to anyone to use this software for any purpose,
 *   including commercial applications, and to alter it and redistribute it
 *   freely, subject to the following restrictions:
 *
 *   1. The origin of this software must not be misrepresented; you must not
 *      claim that you wrote the original software. If you use this software
 *      in a product, an acknowledgment in the product documentation would be
 *      appreciated but is not required.
 *   2. Altered source versions must be plainly marked as such, and must not be
 *      misrepresented as being the original software.
 *   3. This notice may not be removed or altered from any source distribution.
 */
package com.sudoplay.joise;

import com.sudoplay.joise.module.*;

/* package */ class ModuleFactory {

  /* package */ Module create(String string) {

    // Can't switch on string and retain Java 6 compatibility.

    // Thanks to https://github.com/BenMcLean for this!

    if (string.equalsIgnoreCase("ModuleAbs")) return new ModuleAbs();
    if (string.equalsIgnoreCase("ModuleAutoCorrect")) return new ModuleAutoCorrect();
    if (string.equalsIgnoreCase("ModuleBasisFunction")) return new ModuleBasisFunction();
    if (string.equalsIgnoreCase("ModuleBias")) return new ModuleBias();
    if (string.equalsIgnoreCase("ModuleBlend")) return new ModuleBlend();
    if (string.equalsIgnoreCase("ModuleBrightContrast")) return new ModuleBrightContrast();
    if (string.equalsIgnoreCase("ModuleCache")) return new ModuleCache();
    if (string.equalsIgnoreCase("ModuleCellGen")) return new ModuleCellGen();
    if (string.equalsIgnoreCase("ModuleCellular")) return new ModuleCellular();
    if (string.equalsIgnoreCase("ModuleClamp")) return new ModuleClamp();
    if (string.equalsIgnoreCase("ModuleCombiner")) return new ModuleCombiner();
    if (string.equalsIgnoreCase("ModuleCos")) return new ModuleCos();
    if (string.equalsIgnoreCase("ModuleFloor")) return new ModuleFloor();
    if (string.equalsIgnoreCase("ModuleFractal")) return new ModuleFractal();
    if (string.equalsIgnoreCase("ModuleFunctionGradient")) return new ModuleFunctionGradient();
    if (string.equalsIgnoreCase("ModuleGain")) return new ModuleGain();
    if (string.equalsIgnoreCase("ModuleGradient")) return new ModuleGradient();
    if (string.equalsIgnoreCase("ModuleInvert")) return new ModuleInvert();
    if (string.equalsIgnoreCase("ModuleMagnitude")) return new ModuleMagnitude();
    if (string.equalsIgnoreCase("ModuleNormalizedCoords")) return new ModuleNormalizedCoords();
    if (string.equalsIgnoreCase("ModulePow")) return new ModulePow();
    if (string.equalsIgnoreCase("ModuleRotateDomain")) return new ModuleRotateDomain();
    if (string.equalsIgnoreCase("ModuleSawtooth")) return new ModuleSawtooth();
    if (string.equalsIgnoreCase("ModuleScaleDomain")) return new ModuleScaleDomain();
    if (string.equalsIgnoreCase("ModuleScaleOffset")) return new ModuleScaleOffset();
    if (string.equalsIgnoreCase("ModuleSelect")) return new ModuleSelect();
    if (string.equalsIgnoreCase("ModuleSin")) return new ModuleSin();
    if (string.equalsIgnoreCase("ModuleSphere")) return new ModuleSphere();
    if (string.equalsIgnoreCase("ModuleTiers")) return new ModuleTiers();
    if (string.equalsIgnoreCase("ModuleTranslateDomain")) return new ModuleTranslateDomain();
    if (string.equalsIgnoreCase("ModuleTriangle")) return new ModuleTriangle();
    throw (new UnsupportedOperationException());
  }
}
