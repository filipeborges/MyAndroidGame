package com.jabarasca.myandroidgame;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Point;
import android.graphics.Paint;
import android.graphics.Color;

public class StageActivity extends Activity {

	StageSurfaceView stageSurfaceView;
	Point startPoint;
	Point endPoint;
	Paint paint;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stage_screen);
		
		stageSurfaceView = (StageSurfaceView)findViewById(R.id.stageSurfaceView);
		startPoint = this.createPoint(0, 0);
		endPoint = this.createPoint(this.getWindowManager().getDefaultDisplay().getWidth(), this.getWindowManager().getDefaultDisplay().getHeight());
		paint = this.createPaint(Color.RED, (float)1.8);
		stageSurfaceView.drawSingleLine(startPoint, endPoint, paint);
		
		paint = this.createPaint(Color.GREEN, (float)1.8);
		startPoint = this.createPoint(0, this.getWindowManager().getDefaultDisplay().getHeight()/2);
		endPoint = this.createPoint(this.getWindowManager().getDefaultDisplay().getWidth(), this.getWindowManager().getDefaultDisplay().getHeight()/2);
		stageSurfaceView.drawSingleLine(startPoint, endPoint, paint);
		
		stageSurfaceView.drawSingleRectangle(10, 10, 60, 60, paint);
	}
	
	private Point createPoint(int x, int y) {
		return new Point(x, y);
	}
	
	private Paint createPaint(int color, float strokeWidth) {
		Paint paint = new Paint();
		paint.setColor(color);
		paint.setStrokeWidth(strokeWidth);
		return paint;
	}
	
}
