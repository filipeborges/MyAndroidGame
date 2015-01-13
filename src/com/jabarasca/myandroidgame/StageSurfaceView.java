package com.jabarasca.myandroidgame;

import android.view.SurfaceView;
import android.content.Context;
import android.view.SurfaceHolder;
import android.util.AttributeSet;
import android.os.AsyncTask;
import android.graphics.Point;
import android.graphics.Paint;
import android.graphics.Canvas;

/*Desenho anterior não esta sendo preservado na superficie.*/
/*Usando o mesmo Paint, na hora que outro esta desenhando.*/
/*Tentar usar um semáforo pra sincronizar as operações.*/

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
		
		DrawThread draw = new DrawThread(DrawThread.DRAW_SINGLE_LINE_STAGE);
		draw.execute(startX, startY, endX, endY);
		
		return true;
	}
	
	public boolean drawObjectInLine(Point startPoint, Point endPoint, Paint paint) {
		final int smallerX, smallerY, biggerX, biggerY;
		
		if(startPoint.x > endPoint.x) {
			biggerX = startPoint.x;
			smallerX = endPoint.y;
		}
		else {
			biggerX = endPoint.x;
			smallerX = startPoint.x;
		}
		
		if(startPoint.y > endPoint.y) {
			biggerY = startPoint.y;
			smallerY = endPoint.y;
		}
		else {
			biggerY = endPoint.y;
			smallerY = startPoint.y;
		}
		
		this.paint = paint; 
		
		DrawThread draw = new DrawThread(DrawThread.DRAW_OBJECT_IN_LINE);
		draw.execute(smallerX, smallerY, biggerX, biggerY);
		
		return true;
	}
	
	private class DrawThread extends AsyncTask<Integer, Void, Void> {
		
		public static final int DRAW_SINGLE_LINE_STAGE = 1;
		public static final int DRAW_OBJECT_IN_LINE = 2;
		private int mode;
		
		public DrawThread(int mode) {
			this.mode = mode;
		}
		
		@Override
		protected Void doInBackground(Integer... params) {
			while(!surfaceIsValid){}
			
			switch(mode) {
				case DRAW_SINGLE_LINE_STAGE:
					this.drawSingleLine(params[0], params[1], params[2], params[3]);
					break;
				case DRAW_OBJECT_IN_LINE:
					this.drawObjectInLine(params[0], params[1], params[2], params[3]);
					break;
			}
			
			return null;
		}
		
		private void drawSingleLine(int startX, int startY, int endX, int endY) {
			Canvas canvas = surfaceHolder.lockCanvas();
			canvas.drawLine(startX, startY, endX, endY, paint);
			surfaceHolder.unlockCanvasAndPost(canvas);
		}
		
		//Jogar public para fins de teste??
		private void drawObjectInLine(float smallerX, float smallerY, float biggerX, float biggerY) {
			final float tanAlpha = (biggerY - smallerY)/(biggerX - smallerX);
			
			for(float x = smallerX; x <= biggerX; x += 1) {
				Canvas canvas = surfaceHolder.lockCanvas();
				float y = (x - smallerX)*tanAlpha + smallerY;
				canvas.drawPoint(x, y, paint);
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
}
