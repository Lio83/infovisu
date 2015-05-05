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
		image(binaryThresholding(img), 0, 0);
	}

	private PImage thresholding(int threshold, boolean inverted) {
		PImage result = createImage(width, height, RGB); // create a new, initially transparent, 'result' image

		for(int i = 0; i < img.width * img.height; i++) {
			// do something with the pixel img.pixels[i]
			result.pixels[i] = color(brightness(img.pixels[i]) > threshold ? (inverted ? 0 : 255) : (inverted ? 255 : 0));
		}
		return result;
	}

	private PImage invertedBinaryThresholding(int threshold) {
		return thresholding(threshold, true);
	}
	private PImage binaryThresholding(int threshold) {
		return thresholding(threshold, false);
	}

}
