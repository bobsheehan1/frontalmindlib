package com.frontalmind.shape.behavior;

import com.frontalmind.StrokeAndFillDrawable;

public class DeathBehavior implements IBehavior {

	private int alphaInc;

	public DeathBehavior(int alphaInc){
		this.alphaInc = alphaInc;
	}

	@Override
	public boolean animate(StrokeAndFillDrawable drawable) {
		int fillAlpha = drawable.getFillAlpha();
		fillAlpha -= alphaInc;
		int strokeAlpha = drawable.getStrokeAlpha();
		strokeAlpha -= alphaInc;

		if (fillAlpha >= 0 )
			drawable.setFillAlpha(fillAlpha);
		if (strokeAlpha >= 0 )
			drawable.setStrokeAlpha(strokeAlpha);
		
		if (fillAlpha < 0 && strokeAlpha < 0)
		{
			drawable.setEnable(false);
			drawable.resetShape();
		}

		return true;
	}

}
