package cs211.imageprocessing.transformers;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public final class BinaryThreshold implements ImageTransformer {
    private final PApplet p;

    private int threshold = 128;

    public BinaryThreshold(PApplet parent) {
        p = parent;
    }

    /**
     * @param params
     *            threshold for binary filter in (0, 255)
     */
    @Override
    public PImage apply(PImage src, float... params) {
        if (params != null && params.length == 1) threshold = (int) params[0];
        int width = src.width, height = src.height;

        PImage result = p.createImage(width, height, PConstants.RGB);

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                float bri = p.brightness(src.get(x, y));
                int c = (bri > threshold) ? p.color(255) : p.color(0);
                result.set(x, y, c);
            }
        }
        return result;
    }

    /**
     * Default value for threshold 128
     */
    @Override
    public PImage apply(PImage src) {
        return apply(src, threshold);
    }
}
