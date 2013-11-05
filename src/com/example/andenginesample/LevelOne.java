package com.example.andenginesample;

import android.util.Log;

public class LevelOne {
	private BaseScene mScene;

	public LevelOne(BaseScene scene) {
		mScene = scene;
		createBasePoints();
		createWayPoints();

	}

	public void createBasePoints() {
		ResourcesManager.basePointsArray.add(new BasePoint(130, 150, mScene));
		ResourcesManager.basePointsArray.add(new BasePoint(370, 215, mScene));
		ResourcesManager.basePointsArray.add(new BasePoint(360, 354, mScene));
		ResourcesManager.basePointsArray.add(new BasePoint(523, 97, mScene));
		ResourcesManager.basePointsArray.add(new BasePoint(780, 182, mScene));
	}

	public void createWayPoints() {
		ResourcesManager.path.add(new WayPoint(-10, 200, mScene));
		ResourcesManager.path.add(new WayPoint(0, 200, mScene));
		ResourcesManager.path.add(new WayPoint(14, 200, mScene));
		ResourcesManager.path.add(new WayPoint(20, 200, mScene));
		ResourcesManager.path.add(new WayPoint(50, 200, mScene));
		ResourcesManager.path.add(new WayPoint(100, 200, mScene));
		ResourcesManager.path.add(new WayPoint(150, 203, mScene));
		ResourcesManager.path.add(new WayPoint(310, 200, mScene));
		ResourcesManager.path.add(new WayPoint(310, 320, mScene));
		ResourcesManager.path.add(new WayPoint(504, 320, mScene));
		ResourcesManager.path.add(new WayPoint(501, 146, mScene));
		ResourcesManager.path.add(new WayPoint(853, 144, mScene));
	}

	public void createEnemies(int n, int wave) {
		Log.v("GAME", "Create enemies n:" + String.valueOf(n) + "wave: "
				+ String.valueOf(wave));
		GameScene.refresfWaves();
		switch (wave) {
		case 1:
			for (int i = 0; i < n; i++)
				ResourcesManager.mCurrentEnemies.add(new BananaY(
						ResourcesManager.path, -10 * i, 200, mScene));
			break;
		case 2:
			for (int i = 0; i < n; i++)
				ResourcesManager.mCurrentEnemies.add(new BananaB(
						ResourcesManager.path, -10 * i, 200, mScene));
			break;
		case 3:
			for (int i = 0; i < n; i++)
				ResourcesManager.mCurrentEnemies.add(new BananaG(
						ResourcesManager.path, -10 * i, 200, mScene));
			break;
		default:
			for (int i = 0; i < n; i++)
				ResourcesManager.mCurrentEnemies.add(new BananaY(
						ResourcesManager.path, -10 * i, 200, mScene));
			break;
		}
		// ResourcesManager.mEnemiesTotal += ResourcesManager.mCurrentEnemies
		// .size();
	}
}
