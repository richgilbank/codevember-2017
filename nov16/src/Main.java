import processing.core.*;

public class Main extends PApplet {
	
	PVector center;
	ParticleController controller;
	
	int numParticles = 1000;
	
	public static void main(String[] args) {
		PApplet.main("Main");
	}
	
	public void settings() {
		smooth(8);
		size(1000, 1000);
		
		center = new PVector(width / 2, height / 2);
		controller = new ParticleController(this, numParticles);
	}
	
	public void setup() {
		background(0);	
	}
	
	public void draw() {
		controller.draw();
	}
}