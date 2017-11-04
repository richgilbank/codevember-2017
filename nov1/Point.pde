class Point {
  PVector location;
  boolean isActive = false;
  float timeAlive = 0;
  float lifeSpan = 100;
  
  Point(PVector location) {
    this.location = location; 
  }
  
  void start() {
    this.isActive = true; 
  }
  
  void draw() {
    if(!isActive) {
      return; 
    }
    strokeWeight((this.timeAlive / this.lifeSpan * 6));
    stroke(255, 162, 0, 255 - (this.timeAlive / this.lifeSpan * 255));
    
    point(this.location.x, this.location.y, this.location.z);
    
    if(this.timeAlive >= lifeSpan) {
      this.isActive = false;
      this.timeAlive = 0;
    }
    else {
      this.timeAlive++; 
    }
  }
}