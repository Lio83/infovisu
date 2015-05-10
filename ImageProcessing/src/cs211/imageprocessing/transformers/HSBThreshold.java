package cs211.imageprocessing.transformers;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public final class HSBThreshold implements ImageTransformer {
    private final PApplet p;

    private float hueMin = 80, hueMax = 140, satMin = 80, satMax = 255, briMin = 30, briMax = 200;

    public HSBThreshold(PApplet parent) {
        p = parent;
    }

    /**
     * @param params
     *            6 elements (hue, saturation, brightness), for each min and max
     *            value.
     */
    @Override
    public PImage apply(PImage src, float... params) {
        if (params != null && params.length == 6) {
            hueMin = params[0];
            hueMax = params[1];
            satMin = params[2];
            satMax = params[3];
            briMin = params[4];
            briMax = params[5];
        }

        PImage result = p.createImage(src.width, src.height, PConstants.RGB);

        for (int y = 0; y < src.height; ++y) {
            for (int x = 0; x < src.width; ++x) {
                int pix = src.get(x, y);
                float hue = p.hue(pix);
                float bri = p.brightness(pix);
                float sat = p.saturation(pix);

                int c = (hue > hueMin && hue < hueMax &&
                         sat > satMin && sat < satMax &&
                         bri > briMin && bri < briMax) ? pix : p.color(0);

                result.set(x, y, c);
            }
        }
        return result;
    }

    /**
     * Default value for HSB : 80, 140, 80, 255, 30, 200
     */
    @Override
    public PImage apply(PImage src) {
        return apply(src, hueMin, hueMax, satMin, satMax, briMin, briMax);
    }
}
