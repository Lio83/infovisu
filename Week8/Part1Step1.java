import processing.core.PApplet;
import processing.core.PImage;

public class ImageProcessing extends PApplet {
	PImage img;

	public void setup() {
		size(800, 600);
		img = loadImage("board1.jpg");
}

  public void draw() {
    background(color(0,0,0));
		image(img, 0, 0);
	}
}
