package org.worldsproject.game.runningman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class RunningMan extends Game {
	public SpriteBatch batch;
    public BitmapFont font;

    private Music track1;
    private Music track2;
    private Music track3;
    private Music track4;

    private Random random = new Random();

    private Music current_track;

	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        font = new BitmapFont();

        track1 = Gdx.audio.newMusic(Gdx.files.internal("data/music/battle_escape.mp3"));
        track2 = Gdx.audio.newMusic(Gdx.files.internal("data/music/carmack.mp3"));
        track3 = Gdx.audio.newMusic(Gdx.files.internal("data/music/last_minute.mp3"));
        track4 = Gdx.audio.newMusic(Gdx.files.internal("data/music/space_chase.mp3"));

        select_music();

		this.setScreen(new MenuScreen(this));
	}

    @Override
    public void render() {
        super.render();

        if(current_track.isPlaying() == false) {
            select_music();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    private void select_music() {
        int which = random.nextInt(4);

        switch (which) {
            case 0: current_track = track1; break;
            case 1: current_track = track2; break;
            case 2: current_track = track3; break;
            case 3: current_track = track4; break;
        }

        current_track.play();
    }
}
