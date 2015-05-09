package cs211;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Part3 extends PApplet {
	PImage img;
	/*
	HScrollbar hueBarMin;
	HScrollbar hueBarMax;
	HScrollbar satBarMin;
	HScrollbar satBarMax;
	HScrollbar brighBarMin;
	HScrollbar brighBarMax;
	HScrollbar blurWeight;
	*/

	public void setup() {
		size(800, 600);
		img = loadImage("board1.jpg");
		
		/*
		hueBarMin = new HScrollbar(this, 0, 760, 800, 20);
		hueBarMax = new HScrollbar(this, 0, 730, 800, 20);
		satBarMin = new HScrollbar(this, 0, 700, 800, 20);
		satBarMax = new HScrollbar(this, 0, 670, 800, 20);
		brighBarMin = new HScrollbar(this, 0, 640, 800, 20);
		brighBarMax = new HScrollbar(this, 0, 610, 800, 20);
		blurWeight = new HScrollbar(this, 0, 790, 800, 20);
		*/
		
		// noLoop();
	}

	public void draw() {
		background(color(0,0,0));
		PImage result;
		
		/*
		float hueMin = 255 * hueBarMin.getPos();
		float hueMax = 255 * hueBarMax.getPos();
		float satMin = 255 * satBarMin.getPos();
		float satMax = 255 * satBarMax.getPos();
		float brighMin = 255 * brighBarMin.getPos();
		float brighMax = 255 * brighBarMax.getPos();
		*/

		result = img;
		
		// result = hueMap(result, hueMin, hueMax, satMin, satMax, brighMin, brighMax);

		result = hueMap(result, 72, 134, 97, 255, 61, 143);		
		result = blur(result);
		result = binaryThresholding(result, 40);
		result = sobel(result);


		
		image(result, 0, 0);
		
		/*
		hueBarMin.display();
		hueBarMax.display();
		satBarMin.display();
		satBarMax.display();
		brighBarMin.display();
		brighBarMax.display();
		
		blurWeight.display();
		blurWeight.update();

		hueBarMin.update();
		hueBarMax.update();
		satBarMin.update();
		satBarMax.update();
		brighBarMin.update();
		brighBarMax.update();

		textSize(20);
		text("Brigh min : "+(int)brighMin+", Brigh max : "+(int)brighMax
			+"\nSat min : "+(int)satMin+", Sat max : "+(int)satMax
			+"\nHue min : "+(int)hueMin+", Hue max : "+(int)hueMax
			+"\nBlur : "+blurWeight.getPos()*100, 10, 30);
		*/
	}

	public PImage blur(PImage img) {
		float[][] kernel = {
				{ 9, 12, 9 },
				{ 12, 15, 12 },
				{ 9, 12, 9 }};

		// float weight = blurWeight.getPos()*100;
		float weight = 5f;

		// create a greyscale image (type: ALPHA) for output
		PImage result = createImage(img.width, img.height, RGB);

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
				result.pixels[y*result.width+x] = ((int)(partialBlur(img, x, y, kernel, weight)));
			}
		}
		return result;
	}

	private float partialBlur(PImage img, int x, int y, float[][] matrix, float weight) {
		colorMode(RGB, 255);
		PVector acc = new PVector(0,0,0);
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
		        // Clamp the image coordinates
				int clampedX = (x+i-1 < 0) ? 0 :
					((x+i-1 > img.width-1)? img.width-1 : x + i -1);
				int clampedY = (y+j-1 < 0) ? 0 :
					((y+j-1 > img.height-1)? img.height-1 : y+j-1);
				// Do the actual calculation
				acc.x += red(img.pixels[clampedY*img.width+clampedX])*matrix[i][j];
				acc.y += green(img.pixels[clampedY*img.width+clampedX])*matrix[i][j];
				acc.z += blue(img.pixels[clampedY*img.width+clampedX])*matrix[i][j];
			}
		}
		return color(acc.x/weight, acc.y/weight, acc.z/weight);
	}
	
	private float partialSobel(PImage img, int x, int y, float[][] matrix) {
		float acc = 0f;
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 2; j++) {
		        // Clamp the image coordinates
				int clampedX = (x+i-1 < 0) ? 0 :
					((x+i-1 > img.width-1)? img.width-1 : x + i -1);
				int clampedY = (y+j-1 < 0) ? 0 :
					((y+j-1 > img.height-1)? img.height-1 : y+j-1);
				// Do the actual calculation
				acc += img.pixels[clampedY*img.width+clampedX]*matrix[i][j];
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
				sum_h = (int)(partialSobel(img, x, y, hKernel));
				sum_v = (int)(partialSobel(img, x, y, vKernel));
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
	
	private PImage hueMap(PImage img, float hueMin, float hueMax, float satMin, float satMax, float brighMin, float brighMax) {
		PImage result = createImage(width, height, RGB); 
		// create a new, initially transparent, 'result' image
		for(int i = 0; i< img.width*img.height; i++) {
			float hue = hue(img.pixels[i]);
			float sat = saturation(img.pixels[i]);
			float brigh = brightness(img.pixels[i]);
			result.pixels[i] = (sat > satMin && brigh > brighMin && hue > hueMin && 
								sat < satMax && brigh < brighMax && hue < hueMax) ? img.pixels[i] : color(0);
		}
		return result;
	}
	
	private PImage thresholding(PImage img, int threshold, boolean inverted) {
		PImage result = createImage(width, height, RGB); // create a new, initially transparent, 'result' image

		for(int i = 0; i < img.width * img.height; i++) {
			// do something with the pixel img.pixels[i]
			result.pixels[i] = color(brightness(img.pixels[i]) > threshold ? (inverted ? 0 : 255) : (inverted ? 255 : 0));
		}
		return result;
	}

	private PImage invertedBinaryThresholding(PImage img, int threshold) {
		return thresholding(img, threshold, true);
	}
	private PImage binaryThresholding(PImage img, int threshold) {
		return thresholding(img, threshold, false);
	}
}