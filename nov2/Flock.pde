class Flock {
  ArrayList<Particle> particles = new ArrayList<Particle>();
  ArrayList<Blob> blobs;
  
  Flock(int particleCount, ArrayList b) {
    blobs = b;
    for(int i = 0; i < particleCount; i++) {
      particles.add(new Particle(random(width), 0));
    }
  }
  
  void update() {
    for(Particle p : particles) {
      p.update();
      for(Blob b : blobs) {
        if(b.poly.containsPoint(new Vec2D(p.position.x, p.position.y))) {
          p.isAlive = false;
        }
      }
    }
  }
  
  void draw() {
    for(Particle p : particles) {
      p.draw(); 
    }
  }
}