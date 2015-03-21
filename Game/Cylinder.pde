// ======================================================
//   Cylinder constants
// ======================================================
final float cylBaseSize = 30; 
final float cylinderHeight = 40; 
final int cylinderResolution = 40;
final float cylZ = -plate.y/2;
final int cylinderColor = 0xFF1010FF;

// ======================================================
//   Cylinder
// ======================================================
class Cylinder {
  private PShape side, top, base, cylinder;
  private PVector pos;
  
// =========================================  Constructor
  Cylinder(PVector v) {
    this.pos = v;
    initCylinder();
  }

// ======================================= Initialization
  private void initCylinder() {
    fill(cylinderColor);
    // -----------------------------------  Points arrays
    float angle;
    float[] x = new float[cylinderResolution + 1]; 
    float[] y = new float[cylinderResolution + 1];
    for(int i = 0; i < x.length; i++) {
      angle = (TWO_PI / cylinderResolution) * i; 
      x[i] = sin(angle) * cylBaseSize; 
      y[i] = cos(angle) * cylBaseSize;
    }
    // ---------------------------------------  Side part
    side = createShape();
    side.beginShape(QUAD_STRIP);
    //draw the border of the cylinder
    for(int i = 0; i < x.length; i++) { 
      side.vertex(x[i], y[i] , 0); 
      side.vertex(x[i], y[i], cylinderHeight);
    }
    side.endShape();
    // ---------------------------------------  Base part
    base = createShape();
    base.beginShape(TRIANGLE_FAN);
    base.vertex(0,0,0);
    for(int i = 0; i < x.length; i++) {
      base.vertex(x[i], y[i], 0);
    }
    base.endShape();
    // ----------------------------------------  Top part
    top = createShape();
    top.beginShape(TRIANGLE_FAN);
    top.vertex(0,0,cylinderHeight);
    for(int i = 0; i < x.length; i++) {
      top.vertex(x[i], y[i], cylinderHeight);
    }
    top.endShape();
    // ----------------------------------  Entire cylinder
    cylinder = createShape(GROUP);
    cylinder.addChild(side);
    cylinder.addChild(base);
    cylinder.addChild(top);
  }

// ============================================= Render 2D
  void render2D() {
    fill(cylinderColor);
    float size = 2*cylBaseSize;
    ellipse(pos.x, pos.y, size, size);
  }

// ================================================ Render
  void render() {
    pushMatrix();
    translate(pos.x, cylZ, pos.y);
    rotateX(HALF_PI); //to change orientation
    fill(cylinderColor);
    shape(cylinder);
    popMatrix();
  }
}

