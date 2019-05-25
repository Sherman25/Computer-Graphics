package edu.cg.scene.camera;

import edu.cg.UnimplementedMethodException;
import edu.cg.algebra.Ops;
import edu.cg.algebra.Point;
import edu.cg.algebra.Vec;

public class PinholeCamera {
	// Camera properties
	private Point cameraPosition;
	private Vec toward;
	private Vec up;
	private Vec right;
	private double d; // Distance to plain
	
	// Image properties 
	private Point imgCenter;
	private int Rx;
	private int Ry;
	private double pxlHeight;
	private double pxlWidth; 
	private double imgCenterX;
	private double imgCenterY;
	
	
	
	/**
	 * Initializes a pinhole camera model with default resolution 200X200 (RxXRy) and image width 2.
	 * @param cameraPosition - The position of the camera.
	 * @param towardsVec - The towards vector of the camera (not necessarily normalized).
	 * @param upVec - The up vector of the camera.
	 * @param distanceToPlain - The distance of the camera (position) to the center point of the image-plain.
	 * 
	 */
	public PinholeCamera(Point cameraPosition, Vec towardsVec, Vec upVec, double distanceToPlain) {
		this.cameraPosition = cameraPosition;
		this.toward = towardsVec.normalize();
		this.right = towardsVec.cross(upVec).normalize();
		this.up = right.cross(towardsVec).normalize();
		this.d = distanceToPlain;
		this.imgCenter = cameraPosition.add(Ops.mult(d, toward));
		initResolution(200, 200, 2);
	}
	
	/**
	 * Initializes the resolution and width of the image.
	 * @param height - the number of pixels in the y direction.
	 * @param width - the number of pixels in the x direction.
	 * @param viewPlainWidth - the width of the image plain in world coordinates.
	 */
	public void initResolution(int height, int width, double viewPlainWidth) {
		this.Rx = width;
		this.Ry = height;
		this.pxlWidth = viewPlainWidth / Rx;
		this.imgCenterX = Rx / 2;
		this.imgCenterY = Ry / 2;
	}

	/**
	 * Transforms from pixel coordinates to the center point of the corresponding pixel in model coordinates.
	 * @param x - the index of the x direction of the pixel.
	 * @param y - the index of the y direction of the pixel.
	 * @return the middle point of the pixel (x,y) in the model coordinates.
	 */
	public Point transform(int x, int y) {
		return imgCenter.add(right.mult((x - imgCenterX) * pxlWidth)).add(up.mult((imgCenterX - y) * pxlWidth));
	}
	
	/**
	 * Returns a copy of the camera position
	 * @return a "new" point representing the camera position.
	 */
	public Point getCameraPosition() {
		return new Point(cameraPosition.x, cameraPosition.y, cameraPosition.z);
	}
}
