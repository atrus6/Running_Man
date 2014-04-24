package org.worldsproject.game.runningman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by atrus on 4/23/14.
 */
public class CreditsScreen implements Screen {
    final RunningMan game;

    final String credits0 = "Music\n\nCarmack\n    By:professorlamp\n    http://opengameart.org/content/carmack\n\n";
    final String credits1 = "Space Chase\n    By: Szymon Matuszewski\n    http://opengameart.org/content/szymon-matuszewski-space-chase\n\n";
    final String credits2 = "Last Minute\n    By: HorrorPen\n    http://opengameart.org/content/last-minute\n\n";
    final String credits3 = "Battle Escape\n    By: bf5man\n    http://opengameart.org/content/battle-escape\n";

    private OrthographicCamera camera;

    public CreditsScreen(RunningMan game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.drawMultiLine(game.batch, credits0+credits1+credits2+credits3, 20, 440);
        game.batch.end();

        if(Gdx.input.isTouched()) {
            game.setScreen(new MenuScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
