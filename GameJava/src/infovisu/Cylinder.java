package infovisu;

import processing.core.*;

public class Cylinder {
    // ======================================================
    // Cylinder constants
    // ======================================================
    final static float cylBaseSize = 30;
    final static float cylinderHeight = 40;
    final static int cylinderResolution = 40;
    final static float cylZ = -Main.plate.y / 2;
    final static int cylinderColor = 0xFF1010AA;

    // ======================================================
    // Cylinder
    // ======================================================
    private PShape side, top, base, cylinder;
    private PVector pos;

    private PApplet p;

    // ========================================= Constructor
    Cylinder(PApplet parent, PVector v) {
        p = parent;
        this.pos = v;
        initCylinder();
    }

    // ======================================= Initialization
    private void initCylinder() {
        p.fill(cylinderColor);
        // ----------------------------------- Points arrays
        float angle;
        float[] x = new float[cylinderResolution + 1];
        float[] y = new float[cylinderResolution + 1];
        for (int i = 0; i < x.length; i++) {
            angle = (PConstants.TWO_PI / cylinderResolution) * i;
            x[i] = PApplet.sin(angle) * cylBaseSize;
            y[i] = PApplet.cos(angle) * cylBaseSize;
        }
        // --------------------------------------- Side part
        side = p.createShape();
        side.beginShape(PConstants.QUAD_STRIP);
        // draw the border of the cylinder
        for (int i = 0; i < x.length; i++) {
            side.vertex(x[i], y[i], 0);
            side.vertex(x[i], y[i], cylinderHeight);
        }
        side.endShape();
        // --------------------------------------- Base part
        base = p.createShape();
        base.beginShape(PConstants.TRIANGLE_FAN);
        base.vertex(0, 0, 0);
        for (int i = 0; i < x.length; i++) {
            base.vertex(x[i], y[i], 0);
        }
        base.endShape();
        // ---------------------------------------- Top part
        top = p.createShape();
        top.beginShape(PConstants.TRIANGLE_FAN);
        top.vertex(0, 0, cylinderHeight);
        for (int i = 0; i < x.length; i++) {
            top.vertex(x[i], y[i], cylinderHeight);
        }
        top.endShape();
        // ---------------------------------- Entire cylinder
        cylinder = p.createShape(PConstants.GROUP);
        cylinder.addChild(side);
        cylinder.addChild(base);
        cylinder.addChild(top);
    }

    // ============================================= Render 2D
    void render2D() {
        p.fill(cylinderColor);
        p.noStroke();
        float size = 2 * cylBaseSize;
        p.ellipse(pos.x, pos.y, size, size);
    }

    // ================================================ Render
    void render() {
        p.fill(cylinderColor);
        p.noStroke();
        p.pushMatrix();
        p.translate(pos.x, cylZ, pos.y);
        p.rotateX(PConstants.HALF_PI); // to change orientation
        p.shape(cylinder);
        p.popMatrix();
    }
}
