package com.sudoplay.joise.examples;

import com.sudoplay.joise.mapping.*;
import com.sudoplay.joise.module.ModuleAutoCorrect;
import com.sudoplay.joise.module.ModuleBasisFunction.BasisType;
import com.sudoplay.joise.module.ModuleBasisFunction.InterpolationType;
import com.sudoplay.joise.module.ModuleFractal;
import com.sudoplay.joise.module.ModuleFractal.FractalType;

import javax.swing.*;
import java.awt.*;
import java.nio.channels.FileChannel;

public class Example_03 {

  public static void main(String[] args) {
    int width = 640;
    int height = 480;

    JFrame frame = new JFrame("Joise Example 03");
    frame.setPreferredSize(new Dimension(width, height));

    MappingCanvas canvas = new MappingCanvas(width, height);
    frame.add(canvas);

    frame.setVisible(true);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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
    ModuleAutoCorrect module = new ModuleAutoCorrect();
    module.setSource(gen); // set source (can usually be either another Module or a
                       // double value; see specific module for details)
    module.setRange(0.0f, 1.0f); // set the range to auto-correct to
    module.setSamples(10000); // set how many samples to take
    module.calculate3D(); // perform the calculations, 3D because map2D below samples in 3D with a fixed z value

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

    /*
     * ... draw it.
     */
    canvas.updateImage(data);

    frame.pack();
    frame.setLocationRelativeTo(null);
  }

}
