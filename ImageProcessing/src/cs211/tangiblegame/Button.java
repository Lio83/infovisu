package cs211.tangiblegame;

import processing.core.PApplet;
import processing.core.PConstants;

public class Button {
    int size;
    float posX;
    float posY;
    String text;
    float width;
    float height = 50;

    boolean clicked;
    boolean hover;
    PApplet p;

    Button(PApplet parent, String text, float posX, float posY, float width) {
        p = parent;

        clicked = false;
        hover = false;
        this.posX = posX;
        this.posY = posY;
        this.text = text;
        this.width = width;
    }

    void display() {
        p.pushMatrix();
        if (hover) {
            p.fill(255, 0, 0);
        } else {
            p.fill(0xFFB4FAFA);
        }
        p.rectMode(PConstants.CENTER);

        p.rect(posX, posY, width, height, 7);
        p.fill(0x000000);

        p.textSize(20);
        p.textAlign(PConstants.CENTER, PConstants.CENTER);

        p.text(text, posX, posY, width, height);
        p.point(posX, posY);
        p.rectMode(PConstants.CORNER);
        p.popMatrix();
    }

    void update() {
        mouseCheck();
    }

    void mouseCheck() {
        if (p.mouseX > posX - width / 2 && p.mouseX < posX + width / 2 && p.mouseY > posY - height / 2
                && p.mouseY < posY + height / 2) {
            hover = true;
        } else {
            hover = false;
        }
    }
}