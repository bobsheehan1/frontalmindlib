package com.frontalmind;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.Message;

public class ColorBurstModel {
	private ColorGrid colorGrid;
	
	IViewUpdate view;
	private int  animationRate;
	private Timer timer, lifeTimer;
	private int viewWidth, viewHeight;
	private boolean enableDraw = false;
	
	android.media.ToneGenerator tg= new ToneGenerator(AudioManager.STREAM_DTMF, 60);
	

	Random random = new Random();

	private int birthRate = 1000;

	private int gridPercentAlive;

	private MediaPlayer mp;
	
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
		if (lifeTimer != null){
			lifeTimer.cancel();
			lifeTimer.purge();
			lifeTimer = null;
		}
		if (enable){
    		timer = new Timer();
    		timer.scheduleAtFixedRate(new AnimateTask(), 0, animationRate);
    		lifeTimer = new Timer();
    		lifeTimer.scheduleAtFixedRate(new ReincarnateTask(), 0, birthRate);
		}
	}
	
	public void createGrid() {
		colorGrid.createGrid(this.viewWidth, this.viewHeight);
		setGridAlivePercent(this.gridPercentAlive);
	}

	public void setRate(int rate) {
		this.animationRate =  rate;
		enableAnimation(true);
	}
	
	public void setBlockSize(int blockSize) {
		colorGrid.setBlockSize(blockSize);
		colorGrid.setGridAlivePercent(this.gridPercentAlive);
	}

	public void sizeChanged(int w, int h) {
		 this.viewWidth = w;
		 this.viewHeight = h;
		 colorGrid.createGrid(w,h);
		 setGridAlivePercent(this.gridPercentAlive);
	}

	public void draw(Canvas canvas) {
		colorGrid.draw(canvas);
	}
	
	class ReincarnateTask extends TimerTask {
		private Handler updateUI = new Handler() {
			@Override
			public void dispatchMessage(Message msg) {
				super.dispatchMessage(msg);
				if (colorGrid.destroy());
				{
//					int tone = random.nextInt(99);
//					switch ()
//					{
//					case 0:
//						tone = ToneGenerator.TONE_DTMF_0;
//						break;
//					case 1:
//						tone = ToneGenerator.TONE_DTMF_1;
//						break;
//					case 2:
//						tone = ToneGenerator.TONE_DTMF_2;
//						break;
//					case 3:
//						tone = ToneGenerator.TONE_DTMF_3;
//						break;
//					case 4:
//						tone = ToneGenerator.TONE_DTMF_4;
//						break;
//					case 5:
//						tone = ToneGenerator.TONE_DTMF_5;
//						break;
//					case 6:
//						tone = ToneGenerator.TONE_DTMF_6;
//						break;
//					case 7:
//						tone = ToneGenerator.TONE_DTMF_7;
//						break;
//					case 8:
//						tone = ToneGenerator.TONE_DTMF_8;
//						break;
//					case 9:
//						tone = ToneGenerator.TONE_DTMF_9;
//						break;
//					case 10:
//						tone = ToneGenerator.TONE_DTMF_A;
//						break;
//					case 11:
//						tone = ToneGenerator.TONE_DTMF_B;
//						break;
//					case 12:
//						tone = ToneGenerator.TONE_DTMF_C;
//						break;
//					case 13:
//						tone = ToneGenerator.TONE_DTMF_B;
//						break;
//					case 14:
//						tone = ToneGenerator.TONE_DTMF_P;
//						break;
//					case 15:
//						tone = ToneGenerator.TONE_DTMF_S;
//						break;
//					}
//					tg.startTone(tone, 50);
					  
//				    mp.start();
					colorGrid.reincarnate();
					ColorBurstModel.this.view.updateView();
				}
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

	public void setSoundEffects(MediaPlayer mp) {
		this.mp = mp;
	}
	   
	class AnimateTask extends TimerTask {
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
	
	public void setGridBirthRate(int birthRate) {
		this.birthRate = birthRate;
	}
	
	public void setGridAlivePercent(int percent){
		this.gridPercentAlive = percent;
		colorGrid.setGridAlivePercent(this.gridPercentAlive);

	}
}
