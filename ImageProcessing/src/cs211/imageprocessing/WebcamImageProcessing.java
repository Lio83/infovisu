package cs211.imageprocessing;

import java.util.ArrayList;
import java.util.Random;

import cs211.imageprocessing.transformers.*;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.video.*;

@SuppressWarnings("serial")
public class WebcamImageProcessing extends PApplet {

	Capture cam;
    PImage src;
    ImageTransformer hsb, blur, binary, sobel;
    Hough hough;
    QuadGraph QG;

    @Override
    public void setup() {
        size(1280, 480);
        
        String[] cameras = Capture.list();
        if (cameras.length == 0) {
            println("There are no cameras available for capture.");
            exit();
        } else {
            println("Available cameras:");
            for (int i = 0; i < cameras.length; i++) {
                println(cameras[i]);
            }
            cam = new Capture(this, cameras[0]);
            cam.start();
        }
        
        hsb = new HSBThreshold(this);
        blur = new GaussianBlur(this);
        binary = new BinaryThreshold(this);
        sobel = new Sobel(this);
        hough = new Hough(this);

        QG = new QuadGraph(this);

    }

    @Override
    public void draw() {
        background(color(0, 0, 0));
        if (cam.available() == true) {
            cam.read();
        
        src = cam.get();
        
        PImage h, b, t, s;

        h = hsb.apply(src, 80, 140, 80, 255, 80, 200);
        b = blur.apply(h, 40);
        t = binary.apply(b);
        s = sobel.apply(t);

        image(src, 0, 0);
        hough.apply(s, 6, 120);
        //hough.intersections(src);

        h.resize(320, 240);
        b.resize(320, 240);
        t.resize(320, 240);
        s.resize(320, 240);

        image(h, 640, 0);
        image(b, 960, 0);
        image(t, 640, 240);
        image(s, 960, 240);
        
        if (true) {
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
}
