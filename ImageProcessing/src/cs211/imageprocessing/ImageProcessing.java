package cs211.imageprocessing;

import java.util.ArrayList;

import cs211.imageprocessing.transformers.*;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

@SuppressWarnings("serial")
public class ImageProcessing extends PApplet {

    PImage src;
    ImageTransformer hsb, blur, binary, sobel;
    Hough hough;
    QuadGraph QG;

    @Override
    public void setup() {
        size(1600, 480);
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

        ArrayList<PVector> intersections = hough.intersections;
        getIntersection(lines);
        // QG.build(lines, width, img.height);
    }
}
