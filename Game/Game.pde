// Angles and limits +/-60 deg
final float MIN_ANGLE = -PI/3;
final float MAX_ANGLE = PI/3;
float xAngle = 0.0;
float yAngle = 0.0;
float zAngle = 0.0;

// Sensitivity and limits
final float MIN_SENSITIVITY = 0.1;
final float MAX_SENSITIVITY = 1.0;
float sensitivity = 0.5;

//  Plate dimensions and limits
final PVector plate = new PVector(400, 20, 400);
final float minX = -plate.x/2;
final float maxX = plate.x/2;
final float minY = -plate.z/2;
final float maxY = plate.z/2;
final int plateColor = 0xFF10FF10;

// Ball (Mover)
Mover ball = new Mover();

// Cylinders and mode checking boolean
boolean isShift = false;
ArrayList<PVector> cylinders = new ArrayList<PVector>();

// Auxilliary function for min/max values.
float clamp(float a, float min_a, float max_a) {
  if (a > max_a) return max_a;
  else if (a < min_a) return min_a;
  else return a;
}

void setup() { 
  size(800, 600, P3D);
  smooth();
}

void draw() {
  directionalLight(50, 100, 125, 0, 1, -1);
  //spotLight(51, 102, 126, 80, 20, 40, 1, 1, -1, PI/2, 1);
  ambientLight(102, 102, 102);
  background(180,250,250);
  noStroke();
  
  // Text sensitivity
  fill(0);
  text("Sensitivity:" + sensitivity, 10, height-20);

  // Coordinates at the window's center
  translate(width/2, height/2, 0);
  
  if(!isShift) {
    pushMatrix();
    
    // Rotations around the 3 axis
    rotateX(xAngle);
    rotateY(yAngle);
    rotateZ(zAngle);
    
    // Plate rendering
    fill(plateColor);
    box(plate.x, plate.y, plate.z);
    
    // Cylinders rendering
    for(PVector c : cylinders)
      (new Cylinder(c)).render();

    // Ball rendering
    ball.render();

    popMatrix();
  } else {
    
    // Plate 2D rendering
    fill(plateColor);
    PShape rect = createShape();
    rect.beginShape(QUADS);
    rect.vertex(minX, minY, 0);
    rect.vertex(minX, maxY, 0);
    rect.vertex(maxX, maxY, 0);
    rect.vertex(maxX, minY, 0);
    rect.endShape();
    shape(rect);
    
    // Cylinders 2D rendering
    for(PVector c : cylinders)
      (new Cylinder(c)).render2D();
      
    // Ball 2D rendering
    ball.render2D();
    
    // Cylinder for mouse drag
    float xpos = clamp(mouseX-width/2, minX, maxX);
    float ypos = clamp(mouseY-height/2, minY, maxY);   
    (new Cylinder(xpos, ypos)).render2D();
  }
}

void mouseDragged() {
  if(!isShift) {
    zAngle += sensitivity * (mouseX - pmouseX)/100.0;
    xAngle += sensitivity * (pmouseY - mouseY)/100.0;
    zAngle = clamp(zAngle, MIN_ANGLE, MAX_ANGLE);
    xAngle = clamp(xAngle, MIN_ANGLE, MAX_ANGLE);
  }
}

void mouseWheel(MouseEvent event) {
  sensitivity += event.getCount()/10.0;
  sensitivity = clamp(sensitivity, MIN_SENSITIVITY, MAX_SENSITIVITY);
}

void mouseClicked() {
  if(isShift) {
    float xpos = clamp(mouseX - width/2, minX, maxX);
    float ypos = clamp(mouseY - height/2, minY, maxY);
    PVector v = new PVector(xpos, ypos);
    cylinders.add(v);
  }
}

void keyReleased() {
  if (key == CODED) {
    if (keyCode == SHIFT) isShift = false;
  }
}

void keyPressed() {
  if (key == CODED) {
    if (keyCode == SHIFT) isShift = true;
    /*
    if (keyCode == LEFT)
      yAngle -= 0.1;
    else if (keyCode == RIGHT)
      yAngle += 0.1;
    yAngle = clamp(yAngle, MIN_ANGLE, MAX_ANGLE);
    }
    */
  }
}

