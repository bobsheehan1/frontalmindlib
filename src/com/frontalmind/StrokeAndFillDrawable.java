package com.frontalmind;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

import com.frontalmind.shape.behavior.DeathBehavior;
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

	
	public String borderColorRange;
	public String colorRange;
	public int rotation;
	//protected float radialRadius;
	protected RadialGradient radialGradient;
	List<IBehavior> behaviors;
	public float scale = 1.0f;
	public boolean dieing;
	
	static private Random random = new Random();

	public StrokeAndFillDrawable() {
		super();
		dieing = false;
		fillpaint = this.getPaint();
		fillpaint.setStyle(Paint.Style.FILL);
		strokePaint = new Paint();
		strokePaint.setStyle(Paint.Style.STROKE);

		enableStroke = true;
		enable = false;
		colorRange = "All";
		borderColorRange = "All";	
		behaviors = new ArrayList<IBehavior>();	
	}
	
	public void addBehavior(IBehavior behavior){
		synchronized(this){
			this.behaviors.add(behavior);
		}
	}
	
	public void resetShape() {
		synchronized(this){
			setShape(null);
			ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
			    @Override
			    public Shader resize(int width, int height) {
			    	return null;
			    }
			};
	
			setShaderFactory(sf);
			radialGradient = null;
			
		}
	}

	public StrokeAndFillDrawable(Shape s) {
		super(s);
		fillpaint = this.getPaint();
		fillpaint.setStyle(Paint.Style.FILL);
		strokePaint = new Paint();
		strokePaint.setStyle(Paint.Style.STROKE);

		enableStroke = true;
		enable = false;
		colorRange = "All";
		borderColorRange = "All";
		
		dieing = false;

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
		int valWhiteBalance = random.nextInt(128);
		int val1 = random.nextInt(256);
		int val2 = random.nextInt(256);
		int val3 = random.nextInt(256);
		
		final int bias = 128;
		int c = Color.WHITE;
		if (colorStr.equals("All")){
			while (val1 < bias && val2 < bias && val3 < 128){
				val1 = random.nextInt(256);
				val2 = random.nextInt(256);
				val3 = random.nextInt(256);		
			}
			c = Color.argb(valA, val1, val2, val3);
		}else if (colorStr.equals("Red")) {
			c = Color.argb(valA, 255, valWhiteBalance, valWhiteBalance);
		} else if (colorStr.equals("Green")) {
			c = Color.argb(valA, valWhiteBalance, 255, valWhiteBalance);
		} else if (colorStr.equals("Blue")) {
			c = Color.argb(valA, valWhiteBalance, valWhiteBalance, 255);
		} else if (colorStr.equals("Cyan")) {
			while (val1 < bias && val2 < bias){
				val1 = random.nextInt(256);
				val2 = random.nextInt(256);
			}
			c = Color.argb(valA, 0, val1, val2);
		} else if (colorStr.equals("Yellow")) {
			while (val1 < bias && val2 < bias){
				val1 = random.nextInt(256);
				val2 = random.nextInt(256);
			}
			c = Color.argb(valA, val1, val2, 0);
		} else if (colorStr.equals("Magenta")) {
			while (val1 < bias && val2 < bias){
				val1 = random.nextInt(256);
				val2 = random.nextInt(256);
			}
			c = Color.argb(valA, val1, 0, val2);
		} else if (colorStr.equals("Gray")) {		
			while (val1 < bias && val2 < bias){
				val1 = random.nextInt(256);
			}
			c = Color.argb(valA, val1, val1, val1);
		}
		return c;
	}
	
	public void animate() {
		synchronized(this){
		for (IBehavior behavior : behaviors)
			behavior.animate(this);	
		}
	}

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

	public void clearBehaviors() {
		synchronized(this){
			//rotation= 0;
			//scale = 1.0f;
			this.behaviors.clear();
		}
	}

	public void updateShader() {
		//final int centerColor = generateColor(true, 255);
		//final int edgeColor = generateColor(true, 255);
		
		ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
		    @Override
		    public Shader resize(int width, int height) {
		    	if (width == 0)
		    		return null;
		    	
		    	createGradient(width, height);
		    	//drawable.radialRadius = ((width/2) * (float)innerRadius/(float)outerRadius);
				return radialGradient;
		    }

			private void createGradient(int width, int height) {
				
				int radius = width/2;
				final int numberColors = random.nextInt(8) + 2;
				int[] colors = new int[numberColors];
				for (int i = 0; i < numberColors; ++i){
					colors[i] = generateColor(true, 255);
				}
				
				radialGradient = new RadialGradient(
						(int) (width/2), 
						(int) (height/2), 
						radius,
						colors, 
						null,
					    Shader.TileMode.CLAMP);
			
			}
		};			
		setShaderFactory(sf);
	}

	public void slowDeath(int decay) {
		this.dieing = true;
		clearBehaviors();
		addBehavior(new DeathBehavior(decay));
	}
}
