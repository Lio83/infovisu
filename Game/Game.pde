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
// Pause mode
boolean paused = false;
PVector anglesBackup;
PVector currentCylinderPos = new PVector(0,0,0);
PShape openCylinder = new PShape();
PShape coverTop = new PShape();
PShape coverBottom = new PShape();

void setup() { 
  size(500, 500, P3D);
  if (frame != null) {
    frame.setLocation(
      displayWidth / 2 - width / 2, 
      displayHeight / 2- height / 2);
  }
  
  initCylinder();
}

void draw() {
  if (!paused)
    camera(width/2, height/2, depth, 250, 250, 0, 0, 1, 0);
  directionalLight(50, 100, 125, 0, -1, 0);
  ambientLight(102, 102, 102);
  background(180,250,250);

  pushMatrix();
  translate(width / 2, height / 2, 0);
  // Rotations along the 3 axis
  rotateX(xAngle);
  rotateY(yAngle);
  rotateZ(zAngle);
  // Plate rendering
  fill(20,255,20);
  noStroke();
  box(box.x, box.y, box.z);
  if (!paused) {
    // Ball rendering
    mover.checkEdges();
    mover.update();
  }
  translate(mover.location.x, mover.location.z, -mover.location.y);
  fill(255,20,20);
  sphere(sphereSize);
   
/*  if (paused) {
    pushMatrix();
    // Drawing cylinders
    translate(currentCylinderPos.x, currentCylinderPos.y);
    shape(openCylinder);
    shape(coverTop);
    shape(coverBottom);
    popMatrix();
  } */
 
  popMatrix();
  // Text for sensitivity
  stroke(0);
  fill(0);
  textSize(15f);
  text("Sensitivity:" + sensitivity, 0f, 10f);
}

void initCylinder() {
  float cylinderBaseSize = 50;
  float cylinderHeight = 50;
  int cylinderResolution = 20;
  float angle;
  float[] x = new float[cylinderResolution + 1];
  float[] y = new float[cylinderResolution + 1];
  
  //get the x and y position on a circle for all the sides
  for(int i = 0; i < x.length; i++) {
    angle = (TWO_PI / cylinderResolution) * i;
    x[i] = sin(angle) * cylinderBaseSize;
    y[i] = cos(angle) * cylinderBaseSize;
  }
  
  openCylinder = createShape();
  coverTop = createShape();
  coverBottom = createShape();
  
  openCylinder.beginShape(QUAD_STRIP);
  //draw the border of the cylinder
  for(int i = 0; i < x.length; i++) {
    openCylinder.vertex(x[i], y[i] , 0);
    openCylinder.vertex(x[i], y[i], cylinderHeight);
  }
  openCylinder.endShape();
  
  coverTop.beginShape(TRIANGLE_FAN);
  
  coverTop.vertex(0,0,0);
  for(int i = 0; i < x.length; i++) {    
    coverTop.vertex(x[i], y[i] , 0);
  }
  coverTop.endShape();

  coverBottom.beginShape(TRIANGLE_FAN);
  coverBottom.vertex(0,0,cylinderHeight);
  for(int i = 0; i < x.length; i++) {
     coverBottom.vertex(x[i], y[i], cylinderHeight);
  }
  coverBottom.endShape();
}

void mouseMoved() {
  if (paused) {
    currentCylinderPos.x = mouseX;
    currentCylinderPos.y = mouseY;
  }
}

void mouseDragged() {
  if (!paused) {
    zAngle += sensitivity * (mouseX - pmouseX)/100.0;
    xAngle += sensitivity * (pmouseY - mouseY)/100.0;
    zAngle = clamp(zAngle, MIN_ANGLE, MAX_ANGLE);
    xAngle = clamp(xAngle, MIN_ANGLE, MAX_ANGLE);
  }
}

float clamp(float a, float min_a, float max_a) {
  if (a > max_a) return max_a;
  else if (a < min_a) return min_a;
  else return a;
}

void mouseWheel(MouseEvent event) {
  sensitivity += event.getCount()/10.0;
  sensitivity = clamp(sensitivity, MIN_SENSITIVITY, MAX_SENSITIVITY);
}

void pauseGame() {
  anglesBackup = new PVector(xAngle, yAngle, zAngle);
  paused = true;
  xAngle = 0;
  yAngle = 0;
  zAngle = 0;
  camera(width/2.0, height/2.0, (height/2.0) / tan(PI*30.0 / 180.0), width/2.0, height/2.0, 0, 0, 1, -1);

}

void resumeGame() {
  xAngle = anglesBackup.x;
  yAngle = anglesBackup.x;
  zAngle = anglesBackup.x;
  paused = false;
}

void keyPressed() {
  if (key == CODED) {
    if (keyCode == SHIFT)
      pauseGame();
  }
}

void keyReleased() {
  if (key == CODED) {
    if (keyCode == SHIFT)
      resumeGame();
  }
}

/* // Optional
void keyPressed() {
  if (key == CODED) {
    if (keyCode == LEFT)
      yAngle -= 0.1;
    else if (keyCode == RIGHT)
      yAngle += 0.1;
    yAngle = clamp(yAngle, MIN_ANGLE, MAX_ANGLE);
  }
}
*/
