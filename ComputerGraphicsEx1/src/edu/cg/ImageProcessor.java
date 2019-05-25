package edu.cg;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageProcessor extends FunctioalForEachLoops {

	// MARK: fields
	public final Logger logger;
	public final BufferedImage workingImage;
	public final RGBWeights rgbWeights;
	public final int inWidth;
	public final int inHeight;
	public final int workingImageType;
	public final int outWidth;
	public final int outHeight;

	// MARK: constructors
	public ImageProcessor(Logger logger, BufferedImage workingImage, RGBWeights rgbWeights, int outWidth,
			int outHeight) {
		super(); // initializing for each loops...

		this.logger = logger;
		this.workingImage = workingImage;
		this.rgbWeights = rgbWeights;
		inWidth = workingImage.getWidth();
		inHeight = workingImage.getHeight();
		workingImageType = workingImage.getType();
		this.outWidth = outWidth;
		this.outHeight = outHeight;
		setForEachInputParameters();
	}

	public ImageProcessor(Logger logger, BufferedImage workingImage, RGBWeights rgbWeights) {
		this(logger, workingImage, rgbWeights, workingImage.getWidth(), workingImage.getHeight());
	}

	// MARK: change picture hue - example
	public BufferedImage changeHue() {
		logger.log("Prepareing for hue changing...");

		int r = rgbWeights.redWeight;
		int g = rgbWeights.greenWeight;
		int b = rgbWeights.blueWeight;
		int max = rgbWeights.maxWeight;

		BufferedImage ans = newEmpInOutScaleYInputSizedImage();

		forEach((y, x) -> {
			Color c = new Color(workingImage.getRGB(x, y));
			int red = r * c.getRed() / max;
			int green = g * c.getGreen() / max;
			int blue = b * c.getBlue() / max;
			Color color = new Color(red, green, blue);
			ans.setRGB(x, y, color.getRGB());
		});

		logger.log("Changing hue done!");

		return ans;
	}

	public final void setForEachInputParameters() {
		setForEachParameters(inWidth, inHeight);
	}

	public final void setForEachOutputParameters() {
		setForEachParameters(outWidth, outHeight);
	}

	public final BufferedImage newEmpInOutScaleYInputSizedImage() {
		return newEmpInOutScaleYImage(inWidth, inHeight);
	}

	public final BufferedImage newEmpInOutScaleYOutputSizedImage() {
		return newEmpInOutScaleYImage(outWidth, outHeight);
	}

	public final BufferedImage newEmpInOutScaleYImage(int width, int height) {
		return new BufferedImage(width, height, workingImageType);
	}

	// A helper method that deep copies the current working image.
	public final BufferedImage duplicateWorkingImage() {
		BufferedImage output = newEmpInOutScaleYInputSizedImage();
		setForEachInputParameters();
		forEach((y, x) -> output.setRGB(x, y, workingImage.getRGB(x, y)));

		return output;
	}
	
	/**
	 * Convert the image into greyscale, using the RGB weights that was entered in the
	 * application main window.
	 * @return
	 */
	public BufferedImage greyscale() {
		BufferedImage greyScaled = newEmpInOutScaleYImage(inWidth, inHeight);
		int i, j, red, green, blue, greyColorValue;
		for (i = 0; i < inWidth; i++) {
			for (j = 0; j < inHeight; j++) {
				Color c = new Color(workingImage.getRGB(i, j));
				red = c.getRed();
				green = c.getGreen();
				blue = c.getBlue();
				greyColorValue = (red * rgbWeights.redWeight + green * rgbWeights.greenWeight +
						blue * rgbWeights.blueWeight) / rgbWeights.weightsAmount;
				Color greyColor = new Color(greyColorValue, greyColorValue, greyColorValue);
				greyScaled.setRGB(i, j, greyColor.getRGB());
			}
		}
		
		return greyScaled;
		
	}
	
	/**
	 * Resizing image by using Nearest Neighbor method.
	 * Each new sized image pixel's color will be taken from
	 * the nearest appropriate pixel in the original image.
	 * Each image has different dimensions.
	 * @return
	 */
	public BufferedImage nearestNeighbor() { 
		// New different dimension image that will be returned.
		BufferedImage img = new BufferedImage(outWidth, outHeight, workingImageType);
		
		// The scale of original image to the new
		double OldToNewScaleX = (double) inWidth / outWidth;
		double OldToNewScaleY = (double) inHeight / outHeight;
		
		// The scale of the new image to original.
		// If one of dimensions of the new image is bigger than dimension of the original
		// image, then we would like keep the dimension scale of new to old. 
		// This values will help us not to go out of bounds in original image.
		double NewToOldScaleX = 0;
		if(OldToNewScaleX < 1) {							// Increasing the X dimension.
			NewToOldScaleX = (double) outWidth / inWidth;
		}
		double NewToOldScaleY = 0;
		if(OldToNewScaleY < 1) {							// Increasing the Y dimension.
			NewToOldScaleY = (double) outHeight / inHeight;
		}
		// Running through the new image and choosing nearest pixels from the old image
		for(int i = 0; i < outWidth; i++) {
			for(int j = 0; j < outHeight; j++) {	
				int iCorrect = (i < outWidth - NewToOldScaleX) ? i :  (int) Math.round((double) i - NewToOldScaleX);
				int jCorrect = (j < outHeight - NewToOldScaleY) ? j : (int) Math.round((double) j - NewToOldScaleY);
				img.setRGB(i, j, workingImage.getRGB((int) (iCorrect * OldToNewScaleX), 
						(int) Math.round(jCorrect * OldToNewScaleY)));
			}
		}
		return img;
	}
}
