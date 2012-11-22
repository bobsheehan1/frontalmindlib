package com.frontalmind.shape;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.shapes.Shape;

public class StarShape extends Shape implements IBehaveShape {

    private Path path = new Path();
    private int arms;
    private double innerRadius, outerRadius;
	Point center = new Point(5,5);
	Paint circlePaint = new Paint();
	Paint starPaint;


	@Override
	public void draw(Canvas canvas, Paint paint) {
		starPaint = paint;
		canvas.drawPath(path, paint);
	}
	
	@Override
	public void onResize(float width, float height){
		super.onResize(width, height);
		center = new Point();
		center.x = (int) (width/2);
		center.y = (int) (height/2);
		double rOuter = width/2; 
		double radiusRatio = this.innerRadius/this.outerRadius;
		this.innerRadius = rOuter *radiusRatio;
		this.outerRadius = rOuter;
		calcPath(center, this.outerRadius, this.innerRadius);
	}
	
	public StarShape(int arms, Point center, double rOuter, double rInner)
	{
		innerRadius = rInner;
		outerRadius = rOuter;
		this.arms = arms;
	    calcPath(center, rOuter, rInner);
	}

	private void calcPath(Point center, double rOuter, double rInner) {
		double angle = Math.PI / arms;

		path.reset();
	    for (int i = 0; i < 2 * arms; i++)
	    {
	        double r = (i & 1) == 0 ? rOuter : rInner;
	        PointF p = new PointF();
	        p.x = (float) (center.x + Math.cos(i * angle) * r);
	        p.y = (float) (center.y + Math.sin(i * angle) * r);
	        if (i == 0) 
	        	path.moveTo(p.x, p.y);
	        else 
	        	path.lineTo(p.x, p.y);
	    }
	    path.close();
	}


	@Override
	public void updateBirth() {
		// TODO Auto-generated method stub
		
	}
}
