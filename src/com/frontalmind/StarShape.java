package com.frontalmind;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.shapes.Shape;

public class StarShape extends Shape {

    private Path path = new Path();
    private int arms;
    private double innerRadius, outerRadius;
	static private Random random = new Random();

	@Override
	public void draw(Canvas canvas, Paint paint) {
	      canvas.drawPath(path, paint);	
	}
	
	@Override
	public void onResize(float width, float height){
		super.onResize(width, height);
		Point center = new Point();
		center.x = (int) (width/2);
		center.y = (int) (height/2);
		double rOuter = width/2; 
		double radiusRatio = this.innerRadius/this.outerRadius;
		double rInner = rOuter *radiusRatio;
		calcPath(center, rOuter, rInner);
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

	public static Shape generateRandomStar() {
		int n = 7 + random.nextInt(20);
		int innerRadius = 8 - random.nextInt(4);
		return new StarShape(n, new Point (5,5), 10, innerRadius);
	}

}
