import joons.JoonsRenderer;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class Shard {
	PApplet p;
	JoonsRenderer jr;
	PVector center;
	PVector point;
	
	float maxRotation = (float)Math.PI / 3;
	float minSize = 2;
	float maxSize = 15;
	float thickness = 0.5f;
	
	float wallWidth;
	float wallHeight;
	
	float rotation;
	float size;
	
	PVector upperTiePoint1;
	PVector upperTiePoint2;
	
	Shard(PApplet parent, JoonsRenderer j, float w, float h) {
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
		rotation = p.random(-1 * maxRotation / 2, maxRotation / 2);
		
		float pointX = p.random(-1 * (size / 3) * 4, p.random(size / 3 * 4));
		float pointZ = p.random(size / 3, 2 * size);
		point = new PVector(pointX, p.random(-5, 5), pointZ);
	}
	
	public void draw() {
		p.pushMatrix();
		p.translate(center.x, center.y, center.z);
		p.rotateZ(rotation);
		jr.fill("glass", 255, 255, 255, 2.0f, 5.0f, 255, 255, 255);
		
		p.beginShape(p.TRIANGLES);
		// left side
		p.vertex(-1 * size / 2, 0, 0); // top back left
		p.vertex(-1 * size / 2, thickness, 0); // bottom back left
		p.vertex(point.x, point.y, point.z); // top point
		
		p.vertex(point.x, point.y, point.z); // top point
		p.vertex(point.x, point.y + thickness, point.z); // bottom point
		p.vertex(-1 * size / 2, thickness, 0); // bottom back left
		
		// right side
		p.vertex(point.x, point.y, point.z); // top point
		p.vertex(size / 2, 0, 0); // top back right
		p.vertex(size / 2, thickness, 0); // bottom back right
		
		p.vertex(size / 2, thickness, 0); // bottom back right
		p.vertex(point.x, point.y + thickness, point.z); // bottom point
		p.vertex(point.x, point.y, point.z); // top point
		
		// top side
		p.vertex(-1 * size / 2, 0, 0); // top back left
		p.vertex(point.x, point.y, point.z); // top point
		p.vertex(size / 2, 0, 0); // top back right
		
		// bottom side
		p.vertex(size / 2, thickness, 0); // bottom back right
		p.vertex(point.x, point.y + thickness, point.z); // bottom point
		p.vertex(-1 * size / 2, thickness, 0); // bottom back left
		p.endShape();
		
		p.popMatrix();
	}
}