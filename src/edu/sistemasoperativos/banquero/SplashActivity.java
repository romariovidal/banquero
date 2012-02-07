package edu.sistemasoperativos.banquero;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SplashActivity extends Activity implements AnimationListener {
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.splash);
	        
	        TextView textTitulo = (TextView) this.findViewById(R.id.textViewTitulo);
	        Animation fade1 = AnimationUtils.loadAnimation(this, R.anim.fade_in_banquero);
	        textTitulo.startAnimation(fade1);
	        TextView textVersion = (TextView) this.findViewById(R.id.textViewVersion);
	        Animation fade2 = AnimationUtils.loadAnimation(this, R.anim.fade_in2_banquero);
	        textVersion.startAnimation(fade2);
	        
	        fade2.setAnimationListener(this);
	        
	 }

	public void onAnimationEnd(Animation animation) {
		Intent intent = new Intent(this,MenuActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}

	public void onAnimationRepeat(Animation animation) {
		
	}

	public void onAnimationStart(Animation animation) {
	}

}
