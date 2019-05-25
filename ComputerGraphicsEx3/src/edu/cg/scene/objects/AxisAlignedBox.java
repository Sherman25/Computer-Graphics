package edu.cg.scene.objects;

import edu.cg.UnimplementedMethodException;
import edu.cg.algebra.Hit;
import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;

public class AxisAlignedBox extends Shape {
	private Point minPoint;
	private Point maxPoint;
	private String name = "";
	static private int CURR_IDX;

	/**
	 * Creates an axis aligned box with a specified minPoint and maxPoint.
	 */
	public AxisAlignedBox(Point minPoint, Point maxPoint) {
		this.minPoint = minPoint;
		this.maxPoint = maxPoint;
		name = new String("Box " + CURR_IDX);
		CURR_IDX += 1;
		fixBoundryPoints();
	}

	/**
	 * Creates a default axis aligned box with a specified minPoint and maxPoint.
	 */
	public AxisAlignedBox() {
		minPoint = new Point(-1.0, -1.0, -1.0);
		maxPoint = new Point(1.0, 1.0, 1.0);
	}
	
	/**
	 * This methods fixes the boundary points minPoint and maxPoint so that the values are consistent.
	 */
	private void fixBoundryPoints() {
		double min_x = Math.min(minPoint.x, maxPoint.x), max_x = Math.max(minPoint.x, maxPoint.x),
				min_y = Math.min(minPoint.y, maxPoint.y), max_y = Math.max(minPoint.y, maxPoint.y),
				min_z = Math.min(minPoint.z, maxPoint.z), max_z = Math.max(minPoint.z, maxPoint.z);
		minPoint = new Point(min_x, min_y, min_z);
		maxPoint = new Point(max_x, max_y, max_z);
	}
	
	@Override
	public String toString() {
		String endl = System.lineSeparator();
		return name + endl + "Min Point: " + minPoint + endl + "Max Point: " + maxPoint + endl;
	}
	
	public AxisAlignedBox initMinPoint(Point minPoint) {
		this.minPoint = minPoint;
		fixBoundryPoints();
		return this;
	}

	public AxisAlignedBox initMaxPoint(Point maxPoint) {
		this.maxPoint = maxPoint;
		fixBoundryPoints();
		return this;
	}

	@Override
	public Hit intersect(Ray ray) {
		
		Point closeCorner = minPoint.add(new Vec());
		
		boolean flippedX = ray.direction().x < 0;
		boolean flippedY = ray.direction().y < 0;
		boolean flippedZ = ray.direction().z < 0;

		boolean rayIn = isIn(convergence(ray.source()));
		if (rayIn) { 
			flippedX = !flippedX;
			flippedY = !flippedY;
			flippedZ = !flippedZ;
		}

		if (flippedX) closeCorner.x = maxPoint.x;
		if (flippedY) closeCorner.y = maxPoint.y;
		if (flippedZ) closeCorner.z = maxPoint.z;

		if (rayIn) {
			flippedX = !flippedX;
			flippedY = !flippedY;
			flippedZ = !flippedZ;
		}

		double tValue = 0;
		
		// Checking the plane the Ray will intersect: XY, XZ, YZ;
		tValue = zDirection(ray, closeCorner.z);
		if (tValue >= 0) {
			Point XY = ray.add(tValue);
			Point corrected = convergence(XY);
			if (minPoint.x <= corrected.x & corrected.x <= maxPoint.x & minPoint.y <= corrected.y & corrected.y <= maxPoint.y) {
				return new Hit(tValue,	new Vec(0, 0, flippedZ ? 1 : -1), XY);
			}
		}
				
		tValue = xDirection(ray, closeCorner.x);
		if (tValue >= 0) {
			Point YZ = ray.add(tValue);
			Point corrected = convergence(YZ);
			if (minPoint.y <= corrected.y & corrected.y <= maxPoint.y & minPoint.z <= corrected.z & corrected.z <= maxPoint.z) {
				return new Hit(tValue,	new Vec(flippedX ? 1 : -1, 0, 0), YZ);
			}
		}
				
		tValue = yDirection(ray, closeCorner.y);
		if (tValue >= 0) {
			Point XZ = ray.add(tValue);
			Point corrected = convergence(XZ);
			if (minPoint.x <= corrected.x & corrected.x <= maxPoint.x & minPoint.z <= corrected.z & corrected.z <= maxPoint.z) {
				return new Hit(tValue,	new Vec(0, flippedY ? 1 : -1, 0), XZ);
			}
		}

		return null;
	}
	
	// Cutting the tail of the double value by casting to float and returning back 
	// the double. In case of too close coordinates we can assume that there is intersection. 
	public static double convergence(double value) {
		return (double)((float)value);
	}

	public static Vec convergence(Vec vec) {
		return new Vec(convergence(vec.x), convergence(vec.y), convergence(vec.z));
	}

	public static Point convergence(Point pnt) {
		return new Point(convergence(pnt.x), convergence(pnt.y), convergence(pnt.z));
	}
	
	private boolean isIn(Point point) {
		return (minPoint.x < point.x & point.x < maxPoint.x &
				minPoint.y < point.y & point.y < maxPoint.y &
				minPoint.z < point.z & point.z < maxPoint.z);
	}
	
	
	public Double zDirection(Ray ray, double z) {
		if (ray.direction().z == 0) {
			return (ray.source().z == z) ? 0d : -1d;
		}
		return ((z - ray.source().z) / ray.direction().z);
	}

	public Double xDirection(Ray ray, double x) {
		if (ray.direction().x == 0) {
			return (ray.source().x == x) ? 0d : -1d;
		}
		return ((x - ray.source().x) / ray.direction().x);
	}

	public Double yDirection(Ray ray, double y) {
		if (ray.direction().y == 0) {
			return (ray.source().y == y) ? 0d : -1d;
		}
		return ((y - ray.source().y) / ray.direction().y);
	}

	
}
