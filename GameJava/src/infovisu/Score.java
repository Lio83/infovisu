package infovisu;

import java.util.ArrayList;

import processing.core.*;

public class Score {
    // ======================================================
    // Score variables
    // ======================================================
    final int scoreBackgndColor = 0xFFEEEEAA;
    final int scoreHeight = 120;
    final int topViewHeight = scoreHeight - 10;
    final int shift = (scoreHeight - topViewHeight) / 2;
    final int scoreSize = 100;
    static int chartWidth = 0;
    final float scaling = topViewHeight / Main.plate.z;
    PGraphics background, topView, scoreboard, barChart;
    float score = 0.0f, lastScore = 0.0f;
    int squareWidth = 0, squareHeight = 3;
    int prevSec = 0;
    ArrayList<Integer> scores = new ArrayList<Integer>();
    Main p;

    Score(PApplet parent) {
        p = (Main) parent;
    }

    // ======================================= Change score
    void changeScore(float ds) {
        if (ds > 1 || ds < -1) {
            score += ds;
            score = Math.max(0, score);
            lastScore = ds;
        }
    }

    // ============================================== Setup
    void setupScore() {
        background = p.createGraphics(p.width, scoreHeight, PConstants.P2D);
        topView = p.createGraphics(topViewHeight, topViewHeight, PConstants.P2D);
        scoreboard = p.createGraphics(scoreSize, topViewHeight, PConstants.P2D);
        chartWidth = p.width - scoreSize - topViewHeight - 4 * shift;
        barChart = p.createGraphics(chartWidth, scoreSize - 20, PConstants.P2D);
    }

    // ===================================== Draw Bar Chart
    void drawBarChart() {
        squareWidth = (int) ((p.scrollbar.getPos() + 0.1) * 30);

        if (PApplet.second() != prevSec) {
            prevSec = PApplet.second();
            scores.add((int) score);
        }

        barChart.beginDraw();
        barChart.background(scoreBackgndColor - 0x00202020);
        barChart.pushMatrix();
        barChart.translate(0, scoreSize - 20);
        barChart.noStroke();
        barChart.fill(Cylinder.cylinderColor);

        for (int i = 0; i < scores.size(); ++i) {
            int s = scores.get(i), j = 0;
            while (s > 0) {
                barChart.rect(i * squareWidth, --j * squareHeight, squareWidth - 1, squareHeight - 1);
                s -= 10;
            }
        }
        barChart.popMatrix();
        barChart.endDraw();

    }

    // ===================================== Draw score board
    void drawScoreBoard() {
        scoreboard.beginDraw();
        scoreboard.background(scoreBackgndColor - 0x00202020);
        scoreboard.stroke(0);
        scoreboard.fill(0);
        String velocity = String.format("%.2f", p.ball.velocity.mag());
        String rscore = String.format("%.2f", score);
        String lscore = String.format("%.2f", lastScore);
        scoreboard.text("Total score:\n  " + rscore, 10, 15);
        scoreboard.text("Velocity:\n  " + velocity, 10, 50);
        scoreboard.text("Last score:\n  " + lscore, 10, 85);
        scoreboard.endDraw();
    }

    // ====================================== Draw background
    void drawBackground() {
        background.beginDraw();
        background.background(scoreBackgndColor);
        background.endDraw();
    }

    // ======================================== Draw top view
    void drawTopView() {
        topView.beginDraw();
        topView.background(Main.plateColor);
        topView.noStroke();
        topView.smooth();
        topView.fill(Mover.ballColor);
        float size = 2 * Mover.sphereSize * scaling;
        topView.ellipse(p.ball.location.x * scaling + topViewHeight / 2, p.ball.location.y * scaling + topViewHeight
                / 2, size, size);
        float cylSize = 2 * Cylinder.cylBaseSize * scaling;
        topView.fill(Cylinder.cylinderColor);
        for (PVector c : p.cylinders) {
            topView.ellipse(c.x * scaling + topViewHeight / 2, c.y * scaling + topViewHeight / 2, cylSize, cylSize);
        }
        topView.endDraw();
    }

    // ====================================== Render score
    void renderScore() {
        p.pushMatrix();
        // -------------------------------------- Background
        p.translate(0, p.height - scoreHeight);
        drawBackground();
        p.image(background, 0, 0);
        // ---------------------------------------- Top view
        p.translate(shift, shift);
        drawTopView();
        p.image(topView, 0, 0);
        // ------------------------------------- Score board
        p.translate(topViewHeight + shift, 0);
        drawScoreBoard();
        p.image(scoreboard, 0, 0);
        // --------------------------------------- Bar chart
        p.translate(scoreSize + shift, 0);
        drawBarChart();
        p.image(barChart, 0, 0);
        p.popMatrix();
    }

}
