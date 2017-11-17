import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import processing.core.*;

public class ColourThreshold {
  
	PApplet p;
	ArrayList<Float> thresholdDistances = new ArrayList<Float>();
	ArrayList<Integer> thresholdColours = new ArrayList<Integer>();
	
	int currentThreshold = 0;
	
	PVector center;
	float maxDistance;
	float distanceTravelled = 0;
	PVector prevLocation;

	ColourThreshold(PApplet parent) {
		p = parent;
		center = new PVector(0, 0);
		maxDistance = p.width / 4;
		prevLocation = new PVector(0, 0);
		
		// percentages
		thresholdDistances.add(0f);
		thresholdDistances.add(20f);
		thresholdDistances.add(30f);
		thresholdDistances.add(40f);
		thresholdDistances.add(80f);
		thresholdDistances.add(99f);
		
		// colours
		thresholdColours.add(p.color(0, 0, 0));
		thresholdColours.add(p.color(205, 127, 70));
		thresholdColours.add(p.color(220, 188, 129));
		thresholdColours.add(p.color(113, 139, 152));
		thresholdColours.add(p.color(76, 151, 206));
		thresholdColours.add(p.color(0, 0, 0));
	}

	public int update(PVector location) {
		distanceTravelled += PVector.dist(location,  prevLocation);
		float distancePercent = (distanceTravelled / maxDistance) * 100;
		
		for(int i = 0; i < thresholdDistances.size(); i++) {
			float d = thresholdDistances.get(i);
			if(distancePercent > d && i != currentThreshold) {
				currentThreshold = i;
			}
		}
		
		prevLocation = location;
		return thresholdColours.get(currentThreshold);
	}
}