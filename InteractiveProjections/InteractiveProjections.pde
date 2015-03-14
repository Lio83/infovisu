// Série 3 Infovisu
void setup() {
  size(600, 600, P2D);
}

My3DBox input3DBox;
float scale = 1.0;
float xAngle = PI/12;
float yAngle = 0;
final float dAngle = 0.1;
final float scaling = 0.05;
final float MIN_SCALE = 0.5;
final float MAX_SCALE = 1.5;
int X, Y;

void draw() {
  background(255, 255, 255);
  
  My3DPoint eye = new My3DPoint(0, 0, -5000);
  
  My3DPoint origin = new My3DPoint(0, 0, 0);
  input3DBox = new My3DBox(origin, 100, 150, 300);
  
  //rotated around x
  float[][] transform1 = rotateXMatrix(xAngle); 
  input3DBox = transformBox(input3DBox, transform1); 
  //projectBox(eye, input3DBox).render();
  
  //rotated around y
  float[][] transform4 = rotateYMatrix(yAngle); 
  input3DBox = transformBox(input3DBox, transform4);
  
  //rotated and translated
  float[][] transform2 = translationMatrix(130, 65, 0); 
  input3DBox = transformBox(input3DBox, transform2); 
  //projectBox(eye, input3DBox).render();
  
  //rotated, translated, and scaled
  float[][] transform3 = scaleMatrix(scale, scale, scale); 
  input3DBox = transformBox(input3DBox, transform3); 
  projectBox(eye, input3DBox).render();
  Y = mouseY;
  X = mouseX;
}

void keyPressed() {
  if (key == CODED) {
    if (keyCode == UP)
      xAngle += dAngle;
    else if (keyCode == DOWN)
      xAngle -= dAngle;
    else if (keyCode == LEFT)
      yAngle += dAngle;
    else if (keyCode == RIGHT)
      yAngle -= dAngle;
  }
}

void mouseDragged() {
  int dx = X - mouseX;
  int dy = Y - mouseY;
  if(abs(dy)>abs(dx)) {
    if(dy>0)
      scale += scaling;
    else
      scale -= scaling;
  }
  scale = max(MIN_SCALE, min(MAX_SCALE, scale));
}

class My2DPoint { 
  float x;
  float y;
  My2DPoint(float x, float y) {
    this.x = x;
    this.y = y;
  }
}

class My3DPoint { 
  float x;
  float y;
  float z;
  My3DPoint(float x, float y, float z) {
    this.x = x; 
    this.y = y; 
    this.z = z;
  }
}

My2DPoint projectPoint(My3DPoint eye, My3DPoint p) {
  float c = eye.z / (eye.z - p.z);
  return new My2DPoint((p.x - eye.x) / c, (p.y - eye.y) / c);
}

class My2DBox { 
  My2DPoint[] s; 
  My2DBox(My2DPoint[] s) {
    this.s = s;
  }
  void render() {
    strokeWeight(4);
    stroke(0,255,0);
    line(s[0].x, s[0].y, s[1].x, s[1].y);
    line(s[1].x, s[1].y, s[2].x, s[2].y);
    line(s[2].x, s[2].y, s[3].x, s[3].y);
    line(s[0].x, s[0].y, s[3].x, s[3].y);
    strokeWeight(3);
    stroke(0,0,255);
    line(s[0].x, s[0].y, s[4].x, s[4].y);
    line(s[1].x, s[1].y, s[5].x, s[5].y);
    line(s[2].x, s[2].y, s[6].x, s[6].y);
    line(s[3].x, s[3].y, s[7].x, s[7].y);
    strokeWeight(2);
    stroke(255,0,0);
    line(s[4].x, s[4].y, s[5].x, s[5].y);
    line(s[4].x, s[4].y, s[7].x, s[7].y);
    line(s[5].x, s[5].y, s[6].x, s[6].y);
    line(s[6].x, s[6].y, s[7].x, s[7].y);
  }
}

class My3DBox {
  My3DPoint[] p;
  My3DBox(My3DPoint origin, float dimX, float dimY, float dimZ) {
    float x = origin.x;
    float y = origin.y;
    float z = origin.z;
    this.p = new My3DPoint[] {
      new My3DPoint(x, y+dimY, z+dimZ), 
      new My3DPoint(x, y, z+dimZ), 
      new My3DPoint(x+dimX, y, z+dimZ), 
      new My3DPoint(x+dimX, y+dimY, z+dimZ), 
      new My3DPoint(x, y+dimY, z), 
      origin, 
      new My3DPoint(x+dimX, y, z), 
      new My3DPoint(x+dimX, y+dimY, z)
    };
  }
  My3DBox(My3DPoint[] p) {
    this.p = p;
  }
}

My2DBox projectBox (My3DPoint eye, My3DBox box) {
  My2DPoint[] s = new My2DPoint[box.p.length];
  for (int i = 0; i < s.length; i++) {
    s[i] = projectPoint(eye, box.p[i]);
  }
  return new My2DBox(s);
}

float[] homogeneous3DPoint (My3DPoint p) { 
  float[] result = {p.x, p.y, p.z , 1};
  return result; 
}

float[][] rotateXMatrix(float angle) { 
  return(new float[][] {{1, 0 , 0 , 0},
                        {0, cos(angle), sin(angle), 0},
                        {0, -sin(angle), cos(angle), 0},
                        {0, 0 , 0 , 1}});
}

float[][] rotateYMatrix(float angle) {
   return(new float[][] {{cos(angle), 0, sin(angle), 0},
                        {0, 1, 0, 0},
                        {-sin(angle), 0, cos(angle), 0},
                        {0, 0 , 0 , 1}});
}

float[][] rotateZMatrix(float angle) {
   return(new float[][] {{cos(angle), -sin(angle), 0, 0},
                        {sin(angle), cos(angle), 0, 0},
                        {0, 0, 1, 0},
                        {0, 0 , 0 , 1}});
}

float[][] scaleMatrix(float x, float y, float z) {
   return(new float[][] {{x, 0, 0, 0},
                         {0, y, 0, 0},
                         {0, 0, z, 0},
                         {0, 0, 0, 1}});
}

float[][] translationMatrix(float x, float y, float z) {
  return(new float[][] {{1, 0, 0, x},
                        {0, 1, 0, y},
                        {0, 0, 1, z},
                        {0, 0, 0, 1}});
}

float[] matrixProduct(float[][] a, float[] b) {
  float[] r = new float[a.length];
  for(int i = 0; i < b.length; i++) {
    for(int j = 0; j < a[i].length; j++) {
      r[i] += a[i][j] * b[j];
    }
  }
  return r;
}

My3DPoint euclidian3DPoint (float[] a) {
  return new My3DPoint(a[0]/a[3], a[1]/a[3], a[2]/a[3]);
}

My3DBox transformBox(My3DBox box, float[][] transformMatrix) {
  My3DPoint[] r = new My3DPoint[box.p.length];
  for(int i = 0; i < r.length; i++) {
    r[i] = euclidian3DPoint(
      matrixProduct(transformMatrix, 
        homogeneous3DPoint(box.p[i])));
  }
  return new My3DBox(r);
}

