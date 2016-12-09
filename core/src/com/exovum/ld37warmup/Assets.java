package com.exovum.ld37warmup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by exovu_000 on 12/3/2016.
 * Uses an AssetManager to keep track of all resources.
 * It would be possible to do a loading screen too.
 */

public class Assets {

    public static AssetManager am;

    // Resources
    public static Animation treeNormal;
    public static Animation treeLights;

    public static AssetManager load() {
        // Load the image for the splash screen
        loadSplash();

        am = new AssetManager();
        // Load all of the assets via am.load(asset_to_load, asset_class_type) [Texture.class, etc]
        // load the animations atlas to reference and find the connected animations
        am.load(ANIMATION_ATLAS, TEXTURE_ATLAS);
        am.load("sounds/explosion-1.wav", SOUND);
        am.load("sounds/drop.wav", SOUND);
        am.load("sounds/song1.wav", MUSIC);

        // Setup static resources
        // TODO add this resources to the AssetManager
        if(am.isLoaded(ANIMATION_ATLAS)) {
            Gdx.app.log("Assets", "Setting up tree Animations manually");
            treeNormal = new Animation(1f/8f, getTreeNormalArray(), Animation.PlayMode.LOOP);
            treeLights = new Animation(1f/8f, getTreeLightsArray(), Animation.PlayMode.LOOP);
        }

        return am;
    }

    // Use the static references below to improve readability and writability
    // when including new assets to load
    private static final String FONT = "";
    private static final String ANIMATION_ATLAS = "animations/animations.atlas";

    private static Class<TextureAtlas> TEXTURE_ATLAS = TextureAtlas.class;
    private static Class<Sound> SOUND = Sound.class;
    private static Class<Music> MUSIC = Music.class;

    public static TextureRegion splashScreen;

    private static void loadSplash() {
        splashScreen = new TextureRegion(new Texture("ld37-bg.png"));
    }

    public static Array<TextureAtlas.AtlasRegion> getBallArray() {
        return am.get(ANIMATION_ATLAS, TEXTURE_ATLAS).findRegions("ball/ball");
    }

    public static Array<TextureAtlas.AtlasRegion> getBallMoveArray() {
        return am.get(ANIMATION_ATLAS, TEXTURE_ATLAS).findRegions("ball/move");
    }


    public static Array<TextureAtlas.AtlasRegion> getTreeNormalArray() {
        //Array<TextureAtlas.AtlasRegion> treeArray = am.get(ANIMATION_ATLAS, TEXTURE_ATLAS).findRegions("tree/tree");
        Gdx.app.log("Assets", "Retrieving TreeNormalArray");
        return am.get(ANIMATION_ATLAS, TEXTURE_ATLAS).findRegions("tree/tree");
    }

    public static Array<TextureAtlas.AtlasRegion> getTreeLightsArray() {
        //Array<TextureAtlas.AtlasRegion> treeArray = am.get(ANIMATION_ATLAS, TEXTURE_ATLAS).findRegions("tree/lights");
        Gdx.app.log("Assets", "Retrieving TreeLightsArray");
        return am.get(ANIMATION_ATLAS, TEXTURE_ATLAS).findRegions("tree/lights");
    }

    public static Animation getTreeNormalAnimation() {
        // By using getTreeNormalArray, this ensures the Animation can be loaded
        // because the AssetManager will have loaded the AnimationAtlas and the needed Textures.
        return new Animation(1f/8f, getTreeNormalArray(), Animation.PlayMode.LOOP);
    }

    public static Animation getTreeLightsAnimation() {

        return new Animation(1f/8f, getTreeLightsArray(), Animation.PlayMode.LOOP);
    }
}
