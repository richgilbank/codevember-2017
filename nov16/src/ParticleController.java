import java.util.ArrayList;

import processing.core.*;

public class ParticleController {
	PApplet p;
	int numParticles;
	int ticker;
  
	ArrayList<Particle> particles;

	ParticleController(PApplet parent, int num) {
		p = parent;
		numParticles = num;
		ticker = 0;
    
		particles = new ArrayList<Particle>();
		for(int i = 0; i < numParticles; i++) {
			particles.add(new Particle(p));
		}
	}

	public void draw() {
		ArrayList<Particle> particlesToAdd = new ArrayList<Particle>();
		for(Particle particle : particles) {
			if(p.random(1f) > 0.993f) {
				Particle newParticle = new Particle(p);
				newParticle.setInitialLocation(particle.location);
				newParticle.direction = particle.direction;
				particlesToAdd.add(newParticle);
			}
			particle.draw(ticker++);
		}
		for(Particle newParticle : particlesToAdd) {
			particles.add(newParticle);
		}
	}
}