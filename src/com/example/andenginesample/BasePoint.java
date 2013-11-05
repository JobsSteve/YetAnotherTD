package com.example.andenginesample;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.HorizontalAlign;

import android.util.Log;

public class BasePoint extends Sprite {
	// texture constants
	public static final ITextureRegion TEXTURE = ResourcesManager.TEXTURE_BASEPOINT;

	// globals
	public float mCenterX;
	public float mCenterY;
	private float pX;
	private float pY;
	private BaseScene mScene;
	public Sprite iconTowerInfantry;
	public Sprite iconTowerMagic;
	public Tower mTower;
	Text tt;

	// public Tower mCurrentTower = null;

	public BasePoint(float x, float y, BaseScene scene) {
		super(x - (TEXTURE.getWidth() / 2), y - (TEXTURE.getHeight() / 2),
				TEXTURE, ResourcesManager.getInstance().vbom);
		mScene = scene;
		// setZIndex(100);
		pX = x;
		pY = y;
		mCenterX = getX() + (TEXTURE.getWidth() / 2);
		mCenterY = getY() + (TEXTURE.getHeight() / 2);
		mScene.attachChild(this);
		mScene.registerTouchArea(this);
		Log.d("GAME", "Init" + String.valueOf(pX) + " " + String.valueOf(pY));
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		mScene.detachChild(tt);
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			Log.e("GAME", "BasePoint OnTouch");
			GameScene.unselectPoints();
			Log.d("GAME",
					"inf tower:" + String.valueOf(pX) + " "
							+ String.valueOf(pY));
			iconTowerInfantry = new Sprite(getX() - 50, getY() - 50,
					ResourcesManager.TEXTURE_TOWER_ICON_INFANTRY,
					ResourcesManager.getInstance().vbom) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
						// DeletePoint();
						if (ResourcesManager.mCoins >= 500) {
							Log.d("GAME", "inf tower:" + String.valueOf(pX)
									+ " " + String.valueOf(pY));
							mScene.unregisterTouchArea(getInstance());
							deleteIcons();
							unregisterIcons();

							Log.d("GAME",
									String.valueOf(ResourcesManager.mCoins));
							mTower = new Tower(pX - 41, pY - 40,
									ResourcesManager.TEXTURE_TOWER_INFANTRY,
									mScene, getInstance());
							mScene.attachChild(mTower);
						} else {
							tt = new Text(400, 400, ResourcesManager.font, "Need money 500 :(",
									new TextOptions(HorizontalAlign.LEFT), ResourcesManager.getInstance().vbom);
							mScene.attachChild(tt);
							deleteIcons();
							unregisterIcons();
						}
					}
					return true;
				}
			};
			mScene.attachChild(iconTowerInfantry);
			mScene.registerTouchArea(iconTowerInfantry);
			iconTowerMagic = new Sprite(getX() + 75, getY() - 50,
					ResourcesManager.TEXTURE_TOWER_ICON_MAGIC,
					ResourcesManager.getInstance().vbom) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
						if (ResourcesManager.mCoins >= 500) {
							mScene.unregisterTouchArea(getInstance());
							deleteIcons();
							unregisterIcons();
							Log.d("GAME",
									"mag tower before:" + String.valueOf(pX)
											+ " " + String.valueOf(pY));
							mTower = new Tower(pX - 41, pY - 23,
									ResourcesManager.TEXTURE_TOWER_MAGIC,
									mScene, getInstance());
							mScene.attachChild(mTower);
							Log.d("GAME", "mag tower:" + String.valueOf(pX)
									+ " " + String.valueOf(pY));
						} else {
							tt = new Text(400, 400, ResourcesManager.font, "Need money 500 :(",
									new TextOptions(HorizontalAlign.LEFT), ResourcesManager.getInstance().vbom);
							mScene.attachChild(tt);
							deleteIcons();
							unregisterIcons();
						}
					}
					return true;
				}
			};
			mScene.attachChild(iconTowerMagic);
			mScene.registerTouchArea(iconTowerMagic);
		}
		return true;
	}

	public void DeletePoint() {
		this.detachSelf();
		// this.dispose();
		mScene.unregisterTouchArea(this);
	}

	public void deleteIcons() {
		if (iconTowerInfantry != null) {
			iconTowerInfantry.detachSelf();
			iconTowerMagic.detachSelf();
		}
	}

	public void unregisterIcons() {
		if (iconTowerInfantry != null) {
			mScene.unregisterTouchArea(iconTowerInfantry);
			mScene.unregisterTouchArea(iconTowerMagic);
		}
	}

	public void restorePoint() {

	}

	public BasePoint getInstance() {
		return this;
	}

	public Tower getTower() {
		return mTower;
	}

}