package cs211.imageprocessing;

import cs211.imageprocessing.transformers.*;
import processing.core.PApplet;
import processing.core.PImage;
import processing.video.*;

@SuppressWarnings("serial")
public class WebcamImageProcessing extends PApplet {

	Capture cam;
    PImage src;
    ImageTransformer hsb, blur, binary, sobel, hough;
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

        // QG = new QuadGraph(this);

    }

    @Override
    public void draw() {
        background(color(0, 0, 0));
        if (cam.available() == true) {
            cam.read();
        
        src = cam.get();
        
        PImage h, b, t, s;

        h = hsb.apply(src);
        b = blur.apply(h, 10);
        t = binary.apply(b);
        s = sobel.apply(t, .2f);

        image(src, 0, 0);
        hough.apply(s, 6, 120);

        h.resize(320, 240);
        b.resize(320, 240);
        t.resize(320, 240);
        s.resize(320, 240);

        image(h, 640, 0);
        image(b, 960, 0);
        image(t, 640, 240);
        image(s, 960, 240);

        // ArrayList<PVector> intersections = hough.intersections;
        // getIntersection(lines);
        // QG.build(lines, width, img.height);
        }
    }
}
