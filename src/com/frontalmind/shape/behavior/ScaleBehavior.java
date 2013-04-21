package com.frontalmind.shape.behavior;

import java.util.Random;

import com.frontalmind.StrokeAndFillDrawable;

public class ScaleBehavior implements IBehavior {

	private float scaleMin, scaleMax, scaleInc;
	private boolean enableToggle;

	public ScaleBehavior(float scaleMin, float scaleMax, float scaleInc, boolean enableRandom, boolean enableToggle)
	{
		this.enableToggle = enableToggle;
		this.scaleMin = scaleMin;
		this.scaleMax = scaleMax;
		if (!enableRandom){
			this.scaleInc = scaleInc;
		} else {	
			Random random = new Random();
			this.scaleInc = (.75f + .5f*random.nextFloat())*scaleInc;
		}
	}
	
	@Override
	public boolean animate(StrokeAndFillDrawable drawable) {
		float scale = drawable.scale + scaleInc;
		if (scale >= scaleMin && scale < scaleMax) {
			 drawable.scale = scale;
		} else if (enableToggle){
			scaleInc *= -1;
		}
		
		if (scale < scaleMin)
			 drawable.scale = scaleMin;
		if (scale > scaleMax)
			 drawable.scale = scaleMax;

		
		return true;
	}

}
