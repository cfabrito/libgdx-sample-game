package com.mygdx.tutorial;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by cfabr on 2/8/2018.
 */

public class TutorialGame extends Game {
    public SpriteBatch batch;
    public BitmapFont font;

    public void create()
    {
        batch = new SpriteBatch();

        //Use LibGDX's default Arial font.
        font = new BitmapFont();
        this.setScreen(new MainMenuScreen(this));
    }

    public void render()
    {
        super.render(); //important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}