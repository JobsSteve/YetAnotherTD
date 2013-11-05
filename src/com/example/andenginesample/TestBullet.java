package com.example.andenginesample;

import org.andengine.opengl.texture.region.ITextureRegion;

public class TestBullet extends Projectile {

	// constants
	public static final int SPEED = 5;
	public static final double DAMAGE = 15;

	// constructor
	public TestBullet(Enemy target, ITextureRegion texture, float x, float y) {

		// superconstructor
		super(target, texture, x, y);

		// set variables
		mTarget = target;
		mSpeed = SPEED;
		mDamage = DAMAGE;
	}
}