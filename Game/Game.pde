// Angles properties
final float MIN_ANGLE = -PI/3;
final float MAX_ANGLE = PI/3;
float xAngle = 0.0;
float yAngle = 0.0;
float zAngle = 0.0;

// Sensitivity properties
final float MIN_SENSITIVITY = 0.1;
final float MAX_SENSITIVITY = 1.0;
float sensitivity = 0.5;

// Plate properties
final PVector plate = new PVector(400, 20, 400);
final float minX = -plate.x/2;
final float maxX = plate.x/2;
final float minY = -plate.z/2;
final float maxY = plate.z/2;
final int plateColor = 0xFF10FF10;

// Ball (Mover)
Mover ball = new Mover();

// Cylinders and mode checking boolean
ArrayList<PVector> cylinders = new ArrayList<PVector>();
boolean isShift = false;

// Auxilliary function for min/max values.
float clamp(float a, float min_a, float max_a) {
  if (a > max_a) return max_a;
  else if (a < min_a) return min_a;
  else return a;
}

void setup() { 
  size(800, 600, P3D);
}

void draw() {
  directionalLight(50, 100, 125, 0, 1, -1);
  ambientLight(102, 102, 102);
  background(180,250,250);
  noStroke();
  smooth();
  
  // Text sensitivity
  fill(0);
  text("Sensitivity: " + sensitivity, 10, height-10);

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
    rect(minX, minY, plate.x, plate.z);
    
    // Cylinders 2D rendering
    for(PVector c : cylinders)
      (new Cylinder(c)).render2D();
      
    // Ball 2D rendering
    ball.render2D();
    
    // Cylinder for mouse drag   
    (new Cylinder(getPosition())).render2D();
  }
}

/*******************************
    MOUSE and KEYBOARD EVENTS
********************************/
private PVector getPosition() {
  float xpos = clamp(mouseX - width/2, minX, maxX);
  float ypos = clamp(mouseY - height/2, minY, maxY);
  return new PVector(xpos, ypos);
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
    cylinders.add(getPosition());
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
    /* old version with Y rotation
    if (keyCode == LEFT)
      yAngle -= 0.1;
    else if (keyCode == RIGHT)
      yAngle += 0.1;
    yAngle = clamp(yAngle, MIN_ANGLE, MAX_ANGLE);
    }
    */
  }
}

