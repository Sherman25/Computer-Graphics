package edu.cg.models.Car;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import edu.cg.models.IRenderable;

public class PairOfWheels implements IRenderable {
	// TODO: Use the wheel field to render the two wheels.
	private final Wheel wheel = new Wheel();
	
	@Override
	public void render(GL2 gl) {
		GLU glu = new GLU();
		GLUquadric quad = glu.gluNewQuadric();
		gl.glPushMatrix();
		Materials.SetDarkGreyMetalMaterial(gl);
		gl.glTranslated(0, 0, -Specification.PAIR_OF_WHEELS_ROD_DEPTH / 2);
		glu.gluCylinder(quad, Specification.PAIR_OF_WHEELS_ROD_RADIUS, Specification.PAIR_OF_WHEELS_ROD_RADIUS, 
												Specification.PAIR_OF_WHEELS_ROD_DEPTH, 20, 2);
		gl.glPushMatrix();
		gl.glTranslated(0, 0, Specification.PAIR_OF_WHEELS_ROD_DEPTH + Specification.TIRE_DEPTH);
		wheel.render(gl);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslated(0, 0, -(Specification.PAIR_OF_WHEELS_ROD_DEPTH));
		gl.glRotated(180, 0, 1, 0);
		wheel.render(gl);
		gl.glPopMatrix();
		
		glu.gluDeleteQuadric(quad);
	}

	@Override
	public void init(GL2 gl) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString() {
		return "PairOfWheels";
	}

}
