import processing.core.*;

import java.util.ArrayList;

import peasy.*;

public class Main extends PApplet {
	PeasyCam cam;
	
	int resolution = 100;
	float[][] noiseLevels = new float[resolution][resolution];
	ArrayList<ArrayList<PVector>> directionChanges = new ArrayList<ArrayList<PVector>>();
	
	float rowGap = 5;
	float heightMultiplier = 250;
	
	float width = rowGap * resolution;
	float height = rowGap * resolution;
	
	float eyeX = 0;
	float eyeY = height / 2;
	float eyeZ = 200;
	float centerX = width / 2;
	float centerY = height / 2;
	float centerZ = 0;
	float upX = 0;
	float upY = 1;
	float upZ = 0;
	
	public static void main(String[] args) {
		PApplet.main("Main");
	}
	
	public void settings() {
		size(720, 720, P3D);
	}
	
	public void setup() {
		cam = new PeasyCam(this, eyeX, eyeY, eyeZ, 100);
		cam.lookAt(width /2,  height / 2,  0);
		cam.setMinimumDistance(50);
		cam.setMaximumDistance(500);
		
		calculatePoints();
	}
	
	public void calculatePoints() {
		boolean direction = true;
		
		for(int x = 0; x < resolution; x++) {
			directionChanges.add(new ArrayList());
			for(int y = 0; y < resolution; y++) {
				float noiseLevel = noise(x * 0.02f, y * 0.02f);
				noiseLevels[x][y] = noiseLevel;
				if(y == 0) {
					directionChanges.get(x).add(new PVector(x, y));
				}
				if(y == 1) {
					direction = noiseLevels[x][y-1] > noiseLevel ? false : true;
				}
				if(y >= 1) {
					if((direction && noiseLevel < noiseLevels[x][y-1]) || (!direction && noiseLevel > noiseLevels[x][y-1])) {
						direction = !direction;
						directionChanges.get(x).add(new PVector(x, y));
					}
				}
				if(y == resolution - 1) {
					directionChanges.get(x).add(new PVector(x, y));
				}
			}
		}
	}
		
	public void draw() {
//		camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
		
		background(0);
		stroke(255);
		strokeWeight(4);
		
		for(int x = 0; x < resolution; x++) {
			for(int y = 0; y < directionChanges.get(x).size(); y++) {
				PVector loc = directionChanges.get(x).get(y);
				if(y > 0) {
					PVector prevLoc = directionChanges.get(x).get(y-1);
					PVector thisPoint = new PVector(loc.x * rowGap, loc.y * rowGap, noiseLevels[round(loc.x)][round(loc.y)] * heightMultiplier);
					PVector prevPoint = new PVector(prevLoc.x * rowGap, prevLoc.y * rowGap, noiseLevels[round(prevLoc.x)][round(prevLoc.y)] * heightMultiplier);
					line(thisPoint.x, thisPoint.y, thisPoint.z, prevPoint.x, prevPoint.y, prevPoint.z);
				}
			}
//			for(int y = 0; y < resolution; y++) {
//				point(x * rowGap, y * rowGap, noiseLevels[x][y] * heightMultiplier);
//			}
		}
	}
}