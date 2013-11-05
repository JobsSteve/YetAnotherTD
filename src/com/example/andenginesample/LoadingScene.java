package com.example.andenginesample;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.color.Color;

import com.example.andenginesample.SceneManager.SceneType;

public class LoadingScene extends BaseScene {
	 @Override
	    public void createScene()
	    {
		 setBackground(new Background(Color.WHITE));
		    attachChild(new Text(350, 200, resourcesManager.font, "Loading...", vbom));	    }

	    @Override
	    public void onBackKeyPressed()
	    {
	        return;
	    }

	    @Override
	    public SceneType getSceneType()
	    {
	        return SceneType.SCENE_LOADING;
	    }

	    @Override
	    public void disposeScene()
	    {

	    }
}
