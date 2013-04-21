package com.frontalmind;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;

import com.frontalmind.shape.ShapeFactory;
import com.frontalmind.shape.StarShape;
import com.frontalmind.shape.behavior.AlphaBehavior;
import com.frontalmind.shape.behavior.MoveBehavior;
import com.frontalmind.shape.behavior.ScaleBehavior;

public class HeartModel {
	private Random random;
	private int  viewWidth, viewHeight;
	private Timer createTimer;
	private Timer animateTimer;
	private Timer spiralTimer;
	private boolean toggleAnimate = true;
	Paint paint = new Paint();
	RectF rect = new RectF();
	List<StrokeAndFillDrawable> drawables;
	
	private String shapeStr = "heart";
	private String colorRange = "All";
	private String borderColorRange = "All";
	private int maxNumber;
	private IViewUpdate starBurstView;
	
	public double azimuth = 0.0;
	
	public double azimuthInc = Math.PI/40.0;
	
	final double PI2 = 2.0*Math.PI;
	
	
	public HeartModel(IViewUpdate viewUpdate){
		this.starBurstView = viewUpdate;
		maxNumber = 100;
		paint.setStyle(Style.FILL_AND_STROKE);
		
		random = new Random();
		drawables = new LinkedList<StrokeAndFillDrawable>();
	}
	

	public void enableAnimation(boolean enable) {
		if (createTimer != null) {
			createTimer.cancel();
			createTimer.purge();
		}
		if (enable) {
			createTimer = new Timer();
			createTimer.scheduleAtFixedRate(new CreateTask(), 0, 500);
		}
		
		if (animateTimer != null) {
			animateTimer.cancel();
			animateTimer.purge();
		}
		
		if (enable) {
			animateTimer  = new Timer();
			animateTimer.scheduleAtFixedRate(new AnimateTask(), 0, 250);
		}
		
		if (spiralTimer != null) {
			spiralTimer.cancel();
			spiralTimer.purge();
		}
		
		if (enable) {
			spiralTimer  = new Timer();
			spiralTimer.scheduleAtFixedRate(new SpiralTask(), 0, 200);
		}
	}


	class AnimateTask extends TimerTask {
		private Handler updateUI = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				super.dispatchMessage(msg);
				if (HeartModel.this.viewWidth == 0)
					return;
				Iterator<StrokeAndFillDrawable> iter = drawables.iterator();
				while (iter.hasNext()){
					StrokeAndFillDrawable drawable = iter.next();
					drawable.animate();
				}

				HeartModel.this.starBurstView.updateView();
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
	}
	
	class SpiralTask extends TimerTask {
		private Handler updateUI = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				super.dispatchMessage(msg);
				if (HeartModel.this.viewWidth == 0)
					return;
				//Iterator<StrokeAndFillDrawable> iter = drawables.iterator();
//				int size = drawables.size();
//				if (size  > maxNumber) {
//					Iterator<StrokeAndFillDrawable> iter = drawables.iterator();
//					while (iter.hasNext()) {
//						StrokeAndFillDrawable drawable = iter.next();
//						if (drawable.getEnable()){
//							drawable.slowDeath(40);
//							break;
//						}
//						else
//							iter.remove();
//					}
//					size = drawables.size();
//				}

				HeartModel.this.starBurstView.updateView();
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
	}

	class CreateTask extends TimerTask {
		private Handler updateUI = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				super.dispatchMessage(msg);
		   		if (HeartModel.this.viewWidth == 0)
					 return;
				if (drawables.size()  > maxNumber) {
					Iterator<StrokeAndFillDrawable> iter = drawables.iterator();
					while (iter.hasNext()) {
						StrokeAndFillDrawable drawable = iter.next();
							if (drawable.scale >= 1.0f)
								iter.remove();
					}
				}
				
				synchronized (HeartModel.this) {
					HeartModel.this.azimuth += azimuthInc;
					if (HeartModel.this.azimuth > PI2)
						HeartModel.this.azimuth = HeartModel.this.azimuth - PI2;
				}


				//HeartModel.this.azimuth += azimuthInc;
				//if (HeartModel.this.azimuth > PI2)
				//	HeartModel.this.azimuth = HeartModel.this.azimuth - PI2;

				//if (drawables.size() < maxNumber) {

				int width = HeartModel.this.viewWidth;
				if (HeartModel.this.viewWidth > HeartModel.this.viewHeight)
					width = HeartModel.this.viewHeight/8;
					//if (width < 50)
					//	width = 50;
					int left = HeartModel.this.viewWidth/2 - width/2;
					int top = HeartModel.this.viewHeight/2 - width/2;

					int right = left + width;
					int bottom = top + width;

					StrokeAndFillDrawable shape = new StrokeAndFillDrawable();
					ShapeFactory.initializeDrawable(shape, HeartModel.this.shapeStr, colorRange, borderColorRange, 0);

					shape.setFillAlpha(255);
					//shape.addBehavior(new AlphaBehavior(5, 255, 255, 0, false, false));
					synchronized (HeartModel.this) {
						int tx = (int)(10.0*Math.cos(HeartModel.this.azimuth));
						int ty = (int)(10.0*Math.sin(HeartModel.this.azimuth));
						shape.addBehavior(new MoveBehavior(tx, ty, false));						
					}
					shape.scale =.25f;
					//shape.addBehavior(new ScaleBehavior(.25f, 1.0f, .01f, false, false));
	
					shape.setBounds(left, top, right, bottom);
					drawables.add(shape);
					
					//StrokeAndFillDrawable shape2 = new StrokeAndFillDrawable();
					//ShapeFactory.initializeDrawable(shape2, HeartModel.this.shapeStr, colorRange, borderColorRange, 0);

//					shape2.addBehavior(new AlphaBehavior(5, 255, 255, 0, false, false));
//					///shape2.addBehavior(new MoveBehavior(-tx, -ty, false));
//					shape2.scale =.25f;
//					shape2.addBehavior(new ScaleBehavior(.25f, 1.5f, .02f, false, false));
//	
//					shape2.setBounds(left, top, right, bottom);
//					drawables.add(shape2);
				//}



				HeartModel.this.starBurstView.updateView();
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
	}

	public void onTouch() {
		toggleAnimate = !toggleAnimate;
		enableAnimation(toggleAnimate);
	}


	public void sizeChanged(int w, int h) {
		this.viewHeight = h;
		this.viewWidth = w;
		enableAnimation(true);
	}

	public void draw(Canvas canvas) {
		for (int i = 0; i < drawables.size(); ++i) {
			StrokeAndFillDrawable drawable = drawables.get(i);
			if (drawable.rotation != 0 || drawable.scale != 1.0f){
				canvas.save();
				canvas.translate(drawable.getBounds().exactCenterX(), drawable.getBounds().exactCenterY());
				if (drawable.rotation != 0)
					canvas.rotate(drawable.rotation);
				if (drawable.scale != 1.0f)
					canvas.scale(drawable.scale, drawable.scale);
				canvas.translate(-drawable.getBounds().exactCenterX(), -drawable.getBounds().exactCenterY());
				drawable.draw(canvas);
				canvas.restore();
			}
			else
				drawable.draw(canvas);
		}
	}

	public void setShape(String shape) {
		this.shapeStr = shape;
	}

	public void setStarColor(String starColor) {
		colorRange = starColor;
		for (StrokeAndFillDrawable drawable : drawables) {
			drawable.colorRange = starColor;
			if (drawable.getShape() instanceof StarShape)
				drawable.updateShader();
		}
	}


	public void setStarNumber(int starNumber) {
		maxNumber = starNumber;
	}

}
