package cs211.imageprocessing;

import java.util.ArrayList;

import cs211.imageprocessing.transformers.*;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

@SuppressWarnings("serial")
public class ImageProcessing extends PApplet {

    PImage src;
    ImageTransformer hsb, blur, binary, sobel, hough;
    QuadGraph QG;

    @Override
    public void setup() {
        size(1600, 600);
        src = loadImage("board1.jpg");

        hsb = new HSBThreshold(this);
        blur = new GaussianBlur(this);
        binary = new BinaryThreshold(this);
        sobel = new Sobel(this);
        hough = new Hough(this);

        QG = new QuadGraph(this);

        noLoop(); // no refresh
    }

    @Override
    public void draw() {
        background(color(0, 0, 0));

        PImage h, b, t, s;

        h = hsb.apply(src);
        b = blur.apply(h, 10);
        t = binary.apply(b);
        s = sobel.apply(t);

        image(src, 0, 0);
        hough.apply(s, 4, 180);

        h.resize(400, 300);
        b.resize(400, 300);
        t.resize(400, 300);
        s.resize(400, 300);

        image(h, 800, 0);
        image(b, 1200, 0);
        image(t, 800, 300);
        image(s, 1200, 300);

        ArrayList<PVector> intersections = hough.intersections;
        getIntersection(lines);
        // QG.build(lines, width, img.height);
    }
}
