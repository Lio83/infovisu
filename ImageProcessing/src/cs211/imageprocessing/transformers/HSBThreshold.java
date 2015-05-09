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
        if (params.length == 6) {
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

                float hue = p.hue(src.get(x, y));
                float bri = p.brightness(src.get(x, y));
                float sat = p.saturation(src.get(x, y));

                int c = hue > hueMin && hue < hueMax && sat > satMin && sat < satMax && bri > briMin && bri < briMax ? src
                        .get(x, y) : p.color(0);

                result.set(x, y, c);
            }
        }

        return result;
    }

    @Override
    public PImage apply(PImage src) {
        return apply(src, hueMin, hueMax, satMin, satMax, briMin, briMax);
    }
}
