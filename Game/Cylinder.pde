final float cylinderBaseSize = 20; 
final float cylinderHeight = 40; 
final int cylinderResolution = 40;
final float cylinderZ = -plate.y/2;
final int cylinderColor = 0xFF1010FF;
  
class Cylinder {
  private PShape openCylinder, top, bottom, cylinder;
  private PVector pos;
  
  Cylinder(PVector v) {
    this.pos = v;
    createCylinder();
  }
  
  void render2D() {
    fill(cylinderColor);
    ellipse(pos.x, pos.y, 2*cylinderBaseSize, 2*cylinderBaseSize);
  }
  
  void render() {
    pushMatrix();
    translate(pos.x, cylinderZ, pos.y);
    rotateX(HALF_PI);
    fill(cylinderColor);
    shape(cylinder);
    popMatrix();
  }
  
  private void createCylinder() {
    fill(cylinderColor);
    float angle;
    float[] x = new float[cylinderResolution + 1]; 
    float[] y = new float[cylinderResolution + 1];
    //get the x and y position on a circle for all the sides
    for(int i = 0; i < x.length; i++) {
      angle = (TWO_PI / cylinderResolution) * i; 
      x[i] = sin(angle) * cylinderBaseSize; 
      y[i] = cos(angle) * cylinderBaseSize;
    }
    // Side part
    openCylinder = createShape();
    openCylinder.beginShape(QUAD_STRIP);
    //draw the border of the cylinder
    for(int i = 0; i < x.length; i++) { 
      openCylinder.vertex(x[i], y[i] , 0); 
      openCylinder.vertex(x[i], y[i], cylinderHeight);
    }
    openCylinder.endShape();
    // Bottom part
    bottom = createShape();
    bottom.beginShape(TRIANGLE_FAN);
    bottom.vertex(0,0,0);
    for(int i = 0; i < x.length; i++) {
      bottom.vertex(x[i], y[i], 0);
    }
    bottom.endShape();
    // Top part
    top = createShape();
    top.beginShape(TRIANGLE_FAN);
    top.vertex(0,0,cylinderHeight);
    for(int i = 0; i < x.length; i++) {
      top.vertex(x[i], y[i], cylinderHeight);
    }
    top.endShape();
    // Cylinder in its entire
    cylinder = createShape(GROUP);
    cylinder.addChild(openCylinder);
    cylinder.addChild(bottom);
    cylinder.addChild(top);
  }
}
