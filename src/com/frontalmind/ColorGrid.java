package com.frontalmind;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.frontalmind.shape.HeartShape;
import com.frontalmind.shape.ShapeFactory;
import com.frontalmind.shape.StarShape;
import com.frontalmind.shape.behavior.AlphaBehavior;
import com.frontalmind.shape.behavior.RotateBehavior;
import com.frontalmind.shape.behavior.ScaleBehavior;

public class ColorGrid {
	private int incrX, incrY, viewWidth, viewHeight, blockSize, 
	numX, numY, offsetX, offsetY, padding, strokeWidth;
	private Vector<StrokeAndFillDrawable> drawables;
	private String shapeStr;
	
	public int fillAlpha;
	public int strokeAlpha;
	public String borderColorRange;
	public String colorRange;
	public int alphaDecay, threshold;

	public int minAliveSize = 20;
	
	public ColorGrid() {
		padding = 2;
		shapeStr = "random";
		drawables = new Vector<StrokeAndFillDrawable>();

		setBlockSize(50);
		setStrokeWidth(2);
	}
	
	public void createGrid(int w, int h) {
		if (shapeStr.equals("hexagon")){
			createHoneycomb(w, h);
			return;
		}
		
		if (w <= 0 || h <= 0)
			return;
		drawables.clear();
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
				
				StrokeAndFillDrawable drawable = new StrokeAndFillDrawable();
				drawable.setBounds(bounds);
				drawables.add(drawable);
			}
		}
	}


	
	public void createHoneycomb(int w, int h) {
		if (w <= 0 || h <= 0)
			return;
		drawables.clear();
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
		
		this.numY = evenColNumY+oddColNumY;
		// this draws 2 columns for each j offset by incrY
		for (int i = 0; i < numX; ++i) {
			boolean bail = false;
			if ((evenColNumX < oddColNumX) && (i == numX-1))
				bail = true;

			for (int j = 0; j < this.numY; ++j) {
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
				StrokeAndFillDrawable drawable = new StrokeAndFillDrawable();
				//ShapeFactory.initializeDrawable(drawable, this.shapeStr, colorRange, borderColorRange, strokeWidth);		
	
				//addBehaviors(drawable);

				drawable.setBounds(bounds);
				drawables.add(drawable);
			}
		}
	}


	private void addBehaviors(StrokeAndFillDrawable drawable) {
		drawable.clearBehaviors();
		if (shapeStr.equals("hexagon")){
			drawable.addBehavior(new AlphaBehavior(this.alphaDecay, this.fillAlpha, this.strokeAlpha, this.threshold, true, true));
		} else {
			drawable.addBehavior(new AlphaBehavior(this.alphaDecay, this.fillAlpha, this.strokeAlpha, this.threshold, true, true));
			if (drawable.getShape() instanceof StarShape)
				drawable.addBehavior(new RotateBehavior(5, true));
			if (drawable.getShape() instanceof HeartShape)
				drawable.addBehavior(new ScaleBehavior(.75f, 1.25f, .05f, true, true));
		}		
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
		ShapeFactory.setCornerRadius(blockSize/4);
	}

	public void setColorRange(String colorRange) {
		this.colorRange = colorRange;
		for (StrokeAndFillDrawable shape : drawables)
			shape.colorRange = colorRange;
	}

	public void setBorderColorRange(String colorRange) {
		this.borderColorRange = colorRange;
		for (StrokeAndFillDrawable shape : drawables)
			shape.borderColorRange = colorRange;
	}
	
	public void setDecayStep(int decayStep) {
		this.alphaDecay = decayStep;
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
		for (StrokeAndFillDrawable shape : drawables)
			shape.setStrokeWidth(strokeWidth);
	}
	
	public void draw(Canvas canvas) {
		for (StrokeAndFillDrawable drawable : drawables){
			if (!drawable.getEnable())
				continue;
			if (drawable.rotation != 0 || drawable.scale != 1.0f){
				canvas.save();
				if (drawable.rotation != 0)
					canvas.rotate(drawable.rotation, drawable.getBounds().exactCenterX(), drawable.getBounds().exactCenterY());
				if (drawable.scale != 1.0f)
					canvas.scale(drawable.scale, drawable.scale, drawable.getBounds().exactCenterX(), drawable.getBounds().exactCenterY());
				drawable.draw(canvas);
				canvas.restore();
			}
			else
				drawable.draw(canvas);
				
		}
	}
	
	public void setFillAlpha(int fillAlpha) {
		this.fillAlpha = fillAlpha;
		for (StrokeAndFillDrawable shape : drawables)
			shape.setFillAlpha(fillAlpha);
	}
	
	public void setStrokeAlpha(int strokeAlpha) {
		this.strokeAlpha = strokeAlpha;
		for (StrokeAndFillDrawable shape : drawables)
			shape.setStrokeAlpha(strokeAlpha);
	}

	public void animate() {
		for (StrokeAndFillDrawable drawable : drawables)
			drawable.animate();
	}

	public void reincarnate() {
		synchronized (this){
			if (drawables.size() == 0)
				return;
			List<StrokeAndFillDrawable> dead = new ArrayList<StrokeAndFillDrawable>();
			for (StrokeAndFillDrawable drawable : drawables)
				if (drawable.getShape() == null)
					dead.add(drawable);

			if (dead.size() == 0)
				return;
			
			if (drawables.size() - dead.size() > minAliveSize)
				return;
			//randomly create
			Random random = new Random();
			int index = random.nextInt(dead.size());
			StrokeAndFillDrawable reincarnated = dead.get(index);
			reincarnated.rotation = 0;
			reincarnated.scale = 1.0f;
			reincarnated.resetShape();
			reincarnated.clearBehaviors();

			ShapeFactory.initializeDrawable(reincarnated, shapeStr, colorRange, borderColorRange, strokeWidth);
			addBehaviors(reincarnated);
			reincarnated.setEnable(true);
			
			// HACK! to get shaderfactory on Drawable to reset color gradient for on resize
			if (reincarnated.getShape() instanceof StarShape || reincarnated.getShape() instanceof HeartShape)
			{
				Rect bounds = reincarnated.getBounds();
				Rect bounds2 = new Rect(bounds);
				bounds2.bottom -= 1;
				reincarnated.setBounds(bounds2);
				bounds2.bottom += 1;
				reincarnated.setBounds(bounds2);
			}

		}
	}
	
	public boolean destroy() {
		synchronized (this){
			if (drawables.size() == 0)
				return false;
			List<StrokeAndFillDrawable> alive = new ArrayList<StrokeAndFillDrawable>();
			for (StrokeAndFillDrawable drawable : drawables)
				if (drawable.getShape() != null)
					alive.add(drawable);

			if (alive.size() == 0 || alive.size() < minAliveSize)
				return false;
			//randomly destroy
			Random random = new Random();
			int index = random.nextInt(alive.size());
			StrokeAndFillDrawable aliveDrawable = alive.get(index);
			aliveDrawable.slowDeath(this.alphaDecay);

			return alive.size() < minAliveSize;
		}

	}

	public void setGridAlivePercent(int percent) {
		minAliveSize = (int) ((float)drawables.size() *((float)percent)/100.0f);
	}

}
