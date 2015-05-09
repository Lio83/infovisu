package cs211.imageprocessing.transformers;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public final class Sobel implements ImageTransformer {
    private final PApplet p;
    private final static int[][] hKernel = { { 0, 1, 0 }, { 0, 0, 0 }, { 0, -1, 0 } };

    private final static int[][] vKernel = { { 0, 0, 0 }, { 1, 0, -1 }, { 0, 0, 0 } };
    
    private float threshold = .3f;

    public Sobel(PApplet parent) {
        p = parent;
    }

    /**
     * @param params threshold value (default: 0.3).
     */
    @Override
    public PImage apply(PImage src, float... params) {
        if (params != null && params.length > 0) threshold = params[0];
        int width = src.width;
        int height = src.height;

        PImage result = p.createImage(width, height, PConstants.RGB);

        float max = 0;
        float[] buffer = new float[width * height];
        float sum_h = 0, sum_v = 0, sum = 0;

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                sum_h = sobel(src, x, y, hKernel);
                sum_v = sobel(src, x, y, vKernel);
                sum = (int) PApplet.sqrt(sum_h * sum_h + sum_v * sum_v);

                buffer[y * width + x] = sum;

                if (sum > max) max = sum;
            }
        }

        for (int y = 1; y < height - 1; ++y) {
            for (int x = 1; x < width - 1; ++x) {
                int c = buffer[y * width + x] > max * threshold 
                        ? p.color(255) : p.color(0);
                result.set(x, y, c);
            }
        }

        return result;
    }

    private float sobel(PImage src, int x, int y, int[][] kernel) {
        float acc = 0;
        for (int j = -1; j < 2; ++j) {
            for (int i = -1; i < 2; ++i) {
                int c = src.get(x + i, y + j);
                acc += c * kernel[i + 1][j + 1];
            }
        }
        return acc;
    }

    @Override
    public PImage apply(PImage src) {
        return apply(src, threshold);
    }
}
