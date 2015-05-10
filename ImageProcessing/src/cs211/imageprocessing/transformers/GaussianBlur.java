package cs211.imageprocessing.transformers;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public final class GaussianBlur implements ImageTransformer {
    private final PApplet p;

    private final static float[][] kernel = { { 9, 12, 9 }, { 12, 15, 12 }, { 9, 12, 9 } };

    private float weight = 1;

    public GaussianBlur(PApplet parent) {
        p = parent;
    }

    /**
     * @param params
     *            weight of the applied blur > 0. (default: 1)
     */
    @Override
    public PImage apply(PImage src, float... params) {
        if (params != null && params.length == 1) weight = params[0];
        int width = src.width, height = src.height;

        PImage result = p.createImage(width, height, PConstants.RGB);

        for (int y = 1; y < height - 1; ++y)
            for (int x = 1; x < width - 1; ++x)
                result.set(x, y, blur(src, x, y));

        return result;
    }

    private int blur(PImage src, int x, int y) {
        float[] rgb = new float[3];

        for (int j = -1; j < 2; ++j) {
            for (int i = -1; i < 2; ++i) {
                int c = src.get(x + i, y + j);
                float k = kernel[i + 1][j + 1];
                rgb[0] += p.red(c) * k;
                rgb[1] += p.green(c) * k;
                rgb[2] += p.blue(c) * k;
            }
        }
        
        for (int i = 0; i < 3; ++i)
            rgb[i] = PApplet.constrain(rgb[i] / weight, 0, 255);

        return p.color(rgb[0], rgb[1], rgb[2]);
    }

    /**
     * Default value for weight 1
     */
    @Override
    public PImage apply(PImage src) {
        return apply(src, weight);
    }
}
