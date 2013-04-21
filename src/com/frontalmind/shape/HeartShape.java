package com.frontalmind.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.shapes.Shape;

/*package chapter4;*/

public class HeartShape extends Shape {
	Path path;

	public HeartShape(float x, float y, float w, float h) {
		path = new Path();
		calcPath(x, y, w, h);
	}

	private void calcPath(float x, float y, float w, float h) {
		
		// v cleavage point
		float x0 = x + 0.5f * w;
		float y0 = y + 0.3f * h;
		
		// top left heart lobe apex
		float x1 = x + 0.15f * w;
		float y1 = y;
		
		// ccw left edge control point
		float x2 = x;
		float y2 = y + 0.6f * h;
		
		// ccw bottom point
		float x3 = x + 0.5f * w;
		float y3 = y + 0.9f * h;
		
		// right edge control point
		float x4 = x + w;
		float y4 = y + 0.6f * h;
		
		// top right heart lobe apex
		float x5 = x + 0.85f * w;
		float y5 = y;
		
		path.reset();
		path.moveTo(x0, y0);
		path.cubicTo(x1, y1, x2, y2, x3, y3);
		path.cubicTo(x4, y4, x5, y5, x0, y0);
	}

	@Override
	public void onResize(float width, float height){
		super.onResize(width, height);
//		cx = width/2;
//		cy = height/2;
//		r = width/2;
//		calcArrays();
		calcPath(0,0,width, height);
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawPath(path, paint);
	}
}
