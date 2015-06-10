package cs211.tangiblegame;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

/**
 * Cylinder representation for the Game.
 *
 * @author Lionel, Guillaume, Yannick
 *
 */
class Cylinder implements GameParameters {

    /**
     * Position of the cylinder's center.
     */
    final PVector position;

    private final PShape cylinder;
    private final PApplet p; // Parent needed.

    /**
     * Constructor of the Cylinder class.
     *
     * @param parent
     *            Caller of this constructor.
     * @param position
     *            Coordinates (x,y) of the center.
     */
    Cylinder(PApplet parent, PVector center) {
        p = parent;
        position = center;
        cylinder = initCylinder();
    }

    /**
     * Rendering cylinder in 2D.
     */
    void render2D() {
        p.fill(CYLINDER_COLOR);
        p.noStroke();
        float size = 2 * CYLINDER_RADIUS;
        p.ellipse(position.x, position.y, size, size);
    }

    /**
     * Rendering cylinder in 3D.
     */
    void render() {
        p.pushMatrix();
        p.fill(CYLINDER_COLOR);
        p.noStroke();
        p.translate(position.x, CYLINDER_Z, position.y);
        p.rotateX(PConstants.HALF_PI); // to change orientation
        p.shape(cylinder);
        p.popMatrix();
    }

    /**
     * Create cylinder.
     */
    private final PShape initCylinder() {
        // Points arrays
        float angle;
        float[] x = new float[CYLINDER_RESOLUTION + 1];
        float[] y = new float[CYLINDER_RESOLUTION + 1];
        for (int i = 0; i < x.length; i++) {
            angle = PConstants.TWO_PI / CYLINDER_RESOLUTION * i;
            x[i] = PApplet.sin(angle) * CYLINDER_RADIUS;
            y[i] = PApplet.cos(angle) * CYLINDER_RADIUS;
        }

        // Side part
        p.fill(CYLINDER_COLOR);
        PShape side = p.createShape();
        side.beginShape(PConstants.QUAD_STRIP);
        for (int i = 0; i < x.length; i++) {
            side.vertex(x[i], y[i], 0);
            side.vertex(x[i], y[i], CYLINDER_HEIGHT);
        }
        side.endShape();

        // Base part
        PShape base = p.createShape();
        base.beginShape(PConstants.TRIANGLE_FAN);
        base.vertex(0, 0, 0);
        for (int i = 0; i < x.length; i++)
            base.vertex(x[i], y[i], 0);
        base.endShape();

        // Top part
        PShape top = p.createShape();
        top.beginShape(PConstants.TRIANGLE_FAN);
        top.vertex(0, 0, CYLINDER_HEIGHT);
        for (int i = 0; i < x.length; i++)
            top.vertex(x[i], y[i], CYLINDER_HEIGHT);
        top.endShape();

        // Entire cylinder
        PShape cylinder = p.createShape(PConstants.GROUP);
        cylinder.addChild(side);
        cylinder.addChild(base);
        cylinder.addChild(top);

        return cylinder;
    }

}
