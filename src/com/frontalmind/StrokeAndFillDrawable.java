package com.frontalmind;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

public class StrokeAndFillDrawable extends ShapeDrawable {
	Paint fillpaint, strokepaint;
	private boolean enableStroke;
	private int strokeWidth;
	private boolean enable;
	private boolean locked = false;
	
	public StrokeAndFillDrawable(Shape s) {
		super(s);
		fillpaint = this.getPaint();
		fillpaint.setStyle(Paint.Style.FILL);
		enableStroke = true;
		enable = true;
	}
	
	public void setStrokeColor(int color, int width){
		if (strokepaint == null){
			strokepaint = new Paint(fillpaint);
			strokepaint.setStyle(Paint.Style.STROKE);
			strokepaint.setStrokeWidth(width);				
		}
		if (width == 0)
			setEnableStroke(false);
		else
			setEnableStroke(true);

		strokeWidth = width;
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
}
