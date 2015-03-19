final float sphereSize = 20.0;
final float G = 1.0;
final float mu = 0.1;
final float rebound = 0.7;
final float collisionDist = cylinderBaseSize + sphereSize;
final float moverZ = -sphereSize-plate.y/2;
final int ballColor = 0xFFFF1010;

class Mover {
  PVector location = new PVector(0,0);
  PVector velocity = new PVector(0,0);
  PVector gravity = new PVector(0,0);

  void update() {
    gravity.x = sin(zAngle) * G;
    gravity.y = -sin(xAngle) * G;
    PVector friction = velocity.get();
    friction.normalize();
    friction.mult(-mu);
    gravity.add(friction);
    velocity.add(gravity);
    location.add(velocity);
  }

  void checkEdges() {
    if (location.x >= maxX || location.x <= minX) {
      velocity.x *= -rebound;
      location.x = clamp(location.x, minX, maxX);
    }
    if (location.y >= maxY || location.y <= minY) {
      velocity.y *= -rebound;
      location.y = clamp(location.y, minY, maxY);
    }
  }
  
  void checkCylinderCollision() {
    PVector n;
    for(PVector c : cylinders) {
      n = PVector.sub(location, c);
      if(n.mag() <= collisionDist) {
        n.setMag(collisionDist);
        PVector newLocation = PVector.add(c, n);
        location.set(newLocation);
        checkEdges();
        // cylinder rebound
        n.normalize();
        float d = -2.0 * velocity.dot(n) * rebound;
        n.mult(d);
        velocity.add(n);
      }
    }
  }
  
  void render2D() {
    fill(ballColor);
    ellipse(location.x, location.y, 2*sphereSize, 2*sphereSize);
  }
  
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
