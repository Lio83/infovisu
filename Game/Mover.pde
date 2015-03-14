class Mover { 
  PVector location;
  PVector velocity;
  PVector gravity;
  PVector friction;
  
  final float G = 1.0;
  final float mu = 0.1;
  final float rebound = 0.7;

  Mover() {
    location = new PVector(0,0,-sphereSize-box.y/2); 
    velocity = new PVector(0,0,0);
    gravity = new PVector(0,0,0);
    friction = new PVector(0,0,0);
  }

  void update() {
    gravity.x = sin(zAngle) * G;
    gravity.y = sin(xAngle) * G;
    friction = velocity.get();
    friction.normalize();
    friction.mult(-mu);
    gravity.add(friction);
    velocity.add(gravity);
    location.add(velocity);
  }

  void display() {
    stroke(0);
    strokeWeight(2);
    fill(127);
    ellipse(location.x, location.y, 48, 48);
  }

  void checkEdges() {
    if (location.x >= box.x/2 || location.x <= -box.x/2) {
      velocity.x *= -rebound;
      if (location.x > box.x/2) location.x = box.x/2;
      if (location.x < -box.x/2) location.x = -box.x/2;
    }
    if (location.y >= box.z/2 || location.y <= -box.z/2) {
      velocity.y *= -rebound;
      if (location.y > box.z/2) location.y = box.z/2;
      if (location.y < -box.z/2) location.y = -box.z/2;
    }
  } 
}
