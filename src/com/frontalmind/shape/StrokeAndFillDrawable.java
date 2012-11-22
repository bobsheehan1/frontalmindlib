package com.frontalmind.shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

import com.frontalmind.shape.behavior.IBehavior;

public class StrokeAndFillDrawable extends ShapeDrawable {

	//static private BlurMaskFilter blurMaskFilterOuter = new BlurMaskFilter(15, Blur.OUTER);
	//static private BlurMaskFilter blurMaskFilterInner = new BlurMaskFilter(15, Blur.INNER);
	//static private BlurMaskFilter blurMaskFilterNormal = new BlurMaskFilter(15, Blur.NORMAL);
	//static private BlurMaskFilter blurMaskFilterSolid = new BlurMaskFilter(15, Blur.SOLID);
	Paint fillpaint, strokePaint;
	private boolean enableStroke;
	private boolean enable;
	private boolean locked = false;
	
	float shaderCx = 0;
	float shaderCy = 0;
	float shaderRadius = 100;
	
	RadialGradient radialGradientShader;
	int centerColor, edgeColor;
	
	private BlurMaskFilter blurMaskFilter;
	
	public String borderColorRange;
	public String colorRange;
	public int rotation;
	protected float radialRadius;
	protected RadialGradient radialGradient;
	List<IBehavior> behaviors;
	
	static private Random random = new Random();

	public StrokeAndFillDrawable() {
		super();
		fillpaint = this.getPaint();
		fillpaint.setStyle(Paint.Style.FILL);
		strokePaint = new Paint();
		strokePaint.setStyle(Paint.Style.STROKE);

		enableStroke = true;
		enable = true;
		colorRange = "All";
		borderColorRange = "All";
		
		behaviors = new ArrayList<IBehavior>();
		
	}
	
	public void addBehavior(IBehavior behavior){
		this.behaviors.add(behavior);
	}

	public StrokeAndFillDrawable(Shape s) {
		super(s);
		fillpaint = this.getPaint();
		fillpaint.setStyle(Paint.Style.FILL);
		strokePaint = new Paint();
		strokePaint.setStyle(Paint.Style.STROKE);

		enableStroke = true;
		enable = true;
		colorRange = "All";
		borderColorRange = "All";

	}
	
	public void setStrokeWidth(int width){
		strokePaint.setStrokeWidth(width);				
		
		if (width == 0)
			setEnableStroke(false);
		else
			setEnableStroke(true);
	}
	
	public void setStrokeColor(int color){
		strokePaint.setARGB(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
	}

	
	public int getStrokeColor(){
		if (strokePaint != null){
			return strokePaint.getColor();			
		}
		return 0;
	}
	
	@Override
	protected
	void onBoundsChange(Rect bounds){
		super.onBoundsChange(bounds);

	}
	/* (non-Javadoc)
	 * @see android.graphics.drawable.ShapeDrawable#onDraw(android.graphics.drawable.shapes.Shape, android.graphics.Canvas, android.graphics.Paint)
	 */
	@Override
	protected void onDraw(Shape shape, Canvas canvas, Paint fillPaint) {
		if (!enable)
			return;
		//fillPaint.setMaskFilter(blurMaskFilter);
		//fillPaint.setShader(radialGradientShader);
		shape.draw(canvas, fillPaint);
		if (enableStroke && strokePaint != null) {
			//strokepaint.setMaskFilter(blurMaskFilterNormal);
			shape.draw(canvas, strokePaint);
		}
	}

	public void setFillColor(int c) {
		fillpaint.setColor(c);
	}
	
	public void setFillAlpha(int a) {
		fillpaint.setAlpha(a);
	}
	
	public int getFillAlpha() {
		return fillpaint.getAlpha();
	}

	public boolean isEnableStroke() {
		return enableStroke;
	}

	public void setEnableStroke(boolean enableStroke) {
		this.enableStroke = enableStroke;
	}

	public float getStrokeWidth() {
		return strokePaint.getStrokeWidth();
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
	
	// public boolean updateColor(boolean fill) {
	// //
	// // int alpha = this.getPaint().getAlpha() - 5;
	// // if (alpha < 0)
	// // alpha = 0;
	// // this.getPaint().setAlpha(alpha);
	// //
	// // return true;
	//
	// boolean thresholdHit = false;
	// int c, a;
	// String colorStr = colorRange;
	// //stroke
	// if (!fill && isEnableStroke()){
	// c = getStrokeColor();
	// a = getStrokeAlpha();
	// colorStr = borderColorRange;
	// } else {
	// c = getPaint().getColor();
	// a = getFillAlpha();
	// }
	//
	// int r = Color.red(c);
	// int g = Color.green(c);
	// int b = Color.blue(c);
	// r -= decay;
	// g -= decay;
	// b -= decay;
	// if (r < 0)
	// r = 0;
	// if (g < 0)
	// g = 0;
	// if (b < 0)
	// b = 0;
	//
	// if (colorStr.equals("All")){
	// if (r < threshold)
	// r = threshold;
	// if (g < threshold)
	// g = threshold;
	// if (b < threshold)
	// b = threshold;
	// if (r <= threshold && g <= threshold && b <= threshold){
	// c = generateColor(fill);
	// thresholdHit = true;
	// }else
	// c = Color.argb(a, r, g, b);
	//
	// }else if (colorStr.equals("Red")){
	// if (r < threshold)
	// r = threshold;
	// if (r <= threshold){
	// thresholdHit = true;
	// c = generateColor(fill);
	// }else
	// c = Color.argb(a, r, g, b);
	// }else if (colorStr.equals("Green")){
	// if (g < threshold)
	// g = threshold;
	// if (g <= threshold){
	// thresholdHit = true;
	// c = generateColor(fill);
	// }else
	// c = Color.argb(a, r, g, b);
	//
	// }else if (colorStr.equals("Blue")){
	// if (b < threshold)
	// b = threshold;
	// if (b <= threshold){
	// c = generateColor(fill);
	// thresholdHit = true;
	// } else
	// c = Color.argb(a, r, g, b);
	// }else if (colorStr.equals("Cyan")){
	// if (g < threshold)
	// g = threshold;
	// if (b < threshold)
	// b = threshold;
	// if (g <= threshold && b <= threshold){
	// c = generateColor(fill);
	// thresholdHit = true;
	// } else
	// c = Color.argb(a, r, g, b);
	// }else if (colorStr.equals("Yellow")){
	// if (r < threshold)
	// r = threshold;
	// if (g < threshold)
	// g = threshold;
	// if (r <= threshold && g <= threshold){
	// c = generateColor(fill);
	// thresholdHit = true;
	// } else
	// c = Color.argb(a, r, g, b);
	//
	// }else if (colorStr.equals("Magenta")){
	// if (r < threshold)
	// r = threshold;
	// if (b < threshold)
	// b = threshold;
	// if (r <= threshold && b <= threshold){
	// c = generateColor(fill);
	// thresholdHit = true;
	// }else
	// c = Color.argb(a, r, g, b);
	//
	// }else if (colorStr.equals("Gray")){
	// if (r < threshold)
	// r = threshold;
	// if (g < threshold)
	// g = threshold;
	// if (b < threshold)
	// b = threshold;
	// if (r <= threshold && g <= threshold && b <= threshold){
	// c = generateColor(fill);
	// thresholdHit = true;
	// }else
	// c = Color.argb(a, r, g, b);
	//
	// }
	//
	// //stroke
	// if (!fill && isEnableStroke()){
	// setStrokeColor(c);
	// } else
	// setFillColor(c);
	//
	// return thresholdHit;
	// }

	public int generateColor(boolean fill, int valA) {
		// int valA = getFillAlpha();
		String colorStr = colorRange;
		// //stroke
		if (!fill) {
			// valA = getStrokeAlpha();
			colorStr = borderColorRange;
			//
		}

		// int valR = threshold + random.nextInt(256-threshold);
		// int valG = threshold + random.nextInt(256-threshold);
		// int valB = threshold + random.nextInt(256-threshold);
		//
		int valR = random.nextInt(256);
		int valG = random.nextInt(256);
		int valB = random.nextInt(256);

		int c = Color.WHITE;
		if (colorStr.equals("All"))
			c = Color.argb(valA, valR, valG, valB);
		else if (colorStr.equals("Red")) {
			if (valR < 100)
				valR += 100;
			c = Color.argb(valA, valR, 0, 0);
		} else if (colorStr.equals("Green")) {
			if (valG < 100)
				valG += 100;
			c = Color.argb(valA, 0, valG, 0);
		} else if (colorStr.equals("Blue")) {
			if (valB < 100)
				valB += 100;
			c = Color.argb(valA, 0, 0, valB);
		} else if (colorStr.equals("Cyan")) {
			if (valB < 100 && valG < 100) {
				valB += 100;
				valG += 100;
			}
			c = Color.argb(valA, 0, valG, valB);
		} else if (colorStr.equals("Yellow")) {
			if (valR < 100 && valG < 100) {
				valR += 100;
				valG += 100;
			}
			c = Color.argb(valA, valR, valG, 0);
		} else if (colorStr.equals("Magenta")) {
			if (valR < 100 && valB < 100) {
				valR += 100;
				valB += 100;
			}
			c = Color.argb(valA, valR, 0, valB);
		} else if (colorStr.equals("Gray")) {
			if (valR < 100) {
				valR += 100;
			}			
			c = Color.argb(valA, valR, valR, valR);
		}
		return c;
	}
	
	public void animate() {
		for (IBehavior behavior : behaviors)
			behavior.animate(this);	
	}

	public void setRadialGradientShaderColor(int centerColor, int edgeColor) {
		this.centerColor = centerColor;
		this.edgeColor = edgeColor;
		//if (reverseGradient){
		//	this.centerColor = edgeColor;
		//	this.edgeColor = centerColor;
		//}
//		if (this.radialGradientShader != null)
//			this.radialGradientShader = new RadialGradient(
//				center.x, center.y, (float)this.innerRadius,
//				this.centerColor, this.edgeColor,
//			    Shader.TileMode.CLAMP);

	}

//	public void getRadialGradientShaderColors(int []colors) {
//		if (reverseGradient){
//			colors[1] = this.centerColor;
//			colors[0] = this.edgeColor;
//		} else {
//			colors[0] = this.centerColor;
//			colors[1] = this.edgeColor;
//		}
//		
//	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public int getStrokeAlpha() {
		return strokePaint.getAlpha();
		//return Color.alpha(strokePaint.getColor());

	}

	public void setStrokeAlpha(int alpha) {
		strokePaint.setAlpha(alpha);

//		if (strokePaint != null){
//			int red = Color.red(strokePaint.getColor());
//			int green = Color.green(strokePaint.getColor());
//			int blue = Color.blue(strokePaint.getColor());
//			strokePaint.setColor(Color.argb(alpha, red, green, blue));
//		} 
	}

	
}
