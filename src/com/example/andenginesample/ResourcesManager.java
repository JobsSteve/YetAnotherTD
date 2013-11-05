package com.example.andenginesample;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import android.graphics.Color;
import android.os.Vibrator;

public class ResourcesManager {
	// ---------------------------------------------
	// VARIABLES
	// ---------------------------------------------
	public final static int SCREEN_WIDTH = 854;
	public final static int SCREEN_HEIGHT = 480;
	public static ArrayList<Enemy> mobsArray;
	public static LinkedList<BasePoint> basePointsArray;
	public static ArrayList<WayPoint> path;
	public static ArrayList<Enemy> mCurrentEnemies = new ArrayList<Enemy>();
	public static int mEnemiesFinished = -1;
	public static int mEnemiesTotal = 30;
	public static int mLifes = 6;
	public static int mCoins = 1000;
	public static int wave = 1;
	public static int mWavesAll = 4;
	private static final ResourcesManager INSTANCE = new ResourcesManager();

	public Engine engine;
	public GameActivity activity;
	public Camera mCamera;
	public VertexBufferObjectManager vbom;
	public static Font font;

	// ---------------------------------------------
	// TEXTURES & TEXTURE REGIONS
	// ---------------------------------------------
	public ITextureRegion splash_region;
	private BitmapTextureAtlas splashTextureAtlas;
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion options_region;
	public ITiledTextureRegion mobTextureRegion;
	public static ITextureRegion TEXTURE_BASEPOINT;
	public static ITextureRegion TEXTURE_TOWER_ICON_INFANTRY;
	public static ITextureRegion TEXTURE_TOWER_ICON_MAGIC;
	public static ITextureRegion TEXTURE_BACKGROUND;
	public static ITextureRegion TEXTURE_TOWER_MAGIC;
	public static ITextureRegion TEXTURE_TOWER_INFANTRY;
	public static ITextureRegion TEXTURE_OPTION_SELL;
	public static ITextureRegion TEXTURE_OPTION_UPGRADE;
	public static ITextureRegion TEXTURE_ENEMY;
	public static ITextureRegion TEXTURE_WAYPOINT;
	public static ITextureRegion TEXTURE_PROJECTILE_ROUND;
	public static ITextureRegion TEXTURE_DEFEAT;
	public static ITextureRegion TEXTURE_WIN;
	public static ITextureRegion TEXTURE_BANANA_YELLOW;
	public static ITextureRegion TEXTURE_BANANA_BLUE;
	public static ITextureRegion TEXTURE_BANANA_GREY;
	public RepeatingSpriteBackground mGrassBackground;

	private BuildableBitmapTextureAtlas menuTextureAtlas;

	public static Vibrator mVibrator;

	// ---------------------------------------------
	// CLASS LOGIC
	// ---------------------------------------------

	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
	}

	public void loadGameResources() throws IOException {
		initVariables();
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}

	private void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(
				activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		menu_background_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(menuTextureAtlas, activity,
						"menu_background.png");
		play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				menuTextureAtlas, activity, "play_button.png");
		options_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(menuTextureAtlas, activity,
						"options_button.png");

		try {
			this.menuTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 0));
			this.menuTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	private void loadMenuAudio() {

	}

	private void loadGameGraphics() throws IOException {

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		this.mGrassBackground = new RepeatingSpriteBackground(SCREEN_WIDTH,
				SCREEN_HEIGHT, activity.getTextureManager(),
				AssetBitmapTextureAtlasSource.create(activity.getAssets(),
						"gfx/game/background_grass.png"),
				activity.getVertexBufferObjectManager());

		ITexture basePointTexture = new BitmapTexture(
				activity.getTextureManager(), new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return activity.getAssets().open(
								"gfx/game/basepoint.png");
					}
				});
		ITexture towerIconInfantryTexture = new BitmapTexture(
				activity.getTextureManager(), new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return activity.getAssets().open(
								"gfx/game/icon_infantry.png");
					}
				});
		ITexture towerIconMagicTexture = new BitmapTexture(
				activity.getTextureManager(), new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return activity.getAssets().open(
								"gfx/game/icon_magician.png");
					}
				});
		ITexture backgroundTexture = new BitmapTexture(
				activity.getTextureManager(), new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return activity.getAssets().open(
								"gfx/game/background_grass.png");
					}
				});
		ITexture towerMagicTexture = new BitmapTexture(
				activity.getTextureManager(), new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return activity.getAssets().open(
								"gfx/game/tower_magician.png");
					}
				});
		ITexture towerInfantryTexture = new BitmapTexture(
				activity.getTextureManager(), new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return activity.getAssets().open(
								"gfx/game/tower_infantry.png");
					}
				});
		ITexture optionSellTexture = new BitmapTexture(
				activity.getTextureManager(), new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return activity.getAssets().open(
								"gfx/game/option_sell.png");
					}
				});
		ITexture optionUpgradeTexture = new BitmapTexture(
				activity.getTextureManager(), new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return activity.getAssets().open(
								"gfx/game/option_upgrade.png");
					}
				});
		ITexture enemyTexture = new BitmapTexture(activity.getTextureManager(),
				new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return activity.getAssets().open("gfx/game/enemy.png");
					}
				});
		ITexture waypointTexture = new BitmapTexture(
				activity.getTextureManager(), new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return activity.getAssets().open(
								"gfx/game/waypoint.png");
					}
				});
		ITexture projectileRoundTexture = new BitmapTexture(
				activity.getTextureManager(), new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return activity.getAssets().open("gfx/game/round.png");
					}
				});
		ITexture defeatTexture = new BitmapTexture(
				activity.getTextureManager(), new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return activity.getAssets().open("gfx/game/defeat.png");
					}
				});
		ITexture winTexture = new BitmapTexture(activity.getTextureManager(),
				new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return activity.getAssets().open("gfx/game/win.png");
					}
				});
		ITexture bananaYTexture = new BitmapTexture(
				activity.getTextureManager(), new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return activity.getAssets().open(
								"gfx/game/banana_y.png");
					}
				});
		ITexture bananaBTexture = new BitmapTexture(
				activity.getTextureManager(), new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return activity.getAssets().open(
								"gfx/game/banana_b.png");
					}
				});
		ITexture bananaGTexture = new BitmapTexture(
				activity.getTextureManager(), new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						return activity.getAssets().open(
								"gfx/game/banana_g.png");
					}
				});
		basePointTexture.load();
		towerIconInfantryTexture.load();
		towerIconMagicTexture.load();
		backgroundTexture.load();
		towerMagicTexture.load();
		towerInfantryTexture.load();
		optionSellTexture.load();
		optionUpgradeTexture.load();
		enemyTexture.load();
		waypointTexture.load();
		projectileRoundTexture.load();
		defeatTexture.load();
		winTexture.load();
		bananaBTexture.load();
		bananaYTexture.load();
		bananaGTexture.load();

		ResourcesManager.TEXTURE_BASEPOINT = TextureRegionFactory
				.extractFromTexture(basePointTexture);
		ResourcesManager.TEXTURE_TOWER_ICON_INFANTRY = TextureRegionFactory
				.extractFromTexture(towerIconInfantryTexture);
		ResourcesManager.TEXTURE_TOWER_ICON_MAGIC = TextureRegionFactory
				.extractFromTexture(towerIconMagicTexture);
		ResourcesManager.TEXTURE_BACKGROUND = TextureRegionFactory
				.extractFromTexture(backgroundTexture);
		ResourcesManager.TEXTURE_TOWER_MAGIC = TextureRegionFactory
				.extractFromTexture(towerMagicTexture);
		ResourcesManager.TEXTURE_TOWER_INFANTRY = TextureRegionFactory
				.extractFromTexture(towerInfantryTexture);
		ResourcesManager.TEXTURE_OPTION_SELL = TextureRegionFactory
				.extractFromTexture(optionSellTexture);
		ResourcesManager.TEXTURE_OPTION_UPGRADE = TextureRegionFactory
				.extractFromTexture(optionUpgradeTexture);
		ResourcesManager.TEXTURE_ENEMY = TextureRegionFactory
				.extractFromTexture(enemyTexture);
		ResourcesManager.TEXTURE_WAYPOINT = TextureRegionFactory
				.extractFromTexture(waypointTexture);
		ResourcesManager.TEXTURE_PROJECTILE_ROUND = TextureRegionFactory
				.extractFromTexture(projectileRoundTexture);
		ResourcesManager.TEXTURE_DEFEAT = TextureRegionFactory
				.extractFromTexture(defeatTexture);
		ResourcesManager.TEXTURE_WIN = TextureRegionFactory
				.extractFromTexture(winTexture);
		ResourcesManager.TEXTURE_BANANA_BLUE = TextureRegionFactory
				.extractFromTexture(bananaBTexture);
		ResourcesManager.TEXTURE_BANANA_GREY = TextureRegionFactory
				.extractFromTexture(bananaGTexture);
		ResourcesManager.TEXTURE_BANANA_YELLOW = TextureRegionFactory
				.extractFromTexture(bananaYTexture);

	}

	private void loadGameFonts() {

	}

	private void loadGameAudio() {

	}

	public void initVariables() {
		basePointsArray = new LinkedList<BasePoint>();
		mobsArray = new ArrayList<Enemy>();
		path = new ArrayList<WayPoint>();
	}

	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity, "splash.png", 0, 0);
		splashTextureAtlas.load();
	}

	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		splash_region = null;
	}

	public void unloadMenuTextures() {
		menuTextureAtlas.unload();
	}

	public void loadMenuTextures() {
		menuTextureAtlas.load();
	}

	public void unloadGameTextures() {
		// TODO (Since we did not create any textures for game scene yet)
	}

	/**
	 * @param engine
	 * @param activity
	 * @param camera
	 * @param vbom
	 * <br>
	 * <br>
	 *            We use this method at beginning of game loading, to prepare
	 *            Resources Manager properly, setting all needed parameters, so
	 *            we can latter access them from different classes (eg. scenes)
	 */
	public static void prepareManager(Engine engine, GameActivity activity,
			Camera camera, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().mCamera = camera;
		getInstance().vbom = vbom;
	}

	private void loadMenuFonts() {
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(
				activity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(activity.getFontManager(),
				mainFontTexture, activity.getAssets(), "Droid.ttf", 40, true,
				Color.WHITE, 2, Color.BLACK);
		font.load();
	}

	// ---------------------------------------------
	// GETTERS AND SETTERS
	// ---------------------------------------------

	public static ResourcesManager getInstance() {
		return INSTANCE;
	}

}