package algorithms;

import processing.core.*;
import processing.video.*;

@SuppressWarnings("serial")
public final class HoughTransform extends PApplet {
    Capture cam;
    PImage img;
    
    private final Hough algo = new Hough(this);

    public void setup() {
        size(640, 480);
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
    }

    public void draw() {
        if (cam.available() == true) {
            cam.read();
        }
        img = cam.get();
//        TODO : call hough algorithm to compute img
//        algo.hough(img);
        image(img, 0, 0);
    }
}
