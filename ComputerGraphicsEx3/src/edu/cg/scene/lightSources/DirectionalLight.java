package edu.cg.scene.lightSources;

import java.util.List;

import edu.cg.algebra.Hit;
import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;
import edu.cg.scene.objects.Surface;

public class DirectionalLight extends Light { // Infinite distance away from the scene
	private Vec direction = new Vec(0, -1, -1);
	private Vec revDirection;

	public DirectionalLight initDirection(Vec direction) {
		this.direction = direction;
		this.revDirection = direction.mult(-1);
		return this;
	}

	@Override
	public String toString() {
		String endl = System.lineSeparator();
		return "Directional Light:" + endl + super.toString() +
				"Direction: " + direction + endl;
	}

	@Override
	public DirectionalLight initIntensity(Vec intensity) {
		return (DirectionalLight)super.initIntensity(intensity);
	}
	
	public boolean isOccludedBy(Ray rayToLight, Hit hit, List<Surface> surfaces) {

		Point pointToCheck = rayToLight.getHittingPoint(hit);
		Ray toLight = new Ray(pointToCheck, direction(pointToCheck));
		Hit gotHit = null;
		for (Surface surface : surfaces) {
			gotHit = surface.intersect(toLight);
			if ((gotHit != null)) 
				return false;
		}
		return true;
	}

	@Override
	public Ray rayToLight(Point fromPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vec intensityDiffuse(Point hittingPoint, Ray rayToLight) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vec intensitySpecular(Point hittingPoint, Ray rayToLight) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vec intensity(Point hittingPoint) {
		return this.intensity;
	}
	
	@Override
	public Vec direction(Point hittingPoint) {
		return direction.normalize().neg();
	}
}
