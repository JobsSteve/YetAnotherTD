package com.example.andenginesample;

import java.util.ArrayList;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

import android.renderscript.Sampler.Value;
import android.util.Log;

public class Enemy extends Sprite {
	// enemy constants
	public static final int ENEMY_TEST = 0;

	// speed constants
	public static final double SPEED_SLOW = 0.5;
	public static final double SPEED_NORMAL = 1;
	public static final double SPEED_FAST = 2;

	// state constants
	public static final int STATE_DEAD = 0;
	public static final int STATE_NORMAL = 1;
	public static final int STATE_SLOW = 2;
	public static final int STATE_PAUSED = 3;
	public static final int DURATION_SLOW = 500;

	// globals
	public float mCenterCX;
	public float mCenterCY;
	public float mOffsetX = (float) (Math.random() * 19 - 7);
	public float mOffsetY = (float) (Math.random() * 19 - 7);
	public ArrayList<WayPoint> mPath;
	public float mMaxHealth;
	public float mHealth;
	public double mSpeed;
	public double mSpeedFactor;
	public int mCoins;
	public WayPoint mTarget;
	public int mCurrentTarget = 0;
	public int mState = STATE_NORMAL;
	public int mSlowCount = 0;
	public Thread mEnemyLifeThread;
	public Sprite defeatSprite;
	public BaseScene mScene;

	public Enemy(ArrayList<WayPoint> path, float pX, float pY, BaseScene scene, ITextureRegion texture) {
		
		super(pX, pY, texture, ResourcesManager
				.getInstance().vbom);
		mCenterCX = getX() + (texture.getWidth() / 2);
		mCenterCY = getY() + (texture.getHeight() / 2);
		mPath = path;
		mTarget = mPath.get(mCurrentTarget);
		mSpeedFactor = SPEED_NORMAL;

		mMaxHealth = 100;
		mHealth = mMaxHealth;
		mSpeed = (float) 1;
		mCoins = 30;
		mScene = scene;
		mScene.attachChild(this);

		mEnemyLifeThread = new Thread(new MoveTask());
		mEnemyLifeThread.start();
		Log.d("GAME", "Enemy created, life thread started" + String.valueOf(pX));
	}

	class MoveTask implements Runnable {

		@Override
		public void run() {
			while (true) { // пока конец дороги

				while (true) { // пока не на wp

					// stop if died
					if (mState == STATE_DEAD) {
						return;
					}

					// тодо добавить паузу
					if (ResourcesManager.SCREEN_HEIGHT > 10) {

						if (mState == STATE_SLOW) {
							mSlowCount++;
							if (mSlowCount == DURATION_SLOW) {
								mState = STATE_NORMAL;
								mSpeedFactor = SPEED_NORMAL;
								mSlowCount = 0;
							}
						}

						// считаем расстояние 
						float distX = mTarget.mCenterX - (mCenterCX + mOffsetX);
						float distY = mTarget.mCenterY - (mCenterCY + mOffsetY);
						float dist = (float) Math.sqrt(Math.pow(distX, 2)
								+ Math.pow(distY, 2));

						// если слишком близко до wp, ищем следущую
						if (dist < mSpeed) {
							break;
						}

						//на направлению на wp
						float dX = distX / dist * (float) mSpeed
								* (float) mSpeedFactor;
						float dY = distY / dist * (float) mSpeed
								* (float) mSpeedFactor;
						move(dX, dY);

						try {
							Thread.sleep(30);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}

				}

				//стоим на wp
				if (mCurrentTarget < mPath.size() - 1) {

					//идем дальше
					mCurrentTarget++;
					mTarget = mPath.get(mCurrentTarget);

				} else {

					finish();
					break;

				}

			}

		}

	}

	public void move(float dX, float dY) {

		setPosition(getX() + dX, getY() + dY);
		mCenterCX += dX;
		mCenterCY += dY;

	}

	public synchronized void die() {

		if (mState != STATE_DEAD) {
			Log.d("GAME", "Enemy die");


			GameScene.addToMoney(mCoins);
			// remove enemy
			removeEnemy();

		}

	}

	public void removeEnemy() {
		mState = STATE_DEAD;
		ResourcesManager.mEnemiesFinished++;
		Log.d("GAME", "Enemy removed");

		setVisible(false);
		setTag(-1);

		synchronized (ResourcesManager.mCurrentEnemies) {
			ResourcesManager.mCurrentEnemies.remove(this);
		}
		Log.e("GAME", String.valueOf(ResourcesManager.mCurrentEnemies.size() + "XXX" +String.valueOf(ResourcesManager.mLifes)));
		Log.v("GAME", "else if" + String.valueOf(ResourcesManager.mCurrentEnemies.size()) + String.valueOf(ResourcesManager.mLifes) + "T: " +String.valueOf(ResourcesManager.mEnemiesTotal));
		if(ResourcesManager.mCurrentEnemies.size() == 0 && ResourcesManager.mLifes > 1 && ResourcesManager.mEnemiesTotal <= ResourcesManager.mEnemiesFinished)
		{
			Sprite win = new Sprite(0, 0, ResourcesManager.TEXTURE_WIN, ResourcesManager.getInstance().vbom);
			win.setZIndex(9999);
			mScene.attachChild(win);
		}
		else if(ResourcesManager.mCurrentEnemies.size() == 0 && ResourcesManager.mLifes> 1 && ResourcesManager.mEnemiesTotal >= ResourcesManager.mEnemiesFinished) {
			Log.d("GAME", "else if");
			GameScene.nextWave(10, ResourcesManager.wave++);
		}
		Log.v("GAME", "else if" + String.valueOf(ResourcesManager.mCurrentEnemies.size()) + String.valueOf(ResourcesManager.mLifes) + String.valueOf(ResourcesManager.mEnemiesFinished) + "RES WAVES: " + String.valueOf(ResourcesManager.wave));
	}

	@Override
	public void onDetached() {
	}

	public void finish() {

		// TODO play a sound
		ResourcesManager.mVibrator.vibrate(20);

		// player takes damage
		if (GameScene.getDamage() == -1) {
			defeatSprite = new Sprite(0, 0, ResourcesManager.TEXTURE_DEFEAT,
					ResourcesManager.getInstance().vbom);
			defeatSprite.setZIndex(9999);
			mScene.attachChild(defeatSprite);
		}
		GameScene.refreshLifes();

	}

	public synchronized void hit(double damage, int effectCode) {

		// enemy takes damage
		mHealth -= damage;


		// die if health is too low
		if (mHealth <= 0) {
			die();
		} else {
		}

	}

	

	public boolean isDead() {
		return (mState == STATE_DEAD);
	}

	public boolean isSlow() {
		return (mState == STATE_SLOW);
	}

}

class BananaY extends Enemy {

	public BananaY(ArrayList<WayPoint> path, float pX, float pY, BaseScene scene) {
		super(path, pX, pY, scene, ResourcesManager.TEXTURE_BANANA_YELLOW);
		// TODO Auto-generated constructor stub
	}
	
}
class BananaB extends Enemy {
	
	public BananaB(ArrayList<WayPoint> path, float pX, float pY, BaseScene scene) {
		super(path, pX, pY, scene, ResourcesManager.TEXTURE_BANANA_BLUE);
		mMaxHealth = 120;
		mHealth = mMaxHealth;
		mCoins = 40;
		// TODO Auto-generated constructor stub
	}
	
}
class BananaG extends Enemy {

	public BananaG(ArrayList<WayPoint> path, float pX, float pY, BaseScene scene) {
		super(path, pX, pY, scene, ResourcesManager.TEXTURE_BANANA_GREY);
		mMaxHealth = 140;
		mHealth = mMaxHealth;
		mCoins = 50;
		// TODO Auto-generated constructor stub
	}
	
}