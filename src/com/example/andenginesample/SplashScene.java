package com.example.andenginesample;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.andenginesample.SceneManager.SceneType;

public class SplashScene extends BaseScene {

	private Sprite splash;
	private final static int SCREEN_WIDTH = 854;
	private final static int SCREEN_HEIGHT = 480;
	private static int SPLASH_WIDTH;
	private static int SPLASH_HEIGHT;
	AssetManager am;

	@Override
	public void createScene() {
		splash = new Sprite(0, 0, resourcesManager.splash_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};

		AssetManager am = ResourcesManager.getInstance().activity.getAssets();
		try {
			Bitmap bmp = BitmapFactory.decodeStream(am.open("gfx/splash.png"));
			SPLASH_HEIGHT = bmp.getHeight();
			SPLASH_WIDTH = bmp.getWidth();
		} catch (IOException e) {

			e.printStackTrace();
		}
		splash.setScale(1.0f);
		splash.setPosition(SCREEN_WIDTH / 2 - SPLASH_HEIGHT / 2, SCREEN_HEIGHT
				/ 2 - SPLASH_WIDTH / 2);
		attachChild(splash);
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return SceneType.SCENE_SPLASH;
	}

	@Override
	public void disposeScene() {
		splash.detachSelf();
		splash.dispose();
		this.detachSelf();
		this.dispose();
	}

}
