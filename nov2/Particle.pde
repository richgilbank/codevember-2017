PVector gravity = new PVector(0.05, 1.0, 0);

class Particle {
  PVector position;
  float friction;
  color strokeColor;
  boolean isAlive = true;
 
  Particle(float x, float y) {
    position = new PVector(x, y);
    friction = random(1.5);
    strokeColor = color(255, random(50, 100));
  }
  
  void update() {
    PVector velocity = PVector.mult(gravity, friction);
    position = PVector.add(position, velocity);
  }
  
  void draw() {
    if(!isAlive) {
      return; 
    }
    strokeWeight(1);
    stroke(strokeColor);
    point(position.x, position.y);
  }
}