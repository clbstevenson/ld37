package com.exovum.ld37warmup.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.exovum.ld37warmup.components.FontComponent;
import com.exovum.ld37warmup.components.TextureComponent;
import com.exovum.ld37warmup.components.TransformComponent;

import java.util.Comparator;

public class FontSystem extends SortedIteratingSystem {

    static final float PPM = 16.0f;
    static final float FRUSTUM_WIDTH = 40f;//Gdx.graphics.getWidth()/PPM;//37.5f;
    static final float FRUSTUM_HEIGHT = 30f;//Gdx.graphics.getHeight()/PPM;//.0f;

    public static final float PIXELS_TO_METRES = 1.0f / PPM;

    private static Vector2 meterDimensions = new Vector2();
    private static Vector2 pixelDimensions = new Vector2();
    public static Vector2 getScreenSizeInMeters(){
        meterDimensions.set(Gdx.graphics.getWidth()*PIXELS_TO_METRES,
                            Gdx.graphics.getHeight()*PIXELS_TO_METRES);
        return meterDimensions;
    }

    public static Vector2 getScreenSizeInPixesl(){
        pixelDimensions.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        return pixelDimensions;
    }

    public static float PixelsToMeters(float pixelValue){
        return pixelValue * PIXELS_TO_METRES;
    }

    private SpriteBatch batch;
    private Array<Entity> renderQueue;
    private Comparator<Entity> comparator;
    private OrthographicCamera cam;

    private ComponentMapper<TransformComponent> transformM;
    private ComponentMapper<FontComponent> fontM;

    public FontSystem(SpriteBatch batch) {
        super(Family.all(TransformComponent.class, FontComponent.class).get(), new ZComparator());

        transformM = ComponentMapper.getFor(TransformComponent.class);
        fontM = ComponentMapper.getFor(FontComponent.class);

        renderQueue = new Array<>();

        // CONFIRMED: Add a comparator so the queue.sort doesn't crash *crosses fingers*

        comparator = new Comparator<Entity>() {
            @Override
            public int compare(Entity entityA, Entity entityB) {
                return (int)Math.signum(transformM.get(entityB).position.z -
                        transformM.get(entityA).position.z);
            }
        };

        this.batch = batch;

        cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        cam.position.set(FRUSTUM_WIDTH / 2f, FRUSTUM_HEIGHT / 2f, 0);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        renderQueue.sort(comparator);

        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.enableBlending();
        batch.begin();

        for(Entity entity: renderQueue) {
            // Every font will have FontCP and TransformCP
            // FontComponent has a BitmapFont and GlyphLayout
            FontComponent font = fontM.get(entity);
            TransformComponent t = transformM.get(entity);
            if(font == null) {
                continue;
            }
            GlyphLayout glyph = font.glyph;
            if(font.font == null || glyph == null || t.isHidden)
                continue;

            //Gdx.app.log("FontSystem", "Starting fontQueue rendering");

            // center the text around TransformComponent position
            font.font.draw(batch, font.glyph, t.position.x - glyph.width / 2,
                    t.position.y - glyph.height / 2);
        }

        batch.end();
        renderQueue.clear();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
        // remember to add entity to font queue so they can actually render
    }

    public OrthographicCamera getCamera() {
        return cam;
    }
}
