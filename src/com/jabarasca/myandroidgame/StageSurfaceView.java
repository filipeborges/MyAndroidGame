package com.jabarasca.myandroidgame;

import android.view.SurfaceView;
import android.content.Context;
import android.view.SurfaceHolder;
import android.util.AttributeSet;
import android.os.AsyncTask;
import android.graphics.Point;
import android.graphics.Paint;
import android.graphics.Canvas;

public class StageSurfaceView extends SurfaceView {
	
	private boolean surfaceIsValid = false;
	private Paint paint;
	private SurfaceHolder surfaceHolder;
	private SurfaceHolder.Callback callbacks = new SurfaceHolder.Callback() {
		
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			surfaceIsValid = false;
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			surfaceIsValid = true;
		}
		
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {}
	};
	
	public StageSurfaceView(Context context, AttributeSet attr) {
		super(context, attr);
		surfaceHolder = this.getHolder();
		surfaceHolder.addCallback(callbacks);
	}
	
	//Return true only if draws a Line on the screen.
	public boolean drawSingleLineStage(Point startPoint, Point endPoint, Paint paint) {
		if(paint == null || startPoint == null || endPoint == null) {
			return false;
		}else {
			if(startPoint.x < 0 || startPoint.y < 0 || endPoint.x < 0 || endPoint.y < 0) {
				return false;
			}
			if(startPoint.x == endPoint.x && startPoint.y == endPoint.y) {
				return false;
			}
		}
		
		final int startX = startPoint.x;
		final int startY = startPoint.y;
		final int endX = endPoint.x;
		final int endY = endPoint.y;
		this.paint = paint;
		
		DrawThread draw = new DrawThread();
		draw.execute(startX, startY, endX, endY);
		
		return true;
	}
	
	private class DrawThread extends AsyncTask<Integer, Void, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			while(!surfaceIsValid){}
			Canvas canvas = surfaceHolder.lockCanvas();
			canvas.drawLine(params[0], params[1], params[2], params[3], paint);
			surfaceHolder.unlockCanvasAndPost(canvas);
			return null;
		}
	}
	
}
