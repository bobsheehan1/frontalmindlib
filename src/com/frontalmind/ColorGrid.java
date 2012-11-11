package com.frontalmind;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;

public class ColorGrid {
	private int incrX, incrY, viewWidth, viewHeight, blockSize, 
	numX, numY, offsetX, offsetY, padding, strokeWidth;
	private Vector<StrokeAndFillDrawable> shapes;
	private String shapeStr;
	
	public int fillAlpha;
	public int strokeAlpha;
	public String borderColorRange;
	public String colorRange;
	public int decay, threshold;

	
	public ColorGrid() {
		padding = 2;
		shapeStr = "random";
		shapes = new Vector<StrokeAndFillDrawable>();

		setBlockSize(50);
		setStrokeWidth(2);
	}

	public void updateColors() {
		for (StrokeAndFillDrawable drawable : shapes) {

			// fill
			if (drawable.updateColor(true)) {
				if (shapeStr.equals("random")){
					drawable.setShape(ShapeFactory.generateRandomShape());
					drawable.updateColor(true);
				}
			}

			if (drawable.isEnableStroke() && drawable.updateColor(false)) {
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
				
				StrokeAndFillDrawable drawable = ShapeFactory.generateDrawable(this.shapeStr,
						this.colorRange,
						this.borderColorRange,
						this.fillAlpha,
						this.strokeAlpha,
						this.strokeWidth,
						this.threshold,
						this.decay);

				drawable.setBounds(bounds);
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
				StrokeAndFillDrawable drawable = ShapeFactory.generateDrawable(this.shapeStr,
						this.colorRange,
						this.borderColorRange,
						this.fillAlpha,
						this.strokeAlpha,
						this.strokeWidth,
						this.threshold,
						this.decay);		
				drawable.setBounds(bounds);
				shapes.add(drawable);
			}
		}
	}


	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
		ShapeFactory.setCornerRadius(blockSize/4);

	}

	public void setColorRange(String colorRange) {
		this.colorRange = colorRange;
		for (StrokeAndFillDrawable shape : shapes)
			shape.colorRange = colorRange;
	}

	public void setBorderColorRange(String colorRange) {
		this.borderColorRange = colorRange;
		for (StrokeAndFillDrawable shape : shapes)
			shape.borderColorRange = colorRange;
	}
	
	public void setDecayStep(int decayStep) {
		this.decay = decayStep;
		for (StrokeAndFillDrawable shape : shapes)
			shape.decay = decayStep;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
		for (StrokeAndFillDrawable shape : shapes)
			shape.threshold = threshold;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public void setShape(String shape) {
		this.shapeStr = shape;	
	}

	public void setStrokeWidth(int strokeWidth) {
		this.strokeWidth = strokeWidth;
		for (StrokeAndFillDrawable shape : shapes)
			shape.setStrokeWidth(strokeWidth);
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
		for (StrokeAndFillDrawable shape : shapes)
			shape.fillAlpha = fillAlpha;
	}
	
	public void setStrokeAlpha(int strokeAlpha) {
		this.strokeAlpha = strokeAlpha;
		for (StrokeAndFillDrawable shape : shapes)
			shape.strokeAlpha = strokeAlpha;
	}

}
