package edu.cg.scene.lightSources;

import java.util.List;

import edu.cg.algebra.Hit;
import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;
import edu.cg.scene.objects.Surface;

public class Spotlight extends PointLight {
	private Vec direction;
	
	public Spotlight initDirection(Vec direction) {
		this.direction = direction;
		return this;
	}
	
	@Override
	public String toString() {
		String endl = System.lineSeparator();
		return "Spotlight: " + endl +
				description() + 
				"Direction: " + direction + endl;
	}
	
	@Override
	public Spotlight initPosition(Point position) {
		return (Spotlight)super.initPosition(position);
	}
	
	@Override
	public Spotlight initIntensity(Vec intensity) {
		return (Spotlight)super.initIntensity(intensity);
	}
	
	@Override
	public Spotlight initDecayFactors(double q, double l, double c) {
		return (Spotlight)super.initDecayFactors(q, l, c);
	}
	
	public boolean isOccludedBy(Ray rayToLight, Hit hit, List<Surface> surfaces) {

		Point point = rayToLight.getHittingPoint(hit);
		Ray toLight = new Ray(point, position);
		Hit gotHit = null;
		for (Surface surface : surfaces) {
			gotHit = surface.intersect(toLight);
			if ((gotHit != null) && gotHit.t() <= point.sub(position).length()) 
				return false;
		}
		return true;
	}
}
