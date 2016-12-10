package com.exovum.ld37warmup;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.exovum.ld37warmup.components.StateComponent;
import com.exovum.ld37warmup.components.TextureComponent;
import com.exovum.ld37warmup.components.TransformComponent;

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

        generateSchool(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        // addChild();
        // removeChild();
        // updateChild();
        // generateUI();
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

        position.position.set(x, y, 1.0f); // does a lower z-value mean closer or farther?
        position.scale.set(0.5f, 0.5f);

        state.set(SchoolComponent.STATE_NORMAL);

        e.add(school);
        e.add(texture);
        e.add(position);
        e.add(state);

        engine.addEntity(e);
    }

}
