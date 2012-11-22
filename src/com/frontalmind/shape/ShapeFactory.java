package com.frontalmind.shape;

import java.util.Random;

import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
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

	public static StrokeAndFillDrawable generateDrawable(String shapeStr, 
			String colorRange, 
			String borderColorRange,
			int strokeWidth
			) 
	{
		
		StrokeAndFillDrawable drawable = new StrokeAndFillDrawable();
		drawable.colorRange = colorRange;
		drawable.borderColorRange = borderColorRange;
		drawable.setStrokeWidth(strokeWidth);
		drawable.setFillColor(drawable.generateColor(true, 0));
		drawable.setStrokeColor(drawable.generateColor(false, 0));
		drawable.setStrokeAlpha(0);
		generateDrawable(shapeStr, drawable);
		

		return drawable;
	}
	
	public static StrokeAndFillDrawable generateDrawable(String shapeStr, final StrokeAndFillDrawable drawable) 
	{
		if (shapeStr.equals("circle")) {
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
			drawable.centerColor = drawable.generateColor(true, 255);
			drawable.edgeColor = drawable.generateColor(true, 255);
			
			ShapeDrawable.ShaderFactory sf = new ShapeDrawable.ShaderFactory() {
			    @Override
			    public Shader resize(int width, int height) {
					Point center = new Point();
					center.x = (int) (width/2);
					center.y = (int) (height/2);
					float rOuter = width/2; 
					float radiusRatio = (float)innerRadius/(float)outerRadius;
					drawable.radialRadius = (rOuter *radiusRatio);
					drawable.radialGradient = new RadialGradient(
							center.x, center.y, drawable.radialRadius,
							drawable.centerColor, drawable.edgeColor,
						    Shader.TileMode.CLAMP);
					return drawable.radialGradient;
			    }
			};
			
			drawable.setShaderFactory(sf);
			
		} else if (shapeStr.equals("random")) {
			switch (random.nextInt(5)) {
			case 0:
				return generateDrawable("circle", drawable);
			case 1:
				return generateDrawable("rect", drawable);
			case 2:
				return generateDrawable("round_rect", drawable);
			case 3:
				return generateDrawable("star", drawable);
			case 4:
				return generateDrawable("hexagon", drawable);
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
