package cs211.imageprocessing;

import processing.core.PApplet;
import processing.core.PImage;
import cs211.imageprocessing.transformers.BinaryThreshold;
import cs211.imageprocessing.transformers.GaussianBlur;
import cs211.imageprocessing.transformers.HSBThreshold;
import cs211.imageprocessing.transformers.Hough;
import cs211.imageprocessing.transformers.ImageTransformer;
import cs211.imageprocessing.transformers.Sobel;

@SuppressWarnings("serial")
public class ImageProcessing extends PApplet {

    PImage src;
    ImageTransformer hsb, blur, binary, sobel;
    Hough hough;

    @Override
    public void setup() {
        size(1600, 480);
        src = loadImage("board1.jpg");

        hsb = new HSBThreshold(this);
        blur = new GaussianBlur(this);
        binary = new BinaryThreshold(this);
        sobel = new Sobel(this);
        hough = new Hough(this);

        noLoop(); // no refresh
    }

    @Override
    public void draw() {
        background(color(0, 0, 0));

        PImage h, b, t, s;
        src.resize(640, 480);

        h = hsb.apply(src);
        b = blur.apply(h, 10);
        t = binary.apply(b);
        s = sobel.apply(t);

        image(src, 0, 0);
        h = hough.apply(s, 4, 150);
        hough.intersections(src);

        h.resize(320, 480);
        //b.resize(320, 240);
        //t.resize(320, 240);
        //s.resize(320, 240);

        image(h, 640, 0);
        //image(b, 960, 0);
        //image(t, 640, 240);
        image(s, 960, 0);

    }
}
