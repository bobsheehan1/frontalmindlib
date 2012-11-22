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
import com.frontalmind.shape.StrokeAndFillDrawable;
import com.frontalmind.shape.behavior.AlphaBehavior;
import com.frontalmind.shape.behavior.MoveBehavior;
import com.frontalmind.shape.behavior.RotateBehavior;

public class StarBurstModel {
	private Random random;
	private int  spinRate, moveRate, viewWidth, viewHeight;
	private Timer createTimer;
	private Timer moveTimer;
	private boolean toggleAnimate = true;
	String shape = "star";
	Paint paint = new Paint();
	RectF rect = new RectF();
	List<StrokeAndFillDrawable> drawables;
	
	private String shapeStr = "star";
	private String colorRange = "All";
	private String borderColorRange = "All";
	private int fillAlpha = 0;
	private int strokeAlpha = 255;
	private int strokeWidth = 2;
	private int threshold = 0;
	private int decay = 4;
	private int maxNumber;
	private IViewUpdate starBurstView;
	
	public StarBurstModel(IViewUpdate viewUpdate){
		this.starBurstView = viewUpdate;
		decay = 8;
		threshold = 0;
		maxNumber = 100;
		paint.setStyle(Style.FILL_AND_STROKE);
		spinRate = 1;
		moveRate = 1;
		
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
			moveTimer.scheduleAtFixedRate(new MoveTask(), 0, 100);
		}
	}


	class MoveTask extends TimerTask {
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

				if (drawables.size() < maxNumber) {

					int left = random.nextInt(StarBurstModel.this.viewWidth);
					int top = random.nextInt(StarBurstModel.this.viewHeight);
					int width = random
							.nextInt(StarBurstModel.this.viewWidth / 4);
					int height = random
							.nextInt(StarBurstModel.this.viewHeight / 4);
					if (width < 10)
						width = 10;
					if (height < 10)
						height = 10;

					int right = left + width;
					int bottom = top + height;

					StrokeAndFillDrawable shape = ShapeFactory
							.generateDrawable(StarBurstModel.this.shapeStr,
									StarBurstModel.this.colorRange,
									StarBurstModel.this.borderColorRange,
									0);
					shape.addBehavior(new AlphaBehavior(5, 255, 255, 0, false, false));
					shape.addBehavior(new MoveBehavior(5,5, true));
					shape.addBehavior(new RotateBehavior(10, true));
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
			if (drawable.rotation != 0){
				canvas.save();
				canvas.rotate(drawable.rotation, drawable.getBounds().exactCenterX(), drawable.getBounds().exactCenterY());
				drawable.draw(canvas);
				canvas.restore();
			} else {
				drawable.draw(canvas);
				
			}
		}
	}

	public void setColorRange(String colorRange) {
		this.colorRange = colorRange;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}


	public void setStarColor(String starColor) {
		colorRange = starColor;
		for (StrokeAndFillDrawable drawable : drawables) {
			drawable.colorRange = starColor;
		}
	}


	public void setStarNumber(int starNumber) {
		maxNumber = starNumber;
	}


	public void setSpinRate(int spinRate) {
		this.spinRate = spinRate;
		for (StrokeAndFillDrawable drawable : drawables) {
		//	drawable.setRotationIncMax(spinRate);
		}
	}

	public void setMoveRate(int moveRate) {
		this.moveRate = moveRate;

		for (StrokeAndFillDrawable drawable : drawables) {
		//	drawable.setMoveIncMax(moveRate);
		}
	};

}
