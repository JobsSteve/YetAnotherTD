package com.example.andenginesample;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.example.andenginesample.SceneManager.SceneType;

public class GameScene extends BaseScene {
	private HUD gameHUD;
	private Sprite background;
	private static Text moneyText;
	private static Text lifeText;
	private static Text noMoneyText;
	private static Text waveText;
	private static int money = 0;
	private PhysicsWorld physicsWorld;
	private static LevelOne lvl;

	@Override
	public void createScene() {
		setOnAreaTouchTraversalFrontToBack();
		createBackground();
		createHUD();
		lvl = new LevelOne(this);
		nextWave(10, 0);
	}

	@Override
	public void onBackKeyPressed() {
		
		for(int i = 0; i < ResourcesManager.basePointsArray.size(); i++) {
			if(ResourcesManager.basePointsArray.get(i).mTower != null) {
				ResourcesManager.basePointsArray.get(i).mTower.deleteTower();
			}
		}
		for (int i = 0; i < this.getChildCount(); i++) {
			IEntity child = this.getChildByIndex(i);
			if (child != null && child.getTag() == -1) {
				this.detachChild(child);
				Log.e("GAME", "detached smth");
			}
		}
		SceneManager.getInstance().loadMenuScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disposeScene() {
		camera.setHUD(null);
		camera.setCenter(427, 240);

		// TODO code responsible for disposing scene
		// removing all game scene objects.
	}

	private void createBackground() {
		background = new Sprite(0, 0, ResourcesManager.TEXTURE_BACKGROUND,
				ResourcesManager.getInstance().vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
					Log.e("GAME", "In OnTouch");
					unselectPoints();
				}
				Log.e("GAME", "BACK_ON_TASH");
				return true;
			}
		};
		registerTouchArea(background);
		attachChild(background);
		// setBackground(resourcesManager.mGrassBackground);
	}

	private void createHUD() {
		gameHUD = new HUD();

		// CREATE money TEXT
		moneyText = new Text(0, 0, resourcesManager.font, "money: 0123456789",
				new TextOptions(HorizontalAlign.LEFT), vbom);
		lifeText = new Text(300, 0, resourcesManager.font, "lifes: 0123456789",
				new TextOptions(HorizontalAlign.LEFT), vbom);
		waveText = new Text(470, 0, resourcesManager.font, "lifes: 0123456789",
				new TextOptions(HorizontalAlign.LEFT), vbom);
		noMoneyText = new Text(470, 0, resourcesManager.font, "Not enought money, need 500",
				new TextOptions(HorizontalAlign.LEFT), vbom);
		// moneyText.setSkewCenter(0, 0);
		moneyText.setText("Money: " + ResourcesManager.mCoins);
		lifeText.setText("Lifes: " + ResourcesManager.mLifes);
		waveText.setText("Wave:" + ResourcesManager.wave + "/" + ResourcesManager.mWavesAll);
		gameHUD.attachChild(moneyText);
		gameHUD.attachChild(lifeText);
		gameHUD.attachChild(waveText);
		camera.setHUD(gameHUD);
	}

	public static void addToMoney(int i) {
		ResourcesManager.mCoins += i;
		moneyText.setText("Money: " + ResourcesManager.mCoins);
	}

	public static void refreshLifes() {
		lifeText.setText("Lifes: " + ResourcesManager.mLifes);
	}
	public static void refresfWaves() {
		waveText.setText("Wave:" + ResourcesManager.wave + "/" + ResourcesManager.mWavesAll);
	}
	public static void createNoMoneyText() {
		
	}
	private void createPhysics() {
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
		registerUpdateHandler(physicsWorld);
	}

	

	public static void unselectPoints() {
		for (int i = 0; i < ResourcesManager.basePointsArray.size(); i++) {
			if (ResourcesManager.basePointsArray.get(i).iconTowerInfantry != null) {
				if (ResourcesManager.basePointsArray.get(i).mTower != null) {
					ResourcesManager.basePointsArray.get(i).mTower.deleteOptionsIcons();
					ResourcesManager.basePointsArray.get(i).mTower.unregisterOptionsIcons();
				}
				Log.e("GAME", "in foreach");
				ResourcesManager.basePointsArray.get(i).deleteIcons();
				ResourcesManager.basePointsArray.get(i).unregisterIcons();
			}
		}
	}

	public static void unselectTowers() {
		for (int i = 0; i < ResourcesManager.basePointsArray.size(); i++) {
			if (ResourcesManager.basePointsArray.get(i).iconTowerInfantry != null) {
				if (ResourcesManager.basePointsArray.get(i).mTower != null)
					ResourcesManager.basePointsArray.get(i).mTower.deleteOptionsIcons();
			}
		}
	}

	public static int getDamage() {
		if (ResourcesManager.mLifes > 1)
			return ResourcesManager.mLifes--;
		else {
			return -1;
		}
	}
	
	public static void nextWave(int n, int wave) {
		lvl.createEnemies(n, wave);
	}
}
