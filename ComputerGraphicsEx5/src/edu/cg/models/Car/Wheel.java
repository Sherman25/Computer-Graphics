package edu.cg.models.Car;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import edu.cg.models.IRenderable;

public class Wheel implements IRenderable {

	@Override
	public void render(GL2 gl) {
		// We will draw a wheel by using cylinder and disk
		GLU glu = new GLU();
		Materials.setMaterialRims(gl);
		GLUquadric quadric = glu.gluNewQuadric();
		gl.glPushMatrix();
		gl.glTranslated(0, 0, -(Specification.TIRE_DEPTH / 2));
		gl.glTranslated(0, 0, -(Specification.TIRE_DEPTH / 2));
		
		glu.gluCylinder(quadric, Specification.TIRE_RADIUS, Specification.TIRE_RADIUS, Specification.TIRE_DEPTH, 20, 2);
		gl.glRotated(180, 1, 0, 0);
        glu.gluDisk(quadric, Specification.TIRE_RADIUS - 0.02, Specification.TIRE_RADIUS, 20, 2); 
        glu.gluDisk(quadric, 0, Specification.TIRE_RADIUS - 0.02, 20, 2);
        gl.glRotated(180, 1, 0, 0);
		gl.glTranslated(0, 0, Specification.TIRE_DEPTH);
		Materials.setMaterialTire(gl);
        glu.gluDisk(quadric, Specification.TIRE_RADIUS - 0.02, Specification.TIRE_RADIUS, 20, 2);
		Materials.setMaterialRims(gl);
        glu.gluDisk(quadric, 0, Specification.TIRE_RADIUS - 0.02, 20, 2);
		gl.glPopMatrix();
		glu.gluDeleteQuadric(quadric);
	}

	@Override
	public void init(GL2 gl) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString() {
		return "Wheel";
	}

}
