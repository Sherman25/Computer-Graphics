package edu.cg.scene;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.cg.Logger;
//import edu.cg.UnimplementedMethodException;
import edu.cg.algebra.Hit;
import edu.cg.algebra.Ops;
import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;
import edu.cg.scene.camera.PinholeCamera;
import edu.cg.scene.lightSources.Light;
import edu.cg.scene.objects.Surface;

public class Scene {
	private String name = "scene";
	private int maxRecursionLevel = 1;
	private int antiAliasingFactor = 1; //gets the values of 1, 2 and 3
	private boolean renderRefractions = false;
	private boolean renderReflections = false;
	
	private PinholeCamera camera;
	private Vec ambient = new Vec(1, 1, 1); //white
	private Vec backgroundColor = new Vec(0, 0.5, 1); //blue sky
	private List<Light> lightSources = new LinkedList<>();
	private List<Surface> surfaces = new LinkedList<>();
	
	
	//MARK: initializers
	public Scene initCamera(Point eyePoistion, Vec towardsVec, Vec upVec,  double distanceToPlain) {
		this.camera = new PinholeCamera(eyePoistion, towardsVec, upVec,  distanceToPlain);
		return this;
	}
	
	public Scene initAmbient(Vec ambient) {
		this.ambient = ambient;
		return this;
	}
	
	public Scene initBackgroundColor(Vec backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}
	
	public Scene addLightSource(Light lightSource) {
		lightSources.add(lightSource);
		return this;
	}
	
	public Scene addSurface(Surface surface) {
		surfaces.add(surface);
		return this;
	}
	
	public Scene initMaxRecursionLevel(int maxRecursionLevel) {
		this.maxRecursionLevel = maxRecursionLevel;
		return this;
	}
	
	public Scene initAntiAliasingFactor(int antiAliasingFactor) {
		this.antiAliasingFactor = antiAliasingFactor;
		return this;
	}
	
	public Scene initName(String name) {
		this.name = name;
		return this;
	}
	
	public Scene initRenderRefarctions(boolean renderRefractions) {
		this.renderRefractions = renderRefractions;
		return this;
	}
	
	public Scene initRenderReflections(boolean renderReflections) {
		this.renderReflections = renderReflections;
		return this;
	}
	
	//MARK: getters
	public String getName() {
		return name;
	}
	
	public int getFactor() {
		return antiAliasingFactor;
	}
	
	public int getMaxRecursionLevel() {
		return maxRecursionLevel;
	}
	
	public boolean getRenderRefarctions() {
		return renderRefractions;
	}
	
	public boolean getRenderReflections() {
		return renderReflections;
	}
	
	@Override
	public String toString() {
		String endl = System.lineSeparator(); 
		return "Camera: " + camera + endl +
				"Ambient: " + ambient + endl +
				"Background Color: " + backgroundColor + endl +
				"Max recursion level: " + maxRecursionLevel + endl +
				"Anti aliasing factor: " + antiAliasingFactor + endl +
				"Light sources:" + endl + lightSources + endl +
				"Surfaces:" + endl + surfaces;
	}
	
	private transient ExecutorService executor = null;
	private transient Logger logger = null;
	
	private void initSomeFields(int imgWidth, int imgHeight, Logger logger) {
		this.logger = logger;
		//TODO: initialize your additional field here.
		//      You can also change the method signature if needed.
	}
	
	
	public BufferedImage render(int imgWidth, int imgHeight, double viewPlainWidth,Logger logger)
			throws InterruptedException, ExecutionException {
		// TODO: Please notice the following comment.
		// This method is invoked each time Render Scene button is invoked.
		// Use it to initialize additional fields you need.
		initSomeFields(imgWidth, imgHeight, logger);
		
		BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		camera.initResolution(imgHeight, imgWidth, viewPlainWidth);
		int nThreads = Runtime.getRuntime().availableProcessors();
		nThreads = nThreads < 2 ? 2 : nThreads;
		this.logger.log("Intitialize executor. Using " + nThreads + " threads to render " + name);
		executor = Executors.newFixedThreadPool(nThreads);
		
		@SuppressWarnings("unchecked")
		Future<Color>[][] futures = (Future<Color>[][])(new Future[imgHeight][imgWidth]);
		
		this.logger.log("Starting to shoot " +
			(imgHeight*imgWidth*antiAliasingFactor*antiAliasingFactor) +
			" rays over " + name);
		
		for(int y = 0; y < imgHeight; ++y)
			for(int x = 0; x < imgWidth; ++x)
				futures[y][x] = calcColor(x, y);
		
		this.logger.log("Done shooting rays.");
		this.logger.log("Wating for results...");
		
		for(int y = 0; y < imgHeight; ++y)
			for(int x = 0; x < imgWidth; ++x) {
				Color color = futures[y][x].get();
				img.setRGB(x, y, color.getRGB());
			}
		
		executor.shutdown();
		
		this.logger.log("Ray tracing of " + name + " has been completed.");
		
		executor = null;
		this.logger = null;
		
		return img;
	}
	
	private Future<Color> calcColor(int x, int y) {
		return executor.submit(() -> {
			// TODO: You need to re-implement this method if you want to handle
			//       super-sampling. You're also free to change the given implementation as you like.
			Point pixelPositionInSpace = camera.transform(x, y);
			
			Ray ray = new Ray(camera.getCameraPosition(), pixelPositionInSpace);
			Vec color = calcColor(ray, 0);
			return color.toColor();
		});
	}
	
	/** Calculate ambience, calculate specular, calculate diffuse
	 	For every light, calculate
	 	You'll need the hit position
	 	s = occlusion factor, can be 1 or 0... only calculate if s is 1
	 	calculate & sum diffuse illumination,
	 	calculate & sum specular illumination
	 	return ambience + diffuse sum + specular sum
	 	Or more specifically: return hit.getSurface().Ka() + diffuseSum * hit.getSurface().Kd() + specularSum * hit.getSurface().Ks()	
	*/ 
	private Vec calcPhongReflection(Ray ray, Hit hit) {
		
		Surface curSurface = hit.getSurface();
		Vec Ka = curSurface.Ka();
		Vec Kd = curSurface.Kd();
		Vec Ks = curSurface.Ks();
		Vec intensitySum = Ka.mult(ambient);
		Vec diffuseSum = new Vec();
		Vec specularSum = new Vec();
		
		for(Light light : lightSources) {
			if(!light.isOccludedBy(ray, hit, surfaces)) {
				Vec Id = Kd.mult(calcDiffuseColor(hit, light, ray)); 
				diffuseSum = diffuseSum.add(Id); 
				
				Vec Is = Ks.mult(calcSpecularColor(hit, light, ray));
				specularSum = specularSum.add(Is);
			}
		}
		intensitySum = intensitySum.add(diffuseSum.add(specularSum));
		
		return intensitySum;
	}
	
	private Vec calcDiffuseColor(Hit hit, Light light, Ray ray) {
		Vec I_d = new Vec();
		Point hittingPoint = ray.getHittingPoint(hit);
		Vec l = light.direction(hittingPoint);
		Vec I_l = light.intensity(hittingPoint);
		Vec n = hit.getNormalToSurface();
		I_d = I_l.mult(n.dot(l));
		return I_d;
	}

	private Vec calcSpecularColor(Hit hit, Light light, Ray ray) {
		Vec I_s = new Vec();
		Point hittingPoint = ray.getHittingPoint(hit);
		Vec I_l = light.intensity(hittingPoint);
		Vec v = ray.direction();
		Vec r  = Ops.reflect(light.direction(hittingPoint), hit.getNormalToSurface()).normalize();
		int m = hit.getSurface().shininess();
		double ang = (r.dot(v));	
		if (ang < 0) {
			return new Vec(0);
		}
		I_s = I_l.mult(Math.pow(ang, m));
		return I_s;
	}
	
	
	
	private Ray calcRefractionRay(Ray ray, Hit hit) {
		Ray res = new Ray(hit.getIn(), Ops.refract(ray.direction(), hit.getNormalToSurface(),
								hit.getSurface().n1(hit),hit.getSurface().n2(hit)));
		return res;
				
	}
	
	private Ray calcReflectionRay(Ray ray, Hit hit) {
		Ray res = new Ray(hit.getOut(), Ops.reflect(ray.direction(), hit.getNormalToSurface()));
		return res;
	}
	
	public Hit getRayMinimalHit(Ray ray) {
		Hit minimalHit = null;
		for (Surface surface : surfaces) {
			Hit newHit = surface.intersect(ray);
			if (newHit == null) continue;
			if (minimalHit != null) {
				if (newHit.compareTo(minimalHit) < 0) minimalHit = newHit;
			} else {
				minimalHit = newHit;
			}
		}
		return minimalHit;
	}
	
	private Vec calcColor(Ray ray, int recursionLevel) {
		if(recursionLevel >= maxRecursionLevel) {
			return new Vec();
		}
		
		Vec resColor = new Vec(backgroundColor);
		Hit minHit = getRayMinimalHit(ray);
		
		if(minHit != null) {
			resColor = calcPhongReflection(ray, minHit);
		}
		
		if(renderReflections && minHit != null) {				
			resColor = resColor.add(calcColor(calcReflectionRay(ray, minHit), recursionLevel + 1));
		}
		
		if(renderRefractions && minHit != null) {				
			resColor = resColor.add(calcColor(calcRefractionRay(ray, minHit), recursionLevel + 1));
		}
		

		return resColor;
	}
}
