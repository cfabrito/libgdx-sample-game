package com.mygdx.tutorial;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.tutorial.Tutorial;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		// Disable accelerometer and compass for saving battery life
		config.useAccelerometer = false;
		config.useCompass = false;
		initialize(new Tutorial(), config);
	}
}
