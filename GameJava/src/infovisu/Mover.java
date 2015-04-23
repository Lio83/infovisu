package infovisu;

import processing.core.*;
import infovisu.Main;

public class Mover {
 // ======================================================
//  Mover constants
//======================================================
final static float sphereSize = 20.0f;
final static float G = 1.0f;
final static float mu = 0.1f;
final static float rebound = 0.7f;
final static float collisionDist = Cylinder.cylBaseSize + sphereSize;
final static float moverZ = -sphereSize-Main.plate.y/2;
final static int ballColor = 0xFFAA1010;

//======================================================
//  Mover
//======================================================
 PVector location = new PVector();
 PVector velocity = new PVector();
 private PVector gravity = new PVector();
 private Main p;
 
 Mover(PApplet parent) {
     p = (Main)parent;
 }

//======================================  Update physics
 private void update() {
   // -----------------------------------------  Gravity
   gravity.x = PApplet.sin(p.zAngle) * G;
   gravity.y = -PApplet.sin(p.xAngle) * G;
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

//===================================  Check for rebound
 private void checkEdges() {
   // ------------------------------------  coordinate X
   if (location.x >= Main.maxX || location.x <= Main.minX) {
     velocity.x *= -rebound;
     location.x = Main.clamp(location.x, Main.minX, Main.maxX);
     p.score.changeScore(-velocity.mag());
   }
   // ------------------------------------- coordinate Y
   if (location.y >= Main.maxY || location.y <= Main.minY) {
     velocity.y *= -rebound;
     location.y = Main.clamp(location.y, Main.minY, Main.maxY);
     p.score.changeScore(-velocity.mag());
   }
 }

//=======================  Check for cylinder collisions
 private void checkCylinderCollision() {
   for(PVector c : p.cylinders) {
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
       float d = -2.0f * velocity.dot(n) * rebound;
       n.mult(d);
       velocity.add(n);
       p.score.changeScore(velocity.mag());
     }
   }
 }

//===========================================  Render 2D
 public void render2D() {
   p.noStroke();
   p.fill(ballColor);
   float size = 2 * sphereSize;
   p.ellipse(location.x, location.y, size, size);
 }
 
//==============================================  Render
 public void render() {
     p.noStroke();
     p.fill(ballColor);
     checkCylinderCollision();
   checkEdges();
   update();
   p.pushMatrix();
   p.translate(location.x, moverZ, location.y);
   p.sphere(sphereSize);
   p.popMatrix();
 }
}

