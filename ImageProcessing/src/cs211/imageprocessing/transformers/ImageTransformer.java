package cs211.imageprocessing.transformers;

import processing.core.PImage;

public interface ImageTransformer {

    /**
     * Transforms the image with the given parameters.
     *
     * @param src
     *            source image.
     * @param params
     *            (defined in implementing class).
     * @return the modified image.
     */
    public PImage apply(PImage src, float... params);

    /**
     * Transforms the image with the defaults parameters.
     *
     * @param src
     *            source image.
     * @return the modified image.
     */
    public PImage apply(PImage src);
}
