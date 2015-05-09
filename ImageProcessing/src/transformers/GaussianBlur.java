package transformers;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public final class GaussianBlur implements ImageTransformer {
    private final PApplet p;

    private final static float[][] kernel = { { 9, 12, 9 }, { 12, 15, 12 }, { 9, 12, 9 } };

    private float weight = 1f;

    public GaussianBlur(PApplet parent) {
        p = parent;
    }

    /**
     * @param params weight of the applied blur > 0. (default: 1)
     */
    @Override
    public PImage apply(PImage src, float... params) {
        if (params != null && params.length > 0) weight = params[0];
        int width = src.width;
        int height = src.height;

        PImage result = p.createImage(width, height, PConstants.RGB);

        for (int y = 1; y < height - 1; ++y)
            for (int x = 1; x < width - 1; ++x)
                result.set(x, y, blur(src, x, y));

        return result;
    }

    private int blur(PImage src, int x, int y) {
        
        float[] rgba = new float[4];

        for (int j = -1; j < 2; ++j) {
            for (int i = -1; i < 2; ++i) {
                int c = src.get(x + i, y + j);
                rgba[0] += p.red(c) * kernel[i + 1][j + 1];
                rgba[1] += p.green(c) * kernel[i + 1][j + 1];
                rgba[2] += p.blue(c) * kernel[i + 1][j + 1];
                rgba[3] += p.alpha(c) * kernel[i + 1][j + 1];
            }
        }
        for (int i = 0; i < 4; ++i)
            rgba[i] = PApplet.constrain(rgba[i] / weight, 0, 255);
        
        return p.color(rgba[0],rgba[1],rgba[2],rgba[3]);
   
    }

    @Override
    public PImage apply(PImage src) {
        return apply(src, weight);
    }

}
