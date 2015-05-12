package cs211.imageprocessing;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import cs211.imageprocessing.transformers.*;

@SuppressWarnings("serial")
public class ImageProcessing extends PApplet {

    PImage src;
    ImageTransformer hsb, blur, binary, sobel;
    Hough hough;
    QuadGraph QG;
    boolean isGraph = true;

    @Override
    public void setup() {
        size(1280, 480);
        src = loadImage("board4.jpg");

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
        hough.apply(s, 4, 150);
        //hough.intersections(src);

        h.resize(320, 240);
        b.resize(320, 240);
        t.resize(320, 240);
        s.resize(320, 240);

        image(h, 640, 0);
        image(b, 960, 0);
        image(t, 640, 240);
        image(s, 960, 240);

        if (isGraph) {
            ArrayList<PVector> lines = hough.getLines(src);
            hough.intersections(src, null);
            QG.build(lines, src.width, src.height);

            for (int[] quad : QG.cycles) {
                PVector l1 = lines.get(quad[0]);
                PVector l2 = lines.get(quad[1]);
                PVector l3 = lines.get(quad[2]);
                PVector l4 = lines.get(quad[3]);
                // (intersection() is a simplified version of the
                // intersections() method you wrote last week, that simply
                // return the coordinates of the intersection between 2 lines)
                PVector c12 = QuadGraph.intersection(l1, l2);
                PVector c23 = QuadGraph.intersection(l2, l3);
                PVector c34 = QuadGraph.intersection(l3, l4);
                PVector c41 = QuadGraph.intersection(l4, l1);
                // Choose a random, semi-transparent colour
                Random random = new Random();
                fill(color(min(255, random.nextInt(300)), min(255, random.nextInt(300)), min(255, random.nextInt(300)),
                        50));
                quad(c12.x, c12.y, c23.x, c23.y, c34.x, c34.y, c41.x, c41.y);
            }
        }
    }
}
