// GAME
// Max angle value = +/-60 deg
final float MIN_ANGLE = -PI/3;
final float MAX_ANGLE = PI/3;
// Sensitivity limits
final float MIN_SENSITIVITY = 0.1;
final float MAX_SENSITIVITY = 1.0;
float sensitivity = 0.5;
// Depth of camera
float depth = 500f;
// Angles variables
float xAngle = 0.0;
float yAngle = 0.0;
float zAngle = 0.0;
//  Dimensions
PVector box = new PVector(300,20,300);
float sphereSize = 20.0;
// Adding physics
Mover mover = new Mover();
// Cylinder mode
boolean isCylMode = false;
ArrayList<PVector> cylinders = new ArrayList<PVector>();
PShape plate;

float clamp(float a, float min_a, float max_a) {
  if (a > max_a) return max_a;
  else if (a < min_a) return min_a;
  else return a;
}

void setup() { 
  size(500, 500, P3D);
//  if (frame != null) {
//    frame.setLocation(
//      displayWidth / 2 - width / 2, 
//      displayHeight / 2- height / 2);
//  }
}

void draw() {
  camera(width/2, height/2, depth, 250, 250, 0, 0, 1, 0);
  directionalLight(50, 100, 125, 0, -1, 0);
  ambientLight(102, 102, 102);
  background(180,250,250);
  
  // Text for sensitivity
  stroke(0);
  fill(0);
  textSize(15f);
  text("Sensitivity:" + sensitivity, 0f, 10f);

  fill(20,255,20);
  noStroke();

  pushMatrix();
  translate(width/2, height/2, 0);
  
  if(!isCylMode) {
    pushMatrix();
    // Rotations along the 3 axis
    rotateX(xAngle);
    rotateY(yAngle);
    rotateZ(zAngle);
    // Plate rendering
    box(box.x, box.y, box.z);
    
    for(PVector c : cylinders) {
      Cylinder cyl = new Cylinder(c.x,c.y);
      cyl.render();
    }
          
    // Ball rendering
    mover.render();
  
    
    
    popMatrix();
  } else {
    float xpos = clamp(mouseX - width/2, -box.x/2, box.x/2);
    float ypos = clamp(mouseY - height/2, -box.z/2, box.z/2);
    Cylinder c = new Cylinder(xpos, ypos);
    c.render2D();
    for(PVector cyl : cylinders)
      new Cylinder(cyl.x, cyl.y).render2D();
    fill(20,255,20);
    noStroke();
    plate = createShape();
    plate.beginShape();
    plate.vertex(-box.x/2,-box.z/2,0);
    plate.vertex(-box.x/2,box.z/2,0);
    plate.vertex(box.x/2,box.z/2,0);
    plate.vertex(box.x/2,-box.z/2,0);
    plate.endShape(CLOSE);
    shape(plate);

    mover.render2D();
  }
  popMatrix();
}

void mouseDragged() {
  if(isCylMode) {
    
  } else {
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
  if(isCylMode) {
    float xpos = clamp(mouseX - width/2, -box.x/2, box.x/2);
    float ypos = clamp(mouseY - height/2, -box.z/2, box.z/2);
    PVector v = new PVector(xpos, ypos);
    cylinders.add(v);
  }
}

void keyReleased() {
  if (key == CODED) {
    if (keyCode == SHIFT) {
      isCylMode = false;
    }
  }
}

void keyPressed() {
  if (key == CODED) {
    if (keyCode == SHIFT) {
      isCylMode = true;
    }
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

