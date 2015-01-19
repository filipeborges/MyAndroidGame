package com.jabarasca.myandroidgame;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.ViewTreeObserver;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.util.AttributeSet;

public class StageSurfaceView extends SurfaceView {
	private boolean surfaceIsValid = false;
	private Bitmap stageBitmap = null;
	private SurfaceHolder surfaceHolder;
	
	public StageSurfaceView(Context context, AttributeSet attr) {
		super(context, attr);
		surfaceHolder = this.getHolder();
		
		surfaceHolder.addCallback(new SurfaceHolder.Callback() {
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
		});
		
		this.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				setupStageBitmap();
				return true;
			}
		});
	}//End of Constructor.
	
	//Return true only if draws a Line on the screen.
	public boolean drawSingleLine(Point startPoint, Point endPoint, Paint paint) {			
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
		
		DrawStageTask drawTask = new DrawStageTask();
		drawTask.setupDrawSingleLine(startX, startY, endX, endY, paint);
		new Thread(drawTask).start();
		
		return true;
	}//End drawSingleLine.
	
	//Return true only if draws a Rectangle or Square on the screen.
	public boolean drawSingleRectangle(int left, int top, int right, int bottom, Paint paint) {
		//Verificar se as coordenadas estão corretas.
		if(left == right || top == bottom || paint == null || left < 0 || top < 0 || right < 0 || bottom < 0) {
			return false;
		}
		
		DrawStageTask drawTask = new DrawStageTask();
		drawTask.setupDrawSingleRectangle(left, top, right, bottom, paint);
		new Thread(drawTask).start();
		
		return true;
	}//End drawSingleRectagle.
	
	private void setupStageBitmap() {
		if(stageBitmap == null) {
			stageBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
		}
	}
	
	private class DrawStageTask implements Runnable {
		private final int DRAW_SINGLE_LINE = 1;
		private int startX, startY, endX, endY;
		private final int DRAW_SINGLE_RECTANGLE = 2;
		private int left, top, right, bottom;
		private int mode = 0;
		private Paint paint;
		
		public void setupDrawSingleLine(int startX, int startY, int endX, int endY, Paint paint) {
			this.startX = startX;
			this.startY = startY;
			this.endX = endX;
			this.endY = endY;
			this.paint = paint;
			this.mode = DRAW_SINGLE_LINE;
		}
		
		public void setupDrawSingleRectangle(int left, int top, int right, int bottom, Paint paint) {
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
			this.paint = paint;
			this.mode = DRAW_SINGLE_RECTANGLE;
		}
		
		@Override
		public void run() {
			while(!surfaceIsValid){}
			switch(mode) {
				case DRAW_SINGLE_LINE:
					this.drawSingleLine();
					break;
				case DRAW_SINGLE_RECTANGLE:
					this.drawSingleRectangle();
					break;
			}
		}
		
		private void drawSingleRectangle() {
			Rect square = new Rect(left, top, right, bottom);
			Canvas canvas = new Canvas(stageBitmap);
			canvas.drawRect(square, paint);
			canvas = surfaceHolder.lockCanvas();
			canvas.drawBitmap(stageBitmap, 0, 0, null);
			surfaceHolder.unlockCanvasAndPost(canvas);
		}
		
		private void drawSingleLine() {
			Canvas canvas = new Canvas(stageBitmap);
			canvas.drawLine(startX, startY, endX, endY, paint);
			canvas = surfaceHolder.lockCanvas();	
			canvas.drawBitmap(stageBitmap, 0, 0, null);
			surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}//End of DrawStageTask.
}
