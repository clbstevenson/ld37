package com.exovum.ld37warmup;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.exovum.ld37warmup.systems.AnimationSystem;
import com.exovum.ld37warmup.systems.FontSystem;
import com.exovum.ld37warmup.systems.PhysicsDebugSystem;
import com.exovum.ld37warmup.systems.PhysicsSystem;
import com.exovum.ld37warmup.systems.RenderingSystem;
import com.exovum.ld37warmup.systems.UselessStateSwapSystem;

/**
 * Created by exovu_000 on 12/9/2016.
 */

public class SchoolGameScreen extends ScreenAdapter {

    Game game; // May need to change this to LD37Game type. Maybe not though

    private boolean initialized;
    private float elapsedTime = 0f;
    private World world; // Box2D World

    OrthographicCamera camera;
    private SchoolWorld gameWorld; // DONE: change this game world - changed to SchoolWorld

    private PooledEngine engine; // Ashley ECS engine

    private SpriteBatch batch;
    private IScreenDispatcher dispatcher; // TODO change this simple screen switcher or don't use it

    // font related items. glyphlayout makes it easier to draw and center fonts and other stuff probably
    // Font rendering should be processed as FontComponent so the ECS RenderingSystem can grab it
    private BitmapFont mediumFont;
    private GlyphLayout glyphLayout;

    // TODO add InputListener

    public SchoolGameScreen(Game game, SpriteBatch batch, IScreenDispatcher dispatcher) {
        this.game = game;
        this.batch = batch;
        this.dispatcher = dispatcher;
    }

    private void init() {
        Gdx.app.log("School Game Screen", "Initializing School Game Screen");

        // new Box2D world with no gravity
        world = new World(new Vector2(0f, 0f), true);
        engine = new PooledEngine();
        // DONE: change this game world - changed to School World
        gameWorld = new SchoolWorld(engine);

        // create ECS system to process rendering
        RenderingSystem renderingSystem = new RenderingSystem(batch);
        engine.addSystem(renderingSystem);
        engine.addSystem(new FontSystem(batch));
        engine.addSystem(new AnimationSystem());
        // add ECS system to process physics in the Box2D world
        engine.addSystem(new PhysicsSystem(world));

        // TODO: eventually remove these systems. they are just used for testing
        engine.addSystem(new PhysicsDebugSystem(world, renderingSystem.getCamera()));
        engine.addSystem(new UselessStateSwapSystem());

        // DONE: changed to school world
        gameWorld.create();

        camera = engine.getSystem(RenderingSystem.class).getCamera();

        // load fonts
        //mediumFont = Assets.getMediumFont();
        glyphLayout = new GlyphLayout();

        // set initialized if everything else was a success
        initialized = true;
    }

    private void update(float delta) {
        engine.update(delta);

        elapsedTime += delta;

        // Use a FontComponent to render text. It will be processed by RenderingSystem
        //glyphLayout.setText(Assets.getMediumFont(), "Time: " + elapsedTime);
        //mediumFont.draw(batch, glyphLayout, )
        //Assets.getMediumFont().draw(batch, glyphLayout, camera.position.x - glyphLayout.width / 2,
        //        camera.viewportHeight / 2);
        // can trigger stuff based on elapsedTime [children etc]
    }

    @Override
    public void render(float delta) {
        // if everything is ready, then update. otherwise need to setup game world
        if(initialized) {
            update(delta);
        } else {
            init();
        }
    }

}
