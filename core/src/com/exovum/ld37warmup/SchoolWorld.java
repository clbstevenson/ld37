package com.exovum.ld37warmup;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.exovum.ld37warmup.components.AnimationComponent;
import com.exovum.ld37warmup.components.BookComponent;
import com.exovum.ld37warmup.components.FontComponent;
import com.exovum.ld37warmup.components.SchoolComponent;
import com.exovum.ld37warmup.components.StateComponent;
import com.exovum.ld37warmup.components.TextureComponent;
import com.exovum.ld37warmup.components.TransformComponent;
import com.exovum.ld37warmup.components.TreeComponent;
import com.exovum.ld37warmup.systems.RenderingSystem;

import java.util.Random;

/**
 * Created by exovu_000 on 12/9/2016.
 */

public class SchoolWorld {
    // Cooldown timer for controlling events such as throwing books
    // TODO: array of cooldown timers?
    private float cooldown;

    private Random random;

    // PS: Use an abstract World class next time that supplies the base info
    // connecting SchoolWorld and GameWorld together for simplification

    enum State {
        RUNNING, PAUSED, GAMEOVER, GAMEWON, UPGRADE
    }
    State state;

    enum BookTitle {
        QUIXOTE, MOCKINGBIRD, GATSBY, IDIOT;

        public String getAssetName() {
            switch(this) {
                case QUIXOTE:
                    return "donq";
                case MOCKINGBIRD:
                    return "mock";
                case GATSBY:
                    return "gatsby";
                case IDIOT:
                    return "idiot";
                default:
                    return null;
            }
        }

        /**
         * Generate a random quote for the BookTitle
         * @return Text for a random quote from the book
         */
        public String getRandomQuote() {
            // TODO: add quotes
            switch(this) {
                case QUIXOTE:
                    return "\'Don Quixote\' by Miguel de Cervantes";
                case MOCKINGBIRD:
                    return "'To Kill a Mockingbird' by Harper Lee";
                case GATSBY:
                    return "'The Great Gatsby' by F. Scott Fitzgerald";
                case IDIOT:
                    return "'The Idiot' by Fyodor Dostoevsky";
                default:
                    return "NO QUOTE";
            }
        }
    }

    public static final float WORLD_WIDTH = 40f; // TODO: To Be Determined. Check with RenderingSystem
    public static final float WORLD_HEIGHT = 30f; // TODO: TBD. Check with RenderingSystem

    PooledEngine engine;

    public SchoolWorld(PooledEngine engine) {
        this.engine = engine;
        random = new Random();
    }

    public void create() {
        this.state = State.RUNNING;

        generateSchool(WORLD_WIDTH / 2, WORLD_HEIGHT - SchoolComponent.HEIGHT * 3 / 4);

        generateTextWithFont("One Room Schoolhouse", WORLD_WIDTH / 2, WORLD_HEIGHT,
                "candara36b.fnt");
        generateText("A Simple Text", 10f, 10f);//WORLD_WIDTH / 2, WORLD_HEIGHT - SchoolComponent.HEIGHT);


        // addChild();
        // removeChild();
        // updateChild();
        // generateUI();
    }

    public void update(float delta) {
        cooldown -= delta;
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

    /**
     * Create a Book entity aimed at (x,y)
     * @param x x-value of target position
     * @param y y-value of target position
     */
    public void throwBook(float x, float y) {
        Gdx.app.log("School World", "Starting to throw book");
        if(cooldown < 0) {
            // get a value from [0, number of booktitles ) excluding end point

            Entity e = engine.getEntitiesFor(Family.all(SchoolComponent.class).get()).first();
            //if(e == null || e.getComponents().
            //createBook(random.nextInt(BookTitle.values().length), engine.getEntitiesFor(Family.all(SchoolComponent.)), y);
            //TODO: change the x and y values. they spawn where they are clicked.
            // they should start at the schoolhouse and move towards point (x,y) instead.
            createBook(random.nextInt(BookTitle.values().length), x, y);
            //reset the cooldown timer
            cooldown = 1000;
        }
    }

    private BookTitle getBookTitleByID(int bookid) {
        return BookTitle.values()[bookid];
    }

    /**
     * Creates a new Book entity
     * @param bookid enum BookTitle for the new book
     */
    private void createBook(int bookid, float x, float y) {
        Entity e = new Entity(); // OR engine.creatEntity(); not sure which is better

        BookComponent book = engine.createComponent(BookComponent.class);
        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        //TODO: add BodyComponent for collisisions

        BookTitle title = getBookTitleByID(bookid);

        animation.animations.put(BookComponent.STATE_THROWN, Assets.getBookByName(title.getAssetName()));
        animation.animations.put(BookComponent.STATE_CAUGHT, Assets.getHeldBookByName(title.getAssetName()));
        //animation.animations.put(TreeComponent.STATE_THROWN, Assets.treeNormal);
        //animation.animations.put(TreeComponent.STATE_CAUGHT, Assets.treeLights);

        // TODO use BookComponent.width and BookComponent.height for Bounds -> and BodyComponent
        position.position.set(x, y, 3.0f);
        position.scale.set(0.5f, 0.5f);

        state.set(BookComponent.STATE_THROWN);

        e.add(book);
        e.add(animation);
        e.add(state);
        e.add(texture);
        e.add(position);

        engine.addEntity(e);
    }

}
