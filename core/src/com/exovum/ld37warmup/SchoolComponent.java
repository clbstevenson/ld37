package com.exovum.ld37warmup;

import com.badlogic.ashley.core.Component;

/**
 * Created by exovu_000 on 12/9/2016.
 */
public class SchoolComponent implements Component{
    // school is 2x3 generally
    public static final float WIDTH = 2f;
    public static final float HEIGHT = 3f;
    // is there a reason to give the School a State?
    // maybe so it can only use or do certain tasks in certain states
    public static final String STATE_NORMAL = "DEFAULT";
}
