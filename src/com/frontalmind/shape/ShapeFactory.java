package com.frontalmind.shape;

import java.util.Random;

import com.frontalmind.StrokeAndFillDrawable;

import android.graphics.Point;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;



public class ShapeFactory
{
	static private Random random = new Random(); 
	static float[] outerR = new float[] { 3, 3, 3, 3, 3, 3, 3, 3 };

	private ShapeFactory(){

	}

	public static StrokeAndFillDrawable initializeDrawable(StrokeAndFillDrawable drawable, String shapeStr, String colorRange, String borderColorRange, int strokeWidth) 
	{
		//drawable.setShaderFactory(null);

		drawable.borderColorRange = borderColorRange;
		drawable.setStrokeWidth(strokeWidth);
		drawable.colorRange = colorRange;
		drawable.setFillColor(drawable.generateColor(true, 0));
		drawable.setStrokeColor(drawable.generateColor(false, 0));
		drawable.setFillAlpha(0);
		drawable.setStrokeAlpha(0);
		initializeShape(shapeStr, drawable);
		
		drawable.setEnable(true);
		
		return drawable;
	}
	
	public static StrokeAndFillDrawable initializeShape(String shapeStr, final StrokeAndFillDrawable drawable) 
	{
		if (shapeStr.equals("heart")) {
			drawable.setShape(new HeartShape(0,0,10,10));
			drawable.updateShader();		
		}else if (shapeStr.equals("circle")) {
			drawable.setShape(new OvalShape());
		} else if (shapeStr.equals("rect")) {
			drawable.setShape(new RectShape());
		} else if (shapeStr.equals("round_rect")) {
			drawable.setShape(new RoundRectShape(outerR, null, null));
		} else if (shapeStr.equals("triangle")) {
			drawable.setShape(new PolygonShape(3));
		} else if (shapeStr.equals("hexagon")) {
			drawable.setShape(new PolygonShape(6));
		} else if (shapeStr.equals("star")) {
			final int n = 7 + random.nextInt(20);
			final int innerRadius = 8 - random.nextInt(6);
			final int outerRadius = 10;
			Shape star = new StarShape(n, new Point (5,5), outerRadius, innerRadius);
			drawable.setShape(star);		
			drawable.updateShader();		
		} else if (shapeStr.equals("random")) {
			switch (random.nextInt(6)) {
			case 0:
				return initializeShape("circle", drawable);
			case 1:
				return initializeShape("rect", drawable);
			case 2:
				return initializeShape("round_rect", drawable);
			case 3:
				return initializeShape("star", drawable);
			case 4:
				return initializeShape("hexagon", drawable);
			case 5:
				return initializeShape("heart", drawable);
			}
		}


		return drawable;
	}




	public static void setCornerRadius(float cornerRadius) {
		outerR = new float[] { cornerRadius, cornerRadius, cornerRadius,
				cornerRadius, cornerRadius, cornerRadius, cornerRadius,
				cornerRadius };
		
	}
}
