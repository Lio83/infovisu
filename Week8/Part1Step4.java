import processing.core.PApplet;
import processing.core.PImage;

public class ImageProcessing extends PApplet {
	PImage img;

	HScrollbar hueBarMin;
	HScrollbar hueBarMax;

	public void setup() {
		size(800, 600);
		img = loadImage("board1.jpg");

		hueBarMin = new HScrollbar(this, 0, 580, 800, 20);
		hueBarMax = new HScrollbar(this, 0, 550, 800, 20);
	}

	public void draw() {
		background(color(0,0,0));

		float minVal = 255 * hueBarMin.getPos();
		float maxVal = 255 * hueBarMax.getPos();

    image(hueMap(minVal, maxVal), 0, 0);

		hueBarMin.display();
		hueBarMax.display();

		hueBarMin.update();
		hueBarMax.update();
	}

	private PImage hueMap(float minVal, float maxVal) {
		PImage result = createImage(width, height, RGB); // create a new, initially transparent, 'result' image
		for(int i = 0; i< img.width*img.height; i++) {
			float hue = hue(img.pixels[i]);
			if (hue > minVal && hue < maxVal) {
				result.pixels[i] = color(hue);
			} else {
				result.pixels[i] = 0;
			}
		}
		return result;
	}
}
