package com.example.andenginesample;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.util.Log;

public class Tower extends Sprite {

	public static final int TOWER_TEST = 0;
	public static final int TOWER_SLOW = 1;
	public static final int TOWER_FIRE = 2;
	

	public static final int SCAN_FIRST = 0;
	public static final int SCAN_LAST = 1;

	private float pX;
	private float pY;
	public float mRange;
	public int mDelay;
	private Sprite mTower;
	private BaseScene mScene;
	private BasePoint parent;
	private Sprite iconOptionSell, iconOptionUpgrade;
	public int mScanMethod;
	public boolean mActive = true;
	public Enemy mTarget;
	private Thread mScanThread;

	public Tower(float pX, float pY, ITextureRegion texture, BaseScene scene,
			BasePoint bp) {
		super(pX, pY, texture, ResourcesManager.getInstance().vbom);
		Log.d("GAME", "Tower added");
		parent = bp;
		mRange = 150;
		mDelay = 200;
		this.pX = pX;
		this.pY = pY;
		mScene = scene;
		mScene.registerTouchArea(this);
		// start scanning for enemies
		mScanThread = new Thread(new ScanTask());
		mScanThread.start();
		GameScene.addToMoney(-500);
		Log.d("GAME", "Tower created, life thread started");

	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			GameScene.unselectPoints();
			iconOptionSell = new Sprite(pX + 65, pY - 45,
					ResourcesManager.TEXTURE_OPTION_SELL,
					ResourcesManager.getInstance().vbom) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
						deleteTower();
					}
					return true;
				}
			};
			mScene.attachChild(iconOptionSell);
			mScene.registerTouchArea(iconOptionSell);
			iconOptionUpgrade = new Sprite(pX + 65, pY + 65,
					ResourcesManager.TEXTURE_OPTION_UPGRADE,
					ResourcesManager.getInstance().vbom) {

				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					GameScene.unselectPoints();
					return true;
				};
			};
			mScene.attachChild(iconOptionUpgrade);
			mScene.registerTouchArea(iconOptionUpgrade);
		}
		return true;
	}

	public void showTowerOptions() {
		iconOptionSell = new Sprite(getX() - 50, getY() - 50,
				ResourcesManager.TEXTURE_OPTION_SELL,
				ResourcesManager.getInstance().vbom);
	}

	public void deleteOptionsIcons() {
		if (iconOptionSell != null) {
			iconOptionSell.detachSelf();
			iconOptionUpgrade.detachSelf();
		}
	}

	public void unregisterOptionsIcons() {
		if (iconOptionSell != null) {
			mScene.unregisterTouchArea(iconOptionSell);
			mScene.unregisterTouchArea(iconOptionUpgrade);
		}
	}

	public void deleteTower() {
		mScene.unregisterTouchArea(this);
		// parent.unregisterIcons();
		this.detachSelf();
		GameScene.unselectPoints();
		Log.d("GAME", "Tower deleted");
		mScene.registerTouchArea(parent);
		GameScene.addToMoney(500);
	}

	class ScanTask implements Runnable {

		@Override
		public void run() {
			while (mActive) {
				//Log.e("GAME", "Tower thread");
				if (ResourcesManager.SCREEN_HEIGHT > 10) {
					mTarget = scan(mScanMethod);
					if (mTarget != null) {
						if (mTarget.mState == Enemy.STATE_DEAD) {
							mTarget = null;
						} else {
							if (inRange(mTarget)) {
								Log.e("GAME", "In range");
								fireProjectile();
								try {
									Thread.sleep(mDelay);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							} else {
								mTarget = null;
							}
						}
					}
				}
			}
		}
	}

	public Enemy scan(int scanMethod) {

		synchronized (ResourcesManager.mCurrentEnemies) {

			switch (scanMethod) {
			case SCAN_FIRST:
				for (int i = 0; i < ResourcesManager.mCurrentEnemies.size(); i++) {
					Enemy potentialTarget = ResourcesManager.mCurrentEnemies
							.get(i);
					if (!potentialTarget.isDead() && inRange(potentialTarget)) {
						return potentialTarget;
					}
				}
				break;
			case SCAN_LAST:
				Enemy lastTarget = null;
				for (int i = 0; i < ResourcesManager.mCurrentEnemies.size(); i++) {
					Enemy potentialTarget = ResourcesManager.mCurrentEnemies
							.get(i);
					if (!potentialTarget.isDead() && inRange(potentialTarget)) {
						lastTarget = ResourcesManager.mCurrentEnemies.get(i);
					}
				}
				return lastTarget;
			}

		}

		return null;

	}

	public boolean inRange(Enemy enemy) {

		float diffX = pX - enemy.mCenterCX;
		float diffY = (pY - enemy.mCenterCY) / 0.8f;
		double dist = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2));

		return (dist <= mRange);

	}

	public void fireProjectile() {
		Projectile proj = new TestBullet(mTarget, ResourcesManager.TEXTURE_PROJECTILE_ROUND, pX + 30, pY + 30);
		mScene.attachChild(proj);
	}
	
	@Override
	public void onDetached() {
		mActive = false;
		Log.e("GAME", "In range");
	}
}
