// ======================================================
//   Mover constants
// ======================================================
final float sphereSize = 20.0;
final float G = 1.0;
final float mu = 0.1;
final float rebound = 0.7;
final float collisionDist = cylBaseSize + sphereSize;
final float moverZ = -sphereSize-plate.y/2;
final int ballColor = 0xFFFF1010;

// ======================================================
//   Mover
// ======================================================
class Mover {
  private PVector location = new PVector();
  private PVector velocity = new PVector();
  private PVector gravity = new PVector();

// ======================================  Update physics
  private void update() {
    // -----------------------------------------  Gravity
    gravity.x = sin(zAngle) * G;
    gravity.y = -sin(xAngle) * G;
    // ----------------------------------------  Friction
    PVector friction = velocity.get();
    friction.normalize();
    friction.mult(-mu);
    // ----------------------------------------  Velocity
    gravity.add(friction);
    velocity.add(gravity);
    // ----------------------------------------  Location
    location.add(velocity);
  }

// ===================================  Check for rebound
  private void checkEdges() {
    // ------------------------------------  coordinate X
    if (location.x >= maxX || location.x <= minX) {
      velocity.x *= -rebound;
      location.x = clamp(location.x, minX, maxX);
    }
    // ------------------------------------- coordinate Y
    if (location.y >= maxY || location.y <= minY) {
      velocity.y *= -rebound;
      location.y = clamp(location.y, minY, maxY);
    }
  }

// =======================  Check for cylinder collisions
  private void checkCylinderCollision() {
    for(PVector c : cylinders) {
      // ---------------------------------  Normal vector
      PVector n = PVector.sub(location, c);
      // ---------------------------  Collision detection
      if(n.mag() <= collisionDist) {
        n.setMag(collisionDist);
        // --------------------  Location out of cylinder
        PVector v = PVector.add(c, n);
        location.set(v);
        checkEdges(); // avoids side effects
        // ----------------------------  Cylinder rebound
        n.normalize();
        float d = -2.0 * velocity.dot(n) * rebound;
        n.mult(d);
        velocity.add(n);
      }
    }
  }

// ===========================================  Render 2D
  void render2D() {
    fill(ballColor);
    float size = 2 * sphereSize;
    ellipse(location.x, location.y, size, size);
  }
  
// ==============================================  Render
  void render() {
    checkCylinderCollision();
    checkEdges();
    update();
    pushMatrix();
    translate(location.x, moverZ, location.y);
    fill(ballColor);
    sphere(sphereSize);
    popMatrix();
  }
}
