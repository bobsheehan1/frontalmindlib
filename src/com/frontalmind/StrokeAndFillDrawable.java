package com.frontalmind;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

public class StrokeAndFillDrawable extends ShapeDrawable {

	
	Paint fillpaint, strokepaint;
	private boolean enableStroke;
	private boolean enable;
	private boolean locked = false;
	
	private int strokeWidth;
	public int fillAlpha;
	public int strokeAlpha;
	public String borderColorRange;
	public String colorRange;
	public int decay, threshold;
	private int animateX, animateY;
	
	static private Random random = new Random();

	
	public StrokeAndFillDrawable(Shape s) {
		super(s);
		fillpaint = this.getPaint();
		fillpaint.setStyle(Paint.Style.FILL);
		enableStroke = true;
		enable = true;
		strokeWidth = 2;
		fillAlpha = 255;
		strokeAlpha = 255;
		decay = 8;
		threshold = 0;
		colorRange = "All";
		borderColorRange = "All";
		
		animateX = random.nextInt(10);
		if (animateX == 0)
			animateX = 1;
		if (animateY == 0)
			animateY = 1;
		
		animateY = random.nextInt(10);
		if (random.nextBoolean())
			animateX = -animateX;
		if (random.nextBoolean())
			animateY = -animateY;
		
			
		

	}
	
	public void setStrokeWidth(int width){
		this.strokeWidth = width;
	}
	
	public void setStrokeColor(int color){
		if (strokepaint == null){
			strokepaint = new Paint();
			strokepaint.setStyle(Paint.Style.STROKE);
			strokepaint.setStrokeWidth(strokeWidth);				
		}
		if (strokeWidth == 0)
			setEnableStroke(false);
		else
			setEnableStroke(true);

		strokepaint.setARGB(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
	}

	
	public int getStrokeColor(){
		if (strokepaint != null){
			return strokepaint.getColor();			
		}
		return 0;
	}
	@Override
	protected void onDraw(Shape shape, Canvas canvas, Paint fillPaint) {
		if (!enable)
			return;
		shape.draw(canvas, fillPaint);
		if (enableStroke)
			shape.draw(canvas, strokepaint);
	}

	public void setFillColour(int c) {
		fillpaint.setColor(c);
	}


	public boolean isEnableStroke() {
		return enableStroke;
	}

	public void setEnableStroke(boolean enableStroke) {
		this.enableStroke = enableStroke;
	}

	public int getStrokeWidth() {
		return strokeWidth;
	}

	public boolean getEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		if (!locked)
			this.enable = enable;
	}

	public void setLock(boolean locked) {
		this.locked  = locked;
	}
	
	public boolean updateColor(boolean fill) {

		boolean thresholdHit = false;
		int c, a;
		String colorStr = colorRange;
		//stroke
		if (!fill && isEnableStroke()){
			c = getStrokeColor();
			a = strokeAlpha;
			colorStr = borderColorRange;
		} else {
			c = getPaint().getColor();
			a = fillAlpha;
		}
		
		int r = Color.red(c);
		int g = Color.green(c);
		int b = Color.blue(c);
		r -= decay;
		g -= decay;
		b -= decay;
		if (r < 0)
			r = 0;
		if (g < 0)
			g = 0;
		if (b < 0)
			b = 0;

		if (colorStr.equals("All")){
			if (r < threshold)
				r = threshold;
			if (g < threshold)
				g = threshold;
			if (b < threshold)
				b = threshold;
			if (r <= threshold && g <= threshold && b <= threshold){
				c = generateColor(fill);
				thresholdHit = true;
			}else
				c = Color.argb(a, r, g, b);

		}else if (colorStr.equals("Red")){
			if (r < threshold)
				r = threshold;
			if (r <= threshold){
				thresholdHit = true;
				c = generateColor(fill);
			}else
				c = Color.argb(a, r, g, b);
		}else if (colorStr.equals("Green")){
			if (g < threshold)
				g = threshold;
			if (g <= threshold){
				thresholdHit = true;
				c = generateColor(fill);
			}else
				c = Color.argb(a, r, g, b);

		}else if (colorStr.equals("Blue")){
			if (b < threshold)
				b = threshold;
			if (b <= threshold){
				c = generateColor(fill);
				thresholdHit = true;
			} else
				c = Color.argb(a, r, g, b);
		}else if (colorStr.equals("Cyan")){
			if (g < threshold)
				g = threshold;
			if (b < threshold)
				b = threshold;
			if (g <= threshold && b <= threshold){
				c = generateColor(fill);
				thresholdHit = true;
			} else
				c = Color.argb(a, r, g, b);
		}else if (colorStr.equals("Yellow")){
			if (r < threshold)
				r = threshold;
			if (g < threshold)
				g = threshold;
			if (r <= threshold && g <= threshold){
				c = generateColor(fill);
				thresholdHit = true;
			} else
				c = Color.argb(a, r, g, b);

		}else if (colorStr.equals("Magenta")){
			if (r < threshold)
				r = threshold;
			if (b < threshold)
				b = threshold;
			if (r <= threshold && b <= threshold){
				c = generateColor(fill);
				thresholdHit = true;
			}else
				c = Color.argb(a, r, g, b);

		}else if (colorStr.equals("Gray")){
			if (r < threshold)
				r = threshold;
			if (g < threshold)
				g = threshold;
			if (b < threshold)
				b = threshold;
			if (r <= threshold && g <= threshold && b <= threshold){
				c = generateColor(fill);
				thresholdHit = true;
			}else
				c = Color.argb(a, r, g, b);

		}
	
		//stroke
		if (!fill && isEnableStroke()){
			setStrokeColor(c);
		} else 
			setFillColour(c);
		
		return thresholdHit;
	}
	
	private int generateColor(boolean fill) {
		int valA = fillAlpha;
		String colorStr = colorRange;
		//stroke
		if (!fill){
			valA = strokeAlpha;
			colorStr = borderColorRange;

		}

		int valR = threshold + random.nextInt(256-threshold);
		int valG = threshold + random.nextInt(256-threshold);
		int valB = threshold + random.nextInt(256-threshold);

		int c = Color.WHITE;
		if (colorStr.equals("All"))
			c = Color.argb(valA, valR, valG, valB);
		else if (colorStr.equals("Red"))
			c = Color.argb(valA, valR, 0, 0);
		else if (colorStr.equals("Green"))
			c = Color.argb(valA, 0, valG, 0);
		else if (colorStr.equals("Blue"))
			c = Color.argb(valA, 0, 0, valB);
		else if (colorStr.equals("Cyan"))
			c = Color.argb(valA, 0, valG, valB);
		else if (colorStr.equals("Yellow"))
			c = Color.argb(valA, valR, valG, 0);
		else if (colorStr.equals("Magenta"))
			c = Color.argb(valA, valR, 0, valB);
		else if (colorStr.equals("Gray"))
			c = Color.argb(valA, valR, valR, valR);
		return c;
	}
	
	public void animate(){
		Rect bounds = getBounds();
		bounds.bottom = bounds.bottom + animateY;
		bounds.top = bounds.top + + animateY;
		bounds.left = bounds.left + animateX;
		bounds.right = bounds.right + + animateX;
		setBounds(bounds);

	}
	
}
