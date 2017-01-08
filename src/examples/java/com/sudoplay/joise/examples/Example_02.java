package com.sudoplay.joise.examples;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JFrame;

import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction.BasisType;
import com.sudoplay.joise.module.ModuleBasisFunction.InterpolationType;
import com.sudoplay.joise.module.ModuleBias;
import com.sudoplay.joise.module.ModuleCache;
import com.sudoplay.joise.module.ModuleCombiner;
import com.sudoplay.joise.module.ModuleCombiner.CombinerType;
import com.sudoplay.joise.module.ModuleFractal;
import com.sudoplay.joise.module.ModuleFractal.FractalType;
import com.sudoplay.joise.module.ModuleGradient;
import com.sudoplay.joise.module.ModuleScaleDomain;
import com.sudoplay.joise.module.ModuleScaleOffset;
import com.sudoplay.joise.module.ModuleSelect;
import com.sudoplay.joise.module.ModuleTranslateDomain;

/**
 * This example is derived from:
 * http://accidentalnoise.sourceforge.net/minecraftworlds.html
 * <p>
 * This will get you started on the path to creating Terraria-style 2D noise for
 * use in a game. For an in-depth explanation of how the functions affect the
 * output, study the information located at the above link.
 * <p>
 * You can click in the window that pops up to re-generate new noise.
 * <p>
 * 'It all just comes down to thinking about what you want, understanding what
 * effect the various functions will have on the output, and experimenting with
 * the various parameters until you get something you like. It's not a perfect
 * science, and there are often many ways you can accomplish any given effect.'
 * -Joshua Tippetts
 * 
 * @author Jason Taylor
 * 
 */
public class Example_02 {

  public static void main(String[] args) {
    int width = 400;
    int height = 400;

    JFrame frame = new JFrame("Joise Example 02");
    frame.setPreferredSize(new Dimension(width, height));

    final Canvas canvas = new Canvas(width, height);
    frame.add(canvas);

    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.addMouseListener(new MouseListener() {

      @Override
      public void mouseReleased(MouseEvent arg0) {
        //
      }

      @Override
      public void mousePressed(MouseEvent arg0) {
        //
      }

      @Override
      public void mouseExited(MouseEvent arg0) {
        //
      }

      @Override
      public void mouseEntered(MouseEvent arg0) {
        //
      }

      @Override
      public void mouseClicked(MouseEvent arg0) {
        updateImage(canvas);
      }
    });

    updateImage(canvas);

    frame.pack();
    frame.setLocationRelativeTo(null);
  }

  private static void updateImage(Canvas canvas) {
    // ========================================================================
    // = Joise module chain
    // ========================================================================

    Random random = new Random();
    long seed = random.nextLong();

    /*
     * ground_gradient
     */

    // ground_gradient
    ModuleGradient groundGradient = new ModuleGradient();
    groundGradient.setGradient(0, 0, 0, 1);

    /*
     * lowland
     */

    // lowland_shape_fractal
    ModuleFractal lowlandShapeFractal = new ModuleFractal(FractalType.BILLOW, BasisType.GRADIENT, InterpolationType.QUINTIC);
    lowlandShapeFractal.setNumOctaves(2);
    lowlandShapeFractal.setFrequency(0.25);
    lowlandShapeFractal.setSeed(seed);

    // lowland_autocorrect
    ModuleAutoCorrect lowlandAutoCorrect = new ModuleAutoCorrect(0, 1);
    lowlandAutoCorrect.setSource(lowlandShapeFractal);
    lowlandAutoCorrect.calculate();

    // lowland_scale
    ModuleScaleOffset lowlandScale = new ModuleScaleOffset();
    lowlandScale.setScale(0.125);
    lowlandScale.setOffset(-0.45);
    lowlandScale.setSource(lowlandAutoCorrect);

    // lowland_y_scale
    ModuleScaleDomain lowlandYScale = new ModuleScaleDomain();
    lowlandYScale.setScaleY(0);
    lowlandYScale.setSource(lowlandScale);

    // lowland_terrain
    ModuleTranslateDomain lowlandTerrain = new ModuleTranslateDomain();
    lowlandTerrain.setAxisYSource(lowlandYScale);
    lowlandTerrain.setSource(groundGradient);

    /*
     * highland
     */

    // highland_shape_fractal
    ModuleFractal highlandShapeFractal = new ModuleFractal(FractalType.FBM, BasisType.GRADIENT, InterpolationType.QUINTIC);
    highlandShapeFractal.setNumOctaves(4);
    highlandShapeFractal.setFrequency(2);
    highlandShapeFractal.setSeed(seed);

    // highland_autocorrect
    ModuleAutoCorrect highlandAutoCorrect = new ModuleAutoCorrect(-1, 1);
    highlandAutoCorrect.setSource(highlandShapeFractal);
    highlandAutoCorrect.calculate();

    // highland_scale
    ModuleScaleOffset highlandScale = new ModuleScaleOffset();
    highlandScale.setScale(0.25);
    highlandScale.setOffset(0);
    highlandScale.setSource(highlandAutoCorrect);

    // highland_y_scale
    ModuleScaleDomain highlandYScale = new ModuleScaleDomain();
    highlandYScale.setScaleY(0);
    highlandYScale.setSource(highlandScale);

    // highland_terrain
    ModuleTranslateDomain highlandTerrain = new ModuleTranslateDomain();
    highlandTerrain.setAxisYSource(highlandYScale);
    highlandTerrain.setSource(groundGradient);

    /*
     * mountain
     */

    // mountain_shape_fractal
    ModuleFractal mountainShapeFractal = new ModuleFractal(FractalType.RIDGEMULTI, BasisType.GRADIENT, InterpolationType.QUINTIC);
    mountainShapeFractal.setNumOctaves(8);
    mountainShapeFractal.setFrequency(1);
    mountainShapeFractal.setSeed(seed);

    // mountain_autocorrect
    ModuleAutoCorrect mountainAutoCorrect = new ModuleAutoCorrect(-1, 1);
    mountainAutoCorrect.setSource(mountainShapeFractal);
    mountainAutoCorrect.calculate();

    // mountain_scale
    ModuleScaleOffset mountainScale = new ModuleScaleOffset();
    mountainScale.setScale(0.45);
    mountainScale.setOffset(0.15);
    mountainScale.setSource(mountainAutoCorrect);

    // mountain_y_scale
    ModuleScaleDomain mountainYScale = new ModuleScaleDomain();
    mountainYScale.setScaleY(0.1);
    mountainYScale.setSource(mountainScale);

    // mountain_terrain
    ModuleTranslateDomain mountainTerrain = new ModuleTranslateDomain();
    mountainTerrain.setAxisYSource(mountainYScale);
    mountainTerrain.setSource(groundGradient);

    /*
     * terrain
     */

    // terrain_type_fractal
    ModuleFractal terrainTypeFractal = new ModuleFractal(FractalType.FBM, BasisType.GRADIENT, InterpolationType.QUINTIC);
    terrainTypeFractal.setNumOctaves(3);
    terrainTypeFractal.setFrequency(0.125);
    terrainTypeFractal.setSeed(seed);

    // terrain_autocorrect
    ModuleAutoCorrect terrainAutoCorrect = new ModuleAutoCorrect(0, 1);
    terrainAutoCorrect.setSource(terrainTypeFractal);
    terrainAutoCorrect.calculate();

    // terrain_type_y_scale
    ModuleScaleDomain terrainTypeYScale = new ModuleScaleDomain();
    terrainTypeYScale.setScaleY(0);
    terrainTypeYScale.setSource(terrainAutoCorrect);

    // terrain_type_cache
    ModuleCache terrainTypeCache = new ModuleCache();
    terrainTypeCache.setSource(terrainTypeYScale);

    // highland_mountain_select
    ModuleSelect highlandMountainSelect = new ModuleSelect();
    highlandMountainSelect.setLowSource(highlandTerrain);
    highlandMountainSelect.setHighSource(mountainTerrain);
    highlandMountainSelect.setControlSource(terrainTypeCache);
    highlandMountainSelect.setThreshold(0.65);
    highlandMountainSelect.setFalloff(0.2);

    // highland_lowland_select
    ModuleSelect highlandLowlandSelect = new ModuleSelect();
    highlandLowlandSelect.setLowSource(lowlandTerrain);
    highlandLowlandSelect.setHighSource(highlandMountainSelect);
    highlandLowlandSelect.setControlSource(terrainTypeCache);
    highlandLowlandSelect.setThreshold(0.25);
    highlandLowlandSelect.setFalloff(0.15);

    // highland_lowland_select_cache
    ModuleCache highlandLowlandSelectCache = new ModuleCache();
    highlandLowlandSelectCache.setSource(highlandLowlandSelect);

    // ground_select
    ModuleSelect groundSelect = new ModuleSelect();
    groundSelect.setLowSource(0);
    groundSelect.setHighSource(1);
    groundSelect.setThreshold(0.5);
    groundSelect.setControlSource(highlandLowlandSelectCache);

    /*
     * cave
     */

    // cave_shape
    ModuleFractal caveShape = new ModuleFractal(FractalType.RIDGEMULTI, BasisType.GRADIENT, InterpolationType.QUINTIC);
    caveShape.setNumOctaves(1);
    caveShape.setFrequency(8);
    caveShape.setSeed(seed);

    // cave_attenuate_bias
    ModuleBias caveAttenuateBias = new ModuleBias(0.825);
    caveAttenuateBias.setSource(highlandLowlandSelectCache);

    // cave_shape_attenuate
    ModuleCombiner caveShapeAttenuate = new ModuleCombiner(CombinerType.MULT);
    caveShapeAttenuate.setSource(0, caveShape);
    caveShapeAttenuate.setSource(1, caveAttenuateBias);

    // cave_perturb_fractal
    ModuleFractal cavePerturbFractal = new ModuleFractal(FractalType.FBM, BasisType.GRADIENT, InterpolationType.QUINTIC);
    cavePerturbFractal.setNumOctaves(6);
    cavePerturbFractal.setFrequency(3);
    cavePerturbFractal.setSeed(seed);

    // cave_perturb_scale
    ModuleScaleOffset cavePerturbScale = new ModuleScaleOffset();
    cavePerturbScale.setScale(0.25);
    cavePerturbScale.setOffset(0);
    cavePerturbScale.setSource(cavePerturbFractal);

    // cave_perturb
    ModuleTranslateDomain cavePerturb = new ModuleTranslateDomain();
    cavePerturb.setAxisXSource(cavePerturbScale);
    cavePerturb.setSource(caveShapeAttenuate);

    // cave_select
    ModuleSelect caveSelect = new ModuleSelect();
    caveSelect.setLowSource(1);
    caveSelect.setHighSource(0);
    caveSelect.setControlSource(cavePerturb);
    caveSelect.setThreshold(0.8);
    caveSelect.setFalloff(0);

    /*
     * final
     */

    // ground_cave_multiply
    ModuleCombiner groundCaveMultiply = new ModuleCombiner(CombinerType.MULT);
    groundCaveMultiply.setSource(0, caveSelect);
    groundCaveMultiply.setSource(1, groundSelect);

    /*
     * Draw it
     */

    canvas.updateImage(groundCaveMultiply);

  }

}
