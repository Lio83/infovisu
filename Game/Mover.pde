final float G = 1.0;
final float mu = 0.1;
final float rebound = 0.7;

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
    for(PVector c : cylinders) {
      PVector n = location.get();
      n.sub(c);
      float dist = cylinderBaseSize + sphereSize;
      if(n.mag() <= dist) {
        location.set(PVector.add(n,c));
        n.normalize();
        float d = -2.0 * velocity.dot(n);
        velocity.add(PVector.mult(n,d));
      }
    }
  }

  void checkEdges() {
    if (location.x >= box.x/2 || location.x <= -box.x/2) {
      velocity.x *= -rebound;
      location.x = clamp(location.x, -box.x/2, box.x/2);
    }
    if (location.y >= box.z/2 || location.y <= -box.z/2) {
      velocity.y *= -rebound;
      location.y = clamp(location.y, -box.z/2, box.z/2);
    }
    
  }
  
  void render2D() {
    fill(255,20,20);
    ellipse(mover.location.x, mover.location.y, 2*sphereSize, 2*sphereSize);    
  }
  
  void render() {
    checkEdges();
    update();
    pushMatrix();
    translate(location.x, -sphereSize-box.y/2, location.y);
    fill(255,20,20);
    sphere(sphereSize);
    popMatrix();
  }
}
