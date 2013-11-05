package com.example.andenginesample;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.util.Log;

public class Projectile extends Sprite {
	public static final int EFFECT_NONE = 0;
	public static final int EFFECT_SLOW = 1;

	public float mCenterX;
	public float mCenterY;
	public int mType;
	public Enemy mTarget;
	public float mSpeed;
	public double mDamage;
	public int mEffect = EFFECT_NONE;
	public Thread mProjectileMoveThread;
	public float mGrowth = 0;

	public Projectile(Enemy target, ITextureRegion texture, float x, float y) {

		// superconstructor
		super(x - (texture.getWidth() / 2), y - (texture.getHeight() / 2),
				texture, ResourcesManager.getInstance().vbom);
		mTarget = target;
		mSpeed = 5;
		mDamage = 15;
		// set variables
		// setZIndex(TowerDefense.ZINDEX_ROUNDS);
		mCenterX = getX() + (texture.getWidth() / 2);
		mCenterY = getY() + (texture.getHeight() / 2);

		//начать сканировать 
		mProjectileMoveThread = new Thread(new MoveTask());
		mProjectileMoveThread.start();
		Log.d("GAME", "Projectile create, life thread started");

	}

	class MoveTask implements Runnable {

		@Override
		public void run() {

			// пока не достигли врага
			while (mTarget != null) {
				//Log.d("GAME", "Projectile thread");

				if (ResourcesManager.SCREEN_HEIGHT > 10) {

					
					float distX = mTarget.mCenterCX - mCenterX;
					float distY = mTarget.mCenterCY - mCenterY;
					float dist = (float) Math.sqrt(Math.pow(distX, 2)
							+ Math.pow(distY, 2));

					
					if (dist < mSpeed) {
						break;
					}

					float dX = distX / dist * (float) mSpeed;
					float dY = distY / dist * (float) mSpeed;
					setPosition(getX() + dX, getY() + dY);
					mCenterX += dX;
					mCenterY += dY;

					if (mGrowth > 0) {
						setScale(Math.min(1, getScaleX() + mGrowth));
					}

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}


			if (mTarget != null) {
				mTarget.hit(mDamage, mEffect);
			}

			setVisible(false);
			setTag(-1);
			Log.d("GAME", "Projectile die, life thread setTAg");
		}
	}
}
