package com.frontalmind;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;

public class ColorBurstModel {
	private ColorGrid colorGrid;
	
	IViewUpdate view;
	private int  animationRate;
	private Timer timer;
	private int viewWidth, viewHeight;
	private boolean enableDraw = false;
	
	public ColorBurstModel(IViewUpdate view){
		this.view = view;
		colorGrid = new ColorGrid();
		animationRate = 50;
	}
	
	public void enableAnimation(boolean enable){
		if (timer != null){
			timer.cancel();
			timer.purge();
			timer = null;
		}
		if (enable){
    		timer = new Timer();
    		timer.scheduleAtFixedRate(new ColorTask(), 0, animationRate);
    	}
	}
	
	public void createGrid() {
		colorGrid.createGrid(this.viewWidth, this.viewHeight);
	}

	public void setRate(int rate) {
		this.animationRate =  rate;
		enableAnimation(true);
	}
	
	public void setBlockSize(int blockSize) {
		colorGrid.setBlockSize(blockSize);
	}

	public void sizeChanged(int w, int h) {
		 this.viewWidth = w;
		 this.viewHeight = h;
		 colorGrid.createGrid(w,h);
	}

	public void draw(Canvas canvas) {
		colorGrid.draw(canvas);
	}
	   
	class ColorTask extends TimerTask {
		private Handler updateUI = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				super.dispatchMessage(msg);
				colorGrid.animate();
				//colorGrid.updateColors();
				ColorBurstModel.this.view.updateView();
			}	
		};

		@Override
		public void run() {
			try {
				updateUI.sendEmptyMessage(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	 };

	public void onTouch(){
		enableAnimation(enableDraw);
		enableDraw = !enableDraw;

	}
	
	public void setColorRange(String colorRange) {
		colorGrid.setColorRange(colorRange);
	}

	public void setDecayStep(int decayStep) {
		colorGrid.setDecayStep(decayStep);
	}

	public void setThreshold(int threshold) {
		colorGrid.setThreshold(threshold);
	}

	public void setPadding(int padding) {
		colorGrid.setPadding(padding);
	}

	public void setShape(String shape) {
		colorGrid.setShape(shape);
	}

	public void setStrokeWidth(int strokeWidth) {
		colorGrid.setStrokeWidth(strokeWidth);
	}

	public void setFillAlpha(int fillAlpha) {
		colorGrid.setFillAlpha(fillAlpha);
	}
	
	public void setStrokeAlpha(int strokeAlpha) {
		colorGrid.setStrokeAlpha(strokeAlpha);
	}

	public void setBorderColorRange(String borderColorRange) {
		colorGrid.setBorderColorRange(borderColorRange);
	}

}
