import processing.core.PApplet;
import processing.core.PImage;

public class ImageProcessing extends PApplet {
	PImage img;

	public void setup() {
		size(800, 600);
		img = loadImage("board1.jpg");

		noLoop();
	}

  public void draw() {
		background(color(0,0,0));

		image(convolute(img), 0, 0);
	}

	public PImage convolute(PImage img) {
		float[][] kernel = {
				{ 9, 12, 9 },
				{ 12, 15, 12 },
				{ 9, 12, 9}};
		float weight = 1.f;
		// create a greyscale image (type: ALPHA) for output
		PImage result = createImage(img.width, img.height, ALPHA);
		// kernel size N = 3
		//
		// for each (x,y) pixel in the image:
		// - multiply intensities for pixels in the range
		// (x - N/2, y - N/2) to (x + N/2, y + N/2) by the
		// corresponding weights in the kernel matrix
		// - sum all these intensities and divide it by the weight
		// - set result.pixels[y * img.width + x] to this value

		for (int x=0; x<result.width; x++) {
			for (int y = 0; y < result.height; y++) {
				result.pixels[y*result.width+x] = ((int)(partialConvol(img, x, y, kernel)/weight));
			}
		}
		return result;
	}

	private float partialConvol(PImage img, int x, int y, float[][] matrix) {
		float acc = 0;
		for (int i=x-1; i<x+2; i++) {
			for (int j = y-1; j <y+2 ; j++) {
				if (i >= 0 && i < img.width && j >= 0 && j < img.height) {
					acc = acc + brightness(img.pixels[j*img.width+i])*matrix[i-x+1][j-y+1];
				}
			}
		}
		return acc;
	}
}
