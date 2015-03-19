final float cylinderBaseSize = 20; 
final float cylinderHeight = 40; 
final int cylinderResolution = 40;
  
  
class Cylinder {
  PShape openCylinder, top, bottom, cylinder;
  float xpos, ypos;

  Cylinder(float xpos, float ypos) {
    this.xpos = xpos;
    this.ypos = ypos;
    float angle;
    float[] x = new float[cylinderResolution + 1]; 
    float[] y = new float[cylinderResolution + 1];
    //get the x and y position on a circle for all the sides
    for(int i = 0; i < x.length; i++) {
      angle = (TWO_PI / cylinderResolution) * i; 
      x[i] = sin(angle) * cylinderBaseSize; 
      y[i] = cos(angle) * cylinderBaseSize;
    }
    fill(170);
    openCylinder = createShape();
    openCylinder.beginShape(QUAD_STRIP);
    //draw the border of the cylinder
    for(int i = 0; i < x.length; i++) { 
      openCylinder.vertex(x[i], y[i] , 0); 
      openCylinder.vertex(x[i], y[i], cylinderHeight);
    }
    openCylinder.endShape();
  
    top = createShape();
    top.beginShape(TRIANGLE_FAN);
    top.vertex(0,0,0);
    for(int i = 0; i < x.length; i++) {
      top.vertex(x[i], y[i], 0);
    }
    top.endShape();
  
    bottom = createShape();
    bottom.beginShape(TRIANGLE_FAN);
    bottom.vertex(0,0,cylinderHeight);
    for(int i = 0; i < x.length; i++) {
      bottom.vertex(x[i], y[i], cylinderHeight);
    }
    bottom.endShape();
  
    cylinder = createShape(GROUP);
    cylinder.addChild(openCylinder);
    cylinder.addChild(bottom);
    cylinder.addChild(top);
  }
  
  void render2D() {
    pushMatrix();
    translate(xpos, ypos, 0);
    shape(cylinder);
    popMatrix();
  }
  
  void render() {
    pushMatrix();
    translate(xpos, -box.y/2, ypos);
    rotateX(HALF_PI);
    shape(cylinder);
    popMatrix();
  }
}
