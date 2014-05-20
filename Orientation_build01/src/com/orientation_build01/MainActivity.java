/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orientation_build01;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	private GLSurfaceView mGLView;
	private LinearLayout container;
	private Button forwardButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// request full screen
		requestNoTitleFullScreen();
		
		setContentView(R.layout.activity_main);
		findViews();
	}

	private void findViews() {
		// Create a GLSurfaceView instance and set it
				// as the ContentView for this Activity.
		mGLView = new MyGLSurfaceView(this);
		forwardButton = (Button) findViewById(R.id.button_forward);
		forwardButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				((MyGLSurfaceView) mGLView).moveForward();
			}});
		container = (LinearLayout) findViewById(R.id.container);
		container.addView(mGLView);
	}

	private void requestNoTitleFullScreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	protected void onPause() {
		// The following call pauses the rendering thread.
		// If your OpenGL application is memory intensive,
		// you should consider de-allocating objects that
		// consume significant memory here.
		super.onPause();
		mGLView.onPause();
	}

	@Override
	protected void onResume() {
		// The following call resumes a paused rendering thread.
		// If you de-allocated graphic objects for onPause()
		// this is a good place to re-allocate them.
		super.onResume();
		mGLView.onResume();
	}

}