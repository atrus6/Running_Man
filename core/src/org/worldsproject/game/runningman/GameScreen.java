package org.worldsproject.game.runningman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by atrus on 4/17/14.
 */
public class GameScreen implements Screen {

    //Main game class.
    final RunningMan game;

    //Mega texture of all our game sprites.
    final Texture sprites;

    //Viewing mechanism for the game.
    final OrthographicCamera camera;

    //Used for determining the next part of the level.
    final Random random = new Random();

    //Regions for all our graphics.
    private TextureRegion brick;
    private TextureRegion spike;
    private TextureRegion standing;
    private TextureRegion running_1;
    private TextureRegion running_2;
    private TextureRegion running_3;

    //All the blocks that the level is currently made of.
    private LinkedList<Sprite> platform = new LinkedList<Sprite>();

    //All of the spike collision shapes.
    private LinkedList<Rectangle> collisionShapes = new LinkedList<Rectangle>();

    //Our character.
    private Man man;

    //Determines if we need to create a new row.
    private boolean new_row = false;

    //Current running status of character.
    //Used to have the character scroll in.
    private boolean running = false;

    //Keeps track of the total time that's been played.
    private float total_time;

    //Keeps track of when we should start adding obstacles.
    private float start_adding_obstacles = 5f;
    private float time_to_next_obstacle = 2.5f;
    private float obstacle_timer = 0f;

    //Controls the speed of the level
    private float scroll_speed = 128;

    public GameScreen(final RunningMan game) {
        this.game = game;
        sprites = new Texture("data/man.png");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        TextureRegion[][] textures = TextureRegion.split(sprites, 64, 64);

        brick = textures[0][2];
        spike = textures[1][3];

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

            s.setX(s.getX() - (scroll_speed * delta));
        }

        Iterator<Rectangle> it2 = collisionShapes.iterator();

        while(it2.hasNext()) {
            Rectangle r = it2.next();

            if(r.getX() < -65) {
                it2.remove();
                continue;
            }

            if(Intersector.overlaps(r, man.getBounds())) {
                Gdx.app.log("GAME", "Died.");
            }

            r.setX(r.getX() - (scroll_speed * delta));
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

        if(Gdx.input.isTouched() && running) {
            man.jump();
        }

        total_time += delta;
        obstacle_timer += delta;
    }

    private void createNewLine(float location) {
        Sprite ceiling = new Sprite(brick);
        ceiling.setX(location);
        ceiling.setY(64 * 5);


        Sprite floor = new Sprite(brick);
        floor.setX(location);
        floor.setY(0);

        if(total_time > start_adding_obstacles && obstacle_timer > time_to_next_obstacle) {
            obstacle_timer = 0f;

            //Four types of obstacles
            // 0: Spike on floor.
            // 1: Spike on ceiling.
            // 2: Spike on ceiling and spike on floor.
            int type_of_obstacle = random.nextInt(3);

            switch (type_of_obstacle) {
                case 0:
                    Sprite s0 = new Sprite(spike);
                    s0.setX(location);
                    s0.setY(64*1);
                    platform.add(s0);
                    collisionShapes.add(new Rectangle(location+10, 64, 44, 64));
                    break;
                case 1:
                    Sprite s1 = new Sprite(spike);
                    s1.flip(false, true);
                    s1.setX(location);
                    s1.setY(64*4);
                    platform.add(s1);
                    collisionShapes.add(new Rectangle(location+10, 64*4, 44, 64));
                    break;
                case 2:
                    Sprite s2a = new Sprite(spike);
                    s2a.flip(false, true);
                    s2a.setX(location);
                    s2a.setY(64*4);
                    platform.add(s2a);
                    collisionShapes.add(new Rectangle(location+10, 64*4, 44, 64));

                    Sprite s2b = new Sprite(spike);
                    s2b.setX(location);
                    s2b.setY(64*1);
                    platform.add(s2b);
                    collisionShapes.add(new Rectangle(location+10, 64, 44, 64));
                    break;
            }
        }

        platform.add(ceiling);
        platform.add(floor);
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
