import joons.JoonsRenderer;
import peasy.PeasyCam;
import processing.core.*;

public class Main extends PApplet {
	JoonsRenderer jr;
	PeasyCam cam;
	
	// Camera Setting.
	float eyeX = 0;
	float eyeY = -10;
	float eyeZ = 200;
	float centerX = 0;
	float centerY = 0;
	float centerZ = 0;
	float upX = 0;
	float upY = 1;
	float upZ = 0;
	float fov = PI / 4; 
	float aspect = 1f;  
	float zNear = 5;
	float zFar = 10000;
	
	float wallWidth = 130;
	float wallHeight = 130;
	
	public static void main(String[] args) {
		PApplet.main("Main");
	}
	
	public void settings() {
		smooth(8);
		size(1000, 1000, P3D);
	}
	
	public void setup() {
		jr = new JoonsRenderer(this);
		jr.setSampler("bucket");
		jr.setSizeMultiplier(1);
		jr.setAA(0, 2);
		jr.setCaustics(10, 150, 0.2f); // emitInMillions, gather, radius
		jr.setTraceDepths(2, 4, 4); // diffusive, reflective, refractive
		
		cam = new PeasyCam(this, centerX, centerY, centerZ, eyeZ);
		cam.setMinimumDistance(50);
		cam.setMaximumDistance(500);
	}
	
	public void renderScene() {
		jr.beginRecord();
		
		camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
		perspective(fov, aspect, zNear, zFar);
		
		jr.background(255);
		jr.background("gi_ambient");
		
		pushMatrix();
		translate(0, -40, 0);
		drawWall();
		popMatrix();
		
		drawPrism();
		
		jr.endRecord();
		jr.displayRendered(true);
	}
	
	public void drawPrism() {
		float width = 90;
		float faceSize = 15;
		float forward = 40;
		float insetDistance = 0.4f;
		
		pushMatrix();
		translate(30, 0, 60);
		rotateY(-PI/3);
		
		
		// right side
		PVector rightCenter  = new PVector(width / 2, faceSize / 3, forward);
		PVector rightFront = new PVector(width / 2, 0, -faceSize / 2 + forward);
		PVector rightBottom = new PVector(width / 2, faceSize/3 * 2, 0 + forward);
		PVector rightBack = new PVector(width / 2, 0, faceSize / 2 + forward);
		
		PVector rightFrontSmall = PVector.lerp(rightFront, rightCenter, insetDistance);
		PVector rightBackSmall = PVector.lerp(rightBack, rightCenter, insetDistance);
		PVector rightBottomSmall = PVector.lerp(rightBottom, rightCenter, insetDistance);
		
		PVector rightFrontTiny = PVector.lerp(rightFront, rightCenter, 0.8f);
		PVector rightBackTiny = PVector.lerp(rightBack, rightCenter, 0.8f);
		PVector rightBottomTiny = PVector.lerp(rightBottom, rightCenter, 0.8f);
		
		jr.fill("shiny", 50, 50, 50, 3.0f);
		beginShape(TRIANGLE);
		vertex(rightFront.x-0.2f, rightFront.y, rightFront.z);
		vertex(rightBottom.x-0.2f, rightBottom.y, rightBottom.z);
		vertex(rightBack.x-0.2f, rightBack.y, rightBack.z);
		endShape();
		
		jr.fill("light", 255, 255, 255);
		// illumination
		beginShape(TRIANGLE);
		vertex(rightFrontSmall.x, rightFrontSmall.y - 0.4f, rightFrontSmall.z);
		vertex(rightBackSmall.x, rightBackSmall.y - 0.4f, rightBackSmall.z);
		vertex(rightBottomSmall.x, rightBottomSmall.y, rightBottomSmall.z);
		endShape();
		
		// front facing
		beginShape(TRIANGLE);
		vertex(rightFrontSmall.x+0.01f, rightFrontSmall.y - 0.4f, rightFrontSmall.z+0.1f);
		vertex(rightBottomSmall.x + 0.01f, rightBottomSmall.y - 0.01f, rightBottomSmall.z + 0.1f);
		vertex(rightBackSmall.x + 0.01f, rightBackSmall.y - 0.3f, rightBackSmall.z + 0.1f);
		endShape();
		
		jr.fill("glass", 80, 160, 255);
		beginShape(TRIANGLE);
		float glassOffset = 8;
		float depth = 0.7f;
		vertex(rightFrontTiny.x + glassOffset, rightFrontTiny.y, rightFrontTiny.z);
		vertex(rightBottomTiny.x + glassOffset, rightBottomTiny.y, rightBottomTiny.z);
		vertex(rightBackTiny.x + glassOffset, rightBackTiny.y, rightBackTiny.z);
		endShape();
		beginShape(TRIANGLE);
		vertex(rightFrontTiny.x + glassOffset + depth, rightFrontTiny.y, rightFrontTiny.z);
		vertex(rightBottomTiny.x + glassOffset + depth, rightBottomTiny.y, rightBottomTiny.z);
		vertex(rightBackTiny.x + glassOffset + depth, rightBackTiny.y, rightBackTiny.z);
		endShape();
		// top quad
		beginShape(QUADS);
		vertex(rightFrontTiny.x + glassOffset, rightFrontTiny.y, rightFrontTiny.z);
		vertex(rightFrontTiny.x + glassOffset + depth, rightFrontTiny.y, rightFrontTiny.z);
		vertex(rightBackTiny.x + glassOffset + depth, rightBackTiny.y, rightBackTiny.z);
		vertex(rightBackTiny.x + glassOffset, rightBackTiny.y, rightBackTiny.z);
		endShape();
		// front quad
		beginShape(QUADS);
		vertex(rightBackTiny.x + glassOffset + depth, rightBackTiny.y, rightBackTiny.z);
		vertex(rightBackTiny.x + glassOffset, rightBackTiny.y, rightBackTiny.z);
		vertex(rightBottomTiny.x + glassOffset, rightBottomTiny.y, rightBottomTiny.z);
		vertex(rightBottomTiny.x + glassOffset + depth, rightBottomTiny.y, rightBottomTiny.z);
		endShape();
		beginShape(QUADS);
		vertex(rightBottomTiny.x + glassOffset, rightBottomTiny.y, rightBottomTiny.z);
		vertex(rightBottomTiny.x + glassOffset + depth, rightBottomTiny.y, rightBottomTiny.z);
		vertex(rightFrontTiny.x + glassOffset + depth, rightFrontTiny.y, rightFrontTiny.z);
		vertex(rightFrontTiny.x + glassOffset, rightFrontTiny.y, rightFrontTiny.z);
		endShape();
		
		
		// left side
		PVector leftCenter  = new PVector(-width / 2, faceSize / 3, forward);
		PVector leftFront = new PVector(-width / 2, 0, -faceSize / 2 + forward);
		PVector leftBottom = new PVector(-width / 2, faceSize/3 * 2, 0 + forward);
		PVector leftBack = new PVector(-width / 2, 0, faceSize / 2 + forward);
		
		jr.fill("ambient_occlusion", 80, 80, 80);
		beginShape(TRIANGLE);
		vertex(leftFront.x+0.1f, leftFront.y, leftFront.z);
		vertex(leftBottom.x+0.1f, leftBottom.y, leftBottom.z);
		vertex(leftBack.x+0.1f, leftBack.y, leftBack.z);
		endShape();
		
		jr.fill("light", 255, 255, 255);
		beginShape(TRIANGLE);
		PVector leftFrontSmall = PVector.lerp(leftFront, leftCenter, insetDistance);
		vertex(leftFrontSmall.x, leftFrontSmall.y, leftFrontSmall.z);
		
		PVector leftBackSmall = PVector.lerp(leftBack, leftCenter, insetDistance);
		vertex(leftBackSmall.x, leftBackSmall.y, leftBackSmall.z);
		
		PVector leftBottomSmall = PVector.lerp(leftBottom, leftCenter, insetDistance);
		vertex(leftBottomSmall.x, leftBottomSmall.y, leftBottomSmall.z);
		endShape();
		
		// front
		jr.fill("shiny", 50, 50, 50, 3.0f);
		beginShape(QUADS);
		vertex(leftFront.x, leftFront.y, leftFront.z);
		vertex(rightFront.x, rightFront.y, rightFront.z);
		vertex(rightBottom.x, rightBottom.y, rightBottom.z);
		vertex(leftBottom.x, leftBottom.y, leftBottom.z);
		endShape();
		
		// top
		beginShape(QUADS);
		vertex(leftBack.x, leftBack.y, leftBack.z);
		vertex(rightBack.x, rightBack.y, rightBack.z);
		vertex(rightFront.x, rightFront.y, rightFront.z);
		vertex(leftFront.x, leftFront.y, leftFront.z);
		endShape();
		
		// back
		beginShape(QUADS);
		vertex(leftBack.x, leftBack.y, leftBack.z);
		vertex(rightBack.x, rightBack.y, rightBack.z);
		vertex(rightBottom.x, rightBottom.y, rightBottom.z);
		vertex(leftBottom.x, leftBottom.y, leftBottom.z);
		endShape();
		
		popMatrix();
	}
	
	public void drawWall() {
		jr.fill("diffuse", 50, 50, 50);
		
		// back
		drawBackWall();
		
		// left wall
		pushMatrix();
		rotateY(PI * 1.5f);
		translate(wallWidth / 2, 0, wallWidth / 2);
		drawBackWall();
		popMatrix();
		
		// right wall
		pushMatrix();
		rotateY(PI/2);
		translate(-wallWidth/2, 0, wallWidth / 2);
		drawBackWall();
		popMatrix();
		
		// floor
		pushMatrix();
		translate(0, wallWidth/2, wallWidth/2);
		rotateX(PI/2);
		drawBackWall();
		popMatrix();
		
		jr.fill("light", 70, 20, 20);
		// ceil
		pushMatrix();
		beginShape(QUADS);
		vertex(-wallWidth / 4, -wallHeight / 2, 0);
		vertex(-wallWidth / 4, -wallHeight / 2, wallWidth / 2);
		vertex(wallWidth / 4, -wallHeight / 2, wallWidth / 2);
		vertex(wallWidth / 4, -wallHeight / 2, 0);
		
		
		endShape();
		popMatrix();
	}
	
	public void drawBackWall() {
		beginShape(QUADS);
		vertex(-1 * wallWidth / 2, -1 * wallHeight / 2, 0);
		vertex(wallWidth / 2, -1 * wallHeight / 2, 0);
		vertex(wallWidth / 2, wallHeight / 2, 0);
		vertex(-1 * wallWidth / 2, wallHeight / 2, 0);
		endShape(CLOSE);
	}
	
	public void keyPressed() {
		if (key == 'r' || key == 'R') jr.render(); //Press 'r' key to start rendering.
	}
	
	public void draw() {
		renderScene();
	}
}