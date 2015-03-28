// ======================================================
//   Game
// ======================================================
final PVector plate = new PVector(300, 20, 300);
final float minX = -plate.x / 2;
final float maxX = plate.x / 2;
final float minY = -plate.z / 2;
final float maxY = plate.z / 2;
final int plateColor = 0xFF10AA10;
boolean editMode = false;

// ==============================================  Angles  
final float MIN_ANGLE = -PI / 3;
final float MAX_ANGLE = PI / 3;
float xAngle = 0.0;
float yAngle = 0.0;
float zAngle = 0.0;

// =========================================  Sensitivity
final float MIN_SENSI = 0.1;
final float MAX_SENSI = 1.0;
float sensi = 0.5;

// ===============================================  Mover 
Mover ball = new Mover();

// ===========================================  Cylinders 
ArrayList<PVector> cylinders = new ArrayList<PVector>();

// ======================================================
//   Utility function for clamping values
// ======================================================
float clamp(float a, float min_a, float max_a) {
    if (a > max_a) return max_a;
    else if (a < min_a) return min_a;
    else return a;
}

// ======================================================
//   Utility function for the current mouse position
// ======================================================
private PVector getPosition() {
    float x = clamp(mouseX - width / 2, minX, maxX);
    float y = clamp(mouseY - height / 2, minY, maxY);
    return new PVector(x, y);
}

// ======================================================
//   Setup
// ======================================================
void setup() {
    size(800, 600, P3D);
    setupScore();
    scrollbar = new HScrollbar(225, height-25, chartWidth, 15);
}

// ======================================================
//   Draw
// ======================================================
void draw() {
    background(180, 250, 250);

    // -------  Text sensitivity in the upper left corner
    fill(0);
    int s = ((int) (sensi * 10)) * 10;
    text("Sensitivity = " + s + "%", 10, 20);
    
    // ---------------------------------  Add score board
    renderScore();
    scrollbar.update();
    scrollbar.display();
    
    // ------------------  Coordinates at window's center
    translate(width / 2, height / 2, 0);

    // =======================================  GAME MODE
    if (!editMode) {
        pushMatrix();
        lights();
        // -----------------------------  Plate rotations
        rotateX(xAngle);
        rotateY(yAngle);
        rotateZ(zAngle);
        // -----------------------------  Plate rendering
        fill(plateColor);
        noStroke();
        box(plate.x, plate.y, plate.z);
        // -------------------------  Cylinders rendering
        for (PVector c : cylinders)
            (new Cylinder(c)).render();
        // ------------------------------  Ball rendering
        ball.render();
        popMatrix();
    } 
    // =======================================  EDIT MODE
    else {
        // --------------------------  Plate 2D rendering
        fill(plateColor);
        rect(minX, minY, plate.x, plate.z);
        // ----------------------- Cylinders 2D rendering
        for (PVector c : cylinders)
            (new Cylinder(c)).render2D();
        // ---------------------------- Ball 2D rendering
        ball.render2D();
        // ------------------ Mouse cylinder 2D rendering
        (new Cylinder(getPosition())).render2D();
    }
}

// ======================================================
//   Mouse and Keayboard events
// ======================================================

// ==========================================  Mouse drag
void mouseDragged() {
    if (!editMode && !scrollbar.locked) {
        zAngle += sensi * (mouseX-pmouseX) * 0.01;
        xAngle += sensi * (pmouseY-mouseY) * 0.01;
        zAngle = clamp(zAngle, MIN_ANGLE, MAX_ANGLE);
        xAngle = clamp(xAngle, MIN_ANGLE, MAX_ANGLE);
    }
}
// =========================================  Mouse wheel
void mouseWheel(MouseEvent event) {
    sensi += event.getCount() * 0.1;
    sensi = clamp(sensi, MIN_SENSI, MAX_SENSI);
}

// =========================================  Mouse click
void mouseClicked() {
    if (editMode) cylinders.add(getPosition());
}

// =========================================  Key release
void keyReleased() {
    if (key == CODED) {
        if (keyCode == SHIFT) editMode = false;
    }
}

// ===========================================  Key press
void keyPressed() {
    if (key == CODED) {
        if (keyCode == SHIFT) editMode = true;
    }
}

