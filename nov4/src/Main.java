import java.util.ArrayList;

import joons.JoonsRenderer;
import peasy.PeasyCam;
import processing.core.*;

public class Main extends PApplet {
	JoonsRenderer jr;
	
	PeasyCam cam;

	//Camera Setting.
	float eyeX = 0;
	float eyeY = 0;
	float eyeZ = 180;
	float centerX = 0;
	float centerY = 0;
	float centerZ = -1;
	float upX = 0;
	float upY = 1;
	float upZ = 0;
	float fov = PI / 4; 
	float aspect = 9/16f;  
	float zNear = 5;
	float zFar = 10000;
	
	float wallWidth = 90;
	float wallHeight = 150;
	
	int shardCount = 500;
	ArrayList<Shard> shards;
	
	public static void main(String[] args) {
		PApplet.main("Main");
	}
	
	public void settings() {
		size(720, 1280, P3D);
	}
	
	public void setup() {
		jr = new JoonsRenderer(this);
		jr.setSampler("bucket");
		jr.setSizeMultiplier(1);
		jr.setAA(-2, 0);
		jr.setCaustics(2, 150, 0.5f); // emitInMillions, gather, radius
		jr.setTraceDepths(1, 2, 2); // diffusive, reflective, refractive
		
		cam = new PeasyCam(this, centerX, centerY, centerZ, eyeZ);
		cam.setMinimumDistance(50);
		cam.setMaximumDistance(500);
		
		shards = new ArrayList<Shard>();
		for(int i = 0; i < shardCount; i++) {
			shards.add(new Shard(this, jr, wallWidth, wallHeight));
		}
		
		renderScene();
	}
	
	public void renderScene() {
		jr.render();
		jr.beginRecord();
		
		//camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
		perspective(fov, aspect, zNear, zFar);
		
		jr.background(50);
		jr.background("gi_ambient_occlusion");
		
		drawLight();
		drawWall();
		drawShards();
		
		jr.endRecord();
		jr.displayRendered(true);
		//saveFrame("frames/shard-####.png");
	}
	
	public void drawShards() {
		for(Shard shard : shards) {
			shard.draw();
		}
	}
	
	public void drawLight() {
		float lightWidth = wallWidth;
		float lightDepth = 80;
		float lightHeight = -1 * wallHeight / 2;
		float lightLowerHeight = lightHeight + 20;
		
		jr.fill("light", 1, 1, 1);
		beginShape(QUADS);
		vertex(-1 * lightWidth / 2, lightLowerHeight, lightDepth);
		vertex(lightWidth / 2, lightLowerHeight, lightDepth);
		vertex(lightWidth / 2, lightHeight, 0);
		vertex(-1 * lightWidth / 2, lightHeight, 0);
		endShape(CLOSE);
	}
	
	public void drawWall() {
		float sideWallDepth = 80;
		
		jr.fill("diffuse", 255, 255, 255);
		beginShape(QUADS);
		vertex(-1 * wallWidth / 2, -1 * wallHeight / 2, 0);
		vertex(wallWidth / 2, -1 * wallHeight / 2, 0);
		vertex(wallWidth / 2, wallHeight / 2, 0);
		vertex(-1 * wallWidth / 2, wallHeight / 2, 0);
		endShape(CLOSE);
		
		// right side
		jr.fill("mirror");
		beginShape(QUADS);
		vertex(wallWidth / 2, -1 * wallHeight / 2, 0);
		vertex(wallWidth / 2, -1 * wallHeight / 2, sideWallDepth);
		vertex(wallWidth / 2, wallHeight / 2, sideWallDepth);
		vertex(wallWidth / 2, wallHeight / 2, 0);
		endShape(CLOSE);
		
		// left side
		beginShape(QUADS);
		vertex(-1 * wallWidth / 2, -1 * wallHeight / 2, 0);
		vertex(-1 * wallWidth / 2, wallHeight / 2, 0);
		vertex(-1 * wallWidth / 2, wallHeight / 2, sideWallDepth);
		vertex(-1 * wallWidth / 2, -1 * wallHeight / 2, sideWallDepth);
		endShape(CLOSE);
	}
	
	public void keyPressed() {
		println("pressed");
		if (key == 'r' || key == 'R') jr.render(); //Press 'r' key to start rendering.
	}
	
	public void draw() {
		//renderScene();
	}
}