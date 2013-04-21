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

import com.frontalmind.shape.HeartShape;
import com.frontalmind.shape.ShapeFactory;
import com.frontalmind.shape.StarShape;
import com.frontalmind.shape.behavior.AlphaBehavior;
import com.frontalmind.shape.behavior.MoveBehavior;
import com.frontalmind.shape.behavior.RotateBehavior;
import com.frontalmind.shape.behavior.ScaleBehavior;

public class StarBurstModel {
	private Random random;
	private int  viewWidth, viewHeight;
	private Timer createTimer;
	private Timer moveTimer;
	private boolean toggleAnimate = true;
	Paint paint = new Paint();
	RectF rect = new RectF();
	List<StrokeAndFillDrawable> drawables;
	
	private String shapeStr = "star";
	private String colorRange = "All";
	private String borderColorRange = "All";
	private int maxNumber;
	private IViewUpdate starBurstView;
	
	public StarBurstModel(IViewUpdate viewUpdate){
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
			createTimer.scheduleAtFixedRate(new CreateTask(), 0, 1000);
		}
		
		if (moveTimer != null) {
			moveTimer.cancel();
			moveTimer.purge();
		}
		if (enable) {
			moveTimer  = new Timer();
			moveTimer.scheduleAtFixedRate(new AnimateTask(), 0, 50);
		}
	}


	class AnimateTask extends TimerTask {
		private Handler updateUI = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				super.dispatchMessage(msg);
				if (StarBurstModel.this.viewWidth == 0)
					return;
				Iterator<StrokeAndFillDrawable> iter = drawables.iterator();
				while (iter.hasNext()){
					StrokeAndFillDrawable drawable = iter.next();
					drawable.animate();
				}

				StarBurstModel.this.starBurstView.updateView();
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
				if (StarBurstModel.this.viewWidth == 0)
					return;

				while (drawables.size() < maxNumber) {

					int left = random.nextInt(StarBurstModel.this.viewWidth);
					int top = random.nextInt(StarBurstModel.this.viewHeight);
					int width = random
							.nextInt(StarBurstModel.this.viewWidth / 3);
					if (width < 50)
						width = 50;

					int right = left + width;
					int bottom = top + width;

					StrokeAndFillDrawable shape = new StrokeAndFillDrawable();
					ShapeFactory.initializeDrawable(shape, StarBurstModel.this.shapeStr, colorRange, borderColorRange, 0);

					shape.addBehavior(new AlphaBehavior(5, 255, 255, 0, false, false));
					shape.addBehavior(new MoveBehavior(4,4, true));
					if (shape.getShape() instanceof HeartShape){
						shape.addBehavior(new ScaleBehavior(.85f, 1.15f, .05f, true, true));
					} else {
						shape.addBehavior(new RotateBehavior(1, true));
						shape.scale =.1f;
						shape.addBehavior(new ScaleBehavior(.1f, 1.0f, .01f, false, false));
					}
					shape.setBounds(left, top, right, bottom);
					drawables.add(shape);
				}

				// remove if stars have left the window
				Iterator<StrokeAndFillDrawable> iter = drawables.iterator();
				while (iter.hasNext()) {
					StrokeAndFillDrawable drawable = iter.next();
					if (!drawable.getBounds().intersects(0, 0,
							StarBurstModel.this.viewWidth,
							StarBurstModel.this.viewHeight))
						iter.remove();
				}

				StarBurstModel.this.starBurstView.updateView();
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
