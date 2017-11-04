import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

import joons.JoonsRenderer;
import peasy.*;

public class Main extends PApplet {
	JoonsRenderer jr;
	
	PeasyCam cam;

	//Camera Setting.
	float eyeX = 0;
	float eyeY = 0;
	float eyeZ = 120;
	float centerX = 0;
	float centerY = 0;
	float centerZ = -1;
	float upX = 0;
	float upY = 1;
	float upZ = 0;
	float fov = PI / 4; 
	float aspect = 16/9f;  
	float zNear = 5;
	float zFar = 10000;
	
	float boxWidth = 400;
	float boxHeight = 200;
	
	int frame = 0;
	
	int numGlobes = 10;
	ArrayList<PVector> globes;
	Float[] speeds;
	
	public static void main(String[] args) {
		PApplet.main("Main");
	}
	
	public void settings() {
		size(1280, 720, P3D);
	}
	
	public void setup() {
		jr = new JoonsRenderer(this);
		jr.setSampler("bucket");
		jr.setSizeMultiplier(1);
		jr.setAA(-2, 0);
		jr.setCaustics(20);
		//jr.setDOF(100,  1);
		
		speeds = new Float[numGlobes];
		globes = new ArrayList<PVector>();
		for(int i = 0; i < numGlobes; i++) {
			globes.add(PVector.random3D().mult(boxWidth / 2));
			speeds[i] = random(0.02f);
		}
		
		cam = new PeasyCam(this, 100);
		cam.setMinimumDistance(50);
		cam.setMaximumDistance(500);
	}
	
	public void draw() {
		jr.render();
		jr.beginRecord(); //Make sure to include things you want rendered.
		  camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
		  perspective(fov, aspect, zNear, zFar);

		  jr.background(20);
		  
		  drawBox();
		  
		  jr.fill("light", 20, 20, 20);
		  beginShape(QUADS);
		  float gap = 5;
		  vertex(-20, -boxHeight / 2 + gap, 20);
		  vertex(20, -boxHeight / 2 + gap, 20);
		  vertex(20, -boxHeight / 2 + gap, -20);
		  vertex(-20, -boxHeight / 2 + gap, -20);
		  endShape(CLOSE);
		  
//		  jr.fill("light", 20, 20, 20);
//		  pushMatrix();
//		  translate(-20, -30, -20);
//		  sphere(2);
//		  popMatrix();
		  
		  for(int i = 0; i < globes.size(); i++) {
			  PVector globe = globes.get(i);
			  jr.fill("glass", 255, 255, 255, 2.0f, 5f, 255, 255, 255);
			  pushMatrix();
			  rotateY(speeds[i] * frame);
			  translate(globe.x, 0, globe.y);
			  sphere(15);
			  popMatrix();
		  }

		  jr.endRecord();
		  jr.displayRendered(true);
		  saveFrame("frames/line-#####.png");
		  
		  frame++;
	}
	
	public void keyPressed() {
		if (key == 'r' || key == 'R') jr.render(); //Press 'r' key to start rendering.
	}
	
	public void drawBox() {
		jr.fill("diffuse", 255, 255, 255);
		
		// bottom
		beginShape(QUADS);
	    vertex(-boxWidth / 2, boxHeight / 2, -boxWidth / 2);
	    vertex(boxWidth / 2, boxHeight / 2, -boxWidth / 2);
	    vertex(boxWidth / 2, boxHeight / 2, boxWidth / 2);
	    vertex(-boxWidth / 2, boxHeight / 2, boxWidth / 2);
	    endShape(CLOSE);
	    
	    // left
	    beginShape(QUADS);
	    vertex(-boxWidth / 2, boxHeight / 2, boxWidth / 2);
	    vertex(-boxWidth / 2, boxHeight / 2, -boxWidth / 2);
	    vertex(-boxWidth / 2, -boxHeight / 2, -boxWidth / 2);
	    vertex(-boxWidth / 2, -boxHeight / 2, boxWidth / 2);
	    endShape(CLOSE);
	    
	    //top
	    beginShape(QUADS);
	    vertex(-boxWidth / 2, -boxHeight / 2, -boxWidth / 2);
	    vertex(-boxWidth / 2, -boxHeight / 2, boxWidth / 2);
	    vertex(boxWidth / 2, -boxHeight / 2, boxWidth / 2);
	    vertex(boxWidth / 2, -boxHeight / 2, -boxWidth / 2);
	    endShape(CLOSE);
	    
	    // back
	    beginShape(QUADS);
	    vertex(-boxWidth / 2, -boxHeight / 2, -boxWidth / 2);
	    vertex(boxWidth / 2, -boxHeight / 2, -boxWidth / 2);
	    vertex(boxWidth / 2, boxHeight / 2, -boxWidth / 2);
	    vertex(-boxWidth / 2, boxHeight / 2, -boxWidth / 2);
	    endShape(CLOSE);
	    
	    // right
	    beginShape(QUADS);
	    vertex(boxWidth / 2, boxHeight / 2, boxWidth / 2);
	    vertex(boxWidth / 2, boxHeight / 2, -boxWidth / 2);
	    vertex(boxWidth / 2, -boxHeight / 2, -boxWidth / 2);
	    vertex(boxWidth / 2, -boxHeight / 2, boxWidth / 2);
	    endShape(CLOSE);
	}
}