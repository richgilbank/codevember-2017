int vertices = 30;

class Blob {
  PVector origin;
  float radius;
  Polygon2D poly;
  
  Blob(float x, float y, ToxiclibsSupport gfx) {
    noFill();
    noStroke();
    origin = new PVector(x, y);
    radius = random(20, 100);
    poly = new Polygon2D();
    
    for(int i = 0; i < vertices; i++) {
      Vec2D v = Vec2D.fromTheta(float(i) / vertices * TWO_PI);
      v.scaleSelf(random(0.2, 1) * radius);
      v.addSelf(origin.x, origin.y);
      poly.add(v);
    }
    
    poly.smooth(0.5, 0.25);
    beginShape();
    for(Vec2D vertex : poly.vertices) {
      vertex(vertex.x, vertex.y);
    }
    endShape(CLOSE);
    
    gfx.polygon2D(poly);
  }
}


//float minShapeSize = 30;
//float maxShapeSize = 80;
//float minShapeChaos = 5;
//float maxShapeChaos = 5;

//class Blob {
//  PVector origin;
//  PShape shape;
//  float xoff = 0;
//  float yoff = 0;
  
//  ArrayList<PVector> vertices;
  
//  Blob(float x, float y) {
//   origin = new PVector(x, y);
//   noiseSeed(round(random(20)));
   
//   vertices = new ArrayList<PVector>();
//   for(float a = 0; a < TWO_PI; a+= 0.2) {
//     PVector v = PVector.fromAngle(a);
//     v.mult(random(60, 80));
     
//     float angle = TWO_PI * noise(xoff, yoff);
//     PVector r = PVector.fromAngle(angle);
//     r.mult(5);
//     r.add(v);
     
//     vertices.add(r);
//     xoff += 0.5;
//   }
//   shape = createShape();
//   shape.beginShape();
//   shape.fill(50);
//   shape.noStroke();
//   for(PVector v : vertices) {
//     shape.vertex(v.x, v.y); 
//   }
//   shape.endShape(CLOSE);
      
//   pushMatrix();
//   translate(origin.x, origin.y);
//   shape(shape);
//   popMatrix();
//  }
//}