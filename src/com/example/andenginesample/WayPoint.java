package com.example.andenginesample;

import org.andengine.entity.sprite.Sprite;

public class WayPoint extends Sprite {

	private float pX;
	private float pY;
	public float mCenterX;
	public float mCenterY;
	public BaseScene mScene;

	public WayPoint(float pX, float pY,
			BaseScene scene) {
		super(pX, pY, ResourcesManager.TEXTURE_WAYPOINT, ResourcesManager
				.getInstance().vbom);
		this.pX = pX;
		this.pY = pY;
		mCenterX = getX() + (ResourcesManager.TEXTURE_WAYPOINT.getWidth() / 2);
		mCenterY = getY() + (ResourcesManager.TEXTURE_WAYPOINT.getHeight() / 2);
		mScene = scene;
		setVisible(false);
		mScene.attachChild(this);
		
	}

}
