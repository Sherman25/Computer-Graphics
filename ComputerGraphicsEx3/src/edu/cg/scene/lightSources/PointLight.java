package edu.cg.scene.lightSources;

import java.util.List;

import edu.cg.algebra.Hit;
import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;
import edu.cg.scene.objects.Surface;

public class PointLight extends Light {
	protected Point position;
	
	//Decay factors:
	protected double kq = 0.01;
	protected double kl = 0.1;
	protected double kc = 1;
	
	protected String description() {
		String endl = System.lineSeparator();
		return "Intensity: " + intensity + endl +
				"Position: " + position + endl +
				"Decay factors: kq = " + kq + ", kl = " + kl + ", kc = " + kc + endl;
	}
	
	@Override
	public String toString() {
		String endl = System.lineSeparator();
		return "Point Light:" + endl + description();
	}
	
	@Override
	public PointLight initIntensity(Vec intensity) {
		return (PointLight)super.initIntensity(intensity);
	}
	
	public PointLight initPosition(Point position) {
		this.position = position;
		return this;
	}
	
	public PointLight initDecayFactors(double kq, double kl, double kc) {
		this.kq = kq; // Quadratic decay
		this.kl = kl; // Linear decay
		this.kc = kc; // Constant decay
		return this;
	}
	
	public Vec intensityDiffuse(Point hittingPoint, Ray rayToLight) {
		// ONLY FOR DIFFUSE LIGHT, not specular
		// eventualAttenuation(dist) = kq * dist^2 + kl * dist + kc
		// eventual DIFFUSE light intensity = calcDiffuseIntensity(some arguments).mult(1 / eventualAttenuation(dist))
		return null;
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

	@Override
	public Ray rayToLight(Point fromPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vec intensitySpecular(Point hittingPoint, Ray rayToLight) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vec direction(Point hittingPoint) {
		return position.sub(hittingPoint).normalize();
	}

	@Override
	public Vec intensity(Point hittingPoint) {
		double d  = hittingPoint.dist(position);
		return intensity.mult(1 / (kc + (kl + kq*d)*d));
	}



	//TODO: add some methods
}
