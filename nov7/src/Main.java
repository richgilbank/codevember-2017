import java.util.List;

import joons.JoonsRenderer;
import peasy.PeasyCam;
import processing.core.*;
import toxi.processing.ToxiclibsSupport;
import toxi.geom.*;
import toxi.geom.mesh2d.Voronoi;

public class Main extends PApplet {
	JoonsRenderer jr;
	
	PeasyCam cam;
	ToxiclibsSupport gfx;
	
	Voronoi voronoi;
	SutherlandHodgemanClipper voronoiClip;

	//Camera Setting.
	float eyeX = 0;
	float eyeY = -70;
	float eyeZ = 10;
	float centerX = 0;
	float centerY = -50;
	float centerZ = -1;
	float upX = 0;
	float upY = -1;
	float upZ = 0;
	float fov = PI / 4; 
	float aspect = 9/16f;  
	float zNear = 5;
	float zFar = 10000;
	
	float wallWidth = 90;
	float wallHeight = 150;
	
	int numFragments = 500;
	
	List<Vec2D> sites;
	
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
		jr.setAA(-1, 1, 6);
		jr.setCaustics(10, 150, 0.2f); // emitInMillions, gather, radius
		jr.setTraceDepths(2, 4, 4); // diffusive, reflective, refractive
		jr.setDOF(30, 0.5f);
		
		cam = new PeasyCam(this, centerX, centerY, centerZ, eyeZ);
		cam.setMinimumDistance(50);
		cam.setMaximumDistance(500);
		
		gfx = new ToxiclibsSupport(this);
		voronoiClip = new SutherlandHodgemanClipper(new Rect(0, 0, wallWidth, wallHeight));
		voronoi = new Voronoi(); 
		for ( int i = 0; i < numFragments; i++ ) {
			voronoi.addPoint( new Vec2D( random(wallWidth), random(wallHeight) ) );
	    }
		sites = voronoi.getSites();
		renderScene();
	}
	
	public void renderScene() {
		jr.render();
		jr.beginRecord();
		
		camera(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
		perspective(fov, aspect, zNear, zFar);
		
		jr.background(0);
		jr.background("gi_ambient_occlusion");
		
		drawLight();
		drawWall();
		drawVoronoi();
		
		jr.endRecord();
		jr.displayRendered(true);
		saveFrame("frames/" + hour() + minute() + second() + ".png");
	}
	
	public int getIndexPlus(int current, int total, int add) {
		if((current + add) % total == 0) {
			return total;
		}
		return (current + add) % total;
	}
	
	public void drawVoronoi() {
		float contract = 0.8f;
		float height = 0.5f;
		
		pushMatrix();
		translate(-1 * wallWidth / 2, -1 * wallHeight / 2, 0);
		List<Polygon2D> regions = voronoi.getRegions();
		for (int polyIndex = 0; polyIndex < regions.size(); polyIndex++) {
			Polygon2D poly1 = voronoiClip.clipPolygon(regions.get(polyIndex));
			
			Polygon2D poly2 = new Polygon2D();
			Vec2D center = poly1.getCentroid();
			for(Vec2D v : poly1.vertices) {
				v.x = lerp(center.x, v.x, contract);
				v.y = lerp(center.y, v.y, contract);
				poly2.add(new Vec2D(v.x, v.y));
			}
			
			jr.fill("shiny", 20, 20, 20, 3f);
			for(int i = 0; i < poly1.getNumVertices(); i++) {
				List<Vec2D> verts = poly1.vertices;
				Vec2D current = verts.get(i);
				Vec2D prev = (i == 0 ? verts.get(poly1.getNumVertices() - 1) : verts.get(i - 1));
				
				// sunflow only allows triangles or quads :(
				Vec2D next = verts.get(getIndexPlus(i, verts.size() - 1, 1));
				Vec2D next2 = verts.get(getIndexPlus(i, verts.size() - 1, 2));
				Vec2D next3 = verts.get(getIndexPlus(i, verts.size() - 1, 3));
				Vec2D next4 = verts.get(getIndexPlus(i, verts.size() - 1, 4));
				beginShape(TRIANGLES);
				vertex(current.x, current.y, height);
				vertex(prev.x, prev.y, height);
				vertex(next.x, next.y, height);
				vertex(current.x, current.y, height);
				vertex(next.x, next.y, height);
				vertex(next2.x, next2.y, height);
				vertex(current.x, current.y, height);
				vertex(next2.x, next2.y, height);
				vertex(next3.x, next3.y, height);
				vertex(current.x, current.y, height);
				vertex(next3.x, next3.y, height);
				vertex(next4.x, next4.y, height);
				endShape();

				// sides
				beginShape(QUADS);
				vertex(prev.x, prev.y, height);
				vertex(current.x, current.y, height);
				vertex(current.x, current.y, 0);
				vertex(prev.x, prev.y, 0);
				endShape(CLOSE);
			}
		}
		popMatrix();
	}
		
	public void drawLight() {
		jr.fill("light", 1, 1, 1);
		
		beginShape(QUADS);
		vertex(-30, 20, 30);
		vertex(30, 20, 30);
		vertex(30, -20, 30);
		vertex(-30, -20, 30);
		endShape(CLOSE);
	}
	
	public void drawWall() {
		float sideWallDepth = 80;
		
		jr.fill("ambient_occlusion", 255, 255, 255, 150, 150, 150, 50, 16);
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
		if (key == 'r' || key == 'R') jr.render(); //Press 'r' key to start rendering.
	}
	
	public void draw() {
		//renderScene();
	}
}