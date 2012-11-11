package com.frontalmind;

import java.util.Random;

import android.graphics.Point;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;


public class ShapeFactory
{
	static private Random random = new Random(); 
	static float[] outerR = new float[] { 3, 3, 3, 3, 3, 3, 3, 3 };
//	private static Shape ovalShape;
//	private static Shape rectShape;
//	private static Shape roundRectShape;
//	private static Shape hexShape;
//	private static Shape triangleShape;
//	private static Shape starShape;
	
	static {
//		ovalShape = new OvalShape();
//		rectShape = new RectShape();
//		hexShape = new PolygonShape(6);
//		triangleShape = new PolygonShape(3);
//		starShape = new StarShape(8, new Point (5,5), 5, 10);
//		setCornerRadius(12);		
	}

	ShapeFactory(){

	}
	
	static public Shape generateRandomShape() {
		switch (random.nextInt(5)) {
		case 0:
			return new OvalShape();
		case 1:
			return new RectShape();
		case 2:
			return new RoundRectShape(outerR, null, null);
		case 3:
			return new PolygonShape(6);
		case 4:
			return StarShape.generateRandomStar();
		default:
			Log.e("GridView", "invalid shape enum");
			break;
		}
		return null;
	}

	public static StrokeAndFillDrawable generateDrawable(String shapeStr, 
			String colorRange, 
			String borderColorRange,
			int fillAlpha,
			int strokeAlpha,
			int strokeWidth,
			int threshold,
			int decay
			) 
	{
		StrokeAndFillDrawable drawable = null;
		if (shapeStr.equals("circle")) {
			drawable = new StrokeAndFillDrawable(new OvalShape());
		} else if (shapeStr.equals("rect")) {
			drawable = new StrokeAndFillDrawable(new RectShape());
		} else if (shapeStr.equals("round_rect")) {
			drawable = new StrokeAndFillDrawable(new RoundRectShape(outerR, null, null));
		} else if (shapeStr.equals("triangle")) {
			drawable = new StrokeAndFillDrawable(new PolygonShape(3));
		} else if (shapeStr.equals("hexagon")) {
			drawable = new StrokeAndFillDrawable(new PolygonShape(6));
		} else if (shapeStr.equals("star")) {
			drawable = new StrokeAndFillDrawable(StarShape.generateRandomStar());
		} else if (shapeStr.equals("random")) {
			drawable = new StrokeAndFillDrawable(generateRandomShape());
		}
		drawable.colorRange = colorRange;
		drawable.borderColorRange = borderColorRange;
		drawable.fillAlpha = fillAlpha;
		drawable.strokeAlpha = strokeAlpha;
		drawable.threshold = threshold;
		drawable.decay = decay;
		drawable.setStrokeWidth(strokeWidth);
		drawable.updateColor(true);
		drawable.updateColor(false);
		return drawable;
	}

	public static void setCornerRadius(float cornerRadius) {
		outerR = new float[] { cornerRadius, cornerRadius, cornerRadius,
				cornerRadius, cornerRadius, cornerRadius, cornerRadius,
				cornerRadius };
		
	}
}
