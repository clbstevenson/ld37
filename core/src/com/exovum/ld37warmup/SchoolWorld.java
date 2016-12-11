package com.exovum.ld37warmup;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.exovum.ld37warmup.components.AnimationComponent;
import com.exovum.ld37warmup.components.BodyComponent;
import com.exovum.ld37warmup.components.BookComponent;
import com.exovum.ld37warmup.components.FontComponent;
import com.exovum.ld37warmup.components.SchoolComponent;
import com.exovum.ld37warmup.components.StateComponent;
import com.exovum.ld37warmup.components.TextureComponent;
import com.exovum.ld37warmup.components.TransformComponent;
import com.exovum.ld37warmup.systems.RenderingSystem;

import java.util.Random;

/**
 * Created by exovu_000 on 12/9/2016.
 */

public class SchoolWorld {


    // PS: Use an abstract World class next time that supplies the base info
    // connecting SchoolWorld and GameWorld together for simplification

    enum State {
        RUNNING, PAUSED, GAMEOVER, GAMEWON, UPGRADE
    }
    enum BookTitle {
        QUIXOTE, MOCKINGBIRD, WATCH; // TODO: make larger 128x128 images for: GATSBY, IDIOT;

        public String getAssetName() {
            switch (this) {
                case QUIXOTE:
                    return "donq";
                case MOCKINGBIRD:
                    return "mock";
                case WATCH:
                    return "watch";
                /*
                case GATSBY:
                    return "gatsby";
                case IDIOT:
                    return "idiot";
                    */
                default:
                    return null;
            }
        }

        /**
         * Generate a random quote for the BookTitle
         *
         * @return Text for a random quote from the book
         */
        public String getRandomQuote() {
            // TODO: add quotes
            switch (this) {
                case QUIXOTE:
                    return "\'Don Quixote\' by Miguel de Cervantes";
                case MOCKINGBIRD:
                    return "'To Kill a Mockingbird' by Harper Lee";
                case WATCH:
                    return "'Their Eyes Were Watching God' by Zora Neale Hurston";
                /*
                case GATSBY:
                    return "'The Great Gatsby' by F. Scott Fitzgerald";
                case IDIOT:
                    return "'The Idiot' by Fyodor Dostoevsky";
                */
                default:
                    return "NO QUOTE";
            }
        }
    }
    // Cooldown timer for controlling events such as throwing books
    // TODO: array of cooldown timers?
    private float cooldown;
    private Random random;

    State state;

    World physicsWorld;
    final float PIXELS_TO_METERS = 16f;

    public static final float WORLD_WIDTH = 40f; // TODO: To Be Determined. Check with RenderingSystem
    public static final float WORLD_HEIGHT = 30f; // TODO: TBD. Check with RenderingSystem

    PooledEngine engine;
    Entity school;

    public SchoolWorld(PooledEngine engine, World world) {
        this.engine = engine;
        this.physicsWorld = world;
        random = new Random();
    }

    public void create() {
        this.state = State.RUNNING;

        // initially create a
        //physicsWorld = new World(new Vector2(0f, 0f), true);

        school = generateSchool(WORLD_WIDTH / 2, WORLD_HEIGHT - SchoolComponent.HEIGHT * 3 / 4);

        generateTextWithFont("One Room Schoolhouse", WORLD_WIDTH / 2, WORLD_HEIGHT,
                "candara36b.fnt");
        generateText("A Simple Text", 10f, 10f);//WORLD_WIDTH / 2, WORLD_HEIGHT - SchoolComponent.HEIGHT);


        // addChild();
        // removeChild();
        // updateChild();
        // generateUI();
    }

    public void update(float delta) {
        //Gdx.app.log("School World", "Updating school world and cooldowns");
        // '1' cooldown = 1 second. so don't set cooldown to 1000
        cooldown -= delta;

        //Gdx.app.log("School World", "Updating School World. cooldown: " + cooldown);
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
     *
     * @param x horizontal value of center point for the school
     * @param y vertical value of center point for the school
     */
    private Entity generateSchool(float x, float y) {
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
        position.position.set(x, y, 10.0f); // does a lower z-value mean closer or farther?
                                            // I believe low z-values means closer to the "top"
                                            // so high-z values will appear UNDER low-z values
        position.scale.set(scaleX, scaleY);  //0.75f, 0.5f);
        //position.scale.set(0.5f, 0.5f);

        state.set(SchoolComponent.STATE_NORMAL);

        e.add(school);
        e.add(texture);
        e.add(position);
        e.add(state);

        engine.addEntity(e);

        return e;
    }

    /**
     * Create a Book entity aimed at (x,y)
     *
     * @param x x-value of target position
     * @param y y-value of target position
     */
    public void throwBook(float x, float y) {
        if (cooldown < 0) {
            Gdx.app.log("School World", "Starting to throw book");

            // get a value from [0, number of booktitles ) excluding end point

            //TODO
            //Entity e = engine.getEntitiesFor(Family.all(SchoolComponent.class).get()).first();

            //if(e == null || e.getComponents().
            //createBook(random.nextInt(BookTitle.values().length), engine.getEntitiesFor(Family.all(SchoolComponent.)), y);
            //TODO: change the x and y values. they spawn where they are clicked.
            // they should start at the schoolhouse and move towards point (x,y) instead.
            //createBook(random.nextInt(BookTitle.values().length), x, y);
            //                                                  fromX, fromY, toX, toY);
            // start throwing the book from the position of the SchoolComponent
            createBook(random.nextInt(BookTitle.values().length),
                    school.getComponent(TransformComponent.class).position.x,
                    school.getComponent(TransformComponent.class).position.y,
                    x, y);
                    //x / PIXELS_TO_METERS, WORLD_HEIGHT - (y / PIXELS_TO_METERS));
            //reset the cooldown timer
            cooldown = 1;
        }
    }

    private BookTitle getBookTitleByID(int bookid) {
        return BookTitle.values()[bookid];
    }

    /**
     * Creates a new Book entity
     *
     * @param bookid enum BookTitle for the new book
     */
    private void createBook(int bookid, float x, float y) {
        Entity e = new Entity(); // OR engine.creatEntity(); not sure which is better

        BookComponent book = engine.createComponent(BookComponent.class);
        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);


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

    /**
     * Creates a new Book entity from (fromX, fromY) aiming for (toX, toY)
     *
     * @param bookid  enum BookTitle for the new book
     * @param fromX  x-value of the starting location for the book
     * @param fromY  y-value of the starting location for the book
     * @param toX x-value for the position the book is aimed
     * @param toY y-value for the position the book is aimed
     */
    private void createBook(int bookid, float fromX, float fromY, float toX, float toY) {
        Entity e = new Entity(); // OR engine.creatEntity(); not sure which is better

        BookComponent book = engine.createComponent(BookComponent.class);
        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        //TODO: add BodyComponent for collisions
        BodyComponent body = new BodyComponent();

        BookTitle title = getBookTitleByID(bookid);

        state.set(BookComponent.STATE_THROWN);

        animation.animations.put(BookComponent.STATE_THROWN, Assets.getBookByName(title.getAssetName()));
        animation.animations.put(BookComponent.STATE_CAUGHT, Assets.getHeldBookByName(title.getAssetName()));
        texture.region = animation.animations.get(state.get()).getKeyFrame(0f);
        if(toX < fromX) {
            //toX
        }
        //animation.animations.put(TreeComponent.STATE_THROWN, Assets.treeNormal);
        //animation.animations.put(TreeComponent.STATE_CAUGHT, Assets.treeLights);

        // TODO use BookComponent.width and BookComponent.height for Bounds -> and BodyComponent
        position.position.set(fromX, fromY, 3.0f);
        position.scale.set(0.5f, 0.5f); // TODO: check if scaling is OK

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(fromX, fromY);
        //bodyDef.position.set((fromX + texture.region.getRegionWidth()/2) / PIXELS_TO_METERS,
        //        (fromY + texture.region.getRegionHeight()/2) / PIXELS_TO_METERS);

        body.body = physicsWorld.createBody(bodyDef);
        //apply impulse

        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(BookComponent.WIDTH / 3, BookComponent.HEIGHT / 3);
        //bodyShape.setAsBox(128 / PIXELS_TO_METERS, 128 / PIXELS_TO_METERS);
        //bodyShape.setAsBox(BookComponent.WIDTH * 2 / PIXELS_TO_METERS,
        //        BookComponent.HEIGHT * 2 / PIXELS_TO_METERS);
        //bodyShape.setRadius(BookComponent.WIDTH / 2 / PIXELS_TO_METERS);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = bodyShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;

        // calculate force needed to push object from (fromX, fromY) to (toX, toY);
        double distance = Math.sqrt( (Math.pow(toX - fromX, 2)) + (Math.pow(toY - fromY, 2)) );
        // distance from (fromX, fromY) to (toX, toY)
        float distX = toX - fromX;
        float distY = toY - fromY;
        // time for the book to travel from point to point
        float time = 1f; // 1 second?
        // Final velocity values
        float velXf = 5f;
        float velYf = 5f;
        // solve for initial velocities: Vi = (d/t * 2) - Vf
        float velXi =  (distX / time) - velXf;
        float velYi = (distY / time) - velYf;
        float mass2 = (BookComponent.WIDTH / 2 /PIXELS_TO_METERS) *
                (BookComponent.HEIGHT /2 /PIXELS_TO_METERS)* fixtureDef.density;
        float mass = 0f;//5f;
        // impulse = mass * velocity [x and y accordingly]
        // Apply the calculated Impulses
        Gdx.app.log("School World", "Applying impulse to new book. From (" + fromX + ", " + fromY +
                ") To (" + toX + ", " + toY + ")");
        //body.body.applyLinearImpulse(mass2 * velXi, mass2 * velYi, fromX, fromY, true);
        // Setting linear veolicity based on velXi and velYi is not centering the book correctly. WIP
        //body.body.setLinearVelocity(velXi,velYi);
        //body.body.setLinearVelocity(velXi, velYi);
        //body.body.applyForceToCenter(0f, -10f, true);

        Vector2 direction = new Vector2(toX, toY);
        direction.sub(body.body.getPosition());
        direction.nor();

        float speed = 10;
        body.body.setLinearVelocity(direction.scl(speed));

        body.body.createFixture(fixtureDef);
        bodyShape.dispose();

        e.add(book);
        e.add(animation);
        e.add(state);
        e.add(texture);
        e.add(position);
        e.add(body);


        engine.addEntity(e);
    }
}
