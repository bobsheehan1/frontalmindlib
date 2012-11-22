package com.frontalmind.shape.behavior;

import java.util.Random;

import com.frontalmind.shape.StrokeAndFillDrawable;

public class RotateBehavior implements IBehavior {

	private int rotateInc;

	public  RotateBehavior(int rotateInc, boolean enableRandom)
	{
		if (!enableRandom){
			this.rotateInc = rotateInc;
		} else {	
			Random random = new Random();
			this.rotateInc = random.nextInt(rotateInc);
			if (this.rotateInc == 0)
				this.rotateInc = 1;
			if (random.nextBoolean())
				this.rotateInc = -this.rotateInc;
		}
	}
	
	@Override
	public boolean animate(StrokeAndFillDrawable drawable) {
		drawable.rotation += rotateInc;
		return true;
	}

}
