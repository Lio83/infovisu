package cs211.tangiblegame;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JFrame;

import com.sun.javafx.stage.WindowCloseRequestHandler;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.video.Capture;
import processing.video.Movie;
import cs211.imageprocessing.QuadGraph;
import cs211.imageprocessing.TwoDThreeD;
import cs211.imageprocessing.transformers.BinaryThreshold;
import cs211.imageprocessing.transformers.GaussianBlur;
import cs211.imageprocessing.transformers.HSBThreshold;
import cs211.imageprocessing.transformers.Hough;
import cs211.imageprocessing.transformers.ImageTransformer;
import cs211.imageprocessing.transformers.Sobel;
import cs211.tangiblegame.GameParameters;
import cs211.tangiblegame.GameSettings_backup.CWComparator;

public class GameWithMenu extends PApplet implements GameParameters { 
	Button buttonPlay;
	Button buttonSettings;
	PApplet appletGame;
	PApplet appletSettings;
	float[] hsbParams = {100, 140, 130, 255, 70, 220};
	float blurParams = 10;
	float[] houghParams = {6, 160};
	
	public void setup() {
        size(WINDOW_WIDTH, WINDOW_HEIGHT, P3D);
        buttonPlay = new Button(this, "Play", width/2, height/2 - 50, 150);
        buttonSettings = new Button(this, "Settings", width/2, height/2 + 50, 150);
	}
	
	final static int BUTTON_WIDTH = 120;
	final static int BUTTON_HEIGHT = 30;
	
	public void draw() {
		buttonPlay.update();
		buttonPlay.display();
		buttonSettings.update();
		buttonSettings.display();
	}
	@Override
	public void mouseClicked() {
		PFrame frameSettings = null;
		PFrame frameGame = null;
		if (buttonSettings.hover) {
			appletSettings = new GameSettings();
		}
		
		if (buttonPlay.hover) {
			appletGame = new TangibleGame(hsbParams, blurParams, houghParams);
			frameGame = new PFrame(appletGame, WINDOW_WIDTH, WINDOW_HEIGHT);
		}
	}
	
	class PFrame extends JFrame {
		  public PFrame(PApplet applet, int width, int height) {
			setSize(width+15, height+45);
		    add(applet);
		    applet.init();
		    setVisible(true);
	  }
	}
	
	class GameSettings extends PApplet implements GameParameters {
		private Capture cam;
		PImage src;
		ImageTransformer hsb, blur, binary, sobel;
		Hough hough;

		private HScrollbar[] hsbScrollBars = new HScrollbar[6];
		private String[] hsbTexts = {"Hue min :", "Hue max :", "Sat. min :", "Sat. max :", "Bright. min :", "Bright. max :"};
	    
		private HScrollbar houghScrollLines;
		private HScrollbar houghScrollVote;
		
		Button buttonSave;
		
		PFrame myFrame;
		
		public GameSettings() {
	    	myFrame = new PFrame(this, WINDOW_SETTINGS_WIDTH, WINDOW_SETTINGS_HEIGHT);
		}
		
	    @Override
	    public void setup() {	    	
	        size(WINDOW_SETTINGS_WIDTH, WINDOW_SETTINGS_HEIGHT);
	        
	        String[] cameras = Capture.list();
	        cam = new Capture(this, cameras[0]);
	        cam.start();        

	        hsb = new HSBThreshold(this);
	        blur = new GaussianBlur(this);
	        binary = new BinaryThreshold(this);
	        sobel = new Sobel(this);
	        hough = new Hough(this);
	        
	        // In order : hMin, hMax, sMin, sMax, bMin, bMax
	        for (int i = 0; i < hsbScrollBars.length; i++) {
		        hsbScrollBars[i] = new HScrollbar(this, 130, WINDOW_SETTINGS_HEIGHT - (i+1)*30, 200, 15, hsbParams[i]/255f);
			}
	        houghScrollLines = new HScrollbar(this, 500, WINDOW_SETTINGS_HEIGHT - 180, 200, 15, houghParams[0]/10);
	        houghScrollVote = new HScrollbar(this, 500, WINDOW_SETTINGS_HEIGHT - 150, 200, 15, houghParams[1]/200);
	        buttonSave = new Button(this, "Save", width-100, height-20, 100);

	    }

	    @Override
	    public void draw() {
	        background(color(0, 0, 0));
	        if (cam.available()) {
	            cam.read();
	            src = cam.get();
	            PImage h, b, t, s;

	            h = hsb.apply(src, hsbParams);
	            b = blur.apply(h, blurParams);
	            t = binary.apply(b);
	            s = sobel.apply(t);
	            hough.apply(s, houghParams);
	            hough.intersections(s);
	            image(src, 0, 0);

	            h.resize(320, 240);
	            b.resize(320, 240);
	            t.resize(320, 240);
	            s.resize(320, 240);
	            image(h, 640, 0);
	            image(b, 960, 0);
	            image(t, 640, 240);
	            image(s, 960, 240);
	            hough.getLines(s);

	            pushMatrix();
	            for (int i = 0; i < hsbScrollBars.length; i++) {
					hsbScrollBars[i].display();
					hsbScrollBars[i].update();
		            hsbParams[i] = 255*hsbScrollBars[i].getPos();
		            fill(255,255,255);
		            textSize(20);
		        	text(hsbTexts[i], 0, height - (i+1)*30, 130f, 25f);
		        	text(""+(int)(hsbScrollBars[i].getPos()*255), 350, height - (i+1)*30, 130f, 25f);
				}
	            // Params of hough
	            houghScrollLines.display();
	            houghScrollLines.update();
	            houghParams[0] = (int)(houghScrollLines.getPos()*10);
	            houghScrollVote.display();
	            houghScrollVote.update();
	            houghParams[0] = (houghScrollVote.getPos()*200);
	            fill(255, 255, 255);
	            text((int)(houghScrollLines.getPos()*10), 800, height - 150);
	            text((int)(houghScrollVote.getPos()*200), 800, height - 125);
	            
	            popMatrix();
	            buttonSave.display();
	            buttonSave.update();
	        }
	    }
	    @Override
	    public void mouseClicked() {
	    	// TODO Auto-generated method stub
	    	super.mouseClicked();	
	    	if (buttonSave.hover) {
	    		cam.stop();
	    		cam = null;
	    		stop();
	    		destroy();
	    		setVisible(false);
	    		myFrame.setVisible(false);
	    	}
	    }
	}
}
