package algorithms;

import java.util.ArrayList;
import java.util.Comparator;

import processing.core.*;

public final class Hough {
    private final static float discretizationStepsPhi = 0.06f;
    private final static float discretizationStepsR = 2.5f;
    private final static int minVotes = 200;

    private final PApplet p;

    public Hough(PApplet parent) {
        this.p = parent;
    }

    public void hough(PImage edgeImg, int nLines) {
        // dimensions of the accumulator
        int phiDim = (int) (Math.PI / discretizationStepsPhi);
        int rDim = (int) (((edgeImg.width + edgeImg.height) * 2 + 1) / discretizationStepsR);
        // our accumulator (with a 1 pix margin around)
        int[] accumulator = new int[(phiDim + 2) * (rDim + 2)];
        // Fill the accumulator: on edge points (ie, white pixels of the edge //
        // image), store all possible (r, phi) pairs describing lines going //
        // through the point.
        for (int y = 0; y < edgeImg.height; y++) {
            for (int x = 0; x < edgeImg.width; x++) {
                // Are we on an edge?
                if (p.brightness(edgeImg.pixels[y * edgeImg.width + x]) != 0) {
                    // ...determine here all the lines (r, phi) passing through
                    // pixel (x,y), convert (r,phi) to coordinates in the
                    // accumulator, and increment accordingly the accumulator.
                }
            }
        }

        PImage houghImg = p.createImage(rDim + 2, phiDim + 2, PConstants.ALPHA);
        for (int i = 0; i < accumulator.length; i++) {
            houghImg.pixels[i] = p.color(PApplet.min(255, accumulator[i]));
        }
        houghImg.updatePixels();

        ArrayList<Integer> bestCandidates = new ArrayList<Integer>();

        for (int idx = 0; idx < accumulator.length; idx++) {
            if (accumulator[idx] > minVotes) {
                // first, compute back the (r, phi) polar coordinates:
                int accPhi = (int) (idx / (rDim + 2)) - 1;
                int accR = idx - (accPhi + 1) * (rDim + 2) - 1;
                float r = (accR - (rDim - 1) * 0.5f) * discretizationStepsR;
                float phi = accPhi * discretizationStepsPhi;
                // Cartesian equation of a line: y = ax + b
                // in polar, y = (-cos(phi)/sin(phi))x + (r/sin(phi))
                // => y = 0 : x = r / cos(phi)
                // => x = 0 : y = r / sin(phi)
                // compute the intersection of this line with the 4 borders of
                // // the image
                int x0 = 0;
                int y0 = (int) (r / PApplet.sin(phi));
                int x1 = (int) (r / PApplet.cos(phi));
                int y1 = 0;
                int x2 = edgeImg.width;
                int y2 = (int) (-PApplet.cos(phi) / PApplet.sin(phi) * x2 + r / PApplet.sin(phi));
                int y3 = edgeImg.width;
                int x3 = (int) (-(y3 - r / PApplet.sin(phi)) * (PApplet.sin(phi) / PApplet.cos(phi)));
                // Finally, plot the lines
                p.stroke(204, 102, 0);
                if (y0 > 0) {
                    if (x1 > 0) p.line(x0, y0, x1, y1);
                    else if (y2 > 0) p.line(x0, y0, x2, y2);
                    else p.line(x0, y0, x3, y3);
                } else {
                    if (x1 > 0) {
                        if (y2 > 0) p.line(x1, y1, x2, y2);
                        else p.line(x1, y1, x3, y3);
                    } else p.line(x2, y2, x3, y3);
                }
            }
        }
    }

    private final class HoughComparator implements Comparator<Integer> {
        int[] accumulator;

        public HoughComparator(int[] accumulator) {
            this.accumulator = accumulator;
        }

        @Override
        public int compare(Integer l1, Integer l2) {
            if (accumulator[l1] > accumulator[l2] || (accumulator[l1] == accumulator[l2] && l1 < l2)) return -1;
            return 1;
        }
    }
}
