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
		image(sobel(img), 0, 0);
	}

	public PImage convolute(PImage img) {
		float[][] kernel = {
				{ 0, 0, 0 },
				{ 0, 2, 0 },
				{ 0, 0, 0 }};

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

	public PImage sobel(PImage img) {
		float[][] hKernel = { { 0, 1, 0 },
				{ 0, 0, 0 },
				{ 0, -1, 0 } };
		float[][] vKernel = { { 0, 0, 0 },
				{ 1, 0, -1 },
				{ 0, 0, 0 } };
		PImage result = createImage(img.width, img.height, ALPHA);
		// clear the image
		for (int i = 0; i < img.width * img.height; i++) {
			result.pixels[i] = color(0);
		}
		float max=0;
		float[] buffer = new float[img.width * img.height];
		// *************************************
		// Implement here the double convolution

		int sum_h = 0;
		int sum_v = 0;
		float sum = 0;
		for (int x=0; x<img.width; x++) {
			for (int y = 0; y < result.height; y++) {
				sum_h = (int)(partialConvol(img, x, y, hKernel));
				sum_v = (int)(partialConvol(img, x, y, vKernel));
				sum=sqrt(pow(sum_h, 2) + pow(sum_v, 2));
				buffer[y*img.width + x] = sum;
				if (sum > max) max = sum;
			}
		}
		// *************************************

		for (int y = 2; y < img.height - 2; y++) { // Skip top and bottom edges
			for (int x = 2; x < img.width - 2; x++) { // Skip left and right
				if (buffer[y * img.width + x] > (int)(max * 0.3f)) { // 30% of the max
					result.pixels[y * img.width + x] = color(255);
				} else {
					result.pixels[y * img.width + x] = color(0);
				}
			}
		}
		return result;
	}
}
