import processing.core.PApplet;
import processing.core.PVector;

public class Particle {
	PApplet p;
  
  	PVector prevLocation;
  	PVector location;
  	PVector direction;
  
  	ColourThreshold color;
  
  	float noiseOffset;

  	Particle(PApplet parent) {
	  	p = parent;
	  	location = new PVector(0, 0);
	  	prevLocation = location;
	  	direction = PVector.random2D();
	  	color = new ColourThreshold(p);
  		noiseOffset = p.random(10f);
  	}
  	
  	public void setInitialLocation(PVector loc) {
  		location = loc;
  		prevLocation = loc;
  	}
  	
  	public void draw(int tick) {
  		p.pushMatrix();
  		p.translate(p.width / 2,  p.height / 2);
  		p.rotate(p.atan2(direction.y, direction.x));
	  	location.x = location.x + 1;
	  
	  	// add wiggle
	  	float noiseMultiplier = 5f;
  		PVector wiggle = new PVector(0, (p.noise(tick * 0.02f) - 0.5f) * noiseMultiplier);
	  	location = PVector.add(location, wiggle);
	  
	  	int strokeColour = color.update(location);
	  	p.stroke(strokeColour, 50);
	  	p.line(prevLocation.x, prevLocation.y, location.x, location.y);
	  
	  	prevLocation = location;
	  	p.popMatrix();
  	}
  
}