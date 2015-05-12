package cs211.imageprocessing.transformers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public final class Hough implements ImageTransformer {
    private final PApplet p;

    private float discretizationStepsPhi = 0.06f;
    private float discretizationStepsR = 2.5f;
    private int rDim = 0;
    private static int nLines = 10;
    private static int minVotes = 200;
    
    private int[] accumulator, accumulatorNew;

    private final int phiDim = (int) (PConstants.PI / discretizationStepsPhi);
    private final float[] tabSin = new float[phiDim];
    private final float[] tabCos = new float[phiDim];

    public Hough(PApplet parent) {
        p = parent;
        float phi = 0f;
        for (int i = 0; i < phiDim; ++i, phi += discretizationStepsPhi) {
            tabSin[i] = PApplet.sin(phi);
            tabCos[i] = PApplet.cos(phi);
        }
    }

    /**
     * @param params
     *            the number of lines and the minimum number of votes.
     */
    @Override
    public PImage apply(PImage src, float... params) {
        if (params != null && params.length == 2) {
            nLines = (int)params[0];
            minVotes = (int)params[1];
        }
        
        accumulator = houghAccumulator(src);
        
        PImage houghImg = p.createImage(rDim + 2, phiDim + 2, PConstants.RGB); // TODO : ev. ALPHA

        for (int i = 0; i < accumulator.length; ++i)
            houghImg.pixels[i] = p.color(PApplet.min(255, accumulator[i]));
        houghImg.updatePixels();

        //houghImg.resize(width, height);
        return houghImg;
    }
    
    public ArrayList<PVector> intersections(PImage src, float... params) { // TODO : ajout des paramètres
        accumulatorNew = vote(nLines);
        return getIntersections(getLines(src));
    }
    
    private int[] houghAccumulator(PImage src) {

        int width = src.width, height = src.height;
        rDim = (int) (((width + height) * 2 + 1) / discretizationStepsR);

        final int[] accumulator = new int[(phiDim + 2) * (rDim + 2)];

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (p.brightness(src.get(x, y)) != 0) { // Are we on an edge?
                    float phi = 0f;
                    for (int i = 0; i < phiDim; ++i, phi += discretizationStepsPhi) {

                        float r = x * tabCos[i] + y * tabSin[i];

                        float accPhi = PApplet.round(phi / discretizationStepsPhi);
                        float accR = PApplet.round(r / discretizationStepsR + (rDim - 1) / 2);

                        int idx = (int) (accR + 1 + (accPhi + 1) * (rDim + 2));

                        accumulator[idx] += 1;
                    }
                }
            }
        }
        return accumulator;
    }

    private int[] vote(int nLines) {
        int minVotes = 100;
        int neighbourhood = 10;

        ArrayList<Integer> candidates = new ArrayList<Integer>();

        for (int accR = 0; accR < rDim; ++accR) {
            for (int accPhi = 0; accPhi < phiDim; ++accPhi) {
                // compute current index in the accumulator
                int idx = (accPhi + 1) * (rDim + 2) + accR + 1;
                if (accumulator[idx] > minVotes) {
                    boolean bestCandidate = true;
                    // iterate over the neighbourhood
                    for (int dPhi = -neighbourhood / 2; dPhi < neighbourhood / 2 + 1; ++dPhi) {
                        if (accPhi + dPhi < 0 || accPhi + dPhi >= phiDim) continue;
                        for (int dR = -neighbourhood / 2; dR < neighbourhood / 2 + 1; ++dR) {
                            if (accR + dR < 0 || accR + dR >= rDim) continue;

                            int neighbourIdx = (accPhi + dPhi + 1) * (rDim + 2) + accR + dR + 1;

                            if (accumulator[idx] < accumulator[neighbourIdx]) {
                                // the current idx is not a local maximum!
                                bestCandidate = false;
                                break;
                            }
                        }
                        if (!bestCandidate) break;
                    }
                    if (bestCandidate) candidates.add(idx);
                }
            }
        }

        Collections.sort(candidates, new Comparator<Integer>() {
            @Override
            public int compare(Integer i, Integer j) {
                if (accumulator[i] > accumulator[j] || accumulator[i] == accumulator[j] && i < j) 
                    return -1;
                return 1;
            }
        });

        int[] accumulatorNew = new int[(phiDim + 2) * (rDim + 2)];

        for (int i = 0; i < nLines; ++i) {
            if (i < candidates.size()) {
                int idx = candidates.get(i);
                accumulatorNew[idx] = accumulator[idx];
            }
        }
        return accumulatorNew;
    }

    public ArrayList<PVector> getLines(PImage src) {
        ArrayList<PVector> lines = new ArrayList<PVector>();

        if (accumulatorNew == null) accumulatorNew = vote(nLines);
        
        for (int idx = 0; idx < accumulatorNew.length; ++idx) {
            if (accumulatorNew[idx] > minVotes) {
                // first, compute back the (r, phi) polar coordinates:
                int accPhi = idx / (rDim + 2) - 1;
                int accR = idx - (accPhi + 1) * (rDim + 2) - 1;
                float r = (accR - (rDim - 1) * 0.5f) * discretizationStepsR;
                float phi = accPhi * discretizationStepsPhi;

                lines.add(new PVector(r, phi));

                int x0 = 0;
                int y0 = (int) (r / PApplet.sin(phi));
                int x1 = (int) (r / PApplet.cos(phi));
                int y1 = 0;
                int x2 = src.width;
                int y2 = (int) (-PApplet.cos(phi) / PApplet.sin(phi) * x2 + r / PApplet.sin(phi));
                int y3 = src.width;
                int x3 = (int) (-(y3 - r / PApplet.sin(phi)) * (PApplet.sin(phi) / PApplet.cos(phi)));

                // Finally, plot the lines
                p.stroke(204, 102, 0);
                if (y0 > 0) {
                    if (x1 > 0) p.line(x0, y0, x1, y1);
                    else if (y2 > 0) p.line(x0, y0, x2, y2);
                    else p.line(x0, y0, x3, y3);
                } else if (x1 > 0) {
                    if (y2 > 0) p.line(x1, y1, x2, y2);
                    else p.line(x1, y1, x3, y3);
                } else p.line(x2, y2, x3, y3);
            }
        }
        return lines;
    }

    private ArrayList<PVector> getIntersections(ArrayList<PVector> lines) {
        ArrayList<PVector> intersections = new ArrayList<PVector>();
        
        for (int i = 0; i < lines.size() - 1; ++i) {
            PVector line1 = lines.get(i);
            float r1 = line1.x;
            float phi1 = line1.y;

            for (int j = i + 1; j < lines.size(); ++j) {
                PVector line2 = lines.get(j);
                float r2 = line2.x;
                float phi2 = line2.y;

                // compute the intersection and add it to 'intersections'
                float d = PApplet.cos(phi2) * PApplet.sin(phi1) - PApplet.cos(phi1) * PApplet.sin(phi2);
                float x = (r2 * PApplet.sin(phi1) - r1 * PApplet.sin(phi2)) / d;
                float y = (-1 * r2 * PApplet.cos(phi1) + r1 * PApplet.cos(phi2)) / d;

                PVector pv = new PVector(x, y);
                if (x >= 0 && x <= p.width && y >= 0 && y <= p.height) intersections.add(pv);

                // draw the intersection
                p.fill(255, 128, 0);
                p.ellipse(x, y, 10, 10);
            }
        }
        return intersections;
    }

    /**
     * Default value for number of lines 10 <br>
     * and for number of votes 200
     */
    @Override
    public PImage apply(PImage src) {
        return apply(src);
    }
}
