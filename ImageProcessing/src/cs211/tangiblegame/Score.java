package cs211.tangiblegame;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

/**
 * Scoreboard for the Game project.
 *
 * @author Lionel, Guillaume, Yannick
 *
 */
public class Score implements GameParameters {

    private final static int SPACING = 5;
    private final static int TOP_VIEW_SIZE = SCORE_HEIGHT - (2 * SPACING);
    private final static int SCORE_WIDTH = 100;
    final static int CHART_WIDTH = WINDOW_WIDTH - SCORE_WIDTH - TOP_VIEW_SIZE - 4 * SPACING;
    private final static float scaling = ((float) TOP_VIEW_SIZE) / PLATE_Z;

    private final PGraphics background, topView, scoreboard, chart;

    private float score = 0f;
    private float lastScore = 0f;
    private int squareWidth = 0, squareHeight = 3;
    private int prevSec = 0;
    private final ArrayList<Integer> scores = new ArrayList<Integer>();
    private final PApplet p;

    Score(PApplet parent) {
        p = parent;
        background = p.createGraphics(WINDOW_WIDTH, SCORE_HEIGHT, PConstants.P2D);
        topView = p.createGraphics(TOP_VIEW_SIZE, TOP_VIEW_SIZE, PConstants.P2D);
        scoreboard = p.createGraphics(SCORE_WIDTH, TOP_VIEW_SIZE, PConstants.P2D);
        chart = p.createGraphics(CHART_WIDTH, SCORE_WIDTH - 20, PConstants.P2D);
    }

    // ======================================= Change score
    void changeScore(float ds, float sliderPosition) {
        if (ds > 1 || ds < -1) {
            score += ds;
            score = PApplet.max(0, score);
            lastScore = ds;
        }
        squareWidth = (int) ((sliderPosition + 0.1) * 30);
    }

    // ===================================== Draw Bar Chart
    private void drawBarChart() {

        if (PApplet.second() != prevSec) {
            prevSec = PApplet.second();
            scores.add((int) score);
        }

        chart.beginDraw();
        chart.background(SCORE_BACKGND_COLOR - 0x00202020);
        chart.pushMatrix();
        chart.translate(0, SCORE_WIDTH - 20);
        chart.noStroke();
        chart.fill(CYLINDER_COLOR);

        for (int i = 0; i < scores.size(); ++i) {
            int s = scores.get(i), j = 0;
            while (s > 0) {
                chart.rect(i * squareWidth, --j * squareHeight, squareWidth - 1, squareHeight - 1);
                s -= 10;
            }
        }
        chart.popMatrix();
        chart.endDraw();

    }

    // ===================================== Draw score board
    private void drawScoreBoard(float velocityMag) {
        scoreboard.beginDraw();
        scoreboard.background(SCORE_BACKGND_COLOR - 0x00202020);
        scoreboard.stroke(0);
        scoreboard.fill(0);
        String velocity = String.format("%.2f", velocityMag);
        String rscore = String.format("%.2f", score);
        String lscore = String.format("%.2f", lastScore);
        scoreboard.text("Total score:\n  " + rscore, 10, 15);
        scoreboard.text("Velocity:\n  " + velocity, 10, 50);
        scoreboard.text("Last score:\n  " + lscore, 10, 85);
        scoreboard.endDraw();
    }

    // ====================================== Draw background
    private void drawBackground() {
        background.beginDraw();
        background.background(SCORE_BACKGND_COLOR);
        background.endDraw();
    }

    // ======================================== Draw top view
    private void drawTopView(Mover mover, ArrayList<Cylinder> cylinders) {
        topView.beginDraw();
        topView.background(PLATE_COLOR);
        topView.noStroke();
        topView.fill(BALL_COLOR);
        float size = 2 * SPHERE_RADIUS * scaling;
        float x = mover.location.x * scaling + TOP_VIEW_SIZE / 2;
        float y = mover.location.y * scaling + TOP_VIEW_SIZE / 2;
        topView.ellipse(x, y, size, size);
        float cylSize = 2 * Cylinder.CYLINDER_RADIUS * scaling;
        topView.fill(CYLINDER_COLOR);
        for (Cylinder c : cylinders) {
            x = c.position.x * scaling + TOP_VIEW_SIZE / 2;
            y = c.position.y * scaling + TOP_VIEW_SIZE / 2;
            topView.ellipse(x, y, cylSize, cylSize);
        }
        topView.endDraw();
    }

    // ====================================== Render score
    void renderScore(Mover mover, ArrayList<Cylinder> cylinders) {
        p.pushMatrix();
        // -------------------------------------- Background
        p.translate(0, WINDOW_HEIGHT - SCORE_HEIGHT);
        drawBackground();
        p.image(background, 0, 0);
        // ---------------------------------------- Top view
        p.translate(SPACING, SPACING);
        drawTopView(mover, cylinders);
        p.image(topView, 0, 0);
        // ------------------------------------- Score board
        p.translate(TOP_VIEW_SIZE + SPACING, 0);
        drawScoreBoard(mover.velocity.mag());
        p.image(scoreboard, 0, 0);
        // --------------------------------------- Bar chart
        p.translate(SCORE_WIDTH + SPACING, 0);
        drawBarChart();
        p.image(chart, 0, 0);
        p.popMatrix();
    }

}
