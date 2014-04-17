package org.worldsproject.game.runningman;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by atrus on 4/17/14.
 */
public class Man {
    TextureRegion standing;
    TextureRegion running_1;
    TextureRegion running_2;
    TextureRegion running_3;

    private float next_change = .1f;
    private float current = 0f;
    private TextureRegion current_frame;

    public Man(TextureRegion man, TextureRegion r1, TextureRegion r2, TextureRegion r3) {
        standing = man;
        running_1 = r1;
        running_2 = r2;
        running_3 = r3;

        current_frame = running_1;
    }

    public TextureRegion getStanding() {
        return standing;
    }

    public TextureRegion getRunning(float delta) {
        current += delta;

        if(current > next_change) {
            nextFrame();
            current = 0;
        }

        return current_frame;
    }

    private void nextFrame() {
        if(current_frame == running_1) {
            current_frame = running_2;
        } else if (current_frame == running_2) {
            current_frame = running_3;
        } else {
            current_frame = running_1;
        }
    }
}
