package infovisu;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Mover representation with simple physics.
 * 
 * @author Lionel, Guillaume, Yannick
 *
 */
class Mover implements GameParameters {

    private final static float COLLISION_DIST = CYLINDER_RADIUS + SPHERE_RADIUS;

    final PVector location;
    final PVector velocity;
    private final PVector gravity;

    private final PApplet p;

    Mover(PApplet parent) {
        p = parent;
        location = new PVector();
        velocity = new PVector();
        gravity = new PVector();
    }

    private void update(float xAngle, float zAngle) {
        // Gravity
        gravity.x = PApplet.sin(zAngle) * G;
        gravity.y = -PApplet.sin(xAngle) * G;
        // Friction
        PVector friction = velocity.get();
        friction.normalize();
        friction.mult(-MU);
        // Velocity
        gravity.add(friction);
        velocity.add(gravity);
        // Location
        location.add(velocity);
    }

    private void checkEdges() {
        if (location.x >= MAX_X || location.x <= MIN_X) {
            velocity.x *= -REBOUND;
            location.x = PApplet.constrain(location.x, MIN_X, MAX_X);
            // p.score.changeScore(-velocity.mag());
        }
        if (location.y >= MAX_Y || location.y <= MIN_Y) {
            velocity.y *= -REBOUND;
            location.y = PApplet.constrain(location.y, MIN_Y, MAX_Y);
            // p.score.changeScore(-velocity.mag());
        }
    }

    float checkCylinderCollision(Cylinder c) {
        // --------------------------------- Normal vector
        PVector n = PVector.sub(location, c.position);
        // --------------------------- Collision detection
        if (n.mag() <= COLLISION_DIST) {
            n.setMag(COLLISION_DIST);
            // -------------------- Location out of cylinder
            PVector v = PVector.add(c.position, n);
            location.set(v);
            checkEdges(); // avoids side effects
            // ---------------------------- Cylinder rebound
            n.normalize();
            float d = -2.0f * velocity.dot(n) * REBOUND;
            n.mult(d);
            velocity.add(n);
            return velocity.mag();
        }
        return 0f;
    }

    public void render2D() {
        p.noStroke();
        p.fill(BALL_COLOR);
        float size = 2 * SPHERE_RADIUS;
        p.ellipse(location.x, location.y, size, size);
    }

    public void render(float xAngle, float zAngle) {
        p.noStroke();
        p.fill(BALL_COLOR);
        checkEdges();
        update(xAngle, zAngle);
        p.pushMatrix();
        p.translate(location.x, MOVER_Z, location.y);
        p.sphere(SPHERE_RADIUS);
        p.popMatrix();
    }
}
