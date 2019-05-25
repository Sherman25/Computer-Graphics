package edu.cg.scene.objects;

//import edu.cg.UnimplementedMethodException;
import edu.cg.algebra.Hit;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;

public class Surface implements Intersectable {
	private Shape shape;
	private Material material;
	
	public Surface(Shape shape, Material material) {
		this.shape = shape;
		this.material = material;
	}
	
	public Surface() {
		this(null, null);
	}
	
	@Override
	public String toString() {
		String endl = System.lineSeparator();
		return "Surface:" + endl +
				"Shape:" + endl + shape + endl +
				"Material: " + endl + material + endl;
	}
	
	@Override
	public Hit intersect(Ray ray) {
		Hit ans = shape.intersect(ray);
		
		if(ans != null)
			ans.setSurface(this);

		return ans;
	}
	
	public Vec Ka() {
		return material.Ka; // Ambience (to be added) (vector (r, g, b))
	}
	
	public Vec Kd() {
		return material.Kd; // Diffuse (to be multiplied with diffuse color) (vector (r, g, b))
	}
	
	public Vec Ks() {
		return material.Ks; // Specular (to be multiplied with specular color) (vector (r, g, b))
	}
	
	public int shininess() {
		return material.shininess;
	}
	
	public double refractionIndex() {
		return material.refractionIndex;
	}
	
	// Coefficients 
	public double reflectionIntensity() { // 
		return material.reflectionIntensity; // How mirror-like is our material?
	}
	
	// Coefficients 
	public double refractionIntensity() {
		return material.refractionIntensity; // How refractive is our material?
	}
	
	public boolean isTransparent() {
		return material.isTransparent;
	}
	
	public double n1(Hit hit) {
		return hit.isWithinTheSurface() ? material.refractionIndex : 1;
	}
	
	public double n2(Hit hit) {
		return hit.isWithinTheSurface() ? 1 : material.refractionIndex;
	}
}
