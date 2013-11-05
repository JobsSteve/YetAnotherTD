package com.example.andenginesample;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.widget.Toast;

public class GameActivity extends BaseGameActivity {
	private Camera mCamera;
	private ResourcesManager resourcesManager;
	private final static int SCREEN_WIDTH = 854;
	private final static int SCREEN_HEIGHT = 480;
	@Override
	public EngineOptions onCreateEngineOptions() {
		mCamera = new Camera(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						SCREEN_WIDTH, SCREEN_HEIGHT), this.mCamera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)	throws Exception {
		ResourcesManager.prepareManager(mEngine, this, mCamera,
				getVertexBufferObjectManager());
		resourcesManager = ResourcesManager.getInstance();
		pOnCreateResourcesCallback.onCreateResourcesFinished();

	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
		ResourcesManager.mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		  mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
		    {
		            public void onTimePassed(final TimerHandler pTimerHandler) 
		            {
		                mEngine.unregisterUpdateHandler(pTimerHandler);
		                // load menu resources, create menu scene
		                // set menu scene using scene manager
		                // disposeSplashScene();
		                SceneManager.getInstance().createMenuScene();
		            }
		    }));
		    pOnPopulateSceneCallback.onPopulateSceneFinished();

	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		// TODO Auto-generated method stub
		return new LimitedFPSEngine(pEngineOptions, 60);
	}
	
	@Override
	protected void onDestroy() {
		 super.onDestroy();
	        
		    if (this.isGameLoaded())
		    {
		        System.exit(0);    
		    }
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	        SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
	    }
	    return false; 
	}
	public void makeToast () {
		Toast.makeText(this, "qwe", Toast.LENGTH_SHORT).show();
	}
}

