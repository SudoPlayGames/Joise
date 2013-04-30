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

package com.sudoplay.joise.map;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sudoplay.joise.Joise;
import com.sudoplay.joise.ModuleMap;
import com.sudoplay.joise.generator.LCG;
import com.sudoplay.joise.module.Module;
import com.sudoplay.joise.module.ModuleAbs;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction;
import com.sudoplay.joise.module.ModuleBasisFunction.BasisType;
import com.sudoplay.joise.module.ModuleBasisFunction.InterpolationType;
import com.sudoplay.joise.module.ModuleBias;
import com.sudoplay.joise.module.ModuleBlend;
import com.sudoplay.joise.module.ModuleBrightContrast;
import com.sudoplay.joise.module.ModuleCache;
import com.sudoplay.joise.module.ModuleCellGen;
import com.sudoplay.joise.module.ModuleCellular;
import com.sudoplay.joise.module.ModuleClamp;
import com.sudoplay.joise.module.ModuleCombiner;
import com.sudoplay.joise.module.ModuleCombiner.CombinerType;
import com.sudoplay.joise.module.ModuleCos;
import com.sudoplay.joise.module.ModuleFloor;
import com.sudoplay.joise.module.ModuleFractal;
import com.sudoplay.joise.module.ModuleFractal.FractalType;
import com.sudoplay.joise.module.ModuleFunctionGradient;
import com.sudoplay.joise.module.ModuleFunctionGradient.FunctionGradientAxis;
import com.sudoplay.joise.module.ModuleGain;
import com.sudoplay.joise.module.ModuleGradient;
import com.sudoplay.joise.module.ModuleInvert;
import com.sudoplay.joise.module.ModuleMagnitude;
import com.sudoplay.joise.module.ModuleNormalizedCoords;
import com.sudoplay.joise.module.ModulePow;
import com.sudoplay.joise.module.ModuleRotateDomain;
import com.sudoplay.joise.module.ModuleSawtooth;
import com.sudoplay.joise.module.ModuleScaleDomain;
import com.sudoplay.joise.module.ModuleScaleOffset;
import com.sudoplay.joise.module.ModuleSelect;
import com.sudoplay.joise.module.ModuleSin;
import com.sudoplay.joise.module.ModuleSphere;
import com.sudoplay.joise.module.ModuleTiers;
import com.sudoplay.joise.module.ModuleTranslateDomain;
import com.sudoplay.joise.module.ModuleTriangle;
import com.sudoplay.joise.module.SeedableModule;

public class MapTest {

  private static ModuleBasisFunction func1;
  private static ModuleBasisFunction func2;
  private static ModuleBasisFunction func3;

  @BeforeClass
  public static void init() {
    func1 = new ModuleBasisFunction();
    func1.setType(BasisType.SIMPLEX);
    func1.setInterpolation(InterpolationType.LINEAR);
    func1.setSeed(424242);

    func2 = new ModuleBasisFunction();
    func2.setType(BasisType.GRADVAL);
    func2.setInterpolation(InterpolationType.QUINTIC);
    func2.setSeed(684684654);

    func3 = new ModuleBasisFunction();
    func3.setType(BasisType.VALUE);
    func3.setInterpolation(InterpolationType.CUBIC);
    func3.setSeed(12233815);
  }

  @Test
  public void testModuleAbs() {
    ModuleAbs mod = new ModuleAbs();
    mod.setSource(func2);
    test(mod);
  }

  @Test
  public void testModuleAutoCorrect() {
    ModuleAutoCorrect mod = new ModuleAutoCorrect();
    mod.setHigh(0.8);
    mod.setLow(0.6);
    mod.setSamples(1000);
    mod.setSampleScale(5.0);
    mod.setSource(func2);
    mod.calculate();
    mod.setLocked(true);
    test(mod);
  }

  @Test
  public void testModuleBasisFunction() {
    test(func1);
  }

  @Test
  public void testModuleBias() {
    ModuleBias bias = new ModuleBias();
    bias.setBias(0.6554);
    bias.setSource(func1);
    test(bias);
    bias.setBias(func2);
    test(bias);
  }

  @Test
  public void testModuleBlend() {
    ModuleBlend blend = new ModuleBlend();
    blend.setHighSource(func1);
    blend.setLowSource(func2);
    blend.setControlSource(0.23);
    test(blend);
    blend.setControlSource(func3);
    test(blend);
  }

  @Test
  public void testModuleBrightContrast() {
    ModuleBrightContrast mod = new ModuleBrightContrast();
    mod.setBrightness(0.36);
    mod.setContrastFactor(func2);
    mod.setContrastThreshold(func3);
    mod.setSource(func1);
    test(mod);
  }

  @Test
  public void testModuleCache() {
    ModuleCache mod = new ModuleCache();
    mod.setSource(func2);
    test(mod);
  }

  @Test
  public void testModuleCellular() {
    ModuleCellGen gen = new ModuleCellGen();
    gen.setSeed(6816846);
    ModuleCellular mod = new ModuleCellular();
    mod.setCellularSource(gen);
    mod.setCoefficients(0.2, 0.9, 3.6, 1.7);
    test(mod);
  }

  @Test
  public void testModuleClamp() {
    ModuleClamp mod = new ModuleClamp();
    mod.setRange(0.5, 1.5);
    mod.setSource(func3);
    test(mod);
  }

  @Test
  public void testModuleCombiner() {
    ModuleCombiner mod = new ModuleCombiner();
    mod.setType(CombinerType.ADD);
    mod.setSource(0, func1);
    mod.setSource(1, func2);
    test(mod);
    mod.setSource(5, func3);
    test(mod);
  }

  @Test
  public void testModuleCos() {
    ModuleCos mod = new ModuleCos();
    mod.setSource(func2);
    test(mod);
  }

  @Test
  public void testModuleFloor() {
    ModuleFloor mod = new ModuleFloor();
    mod.setSource(func3);
    test(mod);
  }

  @Test
  public void testModuleFractal() {
    ModuleFractal mod = new ModuleFractal();
    mod.setAllSourceBasisTypes(BasisType.SIMPLEX);
    mod.setAllSourceInterpolationTypes(InterpolationType.CUBIC);
    mod.setNumOctaves(5);
    mod.setFrequency(2.34);
    mod.setType(FractalType.RIDGEMULTI);
    mod.setSeed(898456);
    test(mod);
  }

  @Test
  public void testModuleFractalOverrideSource() {
    ModuleFractal mod = new ModuleFractal();
    mod.setAllSourceBasisTypes(BasisType.SIMPLEX);
    mod.setAllSourceInterpolationTypes(InterpolationType.CUBIC);
    mod.setNumOctaves(5);
    mod.setFrequency(2.34);
    mod.setType(FractalType.RIDGEMULTI);
    mod.setSeed(898456);
    mod.overrideSource(2, func2);
    test(mod);
  }

  @Test
  public void testModuleFunctionGradient() {
    ModuleFunctionGradient mod = new ModuleFunctionGradient();
    mod.setAxis(FunctionGradientAxis.X_AXIS);
    mod.setSpacing(0.365458556);
    test(mod);
  }

  @Test
  public void testModuleGain() {
    ModuleGain mod = new ModuleGain();
    mod.setSource(func2);
    mod.setGain(0.76);
    test(mod);
    mod.setGain(func1);
    test(mod);
  }

  @Test
  public void testModuleNormalizedCoords() {
    ModuleNormalizedCoords mod = new ModuleNormalizedCoords();
    mod.setSource(func2);
    mod.setLength(0.36);
    test(mod);
    mod.setLength(func3);
    test(mod);
  }

  @Test
  public void testModuleGradient() {
    ModuleGradient mod = new ModuleGradient();
    mod.setGradient(0.0, 0.0, 0.0, 1.0);
    test(mod);
  }

  @Test
  public void testModuleInvert() {
    ModuleInvert mod = new ModuleInvert();
    mod.setSource(func3);
    test(mod);
  }

  @Test
  public void testModuleMagnitude() {
    ModuleMagnitude mod = new ModuleMagnitude();
    mod.setX(func3);
    mod.setY(2.156);
    test(mod);
  }

  @Test
  public void testModulePow() {
    ModulePow mod = new ModulePow();
    mod.setSource(func1);
    mod.setPower(6.245);
    test(mod);
    mod.setPower(func2);
    test(mod);
  }

  @Test
  public void testModuleRotateDomain() {
    ModuleRotateDomain mod = new ModuleRotateDomain();
    mod.setAxis(0.36, 1.25, 0.45);
    mod.setAngle(2.556);
    mod.setSource(func3);
    test(mod);
  }

  @Test
  public void testModuleSawtooth() {
    ModuleSawtooth mod = new ModuleSawtooth();
    mod.setSource(func2);
    mod.setPeriod(23.56);
    test(mod);
  }

  @Test
  public void testModuleScaleDomain() {
    ModuleScaleDomain mod = new ModuleScaleDomain();
    mod.setScaleX(23.54);
    mod.setScaleY(func1);
    mod.setSource(func3);
    test(mod);
  }

  @Test
  public void testModuleScaleOffset() {
    ModuleScaleOffset mod = new ModuleScaleOffset();
    mod.setSource(func1);
    mod.setScale(648.231);
    mod.setOffset(func2);
    test(mod);
  }

  @Test
  public void testModuleSelect() {
    ModuleSelect mod = new ModuleSelect();
    mod.setControlSource(1.2);
    mod.setHighSource(func1);
    mod.setLowSource(func3);
    mod.setFalloff(0.5);
    mod.setThreshold(func2);
    test(mod);
  }

  @Test
  public void testModuleSin() {
    ModuleSin mod = new ModuleSin();
    mod.setSource(func2);
    test(mod);
  }

  @Test
  public void testModuleSphere() {
    ModuleSphere mod = new ModuleSphere();
    mod.setCenterX(0.3);
    mod.setCenterY(0.6);
    mod.setCenterZ(func2);
    mod.setRadius(0.9);
    test(mod);
  }

  @Test
  public void testModuleTiers() {
    ModuleTiers mod = new ModuleTiers();
    mod.setNumTiers(5);
    mod.setSmooth(false);
    mod.setSource(func2);
    test(mod);
    mod.setSource(func1);
    mod.setSmooth(true);
    test(mod);
  }

  @Test
  public void testModuleTranslateDomain() {
    ModuleTranslateDomain mod = new ModuleTranslateDomain();
    mod.setAxisXSource(2.6);
    mod.setAxisYSource(84.23);
    mod.setSource(func3);
    test(mod);
  }

  @Test
  public void testModuleTriangle() {
    ModuleTriangle mod = new ModuleTriangle();
    mod.setOffset(0.23);
    mod.setPeriod(4.89);
    mod.setSource(func1);
    test(mod);
  }

  @Test
  public void testSeedName() {
    testWithSeedName(func2);
  }

  private void test(Module module) {
    ModuleMap map = module.getModuleMap();
    // System.out.println(map);
    Joise j = new Joise(map);
    LCG lcg = new LCG();
    lcg.setSeedTime();
    double x, y;
    for (int i = 0; i < 100; i++) {
      x = lcg.get01() * 8.0;
      y = lcg.get01() * 8.0;
      assertEquals(module.get(x, y), j.get(x, y), 1e-15);
    }
  }

  private void testWithSeedName(SeedableModule module) {
    module.setSeedName("externalSeed");

    ModuleMap map = module.getModuleMap();
    // System.out.println(map);
    Joise j = new Joise(map);
    LCG lcg = new LCG();
    lcg.setSeedTime();
    double x, y;
    long seed;
    for (int i = 0; i < 100; i++) {
      x = lcg.get01() * 8.0;
      y = lcg.get01() * 8.0;

      seed = lcg.get();
      module.setSeed(seed);
      j.setSeed("externalSeed", seed);

      assertEquals(module.get(x, y), j.get(x, y), 1e-15);
    }
  }

}
