import wblut.math.*;
import wblut.processing.*;
import wblut.core.*;
import wblut.hemesh.*;
import wblut.geom.*;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

import joons.JoonsRenderer;
import peasy.PeasyCam;

public class Main extends PApplet {
	JoonsRenderer jr;
	PeasyCam cam;
	
	// Camera Setting.
	float eyeX = 0;
	float eyeY = -140;
	float eyeZ = 1000;
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
	
	HE_Mesh mesh;
	WB_Render render;
	
	public static void main(String[] args) {
		PApplet.main("Main");
	}
	
	public void settings() {
		size(1000, 1000, P3D);
	}
	
	public void setup() {
		jr = new JoonsRenderer(this);
		jr.setSampler("bucket");
		jr.setSizeMultiplier(2);
		jr.setAA(0, 2);
		jr.setCaustics(10, 150, 0.5f); // emitInMillions, gather, radius
		jr.setTraceDepths(1, 2, 2); // diffusive, reflective, refractive
		
		render = new WB_Render(this);
		
		cam = new PeasyCam(this, centerX, centerY, centerZ, eyeZ);
		cam.setMinimumDistance(50);
		cam.setMaximumDistance(1000);
		
		HEC_Archimedes creator1 = new HEC_Archimedes();
		creator1.setType(4); // between 1 & 13
		creator1.setEdge( 64 );
		creator1.setCenter(0, 0, 0).setZAxis(1, 1, 1).setZAngle(PI/4);
		
		mesh = new HE_Mesh(creator1);
		
		HEM_Wireframe modifier = new HEM_Wireframe();
		modifier.setStrutRadius(50);
		modifier.setStrutFacets( 6 ).setMaximumStrutOffset( 21 );
		mesh.modify(modifier);
	    mesh.modify( new HEM_Soapfilm().setIterations( 3 ) );
		
		HEM_Twist twist = new HEM_Twist().setAngleFactor(4.2);
		WB_Line L = new WB_Line(0, 100, 0, 0, 100, 0);
		twist.setTwistAxis(L);
		mesh.modify(twist);
	}
	
	public void draw() {
		jr.beginRecord();
		
		camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
		perspective(fov, aspect, zNear, zFar);
		
		jr.background(255);
		
		jr.fill("ambient_occlusion", 200, 200, 200);
		pushMatrix();
		mesh.triangulate();
		rotateX(-PI/7);
//		render.drawFaces(mesh);
		beginShape(TRIANGLES);
		HE_Face[] faces = mesh.getFacesAsArray();
		
		for(int i=0; i < faces.length; i++) {
			List<HE_Vertex> verts = faces[i].getFaceVertices();
			for(HE_Vertex v : verts) {
				vertex(v.xf(), v.yf(), v.zf());
			}
		}
		//sphere(100);
		endShape();
		popMatrix();
		
		// floor
		jr.fill("ambient_occlusion", 255, 255, 255);
		beginShape(QUADS);
		float h = 100;
		float size = 10000;
		vertex(-size, h, -size);
		vertex(size, h, -size);
		vertex(size, h, size);
		vertex(-size, h, size);
		endShape();
		
		// back
		beginShape(QUADS);
		vertex(-size, size, -h);
		vertex(size, size, -h);
		vertex(size, -size, -h);
		vertex(-size, -size, -h);
		endShape();
		
		// light
		jr.fill("light", 255, 255, 255);
		beginShape(QUADS);
		float lightWidth = 300;
		float lightDepth = 300;
		float lightHeight = -300;
		vertex(-lightWidth / 2, lightHeight, -lightDepth/2);
		vertex(-lightWidth / 2, lightHeight, lightDepth/2);
		vertex(lightWidth / 2, lightHeight, lightDepth/2);
		vertex(lightWidth / 2, lightHeight, -lightDepth/2);
		
		
		endShape();
		
	  	jr.endRecord();
	  	jr.displayRendered(true);
	}
	
	public void keyPressed() {
		if (key == 'r' || key == 'R') jr.render(); //Press 'r' key to start rendering.
	}
}