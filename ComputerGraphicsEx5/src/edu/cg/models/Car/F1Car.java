package edu.cg.models.Car;

import com.jogamp.opengl.*;

import edu.cg.models.IRenderable;

/**
 * A F1 Racing Car.
 *
 */
public class F1Car implements IRenderable {

	@Override
	public void render(GL2 gl) {
		// TODO: Render the whole car. 
		//       Here You should only render the three parts: back, center and front.
		// Rendering of the front part
		gl.glPushMatrix();
		gl.glTranslated(Specification.F_BUMPER_LENGTH + Specification.F_HOOD_LENGTH_1, 0, 0);
		new Front().render(gl);
		gl.glPopMatrix();
		
		// Rendering of the center
		new Center().render(gl);
		
		// Rendering of the back part
		gl.glPushMatrix();
		gl.glTranslated(-Specification.B_BASE_LENGTH, 0, 0);
		new Back().render(gl);
		gl.glPopMatrix();
		
		
	}

	@Override
	public String toString() {
		return "F1Car";
	}

	@Override
	public void init(GL2 gl) {

	}
}
