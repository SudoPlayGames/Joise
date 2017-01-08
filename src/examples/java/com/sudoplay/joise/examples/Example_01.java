package com.sudoplay.joise.examples;

import java.awt.Dimension;

import javax.swing.JFrame;

import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction.BasisType;
import com.sudoplay.joise.module.ModuleBasisFunction.InterpolationType;
import com.sudoplay.joise.module.ModuleFractal;
import com.sudoplay.joise.module.ModuleFractal.FractalType;

public class Example_01 {

  public static void main(String[] args) {
    int width = 640;
    int height = 480;

    JFrame frame = new JFrame("Joise Example 01");
    frame.setPreferredSize(new Dimension(width, height));

    Canvas canvas = new Canvas(width, height);
    frame.add(canvas);

    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // ========================================================================
    // = Joise module chain
    // ========================================================================

    /*
     * Start with a fractal generator...
     */
    ModuleFractal gen = new ModuleFractal();
    gen.setAllSourceBasisTypes(BasisType.SIMPLEX);
    gen.setAllSourceInterpolationTypes(InterpolationType.CUBIC);
    gen.setNumOctaves(5);
    gen.setFrequency(2.34);
    gen.setType(FractalType.RIDGEMULTI);
    gen.setSeed(898456);

    /*
     * ... route it through an autocorrection module...
     * 
     * This module will sample it's source multiple times and attempt to
     * auto-correct the output to the range specified.
     */
    ModuleAutoCorrect ac = new ModuleAutoCorrect();
    ac.setSource(gen); // set source (can usually be either another Module or a
                       // double value; see specific module for details)
    ac.setRange(0.0f, 1.0f); // set the range to auto-correct to
    ac.setSamples(10000); // set how many samples to take
    ac.calculate(); // perform the caclulations

    /*
     * ... draw it.
     */
    canvas.updateImage(ac);

    frame.pack();
    frame.setLocationRelativeTo(null);
  }

}
