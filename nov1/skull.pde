import processing.video.*;
Movie fog;

PShape skull;

float ry = 0.5;
int frame = 0;

PVector planeDirection = new PVector(1, 0, 0);
PVector planeLocation = new PVector(-200, 0, 0);
float planeSpeed = 2;
PVector planeIncrement = PVector.mult(planeDirection, planeSpeed);

ArrayList<Point> points = new ArrayList<Point>();

public void setup() {
  size(1280, 720, P3D);
  fog = new Movie(this, "fog.mp4");
  fog.loop();
  skull = loadShape("skull.obj");
  
  for(int i = 0; i < skull.getChildCount(); i++ ) {
    PShape child = skull.getChild(i);
    for(int v = 0; v < child.getVertexCount(); v++) {
      PVector vertex = child.getVertex(v);
      points.add(new Point(vertex));
    }
  }
}

public void draw() {
  background(0);
  tint(255, 50);
  image(fog, 0, 0);
  
  pointLight(30, 30, 30, 900, height/2, -200);
  
  //camera(width / 2, height / 2, 1000, width / 2, height / 2, 200, 0, 1, 0);
  
  translate(width/2, height/2 + 150, -100);
  rotateZ(PI);
  rotateY(ry);
  
  shape(skull);
  
  for(Point p : points) {
    if(p.location.x >= planeLocation.x - planeSpeed && p.location.x <= planeLocation.x && !p.isActive) {
      p.start(); 
    }
    p.draw();
  }
  ry -= 0.0005;
  
  if(frame > 500) {
    planeLocation = new PVector(-200, 0, 0);
    frame = 0;
  }
  
  planeLocation = PVector.add(planeLocation, planeIncrement);
  frame++; 
  saveFrame("frames/line-####.tif");
}

void movieEvent(Movie m) {
  m.read();
}