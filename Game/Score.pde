// ======================================================
//   Score variables
// ======================================================
final int scoreBackgndColor = 0xFFEEEEAA;
final int scoreHeight = 120;
final int topViewHeight = scoreHeight - 10;
final int shift = (scoreHeight - topViewHeight) / 2;
final int scoreSize = 100;
int chartWidth = 0;
final float scaling = topViewHeight / plate.z;
PGraphics background, topView, scoreboard, barChart;
HScrollbar scrollbar;
float score = 0.0, lastScore = 0.0;
int squareWidth = 0, squareHeight = 3;
int prevSec = 0;
ArrayList<Integer> scores = new ArrayList<Integer>();

// =======================================  Change score
void changeScore(float ds) {
  if (ds > 1 || ds < -1) {
    score += ds;
    score = max(0, score);
    lastScore = ds;
  }
}

// ==============================================  Setup
void setupScore() {
  background = createGraphics(width, scoreHeight, P2D);
  topView = createGraphics(topViewHeight, topViewHeight, P2D);
  scoreboard = createGraphics(scoreSize, topViewHeight, P2D);
  chartWidth = width - scoreSize - topViewHeight - 4*shift;
  barChart = createGraphics(chartWidth, scoreSize - 20, P2D);
}

// =====================================  Draw Bar Chart
void drawBarChart() {
  squareWidth = (int)((scrollbar.getPos() + 0.1) * 30);
  
  if (second() != prevSec) {
    prevSec = second();
    scores.add((int)score);
  }
  
  barChart.beginDraw();
  barChart.background(scoreBackgndColor - 0x00202020);
  barChart.pushMatrix();
  barChart.translate(0, scoreSize - 20);
  barChart.noStroke();
  barChart.fill(cylinderColor);

  for (int i = 0; i < scores.size(); ++i) {
    int s = scores.get(i), j = 0;
    while (s > 0) {
      barChart.rect(i*squareWidth, --j*squareHeight, squareWidth-1, squareHeight-1);
      s -= 10;
    }
  }
  barChart.popMatrix();
  barChart.endDraw();

}

// =====================================  Draw score board
void drawScoreBoard() {
  scoreboard.beginDraw();
  scoreboard.background(scoreBackgndColor - 0x00202020);
  scoreboard.stroke(0);
  scoreboard.fill(0);
  String velocity = String.format("%.2f", ball.velocity.mag());
  String rscore = String.format("%.2f", score);
  String lscore = String.format("%.2f", lastScore);
  scoreboard.text("Total score:\n  " + rscore, 10, 15);
  scoreboard.text("Velocity:\n  " + velocity, 10, 50);
  scoreboard.text("Last score:\n  " + lscore, 10, 85);
  scoreboard.endDraw();
}

// ======================================  Draw background
void drawBackground() {
  background.beginDraw();
  background.background(scoreBackgndColor);
  background.endDraw();
}

// ========================================  Draw top view
void drawTopView() {
  topView.beginDraw();
  topView.background(plateColor);
  topView.noStroke();
  topView.smooth();
  topView.fill(ballColor);
  float size = 2 * sphereSize * scaling;
  topView.ellipse(ball.location.x*scaling+topViewHeight/2,
  ball.location.y*scaling+topViewHeight/2, size, size);
  float cylSize = 2 * cylBaseSize * scaling;
  topView.fill(cylinderColor);
  for (PVector c : cylinders) {
    topView.ellipse(c.x*scaling+topViewHeight/2,
    c.y*scaling+topViewHeight/2,cylSize,cylSize);
  }
  topView.endDraw();
}

// ======================================  Render score
void renderScore() {
  pushMatrix();
  // --------------------------------------  Background
  translate(0, height-scoreHeight);
  drawBackground();
  image(background, 0, 0);
  // ----------------------------------------  Top view
  translate(shift, shift);
  drawTopView();
  image(topView, 0, 0);
  // -------------------------------------  Score board
  translate(topViewHeight + shift, 0);
  drawScoreBoard();
  image(scoreboard, 0, 0);
  // ---------------------------------------  Bar chart
  translate(scoreSize + shift, 0);
  drawBarChart();
  image(barChart, 0, 0);
  popMatrix(); 
}
