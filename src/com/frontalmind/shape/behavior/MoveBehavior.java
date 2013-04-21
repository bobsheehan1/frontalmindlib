package com.frontalmind.shape.behavior;

import java.util.Random;

import android.graphics.Rect;

import com.frontalmind.StrokeAndFillDrawable;


public class MoveBehavior implements IBehavior {
	
	private int animateX, animateY;

	public  MoveBehavior(int animateX, int animateY, boolean enableRandom)
	{
		if (!enableRandom){
			this.animateX = animateX;
			this.animateY = animateY;
		} else {
			Random random = new Random();
			this.animateX = random.nextInt(animateX);
			if (this.animateX == 0)
				this.animateX = 1;
			
			this.animateY = random.nextInt(animateY);
			if (this.animateY == 0)
				this.animateY = 1;
			
			if (random.nextBoolean())
				this.animateX = -this.animateX;
			if (random.nextBoolean())
				this.animateY = -this.animateY;
		}
	}
	
	@Override
	public boolean animate(StrokeAndFillDrawable drawable) {
		Rect bounds = drawable.getBounds();
		bounds.bottom = bounds.bottom + animateY;
		bounds.top = bounds.top + animateY;
		bounds.left = bounds.left + animateX;
		bounds.right = bounds.right + animateX;
		drawable.setBounds(bounds);

		return true;
	}

}
