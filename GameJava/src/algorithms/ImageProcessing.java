package algorithms;

import processing.core.PApplet;
import processing.core.PImage;

@SuppressWarnings("serial")
public class ImageProcessing extends PApplet {
    PImage img;

    // HScrollbar thresholdBar;
    // HScrollbar hueBarMin;
    // HScrollbar hueBarMax;

    public void setup() {
        size(800, 600);
        img = loadImage("board1.jpg");
        // thresholdBar = new HScrollbar(this, 0, 580, 800, 20);
        // hueBarMin = new HScrollbar(this, 0, 580, 800, 20);
        // hueBarMax = new HScrollbar(this, 0, 550, 800, 20);

        noLoop();
    }

    public void draw() {
        image(img, 0, 0);
        background(color(0, 0, 0));

        // image(binaryThresholding((int)(thresholdBar.getPos()*255)), 0, 0);
        // thresholdBar.display();
        // thresholdBar.update();
        /*
         * float minVal = 255 * hueBarMin.getPos(); float maxVal = 255 *
         * hueBarMax.getPos();
         * 
         * image(hueMap(minVal, maxVal), 0, 0);
         * 
         * hueBarMin.display(); hueBarMax.display();
         * 
         * hueBarMin.update(); hueBarMax.update();
         */
        image(convolute(img), 0, 0);
    }

    private PImage thresholding(int threshold, boolean inverted) {
        PImage result = createImage(width, height, RGB); // create a new,
                                                         // initially
                                                         // transparent,
                                                         // 'result' image

        for (int i = 0; i < img.width * img.height; i++) {
            // do something with the pixel img.pixels[i]
            result.pixels[i] = color(brightness(img.pixels[i]) > threshold ? (inverted ? 0 : 255)
                    : (inverted ? 255 : 0));
        }
        return result;
    }

    private PImage invertedBinaryThresholding(int threshold) {
        return thresholding(threshold, true);
    }

    private PImage binaryThresholding(int threshold) {
        return thresholding(threshold, false);
    }

    private PImage hueMap(float minVal, float maxVal) {
        PImage result = createImage(width, height, RGB); // create a new,
                                                         // initially
                                                         // transparent,
                                                         // 'result' image
        for (int i = 0; i < img.width * img.height; i++) {
            float hue = hue(img.pixels[i]);
            if (hue > minVal && hue < maxVal) {
                result.pixels[i] = color(hue);
            } else {
                result.pixels[i] = 0;
            }
        }
        return result;
    }

    public PImage convolute(PImage img) {
        float[][] kernel = { { 0, 0, 0 }, { 0, 2, 0 }, { 0, 0, 0 } };
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

        for (int x = 0; x < img.width; x++) {
            for (int y = 0; y < img.height; y++) {
                // System.out.println("("+x+","+y+")");
                result.pixels[y * img.width + x] = ((int) (partialConvol(img, x, y, kernel) / weight));
            }
        }
        return result;
    }

    private float partialConvol(PImage img, int x, int y, float[][] matrix) {
        float acc = 0;
        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {
                if (i > 0 && i < img.width && j > 0 && j < img.height) {
                    acc = acc + img.pixels[j * img.width + i] * matrix[i - x + 1][j - y + 1];
                    int s = i - x + 1;
                    int t = j - y + 1;
                    System.out.println("x : " + s + "y : " + t);
                }
            }
        }
        return acc;
    }
}
