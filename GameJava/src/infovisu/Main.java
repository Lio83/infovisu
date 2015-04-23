package infovisu;

import java.util.ArrayList;

import processing.core.*;
import processing.event.MouseEvent;

@SuppressWarnings("serial")
public final class Main extends PApplet {

    final static PVector plate = new PVector(300, 20, 300);
    final static float minX = -plate.x / 2;
    final static float maxX = plate.x / 2;
    final static float minY = -plate.z / 2;
    final static float maxY = plate.z / 2;
    final static int plateColor = 0xFF10AA10;
    boolean editMode = false;

    // ============================================== Angles
    final static float MIN_ANGLE = -PI / 3;
    final static float MAX_ANGLE = PI / 3;
    float xAngle = 0f;
    float yAngle = 0f;
    float zAngle = 0f;

    // ========================================= Sensitivity
    final float MIN_SENSI = 0.1f;
    final float MAX_SENSI = 1.0f;
    float sensi = 0.5f;

    // =============================================== Mover
    Mover ball = new Mover(this);

    Score score = new Score(this);

    HScrollbar scrollbar;

    // =========================================== Cylinders
    ArrayList<PVector> cylinders = new ArrayList<PVector>();

    // ======================================================
    // Utility function for clamping values
    // ======================================================
    static float clamp(float a, float min_a, float max_a) {
        if (a > max_a) return max_a;
        else if (a < min_a) return min_a;
        else return a;
    }

    // ======================================================
    // Utility function for the current mouse position
    // ======================================================
    private PVector getPosition() {
        float x = clamp(mouseX - width / 2, minX, maxX);
        float y = clamp(mouseY - height / 2, minY, maxY);
        return new PVector(x, y);
    }

    // ======================================================
    // Setup
    // ======================================================
    public void setup() {
        this.size(800, 600, P3D);
        this.score.setupScore();
        this.scrollbar = new HScrollbar(this, 225, height - 25, Score.chartWidth, 15);
    }

    // ======================================================
    // Draw
    // ======================================================
    public void draw() {
        background(180, 250, 250);

        // ------- Text sensitivity in the upper left corner
        fill(0);
        int s = ((int) (sensi * 10)) * 10;
        text("Sensitivity = " + s + "%", 10, 20);

        // --------------------------------- Add score board
        score.renderScore();
        scrollbar.update();
        scrollbar.display();

        // ------------------ Coordinates at window's center
        translate(width / 2, height / 2, 0);

        // ======================================= GAME MODE
        if (!editMode) {
            pushMatrix();
            lights();
            // ----------------------------- Plate rotations
            rotateX(xAngle);
            rotateY(yAngle);
            rotateZ(zAngle);
            // ----------------------------- Plate rendering
            fill(plateColor);
            noStroke();
            box(plate.x, plate.y, plate.z);
            // ------------------------- Cylinders rendering
            for (PVector c : cylinders)
                (new Cylinder(this, c)).render();
            // ------------------------------ Ball rendering
            ball.render();
            popMatrix();
        }
        // ======================================= EDIT MODE
        else {
            // -------------------------- Plate 2D rendering
            fill(plateColor);
            rect(minX, minY, plate.x, plate.z);
            // ----------------------- Cylinders 2D rendering
            for (PVector c : cylinders)
                (new Cylinder(this, c)).render2D();
            // ---------------------------- Ball 2D rendering
            ball.render2D();
            // ------------------ Mouse cylinder 2D rendering
            (new Cylinder(this, getPosition())).render2D();
        }
    }

    // ======================================================
    // Mouse and Keayboard events
    // ======================================================

    // ========================================== Mouse drag
    public void mouseDragged() {
        if (!editMode && !scrollbar.locked) {
            zAngle += sensi * (mouseX - pmouseX) * 0.01;
            xAngle += sensi * (pmouseY - mouseY) * 0.01;
            zAngle = clamp(zAngle, MIN_ANGLE, MAX_ANGLE);
            xAngle = clamp(xAngle, MIN_ANGLE, MAX_ANGLE);
        }
    }

    // ========================================= Mouse wheel
    public void mouseWheel(MouseEvent event) {
        sensi += event.getCount() * 0.1;
        sensi = clamp(sensi, MIN_SENSI, MAX_SENSI);
    }

    // ========================================= Mouse click
    public void mouseClicked() {
        if (editMode) cylinders.add(getPosition());
    }

    // ========================================= Key release
    public void keyReleased() {
        if (key == CODED) {
            if (keyCode == SHIFT) editMode = false;
        }
    }

    // =========================================== Key press
    public void keyPressed() {
        if (key == CODED) {
            if (keyCode == SHIFT) editMode = true;
        }
    }

}
