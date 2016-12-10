package com.exovum.ld37warmup.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ArrayMap;

/**
 * Created by exovu_000 on 12/10/2016.
 */
public class FontComponent implements Component {
    // font to use when rendering
    public BitmapFont font = null;
    // text of the font
    public GlyphLayout glyph = null;
    // will also use a TransformComponent for setting position
}
