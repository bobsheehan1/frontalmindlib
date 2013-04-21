package com.frontalmind.shape.behavior;

import java.util.Random;

import com.frontalmind.StrokeAndFillDrawable;

public class AlphaBehavior implements IBehavior {

	private int alphaInc;
	private int strokeAlphaInc;
	private int fillAlphaMax, strokeAlphaMax, minAlpha;
	private boolean enableToggle;
	
	public AlphaBehavior(int alphaIncIn, int fillAlphaMax, int strokeAlphaMax, int minAlpha, boolean enableRandom, boolean enableToggle)
	{
		this.enableToggle = enableToggle;
		this.minAlpha = minAlpha;
		this.fillAlphaMax = fillAlphaMax;
		this.strokeAlphaMax = strokeAlphaMax;
		if (!enableRandom){
			this.alphaInc = alphaIncIn;
			this.strokeAlphaInc = alphaIncIn;
		} else {
			Random random = new Random();
			this.alphaInc = random.nextInt(alphaIncIn);
			if (this.alphaInc == 0)
				this.alphaInc = 1;
			
			this.strokeAlphaInc = random.nextInt(alphaIncIn);
			if (this.strokeAlphaInc == 0)
				this.strokeAlphaInc = 1;
		}
	}

	@Override
	public boolean animate(StrokeAndFillDrawable drawable) {
		int fillAlpha = drawable.getFillAlpha();
		fillAlpha += alphaInc;
		if (fillAlpha >= minAlpha && fillAlpha < fillAlphaMax) {
			drawable.setFillAlpha(fillAlpha);
		} else if (enableToggle){
			alphaInc *= -1;
			if (fillAlpha < minAlpha)
				drawable.setFillAlpha(minAlpha);

		}

		if (drawable.isEnableStroke()) {
			int strokeAlpha = drawable.getStrokeAlpha();
			strokeAlpha += strokeAlphaInc;
			if (strokeAlpha >= minAlpha && strokeAlpha < strokeAlphaMax) {
				drawable.setStrokeAlpha(strokeAlpha);
			} else if (enableToggle){
				strokeAlphaInc *= -1;
				if (strokeAlpha < minAlpha)
					drawable.setStrokeAlpha(minAlpha);
			}
		}

		return false;
	}

}
