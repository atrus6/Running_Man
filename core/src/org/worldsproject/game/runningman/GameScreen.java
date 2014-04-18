package org.worldsproject.game.runningman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by atrus on 4/17/14.
 */
public class GameScreen implements Screen {
    final RunningMan game;
    final Texture sprites;
    final OrthographicCamera camera;
    final Random random = new Random();

    private TextureRegion brick;
    private TextureRegion spike;
    private TextureRegion water;
    private TextureRegion standing;
    private TextureRegion running_1;
    private TextureRegion running_2;
    private TextureRegion running_3;

    private LinkedList<Sprite> platform = new LinkedList<Sprite>();

    private Man man;

    private boolean new_row = false;
    private boolean running = false;

    public GameScreen(final RunningMan game) {
        this.game = game;
        sprites = new Texture("data/man.png");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        TextureRegion[][] textures = TextureRegion.split(sprites, 64, 64);

        brick = textures[0][2];
        spike = textures[1][3];
        water = textures[2][1];

        standing = textures[1][2];

        running_1 = textures[1][0];
        running_2 = textures[0][3];
        running_3 = textures[1][1];

        for(int i = 0; i < Gdx.graphics.getWidth()/64 + 2; i++) {
            createNewLine(Gdx.graphics.getWidth() + i * 64);
        }

        man = new Man(standing, running_1, running_2, running_3);
    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        Iterator<Sprite> it = platform.iterator();

        while(it.hasNext()) {
            Sprite s = it.next();
            if(s.getX() < 0) {
                running = true;
            }

            if(s.getX() < -65) {
                it.remove();
                new_row = true;
                continue;
            }

            s.setX(s.getX() - (128 * delta));
        }

        if(new_row) {
            createNewLine(platform.getLast().getX()+64);
        }

        game.batch.begin();

        for(Sprite s : platform) {
            s.draw(game.batch);
        }

        if(running) {
            game.batch.draw(man.getRunning(delta), man.getX(), man.getY());
        } else {
            game.batch.draw(man.getStanding(), platform.getFirst().getX() + man.getY(), man.getY());
        }
        game.batch.end();

        if(Gdx.input.isTouched()) {
            man.jump();
        }
    }

    private void createNewLine(float location) {
        Sprite a = new Sprite(brick);
        a.setX(location);
        a.setY(64 * 5);


        Sprite b = new Sprite(brick);
        b.setX(location);
        b.setY(0);

        platform.add(a);
        platform.add(b);

        new_row = false;
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
