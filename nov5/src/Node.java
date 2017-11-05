import joons.JoonsRenderer;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class Node {
	PApplet p;
	JoonsRenderer jr;
	
	PVector center;
	
	float minSize = 0.08f;
	float maxSize = 0.7f;
	
	float wallWidth;
	float wallHeight;
	
	float zRotation;
	float size;
	
	boolean isBox;
	
	float perlinOffset;
	
	Node(PApplet parent, JoonsRenderer j, float w, float h) {
		jr = j;
		p = parent;
		wallWidth = w;
		wallHeight = h;
		
		size = p.random(minSize, maxSize);
		float minX = -1 * wallWidth / 2 + size / 2;
		float maxX = wallWidth / 2 - size / 2;
		float minY = -1 * wallHeight / 2 + size / 2;
		float maxY = wallHeight / 2 - size / 2;
		center = new PVector(p.random(minX, maxX), p.random(minY, maxY), 0);
		zRotation = p.random(-1 * (float)Math.PI, (float)Math.PI);
		perlinOffset = p.noise((center.x + wallWidth) * 0.1f, (center.y + wallHeight) * 0.1f);
		size *= perlinOffset * 2;
		
		isBox = p.random(1) < 0.9 ? true : false;
	}
	
	public void draw() {
		p.pushMatrix();
		p.translate(center.x, center.y, center.z);
		p.rotateY((float)Math.PI/4);
		p.rotateX((float)Math.PI/4);
		p.rotateZ(zRotation);
		//p.translate(0,  0, perlinOffset * 10);
		jr.fill("shiny", 20, 20, 20, 3f);
		
		if(isBox) {
			p.box(size);
		}
		else {
			p.sphereDetail(PApplet.floor(PApplet.map(size, minSize, maxSize, 3, 5)));
			p.sphere(size);	
		}
		
		p.popMatrix();
	}
}