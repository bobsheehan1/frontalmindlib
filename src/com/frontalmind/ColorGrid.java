package com.frontalmind;

import java.util.Random;
import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;

public class ColorGrid {
	private Shape ovalShape;
	private Shape rectShape;
	private Shape roundRectShape;
	private Shape hexShape;
	private Shape triangleShape;
	private Random random;
	private int incrX, incrY, viewWidth, viewHeight, blockSize, 
	numX, numY, offsetX, offsetY, padding, decay, threshold, strokeWidth;
	private Vector<StrokeAndFillDrawable> shapes;
	private String colorRange;
	private String shapeStr;
	
	private int fillAlpha;
	private int strokeAlpha;
	
	public ColorGrid() {
		padding = 2;
		decay = 8;
		threshold = 0;
		strokeWidth = 2;
		colorRange = "All";
		shapeStr = "random";
		fillAlpha = 255;
		strokeAlpha = 255;
		random = new Random();
		shapes = new Vector<StrokeAndFillDrawable>();
		ovalShape = new OvalShape();
		rectShape = new RectShape();
		hexShape = new PolygonShape(6);
		triangleShape = new PolygonShape(3);
	
		setBlockSize(50);
	}

	public void updateColors() {
		for (StrokeAndFillDrawable drawable : shapes) {

			// fill
			if (updateColor(drawable, true)) {
				if (shapeStr.equals("random")){
					drawable.setShape(generateShape());
					updateColor(drawable, true);
				}
			}

			if (drawable.isEnableStroke() && updateColor(drawable, false)) {
			}
		}

	}
	
	public void createGrid(int w, int h) {
		if (shapeStr.equals("hexagon")){
			createHoneycomb(w, h);
			return;
		}
		
		if (w <= 0 || h <= 0)
			return;
		shapes.clear();
		this.viewWidth = w;
		this.viewHeight = h;
		int totalCellWidth = blockSize + padding * 2;
		
		this.incrX = totalCellWidth + strokeWidth;
		this.numX = viewWidth/this.incrX;
		this.incrY = totalCellWidth + strokeWidth;
		this.numY = viewHeight/this.incrY;
		int rowCoverageX = this.numX*this.incrX;
		
		Log.i("bob", Integer.toString(this.numX));
		Log.i("bob", Integer.toString(this.numY));
		
		int graphicHeight = this.numY*this.incrY;
		
		this.offsetX = padding + strokeWidth/2 + (viewWidth-rowCoverageX)/2;
		this.offsetY = padding + strokeWidth/2 + (viewHeight-graphicHeight)/2;


		Rect bounds = new Rect();
		
		for (int i = 0; i < numX; ++i) {
			for (int j = 0; j < numY; ++j) {
				int posX, posY;
				posX = i*incrX + offsetX;
				posY = j*incrY + offsetY;
				
				bounds.left = posX;
				bounds.right = posX + blockSize;
				bounds.top = posY;
				bounds.bottom = posY + blockSize;
				
				StrokeAndFillDrawable drawable = null;
				if (shapeStr.equals("circle")) {
					drawable = new StrokeAndFillDrawable(ovalShape);
				} else if (shapeStr.equals("rect")) {
					drawable = new StrokeAndFillDrawable(rectShape);
				} else if (shapeStr.equals("round_rect")) {
					drawable = new StrokeAndFillDrawable(roundRectShape);
				} else if (shapeStr.equals("triangle")) {
					drawable = new StrokeAndFillDrawable(triangleShape);
				} else if (shapeStr.equals("hexagon")) {
					drawable = new StrokeAndFillDrawable(hexShape);
				} else if (shapeStr.equals("random")) {
					drawable = new StrokeAndFillDrawable( generateShape());
				}
				drawable.setBounds(bounds);
				drawable.getPaint().setColor(generateColor(true));
				drawable.setStrokeColor(generateColor(false), strokeWidth);
				shapes.add(drawable);

			}
		}
	}
	
	public void createHoneycomb(int w, int h) {
		if (w <= 0 || h <= 0)
			return;
		shapes.clear();
		this.viewWidth = w;
		this.viewHeight = h;
		int totalCellWidth = blockSize + padding * 2;
		
		// HANDLE THE Y AXIS PARAMS 
		float scaleY = (float) (Math.sqrt(3)/4.0);
		this.incrY = (int) (scaleY*(totalCellWidth + strokeWidth));
		
		int upperRowDelta = totalCellWidth - (2*this.incrY);
		
		int graphicHeight = 0;
		while (graphicHeight < viewHeight){
			graphicHeight += this.incrY;
		}
		
		if (graphicHeight > viewHeight){
			graphicHeight -= this.incrY;
		}

		//calc oddColNumY
		int oddColNumY = (graphicHeight/this.incrY) >> 1;	

		//calc evenColNumY
		graphicHeight -= this.incrY;
		int evenColNumY = (graphicHeight/this.incrY) >> 1;	
		
		graphicHeight += this.incrY;
		
		this.offsetY = padding + strokeWidth/2 + (viewHeight-graphicHeight)/2 - upperRowDelta/2;
		
		this.incrX = 3*(totalCellWidth + strokeWidth)/2;
		
		// HANDLE X AXIS PARAMS
		int graphicWidth = 0;
		while (graphicWidth < viewWidth){
			graphicWidth += this.incrX;
		}
		
		if (graphicWidth > viewWidth){
			graphicWidth -= this.incrX;
		}

		//calc evenColNumX
		int evenColNumX = (graphicWidth/this.incrX);	

		//calc oddColNumX
		graphicWidth = 0;
		while (graphicWidth < (viewWidth-totalCellWidth)){
			graphicWidth += this.incrX;
		}
		
		if (graphicWidth > (viewWidth-totalCellWidth)){
			graphicWidth -= this.incrX;
		}
		int oddColNumX = (graphicWidth/this.incrX);	
		oddColNumX += 1;
		
		if (evenColNumX >= oddColNumX){
			graphicWidth = this.incrX * evenColNumX + totalCellWidth/4;
			numX = evenColNumX;
		}else{
			graphicWidth = (this.incrX * (oddColNumX-1)) + totalCellWidth;
			numX = oddColNumX;
		}
				
		this.offsetX = padding + strokeWidth/2 + (viewWidth-graphicWidth)/2;

		Rect bounds = new Rect();
		
		// this draws 2 columns for each j offset by incrY
		for (int i = 0; i < numX; ++i) {
			boolean bail = false;
			if ((evenColNumX < oddColNumX) && (i == numX-1))
				bail = true;

			for (int j = 0; j < evenColNumY+oddColNumY; ++j) {
				int posX, posY;
				if (j%2 == 0 && !bail){
					posX = i*incrX + offsetX + this.incrX/2;
					posY = j*incrY + offsetY;
				} else if (j%2 == 0){
					continue;
				} else {
					posX = i*incrX + offsetX;
					posY = j*incrY + offsetY;
				}

				bounds.left = posX;
				bounds.right = posX + blockSize;
				bounds.top = posY;
				bounds.bottom = posY + blockSize;
				
				StrokeAndFillDrawable drawable = null;
				if (shapeStr.equals("circle")) {
					drawable = new StrokeAndFillDrawable(ovalShape);
				} else if (shapeStr.equals("rect")) {
					drawable = new StrokeAndFillDrawable(rectShape);
				} else if (shapeStr.equals("round_rect")) {
					drawable = new StrokeAndFillDrawable(roundRectShape);
				} else if (shapeStr.equals("triangle")) {
					drawable = new StrokeAndFillDrawable(triangleShape);
				} else if (shapeStr.equals("hexagon")) {
					drawable = new StrokeAndFillDrawable(hexShape);
				} else if (shapeStr.equals("random")) {
					drawable = new StrokeAndFillDrawable( generateShape());
				}
				drawable.setBounds(bounds);
				drawable.getPaint().setColor(generateColor(true));
				drawable.setStrokeColor(generateColor(false), strokeWidth);
				shapes.add(drawable);
			}
		}
	}

	private Shape generateShape() {
		switch (random.nextInt(4)) {
		case 0:
			return ovalShape;
		case 1:
			return rectShape;
		case 2:
			return roundRectShape;
		case 3:
			return hexShape;
		//case 4:
		//	return triangleShape;
		default:
			Log.e("GridView", "invalid shape enum");
			break;
		}
		return null;
	}


	private boolean updateColor(StrokeAndFillDrawable shape, boolean fill) {

		boolean thresholdHit = false;
		int c = shape.getPaint().getColor();
		int a = this.fillAlpha;
		
		//stroke
		if (!fill && shape.isEnableStroke()){
			c = shape.getStrokeColor();
			a = this.strokeAlpha;
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

		if (ColorGrid.this.colorRange.equals("All")){
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

		}else if (ColorGrid.this.colorRange.equals("Red")){
			if (r < threshold)
				r = threshold;
			if (r <= threshold){
				thresholdHit = true;
				c = generateColor(fill);
			}else
				c = Color.argb(a, r, g, b);
		}else if (ColorGrid.this.colorRange.equals("Green")){
			if (g < threshold)
				g = threshold;
			if (g <= threshold){
				thresholdHit = true;
				c = generateColor(fill);
			}else
				c = Color.argb(a, r, g, b);

		}else if (ColorGrid.this.colorRange.equals("Blue")){
			if (b < threshold)
				b = threshold;
			if (b <= threshold){
				c = generateColor(fill);
				thresholdHit = true;
			} else
				c = Color.argb(a, r, g, b);
		}else if (ColorGrid.this.colorRange.equals("Cyan")){
			if (g < threshold)
				g = threshold;
			if (b < threshold)
				b = threshold;
			if (g <= threshold && b <= threshold){
				c = generateColor(fill);
				thresholdHit = true;
			} else
				c = Color.argb(a, r, g, b);
		}else if (ColorGrid.this.colorRange.equals("Yellow")){
			if (r < threshold)
				r = threshold;
			if (g < threshold)
				g = threshold;
			if (r <= threshold && g <= threshold){
				c = generateColor(fill);
				thresholdHit = true;
			} else
				c = Color.argb(a, r, g, b);

		}else if (ColorGrid.this.colorRange.equals("Magenta")){
			if (r < threshold)
				r = threshold;
			if (b < threshold)
				b = threshold;
			if (r <= threshold && b <= threshold){
				c = generateColor(fill);
				thresholdHit = true;
			}else
				c = Color.argb(a, r, g, b);

		}else if (ColorGrid.this.colorRange.equals("Gray")){
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
		if (!fill && shape.isEnableStroke()){
			int w = shape.getStrokeWidth();
			shape.setStrokeColor(c, w);
		} else 
			shape.setFillColour(c);
		
		return thresholdHit;
	}
	
	private int generateColor(boolean fill) {
		int valA = this.fillAlpha;
		//stroke
		if (!fill){
			valA = this.strokeAlpha;
		}

		int valR = threshold + random.nextInt(256-threshold);
		int valG = threshold + random.nextInt(256-threshold);
		int valB = threshold + random.nextInt(256-threshold);

		int c = Color.WHITE;
		if (ColorGrid.this.colorRange.equals("All"))
			c = Color.argb(valA, valR, valG, valB);
		else if (ColorGrid.this.colorRange.equals("Red"))
			c = Color.argb(valA, valR, 0, 0);
		else if (ColorGrid.this.colorRange.equals("Green"))
			c = Color.argb(valA, 0, valG, 0);
		else if (ColorGrid.this.colorRange.equals("Blue"))
			c = Color.argb(valA, 0, 0, valB);
		else if (ColorGrid.this.colorRange.equals("Cyan"))
			c = Color.argb(valA, 0, valG, valB);
		else if (ColorGrid.this.colorRange.equals("Yellow"))
			c = Color.argb(valA, valR, valG, 0);
		else if (ColorGrid.this.colorRange.equals("Magenta"))
			c = Color.argb(valA, valR, 0, valB);
		else if (ColorGrid.this.colorRange.equals("Gray"))
			c = Color.argb(valA, valR, valR, valR);
		return c;
	}
	
	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
		float cornerRadius = blockSize / 4;
		float[] outerR = new float[] { cornerRadius, cornerRadius, cornerRadius,
				cornerRadius, cornerRadius, cornerRadius, cornerRadius,
				cornerRadius };
		roundRectShape = new RoundRectShape(
				outerR, null, null);
	

	}

	public void setColorRange(String colorRange) {
		this.colorRange = colorRange;
	}

	public void setDecayStep(int decayStep) {
		this.decay = decayStep;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public void setShape(String shape) {
		this.shapeStr = shape;	
	}

	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
	}
	
	public void draw(Canvas canvas) {
		for (ShapeDrawable shape : shapes){
			shape.getBounds();
			//Paint p = new Paint();
			//p.setColor(Color.BLUE);
			//canvas.drawRect(shape.getBounds(),p);
			shape.draw(canvas);
		}
	}
	
	public void setFillAlpha(int fillAlpha) {
		this.fillAlpha = fillAlpha;
	}
	
	public void setStrokeAlpha(int strokeAlpha) {
		this.strokeAlpha = strokeAlpha;
	}

}
