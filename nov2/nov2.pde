import toxi.audio.*;
import toxi.color.*;
import toxi.color.theory.*;
import toxi.data.csv.*;
import toxi.data.feeds.*;
import toxi.data.feeds.util.*;
import toxi.doap.*;
import toxi.geom.*;
import toxi.geom.mesh.*;
import toxi.geom.mesh.subdiv.*;
import toxi.geom.mesh2d.*;
import toxi.geom.nurbs.*;
import toxi.image.util.*;
import toxi.math.*;
import toxi.math.conversion.*;
import toxi.math.noise.*;
import toxi.math.waves.*;
import toxi.music.*;
import toxi.music.scale.*;
import toxi.net.*;
import toxi.newmesh.*;
import toxi.nio.*;
import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;
import toxi.physics2d.constraints.*;
import toxi.physics3d.*;
import toxi.physics3d.behaviors.*;
import toxi.physics3d.constraints.*;
import toxi.processing.*;
import toxi.sim.automata.*;
import toxi.sim.dla.*;
import toxi.sim.erosion.*;
import toxi.sim.fluids.*;
import toxi.sim.grayscott.*;
import toxi.util.*;
import toxi.util.datatypes.*;
import toxi.util.events.*;
import toxi.volume.*;

ToxiclibsSupport gfx;

int particleCount = 400;
int blobCount = 15;
Flock flock;
PImage texture;

ArrayList<Blob> blobs = new ArrayList<Blob>();

void setup() {
  size(1280, 720);
  
  gfx = new ToxiclibsSupport(this);
  texture = loadImage("tex.jpg");
  
  background(0);
  tint(255, 80);
  image(texture, 0, 0);
  for(int i = 0; i < blobCount; i++) {
    blobs.add(new Blob(random(width), random(height), gfx)); 
  }
  flock = new Flock(particleCount, blobs);
}

void draw() {
  flock.update();
  flock.draw();
}

void keyPressed() {
  if(key == 's') {
    saveFrame("frames/shot-####.png"); 
  }
}