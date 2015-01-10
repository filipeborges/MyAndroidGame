package com.jabarasca.myandroidgame;

import com.jabarasca.myandroidgame.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.view.animation.AnimationUtils;
import android.view.View.OnClickListener;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends Activity {

	ImageView touchStartImgView;
	Context context;
	
	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(context, StageActivity.class);
			context.startActivity(intent);
		}	
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);
		context = this;
		
		touchStartImgView = (ImageView)findViewById(R.id.mainTouchStartImgView);
		touchStartImgView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_screen_animation));
		touchStartImgView.setOnClickListener(clickListener);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
