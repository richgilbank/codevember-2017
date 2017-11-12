import java.util.Collection;
import java.util.List;

import joons.JoonsRenderer;
import peasy.PeasyCam;
import processing.core.*;
import toxi.processing.ToxiclibsSupport;
import toxi.geom.*;
import toxi.geom.mesh.Face;
import toxi.geom.mesh.Vertex;
import toxi.geom.mesh.WETriangleMesh;
import toxi.geom.mesh2d.DelaunayTriangulation;
import toxi.geom.mesh2d.Voronoi;

public class Main extends PApplet {
	JoonsRenderer jr;
	
	PeasyCam cam;
	ToxiclibsSupport gfx;
	
	//Camera Setting.
	float eyeX = 0;
	float eyeY = 0;
	float eyeZ = 150;
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
	
	float floorHeight = wallHeight / 6;
	float floorDepth = 150;
	
	int numFragments = 10;
	
	float[][] yHeights = new float[numFragments][numFragments];
	
	public static void main(String[] args) {
		PApplet.main("Main");
	}
	
	public void settings() {
		size(720, 720, P3D);
	}
	
	public void setup() {
		jr = new JoonsRenderer(this);
		jr.setSampler("bucket");
		jr.setSizeMultiplier(1);
		jr.setAA(0, 2, 2);
		jr.setCaustics(10, 150, 0.2f); // emitInMillions, gather, radius
		jr.setTraceDepths(2, 4, 4); // diffusive, reflective, refractive
//		jr.setDOF(30, 0.5f);
		
		gfx = new ToxiclibsSupport(this);
		
		for(int x = 0; x < numFragments; x++) {
			for(int z = 0; z < numFragments; z++) {
				if((x == 0 || x == numFragments - 1) && (z == 0 || z == numFragments - 1)) {
					yHeights[x][z] = floorHeight;
				}
				else {
					yHeights[x][z] = floorHeight - noise(x * 1f, z * 1f) * 10;
				}
			}
		}
		
		cam = new PeasyCam(this, centerX, centerY, centerZ, eyeZ);
		cam.setMinimumDistance(50);
		cam.setMaximumDistance(500);
		
		renderScene();
	}
	
	public void renderScene() {
		//jr.render();
		jr.beginRecord();
		
		//camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
		perspective(fov, aspect, zNear, zFar);
		
		jr.background(255);
		jr.background("gi_ambient_occlusion");
		
		drawWall();
		drawFloor();
		drawLight();
		
		jr.endRecord();
		jr.displayRendered(true);
		//saveFrame("frames/" + hour() + minute() + second() + ".png");
	}
	
	public void drawLight() {
		float amplitude = 15f;
		float a = 0f;
		float shapeWidth = 2;
		float inc = TWO_PI/ (shapeWidth / 2);
		
		pushMatrix();
		translate(-wallWidth/2.5f, -10, 5);
		sphereDetail(4);
		jr.fill("light", 255, 0, 0);
		for (float x = 0; x < shapeWidth; x++) {
			float adjustedAmplitude = pow(sin(x / shapeWidth * PI), 2);
			pushMatrix();
			translate(x * 5, sin(a) * amplitude * adjustedAmplitude, 0);
			sphere(1f);
			popMatrix();
			a = a + inc;
		}
		popMatrix();
		
		// top light
		jr.fill("mirror", 255, 255, 255);
		pushMatrix();
		beginShape(QUADS);
		vertex(-wallWidth / 2, -wallHeight/2, 0);
		vertex(-wallWidth / 2, -wallHeight/2, floorDepth);
		vertex(wallWidth / 2, -wallHeight/2, floorDepth);
		vertex(wallWidth / 2, -wallHeight/2, 0);
		endShape();
		popMatrix();
	}
	
	public void drawFloor() {
		float xIncr = floor(wallWidth / (numFragments - 1));
		float zIncr = floor(floorDepth / (numFragments - 1)) + 2;
		
		for(int x = 1; x < numFragments; x++) {
			for(int z = 1; z < numFragments; z++) {
				float xPos = xIncr * x - (wallWidth / 2);
				float zPos = zIncr * z;
				
				float midY = lerp(yHeights[x-1][z-1], yHeights[x][z], 0.5f);
				jr.fill("mirror", 190, 220, 255);
				
				// back
				beginShape(TRIANGLE);
				vertex(xPos - xIncr / 2, midY, zPos - zIncr / 2); // center
				vertex(xPos - xIncr, yHeights[x-1][z-1], zPos - zIncr); // back left
				vertex(xPos, yHeights[x][z-1], zPos - zIncr); // back right
				endShape();
				
				// left
				beginShape(TRIANGLE);
				vertex(xPos - xIncr / 2, midY, zPos - zIncr / 2); // center
				vertex(xPos - xIncr, yHeights[x-1][z-1], zPos - zIncr); // back left
				vertex(xPos - xIncr, yHeights[x-1][z], zPos); // front left
				endShape();
				
				// front
				beginShape(TRIANGLE);
				vertex(xPos - xIncr / 2, midY, zPos - zIncr / 2); // center
				vertex(xPos - xIncr, yHeights[x-1][z], zPos); // front left
				vertex(xPos, yHeights[x][z], zPos); // front right
				endShape();
				
				// right
				beginShape(TRIANGLE);
				vertex(xPos - xIncr / 2, midY, zPos - zIncr / 2); // center
				vertex(xPos, yHeights[x][z], zPos); // front right
				vertex(xPos, yHeights[x][z-1], zPos - zIncr); // back right
				endShape();
			}
		}
		
//		strokeWeight(5);
//		for(int i = 0; i < verts.size(); i++) {
//			Vec3D vert = floorMesh.getVertexForID(i);
//			float blue = map(0, verts.size(), 0, 255, i);
//			float red = map(0, verts.size(), 0, 255, verts.size() - i);
//			stroke(red, 0, blue);
//			point(vert.x, vert.y, vert.z);
//		}
//		for(Face face : floorMesh.getFaces()) {
//			Triangle3D triangle = face.toTriangle();
//			Vec3D[] verts = triangle.getVertexArray();
//			beginShape(TRIANGLE);
//			for(Vec3D vert : verts) {
//				float yHeight = vert.y + (noise(vert.x * 0.05f, vert.y * 0.05f) * 100);
//				vertex(vert.x, yHeight, vert.z);
//			}
//			endShape(CLOSE);
//		}
	}
		
	public void drawWall() {
		jr.fill("diffuse", 255, 255, 255);
		beginShape(QUADS);
		vertex(-1 * wallWidth / 2, -1 * wallHeight / 2, 0);
		vertex(wallWidth / 2, -1 * wallHeight / 2, 0);
		vertex(wallWidth / 2, wallHeight / 2, 0);
		vertex(-1 * wallWidth / 2, wallHeight / 2, 0);
		endShape(CLOSE);
		
		// left wall
		pushMatrix();
		rotateY(PI * 1.5f);
		translate(wallWidth / 2, 0, wallWidth / 2);
		beginShape(QUADS);
		vertex(-1 * wallWidth / 2, -1 * wallHeight / 2, 0);
		vertex(wallWidth / 2, -1 * wallHeight / 2, 0);
		vertex(wallWidth / 2, wallHeight / 2, 0);
		vertex(-1 * wallWidth / 2, wallHeight / 2, 0);
		endShape(CLOSE);
		popMatrix();
		
		// right wall
		pushMatrix();
		rotateY(PI/2);
		translate(-wallWidth/2, 0, wallWidth / 2);
		beginShape(QUADS);
		vertex(-1 * wallWidth / 2, -1 * wallHeight / 2, 0);
		vertex(wallWidth / 2, -1 * wallHeight / 2, 0);
		vertex(wallWidth / 2, wallHeight / 2, 0);
		vertex(-1 * wallWidth / 2, wallHeight / 2, 0);
		endShape(CLOSE);
		popMatrix();
		
		// right wall
		pushMatrix();
		rotateY(PI);
		translate(0, 0, -floorDepth);
		beginShape(QUADS);
		vertex(-1 * wallWidth / 2, -1 * wallHeight / 2, 0);
		vertex(wallWidth / 2, -1 * wallHeight / 2, 0);
		vertex(wallWidth / 2, wallHeight / 2, 0);
		vertex(-1 * wallWidth / 2, wallHeight / 2, 0);
		endShape(CLOSE);
		popMatrix();
	}
	
	public void keyPressed() {
		if (key == 'r' || key == 'R') jr.render(); //Press 'r' key to start rendering.
	}
	
	public void draw() {
		renderScene();
	}
}