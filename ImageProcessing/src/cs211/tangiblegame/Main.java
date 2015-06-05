package cs211.tangiblegame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.MouseEvent;
import processing.video.Movie;
import cs211.imageprocessing.QuadGraph;
import cs211.imageprocessing.TwoDThreeD;
import cs211.imageprocessing.transformers.BinaryThreshold;
import cs211.imageprocessing.transformers.GaussianBlur;
import cs211.imageprocessing.transformers.HSBThreshold;
import cs211.imageprocessing.transformers.Hough;
import cs211.imageprocessing.transformers.ImageTransformer;
import cs211.imageprocessing.transformers.Sobel;

/**
 * Main class of the Game project.
 *
 * @author Lionel, Guillaume, Yannick
 * 
 */
@SuppressWarnings("serial")
public final class Main extends PApplet implements GameParameters {

    private Mover mover;
    private Score score;
    private ArrayList<Cylinder> cylinders;
    private HScrollbar scrollbar;

    // Angles of the plate in 3D.
    private float xAngle = 0f;
    private float yAngle = 0f;
    private float zAngle = 0f;

    // Sensitivity of the movements
    private float sensitivity = 0.5f;

    private boolean gameMode = true;

    // Movie renderer.
    Movie cam;

    // Image transformers.
    PImage src;
    ImageTransformer hsb, blur, binary, sobel;
    Hough hough;
    QuadGraph QG;
    TwoDThreeD D3D;

    @Override
    public void setup() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT, P3D);
        mover = new Mover(this);
        cylinders = new ArrayList<>();
        score = new Score(this);
        scrollbar = new HScrollbar(this, 225, WINDOW_HEIGHT - 25, Score.CHART_WIDTH, 15);
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
        background(BACKGROUND_COLOR);
        noStroke();

        // Text sensitivity in the upper left corner
        fill(0);
        int s = (int) (sensitivity * 10) * 10;
        text("Sensitivity = " + s + "%", 10, 20);

        // --------------------------------- Add score board
        score.renderScore(mover, cylinders);
        scrollbar.update();
        scrollbar.display();

        // ------------------ Coordinates at window's center
        pushMatrix();
        translate(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2, 0);

        // ======================================= GAME MODE
        if (gameMode) {
            if (cam.available()) {
                cam.read();
                src = cam.get();
                PImage r;
                r = hsb.apply(src, 100, 140, 130, 255, 70, 220);
                r = blur.apply(r, 10);
                r = binary.apply(r);
                r = sobel.apply(r);
                hough.apply(r, 6, 160);
                hough.intersections(r);
                ArrayList<PVector> lines = hough.getLines(src);
                ArrayList<PVector> quad = QG.build(lines, src.width, src.height);
                
                src.resize(240, 180);
                image(src, width / 2 -240, -height / 2);

                System.out.println(quad);
                if (!quad.isEmpty()) {
                    sortCorners(quad);
                    for (PVector p : quad) {
                        // Afiche les points.
                        // System.out.println("point: " + p.x + "," + p.y + ","
                        // + p.z);
                        p.z = 1f;
                    }
                    PVector rots = D3D.get3DRotations(quad);
                    xAngle = rots.x;
                    zAngle = -rots.y;
                    //zAngle = rots.z;
                    System.out.println("get there!");
                }

                pushMatrix();
                lights();
                // ----------------------------- Plate rotations
                rotateX(xAngle);
                rotateY(yAngle);
                rotateZ(zAngle);
                // ----------------------------- Plate rendering
                fill(PLATE_COLOR);
                box(PLATE_X, PLATE_Y, PLATE_Z);
                // ------------------------- Cylinders rendering
                for (Cylinder c : cylinders) {
                    c.render();
                    float ds = mover.checkCylinderCollision(c);
                    score.changeScore(ds, scrollbar.getPos());
                }
                // ------------------------------ Ball rendering
                mover.render(xAngle, zAngle);
                popMatrix();
            }
        } else {
            // -------------------------- Plate 2D rendering
            fill(PLATE_COLOR);
            rect(MIN_X, MIN_Y, PLATE_X, PLATE_Z);
            // ----------------------- Cylinders 2D rendering
            for (Cylinder c : cylinders) {
                c.render2D();
            }
            // ---------------------------- Ball 2D rendering
            mover.render2D();
            // ------------------ Mouse cylinder 2D rendering
            new Cylinder(this, getPosition()).render2D();
        }
        popMatrix();
    }

//    @Override
//    public void mouseDragged() {
//        if (gameMode && !scrollbar.locked) {
//            zAngle += 0.01f * sensitivity * (mouseX - pmouseX);
//            xAngle += 0.01f * sensitivity * (pmouseY - mouseY);
//            zAngle = constrain(zAngle, MIN_ANGLE, MAX_ANGLE);
//            xAngle = constrain(xAngle, MIN_ANGLE, MAX_ANGLE);
//        }
//    }
//
//    @Override
//    public void mouseWheel(MouseEvent event) {
//        sensitivity += 0.1f * event.getCount();
//        sensitivity = constrain(sensitivity, MIN_SENSI, MAX_SENSI);
//    }

    @Override
    public void mouseClicked() {
        if (!gameMode) cylinders.add(new Cylinder(this, getPosition()));
    }

    @Override
    public void keyReleased() {
        if (key == CODED && keyCode == SHIFT) gameMode = true;
    }

    @Override
    public void keyPressed() {
        if (key == CODED && keyCode == SHIFT) gameMode = false;
    }

    private PVector getPosition() {
        float x = constrain(mouseX - WINDOW_WIDTH / 2, MIN_X, MAX_X);
        float y = constrain(mouseY - WINDOW_HEIGHT / 2, MIN_Y, MAX_Y);
        return new PVector(x, y);
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
        // return quad;
    }

}
