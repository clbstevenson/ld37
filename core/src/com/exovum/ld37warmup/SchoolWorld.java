package com.exovum.ld37warmup;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g3d.utils.BaseAnimationController;
import com.exovum.ld37warmup.components.FontComponent;
import com.exovum.ld37warmup.components.SchoolComponent;
import com.exovum.ld37warmup.components.StateComponent;
import com.exovum.ld37warmup.components.TextureComponent;
import com.exovum.ld37warmup.components.TransformComponent;
import com.exovum.ld37warmup.systems.RenderingSystem;

/**
 * Created by exovu_000 on 12/9/2016.
 */

public class SchoolWorld {
    // PS: Use an abstract World class next time that supplies the base info
    // connecting SchoolWorld and GameWorld together for simplification

    enum State {
        RUNNING, PAUSED, GAMEOVER, GAMEWON, UPGRADE
    }
    State state;

    public static final float WORLD_WIDTH = 40f; // TODO: To Be Determined. Check with RenderingSystem
    public static final float WORLD_HEIGHT = 30f; // TODO: TBD. Check with RenderingSystem

    PooledEngine engine;

    public SchoolWorld(PooledEngine engine) {
        this.engine = engine;
    }

    public void create() {
        this.state = State.RUNNING;

        generateSchool(WORLD_WIDTH / 2, WORLD_HEIGHT - SchoolComponent.HEIGHT);

        generateText("A Simple Text", 10f, 10f);//WORLD_WIDTH / 2, WORLD_HEIGHT - SchoolComponent.HEIGHT);


        // addChild();
        // removeChild();
        // updateChild();
        // generateUI();
    }

    private void generateText(String text, float x, float y) {
        Gdx.app.log("School World", "Generating font text entity");
        Entity e = engine.createEntity();

        FontComponent font = engine.createComponent(FontComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);

        //TODO: fix font rendering scaling: its too big!
        // font.font = Assets.getMediumFont();
        font.font = Assets.getFont("candara12.fnt");
        font.glyph = new GlyphLayout();
        font.glyph.setText(font.font, text);

        position.position.set(x, y, 2.0f); //TODO compare z-value with School's z-value

        //Remembering to add the components to the entity is a good idea
        e.add(font);
        e.add(position);

        engine.addEntity(e);
    }

    private void generateTextWithFont(String text, float x, float y, String fontname) {
        Gdx.app.log("School World", "Generating font text entity");
        Entity e = engine.createEntity();

        FontComponent font = engine.createComponent(FontComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);

        //TODO: fix font rendering scaling: its too big!
        // font.font = Assets.getMediumFont();
        font.font = Assets.getFont(fontname);
        font.glyph = new GlyphLayout();
        font.glyph.setText(font.font, text);

        position.position.set(x, y, 2.0f); //TODO compare z-value with School's z-value

        //Remembering to add the components to the entity is a good idea
        e.add(font);
        e.add(position);

        engine.addEntity(e);
    }

    /**
     * Creates a new School entity centered around @param x and @param y
     * @param x horizontal value of center point for the school
     * @param y vertical value of center point for the school
     */
    private void generateSchool(float x, float y) {
        Entity e = engine.createEntity();

        // make it a school entity
        SchoolComponent school = engine.createComponent(SchoolComponent.class);
        // Use Texture instead of AnimationComponent because the School is just a sprite
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        // TODO add BodyComponent so it can be processed by the Box2D world?

        texture.region = Assets.getSchoolSprite();

        // TODO scale the texture to fit SchoolComponent width and height
        float scaleX = SchoolComponent.WIDTH / (RenderingSystem.PixelsToMeters(texture.region.getRegionWidth()));
        float scaleY = SchoolComponent.HEIGHT / (RenderingSystem.PixelsToMeters(texture.region.getRegionHeight()));

        //position.position.set(x - SchoolComponent.WIDTH, y, 1.0f);
        position.position.set(x, y, 1.0f); // does a lower z-value mean closer or farther?
        position.scale.set(scaleX, scaleY);  //0.75f, 0.5f);
        //position.scale.set(0.5f, 0.5f);

        state.set(SchoolComponent.STATE_NORMAL);

        e.add(school);
        e.add(texture);
        e.add(position);
        e.add(state);

        engine.addEntity(e);
    }

}
