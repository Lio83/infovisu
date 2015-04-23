package infovisu;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;

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

    @Override
    public void setup() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT, P3D);
        mover = new Mover(this);
        cylinders = new ArrayList<>();
        score = new Score(this);
        scrollbar = new HScrollbar(this, 225, WINDOW_HEIGHT - 25, Score.CHART_WIDTH, 15);
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

    @Override
    public void mouseDragged() {
        if (gameMode && !scrollbar.locked) {
            zAngle += 0.01f * sensitivity * (mouseX - pmouseX);
            xAngle += 0.01f * sensitivity * (pmouseY - mouseY);
            zAngle = constrain(zAngle, MIN_ANGLE, MAX_ANGLE);
            xAngle = constrain(xAngle, MIN_ANGLE, MAX_ANGLE);
        }
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        sensitivity += 0.1f * event.getCount();
        sensitivity = constrain(sensitivity, MIN_SENSI, MAX_SENSI);
    }

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
}
