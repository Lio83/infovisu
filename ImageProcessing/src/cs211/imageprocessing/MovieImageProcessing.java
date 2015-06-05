package cs211.imageprocessing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.video.Movie;
import cs211.imageprocessing.transformers.BinaryThreshold;
import cs211.imageprocessing.transformers.GaussianBlur;
import cs211.imageprocessing.transformers.HSBThreshold;
import cs211.imageprocessing.transformers.Hough;
import cs211.imageprocessing.transformers.ImageTransformer;
import cs211.imageprocessing.transformers.Sobel;

@SuppressWarnings("serial")
public class MovieImageProcessing extends PApplet {

    // Capture cam;
    Movie cam;
    PImage src;
    ImageTransformer hsb, blur, binary, sobel;
    Hough hough;
    QuadGraph QG;
    TwoDThreeD D3D;
    
    @Override
    public void setup() {
        size(1280, 480);

        cam = new Movie(this, "testvideo.mp4");
        cam.loop();

        hsb = new HSBThreshold(this);
        blur = new GaussianBlur(this);
        binary = new BinaryThreshold(this);
        sobel = new Sobel(this);
        hough = new Hough(this);

        QG = new QuadGraph(this);
        D3D = new TwoDThreeD(640, 480);
    }

    @Override
    public void draw() {
        background(color(0, 0, 0));
        if (cam.available() == true) {
            cam.read();

            src = cam.get();

            PImage h, b, t, s;

            h = hsb.apply(src, 100, 140, 130, 255, 70, 220);
            b = blur.apply(h, 10);
            t = binary.apply(b);
            s = sobel.apply(t);
            hough.apply(s, 6, 160);
            image(src, 0, 0);

            hough.intersections(s);
            ArrayList<PVector> lines = hough.getLines(src);
            
            //hough.getIntersections(lines);
            ArrayList<PVector> quad = QG.build(lines, src.width, src.height);
            
            if (!quad.isEmpty()) {
                sortCorners(quad);
                for (PVector p : quad) {
                    //Afiche les points.
                    //System.out.println("point: " + p.x + "," + p.y + "," + p.z);
                    p.z = 1f;
                }
                PVector rots = D3D.get3DRotations(quad);

                // Affiche les angles.
                System.out.println("angles: " + PApplet.degrees(rots.x) + "," + PApplet.degrees(rots.y) + ","
                        + PApplet.degrees(rots.z));
            }

            
             
            //
            // System.out.println("angles: " + PApplet.degrees(rots.x) + "," +
            // PApplet.degrees(rots.y) + ","
            // + PApplet.degrees(rots.z));

            h.resize(320, 240);
            b.resize(320, 240);
            t.resize(320, 240);
            s.resize(320, 240);
            image(h, 640, 0);
            image(b, 960, 0);
            image(t, 640, 240);
            image(s, 960, 240);
        }
    }

    static class CWComparator implements Comparator<PVector> {
        PVector center;

        public CWComparator(PVector center) {
            this.center = center;
        }

        @Override
        public int compare(PVector b, PVector d) {
            if (Math.atan2(b.y - center.y, b.x - center.x) < Math.atan2(d.y - center.y, d.x - center.x)) return -1;
            else return 1;
        }
    }

    public static void sortCorners(ArrayList<PVector> quad) {
        // Sort corners so that they are ordered clockwise
        PVector a = quad.get(0);
        PVector b = quad.get(2);
        PVector center = new PVector((a.x + b.x) / 2, (a.y + b.y) / 2);
        Collections.sort(quad, new CWComparator(center));
        int min = 0;
        float dist = 10e6f;
        for (int i = 0; i < quad.size(); ++i) {
            float d = quad.get(i).magSq();
            if (d < dist) {
                dist = d;
                min = i;
            }
        }
        Collections.rotate(quad, -min);
        //return quad;
    }
}
